package com.example.ast;

import com.example.Expr;
import com.example.Token;
import com.example.TokenType;

public class AstPrinter implements Expr.Visitor<String> {
  @Override
  public String visitBinaryExpr(Expr.Binary expr) {
    return parenthesize(expr.operator.lexeme, expr.left, expr.right);
  }

  @Override
  public String visitGroupingExpr(Expr.Grouping expr) {
    return parenthesize("group", expr.expression);
  }

  @Override
  public String visitLiteralExpr(Expr.Literal expr) {
    if (expr.value == null) return "nil";
    return expr.value.toString();
  }

  @Override
  public String visitUnaryExpr(Expr.Unary expr) {
    return parenthesize(expr.operator.lexeme, expr.right);
  }

  private String print(Expr expr) {
    return expr.accept(this);
  }

  private String parenthesize(String name, Expr... exprs) {
    StringBuilder sb = new StringBuilder();
    sb.append("(").append(name);
    for (Expr expr : exprs) {
      sb.append(" ");
      sb.append(expr.accept(this));
    }
    sb.append(")");
    return sb.toString();
  }

  public static void main(String args[]){
    Expr expression = new Expr.Binary(
            new Expr.Unary(
                    new Token(TokenType.MINUS, "-", null, 1),
                    new Expr.Literal(123)),
            new Token(TokenType.STAR, "*", null, 1),
            new Expr.Grouping(
                    new Expr.Literal(45.67)));
    System.out.println(new AstPrinter().print(expression));
  }

}
