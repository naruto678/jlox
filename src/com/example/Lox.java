package com.example;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class Lox {

    static boolean hadError = false;

    public static void main(String[] args) throws IOException {
        if (args.length < 1) {
            System.out.println("Usage : jlox [script]");
            System.exit(64);
        } else if (args.length == 1) {
            runFile(args[0]);
            if (hadError) System.exit(65);
        } else {
            runPrompt();
        }
    }

    public static void runFile(String path) throws IOException {
        byte[] bytes = Files.readAllBytes(Paths.get(path));
        run(new String(bytes, Charset.defaultCharset()));
    }

    public static void runPrompt() {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
            for (; ; ) {
                System.out.print("> ");
                String line = reader.readLine();
                if (line == null)
                    break;
                run(line);
                hadError = false;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void run(String source) {
        Scanner scanner = new Scanner(source);
        List<Token> tokens = scanner.scanTokens();
        for (Token token : tokens) {
            System.out.println(token);
        }
    }

    static void error(int line, String message) {
        report(line, "", message);
    }

    static void report(int line, String where, String message) {
        System.err.println(String.format("[line %d] Error : %s %s", line, message));
        hadError = true;
    }

}
