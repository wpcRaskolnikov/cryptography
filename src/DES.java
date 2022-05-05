import java.util.Arrays;
import java.util.Base64;
import java.util.Collections;

public class DES {

    private static final int[] IP_TABLE = {
            58, 50, 42, 34, 26, 18, 10, 2,
            60, 52, 44, 36, 28, 20, 12, 4,
            62, 54, 46, 38, 30, 22, 14, 6,
            64, 56, 48, 40, 32, 24, 16, 8,
            57, 49, 41, 33, 25, 17, 9, 1,
            59, 51, 43, 35, 27, 19, 11, 3,
            61, 53, 45, 37, 29, 21, 13, 5,
            63, 55, 47, 39, 31, 23, 15, 7};

    private static final int[] IP1_TABLE = {
            40, 8, 48, 16, 56, 24, 64, 32,
            39, 7, 47, 15, 55, 23, 63, 31,
            38, 6, 46, 14, 54, 22, 62, 30,
            37, 5, 45, 13, 53, 21, 61, 29,
            36, 4, 44, 12, 52, 20, 60, 28,
            35, 3, 43, 11, 51, 19, 59, 27,
            34, 2, 42, 10, 50, 18, 58, 26,
            33, 1, 41, 9, 49, 17, 57, 25};

    private static final int[] PC1_TABLE = {
            57, 49, 41, 33, 25, 17, 9, 1, 58, 50, 42, 34, 26, 18, 10, 2,
            59, 51, 43, 35, 27, 19, 11, 3, 60, 52, 44, 36, 63, 55, 47, 39,
            31, 23, 15, 7, 62, 54, 46, 38, 30, 22, 14, 6, 61, 53, 45, 37,
            29, 21, 13, 5, 28, 20, 12, 4};

    private static final int[] PC2_TABLE = {
            14, 17, 11, 24, 1, 5, 3, 28, 15, 6, 21, 10, 23, 19, 12, 4,
            26, 8, 16, 7, 27, 20, 13, 2, 41, 52, 31, 37, 47, 55, 30, 40,
            51, 45, 33, 48, 44, 49, 39, 56, 34, 53, 46, 42, 50, 36, 29, 32};

    private static final int[] EP_TABLE = {
            32, 1, 2, 3, 4, 5,
            4, 5, 6, 7, 8, 9,
            8, 9, 10, 11, 12, 13,
            12, 13, 14, 15, 16, 17,
            16, 17, 18, 19, 20, 21,
            20, 21, 22, 23, 24, 25,
            24, 25, 26, 27, 28, 29,
            28, 29, 30, 31, 32, 1};

    private static final int[] P_TABLE = {
            16, 7, 20, 21,
            29, 12, 28, 17,
            1, 15, 23, 26,
            5, 18, 31, 10,
            2, 8, 24, 14,
            32, 27, 3, 9,
            19, 13, 30, 6,
            22, 11, 4, 25};

    private static final int[][][] S_BOX = {
            {
                    {14, 4, 13, 1, 2, 15, 11, 8, 3, 10, 6, 12, 5, 9, 0, 7},
                    {0, 15, 7, 4, 14, 2, 13, 1, 10, 6, 12, 11, 9, 5, 3, 8},
                    {4, 1, 14, 8, 13, 6, 2, 11, 15, 12, 9, 7, 3, 10, 5, 0},
                    {15, 12, 8, 2, 4, 9, 1, 7, 5, 11, 3, 14, 10, 0, 6, 13}
            },
            {
                    {15, 1, 8, 14, 6, 11, 3, 4, 9, 7, 2, 13, 12, 0, 5, 10},
                    {3, 13, 4, 7, 15, 2, 8, 14, 12, 0, 1, 10, 6, 9, 11, 5},
                    {0, 14, 7, 11, 10, 4, 13, 1, 5, 8, 12, 6, 9, 3, 2, 15},
                    {13, 8, 10, 1, 3, 15, 4, 2, 11, 6, 7, 12, 0, 5, 14, 9}
            },
            {
                    {10, 0, 9, 14, 6, 3, 15, 5, 1, 13, 12, 7, 11, 4, 2, 8},
                    {13, 7, 0, 9, 3, 4, 6, 10, 2, 8, 5, 14, 12, 11, 15, 1},
                    {13, 6, 4, 9, 8, 15, 3, 0, 11, 1, 2, 12, 5, 10, 14, 7},
                    {1, 10, 13, 0, 6, 9, 8, 7, 4, 15, 14, 3, 11, 5, 2, 12}
            },
            {
                    {7, 13, 14, 3, 0, 6, 9, 10, 1, 2, 8, 5, 11, 12, 4, 15},
                    {13, 8, 11, 5, 6, 15, 0, 3, 4, 7, 2, 12, 1, 10, 14, 9},
                    {10, 6, 9, 0, 12, 11, 7, 13, 15, 1, 3, 14, 5, 2, 8, 4},
                    {3, 15, 0, 6, 10, 1, 13, 8, 9, 4, 5, 11, 12, 7, 2, 14}
            },
            {
                    {2, 12, 4, 1, 7, 10, 11, 6, 8, 5, 3, 15, 13, 0, 14, 9},
                    {14, 11, 2, 12, 4, 7, 13, 1, 5, 0, 15, 10, 3, 9, 8, 6},
                    {4, 2, 1, 11, 10, 13, 7, 8, 15, 9, 12, 5, 6, 3, 0, 14},
                    {11, 8, 12, 7, 1, 14, 2, 13, 6, 15, 0, 9, 10, 4, 5, 3}
            },
            {
                    {12, 1, 10, 15, 9, 2, 6, 8, 0, 13, 3, 4, 14, 7, 5, 11},
                    {10, 15, 4, 2, 7, 12, 9, 5, 6, 1, 13, 14, 0, 11, 3, 8},
                    {9, 14, 15, 5, 2, 8, 12, 3, 7, 0, 4, 10, 1, 13, 11, 6},
                    {4, 3, 2, 12, 9, 5, 15, 10, 11, 14, 1, 7, 6, 0, 8, 13}
            },
            {
                    {4, 11, 2, 14, 15, 0, 8, 13, 3, 12, 9, 7, 5, 10, 6, 1},
                    {13, 0, 11, 7, 4, 9, 1, 10, 14, 3, 5, 12, 2, 15, 8, 6},
                    {1, 4, 11, 13, 12, 3, 7, 14, 10, 15, 6, 8, 0, 5, 9, 2},
                    {6, 11, 13, 8, 1, 4, 10, 7, 9, 5, 0, 15, 14, 2, 3, 12}
            },
            {
                    {13, 2, 8, 4, 6, 15, 11, 1, 10, 9, 3, 14, 5, 0, 12, 7},
                    {1, 15, 13, 8, 10, 3, 7, 4, 12, 5, 6, 11, 0, 14, 9, 2},
                    {7, 11, 4, 1, 9, 12, 14, 2, 0, 6, 10, 13, 15, 3, 5, 8},
                    {2, 1, 14, 7, 4, 10, 8, 13, 15, 12, 9, 0, 3, 5, 6, 11}
            }
    };

    private static final int[] shiftBits = {
            1, 1, 2, 2,
            2, 2, 2, 2,
            1, 2, 2, 2,
            2, 2, 2, 1};

    public static void main(String[] args) {
        String plainText = "123456ABCD132536";
        String key = "AABB09182736CCDD";
        boolean[] plainBin = new boolean[plainText.length() * 4];
        boolean[] keyBin = new boolean[key.length() * 4];
        for (int i = 0; i < 8; i++) {
            System.arraycopy(hexToBin(plainText.substring(i * 2, (i + 1) * 2)), 0, plainBin, i * 8, 8);
        }
        for (int i = 0; i < 8; i++) {
            System.arraycopy(hexToBin(key.substring(i * 2, (i + 1) * 2)), 0, keyBin, i * 8, 8);
        }
        boolean[] cipherBin = encrypt(plainBin, keyBin);
        StringBuilder cipherText = new StringBuilder();
        for (int i = 0; i < 8; i++) {
            cipherText.append(binToHex(Arrays.copyOfRange(cipherBin, i * 8, (i + 1) * 8)));
        }
        System.out.println(cipherText);
    }

    public static String binToHex(boolean[] arr) {
        StringBuilder binaryStr = new StringBuilder();
        for (boolean bit : arr) {
            binaryStr.append((bit) ? "1" : "0");
        }
        int decimal = Integer.parseInt(binaryStr.toString(), 2);
        return Integer.toString(decimal, 16);
    }

    public static boolean[] hexToBin(String str) {
        int value = Integer.parseInt(str, 16);
        return intToBin(value, str.length() * 4);
    }

    public static int binToInt(boolean[] arr) {
        int i = 0;
        for (boolean b : arr)
            i = (i << 1) | (b ? 1 : 0);
        return i;
    }

    public static byte[] binToByte(boolean[] arr) {
        byte[] pointCodes = new byte[arr.length / 8];
        int k = 0;
        for (int i = 0; i < pointCodes.length; i++) {
            byte temp = 0;
            for (int j = 0; j < 8; j++) {
                temp = (byte) ((temp << 1) | (arr[k++] ? 1 : 0));
            }
            pointCodes[i] = temp;
        }
        return pointCodes;
    }

    public static boolean[] intToBin(int input, int length) {
        boolean[] inputBin = new boolean[length];
        int k = 0;
        for (int i = length - 1; i >= 0; i--) {
            inputBin[k++] = (input & (1 << i)) != 0;
        }
        return inputBin;
    }

    public static boolean[] strToBin(String input) {
        int[] inputCode = input.codePoints().toArray();
        boolean[] inputBin = new boolean[inputCode.length * 8];
        int k = 0;
        for (int i : inputCode) {
            for (int j = 7; j >= 0; j--) {
                inputBin[k++] = (i & (1 << j)) != 0;
            }
        }
        return inputBin;
    }

    public static boolean[] swapFromMid(boolean[] arr) {
        boolean[] output = arr.clone();
        System.arraycopy(arr, (output.length + 1) / 2, output, 0, output.length / 2);
        System.arraycopy(arr, 0, output, (output.length + 1) / 2, output.length / 2);
        return output;
    }

    public static boolean[] mergeArrays(boolean[] first, boolean[] second) {
        boolean[] result = Arrays.copyOf(first, first.length + second.length);
        System.arraycopy(second, 0, result, first.length, second.length);
        return result;
    }

    public static boolean[] permutation(int[] sequence, boolean[] input) {
        boolean[] output = new boolean[sequence.length];
        for (int i = 0; i < sequence.length; i++)
            output[i] = input[sequence[i] - 1];
        return output;
    }

    public static boolean[] sBox(boolean[] input) {
        boolean[] output = new boolean[32];
        int row, col;
        for (int i = 0; i < 8; i++) {
            boolean[] temp = Arrays.copyOfRange(input, i * 6, (i + 1) * 6);

            row = (0) | (temp[0] ? 1 : 0);
            row = (row << 1) | (temp[5] ? 1 : 0);
            col = binToInt(Arrays.copyOfRange(temp, 1, 5));

            System.arraycopy(intToBin(S_BOX[i][row][col], 4), 0, output, i * 4, 4);
        }
        return output;
    }

    public static boolean[] leftShift(boolean[] input, int srcPos, int dstPos, int round) {
        boolean[] output = input.clone();
        int length = dstPos - srcPos;
        System.arraycopy(input, srcPos, output, dstPos - round, round);
        System.arraycopy(input, srcPos + round, output, srcPos, length - round);
        return output;
    }

    public static boolean[][] getKeys(boolean[] key) {
        boolean[][] keys = new boolean[16][];
        boolean[] temp = permutation(PC1_TABLE, key);
        for (int i = 0; i < 16; i++) {
            temp = leftShift(temp, 0, 28, shiftBits[i]);
            temp = leftShift(temp, 28, 56, shiftBits[i]);

            keys[i] = permutation(PC2_TABLE, temp);
        }

        return keys;
    }

    public static boolean[] round(boolean[] input, boolean[] key) {
        boolean[] left = Arrays.copyOfRange(input, 0, 32);
        boolean[] right = Arrays.copyOfRange(input, 32, 64);

        boolean[] temp = permutation(EP_TABLE, right);
        for (int i = 0; i < 48; i++) {
            temp[i] ^= key[i];
        }

        temp = sBox(temp);
        temp = permutation(P_TABLE, temp);

        for (int i = 0; i < 32; i++) {
            left[i] ^= temp[i];
        }

        return mergeArrays(right, left);
    }

    static boolean[] encrypt(boolean[] plainBin, boolean[] keyBin) {

        boolean[][] keys = getKeys(keyBin);
        plainBin = permutation(IP_TABLE, plainBin);

        for (int i = 0; i < 16; i++) {
            plainBin = round(plainBin, keys[i]);
        }
        plainBin = swapFromMid(plainBin);

        plainBin = permutation(IP1_TABLE, plainBin);
        return plainBin;
    }

    static boolean[] decrypt(boolean[] cipherBin, boolean[] keyBin) {

        boolean[][] keys = getKeys(keyBin);

        cipherBin = permutation(IP_TABLE, cipherBin);

        Collections.reverse(Arrays.asList(keys));
        for (int i = 0; i < 16; i++) {
            cipherBin = round(cipherBin, keys[i]);
        }
        cipherBin = swapFromMid(cipherBin);

        cipherBin = permutation(IP1_TABLE, cipherBin);

        return cipherBin;
    }

    static String encode(String plainText, String key) {
        boolean[] keyBin = strToBin(key);
        boolean[] plainBin = strToBin(plainText);
        boolean[] end = new boolean[64 - (plainBin.length % 64)];
        Arrays.fill(end, false);
        end[0] = true;
        plainBin = mergeArrays(plainBin, end);

        boolean[] cipherBin = encrypt(Arrays.copyOfRange(plainBin, 0, 64), keyBin);
        for (int i = 64; i < plainBin.length; i += 64) {
            cipherBin = mergeArrays(cipherBin, encrypt(Arrays.copyOfRange(plainBin, i, i + 64), keyBin));
        }

        return Base64.getEncoder().encodeToString(binToByte(cipherBin));
    }

    static String decode(String cipherText, String key) {

        byte[] buff = Base64.getDecoder().decode(cipherText);
        boolean[] keyBin = strToBin(key);

        boolean[] cipherBin = new boolean[buff.length * 8];
        int k = 0;
        for (byte b : buff) {
            for (int i = 7; i >= 0; i--) {
                cipherBin[k++] = (b & (1 << i)) != 0;
            }
        }

        boolean[] plainBin = decrypt(Arrays.copyOfRange(cipherBin, 0, 64), keyBin);
        for (int i = 64; i < cipherBin.length; i += 64) {
            plainBin = mergeArrays(plainBin, decrypt(Arrays.copyOfRange(cipherBin, i, i + 64), keyBin));
        }
        int i = plainBin.length - 1;
        while (!plainBin[i--])
            continue;
        return new String(binToByte(Arrays.copyOf(plainBin, i + 1)));
    }
}
