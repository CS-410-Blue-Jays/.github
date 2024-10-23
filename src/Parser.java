import java.beans.Expression;
import java.util.ArrayList;
import java.util.List;

public class Parser extends Scanner{
  private List<Scanner.Token> tokens;
  private int currentIndex = 0;
  // Constructor
  public Parser(List<Token> tokens){
    this.tokens = tokens;
  }

  // Helper method to get current token
  private Scanner.Token getToken(){
  // If current index is less than size of token list, return current token at index
    if(currentIndex < tokens.size()){
      return tokens.get(currentIndex);
    }
    return null;
  }
  // Helper method to look ahead at the next token
  private Scanner.Token advance(){
    return tokens.get(currentIndex++);
  }
  public Expression parseTerm(){
    Scanner.Token token = getToken();
    if(token.getTokenType() == TokenType.LITERAL){

    }
    return null;
  }
}
