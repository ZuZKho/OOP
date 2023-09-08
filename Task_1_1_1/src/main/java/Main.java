public class Main {
    public static void main(String[] args) {
        /*
        int[] ans = heapsort(new int[] {2,3,1});
        for(int i = 0; i < ans.length; i ++) {
            System.out.print(ans[i] + " ");
        }
        */
    }

    public static int[] heapsort(int[] arr) {
        Heap heap = new Heap(arr.length);

        for (int i = 0; i < arr.length; i++) {
            heap.Insert(arr[i]);
        }

        for(int i = 0; i < arr.length; i++) {
            arr[i] = heap.Extract();
        }

        return arr;
    }
}
