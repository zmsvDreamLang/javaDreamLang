package org.dl.vm2.ast;


public class BinaryExpression implements Node{
  private Node left;
  private Node right;
  private String op;
  
  public BinaryExpression(Node left, Node right, String op){
    this.left = left;
    this.right = right;
    this.op = op;
  }
  
  public Node getLeft(){
    return left;
  }
  
  public Node getRight(){
    return right;
  }
  
  public String getOp(){
    return op;
  }

  public String toString(){
    return left.toString() + " " + op + " " + right.toString();
  }
  
  public void accept(Visitor visitor){
    visitor.visitBinaryExpression(this);
  }
}
