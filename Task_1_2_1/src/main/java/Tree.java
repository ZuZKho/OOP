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

    private int modificationsCount = 0;

    Tree(T value) {
        this.value = value;
        this.parent = null;
    }

    /**
     * Функция для обработки ConcurrentModificationException.
     * При любом изменении поддерева изменяем счетчик всех предков.
     * Таким образом, если мы изменили что-то в поддереве исключение выбросится,
     * а если выше по дереву или в соседних поддеревьях
     *  (это вершины, которые не учавствуют в обходе) не выбросится.
     *
     * @param current текущая нода.
     */
    private void propagateToParents(Tree<T> current) {
        if (current == null || current.parent == null) {
            return;
        }
        current.parent.modificationsCount++;
        propagateToParents(current.parent);
    }

    /**
     * Add new child with value.
     *
     * @param value value of new vertex
     * @return new child node
     */
    public Tree<T> addChild(T value) {
        this.modificationsCount++;
        propagateToParents(this);

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
        this.modificationsCount++;
        propagateToParents(this);

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
        this.modificationsCount++;
        propagateToParents(this);

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

        private T rootValue;
        private Tree<T> current;
        private int modificationsBefore;

        DfsIterator(Tree<T> root) {
            modificationsBefore = modificationsCount;

            if (root.value != null) {
                rootValue = root.value;
                this.current = root;
                this.used.add(this.current.value);
            } else {
                this.current = null;
            }
        }

        private Tree<T> getNext(Tree<T> root) {
            for (var entry : root.children.entrySet()) {
                T key = entry.getKey();
                if (!used.contains(key)) {
                    return entry.getValue();
                }
            }
            if (root.parent != null && root.value != rootValue) {
                return getNext(root.parent);
            }
            return null;
        }

        @Override
        public boolean hasNext() throws ConcurrentModificationException {
            if (modificationsCount != modificationsBefore) {
                throw new ConcurrentModificationException();
            }
            return this.current != null;
        }

        @Override
        public T next() throws NoSuchElementException {
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
        public void remove() throws ConcurrentModificationException {
            throw new ConcurrentModificationException();
        }
    }

    private class BfsIterator<T> implements Iterator<T> {
        private ArrayDeque<Tree<T>> queue = new ArrayDeque<>();
        private int modificationsBefore;

        BfsIterator(Tree<T> root) {
            modificationsBefore = modificationsCount;
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
        public boolean hasNext() throws ConcurrentModificationException {
            if (modificationsCount != modificationsBefore) {
                throw new ConcurrentModificationException();
            }
            return !queue.isEmpty();
        }

        @Override
        public T next() throws NoSuchElementException {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            Tree<T> toReturn = queue.pollFirst();
            updateQueue(toReturn);
            return toReturn.value;
        }

        @Override
        public void remove() throws ConcurrentModificationException {
            throw new ConcurrentModificationException();
        }
    }
}
