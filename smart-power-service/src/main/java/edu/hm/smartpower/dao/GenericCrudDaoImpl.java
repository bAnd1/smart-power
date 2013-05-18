package edu.hm.smartpower.dao;

import org.springframework.transaction.annotation.Transactional;

import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.Id;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Named
@Transactional(readOnly = true)
public class GenericCrudDaoImpl implements GenericCrudDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void persist(Object entity) {
        entityManager.persist(entity);
    }

    @Transactional(readOnly = false)
    @Override
    public <T> T save(T entity) {
        return entityManager.merge(entity);
    }

    @Transactional(readOnly = false)
    @Override
    public void save(List<?> entities) {
        for (Object entity : entities) {
            save(entity);
        }
    }

    @Override
    public void save(Object... entities) {
        save(Arrays.asList(entities));
    }

    @Override
    public Long getCount(Class<?> entityClass) {
        return entityManager.createQuery("SELECT count(*) FROM " + entityClass.getSimpleName(), Long.class)
                .getSingleResult();
    }

    @Transactional(readOnly = false)
    @Override
    public void delete(Object entity) {
        entityManager.remove(entity);
    }

    @Override
    public <T> T getById(Class<T> entityClass, Object primaryKey) {
        return entityManager.find(entityClass, primaryKey);
    }

    @Override
    public <T> List<T> getByPropertyIn(Class<T> entityClass, String property, Collection<?> propertyValues) {
        if (propertyValues.isEmpty()) {
            return Collections.emptyList();
        }
        CriteriaQuery<T> criteriaQuery = entityManager.getCriteriaBuilder().createQuery(entityClass);
        Root<T> from = criteriaQuery.from(entityClass);
        criteriaQuery.where(from.get(property).in(propertyValues));
        return entityManager.createQuery(criteriaQuery).getResultList();
    }

    @Override
    public <T> List<T> getByIdIn(Class<T> entityClass, Collection<?> primaryKeys) {
        return getByPropertyIn(entityClass, getPrimaryKeyName(entityClass), primaryKeys);
    }

    private static <T> String getPrimaryKeyName(Class<T> entityClass) {
        return rekGetPrimaryKeyName(entityClass, entityClass);
    }

    private static <T> String rekGetPrimaryKeyName(Class<T> entityClass, Class<? extends T> originalEntityClass) {
        for (Field field : entityClass.getDeclaredFields()) {
            if (field.getAnnotation(Id.class) != null) {
                return field.getName();
            }
        }
        Class<? super T> superclass = entityClass.getSuperclass();
        if (superclass != null) {
            return rekGetPrimaryKeyName(superclass, entityClass);
        }
        throw new IllegalArgumentException(originalEntityClass.getName() + " does not have an @Id Annotation");
    }
}
