import java.util.*;

public class AdjacencyListGraph<V, E extends Number> extends Graph<V, E> implements GraphInterface<V, E> {

    private final HashMap<Integer, ArrayList<Edge<V, E>>> vertexOutEdges;

    public AdjacencyListGraph() {
        this.vertices = new HashMap<>();
        this.edges = new HashMap<>();
        this.vertexOutEdges = new HashMap<>();
    }

    public Vertex<V> addVertex(V value) {
        Vertex<V> newVertex = new Vertex<>(value);
        vertices.put(newVertex.getId(), newVertex);
        vertexOutEdges.put(newVertex.getId(), new ArrayList<>());
        return newVertex;
    }

    public void deleteVertex(Vertex<V> vertex) {
        // Удаляем исходящие ребра
        for (var edge : vertexOutEdges.get(vertex.getId())) {
            edges.remove(edge.getId());
        }

        vertexOutEdges.remove(vertex.getId());

        for (var entry : vertexOutEdges.entrySet()) {

            var iterator = entry.getValue().iterator();
            while (iterator.hasNext()) {
                var current = iterator.next();
                if (current.to.getId() == vertex.getId()) {
                    edges.remove(current.getId()); // Удаляем входящие ребра
                    iterator.remove();
                }
            }
        }

        vertices.remove(vertex.getId());
    }

    public Edge<V, E> addEdge(E value, Vertex<V> from, Vertex<V> to) throws IllegalArgumentException {

        if (!vertexOutEdges.containsKey(from.getId())) {
            throw new IllegalArgumentException("Не существует вершины с таким id.");
        }
        if (!vertexOutEdges.containsKey(to.getId())) {
            throw new IllegalArgumentException("Не существует вершины с таким id.");
        }

        Edge<V, E> newEdge = new Edge<>(value, from, to);
        int fromId = from.getId();

        edges.put(newEdge.getId(), newEdge);
        vertexOutEdges.get(fromId).add(newEdge);
        return newEdge;
    }


    public void deleteEdge(Edge<V, E> edge) {
        int fromId = edge.from.getId();
        vertexOutEdges.get(fromId).remove(edge);
        edges.remove(edge.getId());
    }


    public HashMap<Integer, Double> dijkstra(int startVertexId) {
        int verticesCount = vertexOutEdges.size();

        HashMap<Integer, Double> distances = new HashMap<>();
        HashSet<Integer> used = new HashSet<Integer>();

        distances.put(startVertexId, (double) 0);

        for (int i = 0; i < verticesCount; i++) {

            Double minValue = null;
            Integer minIdx = null;

            for (var entry : vertexOutEdges.entrySet()) {
                Integer vertexId = entry.getKey();
                if (used.contains(vertexId) || !distances.containsKey(vertexId)) {
                    continue;
                }

                if (minValue == null || distances.get(vertexId) < minValue) {
                    minValue = distances.get(vertexId);
                    minIdx = vertexId;
                }
            }

            if (minIdx == null) break;
            used.add(minIdx);

            for (var entry : vertexOutEdges.get(minIdx)) {
                Integer toVertexId = entry.to.getId();
                Double weight = entry.value.doubleValue();

                if (!used.contains(toVertexId) && (!distances.containsKey(toVertexId) || distances.get(toVertexId) > minValue + weight)) {
                    distances.put(toVertexId, minValue + weight);
                }
            }
        }

        return distances;
    }

}
