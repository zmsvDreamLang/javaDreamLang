package org.dl.vm2.lexer;

public enum TokenType{
  // 标识符
  IDENT,
  // 注释
  EXGESIS,
  // 字符串
  STRINGS,
  // 数字
  NUMBERS,
  // 文件结束
  EOF,
  // 加号
  PLUS,
  // 减号
  MINUS,
  // 乘号
  MULTI,
  // 除号
  DASH,
  // 乘幂
  MULTIPLICA,
  // 取模
  IMPRESS,
  // 换行
  LBREAK,
  // 赋值
  ASSIGN,
  // 左括号
  LPARN,
  // 右括号
  RPARN,
  // 左中括号
  LBRACKET,
  // 右中括号
  RBRACKET,
  // 左大括号
  LBRACE,
  // 右大括号
  RBRACE,
  // 逗号
  COMMA,
  // 分号
  SEMICOLON,
  // 冒号
  COLON,
  // 点
  DOT,
  // 可变长参数(...)
  VARARGS,
  // 大于
  GT,
  // 大于等于
  GTE,
  // 小于
  LT,
  // 小于等于
  LTE,
  // 等于
  EQ,
  // 加等于
  PLUSEQ,
  // 减等于
  MINUSEQ,
  // 不等于
  NOTEQ,
  // 与(&&)
  AND,
  // 或(||)
  OR,
  // 非(!)
  NOT,
  // string
  STRING,
  // number
  NUMBER,
  // boolean
  BOOLEAN,
  // thread
  THREAD,
  // class
  CLASS,
  // object
  OBJECT,
  // function
  FUNCTION,
  // true
  TRUE,
  // false
  FALSE,
  // null
  NULL,
  // package
  PACKAGE,
  // import
  IMPORT,
  // var
  VAR,
  // val
  VAL,
  // func
  FUNC,
  // if
  IF,
  // else
  ELSE,
  // elseif
  ELSEIF,
  // return
  RETURN,
  // switch
  SWITCH,
  // case
  CASE,
  // for
  FOR,
  // while
  WHILE,
  // continue
  CONTINUE,
  // do
  DO,
  // break
  BREAK,
  // new
  NEW,
  // extends
  EXTENDS,
  // super
  SUPER,
  // override
  OVERRIDE,
  // public
  PUBLIC,
  // private
  PRIVATE,
  // static
  STATIC;
}