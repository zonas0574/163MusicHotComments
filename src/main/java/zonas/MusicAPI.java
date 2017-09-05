package zonas;

import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MusicAPI {
    private MusicAPI() {
    }

    private static Logger log = Logger.getLogger(MusicAPI.class);
    private static String singerId;

    @SuppressWarnings("unchecked")
    public static String singer(String name) {
        String url = Util.HOSTURL + "weapi/search/suggest/web?csrf_token=";
        String param = "{s:'" + name + "', csrf_token:''}";
        String json = Util.getJson(param, url);
        Map<String, Object> map = Util.getMapByJson(json);
        Map<String, Object> result = (Map<String, Object>) map.get("result");
        String singerId = ((Map<String, Object>) ((List<Object>) result.get("artists")).get(0)).get("id").toString();
        String singId = singerId.substring(0, singerId.length() - 2);
        log.info(name + ":singerId ----> " + singId);
        return singId;
    }

    public static List<String> album(String id) {
        List<String> albumList = new ArrayList<String>();
        singerId = id;
        try {
            String url = Util.HOSTURL + "artist/album?id=" + singerId + "&limit=50";
            Document doc = Jsoup.connect(url).get();
            Elements links = doc.select("a[href].tit");
            for (Element link : links) {
                albumList.add(link.attr("href").substring(10));
            }
        } catch (IOException e) {
            log.debug(e);
        }
        log.info("AlbumId List: " + albumList);
        return albumList;
    }

    public static List<String> song(String albumId) {
        List<String> songList = new ArrayList<String>();
        if (albumId == null) {
            return songList;
        }
        try {
            String url = Util.HOSTURL + "album?id=" + albumId;
            Document doc = Jsoup.connect(url).get();
            Elements links = doc.select("ul.f-hide a");
            for (Element link : links) {
                songList.add(link.attr("href").substring(9));
            }
        } catch (IOException e) {
            log.debug(e);
        }
        log.info(albumId + " : " + songList);
        return songList;
    }

    @SuppressWarnings("unchecked")
    public static List<Comments> comments(String songId) {
        List<Comments> list = new ArrayList<Comments>();
        String url = Util.HOSTURL + "weapi/v1/resource/comments/R_SO_4_" + songId + "?csrf_token=";
        String param = "{rid:'', offset:'0', total:'true', limit:'20', csrf_token:''}";
        String json = Util.getJson(param, url);
        Map<String, Object> map = Util.getMapByJson(json);
        List<Object> comments = (List<Object>) map.get("hotComments");
        Comments com;
        BigDecimal bd;
        for (Object o : comments) {
            Map<String, Object> tmp = (Map<String, Object>) o;
            com = new Comments();
            com.setSongId(songId);
            com.setContent(tmp.get("content").toString());
            com.setLikedCount(tmp.get("likedCount").toString().substring(0, tmp.get("likedCount").toString().length() - 2));
            bd = new BigDecimal(tmp.get("time").toString());
            com.setTime(bd.toPlainString());
            bd = new BigDecimal(((Map<String, Object>) tmp.get("user")).get("userId").toString());
            if (bd.toPlainString().contains(".")) {
                com.setUserId(bd.toPlainString().substring(0, bd.toPlainString().length() - 2));
            } else {
                com.setUserId(bd.toPlainString());
            }
            com.setSingerId(singerId);
            list.add(com);
        }
        if (!list.isEmpty()) {
            log.info("----> 获取" + songId + "的热门评论成功!");
        } else {
            log.info("----> 获取" + songId + "的热门评论失败!");
        }
        return list;
    }
}
