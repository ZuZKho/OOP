import java.util.Stack;
import java.util.function.Supplier;

public class ExpressionCalculator {

    private static Expression[] MyPop2(Stack<Expression> stack) throws WrongPrefixNotationException {
        if (stack.size() < 2) {
            throw new WrongPrefixNotationException();
        }

        Expression first = stack.pop();
        Expression second = stack.pop();
        return new Expression[]{first, second};
    }

    private static Expression[] MyPop1(Stack<Expression> stack) throws WrongPrefixNotationException{
        if (stack.isEmpty()) {
            throw new WrongPrefixNotationException();
        }

        Expression first = stack.pop();
        return new Expression[]{first};
    }

    public static double calculate(String expression) throws IllegalFunctionArgument, IllegalPrefixNotationOperand, WrongPrefixNotationException{

        Stack<Expression> stack = new Stack<>();
        String[] words = expression.split(" ");

        for (int i = words.length - 1; i >= 0; i--) {
            String current = words[i];
            Expression[] operands;

            switch (current) {
                case "+":
                    operands = MyPop2(stack);
                    stack.push(new Expression.Plus(operands[0], operands[1]));
                    break;
                case "-":
                    operands = MyPop2(stack);
                    stack.push(new Expression.Minus(operands[0], operands[1]));
                    break;
                case "*":
                    operands = MyPop2(stack);
                    stack.push(new Expression.Multiplication(operands[0], operands[1]));
                    break;
                case "/":
                    operands = MyPop2(stack);
                    stack.push(new Expression.Division(operands[0], operands[1]));
                    break;
                case "log":
                    operands = MyPop2(stack);
                    stack.push(new Expression.Log(operands[0], operands[1]));
                    break;
                case "pow":
                    operands = MyPop2(stack);
                    stack.push(new Expression.Pow(operands[0], operands[1]));
                    break;
                case "sin":
                    operands = MyPop1(stack);
                    stack.push(new Expression.Sin(operands[0]));
                    break;
                case "cos":
                    operands = MyPop1(stack);
                    stack.push(new Expression.Cos(operands[0]));
                    break;
                case "sqrt":
                    operands = MyPop1(stack);
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
