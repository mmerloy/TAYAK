package model;

import java.security.InvalidParameterException;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public  abstract class Validator {
    private static List<String>  operations = Arrays.asList(".", "*", "/", "+", "-", "^");
    public static String processExpression(String expression) {
        //expression = expression.replaceAll("/\s\s+/g", " ");
        expression = expression.replaceAll("\\s+","");
        expression = expression.replaceAll("(\\d)\\(", "$1*(");
        expression = expression.replaceAll("\\(-(\\d)", "(0-$1");
        expression = changeLog(expression);
        expression = expression.replaceAll(",", ".");
        expression = expression.replaceAll("log", "l");
        validateBrackets(expression);
        validateOperations(expression);
        validateNullDivide(expression);
        validateLog(expression);
        validateNumbers(expression);
        return expression;
    }


    private static long countCharOccurance(String expression ,char element) {
        return expression.chars().filter(ch-> ch==element).count();
    }

    private static void validateNullDivide(String expression) throws RuntimeException {
        if (expression.contains("/0")) {
            throw new RuntimeException("Деление на ноль невозможно:(");
        }
    }

    private static void validateNumbers(String expression) throws RuntimeException {
        if (expression.matches(".*\\d*\\.\\d*\\..*")) {
            throw new InvalidParameterException("Может не нужно?");
        }
    }

    private static void validateBrackets(String expression) {
        if ( countCharOccurance(expression,'(') != countCharOccurance(expression,')'))
            throw new InvalidParameterException("Скобки расставлены неверно:(");
    }


    private static void validateOperations(String expression) {
        for (String oper: operations) {
            validateOperation(oper, expression);
        }
    }

    private static void validateOperation(String oper, String expression) {
        if (expression.contains(oper + oper)) {
            throw new RuntimeException("Что-то тут не так ");
        }
    }
    private static void validateLog(String expression) {
        if (expression.contains("l") && !expression.matches(".*l\\([^|.]+\\|[^|.]+\\).*")) {
            throw new InvalidParameterException("Ошибка аргументов логарифма");
        }
    }

    private static String changeLog(String expression) {
        Pattern pattern = Pattern.compile("l(.+,.+)");
        Matcher matcher = pattern.matcher(expression);
        while (matcher.find()) {
            String logExp = matcher.group(0).replace(",","|");
            expression = expression.replaceFirst(pattern.toString(), logExp);
        }

        return expression;
    }
}