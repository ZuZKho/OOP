/**
 * Класс реализующий представление полинома.
 */
public class Polynomial {

    private int[] coefs;
    private int n;

    Polynomial(int[] arr) {
        this.n = arr.length;
        this.coefs = new int[arr.length];
        System.arraycopy(arr, 0, this.coefs, 0, n);
    }

    private int[] normalize(int[] arr) {
        int nwN = Math.max(0, arr.length - 1);
        while (nwN > 0 && arr[nwN] == 0) {
            nwN--;
        }
        int[] buf = new int[nwN + 1];
        System.arraycopy(arr, 0, buf, 0, nwN + 1);
        return buf;
    }

    /**
     * Сложение двух полиномов.
     *
     * @param b второе слагаемое
     * @return результат сложения
     */
    public Polynomial plus(Polynomial b) {
        int nwN = Math.max(this.n, b.n);
        int[] newCoefs = new int[nwN];

        for (int i = 0; i < nwN; i++) {
            if (i < this.n) {
                newCoefs[i] += this.coefs[i];
            }
            if (i < b.n) {
                newCoefs[i] += b.coefs[i];
            }
        }

        newCoefs = normalize(newCoefs);
        return new Polynomial(newCoefs);
    }

    /**
     * Вычитание двух полиномов.
     *
     * @param b полином - вычитаемое
     * @return разность полиномов
     */
    public Polynomial minus(Polynomial b) {
        int nwN = Math.max(this.n, b.n);
        int[] newCoefs = new int[nwN];

        for (int i = 0; i < nwN; i++) {
            if (i < this.n) {
                newCoefs[i] += this.coefs[i];
            }
            if (i < b.n) {
                newCoefs[i] -= b.coefs[i];
            }
        }

        newCoefs = normalize(newCoefs);
        return new Polynomial(newCoefs);
    }

    /**
     * Умножение двух полиномов.
     *
     * @param b второй множитель
     * @return произведение полиномов
     */
    public Polynomial times(Polynomial b) {
        int nwN = this.n + b.n - 1;
        int[] newCoefs = new int[nwN];
        for (int i = 0; i < this.n; i++) {
            for (int j = 0; j < b.n; j++) {
                newCoefs[i + j] += this.coefs[i] * b.coefs[j];
            }
        }
        newCoefs = normalize(newCoefs);
        return new Polynomial(newCoefs);
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

        int[] newCoefs = new int[this.n];
        int nn = this.n;
        System.arraycopy(this.coefs, 0, newCoefs, 0, this.n);

        for (int i = 0; i < Math.min(this.n, x); i++) {
            System.arraycopy(newCoefs, 1, newCoefs, 0, nn - 1);
            newCoefs[nn - 1] = 0;
            for (int j = 0; j < nn - 1; j++) {
                newCoefs[j] *= (j + 1);
            }
            nn--;
        }

        // Удалим лишние нули
        int nwN = Math.max(0, nn - 1);
        while (nwN > 0 && newCoefs[nwN] == 0) {
            nwN--;
        }
        int[] buf = new int[nwN + 1];
        System.arraycopy(newCoefs, 0, buf, 0, nwN + 1);

        return new Polynomial(buf);
    }

    /**
     * Сравнение двух полиномов.
     *
     * @param b второй полином
     * @return равны ли полиномы
     */
    public boolean isEqual(Polynomial b) {
        if (this.n != b.n) return false;
        for (int i = 0; i < this.n; i++) {
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

        if (this.n == 1 && this.coefs[0] == 0) {
            return "0";
        }

        String res = "";
        for (int i = n - 1; i >= 0; i--) {
            if (this.coefs[i] != 0) {
                if (res == "") {
                    res += this.coefs[i];
                } else {
                    res += " + " + this.coefs[i];
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
