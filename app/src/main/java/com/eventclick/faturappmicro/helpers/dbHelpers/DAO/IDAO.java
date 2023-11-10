package com.eventclick.faturappmicro.helpers.dbHelpers.DAO;

import java.util.List;

/**
 * Interface para um Data Access Object (DAO) genérico
 *
 * @param <T> Modelo de objeto a ser utilizado
 */
public interface IDAO<T> {
    boolean save(T objeto);
    boolean update(T objeto);
    boolean delete(T objeto);
    T getById(int idItem);
    List<T> list();
}
