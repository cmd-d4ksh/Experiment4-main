package com.dhruvijain.deliveryplanner.dao;

import java.util.List;

public interface BaseDAO<T> {
    void create(T t);
    T read(int id);
    List<T> readAll();
    void update(T t);
    void delete(int id);
}
