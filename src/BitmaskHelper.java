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
}
