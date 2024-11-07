package org.dl.vm2.lexer;

public class Token{
  private TokenType type;
  private String token;
  private int line;
  
  private void init(TokenType type,int line){
    this.type = type;
    this.line = line;
  }
  
  public Token(TokenType type,String token,int line){
    init(type,line);
    this.token = token;
  }
  
  public Token(TokenType type,char token,int line){
    init(type,line);
    this.token = String.valueOf(token);
  }
  
  public TokenType getType(){
    return type;
  }
  
  public String getToken(){
    return token;
  }
  
  public int getLine(){
    return line;
  }
  
  @Override
  public String toString(){
    String str = token.replaceAll("\f","\\\\f")
                      .replaceAll("\t","\\\\t")
                      .replaceAll("\n","\\\\n")
                      .replaceAll("\r","\\\\r")
                      .replaceAll("\b","\\\\b");
    return "Token{type: " + type + ",token: \"" + str +"\",line: " + line + "}";
  }
}