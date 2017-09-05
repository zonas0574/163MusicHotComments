package zonas;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.apache.commons.codec.binary.Base64;
import org.apache.log4j.Logger;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.util.Map;

public class Util {
    private Util() {
    }

    private static Logger log = Logger.getLogger(Util.class);

    public static String aesEncrypt(String text, byte[] key, byte[] iv) {
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

    public static Map<String, Object> getMapByJson(String json) {
        Gson g = new Gson();
        return g.fromJson(json, new TypeToken<Map<String, Object>>() {
        }.getType());
    }
}
