package com.company;

import java.util.BitSet;
import java.util.Random;

public class BloomFilterRan implements BloomFilter{
    public BitSet hashTable;
    public int numOfHash;
    private int numOfElem = 0;
    private int filterSize;
    private int p;
    private int[] hashA;
    private int[] hashB;

    public BloomFilterRan(int setSize, int bitsPerElement) {
        filterSize = prime(setSize * bitsPerElement);
        hashTable = new BitSet(filterSize);
        numOfHash = 2 * filterSize / setSize;
        for (int i = 0; i < filterSize; i ++) {
            hashTable.clear(i);
        }
        hashA = new int[numOfHash];
        hashB = new int[numOfHash];
        p = prime(filterSize);
        Random rand = new Random();
        for (int i = 0; i < numOfHash; i ++) {
            hashA[i] = rand.nextInt(p);
            hashB[i] = rand.nextInt(p);
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
            long hash = ran(str, hashA[i], hashB[i]);
            hash = hash % filterSize;
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

    public long ran(String str, int a, int b) {
        long x = str.hashCode();
        long hash = (a * x + b) % p;
        return hash;
    }

    public int prime(int start){
        if(isPrime(start)){
            return start;
        }
        return prime(start + 1);
    }

    public boolean isPrime(int n){
        for (int i = 2; i <=Math.sqrt(n) ; i++) {
            if(n%i==0)
                return false;
        }
        return true;
    }

}
