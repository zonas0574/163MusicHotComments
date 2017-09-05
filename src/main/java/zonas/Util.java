package zonas;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import net.dongliu.requests.Requests;
import org.apache.commons.codec.binary.Base64;
import org.apache.log4j.Logger;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.util.HashMap;
import java.util.Map;

public class Util {
    private Util() {
    }

    private static Logger log = Logger.getLogger(Util.class);
    public static final String HOSTURL = "http://music.163.com/";

    private static String aesEncrypt(String text, byte[] key, byte[] iv) {
        String encryptText = "";
        try {
            SecretKeySpec keySpec = new SecretKeySpec(key, "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, keySpec, new IvParameterSpec(iv));
            byte[] cipherData = cipher.doFinal(text.getBytes("UTF-8"));
            encryptText = new Base64().encodeAsString(cipherData);
        } catch (GeneralSecurityException e) {
            log.debug(e);
        } catch (UnsupportedEncodingException e) {
            log.debug(e);
        }
        return encryptText;
    }

    private static String getParams(String param) {
        byte[] forthParam = {'0', 'C', 'o', 'J', 'U', 'm', '6', 'Q', 'y', 'w', '8', 'W', '8', 'j', 'u', 'd'};
        byte[] iv = {'0', '1', '0', '2', '0', '3', '0', '4', '0', '5', '0', '6', '0', '7', '0', '8'};
        byte[] secondKey = {'F', 'F', 'F', 'F', 'F', 'F', 'F', 'F', 'F', 'F', 'F', 'F', 'F', 'F', 'F', 'F'};
        String encText;
        encText = aesEncrypt(param, forthParam, iv);
        encText = aesEncrypt(encText, secondKey, iv);
        return encText;
    }

    public static String getJson(String params, String url) {
        String encSecKey = "257348aecb5e556c066de214e531faadd1c55d814f9be95fd06d6bff9f4c7a41f831f6394d5a3fd2e3881736d94a02ca919d952872e7d0a50ebfa1769a7a62d512f5f1ca21aec60bc3819a9c3ffca5eca9a0dba6d6f7249b06f5965ecfff3695b54e1c28f3f624750ed39e7de08fc8493242e26dbc4484a01c76f739e135637c";
        Map<String, String> data = new HashMap<String, String>();
        data.put("params", getParams(params));
        data.put("encSecKey", encSecKey);
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("Accept", "*/*");
        headers.put("Accept-Encoding", "gzip, deflate");
        headers.put("Accept-Language", "zh-CN,zh;q=0.8");
        headers.put("Connection", "keep-alive");
        headers.put("Content-Type", "application/x-www-form-urlencoded");
        headers.put("Host", "music.163.com");
        headers.put("Origin", HOSTURL);
        headers.put("Referer", HOSTURL);
        headers.put("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_5) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36");
        return Requests.post(url).headers(headers).forms(data).send().readToText();
    }

    public static Map<String, Object> getMapByJson(String json) {
        Gson g = new Gson();
        return g.fromJson(json, new TypeToken<Map<String, Object>>() {
        }.getType());
    }
}
