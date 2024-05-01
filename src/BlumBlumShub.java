public class BlumBlumShub {
    private long p;
    private long a = 8191;
    private long b = 7351;
    private long n = a * b; // Product of two large primes, n = 60212041

    public BlumBlumShub(long seed) {
        this.p = seed;
    }

    public int next(int round) {
        int q = 0;
        // System.out.println("p: " + p);
        // System.out.println(n);

        for (int i = 0; i < 32; i++) {
            // System.out.println("iterasi: " + i);
            // System.out.println("p = ("+ p+" * "+p+") % "+n+" = "+p*p % n);
            p = (p * p) % n;
            q = q << 1;
            q += p % 2;
            // System.out.println("q = "+ p + " % 2 = "+ p % 2);
        }

        // System.out.println("q: " + q);

        return q & 0xFFFFFFFF;
    }
}
