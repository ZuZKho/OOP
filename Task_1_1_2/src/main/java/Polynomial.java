import java.util.Arrays;
import java.util.Objects;

/**
 * Класс реализующий представление полинома.
 */
public class Polynomial {

    private int[] coefs;
    private int len;

    Polynomial(int[] arr) {
        if (arr.length == 0) {
            System.out.println("Warn: empty array. Polynomial will be equals to 0.");
            this.len = 1;
            this.coefs = new int[this.len];
        } else {
            this.len = arr.length;
            this.coefs = new int[this.len];
            System.arraycopy(arr, 0, this.coefs, 0, this.len);
        }
    }

    /**
     * Данная функция удаляет ведущие нулевые коэффициенты.
     *
     * @param arr полином.
     * @return полином без ведущих нулей.
     */
    private int[] normalize(int[] arr) {
        int polynomialDegree = Math.max(0, arr.length - 1);
        while (polynomialDegree > 0 && arr[polynomialDegree] == 0) {
            polynomialDegree--;
        }

        int newLen = polynomialDegree + 1;
        int[] buf = new int[newLen];
        System.arraycopy(arr, 0, buf, 0, newLen);
        return buf;
    }

    /**
     * Сложение двух полиномов.
     *
     * @param b второе слагаемое
     * @return результат сложения
     */
    public Polynomial plus(Polynomial b) {
        int newLen = Math.max(this.len, b.len);
        int[] newCoefs = new int[newLen];

        for (int i = 0; i < newLen; i++) {
            if (i < this.len) {
                newCoefs[i] += this.coefs[i];
            }
            if (i < b.len) {
                newCoefs[i] += b.coefs[i];
            }
        }

        return new Polynomial(normalize(newCoefs));
    }

    /**
     * Вычитание двух полиномов.
     *
     * @param b полином - вычитаемое
     * @return разность полиномов
     */
    public Polynomial minus(Polynomial b) {
        int newLen = Math.max(this.len, b.len);
        int[] newCoefs = new int[newLen];

        for (int i = 0; i < newLen; i++) {
            if (i < this.len) {
                newCoefs[i] += this.coefs[i];
            }
            if (i < b.len) {
                newCoefs[i] -= b.coefs[i];
            }
        }

        return new Polynomial(normalize(newCoefs));
    }

    /**
     * Умножение двух полиномов.
     *
     * @param b второй множитель
     * @return произведение полиномов
     */
    public Polynomial multiply(Polynomial b) {
        int newLen = this.len + b.len - 1;
        int[] newCoefs = new int[newLen];
        for (int i = 0; i < this.len; i++) {
            for (int j = 0; j < b.len; j++) {
                newCoefs[i + j] += this.coefs[i] * b.coefs[j];
            }
        }
        return new Polynomial(normalize(newCoefs));
    }

    /**
     * Вычисления значения полинома в точке.
     *
     * @param x точка, в которой вычисляется значение
     * @return значение в точке x
     */
    public int evaluate(int x) {
        int cur = 1;
        int res = 0;
        for (int coef : coefs) {
            res += cur * coef;
            cur *= x;
        }
        return res;
    }

    /**
     * Взятие производной от полинома.
     *
     * @param x порядок производной
     * @return x-тая производная полинома
     */
    public Polynomial differentiate(int x) {
        int[] newCoefs = new int[this.len];
        int newLen = this.len;
        System.arraycopy(this.coefs, 0, newCoefs, 0, this.len);

        for (int i = 0; i < Math.min(this.len, x); i++) {
            System.arraycopy(newCoefs, 1, newCoefs, 0, newLen - 1);
            newCoefs[newLen - 1] = 0;
            for (int j = 0; j < newLen - 1; j++) {
                newCoefs[j] *= (j + 1);
            }
            newLen--;
        }

        return new Polynomial(normalize(newCoefs));
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(len);
        result = 31 * result + Arrays.hashCode(coefs);
        return result;
    }

    /**
     * Сравнение двух полиномов.
     *
     * @param obj второй полином
     * @return равны ли полиномы
     */
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Polynomial)) {
            return false;
        }
        Polynomial b = (Polynomial) obj;

        if (this.len != b.len) {
            return false;
        }
        for (int i = 0; i < this.len; i++) {
            if (this.coefs[i] != b.coefs[i]) {
                return false;
            }
        }
        return true;
    }

    /**
     * Формирует строковое представление полинома.
     *
     * @return строковое представление полинома
     */
    public String toString() {
        if (this.len == 1 && this.coefs[0] == 0) {
            return "0";
        }

        String res = "";
        for (int i = this.len - 1; i >= 0; i--) {
            if (this.coefs[i] != 0) {
                if (res == "") {

                    if (this.coefs[i] < 0) {
                        res += "- " + Math.abs(this.coefs[i]);
                    } else {
                        res += this.coefs[i];
                    }

                } else {
                    if (this.coefs[i] < 0) {
                        res += " - " + Math.abs(this.coefs[i]);
                    } else {
                        res += " + " + this.coefs[i];
                    }

                }

                if (i != 0) {
                    res += "x";
                    if (i != 1) {
                        res += "^" + i;
                    }
                }
            }
        }

        return res;
    }
}
