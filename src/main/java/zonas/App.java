package zonas;

import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class App {
    private static ConcurrentLinkedQueue<String> albumLQueue = new ConcurrentLinkedQueue<String>();
    private static ConcurrentLinkedQueue<String> songList = new ConcurrentLinkedQueue<String>();
    private static ExecutorService aq = Executors.newFixedThreadPool(4);

    public static void main(String[] args) {
        String singerId = MusicAPI.singer("周杰伦");
        List<String> albumList = MusicAPI.album(singerId);
        albumLQueue.addAll(albumList);

        for (int i = 0; i < 4; i++) {
            aq.submit(new Comment());
        }
        while (!aq.isTerminated())
            for (String songId : songList) {
                Dao.addComments(MusicAPI.comments(songId));
            }

    }

    static class Comment implements Runnable {
        public void run() {
            while (!albumLQueue.isEmpty()) {
                songList.addAll(MusicAPI.song(albumLQueue.poll()));
            }
            aq.shutdown();
        }
    }
}
