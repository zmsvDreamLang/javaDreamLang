package org.dl.vm2.ast;

import org.dl.vm2.ast.NumberExpression;

public class Visitor {
  private Object value;
  private int code;

  public Visitor() {
    value = null;
    code = 0;
  }

  public Object getValue() {
    return value;
  }

  public int getCode() {
    return code;
  }

  public void visitNumberExpression(NumberExpression nexp) {
    try {
      String valueStr = nexp.getValue();
      if ((valueStr.startsWith("0x") || valueStr.startsWith("0X")) && valueStr.length() == 18) {
        // 处理十六进制表示的双精度浮点数
        value = Double.longBitsToDouble(Long.parseLong(valueStr.substring(2), 16));
      } else {
        // 处理普通整数
        long intValue = Long.parseLong(valueStr.startsWith("0x") || valueStr.startsWith("0X") ? valueStr.substring(2) : valueStr, valueStr.startsWith("0x") || valueStr.startsWith("0X") ? 16 : 10);
        value = (double) intValue;
      }
      code = 0;
    } catch (NumberFormatException e) {
      e.printStackTrace();
      code = -1;
    }
  }
}