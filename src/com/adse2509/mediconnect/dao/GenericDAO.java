/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.adse2509.mediconnect.dao;

import java.sql.SQLException;
import java.util.List;

/**
 * Generic DAO interface defining standard CRUD operations.
 *
 * @param <T>  Entity type
 * @param <ID> Primary key type
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
