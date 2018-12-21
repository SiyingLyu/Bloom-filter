package com.company;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.StringTokenizer;

public class NaiveDifferential {
    private HashMap<String, String> dataMap;
    private File dataFile; // SourceFile is database
    private int lines;

    /**
     * Constructor.
     * @param dataFile the database file.
     * @throws IOException
     * The file is put in the dataMap
     */
    public NaiveDifferential(File dataFile) throws IOException {
        this.dataMap = new HashMap<>();
        this.dataFile = dataFile;
        this.lines = (int) Files.lines(Paths.get(dataFile.getPath())).count();
        BufferedReader br = new BufferedReader(new FileReader(dataFile));
        while(lines > 0) {
            String temp = br.readLine();

            if (checkLine(temp)){
                String[] inp = setUp(temp);
                dataMap.put(inp[0].trim(), inp[1]);
            }
            lines --;
        }
    }

    /**
     * Return the value of key from either diffMap or dataMap.
     * @param key the string from grams.txt
     * @return
     */
    public String retrieveRecord(String key) {
        if(dataMap.containsKey(key)) {
            return dataMap.get(key);
        }
        return null;
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
