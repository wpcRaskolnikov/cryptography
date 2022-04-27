import java.util.Scanner;

public class Vigenere {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
    }

    public static String encode(String plainText, String key) {
        int[] codePoints = plainText.codePoints().toArray();
        int[] keyCodes = key.codePoints().toArray();
        int length = key.length();
        for (int i = 0; i < codePoints.length; i++) {
            if (Character.isLowerCase(codePoints[i])) {
                codePoints[i] = 'a' + ((codePoints[i] - 'a') + (keyCodes[i % length] - 'a')) % 26;
            } else if (Character.isUpperCase(codePoints[i])) {
                codePoints[i] = 'A' + ((codePoints[i] - 'A') + (keyCodes[i % length] - 'A')) % 26;
            }
        }
        return new String(codePoints, 0, codePoints.length);
    }

    public static String decode(String cipherText, String key) {
        int[] codePoints = cipherText.codePoints().toArray();
        int[] keyCodes = key.codePoints().toArray();
        int length = key.length();
        for (int i = 0; i < codePoints.length; i++) {
            if (Character.isLowerCase(codePoints[i])) {
                codePoints[i] = 'a' + ((codePoints[i] - 'a') - (keyCodes[i % length] - 'a') + 26) % 26;
            } else if (Character.isUpperCase(codePoints[i])) {
                codePoints[i] = 'A' + ((codePoints[i] - 'A') - (keyCodes[i % length] - 'A') + 26) % 26;
            }
        }
        return new String(codePoints, 0, codePoints.length);
    }
}
