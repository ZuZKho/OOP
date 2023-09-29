import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.Iterator;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


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

    void test2() {
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

        assertNotEquals(tree1, tree2);
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

    @Test
    void testSingleton() {
        Tree<Integer> tree1 = new Tree<>(1);
        Tree<Integer> tree2 = new Tree<>(1);

        assertEquals(tree1, tree2);
    }

    @Test
    void testEmpty1() {
        Tree<Integer> tree1 = new Tree<>(1);
        tree1.remove();
        Tree<Integer> tree2 = new Tree<>(1);

        assertNotEquals(tree1, tree2);
    }

    @Test
    void testEmpty2() {
        Tree<Integer> tree1 = new Tree<>(1);
        tree1.remove();
        Tree<Integer> tree2 = new Tree<>(1);
        tree2.remove();

        assertEquals(tree1, tree2);
    }

    @Test
    void testDfsBamboo() {
        ArrayList<Integer> expected = new ArrayList<>();
        expected.add(0);

        final int vertexCnt = 5000;
        Tree<Integer> tree = new Tree<>(0);
        Tree<Integer> buf = tree;

        for (int i = 1; i < vertexCnt; i++) {
            buf = buf.addChild(i);
            expected.add(i);
        }

        int j = 0;
        for (Iterator<Integer> it = tree.iterator(); it.hasNext(); ) {
            var vertex = it.next();
            assertEquals(vertex, expected.get(j));
            j++;
        }
    }

    @Test
    void testDfsEmpty() {
        Tree<String> tree = new Tree<>("empty");
        tree.remove();

        for (Iterator<?> it = tree.iterator(); it.hasNext(); ) {
            fail();
        }
    }

    @Test
    void testBfsEmpty() {
        Tree<String> tree = new Tree<>("empty");
        tree.remove();

        for (Iterator<?> it = tree.iterator(); it.hasNext(); ) {
            fail();
        }
    }

    @Test
    void testBfsBamboo() {
        ArrayList<Integer> expected = new ArrayList<>();
        expected.add(0);

        final int vertexCnt = 5000;
        Tree<Integer> tree = new Tree<>(0);
        Tree<Integer> buf = tree;

        for (int i = 1; i < vertexCnt; i++) {
            buf = buf.addChild(i);
            expected.add(i);
        }

        int j = 0;
        for (Iterator<Integer> it = tree.iteratorBfs(); it.hasNext(); ) {
            var vertex = it.next();
            assertEquals(vertex, expected.get(j));
            j++;
        }
    }


    @Test
    public void concurrentModificationBfs() {
        Tree<String> tree = new Tree<>("R1");
        var a1 = tree.addChild("A");
        a1.addChild("B");
        Tree<String> subtree = new Tree<>("R2");
        subtree.addChild("C");
        subtree.addChild("D");
        tree.addChild(subtree);

        Exception exception = assertThrows(ConcurrentModificationException.class, () -> {
            for (Iterator<String> it = tree.iteratorBfs(); it.hasNext(); ) {
                it.remove();
            }
        });
    }

    @Test
    public void concurrentModificationDfs() {
        Tree<String> tree = new Tree<>("R1");
        var a1 = tree.addChild("A");
        a1.addChild("B");
        Tree<String> subtree = new Tree<>("R2");
        subtree.addChild("C");
        subtree.addChild("D");
        tree.addChild(subtree);

        Exception exception = assertThrows(ConcurrentModificationException.class, () -> {
            for (Iterator<String> it = tree.iterator(); it.hasNext(); ) {
                it.remove();
            }
        });
    }

    /**
     * Changes внутри обхода.
     */
    @Test
    public void concurrentModificationDfsMultithread1() {
        Tree<String> tree = new Tree<>("R1");
        var a1 = tree.addChild("A");
        a1.addChild("B");
        Tree<String> subtree = new Tree<>("R2");
        subtree.addChild("C");
        subtree.addChild("D");
        tree.addChild(subtree);
        Iterator<?> it = tree.iterator();

        Exception exception = assertThrows(ConcurrentModificationException.class, () -> {
            a1.remove();
            while (it.hasNext()) {
                it.next();
            }
        });
    }

    /**
     * Changes выше по дереву
     */
    @Test
    public void concurrentModificationDfsMultithread2() {
        Tree<String> tree = new Tree<>("R1");
        var a1 = tree.addChild("A");
        a1.addChild("B");
        Tree<String> subtree = new Tree<>("R2");
        subtree.addChild("C");
        subtree.addChild("D");
        tree.addChild(subtree);
        Iterator<?> it = a1.iterator();

        assertDoesNotThrow(() -> {
            tree.addChild("228");
            while (it.hasNext()) {
                it.next();
            }
        });
    }

    /**
     * Changes в соседних поддеревьях.
     */
    @Test
    public void concurrentModificationDfsMultithread3() {
        Tree<String> tree = new Tree<>("R1");
        var a1 = tree.addChild("A");
        a1.addChild("B");
        Tree<String> subtree = new Tree<>("R2");
        subtree.addChild("C");
        subtree.addChild("D");
        tree.addChild(subtree);
        Iterator<?> it = a1.iterator();

        assertDoesNotThrow(() -> {
            subtree.addChild("228");
            while (it.hasNext()) {
                it.next();
            }
        });
    }

    @Test
    public void concurrentModificationBfsMultithread1() {
        Tree<String> tree = new Tree<>("R1");
        var a1 = tree.addChild("A");
        a1.addChild("B");
        Tree<String> subtree = new Tree<>("R2");
        subtree.addChild("C");
        subtree.addChild("D");
        tree.addChild(subtree);
        Iterator<?> it = tree.iteratorBfs();

        Exception exception = assertThrows(ConcurrentModificationException.class, () -> {
            a1.remove();
            while (it.hasNext()) {
                it.next();
            }
        });
    }

    /**
     * Changes выше по дереву.
     */
    @Test
    public void concurrentModificationBfsMultithread2() {
        Tree<String> tree = new Tree<>("R1");
        var a1 = tree.addChild("A");
        a1.addChild("B");
        Tree<String> subtree = new Tree<>("R2");
        subtree.addChild("C");
        subtree.addChild("D");
        tree.addChild(subtree);
        Iterator<?> it = a1.iteratorBfs();

        assertDoesNotThrow(() -> {
            tree.addChild("228");
            while (it.hasNext()) {
                it.next();
            }
        });
    }

    /**
     * Changes в соседних поддеревьях.
     */
    @Test
    public void concurrentModificationBfsMultithread3() {
        Tree<String> tree = new Tree<>("R1");
        var a1 = tree.addChild("A");
        a1.addChild("B");
        Tree<String> subtree = new Tree<>("R2");
        subtree.addChild("C");
        subtree.addChild("D");
        tree.addChild(subtree);
        Iterator<?> it = a1.iteratorBfs();

        assertDoesNotThrow(() -> {
            subtree.addChild("228");
            while (it.hasNext()) {
                it.next();
            }
        });
    }

}