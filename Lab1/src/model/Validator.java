package model;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public  abstract class Validator {
    private static List<String>  operations = Arrays.asList(".", "*", "/", "+", "-", "^");
    public static String processExpression(String expression) {
        expression = expression.replaceAll(" ","");
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
            throw new RuntimeException("Чыо на ноль делеш а?");
        }
    }

    private static void validateNumbers(String expression) throws RuntimeException {
        if (expression.matches(".*\\d*\\.\\d*\\..*")) {
            throw new RuntimeException("Что-то больна, может хватит?(((");
        }
    }

    private static void validateBrackets(String expression) {
        if ( countCharOccurance(expression,'(') != countCharOccurance(expression,')'))
            throw new RuntimeException("Ты скопки ставеть не умеиш штоли?");
    }

    private static void validateOperations(String expression) {
        for (String oper: operations) {
            validateOperation(oper, expression);
        }
    }

    private static void validateOperation(String oper, String expression) {
        if (expression.contains(oper + oper)) {
            throw new RuntimeException("Чота ты накасячел ");
        }
    }
    private static void validateLog(String expression) {
        if (expression.contains("l") && !expression.matches(".*l\\([^|.]+\\|[^|.]+\\).*")) {
            throw new RuntimeException("Ошибка аргументов логарифма");
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