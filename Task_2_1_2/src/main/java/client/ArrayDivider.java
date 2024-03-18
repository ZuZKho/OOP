package client;

import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.Queue;

/**
 * Class for dividing long[] to multiple subarrayy and provide api for interaction.
 */
public class ArrayDivider {

    /**
     * Wrapper of long[] subarray.
     */
    class Task {
        int id;
        long[] arr;

        /**
         * Constructor.
         *
         * @param id  of task.
         * @param arr long[] subarray of task.
         */
        private Task(int id, long[] arr) {
            this.id = id;
            this.arr = arr;
        }
    }

    HashMap<Integer, Task> tasksMap = new HashMap<>();
    Queue<Integer> queue = new ArrayDeque<>();

    /**
     * Constructor which divides input array into tasks.
     *
     * @param array    input array.
     * @param taskSize size of subarrays.
     */
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

    /**
     * Is all tasks finished.
     *
     * @return is all tasks finished.
     */
    public boolean isFinished() {
        return tasksMap.isEmpty();
    }

    /**
     * Get not finished and not currently processing task.
     *
     * @return task or null.
     */
    public Task getTask() {
        Integer id = queue.poll();
        if (id == null) {
            return null;
        }

        return tasksMap.get(id);
    }

    /**
     * Approve that task was finished.
     *
     * @param id id of task that was finished.
     */
    public void approveTask(int id) {
        tasksMap.remove(id);
    }

    /**
     * Say that previously taken task wasn't finished and need to repeat it.
     *
     * @param id task id.
     */
    public void rejectTask(int id) {
        queue.add(id);
    }

}
