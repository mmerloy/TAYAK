package model;

import java.security.InvalidParameterException;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public  abstract class Validator {
    private static List<String>  operations = Arrays.asList(".", "*", "/", "+", "-", "^");
    public static String processExpression(String expression) {
        //Проверка, что первый аргумент долен быть числом
        boolean foundOp = false;
        for (int i = 0; i < expression.length(); i++) {
            if(expression.contains("log") || operations.contains(Character.toString(expression.charAt(i)))){
                foundOp = true;
                break;
            }
        }
        if(!foundOp){
            Double.parseDouble(expression);
        }
        //Простановка первых в строке унарных оперторов как бинарных
        if(expression.charAt(0) =='-' || expression.charAt(0)=='+'){
            expression = "0" + expression;
        }
        expression = expression.replaceAll("\\s+","");
        expression = expression.replaceAll("(\\d)\\(", "$1*(");
        expression = expression.replaceAll("\\(-\\(", "(0-(");
        expression = expression.replaceAll("\\(-(\\d)", "(0-$1");
        expression = expression.replaceAll("(\\))(\\()", "$1*$2");
        expression = changeLog(expression);
        expression = expression.replaceAll(",", ".");
        expression = expression.replaceAll("log", "l");
        validateBrackets(expression);
        validateOperations(expression);
        validateNullDivide(expression);
        if(expression.length() >= 6)
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


    //Первичная обертка над foundInvalidLogArgs
    private static boolean foundInvalidLogArgs(String expr){
        return foundInvalidLogArgs(expr, 1).success;
    }

    //Рекурсивная проверка корректности данных внутри скобок логарифма
    private static FoundInvalidLogArgsResult foundInvalidLogArgs(String expr, int startPos){
        var result = new FoundInvalidLogArgsResult();

        //Начальный указатель должен указывать на откр скобку
        if(expr.charAt(startPos)!='('){
            result.success = false;
            return result;
        }

        //Переменная которая отвечает за неудачный результат в глубине рекурсии
        boolean innerSuccess = true;
        int tokenCnt = 0;
        for (int i = startPos + 1; i < expr.length(); i++) {
            char c = expr.charAt(i);
            //Если внутри аргументов будет логарифм, то спускаемся глубде в рекурсию
            if(c == 'l'){
                var innerRes = foundInvalidLogArgs(expr, i+1);
                //Елсли в глубине будет хотябы один неудачный вариант, то поднимится вверх с false
                innerSuccess &= innerRes.success;
                i = innerRes.lastIndex;
                break;
            }

            //Если просмотрели все аргументы, то должны были найти хотя бы один разделитель
            if(c==')'){
                result.success = tokenCnt == 1;
                result.lastIndex = i;
                return result;
            }

            //Ведем подсчет количества разделителей
            if(c=='|'){
                ++tokenCnt;
                if(tokenCnt > 1){
                    result.success = false;
                    return result;
                }
            }
        }
        //Все глубинные результаты и количество внехних разделителей должны быть в норме
        result.success = innerSuccess && tokenCnt == 1;
        return result;
    }

    private static void validateLog(String expression) {
        boolean ok = foundInvalidLogArgs(expression);

        if (!ok && expression.contains("l") && !expression.matches(".*l\\([^|.]+\\|[^|.]+\\).*")) {
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
