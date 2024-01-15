package com.hit.model;

import java.io.IOException;

public class ModelSingleton {
    private static final Model instance;

    static {
        try {
            instance = new Model();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static Model getInstance(){
        return instance;
    }
}
