public interface Expression {

    double evaluate() throws IllegalFunctionArgument;

    class Constant implements Expression {
        private final double value;

        Constant(double value) {
            this.value = value;
        }

        public double evaluate() {
            return value;
        }
    }

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

    class Sin implements Expression {
        private final Expression first;

        Sin(Expression first) {
            this.first = first;
        }

        public double evaluate() throws IllegalFunctionArgument {
            return Math.sin(first.evaluate());
        }
    }

    class Cos implements Expression {
        private final Expression first;

        Cos(Expression first) {
            this.first = first;
        }

        public double evaluate() throws IllegalFunctionArgument {
            return Math.cos(first.evaluate());
        }
    }

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
