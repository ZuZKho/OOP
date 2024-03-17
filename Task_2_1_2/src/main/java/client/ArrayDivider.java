package client;

import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.Queue;

public class ArrayDivider {

    class Task {
        int id;
        long[] arr;

        private Task(int id, long[] arr) {
            this.id = id;
            this.arr = arr;
        }
    }

    HashMap<Integer, Task> tasksMap = new HashMap<>();
    Queue<Integer> queue = new ArrayDeque<>();

    public ArrayDivider(long[] array, int taskSize) {
        for (int i = 0; i < array.length; i += taskSize) {
            int length = Math.min(taskSize, array.length - i);
            long[] cur = new long[length];
            for (int j = 0; j < length; j++) {
                cur[j] = array[i + j];
            }

            int id = i / taskSize;
            tasksMap.put(id, new Task(id, cur));
            queue.add(id);
        }
    }

    public boolean isFinished() {
        return tasksMap.isEmpty();
    }

    public Task getTask() {
        Integer id = queue.poll();
        if (id == null) {
            return null;
        }

        return tasksMap.get(id);
    }

    public void approveTask(int id) {
        tasksMap.remove(id);
    }

    public void rejectTask(int id) {
        queue.add(id);
    }

}
