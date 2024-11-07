package org.dl.vm2.ast;

import org.dl.vm2.ast.Node;

public class NumberExpression implements Node{
  private String val;
  
  public NumberExpression(String val){
    this.val = val;
  }
  
  public String getValue(){
    return val;
  }
  
  public void accept(Visitor visitor){
    visitor.visitNumberExpression(this);
  }
}