import java.util.Scanner;

public class Affine {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        int[] key = new int[2];
         key[0] = input.nextInt();
         key[1] = input.nextInt();
        String plainText = input.next();
        String cipherText = encode(plainText, key);
        System.out.println(cipherText);
    }

    public static int inverse(int x) {
        int temp;
        int[] s = new int[2];
        int[] r = new int[2];
        s[0] = 1;
        r[0] = x;
        r[1] = 26;
        while (r[1] != 0) {
            //s list iteration
            temp = s[1];
            s[1] = s[0] - r[0] / r[1] * s[1];
            s[0] = temp;
            //r list iteration
            temp = r[1];
            r[1] = r[0] % r[1];
            r[0] = temp;
        }
        return s[0];
    }
    
    public static String encode(String plainText, int[] key) {
        int[] codePoints = plainText.codePoints().toArray();
        for (int i = 0; i < codePoints.length; i++) {
            if (Character.isLowerCase(codePoints[i])) {
                codePoints[i] = 'a' + ((codePoints[i] - 'a') * key[0] + key[1]) % 26;
            } else if (Character.isUpperCase(codePoints[i])) {
                codePoints[i] = 'A' + ((codePoints[i] - 'A') * key[0] + key[1]) % 26;
            }
        }
        return new String(codePoints, 0, codePoints.length);
    }

    public static String decode(String cipherText, int[] key) {
        int[] codePoints = cipherText.codePoints().toArray();
        for (int i = 0; i < codePoints.length; i++) {
            if (Character.isLowerCase(codePoints[i])) {
                codePoints[i] = 'a' + (codePoints[i] - 'a' - key[1] + 26)*inverse(key[0]) % 26;
            } else if (Character.isUpperCase(codePoints[i])) {
                codePoints[i] = 'A' + (codePoints[i] - 'A' - key[1] + 26)*inverse(key[0]) % 26;
            }
        }
        return new String(codePoints, 0, codePoints.length);
    }
}
