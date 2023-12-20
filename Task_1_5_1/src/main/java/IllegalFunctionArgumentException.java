/**
 * Exception when dividing by zero, sqrt from negative and another...
 */
public class IllegalFunctionArgumentException extends Exception {

    IllegalFunctionArgumentException() {
        super("Illegal function argument");
    }

    IllegalFunctionArgumentException(String msg) {
        super(msg);
    }
}
