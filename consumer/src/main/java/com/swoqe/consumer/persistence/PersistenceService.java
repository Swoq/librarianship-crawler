package com.swoqe.consumer.persistence;

public interface PersistenceService<T> {
    T persist(T entity);
    Iterable<T> persistAll(Iterable<T> entities);
}
