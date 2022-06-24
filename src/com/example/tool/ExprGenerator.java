package com.example.tool;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ExprGenerator {
  private static Logger log = Logger.getLogger(ExprGenerator.class.getName());

  public static void main(String[] args) throws IOException {
    if (args.length != 1) {
      log.info("Usage expr_generator <output directory>");
      Path path = Paths.get(".");
      log.info("This is the current path " + path.toAbsolutePath());
      System.exit(64);
    }
    String outputDir = args[0];
    defineAst(
        outputDir,
        "Expr",
        Arrays.asList(
            "Binary : Expr left,Token operator,Expr right",
            "Grouping :Expr expression",
            "Literal :Object value",
            "Unary :Token operator,Expr right"));
  }

  private static void defineVisitor(PrintWriter writer, String baseName, List<String> list){
    writer.println("  public interface Visitor<R> {");
    for(String type:list){
      String typeName = type.split(":")[0].trim();
      writer.println("    R visit" + typeName + baseName + "(" +
              typeName + " " + baseName.toLowerCase() + ");");
    }
    writer.println("  }");
  }

  private static void defineAst(String outputDir, String baseName, List<String> list)
      throws IOException {
    String path = String.join("/", outputDir, baseName + ".java");
    System.out.println(path);
    try (PrintWriter writer = new PrintWriter(path, "UTF-8")) {
      writer.println("package com.example;");
      writer.println();
      writer.println();
      writer.println(String.format("public abstract class %s {", baseName));
      defineVisitor(writer, baseName,list);
      for (String type : list) {
        String className = type.split(":")[0].trim();
        String fields = type.split(":")[1].trim();
        defineType(writer, baseName, className, fields);
      }
      writer.println();
      writer.println("  public abstract <R> R accept(Visitor<R> visitor);");
      writer.println("}");
    } catch (IOException ex) {
      ex.printStackTrace();
    }
  }

  private static void defineType(PrintWriter writer, String baseName, String className, String fields) {

    writer.println(String.format("  public static class %s extends %s {", className, baseName));
    String params[] = Arrays.stream(fields.split(",")).map(String::trim).toArray(String[]::new);
    log.log(Level.INFO, Arrays.toString(params));
    for (String param : params) {
      String paramType = param.split("\\s+")[0];
      String paramName = param.split("\\s+")[1];
      writer.println(String.format("    public final %s %s;", paramType, paramName));
    }
    writer.println();
    writer.println(String.format("    %s(%s) {", className, fields));
    for (String param : params) {
      String paramName = param.split("\\s+")[1];
      writer.println(String.format("     this.%s = %s;", paramName, paramName));
    }
    writer.println("  }");
    writer.println();
    writer.println("    @Override");
    writer.println("    public <R> R accept(Visitor<R> visitor) {");
    writer.println("      return visitor.visit" +
            className + baseName + "(this);");
    writer.println("    }");
    writer.println("}");
  }


}
