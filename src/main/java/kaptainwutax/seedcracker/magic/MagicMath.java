package kaptainwutax.seedcracker.magic;

/**
 * Math utility library-- I have no idea how it works, don't ask.
 * All credits to Matthew. (The man doesn't have a GitHub, shame!)
 * */
public class MagicMath {

	public static final long MASK_16 = 0xFFFFL;
	public static final long MASK_32 = 0xFFFF_FFFFL;
	public static final long MASK_48 = 0xFFFF_FFFF_FFFFL;

	public static int countTrailingZeroes(long v) {
		int c;  // output: c will count v's trailing zero bits,
		// so if v is 1101000 (base 2), then c will be 3
		v = (v ^ (v - 1)) >> 1;  // Set v's trailing 0s to 1s and zero rest

		for(c = 0; v !=0; c++)  {
			v >>>= 1;
		}

		return c;
	}

	public static long modInverse(long x, int mod) { //Fast method for modular inverse mod powers of 2
		long inv = 0;
		long b = 1;

		for(int i = 0; i < mod; i++) {
			if((b & 1) == 1) {
				inv |= 1L << i;
				b = (b - x) >> 1;
			} else {
				b >>= 1;
			}
		}

		return inv;
	}

}
