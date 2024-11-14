public class Token {

  public static enum TokenType {
    KEYWORD, OPERATOR, IDENTIFIER, LITERAL, OPEN_PARENTHESIS, CLOSE_PARENTHESIS, 
OPEN_BRACKET, CLOSE_BRACKET, OPEN_SQ_BRACKET, CLOSE_SQ_BRACKET, SEMICOLON, 
COMMA, ESCAPE, COLON, PROCESSOR, COMMENT, CHAR, 
STRING, ESCAPE_SEQUENCE, MULTILINE_COMMENT;
}
  TokenType type;
  String value;

  public Token(TokenType type, String value) {
      this.type = type;
      this.value = value;
  }
  public Token(){
    this.type = null;
    this.value = null;
  }
  public TokenType getTokenType(){
    return type;
  }

  @Override
  public String toString() {
      return "Token[type='" + type + "', value='" + value + "']";
  }
}
