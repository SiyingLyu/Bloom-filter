package com.company;

import java.util.ArrayList;

public class CMS {
    private static final long FNV1_64_INIT = 0xcbf29ce484222325L;
    private static final long FNV1_PRIME_64 = 1099511628211L;
    public int[][] cms;
    private int numOfHash;
    private int column;
    public ArrayList<String> set;

    /**
     * Constructor.
     * @param epsilon
     * @param delta
     * @param s list contains all the strings
     */
    public CMS(float epsilon, float delta, ArrayList<String> s) {
        this.numOfHash = (int) (Math.log(1/delta)/Math.log(2));
        this.column = (int) (2/epsilon);
        this.cms = new int[numOfHash][column];
        this.set = s;
        for (int i = 0; i < s.size(); i ++) {
            int[] tempFuncs = functions(s.get(i));
            for (int j = 0; j < tempFuncs.length; j ++) {
                cms[j][tempFuncs[j]] ++;
            }
        }
    }

    /**
     * Gives the approximate frequency of specific string s
     * @param x
     * @return
     */
    public int approximateFrequency(String x) {
        int[] result = new int[numOfHash];
        int[] temp = functions(x);

        for (int i = 0; i < temp.length; i++) {
            result[i] = cms[i][temp[i]];
        }

        int min = result[0];

        for (int j = 1; j < result.length; j++) {
            if (result[j] < min) {
                min = result[j];
            }
        }
        return min;
    }

    /**
     * Gives the list of element that are in the heavy hitter
     * @param q
     * @param r
     * @return
     */
    public ArrayList<String> approximateHeavyHitter(float q, float r) {
        ArrayList<String> hh = new ArrayList<>();
        int size = set.size();
        for  (int i = 0; i < size; i ++) {
            String item = set.get(i);
            int freq = approximateFrequency(item);
            if (freq >= size * q && !hh.contains(item)) {
                hh.add(item);
            }

            if (hh.contains(item)) {
                if (freq < size * r) {
                    hh.remove(item);
                }
            }
        }
        return hh;
    }

    public long fnv(String str) {
        long hash = FNV1_64_INIT;
        for (int j = 0; j < str.length(); j ++) {
            hash ^= str.charAt(j);
            hash *= FNV1_PRIME_64;
        }
        return hash;
    }

    public int[] functions(String str) {
        str = str.toLowerCase();
        int[] func = new int[numOfHash];
        for (int i = 0; i < numOfHash; i++) {
            long hash = fnv(str);
            hash = (hash % column + i * hash / column) % column;
            func[i] = (int) Math.abs(hash);
        }
        return func;
    }
}
