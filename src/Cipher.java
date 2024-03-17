public class Cipher {
    static int MAX_ROUND = 16;

    public static int encrypt(int input, int key) {
        return feistel(input, key, 1, false);
    }

    public static int decrpyt(int input, int key) {
        return feistel(input, key, MAX_ROUND, true);
    }

    public static int feistel(int input, int key, int round, boolean isDecrypt) {
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

    public static int fMethod(int input, int subkey) {
        int output = 0;

        // TODO: Implement more sophisticated F Method
        output = input ^ subkey;

        return output;
    }

    public static int keyScheduler(int key, int round) {
        int subkey = 0;

        // TODO: Implement more sophisticated Key Scheduler
        subkey = ((key >> round - 1) & 0xFFFF);

        return subkey;
    }

    public static void cbc(int[] blocks, int blockIndex, int key) {
        int iv;
        if (blockIndex <= 0)
            iv = ~key;
        else
            iv = blocks[blockIndex - 1];

        // TODO: Implement more sophisticated CBC Encryption
        blocks[blockIndex] = blocks[blockIndex] ^ iv;
        blocks[blockIndex] = blocks[blockIndex] ^ key;
    }

    public static void cbcDecrypt(int[] decipherBlocks, int[] cipherBlocks, int blockIndex, int key) {
        int iv;
        if (blockIndex <= 0)
            iv = ~key;
        else
            iv = cipherBlocks[blockIndex - 1];

        // TODO: Implement more sophisticated CBC Decryption
        decipherBlocks[blockIndex] = decipherBlocks[blockIndex] ^ key;
        decipherBlocks[blockIndex] = decipherBlocks[blockIndex] ^ iv;
    }

    public static void main(String[] args) {
        // Input
        String inputtext = "the quick brown fox jumps";
        int key = 1802464609; // Hex: 0x6B6F6D61, dalam text adalah "koma" wkwk

        // Text length and check if it needs padding
        int textLength = inputtext.length();
        if (textLength % 4 != 0) {
            textLength += 4 - (textLength % 4);
        }

        // Make new plaintext from input + padding
        String plaintext = String.format("%-" + textLength + "s", inputtext);

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

        // Cipher each blocks using feistel
        int[] cipherBlocks = new int[blockCount];
        for (int i = 0; i < blockCount; i++) {
            cipherBlocks[i] = encrypt(blocks[i], key);
        }

        // Cipher whole blocks with CBC
        for (int i = 0; i < blockCount; i++) {
            cbc(cipherBlocks, i, key);
        }

        // Create separate array for decrypting
        int[] decipherBlocks = new int[blockCount];
        System.arraycopy(cipherBlocks, 0, decipherBlocks, 0, blockCount);

        // Decipher whole blocks with CBC (decrypt)
        for (int i = 0; i < blockCount; i++) {
            cbcDecrypt(decipherBlocks, cipherBlocks, i, key);
        }

        // Decipher each block with feistel (decrypt)
        for (int i = 0; i < blockCount; i++) {
            decipherBlocks[i] = decrpyt(decipherBlocks[i], key);
        }

        // Print Cipher Blocks
        System.out.println("Cipher Blocks");
        for (int i : cipherBlocks) {
            System.out.println(i + " ");
        }

        // Print Plain Blocks
        System.out.println("Cipher Blocks");
        for (int i : decipherBlocks) {
            System.out.println(i + " ");
        }
    }
}
