import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class Dao {

    private static Connection conn;
    private static PreparedStatement ps;

    private static void getConnection() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Map<String, String> map = Util.getJdbcProperty();
            conn = DriverManager.getConnection(map.get("url"), map.get("username"), map.get("password"));
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void addComments(List<Comments> list) {
        int result;
        for (int i = 0; i < list.size(); i++) {
            Comments comments = list.get(i);
            result = addComment(comments);
            if (result == 1) {
                System.out.println("<---- 储存" + comments.getSongId() + "第" + ((i + 1) < 10 ? "0" + (i + 1) : (i + 1)) + "条数据成功！");
            } else {
                System.out.println("<---- 储存" + comments.getSongId() + "第" + ((i + 1) < 10 ? "0" + (i + 1) : (i + 1)) + "条数据失败！");
            }
        }
    }

    public static int addComment(Comments comments) {
        getConnection();
        int result = 0;
        try {
            String sql = "INSERT INTO hotComments(songId,userId,content,likedCount,times) VALUES(?,?,?,?,?)";
            String[] strings = {comments.getSongId(), comments.getUserId(), Util.EmojiFilter(comments.getContent()), comments.getLikedCount(), comments.getTime()};
            ps = conn.prepareStatement(sql);
            if (strings != null) {
                for (int i = 0; i < strings.length; i++) {
                    ps.setString(i + 1, strings[i]);
                }
            }
            result = ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e);
        } finally {
            close();
        }
        return result;
    }

    private static void close() {
        try {
            if (ps != null) {
                ps.close();
            }
            if (conn != null) {
                conn.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
