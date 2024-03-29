package edu.hm.smartpower.dao;

import com.mysema.query.jpa.impl.JPAQuery;
import com.mysema.query.types.EntityPath;
import edu.hm.smartpower.domain.User;

import java.util.Collection;
import java.util.List;

/**
 * Defines CRUD (Create, Read, Update, Delete) actions, that can be performed on any @{@link javax.persistence.Entity}
 * annotated class.
 */
public interface GenericCrudDao {

    /**
     * Only to be used for newly created entities
     *
     * @param entity the entity to persist
     */
    void persist(Object entity);

    <T> T save(T entity);

    void save(List<?> entities);

    void save(Object... entities);

    Long getCount(Class<?> entityClass);

    <T> T getById(Class<T> entityClass, Object primaryKey);

    void delete(Object entity);

    public <T> List<T> getByIdIn(Class<T> entityClass, Collection<?> primaryKeys);

    <T> List<T> getByPropertyIn(Class<T> entityClass, String property, Collection<?> propertyValues);

	void detach(Object entity);

	<T> List<T> findAll(Class<T> entityClass);

	JPAQuery queryFrom(EntityPath<?> target);
}
