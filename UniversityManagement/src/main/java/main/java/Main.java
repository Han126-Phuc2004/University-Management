package main.java;

import controller.MenuController;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

public class Main {
    public static void main(String[] args) {
        try {
            PrintStream utf8Out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
            PrintStream utf8Err = new PrintStream(System.err, true, StandardCharsets.UTF_8);
            System.setOut(utf8Out);
            System.setErr(utf8Err);

            MenuController menuController = new MenuController();
            menuController.start();

        } catch (Exception e) {
            System.err.println("Lỗi khởi động ứng dụng: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
