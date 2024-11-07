package org.dl.vm2;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.dl.vm2.ast.BinaryExpression;
import org.dl.vm2.ast.NumberExpression;
import org.dl.vm2.ast.Visitor;

public class Main{
  public static void main(String[] args){
    Visitor visit = new Visitor();
    String value = "";
    try {
      value = new String(Files.readAllBytes(Paths.get("./dlsrc/main.zv")), StandardCharsets.UTF_8);
    } catch (IOException e) {
      e.printStackTrace();
    }
    BinaryExpression bexp = new BinaryExpression(new NumberExpression(value), new NumberExpression("0o101"), "+");
    bexp.accept(visit);
    System.out.println(visit.getValue());
    System.out.println(visit.getCode());
  }
}