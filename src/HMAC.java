public class HMAC {
    public static String computeHMAC(String message, String key) {
        int okeypad = HashCipher.computeHash(key) ^ 0x5c5c;
        int ikeypad = HashCipher.computeHash(key) ^ 0x3636;

        int[] blocks = HashCipher.processInput(message);
        int[] blockss = new int[blocks.length + 1];
        blockss[0] = ikeypad;
        System.arraycopy(blocks, 0, blockss, 1, blocks.length);

        int innerHash = HashCipher.computeHash(blockss);

        int[] innerHashBlock = { okeypad, innerHash };
        int hmac = HashCipher.computeHash(innerHashBlock);

        return Integer.toString(hmac);
    }
}
