
import java.io.UnsupportedEncodingException;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;
public class AESEncrypter {

    /*
     * Do not change following literal
     */
    private static String hardcodedKey = "!@#$q0w9e8r7&t*%";// 16 byte
    private static String HARDCODEDVECTOR = "qwermnbv&^%$!@#$";// 16 byte
    private static String ALGORITHM = "AES";
    private static String CIPHERTOKEN = "AES/CBC/PKCS5Padding";

    private String charSet = "UTF-8";
    private SecretKeySpec secretKey;
    private IvParameterSpec iv;

    public AESEncrypter() {
        try {
            byte[] key = getKey(hardcodedKey, hardcodedKey.length());
            if (key != null) {
                secretKey = new SecretKeySpec(key, ALGORITHM);
                iv = new IvParameterSpec(
                        HARDCODEDVECTOR.getBytes(getCharSet()));
            }
        } catch (UnsupportedEncodingException e) {
            LOG.error("Unsupported Encoding.", e);
        }
    }

    private byte[] getKey(String s, int length)
            throws UnsupportedEncodingException {

        byte[] res = null;
        if (s.length() < length) {
            int padLen = length - s.length();
            for (int i = 0; i < padLen; i++) {
                s += " ";
            }
        }
        res = s.substring(0, length).getBytes(getCharSet());
        return res;
    }

    public String encrypt(String original) {

        String res = null;
        if (secretKey == null) {
            return original;
        }

        try {
            Cipher cipher = null;
            cipher = Cipher.getInstance(CIPHERTOKEN);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, iv);

            byte[] encryptedBytes = process(cipher,
                    original.getBytes(getCharSet()));

            if (encryptedBytes != null) {
                res = Base64.encodeBase64String(encryptedBytes);
            } else {
                res = original;
            }
        } catch (Exception e) {// NOSONAR
            LOG.error("Error when encyrpting string: " + original, e);
        }
        return res;
    }

    private byte[] process(Cipher cipher, byte[] input) {
        byte[] output = null;

        try {
            output = cipher.doFinal(input);
        } catch (IllegalBlockSizeException e) {
            LOG.error("Illegal Block Size Exception", e);
        } catch (BadPaddingException e) {
            LOG.error("No Such Padding Exception.", e);
        }
        return output;

    }

    public String decrypt(String encrypted) {
        String res = null;
        try {
            Cipher cipher = null;
            cipher = Cipher.getInstance(CIPHERTOKEN);

            cipher.init(Cipher.DECRYPT_MODE, this.secretKey, iv);

            byte[] original = process(cipher, Base64.decodeBase64(encrypted));
            res = new String(original);
        } catch (Exception e) {// NOSONAR
            LOG.error("Error when decrypting string: " + encrypted, e);
        }
        return res;
    }

    public String getCharSet() {
        return charSet;
    }

    public void setCharSet(String charSet) {
        this.charSet = charSet;
    }

}
