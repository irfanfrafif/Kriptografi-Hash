import java.nio.charset.StandardCharsets;

public class Cipher2 {

    static int MAX_ROUND = 16;

    public static void feistelCipher1(String input) {
        char[] output = input.toCharArray();
        String bruh = "";
        for (char c : output) {
            String binary = String.format("%8s", Integer.toBinaryString(c)).replaceAll(" ", "0");
            bruh = bruh.concat(binary) + " ";
        }
    }

    public static void feistelCipher2(String input) {
        byte[] b = input.getBytes(StandardCharsets.UTF_8);
        boolean[] bools = BitmaskHelper.bytesToBoolArray(b);
        // String wad = test.toString();
    }

    public static int feistel32bit(int input, int key) {
        return feistel32bit(input, key, 1);
    }

    public static int feistel32bitDecipher(int input, int key) {
        return feistel32bitDecipher(input, key, MAX_ROUND);
    }

    public static int feistel32bit(int input, int key, int round) {
        int output = 0;
        short subkey = keyScheduler(key, round);

        short[] shorts = BitmaskHelper.splitInt(input);

        short shorts1new = fMethod(shorts[1], subkey);
        shorts[0] = (short) (shorts[0] ^ shorts1new);

        if (round < MAX_ROUND) {
            output = BitmaskHelper.shortsToInt(shorts[1], shorts[0]);
            System.out.println("Cipher Round " + round + ": " + output);
            return feistel32bit(output, key, round + 1);
        } else {
            output = BitmaskHelper.shortsToInt(shorts[0], shorts[1]);
            System.out.println("Cipher Round " + round + ": " + output);
            return output;
        }
    }

    public static int feistel32bitDecipher(int input, int key, int round) {
        int output = 0;
        short subkey = keyScheduler(key, round);

        short[] shorts = BitmaskHelper.splitInt(input);

        short shorts1new = fMethod(shorts[1], subkey);
        shorts[0] = (short) (shorts[0] ^ shorts1new);

        if (round > 1) {
            output = BitmaskHelper.shortsToInt(shorts[1], shorts[0]);
            System.out.println("Decipher Round " + round + ": " + output);
            return feistel32bitDecipher(output, key, round - 1);
        } else {
            output = BitmaskHelper.shortsToInt(shorts[0], shorts[1]);
            System.out.println("Decipher Round " + round + ": " + output);
            return output;
        }
    }

    public static short fMethod(short input, short key) {
        int output = 0;

        output = (input ^ key);

        return (short) output;
    }

    public static short keyScheduler(int key, int round) {
        int subkey = 0;

        subkey = ((key >> round - 1) & 0xFFFF);
        // System.out.println(subkey);

        return (short) subkey;
    }

    public static void main(String[] args) {
        // feistelCipher1("tast ");
        // feistelCipher2("aa");
        // byte a = BitmaskHelper.boolArrayToByte(BitmaskHelper.byteToBoolArray((byte)
        // 97));
        // short[] a = BitmaskHelper.splitInt(Integer.parseInt("FF0F00F", 16));
        // byte[] b = BitmaskHelper.splitShort(a[0]);
        // short c = BitmaskHelper.bytesToShort(b[0], b[1]);
        // int d = BitmaskHelper.shortsToInt(c, a[1]);
        // feistel32bit(Integer.parseInt("FF0F00F", 16), 0xAAAAAAAA);

        String inputtext = "abcd";
        int textLength = inputtext.length();
        if (textLength % 4 != 0) {
            textLength += 4 - (textLength % 4);
        }
        String plaintext = String.format("%-" + textLength + "s", inputtext);

        int currentCharIndex = 0;

        int[] blocks = new int[textLength / 4];
        for (int i = 0; i < blocks.length; i++) {
            byte[] b = plaintext.substring(currentCharIndex, currentCharIndex +
                    4).getBytes(StandardCharsets.UTF_8);
            blocks[i] = BitmaskHelper.shortsToInt(BitmaskHelper.bytesToShort(b[0], b[1]),
                    BitmaskHelper.bytesToShort(b[2], b[3]));
            currentCharIndex += 4;
        }

        // int[] blocks = { 876084729 };
        // int[] blocks = { -1465071154 };

        int[] cipherBlocks = new int[blocks.length];
        for (int i = 0; i < blocks.length; i++) {
            cipherBlocks[i] = feistel32bit(blocks[i], 33961015);
        }

        int[] decipherBlocks = { feistel32bitDecipher(cipherBlocks[0], 33961015) };

        for (int i : cipherBlocks) {
            System.out.println(i + " ");
        }
        for (int i : decipherBlocks) {
            System.out.println(i + " ");
        }
    }
}
