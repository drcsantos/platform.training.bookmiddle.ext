package com.ebsco.training.bookmiddle.repository;

import java.io.Serializable;
import java.util.List;

public interface Repository<T extends Serializable> {

    T findById(String id);

    List<T> find();

    T insert(T value);

    T update(T value);

    T delete(T value);

}
