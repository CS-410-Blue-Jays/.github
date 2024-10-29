import java.beans.Expression;
import java.util.List;

public class Parser extends Token{
  private final List<Token> tokens;
  private int currentIndex = 0;

  // Constructor
  public Parser(List<Token> tokens){
    this.tokens = tokens;
  }

  // Helper method to accept a token, if present, advance to next token
  public int accept(Token.TokenType type){
    if(getToken().getTokenType() == type){
      advance();
      return 1;
    } else
      return 0;
  }

  // Helper method to assert a token (if it is not present, crash the program)
  public void expect(Token.TokenType type){
    if(accept(type) == 0)
      throw new RuntimeException("Unexpected token: " + getToken().getTokenType());
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
  private Token advance(){
    return tokens.get(currentIndex++);
  }

  // Helper method to peek at the next token
  private Token peek(){
    return tokens.get(currentIndex + 1);
  }

  // Method to parse the expression
  public Expression parseTerm(){
    Token token = getToken();
    if(token.getTokenType() == TokenType.LITERAL){

    }
    return null;
  }
}
