package com.hit.dao;

import java.util.ArrayList;

public interface IDao<ID extends java.io.Serializable, T> {

    ArrayList<String> getAll();
    T find(ID id) throws IllegalArgumentException;
    boolean save(T object) throws IllegalArgumentException;
}
