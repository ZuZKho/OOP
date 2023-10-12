public class Pair<T1 extends Comparable<T1>, T2> implements Comparable<Pair<T1, T2>>{

    public T1 first;
    public T2 second;

    public Pair(T1 x, T2 y)
    {
        this.first = x;
        this.second = y;
    }

    @Override
    public int compareTo(Pair<T1, T2> a) {
        // if the string are not equal
        if (this.first.compareTo(a.first) != 0) {
            return this.first.compareTo(a.first);
        }
        return 0;
    }
}
