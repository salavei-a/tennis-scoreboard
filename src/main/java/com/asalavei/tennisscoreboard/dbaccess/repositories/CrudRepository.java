package com.asalavei.tennisscoreboard.dbaccess.repositories;

public interface CrudRepository<T> {
    T save(T entity);
}
