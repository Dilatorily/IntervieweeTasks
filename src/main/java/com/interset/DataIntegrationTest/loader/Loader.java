package com.interset.DataIntegrationTest.loader;

public interface Loader<I> {
    public void load(I input);
}