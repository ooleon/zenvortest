package org.example;

public class Main {
    public static void main(String[] args) {
        String myStr = "This is is isisis is   W3Schools is isis is  ";
        String regex = "is";
//        String s2 = myStr.replaceFirst(regex, "at");
        String s2 = myStr.replace(regex, "at");
        System.out.println(s2);
        System.out.println(myStr);
        System.out.println("paso");


    }
}