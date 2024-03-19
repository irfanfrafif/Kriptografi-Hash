public class App {
    public static void main(String[] args) throws Exception {
        String x = "Hello, World!";
        System.out.println(x);

        // 0b5f8bcd9dce9edd1a1b80dc95cb928f050396dc8c8187df5106db8c
        String test1 = Cipher.cipherText("the quick brown fox jumps", "koma", false);
        // output: "0b5f8bcd9dce9edd1a1b80dc95cb928f050396dc8c8187df5106db8c"
        String test2 = Cipher.decipherText(test1, "6B6F6D61", true);
        // output: "the quick brown fox jumps"

        System.out.println(test1 + "\n" + test2);
    }
}
