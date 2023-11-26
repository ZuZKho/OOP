/**
 * Exception when dividing by zero, sqrt from negative and another...
 */
public class IllegalFunctionArgument extends Exception {

    IllegalFunctionArgument() {
        super();
    }

    IllegalFunctionArgument(String msg) {
        super(msg);
    }
}
