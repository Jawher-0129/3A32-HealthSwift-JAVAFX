package com.example.noubez.Service;

import java.util.List;

public interface IService <T> {
    void add(T t);
    void delete(int id);

    void update(T c, int id);

    List<T>getAll();
    T getById(int id);
}



