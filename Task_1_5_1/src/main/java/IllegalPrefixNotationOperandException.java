/**
 * Exception when prefix notation contains not allowed params, like sqr, ln and another...
 */
public class IllegalPrefixNotationOperandException extends Exception {

    IllegalPrefixNotationOperandException() {
        super("Illegal prefix notation");
    }

    IllegalPrefixNotationOperandException(String msg) {
        super(msg);
    }
}
