package com.company;


import java.util.BitSet;

public class BloomFilterMurmur implements BloomFilter{
    private static final long M_64 = 0xc6a4a7935bd1e995L;
    private static final int R_64 = 47;
    private static final int DEFAULT_SEED = 0;
    private BitSet hashTable;
    private int filterSize;
    private int numOfHash;
    private int numOfElem = 0;

    public BloomFilterMurmur(int setSize, int bitsPerElement) {
        filterSize = setSize * bitsPerElement;
        hashTable = new BitSet(filterSize);
        numOfHash = 2 * filterSize / setSize;
        for (int i = 0; i < filterSize; i ++) {
            hashTable.clear(i);
        }
    }

    /**
     * Give the array of result of hash functions
     * @param str
     * @return
     */

    public int[] functions(String str) {
        str = str.toLowerCase();
        int[] func = new int[numOfHash];
        for (int i = 0; i < numOfHash; i++) {
            long hash = murmur(str);
            hash = (hash % filterSize + i * hash / filterSize) % filterSize;
            func[i] = (int) Math.abs(hash);
        }

        return func;
    }

    public void add(String str) {
        int[] strHash = functions(str);
        for (int i = 0; i < strHash.length; i ++) {
            hashTable.set(strHash[i],true);
        }
        numOfElem ++;
    }

    public boolean appears(String str) {
        int[] strHash = functions(str);
        for (int i = 0; i < strHash.length; i ++) {
            if (hashTable.get(strHash[i]) == false) {
                return false;
            }
        }
        return true;
    }

    public int dataSize() {
        return  numOfElem;
    }

    public int numHashes() {
        return numOfHash;
    }

    public long murmur(String str) {
        long h = (DEFAULT_SEED & 0xffffffffl) ^ (str.length() * M_64);
        int length8 = str.length() >> 3;

        // body
        for (int i = 0; i < length8; i++) {
            final int i8 = i << 3;
            long k = ((long) str.charAt(i8) & 0xff)
                    | (((long) str.charAt(i8 + 1) & 0xff) << 8)
                    | (((long) str.charAt(i8 + 2) & 0xff) << 16)
                    | (((long) str.charAt(i8 + 3) & 0xff) << 24)
                    | (((long) str.charAt(i8 + 4) & 0xff) << 32)
                    | (((long) str.charAt(i8 + 5) & 0xff) << 40)
                    | (((long) str.charAt(i8 + 6) & 0xff) << 48)
                    | (((long) str.charAt(i8 + 7) & 0xff) << 56);

            // mix functions
            k *= M_64;
            k ^= k >>> R_64;
            k *= M_64;
            h ^= k;
            h *= M_64;
        }

        // tail
        int tailStart = length8 << 3;
        switch (str.length() - tailStart) {
            case 7:
                h ^= (long) (str.charAt(tailStart + 6) & 0xff) << 48;
            case 6:
                h ^= (long) (str.charAt(tailStart + 5) & 0xff) << 40;
            case 5:
                h ^= (long) (str.charAt(tailStart + 4) & 0xff) << 32;
            case 4:
                h ^= (long) (str.charAt(tailStart + 3) & 0xff) << 24;
            case 3:
                h ^= (long) (str.charAt(tailStart + 2) & 0xff) << 16;
            case 2:
                h ^= (long) (str.charAt(tailStart + 1) & 0xff) << 8;
            case 1:
                h ^= (long) (str.charAt(tailStart) & 0xff);
                h *= M_64;
        }

        // finalization
        h ^= h >>> R_64;
        h *= M_64;
        h ^= h >>> R_64;

        return h;
    }
}
