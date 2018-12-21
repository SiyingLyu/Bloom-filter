package com.company;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class EmpericalComparison {
    private BloomDifferential bd;
    private NaiveDifferential nd;
    private File diff;
    private File keys;
    private File data;
    private int keysLine;

    /**
     * Constructor.
     * @param diff the DiffFile.txt
     * @param keys the grams.txt
     * @param data the database.txt
     * @throws IOException
     * Construct BloomDifferential and NaiveDifferential with the given parameter
     */
    public EmpericalComparison(File diff, File keys, File data) throws IOException {
        this.diff = diff;
        this.keys = keys;
        this.data = data;
        this.keysLine = (int) Files.lines(Paths.get(keys.getPath())).count();
        this.bd = new BloomDifferential(diff,data);
        this.nd = new NaiveDifferential(data);
    }

    /**
     * Print the time for BloomDifferential and NaiveDifferential to retrieve n keys
     * @param bfname the name of bloom filter
     * @param bitsPerElement
     * @param n the number of tests
     * @throws IOException
     */
    public void compareTime(String bfname, int bitsPerElement, int n) throws IOException {
        if (n > keysLine) {
            throw new IllegalArgumentException("Exccess the maximum of keys!");
        }

        bd.createFilter(bfname, bitsPerElement);
        BufferedReader br1 = new BufferedReader(new FileReader(keys));
        int line1 = n;
        String task1;

        long t1 = System.currentTimeMillis();
        while(line1 > 0) {
            task1 = br1.readLine();
            bd.retrieveRecord(task1);
            line1 --;
        }
        long bdTime = System.currentTimeMillis() - t1;


        BufferedReader br2 = new BufferedReader(new FileReader(keys));
        String task2;
        int line2 = n;

        long t2 = System.currentTimeMillis();
        while(line2 > 0) {
            task2 = br2.readLine();
            nd.retrieveRecord(task2);
            line2 --;
        }
        long ndTime = System.currentTimeMillis() - t2;

        System.out.println("Running time for BloomDifferential: " + bdTime);
        System.out.println("Running time for NaiveDifferentual: " + ndTime);
    }


}
