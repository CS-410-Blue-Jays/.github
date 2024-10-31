import java.beans.Expression;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class Parser extends Token{

  private final List<Token> tokens;
  private int currentIndex = 0;
    
    public Parser(List<Token> tokens) {
        this.tokens = tokens;
        this.currentIndex = 0;
    }

  


  public  ArrayList<Atom> parse(){
   
    return parseProgram();
  }

     private boolean hasMoreTokens() {
        return currentIndex < tokens.size();
    }

      private Token getCurrentToken() {
        return hasMoreTokens() ? tokens.get(currentIndex) : null;
    }

    private  Token advance(){
    return tokens.get(currentIndex++);
  }


  private  boolean accept(Token.TokenType type, String value){
    

       Token current = getCurrentToken();
        if (current != null && current.getTokenType() == type && current.value.equals(value)) {
            advance();
            return true;
        }
        return false;
  }

  // excpect is basically the same as accept but on fail should crash / throw exception 
  private void expect(Token.TokenType type, String value){
    if (!accept(type, value)) {
            
            throw new RuntimeException();
        }
  }

  private  ArrayList<Atom> parseProgram(){
    ArrayList<Atom> atoms = new ArrayList<>();
        
        while (hasMoreTokens()) {
            atoms.addAll(parseStatement());
        }
        
        return atoms;
  }

  private  ArrayList<Atom> parseStatement(){
    ArrayList<Atom> atoms = new ArrayList<>();
    Token token = getToken();
    if(accept(TokenType.KEYWORD, "if")){
      atoms.addAll(parseIf());
    } else if(accept(TokenType.KEYWORD, "while")){
      atoms.addAll(parseWhile());
    } else if(accept(TokenType.KEYWORD, "for")){
      atoms.addAll(parseFor());
    } else if(accept(TokenType.KEYWORD, "int") || accept(TokenType.KEYWORD, "float")){
      atoms.addAll(parseInitialization());
    }
    return atoms;
  }

  private  ArrayList<Atom> parseExpression(){
    List<Atom> atoms = new ArrayList<>();
        
        atoms.addAll(parseTerm());
        while (isOperator(getCurrentToken())) {
            atoms.add(new Atom(advance()));
            atoms.addAll(parseTerm());
        }
        
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


  // Helper method to peek at the next token without advancing
  private  Token peek(){
    return tokens.get(currentIndex + 1);
  }

  // Method to parse the expression
  private ArrayList<Atom> parseTerm(){
    ArrayList<Atom> atoms = new ArrayList<>();
        
        if (accept(TokenType.OPEN_PARENTHESIS, "(")) {
            atoms.addAll(parseExpression());
            expect(TokenType.CLOSE_PARENTHESIS, ")");
        }
        else {
            atoms.add(new Atom(parseOperand()));
        }
        
        return atoms;
  }

  private List<Atom> parseInitialization() {
        List<Atom> atoms = new ArrayList<>();
        
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

  private List<Atom> parseCondition(){
    List<Atom> atoms = new ArrayList<>();
    atoms.addAll(parseTerm());
    return atoms;
  }

  // changed parseIf a little bit
  private List<Atom> parseIf(){
    ArrayList<Atom> atoms = new ArrayList<>();
    expect(TokenType.KEYWORD, "if");
    expect(TokenType.OPEN_PARENTHESIS, "(");
    atoms.addAll(parseCondition()); // Parse the condition as an expression
    expect(TokenType.CLOSE_PARENTHESIS, ")");
    expect(TokenType.OPEN_BRACKET, "{");
    int openBrackets = 1;
    while(openBrackets != 0){
      Token token = getToken();
      if(token.getTokenType() == TokenType.OPEN_BRACKET){
        openBrackets++;
      } else if(token.getTokenType() == TokenType.CLOSE_BRACKET){
        openBrackets--;
      }
      atoms.addAll(parseTerm());
    }
    if(accept(TokenType.KEYWORD, "else")){
      expect(TokenType.OPEN_BRACKET, "{");
      openBrackets = 1;
      while(openBrackets != 0){
        Token token = getToken();
        if(token.getTokenType() == TokenType.OPEN_BRACKET){
          openBrackets++;
        } else if(token.getTokenType() == TokenType.CLOSE_BRACKET){
          openBrackets--;
        }
        atoms.addAll(parseTerm());
      }
    }



    return atoms;
  }


    // allllll the helper methods lol 
    private static final Set<String> ASSIGNMENT_OPERATORS = Set.of("=", "+=", "-=");
    private static final Set<String> COMPARATORS = Set.of(">", "<", ">=", "<=", "==");
    private static final Set<String> ARITHMETIC_OPERATORS = Set.of("++", "+", "--", "-", "*", "/", "%");
    private static final Set<String> TYPES = Set.of("int", "float");

     private List<Atom> parseUpdate() {
        List<Atom> atoms = new ArrayList<>();
        
        String identifier = parseIdentifier();
        atoms.add(new Atom(identifier));
        
        String operator = getCurrentToken().value;
        if (!operator.equals("++") && !operator.equals("--")) {
            throw new RuntimeException();
        }
        advance();
        atoms.add(new Atom(operator));
        
        return atoms;
    }

    private String parseType() {
        Token token = getCurrentToken();
        if (token != null && token.getTokenType() == TokenType.KEYWORD && TYPES.contains(token.value)) {
            advance();
            return token.value;
        }
        throw new RuntimeException();
    }
    
    private String parseIdentifier() {
        Token token = getCurrentToken();
        if (token != null && token.getTokenType() == TokenType.IDENTIFIER) {
            advance();
            return token.value;
        }
        throw new RuntimeException();
    }
    
    private String parseOperand() {
        Token token = getCurrentToken();
        if (token != null && (token.getTokenType() == TokenType.LITERAL || token.getTokenType() == TokenType.IDENTIFIER)) {
            advance();
            return token.value;
        }
        throw new RuntimeException();
    }
    
    private String parseComparator() {
        Token token = getCurrentToken();
        if (token != null && token.getTokenType() == TokenType.OPERATOR && COMPARATORS.contains(token.value)) {
            advance();
            return token.value;
        }
        throw new RuntimeException();
    }
    
    private String parseAssignmentOperator() {
        Token token = getCurrentToken();
        if (token != null && token.getTokenType() == TokenType.OPERATOR && ASSIGNMENT_OPERATORS.contains(token.value)) {
            advance();
            return token.value;
        }
        throw new RuntimeException();
    }

  private boolean isType(Token token) {
        return token != null && token.getTokenType() == TokenType.KEYWORD && TYPES.contains(token.value);
    }
    
    private boolean isIdentifier(Token token) {
        return token != null && token.getTokenType() == TokenType.IDENTIFIER;
    }
    
    private boolean isOperand(Token token) {
        return token != null && (token.getTokenType() == TokenType.LITERAL || token.getTokenType() == TokenType.IDENTIFIER);
    }
    
    private boolean isComparator(Token token) {
        return token != null && token.getTokenType() == TokenType.OPERATOR && COMPARATORS.contains(token.value);
    }
    
    private boolean isOperator(Token token) {
        return token != null && token.getTokenType() == TokenType.OPERATOR && ARITHMETIC_OPERATORS.contains(token.value);
    }
}


