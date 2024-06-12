public class HashCipher {
    static int MAX_ROUND = 16;

    private static BlumBlumShub prng;

    private static int hashFunction(int input, int key) {
        return feistel(input, key, 1, false);
    }

    private static int feistel(int input, int key, int round, boolean isDecrypt) {
        int output = 0;
        int subkey = keyScheduler(key, round);

        int right = input & 0xFF;
        int left = (input >> 8) & 0xFF;

        int f1 = fMethod(right, subkey);
        left = left ^ f1;

        if (isDecrypt) {
            if (round > 1) {
                output = (right << 8) | left;
                return feistel(output, key, --round, true);
            }
        } else {
            if (round < MAX_ROUND) {
                output = (right << 8) | left;
                return feistel(output, key, ++round, false);
            }
        }

        output = (left << 8) | right;
        return output & 0xFFFF;
    }

    private static int fMethod(int input, int subkey) {
        int output = ((((BitmaskHelper.polynomialMultMod(input, subkey, 69643)) ^ subkey) >> 1) + 12345) & 0xFF;
        output = ~output;
        return output & 0xFF;
    }

    private static int keyScheduler(int key, int round) {
        prng = new BlumBlumShub(key);
        int randomValue = prng.next(round);
        int subkey = ((key >> round - 1) & 0xFF);
        subkey ^= randomValue;
        return subkey & 0xFF;
    }

    public static int computeHash(int[] blocks, int key) {
        int hash = 0;
        for (int block : blocks) {
            hash = hashFunction(block, key) ^ hash;
        }
        return hash & 0xFFFFFFFF;
    }

    public static int computeHash(String message, int key) {
        int[] blocks = processInput(message);
        return computeHash(blocks, key);
    }

    public static int[] processInput(String input) {
        int textLength = input.length();
        if (textLength % 2 != 0) {
            input += " "; // Padding to make the length even
        }

        int blockCount = input.length() / 2;
        int[] blocks = new int[blockCount];

        for (int i = 0; i < blockCount; i++) {
            char c1 = input.charAt(2 * i);
            char c2 = input.charAt(2 * i + 1);
            blocks[i] = (c1 << 8) | c2;
        }

        return blocks;
    }

    // private static int[] processHexInput(String input) {
    // int blockCount = input.length() / 8;
    // int[] blocks = new int[blockCount];

    // int charIndex = 0;
    // for (int i = 0; i < blocks.length; i++) {
    // blocks[i] = Integer.parseUnsignedInt(input.substring(charIndex, charIndex +
    // 8), 16);
    // charIndex += 8;
    // }

    // return blocks;
    // }

    // private static int processKey(String inputKey, boolean isKeyHex) {
    // // Convert key to int depending whether key is hex or text
    // int key = 0;
    // if (isKeyHex) {
    // key = Integer.parseUnsignedInt(inputKey, 16);
    // } else {
    // byte[] b = inputKey.getBytes();
    // key = ((((b[0] << 24) | (b[1] << 16)) | (b[2] << 8)) | b[3]);
    // }

    // return key;
    // }

    // // Main method for ciphering
    // public static String cipherText(String input, String inputKey, boolean
    // isKeyHex) {
    // if (AppUI.progressArea != null)
    // AppUI.progressArea.append("Input length: " + (input.length() * 8) + "
    // bits\n");

    // int[] blocks = processInput(input);
    // int key = processKey(inputKey, isKeyHex);

    // if (AppUI.progressArea != null)
    // AppUI.progressArea.append("Block count: " + blocks.length + "\nKey: " +
    // Integer.toHexString(key) + "\n");

    // // Cipher each blocks using cbc and feistel
    // int[] cipherBlocks = new int[blocks.length];
    // prng = new BlumBlumShub(key);
    // int iv = prng.next(cipherBlocks.length);

    // if (AppUI.progressArea != null)
    // AppUI.progressArea.append("IV: " + Integer.toHexString(iv) + "\n");

    // for (int i = 0; i < cipherBlocks.length; i++) {
    // if (AppUI.progressArea != null)
    // AppUI.progressArea.append("\n|Block " + (i + 1) + " - " +
    // Integer.toHexString(blocks[i]) + "|\n");
    // if (i == 0) {
    // cipherBlocks[i] = blocks[i] ^ iv;
    // } else {
    // cipherBlocks[i] = cipherBlocks[i - 1] ^ blocks[i];
    // }
    // cipherBlocks[i] = encrypt(cipherBlocks[i], key);
    // }

    // // Convert cipherblocks int into hex string, then concat all into single
    // string
    // String output = "";
    // for (int i = 0; i < cipherBlocks.length; i++) {
    // output = output + String.format("%8s",
    // Integer.toHexString(cipherBlocks[i])).replaceAll(" ", "0");
    // }

    // return output;
    // }

    // // Main method for deciphering
    // public static String decipherText(String input, String inputKey, boolean
    // isKeyHex) {
    // if (AppUI.progressArea != null)
    // AppUI.progressArea.append("Input length: " + (input.length() * 4) + "
    // bits\n");

    // int[] blocks = processHexInput(input);
    // int key = processKey(inputKey, isKeyHex);

    // if (AppUI.progressArea != null)
    // AppUI.progressArea.append("Block count: " + blocks.length + "\nKey: " +
    // Integer.toHexString(key) + "\n");

    // // Create separate array for decrypting
    // int[] decipherBlocks = new int[blocks.length];
    // System.arraycopy(blocks, 0, decipherBlocks, 0, blocks.length);
    // prng = new BlumBlumShub(key);
    // int iv = prng.next(decipherBlocks.length);
    // if (AppUI.progressArea != null)
    // AppUI.progressArea.append("IV: " + Integer.toHexString(iv) + "\n");

    // // Decipher each block with cbc and feistel (decrypt)
    // for (int i = 0; i < blocks.length; i++) {
    // if (AppUI.progressArea != null)
    // AppUI.progressArea.append("\n|Block " + (i + 1) + " - " +
    // Integer.toHexString(blocks[i]) + "|\n");
    // decipherBlocks[i] = decrypt(blocks[i], key);
    // if (i == 0) {
    // decipherBlocks[i] = decipherBlocks[i] ^ iv;
    // } else {
    // decipherBlocks[i] = blocks[i - 1] ^ decipherBlocks[i];
    // }
    // }

    // // comment tobeadded
    // String hexString = "";
    // for (int i = 0; i < decipherBlocks.length; i++) {
    // hexString = hexString + String.format("%8s",
    // Integer.toHexString(decipherBlocks[i])).replaceAll(" ", "0");
    // }

    // String output = "";
    // String[] hexPairs = hexString.split("(?<=\\G.{2})");

    // for (String s : hexPairs) {
    // output = output + Character.toChars(Integer.parseInt(s, 16))[0]; // WTF is
    // this?!!?!?!
    // }

    // return output;
    // }
}
