package com.interset.DataIntegrationTest.extractor;

public interface Extractor<I> {
    public void extract(I input);
}