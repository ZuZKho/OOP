import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

/**
 * Класс графа реализующий представление в виде Матрицы инцидентности.
 *
 * @param <V> тип значения вершины.
 * @param <E> тип значения ребра.
 */
public class IncidenceMatrixGraph<V, E extends Number> extends Graph<V, E>
        implements GraphInterface<V, E> {

    // matrix.get(edge) - map из двух вершни
    private final HashMap<Integer, HashMap<Integer, Double>> matrix;

    public IncidenceMatrixGraph() {
        this.vertices = new HashMap<>();
        this.edges = new HashMap<>();
        this.matrix = new HashMap<Integer, HashMap<Integer, Double>>();
    }

    /**
     * Добавить вершину по значению.
     * Время работы - O(1).
     *
     * @param value значение.
     * @return Объект добавленной вершины.
     */
    public Vertex<V> addVertex(V value) {
        Vertex<V> newVertex = new Vertex<>(value);
        vertices.put(newVertex.getId(), newVertex);
        // Когда добавляем вершину в матрицу добавляются нулевые элементы
        // чтобы зря не терять память, можно ничего не делать
        return newVertex;
    }

    /**
     * Удалить вершину по ее объекту.
     * При удалении вершины также удаляются все ребра инцидентные ей.
     * Время работы - O(E), где E - количество ребер в графе.
     *
     * @param vertex объект вершины.
     */
    public void deleteVertex(Vertex<V> vertex) {
        // Когда удаляем вершину, нужно пройтись по строке с вершиной
        // и занулить все столбцы в которых не ноль и удалить эти ребра
        // + Удалить из списка ребер в графе.
        ArrayList<Integer> keysToDelete = new ArrayList<>();
        for (var entry : matrix.entrySet()) {
            if (entry.getValue().containsKey(vertex.getId())) {
                keysToDelete.add(entry.getKey());
                edges.remove(entry.getKey());
            }
        }

        for (var key : keysToDelete) {
            matrix.remove(key);
        }

        vertices.remove(vertex.getId());
    }

    /**
     * Добавить ребро в граф.
     * Время работы - O(1).
     *
     * @param value значение написанное ребре.
     * @param from  объект вершины из которой ребро выходит.
     * @param to    объект вершины в которую ребро входит
     * @return объект добавленного ребра.
     * @throws IllegalArgumentException выкидывается при несуществующих вершинах в аргументах.
     */
    public Edge<V, E> addEdge(E value, Vertex<V> from, Vertex<V> to)
            throws IllegalArgumentException {

        if (!vertices.containsKey(from.getId())) {
            throw new IllegalArgumentException("Не существует вершины с таким id.");
        }
        if (!vertices.containsKey(to.getId())) {
            throw new IllegalArgumentException("Не существует вершины с таким id.");
        }

        Edge<V, E> newEdge = new Edge<>(value, from, to);
        int fromId = from.getId();

        edges.put(newEdge.getId(), newEdge);
        matrix.put(newEdge.getId(), new HashMap<>());
        matrix.get(newEdge.getId()).put(fromId, -value.doubleValue());
        matrix.get(newEdge.getId()).put(to.getId(), value.doubleValue());
        return newEdge;
    }

    /**
     * Удалить ребро, по его объекту.
     * Время работы - O(1).
     *
     * @param edge объект ребра, которое нужно удалить.
     */
    public void deleteEdge(Edge<V, E> edge) {
        matrix.remove(edge.getId());
        edges.remove(edge.getId());
    }

    /**
     * Алгоритм дейкстры на графе.
     * Время работы - O(V^2*E), где V - кол-во вершин, E - кол-во ребер.
     *
     * @param startVertexId индекс стартовой вершины
     * @return hashmap вида (id вершины, расстояние до нее от стартовой).
     */
    public HashMap<Integer, Double> dijkstra(int startVertexId) {
        int verticesCount = vertices.size();

        HashMap<Integer, Double> distances = new HashMap<>();
        HashSet<Integer> used = new HashSet<Integer>();

        distances.put(startVertexId, (double) 0);

        for (int i = 0; i < verticesCount; i++) {

            Double minValue = null;
            Integer minIdx = null;

            for (var entry : vertices.entrySet()) {
                Integer vertexId = entry.getKey();
                if (used.contains(vertexId) || !distances.containsKey(vertexId)) {
                    continue;
                }

                if (minValue == null || distances.get(vertexId) < minValue) {
                    minValue = distances.get(vertexId);
                    minIdx = vertexId;
                }
            }

            if (minIdx == null) {
                break;
            }
            used.add(minIdx);

            // Смотрим все ребра из вершины minIdx
            for (var entry : matrix.entrySet()) {
                if (!entry.getValue().containsKey(minIdx)) {
                    continue;
                }
                if (entry.getValue().get(minIdx) >= 0) {
                    continue;
                }

                // Знаем, что из вершины minIdx выходит ребро
                // Найдем куда оно выходит
                // entry.getValue() - HashMap размера два
                // Так как делаем матрицу инцидентности, то видимо подразумевается,
                // что нужно пройтись по всем возможным вершинам, чтобы найти
                // куда идет ребро.
                // Поэтому даже при том что у нас HashMap размера два,
                // просто пройдем по всем вершинам

                Integer toVertexId = 1;
                for (var vertex : vertices.entrySet()) {
                    if (vertex.getKey() != minIdx
                            && entry.getValue().containsKey(vertex.getKey())) {
                        toVertexId = vertex.getKey();
                        break;
                    }
                }

                Double weight = entry.getValue().get(toVertexId);

                if (!used.contains(toVertexId) && (!distances.containsKey(toVertexId)
                        || distances.get(toVertexId) > minValue + weight)) {
                    distances.put(toVertexId, minValue + weight);
                }
            }
        }
        return distances;
    }

}