public class Polynomial {

    private int[] coefs;
    private int n;

    Polynomial (int[] arr) {
        this.n = arr.length;
        this.coefs = new int[arr.length];
        System.arraycopy(arr, 0, this.coefs, 0, n);
    }

//    private void realloc(int nwN) {
//        int[] newCoefs = new int[nwN];
//        System.arraycopy(this.coefs, 0, newCoefs, 0, Math.min(this.n, nwN));
//        this.coefs = newCoefs;
//        this.n = nwN;
//    }
//
//    private void normalize() {
//        int nwN = this.n - 1;
//        while(nwN > 0 && this.coefs[nwN] == 0) {
//            nwN--;
//        }
//        realloc(nwN + 1);
//    }

    public Polynomial plus(Polynomial b) {
        int nwN = Math.max(this.n, b.n);
        int[] newCoefs = new int[nwN];

        for(int i = 0; i < nwN; i++) {
            if (i < this.n) {
                newCoefs[i] += this.coefs[i];
            }
            if (i < b.n) {
                newCoefs[i] += b.coefs[i];
            }
        }

        return new Polynomial(newCoefs);
    }

    public Polynomial minus(Polynomial b) {
        int nwN = Math.max(this.n, b.n);
        int[] newCoefs = new int[nwN];

        for(int i = 0; i < nwN; i++) {
            if (i < this.n) {
                newCoefs[i] += this.coefs[i];
            }
            if (i < b.n) {
                newCoefs[i] -= b.coefs[i];
            }
        }

        return new Polynomial(newCoefs);
    }

    public Polynomial times(Polynomial b) {
        int nwN = this.n + b.n - 1;
        int[] newCoefs = new int[nwN];
        for(int i = 0; i < this.n; i++) {
            for(int j = 0; j < b.n; j++) {
                newCoefs[i + j] += this.coefs[i] * b.coefs[j];
            }
        }
        return new Polynomial(newCoefs);
    }

    public int evaluate(int x) {
        int cur = 1;
        int res = 0;
        for(int coef: coefs) {
            res += cur * coef;
            cur *= x;
        }
        return res;
    }

    public Polynomial differentiate(int x) {

        int[] newCoefs = new int[this.n];
        int nn = this.n;
        System.arraycopy(this.coefs, 0, newCoefs, 0, this.n);

        for(int i = 0; i < x; i++) {
            System.arraycopy(newCoefs, 1, newCoefs, 0, nn - 1);
            newCoefs[nn - 1] = 0;
            for(int j = 0; j < nn - 1; j++) {
                newCoefs[j] *= (j + 1);
            }
            nn--;
        }

        // Удалим лишние нули
        int nwN = nn - 1;
        while(nwN > 0 && newCoefs[nwN] == 0) {
            nwN--;
        }

        int[] buf = new int[nwN + 1];
        System.arraycopy(newCoefs, 0, buf, 0, nwN + 1);

        return new Polynomial(buf);
    }

    public boolean isEqual(Polynomial b) {
        if (this.n != b.n) return false;
        for(int i = 0; i < this.n; i++) {
            if (this.coefs[i] != b.coefs[i]) return false;
        }
        return true;
    }

    public String toString() {
        String res = "";
        for(int i = n - 1; i >= 0; i--) {
            if (this.coefs[i] != 0) {
                if (res == "") {
                    res += this.coefs[i];
                } else {
                    res += " + " + this.coefs[i];
                }

                if (i != 0) {
                    res+= "x";
                    if (i != 1) {
                        res += "^" + i;
                    }
                }
            }
        }
        return res;
    }

    public static void main(String[] args) {
        Polynomial p1 = new Polynomial(new int[] {4, 3, 6, 7});
        Polynomial p2 = new Polynomial(new int[] {3, 2, 8});
        System.out.println("Hello, world!");
        System.out.println(p1.plus(p2.differentiate(1)).toString());
        System.out.println(p1.times(p2).evaluate(2));
    }

}
