package org.dl.vm2.lexer;

public class LexerException extends Exception{
  private int line;
  
  public LexerException(){
    super();
  }
  
  public LexerException(String message){
    super(message);
  }
  
  public LexerException(String message,int line){
    super(String.format("line:%d %s",line,message));
    this.line = line;
  }
  
  public LexerException(String message,Throwable cause,int line){
    super(String.format("line:%d %s",line,message),cause);
    this.line = line;
  }
  
  public LexerException(Throwable cause){
    super(cause);
  }
  
  public int getLine(){
    return line;
  }
}