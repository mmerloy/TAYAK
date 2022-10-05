package model;

public class Operation {
    public static final int OPERAND_PRIORITY = 0;
    private String operation;
    private int priority;

    public Operation(String operation, int priority) {
        this.operation = operation;
        this.priority = priority;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public int getPriority() {
        return priority;
    }

    @Override
    public String toString() {
        return operation;
    }
}