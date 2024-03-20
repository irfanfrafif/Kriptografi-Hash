public class Cipher {
    static int MAX_ROUND = 16;

    private static int encrypt(int input, int key) {
        return feistel(input, key, 1, false);
    }

    private static int decrypt(int input, int key) {
        return feistel(input, key, MAX_ROUND, true);
    }

    private static int feistel(int input, int key, int round, boolean isDecrypt) {
        int output = 0;
        int subkey = keyScheduler(key, round);

        int right = input & 0xFFFF;
        int left = (input >> 16) & 0xFFFF;

        int f1 = fMethod(right, subkey);
        left = left ^ f1;

        // If Decrypting and there are still more rounds
        if (isDecrypt) {
            if (round > 1) {
                output = (right << 16) | left;
                return feistel(output, key, --round, true);
            }
        }
        // If Encrypting and there are still more rounds
        else {
            if (round < MAX_ROUND) {
                output = (right << 16) | left;
                return feistel(output, key, ++round, false);
            }
        }
        // If there are no more rounds
        output = (left << 16) | right;
        return output;
    }

    private static int fMethod(int input, int subkey) {
        int output = 0;

        // TODO: Implement more sophisticated F Method
        output = input ^ subkey;

        return output;
    }

    private static int keyScheduler(int key, int round) {
        int subkey = 0;

        // TODO: Implement more sophisticated Key Scheduler
        subkey = ((key >> round - 1) & 0xFFFF);

        return subkey;
    }

    private static void cbc(int[] blocks, int blockIndex, int key) {
        int iv;
        if (blockIndex <= 0)
            iv = ~key;
        else
            iv = blocks[blockIndex - 1];

        // TODO: Implement more sophisticated CBC Encryption
        blocks[blockIndex] = blocks[blockIndex] ^ iv;
        blocks[blockIndex] = blocks[blockIndex] ^ key; // Function
    }

    private static void cbcDecrypt(int[] decipherBlocks, int[] cipherBlocks, int blockIndex, int key) {
        int iv;
        if (blockIndex <= 0)
            iv = ~key;
        else
            iv = cipherBlocks[blockIndex - 1];

        // TODO: Implement more sophisticated CBC Decryption
        decipherBlocks[blockIndex] = decipherBlocks[blockIndex] ^ key; // Function
        decipherBlocks[blockIndex] = decipherBlocks[blockIndex] ^ iv;
    }

    private static int[] processInput(String input) {
        // Text length and check if it needs padding
        int textLength = input.length();
        if (textLength % 4 != 0) {
            textLength += 4 - (textLength % 4);
        }

        // Make new plaintext from input + padding
        String plaintext = String.format("%-" + textLength + "s", input);

        // Calculate Total blocks needed
        int blockCount = textLength / 4;
        int[] blocks = new int[blockCount];

        // Make each 4 chars into 32 bits block in integer form
        int charIndex = 0;
        for (int i = 0; i < blockCount; i++) {
            byte[] b = plaintext.substring(charIndex, charIndex + 4).getBytes();
            blocks[i] = ((((b[0] << 24) | (b[1] << 16)) | (b[2] << 8)) | b[3]);
            charIndex += 4;
        }

        return blocks;
    }

    private static int[] processHexInput(String input) {
        int blockCount = input.length() / 8;
        int[] blocks = new int[blockCount];

        int charIndex = 0;
        for (int i = 0; i < blocks.length; i++) {
            blocks[i] = Integer.parseUnsignedInt(input.substring(charIndex, charIndex + 8), 16);
            charIndex += 8;
        }

        return blocks;
    }

    private static int processKey(String inputKey, boolean isKeyHex) {
        // Convert key to int depending whether key is hex or text
        int key = 0;
        if (isKeyHex) {
            key = Integer.parseUnsignedInt(inputKey, 16);
        } else {
            byte[] b = inputKey.getBytes();
            key = ((((b[0] << 24) | (b[1] << 16)) | (b[2] << 8)) | b[3]);
        }

        return key;
    }

    // Main method for ciphering
    public static String cipherText(String input, String inputKey, boolean isKeyHex) {
        int[] blocks = processInput(input);
        int key = processKey(inputKey, isKeyHex);

        // Cipher each blocks using feistel
        int[] cipherBlocks = new int[blocks.length];
        for (int i = 0; i < cipherBlocks.length; i++) {
            cipherBlocks[i] = encrypt(blocks[i], key);
        }

        // Cipher whole blocks with CBC
        for (int i = 0; i < cipherBlocks.length; i++) {
            cbc(cipherBlocks, i, key);
        }

        // Convert cipherblocks int into hex string, then concat all into single string
        String output = "";
        for (int i = 0; i < cipherBlocks.length; i++) {
            output = output + String.format("%8s", Integer.toHexString(cipherBlocks[i])).replaceAll(" ", "0");
        }

        return output;
    }

    // Main method for deciphering
    public static String decipherText(String input, String inputKey, boolean isKeyHex) {
        int[] blocks = processHexInput(input);
        int key = processKey(inputKey, isKeyHex);

        // Create separate array for decrypting
        int[] decipherBlocks = new int[blocks.length];
        System.arraycopy(blocks, 0, decipherBlocks, 0, blocks.length);

        // Decipher whole blocks with CBC (decrypt)
        for (int i = 0; i < blocks.length; i++) {
            cbcDecrypt(decipherBlocks, blocks, i, key);
        }

        // Decipher each block with feistel (decrypt)
        for (int i = 0; i < blocks.length; i++) {
            decipherBlocks[i] = decrypt(decipherBlocks[i], key);
        }

        // comment tobeadded
        String hexString = "";
        for (int i = 0; i < decipherBlocks.length; i++) {
            hexString = hexString + String.format("%8s", Integer.toHexString(decipherBlocks[i])).replaceAll(" ", "0");
        }

        String output = "";
        String[] hexPairs = hexString.split("(?<=\\G.{2})");

        for (String s : hexPairs) {
            output = output + Character.toChars(Integer.parseInt(s, 16))[0]; // WTF is this?!!?!?!
        }

        return output;
    }
}
