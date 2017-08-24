import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class AESEncrypterTest {
    private static final String[] TESTS = { "@this%is&pASSWord",
            "1234567890abcdefgh", "!@#$%^&*()_+", ",.<>?][{}-" };
    private static final String[] RESULTS = {
            "Ag5oqPx+rU6Z7ZVd3kqBwWPkL2QMRM6LJfxAtRFnii8=",
            "k5T9sOUTSDR1NbkjXWK7Xp3MsxDlomLfTtYvYZh6I+I=",
            "BGagzbjemFu0XRnINiiuwA==", "Wc1vma6dp9ziy0XkZuJUuw==" };

    @Test
    public void test() {
        AESEncrypter worker = new AESEncrypter();

        int count = 0;
        for (String str : TESTS) {
            String res = worker.encrypt(str);
            String recovered = worker.decrypt(res);
            assertTrue(str.equals(recovered));
            System.out.println(res);

            /* Make sure no one change the AESEncrypter configuration, or
            upgrade javax.crypto version without checking the version compatibility
            */
            assertTrue(res.equals(RESULTS[count]));
            count++;
        }

    }
}
