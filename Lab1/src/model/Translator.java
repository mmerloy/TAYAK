package model;

import java.util.*;

public class Translator {
    private static final String UPGRADE_REGEX = "\\.\\d+";
    private Map<String, Integer> priorMap;
    private Stack<Operation> operationsStack;
    private StringBuilder result;
    public List<Operation> resultList;

    // Флаг того что предыдущий элемент - операция.
    // Нужен для корректной записи числа
    private boolean isPrevElementOperation;

    public Translator() {
        priorMap = new HashMap<>() {{
            put("(", 1);
            put(")", 1);
            put("|", 1);
            put("+", 2);
            put("-", 2);
            put("*", 3);
            put("/", 3);
            put("^", 4);
            put("log", 4);
        }};
        operationsStack = new Stack<>();
        result = new StringBuilder();
        resultList = new ArrayList<>();
        isPrevElementOperation = true;
    }

    /**
     * Главный метод объекта, осуществляющий перевод выражения в обратную польскую запись (результат)
     * @param expression выражение
     * @return обратная польская запись (далее результат)
     */
    public List<Operation> translate(String expression) {
        cleanCache();
        expression = Validator.processExpression(expression);
        for (char element: expression.toCharArray()) {
            translateElement(String.valueOf(element));
        }
        moveRemainingOperations();
        upgradeOperands(resultList);
        System.out.println("Обратная польская запись: " + resultList);
        return resultList;
    }

    /**
     * Осуществляет перевод отдельного элемента записи в результат
     * @param element элемент
     */
    private void translateElement(String element) {
        if (priorMap.containsKey(element)) {
            translateOperation(element);
        } else if (element.equals("l")) {
            translateOperation("log");
        }
        else {
            translateOperand(element);
        }
    }

    /**
     * Логика перевода математических операторов
     * @param operation оператор
     */
    private void translateOperation(String operation) {
        isPrevElementOperation = true;
        if (operation.equals("|")) {
            while(!operationsStack.peek().getOperation().equals("(")) {
                result.append(operationsStack.peek());
                resultList.add(operationsStack.pop());
            }
            return;
        }
        if (operation.equals("(")) {
            operationsStack.push(newOper(operation));
            return;
        }
        if (operation.equals(")")) {
            while(!operationsStack.peek().getOperation().equals("(")) {
                result.append(operationsStack.peek());
                resultList.add(operationsStack.pop());
            }
            operationsStack.pop();
            return;
        }
        moveOperations(operation);
    }

    /**
     * логика перевода операндов
     * @param operand операнд
     */
    private void translateOperand(String operand) {
        result.append(operand);
        if (!isPrevElementOperation) {
            int lastIndex = resultList.size() - 1;
            resultList.set(lastIndex, newOper(resultList.get(lastIndex).getOperation() + operand));
        } else {
            resultList.add(newOper(operand));
        }
        isPrevElementOperation = false;
    }

    private List<Operation> upgradeOperands(List<Operation> operations) {
        for (int i = 0; i < operations.size(); i++) {
            Operation oper = operations.get(i);
            if (oper.getOperation().matches(UPGRADE_REGEX)) {
                oper.setOperation("0" + oper.getOperation());
                operations.set(i, oper);
            }
        }

        return operations;
    }

    /**
     * Метод логики занесения/вынесения операции в стэке
     * Приоритет операции из параметров сравнивается с приоритетом операции из стека
     * и пока приоритет операции op <= приоритета операции из стека, они (операции из стека)
     * выкидываются и записываются в результат.
     * Как только в стеке не остается элементов или нарушается неравенство, операция из параметров заносится в стек
     * @param op - операция
     */
    private void moveOperations(String op) {
        while(operationsStack.size() > 0 && priorMap.get(op) <= operationsStack.peek().getPriority()) {
            result.append(operationsStack.peek());
            resultList.add(operationsStack.pop());
        }
        operationsStack.push(newOper(op));
    }

    /**
     * Метод заносит в результат оставшиеся операции
     */
    private void moveRemainingOperations() {
        while (operationsStack.size() > 0) {
            result.append(operationsStack.peek());
            resultList.add(operationsStack.pop());
        }
    }

    /**
     * Метод создания объекта операции.
     * Создан исключительно для упрощения кода
     * Операнды - операция c приоритетом OPERAND_PRIORITY
     * @param op строка операция
     * @return объект операции
     */
    private Operation newOper(String op) {
        if (!priorMap.containsKey(op))
            return new Operation(op, Operation.OPERAND_PRIORITY);
        return new Operation(op, priorMap.get(op));
    }

    /**
     * обнуление параметров обьекта для повторного проведения операции перевода
     */
    private void cleanCache() {
        operationsStack = new Stack<>();
        result = new StringBuilder();
        resultList = new ArrayList<>();
        isPrevElementOperation = true;
    }

}