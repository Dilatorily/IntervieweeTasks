package com.interset.DataIntegrationTest.transformer;

public interface Transformer<I, O> {
    public O transform(I input);
}