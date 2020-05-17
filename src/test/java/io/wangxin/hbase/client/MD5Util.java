package io.wangxin.hbase.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * <p>MD5 工具</p>
 */
public class MD5Util {

    private static final Logger LOG = LoggerFactory.getLogger(MD5Util.class);
    private static final char[] hexDigits = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    private MD5Util() {
    }

    /**
     * get md5 str
     *
     * @param message
     * @return
     */
    public static String getMD5(String message) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] b = md.digest(message.getBytes("UTF-8"));
            return byteToHexString(b);
        } catch (UnsupportedEncodingException e) {
            LOG.error("get md5 error!", e);
            return null;
        } catch (NoSuchAlgorithmException e) {
            LOG.error("get md5 error!", e);
            return null;
        }
    }

    /**
     * get md5 str
     *
     * @param bytes
     * @return
     */
    public static String getMD5(byte[] bytes) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(bytes);
            return byteToHexString(md.digest());
        } catch (NoSuchAlgorithmException e) {
            LOG.error("get md5 error!", e);
            return null;
        }
    }

    /**
     * byte array convert to hex String
     *
     * @param bytes
     * @return
     */
    private static String byteToHexString(byte[] bytes) {
        char[] str = new char[32];
        int k = 0;
        for (int i = 0; i < 16; i++) {
            byte b = bytes[i];
            str[k++] = hexDigits[b >>> 4 & 0xf];
            str[k++] = hexDigits[b & 0xf];
        }
        return new String(str);
    }

}
