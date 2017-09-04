package zonas;

import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class App {
    private static ConcurrentLinkedQueue<String> queue = new ConcurrentLinkedQueue<String>();
    private static ExecutorService es = Executors.newFixedThreadPool(4);

    public static void main(String[] args) {
        String singerId = MusicAPI.singer("周杰伦");
        List<String> albumList = MusicAPI.album(singerId);
        List<String> songList = MusicAPI.song(albumList);
        queue.addAll(songList);
        for (int i = 0; i < 4; i++) {
            es.submit(new Comment());
        }
    }

    static class Comment implements Runnable {
        public void run() {
            synchronized (queue) {
                while (!queue.isEmpty()) {
                    List<Comments> list = MusicAPI.comments(queue.poll());
                    Dao.addComments(list);
                }
                es.shutdown();
            }
        }
    }
}
