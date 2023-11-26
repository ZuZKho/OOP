/**
 * Interface for some arithmetic operations.
 */
public interface Expression {

    /**
     * Recursive expression evaluating.
     *
     * @return result of expression
     * @throws IllegalFunctionArgument throws when functions like log or / gets invalid arguments.
     */
    double evaluate() throws IllegalFunctionArgument;

    /**
     * Constant - number from input string.
     */
    class Constant implements Expression {
        private final double value;

        Constant(double value) {
            this.value = value;
        }

        public double evaluate() {
            return value;
        }
    }

    /**
     * Adding two Expressions.
     */
    class Plus implements Expression {
        private final Expression first;
        private final Expression second;

        Plus(Expression first, Expression second) {
            this.first = first;
            this.second = second;
        }

        public double evaluate() throws IllegalFunctionArgument {
            return first.evaluate() + second.evaluate();
        }
    }

    /**
     * Subtracting two Expressions.
     */
    class Minus implements Expression {
        private final Expression first;
        private final Expression second;

        Minus(Expression first, Expression second) {
            this.first = first;
            this.second = second;
        }

        public double evaluate() throws IllegalFunctionArgument {
            return first.evaluate() - second.evaluate();
        }
    }

    /**
     * Multiply two Expressions.
     */
    class Multiplication implements Expression {
        private final Expression first;
        private final Expression second;

        Multiplication(Expression first, Expression second) {
            this.first = first;
            this.second = second;
        }

        public double evaluate() throws IllegalFunctionArgument {
            return first.evaluate() * second.evaluate();
        }
    }

    /**
     * Divide two Expressions.
     */
    class Division implements Expression {
        private final Expression first;
        private final Expression second;

        Division(Expression first, Expression second) {
            this.first = first;
            this.second = second;
        }

        public double evaluate() throws IllegalFunctionArgument {
            double result = second.evaluate();
            if (result == 0) {
                throw new IllegalFunctionArgument("Can't divide by 0");
            }
            return first.evaluate() / result;
        }
    }

    /**
     * Take Logarithm log_a (b), from expressions a and b.
     */
    class Log implements Expression {
        private final Expression first;
        private final Expression second;

        Log(Expression first, Expression second) {
            this.first = first;
            this.second = second;
        }

        public double evaluate() throws IllegalFunctionArgument {
            double res1 = first.evaluate();
            double res2 = second.evaluate();
            if (res1 <= 0 || res2 <= 0) {
                throw new IllegalFunctionArgument();
            }
            // log_b(x) = log(x) / log(b);
            res1 = Math.log(res1);
            if (res1 == 0) {
                throw new IllegalFunctionArgument("Bad log arguments");
            }
            return Math.log(res2) / res1;
        }
    }

    /**
     * a^b from expressions a and b.
     */
    class Pow implements Expression {
        private final Expression first;
        private final Expression second;

        Pow(Expression first, Expression second) {
            this.first = first;
            this.second = second;
        }

        public double evaluate() throws IllegalFunctionArgument {
            return Math.pow(first.evaluate(), second.evaluate());
        }
    }

    /**
     * Sin of expression.
     */
    class Sin implements Expression {
        private final Expression first;

        Sin(Expression first) {
            this.first = first;
        }

        public double evaluate() throws IllegalFunctionArgument {
            return Math.sin(first.evaluate());
        }
    }

    /**
     * Cos of expression.
     */
    class Cos implements Expression {
        private final Expression first;

        Cos(Expression first) {
            this.first = first;
        }

        public double evaluate() throws IllegalFunctionArgument {
            return Math.cos(first.evaluate());
        }
    }

    /**
     * Sqrt of expression.
     */
    class Sqrt implements Expression {
        private final Expression first;

        Sqrt(Expression first) {
            this.first = first;
        }

        public double evaluate() throws IllegalFunctionArgument {
            double res = first.evaluate();
            if (res < 0) {
                throw new IllegalFunctionArgument("Can't sqrt from negative number");
            }

            return Math.sqrt(first.evaluate());
        }
    }

}
