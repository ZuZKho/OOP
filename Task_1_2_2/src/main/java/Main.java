import java.util.ArrayList;

//public class Main {
//
//    static public void main(String[] args) {
//
//        AdjacencyMatrixGraph<String, Integer> g = new AdjacencyMatrixGraph<>();
//
//        Integer[] vertices = new Integer[7];
//        for(int i = 0; i < 7; i++) {
//            vertices[i] = g.addVertex("A");
//        }
//
//        g.addEdge(5, vertices[0], vertices[1]);
//        g.addEdge(12, vertices[0], vertices[3]);
//        g.addEdge(25, vertices[0], vertices[6]);
//
//        g.addEdge(5, vertices[1], vertices[0]);
//        g.addEdge(8, vertices[1], vertices[3]);
//
//        g.addEdge(2, vertices[2], vertices[3]);
//        g.addEdge(4, vertices[2], vertices[4]);
//        g.addEdge(5, vertices[2], vertices[5]);
//        g.addEdge(10, vertices[2], vertices[6]);
//
//        g.addEdge(12, vertices[3], vertices[0]);
//        g.addEdge(8, vertices[3], vertices[1]);
//        g.addEdge(2, vertices[3], vertices[2]);
//
//        g.addEdge(4, vertices[4], vertices[2]);
//        g.addEdge(5, vertices[4], vertices[6]);
//
//        g.addEdge(5, vertices[5], vertices[2]);
//        g.addEdge(5, vertices[5], vertices[6]);
//
//        g.addEdge(25, vertices[6], vertices[0]);
//        g.addEdge(10, vertices[6], vertices[2]);
//        g.addEdge(5, vertices[6], vertices[4]);
//        g.addEdge(5, vertices[6], vertices[5]);
//
//        Pair<Integer, Integer> p;
//
//        ArrayList< Pair<Double, Integer> > ans = g.sortByDistance(vertices[2]);
//
//        /*for(int i = 0; i < ans.size(); i++) {
//            System.out.println(ans.get(i).first);
//        }*/
//    }
//}
