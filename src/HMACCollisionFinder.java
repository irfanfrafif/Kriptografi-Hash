public class HMACCollisionFinder {
    public static void main(String[] args) {
        String key = "key";
        String message3 = "findthis";
        String message4 = "bruteforce";

        String hmac3 = HMAC.computeHMAC(message3, key);
        String hmac4 = HMAC.computeHMAC(message4, key);

        int iteration = 0;
        while (!hmac3.equals(hmac4)) {
            message4 = incrementString(message4);
            hmac4 = HMAC.computeHMAC(message4, key);

            System.out.print("String: " + message4);
            System.out.println(" | HMAC: " + BitmaskHelper.intToHexString(Integer.parseInt(hmac4), 4));
            iteration += 1;
        }

        System.out.println("HMAC Collision found after " + iteration + " iteration:");
        System.out.print("Message 3: " + message3);
        System.out.println(" | HMAC 3: " + BitmaskHelper.intToHexString(Integer.parseInt(hmac3), 4));
        System.out.print("Message 4: " + message4);
        System.out.println(" | HMAC 4: " + BitmaskHelper.intToHexString(Integer.parseInt(hmac4), 4));
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
