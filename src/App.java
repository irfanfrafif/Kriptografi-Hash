public class App {
    public static void main(String[] args) throws Exception {
        String x = "Hello, World!";
        System.out.println(x);

        short a = (short) 0xAFAF;
        int b = (int) a;
        short c = (short) a;
        int d = b ^ c;
    }
}
