package com.eventclick.faturappmicro.helpers.dbHelpers.DAO;

import java.util.List;

public interface IDAO<T> {
    public boolean save(T objeto);
    public boolean update(T objeto);
    public boolean delete(T objeto);
    public List<T> list();
}
