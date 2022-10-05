package controller;

import model.Calculator;
import model.Translator;

public class Tester {
    public static String testString1 = "(A+B)*(C+D)-E";
    public static String testResult1 = "AB+CD+*E-";
    public static String testString2 = "A+B-C";
    public static String testResult2 = "AB+C-";
    public static String testString3 = "A+B*C";
    public static String testResult3 = "ABC*+";
    public static String testString4 = "A+B-C+D";
    public static String testResult4 = "AB+C-D+";

    public static String ts5 = "15.2+2.8*3.4";

    public static void test(Translator calculator, String expression, String result) {
        System.out.println(expression);
//        String res = calculator.translate(expression);
//        System.out.println(res);
//        System.out.println(res.equals(result));
    }

    public static void testCalc(Translator trans, Calculator calc, String expression, String result) {
        calc.calculateExpression(trans.translate(expression));
//        System.out.println(trans.translate(expression));
    }
}

