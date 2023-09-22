import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.Iterator;
import org.junit.jupiter.api.Test;


class TreeTest {

    @Test
    void test1() {
        Tree<String> tree1 = new Tree<>("R1");
        var a1 = tree1.addChild("A");
        a1.addChild("B");
        Tree<String> subtree = new Tree<>("R2");
        subtree.addChild("C");
        subtree.addChild("D");
        tree1.addChild(subtree);

        Tree<String> tree2 = new Tree<>("R1");
        var r2 = tree2.addChild("R2");
        var a2 = tree2.addChild("A");
        r2.addChild("D");
        a2.addChild("B");
        r2.addChild("C");

        assertEquals(tree1, tree2);
    }


    @Test
    void testRemove() {
        Tree<String> tree1 = new Tree<>("R1");
        var a1 = tree1.addChild("A");
        a1.addChild("B");
        Tree<String> subtree = new Tree<>("R2");
        subtree.addChild("C");
        subtree.addChild("D");
        tree1.addChild(subtree);
        a1.remove();

        Tree<String> tree2 = new Tree<>("R1");
        var r2 = tree2.addChild("R2");
        r2.addChild("D");
        r2.addChild("C");

        assertEquals(tree1, tree2);
    }

    @Test
    void testDfs() {
        Tree<String> tree1 = new Tree<>("R1");
        var a1 = tree1.addChild("A");
        a1.addChild("B");
        Tree<String> subtree = new Tree<>("R2");
        var c = subtree.addChild("C");
        subtree.addChild("D");
        tree1.addChild(subtree);
        c.remove();

        int sz = 0;
        for (Iterator<String> it = tree1.iterator(); it.hasNext(); ) {
            var vertex = it.next();
            sz++;
        }

        assertEquals(sz, 5);
    }

    @Test
    void testBfs() {
        Tree<String> tree1 = new Tree<>("R1");
        var a1 = tree1.addChild("A");
        a1.addChild("B");
        Tree<String> subtree = new Tree<>("R2");
        var c = subtree.addChild("C");
        subtree.addChild("D");
        tree1.addChild(subtree);
        c.remove();

        int sz = 0;
        for (Iterator<String> it = tree1.iteratorBfs(); it.hasNext(); ) {
            var vertex = it.next();
            sz++;
        }

        assertEquals(sz, 5);
    }

}