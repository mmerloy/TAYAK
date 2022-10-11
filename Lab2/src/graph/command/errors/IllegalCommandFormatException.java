package graph.command.errors;

import java.util.IllegalFormatException;

public class IllegalCommandFormatException extends Exception {
    public IllegalCommandFormatException(){
        super();
    }
    public IllegalCommandFormatException(String str){
        super(str);
    }
}