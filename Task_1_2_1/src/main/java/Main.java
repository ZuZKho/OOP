import java.util.Iterator;

public class Main {

    static public void main(String[] args) {
        Tree<String> tree = new Tree<>("R1");
        var a = tree.addChild("A");
        var b = a.addChild("B");
        Tree<String> subtree = new Tree<>("R2");
        subtree.addChild("C");
        subtree.addChild("D");
        tree.addChild(subtree);
        System.out.println("help");

        for (Iterator<String> it1 = tree.iteratorBfs(); it1.hasNext();) {
            String s = it1.next();

            if (s != null)
                System.out.println(s);
        }
    }
}
