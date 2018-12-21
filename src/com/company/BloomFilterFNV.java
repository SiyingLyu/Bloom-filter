package com.company;

import java.util.BitSet;

public class BloomFilterFNV implements BloomFilter{
    private static final long FNV1_64_INIT = 0xcbf29ce484222325L;
    private static final long FNV1_PRIME_64 = 1099511628211L;
    private BitSet hashTable;
    private int filterSize;
    public int numOfHash;
    private int numOfElem = 0;

    public BloomFilterFNV(int setSize, int bitsPerElement) {
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
            long hash = fnv(str);
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

    public long fnv(String str) {
        long hash = FNV1_64_INIT;
        for (int j = 0; j < str.length(); j ++) {
            hash ^= str.charAt(j);
            hash *= FNV1_PRIME_64;
        }
        return hash;
    }
}
