

import java.util.HashMap;
import java.util.concurrent.*;

/**
 * 延迟队列的一次应用
 * 待改进：占用一个线程，若有任务则处理，无任务则阻塞，效率不高
 *
 * @since 2020-12-16
 */
public class DelayTaskManager {

    private static final String TAG = "DelayTaskManager";

    /**
     * 延迟队列
     */
    private final DelayQueue<DelayOrderTask> queue = new DelayQueue<>();

    private final HashMap<String, DelayOrderTask> taskRepo = new HashMap<>();

    private ExecutorService executor = Executors.newSingleThreadExecutor();


    public DelayTaskManager() {


        // 创建子线程，开始在 queue 中循环取任务执行
        executor.execute(new Runnable() {
            @Override
            public void run() {
                while (!Thread.interrupted()) {
                    try {
                        // TODO 如果队列中没有task，此处会阻塞。
                        System.out.println("开始阻塞....");
                        DelayOrderTask task = queue.take();
                        System.out.println("成功获取task....");

                        // TODO 获取 callback 触发回调
                        DelayTaskCallback callback = task.getCallback();
                        if (callback != null) {
                            callback.onTimeUp(task.taskId);
                        }

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                System.err.println("executor shutdown...");


            }
        });
    }


    /**
     * 发生超时时调用此方法
     *
     * @param taskId   任务id
     * @param delay    延迟时间，ms
     * @param callback 任务到期或者取消的回调
     */
    public void add(String taskId, int delay, DelayTaskCallback callback) {
        System.out.println("trigger...taskId=" + taskId + " delay=" + delay);

        DelayOrderTask task = new DelayOrderTask(taskId, delay, callback);

        // 添加定时任务
        queue.add(task);
        taskRepo.put(taskId, task);

    }

    /**
     * 重连成功时调用此处
     */
    public void cancel(String taskId) {
        if (!taskRepo.containsKey(taskId)) {
            return;
        }
        DelayOrderTask task = taskRepo.get(taskId);
        taskRepo.remove(taskId);
        queue.remove(task);
        DelayTaskCallback callback = task.callback;
        if (callback != null) {
            callback.onCancel(taskId);
        }
    }

    /**
     * TODO 终止任务，并停止线程
     * 注意区别 shutdown 与 shutdownNow 的区别
     */
    public void destroy() {
        queue.clear();
        if (executor != null) {
            // 阻止接受新的任务;然后等待当前正在执行的任务执行完
            // executor.shutdown();
            // 如果有阻塞则需要调用executor.shutdownNow()强制结束
            executor.shutdownNow();
            executor = null;
        }
    }

    /**
     * 回调
     */
    public interface DelayTaskCallback {
        /**
         * 延时任务到期
         */
        void onTimeUp(String taskId);

        /**
         * 中途取消
         */
        void onCancel(String taskId);

    }


    /**
     * 延迟task
     */
    private static final class DelayOrderTask implements Delayed {
        public final String taskId;
        public final long delay;
        private final long time;
        public final DelayTaskCallback callback;


        /**
         * 创建一个延迟任务
         *
         * @param delay    指定时间后超时（ms）
         * @param callback 任务
         */
        public DelayOrderTask(String taskId, long delay, DelayTaskCallback callback) {
            this.taskId = taskId;
            this.delay = delay;
            this.time = System.currentTimeMillis() + delay;
            this.callback = callback;
        }

        @Override
        public int compareTo(Delayed o) {
            // TODO Auto-generated method stub
            DelayOrderTask other = (DelayOrderTask) o;
            long diff = time - other.time;
            if (diff > 0) {
                return 1;
            } else if (diff < 0) {
                return -1;
            } else {
                return 0;
            }
        }

        @Override
        public long getDelay(TimeUnit unit) {
            // TODO Auto-generated method stub
            return unit.convert(this.time - System.currentTimeMillis(), TimeUnit.MILLISECONDS);
        }

        public DelayTaskCallback getCallback() {
            return this.callback;
        }

    }


    public static void main(String[] args) throws ExecutionException, InterruptedException {

        DelayTaskManager manager = new DelayTaskManager();


        manager.add("456", 6000, new DelayTaskManager.DelayTaskCallback() {
            @Override
            public void onTimeUp(String taskId) {
                System.out.println("onTimeUp = " + taskId);
                while (true) {
                    System.out.print(".");
                }
            }

            @Override
            public void onCancel(String taskId) {
                System.out.println("onCancel = " + taskId);

            }
        });
        System.out.println("add 456");


        manager.add("789", 9000, new DelayTaskManager.DelayTaskCallback() {
            @Override
            public void onTimeUp(String taskId) {
                System.out.println("onTimeUp = " + taskId);
            }

            @Override
            public void onCancel(String taskId) {
                System.out.println("onCancel = " + taskId);

            }
        });
        System.out.println("add 789");

        manager.add("123", 3000, new DelayTaskManager.DelayTaskCallback() {
            @Override
            public void onTimeUp(String taskId) {
                System.out.println("onTimeUp = " + taskId);
            }

            @Override
            public void onCancel(String taskId) {
                System.out.println("onCancel = " + taskId);

            }
        });
        System.out.println("add 123");


        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(7000);
                    System.out.println("destroy manager");
                    manager.destroy();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();


    }


}
