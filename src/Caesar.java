import java.util.Scanner;

public class Caesar {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        int key = input.nextInt();
        String plainText = input.next();
        String cipherText = encode(plainText, key);
        System.out.println(cipherText);
    }

    public static String decode(String cipherText, int key) {
        int[] codePoints = cipherText.codePoints().toArray();
        for (int i = 0; i < codePoints.length; i++) {
            if (Character.isLowerCase(codePoints[i])) {
                codePoints[i] = 'a' + (codePoints[i] - 'a' - key + 26) % 26;
            } else if (Character.isUpperCase(codePoints[i])) {
                codePoints[i] = 'A' + (codePoints[i] - 'A' - key + 26) % 26;
            }
        }
        return new String(codePoints, 0, codePoints.length);
    }

    public static String encode(String plainText, int key) {
        int[] codePoints = plainText.codePoints().toArray();
        for (int i = 0; i < codePoints.length; i++) {
            if (Character.isLowerCase(codePoints[i])) {
                codePoints[i] = 'a' + (codePoints[i] - 'a' + key) % 26;
            } else if (Character.isUpperCase(codePoints[i])) {
                codePoints[i] = 'A' + (codePoints[i] - 'A' + key ) % 26;
            }
        }
        return new String(codePoints, 0, codePoints.length);
    }

}
