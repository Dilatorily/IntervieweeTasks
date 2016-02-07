package com.interset.DataIntegrationTest.loader;

public interface Loader<I> {
    public void transform(I input);
}