package com.github.cutly.rest.model;

public class Hash {

    private String hash;

    public Hash() {

    }

    public Hash(String hash) {
        this.hash = hash;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    @Override
    public String toString() {
        return "Hash{" +
                "hash='" + hash + '\'' +
                '}';
    }
}
