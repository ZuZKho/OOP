/**
 * Класс вершины.
 *
 * @param <T> тип значения вершины.
 */
public class Vertex<T> {
    private T value;
    private int id;
    private static int freeId = 0;

    Vertex(T value) {
        this.value = value;
        this.id = freeId;
        freeId++;
    }

    /**
     * Установить значение вершины.
     *
     * @param value новое значение.
     */
    protected void setValue(T value) {
        this.value = value;
    }

    /**
     * Получить значение вершины.
     *
     * @return значение вершины.
     */
    public T getValue() {
        return this.value;
    }

    /**
     * Получить id вершины.
     *
     * @return id вершины.
     */
    public int getId() {
        return this.id;
    }
}
