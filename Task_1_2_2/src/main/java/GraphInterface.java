import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Основная идея реализации в том, что в разных вершинах могут находится одинаковые значения.
 * Это накладывает многие ограничения. Из за этого ввод графа довольно некрасивый,
 * и любые операции тоже принимают сложный вид,
 * потому что пользователю приходится поддерживать объекты вершин или их id.
 * <p>
 * Итого у пользователя есть два варианта взаимодействия с графом
 * 1) Поддерживая объекты вершин
 * 2) Поддерживая Id вершин
 * В тестах использую первый вариант.
 *
 * @param <V> Тип объекта находящегося в вершине.
 * @param <E> Тип объекта находящегося в ребре. Он должен наследоваться от Number,
 *            чтобы алгоритм нахождения кратчайшего расстояния мог делать операции сложения и сравнения.
 *            Также при вычислении расстояний все типы приводятся к double.
 */
public interface GraphInterface<V, E extends Number> {

    /**
     * Добавить вершину по значению.
     *
     * @param value значение.
     * @return Объект добавленной вершины.
     */
    Vertex<V> addVertex(V value);


    /**
     * Получить объект вершины по ее ID.
     *
     * @param vertexId айди вершины.
     * @return объект вершины
     */
    Vertex<V> getVertexById(int vertexId);

    /**
     * Удалить вершину по ее объекту.
     * При удалении вершины также удаляются все ребра инцидентные ей.
     *
     * @param vertex объект вершины.
     */
    void deleteVertex(Vertex<V> vertex);

    /**
     * Получить список объектов всех вершин.
     *
     * @return список объектов вершин.
     */
    ArrayList<Vertex<V>> getVerticesList();

    /**
     * Получить список объектов всех ребер.
     *
     * @return список объектов ребер.
     */
    ArrayList<Edge<V, E>> getEdgesList();

    /**
     * Добавить ребро в граф.
     *
     * @param value значение написанное ребре.
     * @param from  объект вершины из которой ребро выходит.
     * @param to    объект вершины в которую ребро входит
     * @return объект добавленного ребра.
     * @throws IllegalArgumentException выкидывается при несуществующих вершинах в аргументах.
     */
    Edge<V, E> addEdge(E value, Vertex<V> from, Vertex<V> to) throws IllegalArgumentException;

    /**
     * Получить Объект ребра по его ID.
     *
     * @param edgeId id ребра.
     * @return объект ребра.
     */
    Edge<V, E> getEdgeById(int edgeId);


    /**
     * Удалить ребро, по его объекту.
     *
     * @param edge объект ребра, которое нужно удалить.
     */
    void deleteEdge(Edge<V, E> edge);


    /**
     * Алгоритм дейкстры на графе
     *
     * @param startVertexId индекс стартовой вершины
     * @return hashmap вида (id вершины, расстояние до нее от стартовой).
     */
    HashMap<Integer, Double> dijkstra(int startVertexId);

    /**
     * Cортирует вершины графа по возрастанию расстояния от стартовой вершины.
     *
     * Время работы - O(dijkstra + VlogV), где V - количество вершин в графе.
     *
     * @param startVertex id стартовой вершины.
     * @return ArrayList пар (расстояние, объект вершины) отсортированный по расстоянию.
     */
    default ArrayList<PairComparable<Double, Vertex<V>>> sortByDistance(Vertex<V> startVertex) {
        HashMap<Integer, Double> distances = dijkstra(startVertex.getId());
        ArrayList<PairComparable<Double, Vertex<V>>> answer = new ArrayList<>();

        for (Map.Entry<Integer, Double> entry : distances.entrySet()) {
            Integer vertexId = entry.getKey();
            Double distance = entry.getValue();

            answer.add(new PairComparable<>(distance, getVertexById(vertexId)));
        }

        Collections.sort(answer);
        return answer;
    }

}
