public class BitmaskHelper {

    public static boolean[] byteToBoolArray(byte b) {
        boolean[] returnedBools = new boolean[8];

        for (int i = 0; i < 8; i++) {
            returnedBools[7 - i] = ((b >> i) & 1) == 1;
        }

        return returnedBools;
    }

    public static boolean[] bytesToBoolArray(byte[] b) {
        int arraySize = b.length * 8;
        boolean[] returnedBools = new boolean[arraySize];
        int currentBitIndex = 0;

        for (int i = 0; i < b.length; i++) {
            while (currentBitIndex < arraySize) {
                returnedBools[arraySize - currentBitIndex - 1] = ((b[i] >> currentBitIndex % 8) & 1) == 1;
                currentBitIndex++;
            }
        }

        return returnedBools;
    }

    public static byte boolArrayToByte(boolean[] bools) {
        int output = 0;

        for (int i = 0; i < bools.length; i++) {
            output = output << 1;
            if (bools[i]) {
                output = output & 0x01;
            }
        }

        return (byte) output;
    }

    public static short boolArrayToShort(boolean[] bools) {
        int output = 0;

        for (int i = 0; i < bools.length; i++) {
            output = output << 1;
            if (bools[i]) {
                output = output & 0x01;
            }
        }

        return (short) output;
    }

    public static short bytesToShort(byte msb, byte lsb) {
        int output = msb;
        output = ((output & 0xFF) << 8) | (lsb & 0xFF);
        return (short) output;
    }

    public static int shortsToInt(short msb, short lsb) {
        int output = msb;
        output = ((output & 0xFFFF) << 16) | (lsb & 0xFFFF);
        return output;
    }

    public static short[] splitInt(int input) {
        short[] output = new short[2];

        output[1] = (short) (input & 0xFFFF);
        output[0] = (short) ((input >> 16) & 0xFFFF);

        return output;
    }

    public static byte[] splitShort(short input) {
        byte[] output = new byte[2];

        output[1] = (byte) (input & 0xFF);
        output[0] = (byte) ((input >> 8) & 0xFF);

        return output;
    }

    public static int SetBit(int i, int index) {
        return i | (1 << index);
    }

    public static int highestSetBit(int input) {
        int r = 0;
        while (input > 1) {
            input >>= 1;
            r++;
        }
        return r;
    }

    // Irreduceable polynomial of GF(2^16) IN OCTAL NUMBER:
    // 210013, 234313, 233303, 307107, 307527, 306357,
    // 201735, 272201, 242413, 270155, 302157, 210205,
    // 305667, 236107
    //
    // in Octal to binary:
    // 0=000, 1=001, 2=010, 3=011
    // 4=100 5=101 6=110 7=111
    //
    // thus
    // 210013(8) = 010001000000001011(2) => x^16 + x^12 + x^3 + x + 1
    //
    // Reference:
    // https://web.eecs.utk.edu/~jplank/plank/papers/CS-07-593/primitive-polynomial-table.txt

    // Polynomial Multiplication implementation reference:
    // https://stackoverflow.com/questions/13202758/multiplying-two-polynomials
    public static int polynomialMultMod(int fx, int gx, int hx) {
        int result = 0;

        // Multiplication
        int multResult = 0;
        int gxDegree = highestSetBit(gx);
        for (int i = 0; i <= gxDegree; i++) {
            if (((gx >> i) & 1) == 1) {
                multResult ^= fx << i;
            }
        }

        // Modulo Reduction
        int hxDegree = highestSetBit(hx);
        int multDegree = highestSetBit(multResult);

        for (int i = 0; i <= multDegree; i++) {
            if (i < hxDegree) {
                result ^= multResult & (1 << i);
            } else {
                result ^= (hx ^ (1 << hxDegree)) << (i - hxDegree);
            }
        }

        return result;
    }
}
