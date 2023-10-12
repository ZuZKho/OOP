public class Vertex<T> {
    private T value;
    private int id;
    private static int freeId = 0;

    Vertex(T value) {
        this.value = value;
        this.id = freeId;
        freeId++;
    }

    public T getValue() {
        return this.value;
    }

    public int getId() {
        return this.id;
    }
}
