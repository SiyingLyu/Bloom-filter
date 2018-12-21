package com.company;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.StringTokenizer;

public class CMSTest {

    public static void main(String[] args) throws IOException {
        // write your code here
        ArrayList<String> set = new ArrayList<>();

        File file1 = new File("./data/shakespear.txt");
        BufferedReader br1 = new BufferedReader(new FileReader(file1));
        int numOfLine=(int) Files.lines(Paths.get(file1.getPath())).count();

        String line;
        while (numOfLine > 0) {
            line = br1.readLine();
            line = line.replaceAll("[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……& ;*（）——+|{}【】‘；：”“\"\"’?|-]", " ");
            StringTokenizer st = new StringTokenizer(line);
            String token;
            while (st.hasMoreTokens()) {
                token = st.nextToken();
                if (token.length() > 2 && !token.toLowerCase().equals("the")) {
                    set.add(token.toLowerCase().trim());
                }
            }
            numOfLine --;
        }


        CMS cms = new CMS((float) 1.0/100, (float) Math.pow(2,-20),set);

        ArrayList<String> HH = cms.approximateHeavyHitter((float) 0.04, (float) 0.03);

        System.out.println("Ands estimate: " + cms.approximateFrequency("and"));

        System.out.println(Arrays.toString(HH.toArray()));

    }
}
