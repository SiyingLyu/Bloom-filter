package com.company;

public interface BloomFilter {

    public int[] functions(String s);

    public void add(String s);

    public boolean appears(String s);

    public int dataSize();

    public int numHashes();

}
