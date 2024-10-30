import java.util.ArrayList;
import java.util.List;

public class Parser extends Token{
  public static void main(String[] args) {
    ArrayList<Token> tokens = new ArrayList<>();
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

  public static ArrayList<Atom> parse(ArrayList<Token> tokens){
    ArrayList<Atom> atoms = new ArrayList<>();
    return parseProgram(tokens, atoms);
  }

  // Helper method to accept a token, if present, advance to next token
  private static int accept(Token.TokenType type, String value){
    if(getToken().getTokenType() == type && getToken().value.equals(value)){
      advance();
      return 1;
    } else
      return 0;
  }

  // Helper method to assert a token (if it is not present, crash the program)
  private void expect(Token.TokenType type, String value){
    if(accept(type, value) == 0)
      throw new RuntimeException("Unexpected token: " + getToken().getTokenType() + " with value: " + getToken().value);
  }

  private static ArrayList<Atom> parseProgram(ArrayList<Token> tokens,ArrayList<Atom> atoms){
    parseStatement(tokens, atoms);
    if(peek(tokens) != null)
      parseProgram(tokens, atoms);
    return atoms;
  }

  private static ArrayList<Atom> parseStatement(ArrayList<Token> tokens, ArrayList<Atom> atoms){
    if(accept(TokenType.KEYWORD, "if") == 1)
      atoms.addAll(parseIf());
    else if(accept(TokenType.KEYWORD, "while") == 1)
      atoms.addAll(parseWhile());
    else if(accept(TokenType.KEYWORD, "for") == 1)
      atoms.addAll(parseFor());
    else if(accept(TokenType.KEYWORD, "int") == 1 || accept(TokenType.KEYWORD, "float") == 1)
      atoms.addAll(parseDeclaration());
    return atoms;
  }

  // Helper method to get current token
  private Token getToken(){
  // If current index is less than size of token list, return current token at index
    if(currentIndex < tokens.size()){
      return tokens.get(currentIndex);
    }
    return null;
  }

  // Helper method to move to the next token 
  private static Token advance(){
    return tokens.get(currentIndex++);
  }

  // Helper method to peek at the next token without advancing
  private static Token peek(ArrayList<Token> tokens){
    return tokens.get(currentIndex + 1);
  }

  // Method to parse the expression
  private ArrayList<Atom> parseExpression(ArrayList<Token> tokens, ArrayList<Atom> atoms){
    while(tokens != null)
      
    return atoms;
  }

  private List<Atom> parseCondition(){
    List<Atom> atoms = new ArrayList<>();
    atoms.add(parseExpression());
    return atoms;
  }

  private ArrayList<Atom> parseIf(ArrayList<Token> tokens, ArrayList<Atom> atoms){
    expect(TokenType.KEYWORD, "if");
    expect(TokenType.OPEN_PARENTHESIS, "(");
    atoms.addAll(parseCondition()); // Parse the condition as an expression
    expect(TokenType.CLOSE_PARENTHESIS, ")");
    parseBrackets(tokens, atoms);
    if(accept(TokenType.KEYWORD, "else") == 1){
      if(accept(TokenType.KEYWORD, "if") == 1){
        atoms.addAll(parseIf(tokens, atoms));
      } else {
        parseBrackets(tokens, atoms);
      }
      parseBrackets(tokens, atoms);
    }
    return atoms;
  }
}

  private ArrayList<Atom> parseWhile(ArrayList<Token> tokens, ArrayList<Atom> atoms){
    return atoms;
  }

  private static void parseBrackets(ArrayList<Token> tokens, ArrayList<Atom> atoms){
    expect(Token.TokenType.OPEN_BRACKET, "{");
    int openBrackets = 1;
    while(openBrackets != 0){
      Token token = getToken();
      if(token.getTokenType() == TokenType.OPEN_BRACKET){
        parseProgram(tokens, atoms);
        openBrackets++;
      } else if(token.getTokenType() == TokenType.CLOSE_BRACKET){
        openBrackets--;
      }
      parseTerm();
    }
  }
