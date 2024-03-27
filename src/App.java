public class App {
    public static void main(String[] args) throws Exception {
        String x = "Hello, World!";
        System.out.println(x);

        // 0b5f8bcd9dce9edd1a1b80dc95cb928f050396dc8c8187df5106db8c
        String test1 = Cipher2.cipherText("the quick brown fox jumps", "komb", false);
        // output: "71a50d714ff56865226a5dab154b2fc464cc14b3520166ba71724748"
        // output: "32437e3711d36365002f55315da76fe90cc73ce34a3b47fe401a3f2a"
        String test2 = Cipher2.decipherText(test1, "6B6F6D62", true);
        // output: "the quick brown fox jumps"

        System.out.println(test1 + "\n" + test2);
    }
}
