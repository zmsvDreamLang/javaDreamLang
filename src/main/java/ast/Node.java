package org.dl.vm2.ast;

import org.dl.vm2.ast.Visitor;

public interface Node{
  public void accept(Visitor visitor);
}