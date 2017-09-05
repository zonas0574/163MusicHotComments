package zonas;

import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

public class App {
    private static int maxThread = 4;
    private static ConcurrentLinkedQueue<String> albumLQueue = new ConcurrentLinkedQueue<String>();
    private static ConcurrentLinkedQueue<String> songList = new ConcurrentLinkedQueue<String>();
    private static AtomicInteger threadNumber = new AtomicInteger(1);
    private static ExecutorService aq = Executors.newFixedThreadPool(maxThread, new ThreadFactory() {
        public Thread newThread(Runnable r) {
            return new Thread(r, "SongList-" + threadNumber.getAndIncrement());
        }
    });

    public static void main(String[] args) {
        String singerId = MusicAPI.singer("周杰伦");
        List<String> albumList = MusicAPI.album(singerId);
        albumLQueue.addAll(albumList);
        for (int i = 0; i < maxThread; i++) {
            aq.submit(new Comment());
        }
        while (!aq.isTerminated()) ;
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
