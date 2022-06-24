package com.example.ast;

import com.example.Expr;
import com.example.Token;
import com.example.TokenType;

public class RPNPrinter implements Expr.Visitor<String> {

  @Override
  public String visitBinaryExpr(Expr.Binary expr) {
    return parenthesize(expr.left, expr.right)+expr.operator.lexeme;
  }

  @Override
  public String visitGroupingExpr(Expr.Grouping expr) {
    return parenthesize(expr.expression);
  }

  @Override
  public String visitLiteralExpr(Expr.Literal expr) {
    if(expr.value==null) return "nil";
    return expr.value.toString();
  }

  @Override
  public String visitUnaryExpr(Expr.Unary expr) {
    return null;
  }

  public String convertToPolish(Expr expr) {
    return expr.accept(this);
  }

  private String parenthesize(Expr ... exprs){
    StringBuilder sb = new StringBuilder("(");
    for(Expr expr: exprs){
      sb.append(" ");
      sb.append(expr.accept(this));
    }
    sb.append(")");
    return sb.toString();
  }

  public static void main(String args[]) {
    Expr expr =
        new Expr.Binary(
            new Expr.Literal("1234"),
            new Token(TokenType.PLUS, "+", null, 1),
            new Expr.Literal("2343"));
    System.out.println(new RPNPrinter().convertToPolish(expr));
  }
}
