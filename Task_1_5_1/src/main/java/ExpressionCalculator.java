import java.util.Stack;

/**
 * Utility class for calculating prefix notation expression from string.
 */
public class ExpressionCalculator {

    private static Expression[] myPop2(Stack<Expression> stack)
            throws WrongPrefixNotationException {
        if (stack.size() < 2) {
            throw new WrongPrefixNotationException();
        }

        Expression first = stack.pop();
        Expression second = stack.pop();
        return new Expression[]{first, second};
    }

    private static Expression[] myPop1(Stack<Expression> stack)
            throws WrongPrefixNotationException {
        if (stack.isEmpty()) {
            throw new WrongPrefixNotationException();
        }

        Expression first = stack.pop();
        return new Expression[]{first};
    }

    /**
     * Calculating expression from string.
     *
     * @param expression input string.
     * @return double value of expression.
     * @throws IllegalFunctionArgument      division by 0, sqrt from negative and another...
     * @throws IllegalPrefixNotationOperand not allowed argument like sqr, ln.
     * @throws WrongPrefixNotationException can't parse string as prefix notation.
     */
    public static double calculate(String expression)
            throws IllegalFunctionArgument,
            IllegalPrefixNotationOperand,
            WrongPrefixNotationException {

        Stack<Expression> stack = new Stack<>();
        String[] words = expression.split(" ");

        for (int i = words.length - 1; i >= 0; i--) {
            String current = words[i];
            Expression[] operands;

            switch (current) {
                case "+":
                    operands = myPop2(stack);
                    stack.push(new Expression.Plus(operands[0], operands[1]));
                    break;
                case "-":
                    operands = myPop2(stack);
                    stack.push(new Expression.Minus(operands[0], operands[1]));
                    break;
                case "*":
                    operands = myPop2(stack);
                    stack.push(new Expression.Multiplication(operands[0], operands[1]));
                    break;
                case "/":
                    operands = myPop2(stack);
                    stack.push(new Expression.Division(operands[0], operands[1]));
                    break;
                case "log":
                    operands = myPop2(stack);
                    stack.push(new Expression.Log(operands[0], operands[1]));
                    break;
                case "pow":
                    operands = myPop2(stack);
                    stack.push(new Expression.Pow(operands[0], operands[1]));
                    break;
                case "sin":
                    operands = myPop1(stack);
                    stack.push(new Expression.Sin(operands[0]));
                    break;
                case "cos":
                    operands = myPop1(stack);
                    stack.push(new Expression.Cos(operands[0]));
                    break;
                case "sqrt":
                    operands = myPop1(stack);
                    stack.push(new Expression.Sqrt(operands[0]));
                    break;
                default:
                    try {
                        double value = Double.parseDouble(current);
                        stack.push(new Expression.Constant(value));
                    } catch (Exception e) {
                        throw new IllegalPrefixNotationOperand();
                    }
                    break;
            }
        }

        Expression result = stack.pop();
        if (!stack.isEmpty()) {
            throw new WrongPrefixNotationException();
        }
        return result.evaluate();
    }
}
