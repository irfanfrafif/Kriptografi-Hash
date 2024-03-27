public class BlumBlumShub {
    private int p;
    private int a = 394595;
    private int b = 123555;
    private int n = a * b; // Product of two large primes

    public BlumBlumShub(int seed) {
        this.p = seed;
    }

    public int next(int round) {
        int q = 0;
        System.out.println("p: " + p);
        System.out.println(n);
        
        for (int i = 0; i < 32 ; i++) {
            p = (p * p) % n;
            q = q << 1;
            q += p % 2;
        }

        q = q >> round - 1;
        
        System.out.println("q: " + q);

        return q & 0xFFFF;
    }
}
