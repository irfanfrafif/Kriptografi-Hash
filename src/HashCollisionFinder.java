public class HashCollisionFinder {
    public static void main(String[] args) {
        int key = 0xaaaa;
        String message1 = "find";
        String message2 = "brute";

        int hash1 = HashCipher.computeHash(message1, key);
        int hash2 = HashCipher.computeHash(message2, key);

        int iteration = 0;
        while (hash1 != hash2) {
            message2 = incrementString(message2);
            hash2 = HashCipher.computeHash(message2, key);

            System.out.print("String: " + message2);
            System.out.println(" | Hash: " + BitmaskHelper.intToHexString(hash2, 4));
            iteration += 1;
        }

        System.out.println("Hash Collision found after " + iteration + " iteration:");
        System.out.println("Message 1: " + message1 + " | Hash 1: " + BitmaskHelper.intToHexString(hash1, 4));
        System.out.println("Message 2: " + message2 + " | Hash 2: " + BitmaskHelper.intToHexString(hash2, 4));
    }

    private static String incrementString(String str) {
        char[] chars = str.toCharArray();
        for (int i = chars.length - 1; i >= 0; i--) {
            if (chars[i] == 'z') {
                chars[i] = 'a';
            } else {
                chars[i]++;
                break;
            }
        }
        return new String(chars);
    }
}
