package com.adse2509.mediconnect.dao;

import java.sql.SQLException;
import java.util.List;

/**
 * Generic Data Access Object interface defining the standard CRUD contract.
 * Demonstrates the use of Java Generics as specified in the ADSE 2509 syllabus.
 *
 * @param <T>  The domain entity type
 * @param <ID> The primary key identifier type
 * @author Group 3
 */
public interface GenericDAO<T, ID>
{
    boolean create(T entity) throws SQLException;
    T getById(ID id) throws SQLException;
    List<T> getAll() throws SQLException;
    boolean update(T entity) throws SQLException;
    boolean delete(ID id) throws SQLException;
}
