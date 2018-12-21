package com.company;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.StringTokenizer;

public class BloomDifferential {
    private HashMap<String, String> diffMap;
    private HashMap<String, String> dataMap;
    private File sourceFile; // SourceFile is DiffFile
    private File dataFile;
    private int diffLines;
    private int dataLines;
    private BloomFilter bf;

    /**
     * Constructor.
     * @param sourceFile the differential file.
     * @param dataFile the database file.
     * @throws IOException
     * The two files are put in HashMaps, diffMap and dataMap, respectively.
     */

    public BloomDifferential(File sourceFile, File dataFile) throws IOException {
        this.diffMap = new HashMap<>();
        this.dataMap = new HashMap<>();

        this.sourceFile = sourceFile;
        this.diffLines = (int) Files.lines(Paths.get(sourceFile.getPath())).count();

        this.dataFile = dataFile;
        this.dataLines = (int) Files.lines(Paths.get(dataFile.getPath())).count();
        BufferedReader br = new BufferedReader(new FileReader(dataFile));
        while(dataLines > 0) {
            String temp1 = br.readLine();

            if (checkLine(temp1)){
                String[] inp = setUp(temp1);
                dataMap.put(inp[0].trim(), inp[1]);
            }
            dataLines --;
        }

        BufferedReader brSource = new BufferedReader(new FileReader(sourceFile));

        while(diffLines > 0) {
            String temp2 = brSource.readLine();

            if (checkLine(temp2)){
                String[] inp = setUp(temp2);
                diffMap.put(inp[0].trim(), inp[1]);
                if (bf != null) {bf.add(inp[0]);}
            }

            diffLines --;
        }
    }

    /**
     * Create the bloom filter with the data from dataMap.
     * @param bfname bfname the name of bloom filter, i.e. fnv, murmur and ran.
     * @param bitsPerElement the bits per element for the construction of bloom filter.
     * @throws IOException
     */

    public void createFilter(String bfname, int bitsPerElement) throws IOException {
        int setSize = (int) Files.lines(Paths.get(sourceFile.getPath())).count();
        bfname = bfname.toLowerCase();
        switch (bfname){
            case "fnv": bf = new BloomFilterFNV(setSize, bitsPerElement);
            case "murmur": bf = new BloomFilterMurmur(setSize, bitsPerElement);
            case "ran": bf = new BloomFilterRan(setSize, bitsPerElement);
        }
    }

    /**
     * Return the value of key from either diffMap or dataMap.
     * @param key the string from grams.txt
     * @return
     */
    public String retrieveRecord(String key){
        if (bf != null && bf.appears(key)) {
            return diffMap.get(key);
        }
        return dataMap.get(key);
    }

    /**
     * Split the line into [key, value] format. The key contains the first four words. The rest of the String
     * is treated as value.
     * @param s string of line from document
     * @return two element array.
     */
    public String[] setUp(String s) {
        String[] result = new String[2];
        StringTokenizer st = new StringTokenizer(s);
        String key = "";
        String value = "";
        for(int i = 0; i<4; i++) {
            key = key + st.nextToken() + " ";
        }
        while(st.hasMoreTokens()) {
            value = value + st.nextToken() + " ";
        }
        result[0] = key;
        result[1] = value;
        return result;
    }

    /**
     * Check if the string length is less than 3 or is equals to "the"
     * @param s
     * @return
     */
    public boolean checkLine(String s) {
        StringTokenizer st = new StringTokenizer(s);
        if (st.countTokens() > 3) {return true;}
        return false;
    }
}
