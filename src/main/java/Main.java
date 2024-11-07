package org.dl.vm2;

import org.dl.vm2.ast.Visitor;
import org.dl.vm2.ast.NumberExpression;

public class Main{
  public static void main(String[] args){
    Visitor visit = new Visitor();
    String value = "0x01";
    NumberExpression nexp = new NumberExpression(value);
    nexp.accept(visit);
    if(visit.getCode() != -1){
      System.out.println("successful!\nValue is "+ value + "\nThe result is " + (double)visit.getValue());
    }else{
      System.out.println("fail!");
    }
  }
}