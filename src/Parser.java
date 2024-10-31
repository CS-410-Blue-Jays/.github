import java.util.ArrayList;
import java.util.Set;

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

  // Helper method to check if there are more tokens to parse ~ Creek
  private static boolean hasMoreTokens(){
    return currentIndex < tokens.size();
  } 

  // Helper method to get current token ~ Creek
  private static Token getCurrentToken(){
    return hasMoreTokens() ? tokens.get(currentIndex) : null;
  }

  // Helper method to accept a token with it's value and advance to next token
  private static boolean accept(Token.TokenType type, String value){
    if(getCurrentToken().type == type && getCurrentToken().value.equals(value)){
      advance();
      return true;
    } else
      return false;
  }

  // Helper method to accept a token without it's value and advance to next token
  private static boolean accept(Token.TokenType type){
    if(getCurrentToken().type == type){
      advance();
      return true;
    } else
      return false;
  }

  // Helper method to assert a token with a specific value
  private static void expect(Token.TokenType type, String value){
    if(accept(type, value))
      throw new RuntimeException("Unexpected token: " + getCurrentToken().getTokenType() + " with value: " + getCurrentToken().value);
  }
 
  // Helper method to assert a token without it's value
  private static void expect(Token.TokenType type){
    if(accept(type))
      throw new RuntimeException("Unexpected token: " + getCurrentToken().getTokenType() + " with value: " + getCurrentToken().value);
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
      Token token = getCurrentToken();
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
      if(accept(TokenType.OPEN_PARENTHESIS)){
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
            parseUpdate();
            break;
          case "update":
            parseUpdate();
            break;
        }
      } else if(accept(TokenType.CLOSE_PARENTHESIS))
        openParen--;
    }
  }

// Sets of available Keywords for types and operators / comparators ~ Creek
private static final Set<String> ASSIGNMENT_OPERATORS = Set.of("=", "+=", "-=");
private static final Set<String> COMPARATORS = Set.of(">", "<", ">=", "<=", "==");
private static final Set<String> ARITHMETIC_OPERATORS = Set.of("++", "+", "--", "-", "*", "/", "%");
private static final Set<String> TYPES = Set.of("int", "float");

// Helper method to check if it is a type ~ Creek
private boolean isType(Token token) {
  return token != null && token.getTokenType() == TokenType.KEYWORD && TYPES.contains(token.value);
}

// Helper method to check if it is an identifier ~ Creek
private boolean isIdentifier(Token token) {
  return token != null && token.getTokenType() == TokenType.IDENTIFIER;
}

// Helper method to check if it is an operand ~ Creek
private boolean isOperand(Token token) {
  return token != null && (token.getTokenType() == TokenType.LITERAL || token.getTokenType() == TokenType.IDENTIFIER);
}

// Helper method to check if it is a comparator ~ Creek
private boolean isComparator(Token token) {
  return token != null && token.getTokenType() == TokenType.OPERATOR && COMPARATORS.contains(token.value);
}

// Helper method to check if it is an operator ~ Creek
private boolean isOperator(Token token) {
  return token != null && token.getTokenType() == TokenType.OPERATOR && ARITHMETIC_OPERATORS.contains(token.value);
}

  /*
   * The following methods are used to recursively parse the given tokens; starting with the 
   * highest abstract level: parseProgram() until the code is fully translated into simple atoms.
   */

  // Parse program is the highest level of abstraction, it will parse the entire program ~ Brandon
  private static void parseProgram(){
    parseStatement();
    if(peek() != null)
      parseProgram();
  }

  // Parse statement is the second highest level of abstraction, it will parse a single statement ~ Brandon
  private static void parseStatement(){
    if(accept(TokenType.KEYWORD, "if"))
      atoms.addAll(parseIf());
    else if(accept(TokenType.KEYWORD, "while"))
      atoms.addAll(parseWhile());
    else if(accept(TokenType.KEYWORD, "for"))
      atoms.addAll(parseFor());
    else if(peek().getTokenType() == TokenType.KEYWORD && 
    (peek().value.equals("int") || peek().value.equals("float")))
      atoms.addAll(parseInitialization());
    else if(peek().getTokenType() == TokenType.LITERAL || peek().getTokenType() == TokenType.IDENTIFIER)
      atoms.addAll(parseExpression());
  }

  // Method to parse if statements ~ Brandon
  private static ArrayList<Atom> parseIf(){
    expect(TokenType.KEYWORD, "if");
    parseParenthesis("condition");
    parseBrackets();
    if(accept(TokenType.KEYWORD, "else")){
      if(accept(TokenType.KEYWORD, "if")){
        atoms.addAll(parseIf());
      } else {
        parseBrackets();
      }
      parseBrackets();
    }
    return atoms;
  }

  // Method to parse while statements ~ Brandon
  private static ArrayList<Atom> parseWhile(){
    expect(TokenType.KEYWORD, "while");
    parseParenthesis("condition");
    parseBrackets();
    return atoms;
  }

  // Method to parse for statements ~ Brandon
  private static ArrayList<Atom> parseFor(){
    expect(TokenType.KEYWORD, "for");
    parseParenthesis("for");
    parseBrackets();
    return atoms;
  }

  // Method to parse expressions ~ Creek
  private static ArrayList<Atom> parseExpression(){
    atoms.addAll(parseOperand());
    if(peek().getTokenType() == TokenType.OPERATOR && peek().value.equals("=")){
      parseAssignment();
    } else if(peek().getTokenType() == TokenType.OPERATOR && (peek().value.equals("++") || peek().value.equals("--"))){
      parseUpdate();
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

  // Method to parse initializations ~ Creek
  private static ArrayList<Atom> parseInitialization(){
    String type = parseType();
    atoms.add(new Atom(type));

    String identifier = parseIdentifier();
    atoms.add(new Atom(identifier));

    expect(TokenType.OPERATOR, "=");

    if (isOperand(getCurrentToken())) {
        atoms.add(new Atom(parseOperand()));
    }
    else {
        atoms.addAll(parseExpression());
    }

    expect(TokenType.OPERATOR, ";");

    return atoms;
  }

  // Method to parse conditional statements
  private static ArrayList<Atom> parseCondition(){
    atoms.addAll(parseExpression());
    return atoms;
  }

  // Method to parse comparators
  private static String parseComparator(){
    Token token = getCurrentToken();
    if (token != null && token.getTokenType() == TokenType.OPERATOR && COMPARATORS.contains(token.value)) {
      advance();
      return token.value;
    }
    throw new RuntimeException();
  }

  // Method to parse operators
  private static ArrayList<Atom> parseOperator(){
      
      return atoms;
  }

  // Method to parse operands ~ Creek
  private static String parseOperand(){
    Token token = getCurrentToken();
    if (token != null && (token.getTokenType() == TokenType.LITERAL || token.getTokenType() == TokenType.IDENTIFIER)) {
      advance();
      return token.value;
    }
    throw new RuntimeException();    
  }

  // Method to parse updates (ie. i++, i--) ~ Creek
  private static void parseUpdate(){
    Atom update;

    if(peek().value.equals("++")){
      update = new Atom(Atom.Operation.ADD, getCurrentToken().value, "1", getCurrentToken().value);
    } else
      update = new Atom(Atom.Operation.SUB, getCurrentToken().value, "1", getCurrentToken().value);
    
    advance();
    advance();
    expect(TokenType.OPERATOR, ";");
    atoms.add(update);
  }

  // Method to parse the types of variables in initializations
  private static String parseType(){
    Token token = getCurrentToken();
    if (token != null && token.getTokenType() == TokenType.KEYWORD && TYPES.contains(token.value)) {
        advance();
        return token.value;
    }
    throw new RuntimeException();
  }
}