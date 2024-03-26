import java.math.BigInteger;

public class BlumBlumShub {
    private BigInteger p;
//  p = 39459
//  q = 12355
//  n = 487515945
    private static final BigInteger MODULUS = new BigInteger("487515945", 10); // Product of two large primes

    public BlumBlumShub(BigInteger seed) {
        this.p = seed;
    }

    public int next() {
        int q = 0;
        System.out.println("p: " + p);
        
        for (int i = 0; i < 16 ; i++) {
            p = p.multiply(p).mod(BigInteger.valueOf(MODULUS.longValue()));
            // q = q.multiply(q).mod(BigInteger.valueOf(MODULUS.longValue()));
            q = q << 1;
            q += p.mod(BigInteger.valueOf(2)).intValue();
        }
        System.out.println("q: " + q);

        return q & 0xFFFF;
    }
}
