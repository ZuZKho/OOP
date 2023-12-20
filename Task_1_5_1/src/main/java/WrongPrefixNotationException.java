/**
 * Exception when string can't be parsed like prefix notation.
 */
public class WrongPrefixNotationException extends Exception {

    WrongPrefixNotationException() {
        super("Unknown operand or symbol");
    }

    WrongPrefixNotationException(String msg) {
        super(msg);
    }
}
