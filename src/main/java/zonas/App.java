package zonas;

import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

public class App {
    private static ConcurrentLinkedQueue<String> albumLQueue = new ConcurrentLinkedQueue<String>();
    private static ConcurrentLinkedQueue<String> songList = new ConcurrentLinkedQueue<String>();
    private static AtomicInteger threadNumber = new AtomicInteger(1);
    private static ExecutorService aq = null;

    public static void main(String[] args) {
        String singer = "周杰伦";
        Scanner sc = new Scanner(System.in);
        System.out.print("请输入歌手名（周杰伦）：");
        String temp = sc.nextLine();
        singer = ("".equals(temp) ? singer : temp);
        System.out.print("请输入最大线程数（4）：");
        int maxThreads = sc.nextInt();
        aq = Executors.newFixedThreadPool(maxThreads, new ThreadFactory() {
            public Thread newThread(Runnable r) {
                return new Thread(r, "SongList-" + threadNumber.getAndIncrement());
            }
        });
        String singerId = MusicAPI.singer(singer);
        List<String> albumList = MusicAPI.album(singerId);
        albumLQueue.addAll(albumList);
        for (int i = 0; i < maxThreads; i++) {
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
