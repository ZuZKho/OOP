public class Heap {

    private int[] arr;
    private int sz;

    private void Sift_Up(int idx) {
        if (idx == 0) return;

        int par = (idx - 1) / 2;
        if (arr[par] > arr[idx]) {
            //swap
            int help = arr[par];
            arr[par] = arr[idx];
            arr[idx] = help;

            Sift_Up(par);
        }
    }

    private void Sift_Down(int idx) {
        if (idx * 2 + 1 < sz && arr[idx] > arr[idx * 2 + 1] ||
            idx * 2 + 2 < sz && arr[idx] > arr[idx * 2 + 2]     ) {
                int toswp = idx * 2 + 1;
                if (idx * 2 + 2 < sz && arr[idx * 2 + 2] < arr[idx * 2 + 1]) toswp = idx * 2 + 2;

                // swap
                int help = arr[idx];
                arr[idx] = arr[toswp];
                arr[toswp] = help;

                Sift_Down(toswp);
        }
    }

    public Heap(int length) {
        sz = 0;
        arr = new int[length];
    }

    public void Insert(int x) {
        arr[sz] = x;
        Sift_Up(sz);
        sz++;
    }

    public int Extract() {
        int res = arr[0];
        arr[0] = arr[sz - 1];
        sz --;
        Sift_Down(0);
        return res;
    }

}
