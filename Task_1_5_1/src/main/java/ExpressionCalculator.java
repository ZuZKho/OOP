import java.util.Stack;

/**
 * Utility class for calculating prefix notation expression from string.
 */
public class ExpressionCalculator {

    private static Expression[] popUnaryOperatorArguments(Stack<Expression> stack)
            throws WrongPrefixNotationException {
        if (stack.isEmpty()) {
            throw new WrongPrefixNotationException("Stack is too small");
        }

        Expression first = stack.pop();
        return new Expression[]{first};
    }

    private static Expression[] popBinaryOperatorArguments(Stack<Expression> stack)
            throws WrongPrefixNotationException {
        if (stack.size() < 2) {
            throw new WrongPrefixNotationException("Stack is too small");
        }

        Expression first = stack.pop();
        Expression second = stack.pop();
        return new Expression[]{first, second};
    }

    /**
     * Calculating expression from string.
     *
     * @param expression input string.
     * @return double value of expression.
     * @throws IllegalFunctionArgumentException      division by 0, sqrt from negative and another...
     * @throws IllegalPrefixNotationOperandException not allowed argument like sqr, ln.
     * @throws WrongPrefixNotationException          can't parse string as prefix notation.
     */
    public static double calculate(String expression)
            throws IllegalFunctionArgumentException,
            IllegalPrefixNotationOperandException,
            WrongPrefixNotationException {

        Stack<Expression> stack = new Stack<>();
        String[] words = expression.split(" ");

        for (int i = words.length - 1; i >= 0; i--) {
            String current = words[i];
            Expression[] operands;

            switch (current) {
                case "+":
                    operands = popBinaryOperatorArguments(stack);
                    stack.push(new Expression.Plus(operands[0], operands[1]));
                    break;
                case "-":
                    operands = popBinaryOperatorArguments(stack);
                    stack.push(new Expression.Minus(operands[0], operands[1]));
                    break;
                case "*":
                    operands = popBinaryOperatorArguments(stack);
                    stack.push(new Expression.Multiplication(operands[0], operands[1]));
                    break;
                case "/":
                    operands = popBinaryOperatorArguments(stack);
                    stack.push(new Expression.Division(operands[0], operands[1]));
                    break;
                case "log":
                    operands = popBinaryOperatorArguments(stack);
                    stack.push(new Expression.Log(operands[0], operands[1]));
                    break;
                case "pow":
                    operands = popBinaryOperatorArguments(stack);
                    stack.push(new Expression.Pow(operands[0], operands[1]));
                    break;
                case "sin":
                    operands = popUnaryOperatorArguments(stack);
                    stack.push(new Expression.Sin(operands[0]));
                    break;
                case "cos":
                    operands = popUnaryOperatorArguments(stack);
                    stack.push(new Expression.Cos(operands[0]));
                    break;
                case "sqrt":
                    operands = popUnaryOperatorArguments(stack);
                    stack.push(new Expression.Sqrt(operands[0]));
                    break;
                default:
                    try {
                        double value = Double.parseDouble(current);
                        stack.push(new Expression.Constant(value));
                    } catch (Exception e) {
                        throw new IllegalPrefixNotationOperandException("Unknown operation");
                    }
                    break;
            }
        }

        Expression result = stack.pop();
        if (!stack.isEmpty()) {
            throw new WrongPrefixNotationException("Empty stack");
        }
        return result.evaluate();
    }
}
