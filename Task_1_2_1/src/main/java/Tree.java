import java.util.*;

/**
 * Tree Class.
 *
 * @param <T> vertex value type
 */
public class Tree<T> implements Iterable<T> {
    private T value;
    private Tree<T> parent;
    private Map<T, Tree<T>> children = new HashMap<>();

    Tree(T value) {
        this.value = value;
        this.parent = null;
    }

    /**
     * Add new child with value.
     *
     * @param value value of new vertex
     * @return new child node
     */
    public Tree<T> addChild(T value) {
        Tree<T> newChild = new Tree<>(value);
        newChild.parent = this;
        Tree<T> old = this.children.put(value, newChild);
        if (old != null) {
            System.out.println("Повторяющееся значение вершины, старая вершина была изменена.");
        }
        return newChild;
    }

    /**
     * Add subtree.
     *
     * @param newChild subtree
     */
    public void addChild(Tree<T> newChild) {
        newChild.parent = this;
        Tree<T> old = this.children.put(newChild.value, newChild);
        if (old != null) {
            System.out.println("Повторяющееся значение вершины, старая вершина была изменена.");
        }
    }

    /**
     * Remove subtree.
     */
    public void remove() {
        if (this.parent != null) {
            this.parent.children.remove(this.value);
        }
        this.value = null;
        this.children.clear();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Tree)) {
            return false;
        }

        if (this.value != ((Tree<?>) obj).value) {
            return false;
        }
        for (var entry : this.children.entrySet()) {
            T key = entry.getKey();
            Tree<T> firstSubtree = entry.getValue();

            if (!((Tree<?>) obj).children.containsKey(key)) {
                return false;
            }

            if (!firstSubtree.equals(((Tree<?>) obj).children.get(key))) {
                return false;
            }
        }
        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value, children);
    }

    /**
     * Iterator по-умолчанию. Выполняет функции обхода в глубину.
     *
     * @return dfs iterator
     */
    @Override
    public Iterator<T> iterator() {
        return new DfsIterator<T>(this);
    }

    /**
     * Iterator выполняющий обход в ширину.
     *
     * @return bfs iterator
     */
    public Iterator<T> iteratorBfs() {
        return new BfsIterator<>(this);
    }

    private class DfsIterator<T> implements Iterator<T> {
        private Set<T> used = new HashSet<>();
        private Tree<T> current;

        DfsIterator(Tree<T> root) {
            if (root.value != null) {
                this.current = root;
                used.add(this.current.value);
            } else {
                this.current = null;
            }
        }

        // Тесты с пустым деревом и единичным
        private Tree<T> getNext(Tree<T> root) {
            for (var entry : root.children.entrySet()) {
                T key = entry.getKey();
                if (!used.contains(key)) {
                    return entry.getValue();
                }
            }
            if (root.parent != null) {
                return getNext(root.parent);
            }
            return null;
        }

        @Override
        public boolean hasNext() {
            return this.current != null;
        }

        @Override
        public T next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            T toReturn = this.current.value;

            this.current = getNext(this.current);
            if (this.current != null) {
                used.add(this.current.value);
            }

            return toReturn;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    private class BfsIterator<T> implements Iterator<T> {
        private ArrayDeque<Tree<T>> queue = new ArrayDeque<>();

        BfsIterator(Tree<T> root) {
            if (root.value != null) {
                queue.addLast(root);
            }
        }

        private void updateQueue(Tree<T> root) {
            for (var entry : root.children.entrySet()) {
                queue.addLast(entry.getValue());
            }
        }

        @Override
        public boolean hasNext() {
            return !queue.isEmpty();
        }

        @Override
        public T next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            Tree<T> toReturn = queue.pollFirst();
            updateQueue(toReturn);
            return toReturn.value;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }
}
