import java.util.ArrayList;

public class Parser extends Token{
  public static void main(String[] args) {
    tokens.add(new Token(TokenType.KEYWORD, "if"));
    tokens.add(new Token(TokenType.OPEN_PARENTHESIS, "("));
    tokens.add(new Token(TokenType.LITERAL, "1"));
    tokens.add(new Token(TokenType.OPERATOR, "<"));
    tokens.add(new Token(TokenType.LITERAL, "2"));
    tokens.add(new Token(TokenType.CLOSE_PARENTHESIS, ")"));
    tokens.add(new Token(TokenType.OPEN_BRACKET, "{"));
    tokens.add(new Token(TokenType.LITERAL, "3"));
    tokens.add(new Token(TokenType.OPERATOR, "+"));
    tokens.add(new Token(TokenType.LITERAL, "4"));
    tokens.add(new Token(TokenType.CLOSE_BRACKET, "}"));
    parse(tokens);
  }
  
  private static int currentIndex = 0;
  private static int openBrackets = 0;
  private static int openParen = 0;
  private static final ArrayList<Atom> atoms = new ArrayList<>();
  private static ArrayList<Token> tokens = new ArrayList<>();

  public static ArrayList<Atom> parse(ArrayList<Token> insertedTokens){
    tokens = insertedTokens;
    parseProgram();
    return atoms;
  }

  // Helper method to accept a token with it's value and advance to next token
  private static int accept(Token.TokenType type, String value){
    if(getToken().type == type && getToken().value.equals(value)){
      advance();
      return 1;
    } else
      return 0;
  }

  // Helper method to accept a token without it's value and advance to next token
  private static int accept(Token.TokenType type){
    if(getToken().type == type){
      advance();
      return 1;
    } else
      return 0;
  }

  // Helper method to assert a token with a specific value
  private static void expect(Token.TokenType type, String value){
    if(accept(type, value) == 0)
      throw new RuntimeException("Unexpected token: " + getToken().getTokenType() + " with value: " + getToken().value);
  }
 
  // Helper method to assert a token without it's value
  private static void expect(Token.TokenType type){
    if(accept(type) == 0)
      throw new RuntimeException("Unexpected token: " + getToken().getTokenType() + " with value: " + getToken().value);
  }
  
  // Helper method to get current token
  private static Token getToken(){
  // If current index is less than size of token list, return current token at index
    if(currentIndex < tokens.size())
      return tokens.get(currentIndex);
    return null;
  }

  // Helper method to move to the next token 
  private static Token advance(){
    return tokens.get(currentIndex++);
  }

  // Helper method to peek at the next token without advancing
  private static Token peek(){
    return tokens.get(currentIndex + 1);
  }

  // Helper method to parse brackets, keeping track of open brackets vs closed brackets
  private static void parseBrackets(){
    expect(TokenType.OPEN_BRACKET, "{");
    openBrackets++;
    while(openBrackets != 0){
      Token token = getToken();
      if(token.getTokenType() == TokenType.OPEN_BRACKET){
        parseProgram();
        openBrackets++;
      } else if(token.getTokenType() == TokenType.CLOSE_BRACKET)
        openBrackets--;
    }
  }

  // Helper method for parsing parenthesis for conditionals/expressions
  private static void parseParenthesis(String type){
    expect(TokenType.OPEN_PARENTHESIS);
    openParen++;
    while(openBrackets != 0){
      if(accept(TokenType.OPEN_PARENTHESIS) == 1){
        openParen++;
        switch(type){
          case "condition":
            atoms.addAll(parseCondition());
            break;
          case "expression":
            atoms.addAll(parseExpression());
            break;
          case "for":
            atoms.addAll(parseInitialization());
            expect(TokenType.SEMICOLON, ";");
            atoms.addAll(parseCondition());
            expect(TokenType.SEMICOLON, ";");
            atoms.addAll(parseUpdate());
            break;
          case "update":
            atoms.addAll(parseUpdate());
            break;
        }
      } else if(accept(TokenType.CLOSE_PARENTHESIS) == 1)
        openParen--;
    }
  }

  /*
   * The following methods are used to recursively parse the given tokens; starting with the 
   * highest abstract level: parseProgram() until the code is fully translated into simple atoms.
   */

  // Parse program is the highest level of abstraction, it will parse the entire program
  private static void parseProgram(){
    parseStatement();
    if(peek() != null)
      parseProgram();
  }

  // Parse statement is the second highest level of abstraction, it will parse a single statement
  private static void parseStatement(){
    if(accept(TokenType.KEYWORD, "if") == 1)
      atoms.addAll(parseIf());
    else if(accept(TokenType.KEYWORD, "while") == 1)
      atoms.addAll(parseWhile());
    else if(accept(TokenType.KEYWORD, "for") == 1)
      atoms.addAll(parseFor());
    // Below are experimental...
    else if(peek().getTokenType() == TokenType.KEYWORD && 
    (peek().value.equals("int") || peek().value.equals("float")))
      atoms.addAll(parseInitialization());
    else if(peek().getTokenType() == TokenType.LITERAL || peek().getTokenType() == TokenType.IDENTIFIER)
      atoms.addAll(parseExpression());
  }

  // Method to parse if statements
  private static ArrayList<Atom> parseIf(){
    expect(TokenType.KEYWORD, "if");
    parseParenthesis("condition");
    parseBrackets();
    if(accept(TokenType.KEYWORD, "else") == 1){
      if(accept(TokenType.KEYWORD, "if") == 1){
        atoms.addAll(parseIf());
      } else {
        parseBrackets();
      }
      parseBrackets();
    }
    return atoms;
  }

  // Method to parse while statements
  private static ArrayList<Atom> parseWhile(){
    expect(TokenType.KEYWORD, "while");
    parseParenthesis("condition");
    parseBrackets();
    return atoms;
  }

  // Method to parse for statements
  private static ArrayList<Atom> parseFor(){
    expect(TokenType.KEYWORD, "for");
    parseParenthesis("for");
    parseBrackets();
    return atoms;
  }

  // Method to parse expressions
  private static ArrayList<Atom> parseExpression(){
    atoms.addAll(parseOperand());
    if(peek().getTokenType() == TokenType.OPERATOR && peek().value.equals("=")){
      atoms.addAll(parseAssignment());
    } else if(peek().getTokenType() == TokenType.OPERATOR && (peek().value.equals("++") || peek().value.equals("--"))){
      atoms.addAll(parseUpdate());
    } else {
      atoms.addAll(parseOperator());
      if(peek().getTokenType() == TokenType.OPEN_PARENTHESIS){
        parseParenthesis("expression");
      } else
        atoms.addAll(parseOperand());
    }
    return atoms;
  }

  // Method to parse assignments
  private static ArrayList<Atom> parseAssignment(){
    expect(TokenType.IDENTIFIER);
    parseOperator();
    return atoms;
  }

  // Method to parse initializations
  private static ArrayList<Atom> parseInitialization(){
    parseType();

    return atoms;
  }

  // Method to parse conditional statements
  private static ArrayList<Atom> parseCondition(){
    
    return atoms;
  }

  // Method to parse comparators
  private static ArrayList<Atom> parseComparator(){

    return atoms;
  }

  // Method to parse operators
  private static ArrayList<Atom> parseOperator(){
      
      return atoms;
  }

  // Method to parse operands
  private static ArrayList<Atom> parseOperand(){
        
        return atoms;
  }

  // Method to parse updates (ie. i++, i--)
  private static ArrayList<Atom> parseUpdate(){
      
      return atoms;
  }

  // Method to parse the types of variables in initializations
  private static ArrayList<Atom> parseType(){
    if(accept(TokenType.KEYWORD, "int") == 1){}
    else expect(TokenType.KEYWORD, "float");    
    return atoms;
  }
}