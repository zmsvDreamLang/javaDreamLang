package org.dl.vm2.ast;

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
      }if ((valueStr.startsWith("0b") || valueStr.startsWith("0B")) && valueStr.length() == 66) {
        // 处理二进制表示的双精度浮点数
        value = Double.longBitsToDouble(Long.parseLong(valueStr.substring(2), 2));
      }if ((valueStr.startsWith("0o") || valueStr.startsWith("0O")) && valueStr.length() == 22) {
        // 处理八进制表示的双精度浮点数
        value = Double.longBitsToDouble(Long.parseLong(valueStr.substring(2), 8));
      }else if (valueStr.contains(".")) {
        // 处理普通浮点数
        value = Double.parseDouble(valueStr);
      } else {
        // 处理普通整数
        long intValue;
        if (valueStr.startsWith("0x") || valueStr.startsWith("0X")) {
          intValue = Long.parseLong(valueStr.substring(2), 16);
        } else if (valueStr.startsWith("0b") || valueStr.startsWith("0B")) {
          intValue = Long.parseLong(valueStr.substring(2), 2);
        } else if (valueStr.startsWith("0o") || valueStr.startsWith("0O")) {
          intValue = Long.parseLong(valueStr.substring(2), 8);
        } else {
          intValue = Long.parseLong(valueStr);
        }
        value = (double) intValue;
      }
      code = 0;
    } catch (NumberFormatException e) {
      e.printStackTrace();
      code = -1;
    }
  }

  public void visitBinaryExpression(BinaryExpression bexp) {
    bexp.getLeft().accept(this);
    double left = (double) value;
    bexp.getRight().accept(this);
    double right = (double) value;
    switch (bexp.getOp()) {
      case "+":
        value = left + right;
        break;
      case "-":
        value = left - right;
        break;
      case "*":
        value = left * right;
        break;
      case "/":
        value = left / right;
        break;
      case "%":
        value = left % right;
        break;
      case "==":
        value = left == right ? 1.0 : 0.0;
        break;
      case "!=":
        value = left != right ? 1.0 : 0.0;
        break;
      case "<":
        value = left < right ? 1.0 : 0.0;
        break;
      case ">":
        value = left > right ? 1.0 : 0.0;
        break;
      case "<=":
        value = left <= right ? 1.0 : 0.0;
        break;
      case ">=":
        value = left >= right ? 1.0 : 0.0;
        break;
      case "&&":
        value = left != 0.0 && right != 0.0 ? 1.0 : 0.0;
        break;
      case "||":
        value = left != 0.0 || right != 0.0 ? 1.0 : 0.0;
        break;
      default:
        code = -1;
    }
  }
}