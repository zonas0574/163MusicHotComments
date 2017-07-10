import java.util.List;

public class App {
    public static void main(String[] args) {
        int totalComments = 0;
        String singerId = MusicAPI.singer("周杰伦");
        List<String> albumList = MusicAPI.album(singerId);
        List<String> songList = MusicAPI.song(albumList);
        for (String songId : songList) {
            List<Comments> list = MusicAPI.comments(songId);
            totalComments += list.size();
            Dao.addComments(list);
        }
        System.out.println("-------------\n共有" + totalComments + "条评论!");
    }
}
