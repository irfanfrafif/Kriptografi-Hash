public class Cipher {
    static int MAX_ROUND = 16;

    private static BlumBlumShub prng;

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
                if (AppUI.progressArea != null)
                    AppUI.progressArea.append("Round " + round + " - " + Integer.toHexString(output) + "\n");
                return feistel(output, key, --round, true);
            }
        }
        // If Encrypting and there are still more rounds
        else {
            if (round < MAX_ROUND) {
                output = (right << 16) | left;
                if (AppUI.progressArea != null)
                    AppUI.progressArea.append("Round " + round + " - " + Integer.toHexString(output) + "\n");
                return feistel(output, key, ++round, false);
            }
        }
        // If there are no more rounds
        output = (left << 16) | right;
        if (AppUI.progressArea != null)
            AppUI.progressArea.append("Round " + round + " - " + Integer.toHexString(output) + "\n");
        return output;
    }

    private static int fMethod(int input, int subkey) {
        int output = 0;
        // int[] sBox = { 0x3, 0x4, 0x1, 0x2, 0x7, 0x6, 0x5, 0x8, 0xB, 0xC, 0x9, 0xA,
        // 0xF, 0xE, 0xD, 0x0, 0xC};

        // 69643(10) = 010001000000001011(2) => x^16 + x^12 + x^3 + x + 1
        output = ((((BitmaskHelper.polynomialMultMod(input, subkey, 69643)) ^ subkey) >> 1) + 12345) & 0xFFFF;
        output = ~output;

        // Key mixing
        // int afterKeyMixing = (((input ^ subkey) >> 1) + 12345) & 0xFFFF;

        // Substitution with S-box
        // int afterSubstitution = sBox[afterKeyMixing & 0xF];

        // Permutation (rotate left by 1 bit)
        // output = (afterSubstitution << 1) | (afterSubstitution >>> (0xFFFF - 1));
        return output & 0xFFFF;
    }

    private static int keyScheduler(int key, int round) {
        int subkey = 0;
        prng = new BlumBlumShub(key);
        int randomValue = prng.next(round);
        subkey = ((key >> round - 1) & 0xFFFF);
        subkey ^= randomValue;
        return subkey;
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
        if (AppUI.progressArea != null)
            AppUI.progressArea.append("Input length: " + (input.length() * 8) + " bits\n");

        int[] blocks = processInput(input);
        int key = processKey(inputKey, isKeyHex);

        if (AppUI.progressArea != null)
            AppUI.progressArea.append("Block count: " + blocks.length + "\nKey: " + Integer.toHexString(key) + "\n");

        // Cipher each blocks using cbc and feistel
        int[] cipherBlocks = new int[blocks.length];
        for (int i = 0; i < cipherBlocks.length; i++) {
            if (AppUI.progressArea != null)
                AppUI.progressArea.append("\n|Block " + (i + 1) + " - " + Integer.toHexString(blocks[i]) + "|\n");
            if (i == 0) {
                prng = new BlumBlumShub(key);
                int iv = prng.next(cipherBlocks.length);
                cipherBlocks[i] = blocks[i] ^ iv;
            } else {
                cipherBlocks[i] = cipherBlocks[i - 1] ^ blocks[i];
            }
            cipherBlocks[i] = encrypt(cipherBlocks[i], key);
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
        if (AppUI.progressArea != null)
            AppUI.progressArea.append("Input length: " + (input.length() * 4) + " bits\n");

        int[] blocks = processHexInput(input);
        int key = processKey(inputKey, isKeyHex);

        if (AppUI.progressArea != null)
            AppUI.progressArea.append("Block count: " + blocks.length + "\nKey: " + Integer.toHexString(key) + "\n");

        // Create separate array for decrypting
        int[] decipherBlocks = new int[blocks.length];
        System.arraycopy(blocks, 0, decipherBlocks, 0, blocks.length);

        // Decipher each block with cbc and feistel (decrypt)
        for (int i = 0; i < blocks.length; i++) {
            if (AppUI.progressArea != null)
                AppUI.progressArea.append("\n|Block " + (i + 1) + " - " + Integer.toHexString(blocks[i]) + "|\n");
            decipherBlocks[i] = decrypt(blocks[i], key);
            if (i == 0) {
                prng = new BlumBlumShub(key);
                int iv = prng.next(decipherBlocks.length);
                decipherBlocks[i] = decipherBlocks[i] ^ iv;
            } else {
                decipherBlocks[i] = blocks[i - 1] ^ decipherBlocks[i];
            }
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
