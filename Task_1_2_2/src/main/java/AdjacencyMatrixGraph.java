// Так как для алгоритма поиска кратчайшего расстояния должны
// быть определены сложение и сравнение,
// То будем приводить все типы к даблу.

import java.util.HashMap;
import java.util.HashSet;

public class AdjacencyMatrixGraph<V, E extends Number> extends Graph<V, E>
        implements GraphInterface<V, E> {
    private final HashMap<Integer, HashMap<Integer, Edge<V, E>>> matrix;

    public AdjacencyMatrixGraph() {
        this.vertices = new HashMap<Integer, Vertex<V>>();
        this.edges = new HashMap<Integer, Edge<V, E>>();
        this.matrix = new HashMap<Integer, HashMap<Integer, Edge<V, E>>>();
    }

    /**
     * Добавить вершину по значению.
     * Время работы - O(1).
     *
     * @param value значение.
     * @return Объект добавленной вершины.
     */
    public Vertex<V> addVertex(V value) {
        Vertex<V> newVertex = new Vertex<V>(value);
        vertices.put(newVertex.getId(), newVertex);
        matrix.put(newVertex.getId(), new HashMap<Integer, Edge<V, E>>());
        return newVertex;
    }

    /**
     * Удалить вершину по ее объекту.
     * При удалении вершины также удаляются все ребра инцидентные ей.
     * Время работы - O(V), где V - количество вершин в графе.
     *
     * @param vertex объект вершины.
     */
    public void deleteVertex(Vertex<V> vertex) {
        // При удалении вершины, также нужно удалить ребра,
        // инцидентные данной вершние

        // Удаляем выходящие ребра
        for (var entry : matrix.get(vertex.getId()).entrySet()) {
            edges.remove(entry.getValue().getId());
        }
        // Удаляем входящие ребра
        for (var entry : matrix.entrySet()) {
            if (entry.getValue().containsKey(vertex.getId())) {
                edges.remove(entry.getValue().get(vertex.getId()).getId());
            }
        }

        matrix.remove(vertex.getId());

        for (var entry : matrix.entrySet()) {
            entry.getValue().remove(vertex.getId());
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
    public Edge<V, E> addEdge(E value, Vertex<V> from, Vertex<V> to) throws IllegalArgumentException {
        if (!matrix.containsKey(from.getId())) {
            throw new IllegalArgumentException("Не существует вершины с таким id.");
        }
        if (!matrix.containsKey(to.getId())) {
            throw new IllegalArgumentException("Не существует вершины с таким id.");
        }


        Edge<V, E> newEdge = new Edge<V, E>(value, from, to);
        this.matrix.get(from.getId()).put(to.getId(), newEdge);
        this.edges.put(newEdge.getId(), newEdge);

        return newEdge;
    }

    /**
     * Удалить ребро, по его объекту.
     * Время работы - O(1).
     *
     * @param edge объект ребра, которое нужно удалить.
     */
    public void deleteEdge(Edge<V, E> edge) {
        int positionFrom = edge.from.getId();
        int positionTo = edge.to.getId();

        this.matrix.get(positionFrom).remove(positionTo);

        this.edges.remove(edge.getId());
    }


    /**
     * Алгоритм дейкстры на графе.
     * Время работы - O(V^2), где V - количество вершин в графе.
     *
     * @param startVertexId индекс стартовой вершины
     * @return hashmap вида (id вершины, расстояние до нее от стартовой).
     */
    public HashMap<Integer, Double> dijkstra(int startVertexId) {
        int verticesCount = matrix.size();

        HashMap<Integer, Double> distances = new HashMap<>();
        HashSet<Integer> used = new HashSet<Integer>();

        distances.put(startVertexId, (double) 0);
        for (int i = 0; i < verticesCount; i++) {

            Double minValue = null;
            Integer minIdx = null;

            for (var entry : matrix.entrySet()) {
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

            for (var entry : matrix.get(minIdx).entrySet()) {
                Integer toVertexId = entry.getKey();
                Double weight = entry.getValue().value.doubleValue();

                if (!used.contains(toVertexId)
                        && (!distances.containsKey(toVertexId)
                        || distances.get(toVertexId) > minValue + weight)) {
                    distances.put(toVertexId, minValue + weight);
                }
            }
        }
        return distances;
    }

}