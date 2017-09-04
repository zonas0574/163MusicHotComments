package zonas;

import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class Dao {
    private Dao() {
    }

    private static Logger log = Logger.getLogger(Dao.class);
    private static Connection conn;
    private static PreparedStatement ps;

    private static void getConnection() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:sqlite:Music163Comments.db");
        } catch (ClassNotFoundException e) {
            log.debug(e);
        } catch (SQLException e) {
            log.debug(e);
        }
    }

    public static void addComments(List<Comments> list) {
        int result;
        for (int i = 0; i < list.size(); i++) {
            Comments comments = list.get(i);
            result = addComment(comments);
            if (result == 1) {
                log.info("<---- 储存" + comments.getSongId() + "第" + ((i + 1) < 10 ? "0" + (i + 1) : (i + 1)) + "条数据成功！");
            } else {
                log.info("<---- 储存" + comments.getSongId() + "第" + ((i + 1) < 10 ? "0" + (i + 1) : (i + 1)) + "条数据失败！");
            }
        }
    }

    public static int addComment(Comments comments) {
        getConnection();
        int result = 0;
        try {
            String sql = "INSERT INTO hotComments(songId,userId,content,likedCount,times) VALUES(?,?,?,?,?)";
            String[] strings = {comments.getSongId(), comments.getUserId(), comments.getContent(), comments.getLikedCount(), comments.getTime()};
            ps = conn.prepareStatement(sql);
            for (int i = 0; i < strings.length; i++) {
                ps.setString(i + 1, strings[i]);
            }
            result = ps.executeUpdate();
        } catch (SQLException e) {
            log.debug(e);
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
            log.debug(e);
        }
    }
}
