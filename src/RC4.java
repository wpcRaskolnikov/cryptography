import java.util.Base64;
import java.util.Scanner;

public class RC4 {

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        String key = input.nextLine();
        String plainText = input.nextLine();

        String cipherText = encode(plainText, key);
        String deText = decode(cipherText, key);
        System.out.println(cipherText);
        System.out.println(deText);
    }

    public static int[] initSBox(String key) {
        int temp;
        int keySize = key.length();
        int[] keyCode = key.codePoints().toArray();
        int[] sBox = new int[256];
        for (int i = 0; i < 256; i++) {
            sBox[i] = i;
        }
        for (int i = 0, j = 0; i < 256; i++) {
            j = (j + sBox[i] + keyCode[i % keySize]) % 256;
            temp = sBox[i];
            sBox[i] = sBox[j];
            sBox[j] = temp;
        }
        return sBox;
    }

    public static int[] generateKeyStream(int[] sBox, int length) {
        int temp;
        int i = 0, j = 0;
        int[] keyStream = new int[length];
        for (int k = 0; k < length; k++) {
            i = (i + 1) % 256;
            j = (j + sBox[i]) % 256;
            temp = sBox[i];
            sBox[i] = sBox[j];
            sBox[j] = temp;
            keyStream[k] = sBox[(sBox[i] + sBox[j]) % 256];
        }
        return keyStream;
    }

    public static String decode(String cipherText, String key) {
        byte[] buff = Base64.getDecoder().decode(cipherText);
        int[] cipherCode = new int[buff.length];
        for (int i = 0; i < buff.length; i++) {
            cipherCode[i] = buff[i] & 0xFF;
        }

        int[] sBox = initSBox(key);
        int[] keyStream = generateKeyStream(sBox, cipherText.length());

        int[] plainCode = new int[cipherCode.length];

        for (int i = 0; i < buff.length; i++) {
            plainCode[i] = keyStream[i] ^ cipherCode[i];
        }

        return new String(plainCode, 0, plainCode.length);
    }

    public static String encode(String plainText, String key) {
        int[] sBox = initSBox(key);
        int[] keyStream = generateKeyStream(sBox, plainText.length());

        int[] plainCode = plainText.codePoints().toArray();
        int[] cipherCode = new int[plainCode.length];

        for (int i = 0; i < plainText.length(); i++) {
            cipherCode[i] = keyStream[i] ^ plainCode[i];
        }

        byte[] buff = new byte[cipherCode.length];
        for (int i = 0; i < buff.length; i++) {
            buff[i] = (byte) cipherCode[i];
        }

        return Base64.getEncoder().encodeToString(buff);
    }
}
