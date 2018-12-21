package com.company;


import java.util.ArrayList;
import java.util.Random;

public class FalsePositive {
    private ArrayList<String> set = new ArrayList<>();
    private ArrayList<String> testSet = new ArrayList<>();
    private BloomFilter bf;

    /**
     * Construct two sets.
     * @param setSize the size of set which is applied to bloom filter.
     * @param testSize the size of testSet which has no common elements with set.
     */
    public FalsePositive(int setSize, int testSize) {

        for (int i = 0; i < setSize; i ++) {
            set.add(generater());
        }
        for (int i = 0; i < testSize; i ++) {
            String temp = generater();
            if (!set.contains(temp)) {
                testSet.add(temp);
            }
        }
    }

    /**
     * Generate the string randomly.
     * @return
     */
    public String generater() {
        int leftLimit = 97; // letter 'a'
        int rightLimit = 122; // letter 'z'
        Random ran = new Random();
        int targetStringLength = ran.nextInt(12);
        StringBuilder buffer = new StringBuilder(targetStringLength);
        for (int i = 0; i < targetStringLength; i++) {
            int randomLimitedInt = leftLimit + (int)
                    (ran.nextFloat() * (rightLimit - leftLimit + 1));
            buffer.append((char) randomLimitedInt);
        }
        String result = buffer.toString();

        return result;
    }

    /**
     * Print the false probability of given bloom filter with given bitsPerElement.
     * @param bfname
     * @param bitsPerElement
     */
    public void testfp(String bfname, int bitsPerElement) {
        float result;
        bfname = bfname.toLowerCase();
        switch (bfname){
            case "fnv": bf = new BloomFilterFNV(set.size(), bitsPerElement);
            case "murmur": bf = new BloomFilterMurmur(set.size(), bitsPerElement);
            case "ran": bf = new BloomFilterRan(set.size(), bitsPerElement);
        }

        for (int i = 0; i < set.size(); i ++) {
            bf.add(set.get(i));
        }

        int fp = 0;
        for (int i = 0; i < testSet.size(); i ++) {
            if(bf.appears(testSet.get(i))){
                fp ++;
            }
        }

        result = (float) fp / testSet.size();
        System.out.println("The theoretical false positive is " + Math.pow(0.617,bitsPerElement));
        System.out.println(bfname.toUpperCase() + " Bloom Filter with bitsPerElement of " +
                bitsPerElement + " gives the false positive of " +result);
    }
}
