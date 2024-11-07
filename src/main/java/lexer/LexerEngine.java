package org.dl.vm2.lexer;

import java.util.ArrayList;
import java.util.List;

/**
 * DL词法分析器
 * @version 1.3.0
 */
public class LexerEngine {

  // 16进制数字解析时可能会有的字符集
  private static boolean[] hexChar = new boolean[128];
  // 特殊字符集
  private static boolean[] specicalChar = new boolean[128];
  // 保留字符集
  private static boolean[] reserChar = new boolean[128];
  // 传入数据的字符数组表示
  private char[] input;
  // 字符索引
  private int index;
  // 行数
  private int line;
  // 前一个TokenType
  private TokenType oldTokenType;

  static {
    for (char i = '0'; i <= '9'; i++) {
      hexChar[i] = true;
    }

    for (char i = 'a'; i <= 'f'; i++) {
      hexChar[i] = true;
    }

    for (char i = 'A'; i <= 'F'; i++) {
      hexChar[i] = true;
    }

    char[] specialChars = {'+', '-', '*', '/', '^', '%', '<', '>', '=', '!', '&', '|', '(', ')', '[', ']', '{', '}', '.', ',', ';', ':', '\n', '\"', '\'', '`'};
    for (char c : specialChars) {
      specicalChar[c] = true;
    }

    char[] reservedChars = {'#', '@', '$'};
    for (char c : reservedChars) {
      reserChar[c] = true;
    }
  }

  /**
   * 类的构造方法
   *
   * @param input 待分析的字符串原文
   */
  public LexerEngine(String input) {
    this.input = input.toCharArray();
    this.index = 0;
    this.line = 1;
  }

  /**
   * 取字符的ASCII码
   *
   * @param c 字符
   * @return 字符的ASCII码
   */
  private static int getByte(char c) {
    byte b = (byte) c;
    if (b > 0) {
      return b;
    }
    return 256 + b;
  }

  /**
   * 取下一个字符
   *
   * @return 下一个字符，若无，则返回null
   */
  private Character nextChar() {
    return index + 1 < input.length ? input[index + 1] : null;
  }

  /**
   * 解析特殊字符(\',\",\f、\t、\n、\r、\b、\\)
   *
   * @param s 待解析的字符串
   * @return 解析完毕的字符串
   */
  private static String replaceEscape(String s) {
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < s.length(); i++) {
      char c = s.charAt(i);
      if (c != '\\' || i + 1 >= s.length()) {
        sb.append(c);
        continue;
      }
      char next = s.charAt(i + 1);
      switch (next) {
        case '\'':
          sb.append('\'');
          break;
        case '\"':
          sb.append('\"');
          break;
        case 'f':
          sb.append('\f');
          break;
        case 't':
          sb.append('\t');
          break;
        case 'n':
          sb.append('\n');
          break;
        case 'r':
          sb.append('\r');
          break;
        case 'b':
          sb.append('\b');
          break;
        case '\\':
          sb.append('\\');
          break;
        default:
          sb.append(c).append(next);
      }
      i++;
    }
    return sb.toString();
  }

  /**
   * 取字符的Token
   *
   * @param c 字符
   * @return 字符的Token实例
   */
  private Token getToken(char c) throws LexerException {
    String s;
    switch (c) {
      case '\n':
        line++;
        index++;
        return new Token(TokenType.LBREAK, "\n", line - 1);
      case '\'':
      case '\"':
      case '`':
        s = checkString(index);
        index += s.length() + 2;
        if (c == '`') {
          return new Token(TokenType.STRINGS, s, line);
        }
        s = replaceEscape(s);
        return new Token(TokenType.STRINGS, s, line);
      case '0':
      case '1':
      case '2':
      case '3':
      case '4':
      case '5':
      case '6':
      case '7':
      case '8':
      case '9':
        s = checkNumber(index);
        index += s.length();
        return new Token(TokenType.NUMBERS, s, line);
      case '+':
        if (nextChar() == '=') {
          index += 2;
          return new Token(TokenType.PLUSEQ, "+=", line);
        }
        index++;
        return new Token(TokenType.PLUS, "+", line);
      case '-':
        if (index + 1 < input.length && Character.isDigit(input[index + 1]) && (oldTokenType != TokenType.NUMBERS || oldTokenType != TokenType.IDENT)) {
          s = checkNumber(index);
          index += s.length();
          return new Token(TokenType.NUMBERS, s, line);
        } else if (nextChar() == '=') {
          index += 2;
          return new Token(TokenType.MINUSEQ, "-=", line);
        }
        index++;
        return new Token(TokenType.MINUS, "-", line);
      case '*':
        index++;
        return new Token(TokenType.MULTI, "*", line);
      case '/':
        char ch = nextChar();
        if (ch == '/' || ch == '*') {
          int start = line;
          s = checkExgesis(index);
          index += s.length();
          return new Token(TokenType.EXGESIS, s, start);
        }
        index++;
        return new Token(TokenType.DASH, "/", line);
      case '^':
        index++;
        return new Token(TokenType.MULTIPLICA, "^", line);
      case '%':
        index++;
        return new Token(TokenType.IMPRESS, "%", line);
      case '=':
        if (nextChar() == '=') {
          index += 2;
          return new Token(TokenType.EQ, "==", line);
        }
        index++;
        return new Token(TokenType.ASSIGN, "=", line);
      case '(':
        index++;
        return new Token(TokenType.LPARN, "(", line);
      case ')':
        index++;
        return new Token(TokenType.RPARN, ")", line);
      case '[':
        index++;
        return new Token(TokenType.LBRACKET, "[", line);
      case ']':
        index++;
        return new Token(TokenType.RBRACKET, "]", line);
      case '{':
        index++;
        return new Token(TokenType.LBRACE, "{", line);
      case '}':
        index++;
        return new Token(TokenType.RBRACE, "}", line);
      case ',':
        index++;
        return new Token(TokenType.COMMA, ",", line);
      case ';':
        index++;
        return new Token(TokenType.SEMICOLON, ";", line);
      case ':':
        index++;
        return new Token(TokenType.COLON, ":", line);
      case '.':
        if (nextChar() == c && input[index + 2] == c) {
          index += 3;
          return new Token(TokenType.VARARGS, "...", line);
        }
        index++;
        return new Token(TokenType.DOT, ".", line);
      case '>':
        if (nextChar() == '=') {
          index += 2;
          return new Token(TokenType.GTE, ">=", line);
        }
        index++;
        return new Token(TokenType.GT, ">", line);
      case '<':
        if (nextChar() == '=') {
          index += 2;
          return new Token(TokenType.LTE, "<=", line);
        }
        index++;
        return new Token(TokenType.LT, "<", line);
      case '!':
        if (nextChar() == '=') {
          index += 2;
          return new Token(TokenType.NOTEQ, "!=", line);
        }
        return new Token(TokenType.NOT, "!", line);
      case '&':
        if (nextChar() == '&') {
          index += 2;
          return new Token(TokenType.AND, "&&", line);
        }
        throw new LexerException("unexpected symbol 38 (&)", line);
      case '|':
        if (nextChar() == '|') {
          index += 2;
          return new Token(TokenType.OR, "||", line);
        }
        throw new LexerException("unexpected symbol 124 (|)", line);
      default:
        return null;
    }
  }

  /**
   * 取关键字的Token实例
   *
   * @param str 关键字
   * @return 关键字的Token实例
   */
  private Token getToken(String str) {
    switch (str) {
      case "string":
        return new Token(TokenType.STRING, str, line);
      case "number":
        return new Token(TokenType.NUMBER, str, line);
      case "boolean":
        return new Token(TokenType.BOOLEAN, str, line);
      case "thread":
        return new Token(TokenType.THREAD, str, line);
      case "class":
        return new Token(TokenType.CLASS, str, line);
      case "object":
        return new Token(TokenType.OBJECT, str, line);
      case "function":
        return new Token(TokenType.FUNCTION, str, line);
      case "true":
        return new Token(TokenType.TRUE, str, line);
      case "false":
        return new Token(TokenType.FALSE, str, line);
      case "null":
        return new Token(TokenType.NULL, str, line);
      case "package":
        return new Token(TokenType.PACKAGE, str, line);
      case "import":
        return new Token(TokenType.IMPORT, str, line);
      case "var":
        return new Token(TokenType.VAR, str, line);
      case "val":
        return new Token(TokenType.VAL, str, line);
      case "func":
        return new Token(TokenType.FUNC, str, line);
      case "if":
        return new Token(TokenType.IF, str, line);
      case "else":
        return new Token(TokenType.ELSE, str, line);
      case "elseif":
        return new Token(TokenType.ELSEIF, str, line);
      case "return":
        return new Token(TokenType.RETURN, str, line);
      case "switch":
        return new Token(TokenType.SWITCH, str, line);
      case "case":
        return new Token(TokenType.CASE, str, line);
      case "for":
        return new Token(TokenType.FOR, str, line);
      case "while":
        return new Token(TokenType.WHILE, str, line);
      case "do":
        return new Token(TokenType.DO, str, line);
      case "break":
        return new Token(TokenType.BREAK, str, line);
      case "continue":
        return new Token(TokenType.CONTINUE, str, line);
      case "new":
        return new Token(TokenType.NEW, str, line);
      case "extends":
        return new Token(TokenType.EXTENDS, str, line);
      case "super":
        return new Token(TokenType.SUPER, str, line);
      case "override":
        return new Token(TokenType.OVERRIDE, str, line);
      case "public":
        return new Token(TokenType.PUBLIC, str, line);
      case "private":
        return new Token(TokenType.PRIVATE, str, line);
      case "static":
        return new Token(TokenType.STATIC, str, line);
      default:
        return new Token(TokenType.IDENT, str, line);
    }
  }

  private boolean containsChar(StringBuilder sb, char ch) {
    for (int i = 0; i < sb.length(); i++) {
      if (sb.charAt(i) == ch) {
        return true;
      }
    }
    return false;
  }

  /**
   * 检查、获取字符串
   *
   * @param i 字符串开始索引
   * @return 解析完成的字符串
   */
  private String checkString(int i) throws LexerException {
    char c = input[i];
    if (c != '\"' && c != '\'' && c != '`') {
      throw new LexerException("incorrect start character", line);
    }
    char start_char = c;
    int start = line;
    StringBuilder sb = new StringBuilder();
    sb.append(c);
    i++;
    while (i < input.length) {
      c = input[i];
      sb.append(c);
      if (c == '\n') {
        line++;
      } else if (c == start_char) {
        break;
      }
      if (c == '\\') {
        if (i + 1 < input.length) {
          sb.append(input[i + 1]);
        }
        i += 2;
      } else {
        i++;
      }
    }
    if (i >= input.length) {
      throw new LexerException("unfinished string", start);
    } else if (containsChar(sb, '\n')) {
      throw new LexerException("unfinished string", start);
    }
    sb.deleteCharAt(0);
    sb.deleteCharAt(sb.length() - 1);
    return sb.toString();
  }

  /**
   * 获取数字
   *
   * @param i 字符数组索引
   * @return 字符串数字
   */
  private String checkNumber(int i) throws LexerException {
    StringBuilder sb = new StringBuilder();
    boolean isHex = false;
    boolean isOct = false;
    boolean isBin = false;
    boolean isE = false;
    int dot = 0;
    char c = input[i];
    if (c == '-') {
      sb.append(c);
      i++;
    } else if (!Character.isDigit(c)) {
      throw new LexerException("not number", line);
    }
    while (i < input.length && !(Character.isWhitespace(input[i]) || specicalChar[input[i]])) {
      c = input[i];
      if (Character.isDigit(c)) {
        sb.append(c);
      } else if (c == '.' && dot == 0) {
        sb.append(c);
        dot++;
      } else if ((c == 'x' || c == 'X') && sb.length() == 1 && sb.charAt(0) == '0') {
        sb.append(c);
        isHex = true;
      } else if ((c == 'b' || c == 'B') && sb.length() == 1 && sb.charAt(0) == '0') {
        sb.append(c);
        isBin = true;
      } else if ((c == 'o' || c == 'O') && sb.length() == 1 && sb.charAt(0) == '0') {
        sb.append(c);
        isOct = true;
      } else if (!isE && (c == 'e' || c == 'E')) {
        sb.append(c);
        isE = true;
      } else if (isHex && hexChar[c]) {
        sb.append(c);
      } else if (isBin && (c == '0' || c == '1')) {
        sb.append(c);
      } else if (isOct && (c >= '0' && c <= '7')) {
        sb.append(c);
      } else {
        throw new LexerException("wrong numbers", line);
      }
      i++;
    }
    return sb.toString();
  }

  /**
   * 取标识符
   * @param i 字符索引
   * @return 截取到的完整标识符
   */
  private String checkIdent(int i) throws LexerException {
    StringBuilder sb = new StringBuilder();
    char c = input[i];
    if (reserChar[c]) {
      throw new LexerException("syntax error", line);
    }
    sb.append(c);
    i++;
    while (i < input.length && !(Character.isWhitespace(input[i]) || specicalChar[input[i]])) {
      c = input[i];
      sb.append(c);
      i++;
    }
    return sb.toString();
  }

  /**
   * 取注释内容
   *
   * @param i 注释开始索引
   * @return 注释内容
   */
  private String checkExgesis(int i) {
    StringBuilder sb = new StringBuilder();
    boolean isMoreEx = false;
    if (input[i + 1] == '*' && input[i] == '/') {
      isMoreEx = true;
      sb.append("/*");
    } else {
      sb.append("//");
    }
    i += 2;
    while (i < input.length) {
      char c = input[i];
      if (c == '\n' && !isMoreEx) {
        break;
      } else if (isMoreEx && i + 1 < input.length && input[i] == '*' && input[i + 1] == '/') {
        sb.append("*/");
        break;
      }
      sb.append(c);
      i++;
    }
    return sb.toString();
  }

  /**
   * 分析词法
   * @return Token的ArrayList
   */
  public List<Token> tokenize() throws LexerException {
    List<Token> tokens = new ArrayList<>();
    while (index < input.length) {
      char c = input[index];
      if (Character.isWhitespace(c) && c != '\n') {
        index++;
        continue;
      } else if (getByte(c) >= 128) {
        int b = getByte(c);
        throw new LexerException(String.format("unexpected symbol %d (%c)", b, c), line);
      }
      Token token = getToken(c);
      if (token == null) {
        String s = checkIdent(index);
        token = getToken(s);
        oldTokenType = token.getType();
        tokens.add(token);
        index += s.length();
        continue;
      }
      oldTokenType = token.getType();
      tokens.add(token);
    }
    return tokens;
  }
}
