import java.util.*;

// Так как для алгоритма поиска кратчайшего расстояния должны быть определены сложение и сравнение,
// То будем приводить все типы к даблу.

public class AdjacencyMatrixGraph<V, E extends Number>  implements Graph<V, E> {

    private final HashMap<Integer, Vertex<V>> vertices = new HashMap<Integer, Vertex<V>>();  // HashMap, который по индексу хранить вершину.
    private final ArrayList<Integer> verticesIds = new ArrayList<Integer>(); // ArrayList, который сохраняет Порядок индексов вершин, которые лежат в матрице.
    private final HashMap<Integer, Edge<V, E>> edges = new HashMap<Integer, Edge<V, E>>(); // HashMap, который по индексу хранит ребро.

    private final ArrayList<ArrayList<Double>> matrix = new ArrayList<ArrayList<Double>>();

    public int addVertex(Vertex<V> newVertex) {

        for(ArrayList<Double> row: matrix) {
            row.add(null);
        }

        // Добавляем строчку
        matrix.add(new ArrayList<Double>());
        for(int i = 0; i < matrix.size(); i++) {
            matrix.get(matrix.size() - 1).add(null);
        }

        verticesIds.add(newVertex.getId());

        return newVertex.getId();
    }

    public void deleteVertex(int vertexId) {
        int idx = verticesIds.indexOf(vertexId);
        if (idx == -1) return;

        matrix.remove(idx);
        for(var row : matrix) {
            row.remove(idx);
        }

        verticesIds.remove(idx);
    }

    public int addEdge(E value, int fromId, int toId) throws IllegalArgumentException {
        if (!verticesIds.contains(fromId)) {
            throw new IllegalArgumentException("Не существует вершины с таким id.");
        }
        if (!verticesIds.contains(toId)) {
            throw new IllegalArgumentException("Не существует вершины с таким id.");
        }

        this.matrix.get(fromId).set(toId, value.doubleValue());
        Edge<V, E> newEdge = new Edge<V, E>(value, vertices.get(fromId), vertices.get(toId));

        this.edges.put(newEdge.getId(), newEdge);
        return newEdge.getId();
    }

    public void deleteEdge(int edgeId) {
        Edge<V, E> curEdge = edges.get(edgeId);

        int positionFrom = verticesIds.indexOf(curEdge.from.getId());
        int positionTo = verticesIds.indexOf(curEdge.to.getId());

        this.matrix.get(positionFrom).set(positionTo, null);

        this.edges.remove(curEdge.getId());
    }

    public Vertex<V> getVertex(int id) {
        return vertices.get(id);
    }

    public Edge<V, E> getEdge(int edgeId) {
        return edges.get(edgeId);
    }

    private HashMap<Integer, Double> dijkstra(int startVertexId) {
        int verticesCount = verticesIds.size();

        HashMap<Integer, Double> distances = new HashMap<>();
        HashSet<Integer> used = new HashSet<Integer>();

        distances.put(startVertexId, (double) 0);
        for(int i = 0; i < verticesCount; i++) {

            Double minValue = null;
            Integer minIdxInArr = null;
            for (int j = 0; j < verticesCount; j++) {
                Integer vertexId = verticesIds.get(j);

                if (!used.contains(vertexId) && distances.containsKey(vertexId)) {
                    if (minValue == null || distances.get(vertexId) < minValue) {
                        minValue = distances.get(vertexId);
                        minIdxInArr = j;
                    }
                }
            }

            if (minIdxInArr == null) break;

            int minIdx = verticesIds.get(minIdxInArr);
            used.add(minIdx);

            double distanceToMinIdx = distances.get(minIdx);
            for (int j = 0; j < verticesCount; j++) {

                if (matrix.get(minIdxInArr).get(j) == null) continue;
                Double edgeWeight = matrix.get(minIdxInArr).get(j).doubleValue();
                int toVertexId = verticesIds.get(j);
                if (!used.contains(toVertexId) && (!distances.containsKey(toVertexId) || distances.get(toVertexId) > minValue + edgeWeight)) {
                    distances.put(toVertexId, minValue + edgeWeight);
                }

            }

        }
        return distances;
    }

    public ArrayList<Pair<Double, Integer>> sortByDistance(int startVertexId) {

        var distances = dijkstra(startVertexId);
        ArrayList<Pair<Double, Integer>> arr = new ArrayList<>();

        for(Map.Entry<Integer, Double> entry : distances.entrySet()) {
            Integer vertexId = entry.getKey();
            Double distance = entry.getValue();

            arr.add(new Pair<Double, Integer>(distance, vertexId));
        }

        Collections.sort(arr);

        return arr;
    }
}