package com.martinfilliau.javaeetrucs.services;

import com.martinfilliau.javaeetrucs.data.BaseEntity;
import com.martinfilliau.javaeetrucs.services.utils.QueryParameter;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

/**
 * CRUD Service / Generic DAO
 * @author Martin Filliau
 * @see <a href="http://www.adam-bien.com/roller/abien/entry/generic_crud_service_aka_dao">Adam Bien blog, Generic CRUD service</a>
 */
@Stateless
@TransactionAttribute(TransactionAttributeType.MANDATORY)
public class CrudService {

    @PersistenceContext(unitName = "javaeetrucsPu")
    private EntityManager em;

    /**
     * Persist a new entity
     * @param object - entity to persist
     * @return managed entity
     */
    public <T extends BaseEntity> T create(final T object) {
        this.em.persist(object);
        this.em.flush();
        this.em.refresh(object);
        return object;
    }

    /**
     * Find an entity
     * @param type - the type of the entity
     * @param id - id of the entity to retrieve
     * @return managed entity
     */
    public <T extends BaseEntity> T get(Class<T> type, Object id) {
        return (T) this.em.find(type, id);
    }

    /**
     * Delete an entity
     * @param type - type of the entity
     * @param id - id of the entity
     */
    public void delete(Class type, Object id) {
        Object ref = this.em.getReference(type, id);
        this.em.remove(ref);
    }

    /**
     * Merge an entity
     * @param object - entity to merge
     * @return managed entity
     */
    public <T extends BaseEntity> T update(T object) {
        this.em.merge(object);
        this.em.flush();
        return (T) object;
    }

    /**
     * Get all the entities for a given type
     * WARNING - this query may be very expensive for the database.
     * @param type - type of the entity
     * @return list of entities
     */
    public <T extends BaseEntity> List<T> getAll(Class<T> type) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<T> c = cb.createQuery(type);
        c.from(type);
        TypedQuery<T> q = em.createQuery(c);
        return q.getResultList();
    }

    /**
     * Count the number of entities for a given type
     * @param type - type of the entity
     * @return count of entities
     */
    public <T extends BaseEntity> Long countAll(Class<T> type) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Long> query = cb.createQuery(Long.class);
        Root<T> r = query.from(type);
        query.select(cb.count(r));
        TypedQuery<Long> tq = em.createQuery(query);
        return tq.getSingleResult();
    }

    /**
     * Get a subset of the entities for a given type
     * @param type - type of the entity
     * @param start - first result to retrieve
     * @param resultLimit - maximum result to retrieve
     * @return list of entities
     */
    public <T extends BaseEntity> List<T> getAllSubSet(Class<T> type, int start, int resultLimit) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<T> c = cb.createQuery(type);
        c.from(type);
        TypedQuery<T> q = em.createQuery(c);
        q.setFirstResult(start);
        q.setMaxResults(resultLimit);
        return q.getResultList();
    }

    /**
     * Find all results for a given query
     * @param cq - criteria query
     * @return list of results
     */
    public <T extends BaseEntity> List<T> findWithCriteriaQuery(CriteriaQuery<T> cq) {
        TypedQuery<T> q = em.createQuery(cq);
        return q.getResultList();
    }

    /**
     * Find first result of the given query
     * @param cq - criteria query
     * @return single (first) result of the query or {@link NoResultException} if there is no result
     */
    public <T extends BaseEntity> T findSingleWithCriteriaQuery(CriteriaQuery<T> cq) {
        TypedQuery<T> q = em.createQuery(cq);
        return q.getSingleResult();     // TODO should we return null instead ?
    }

    /**
     * Find all results for a given query
     * @param namedQueryName - name of the named query
     * @return list of entities
     */
    public List findWithNamedQuery(String namedQueryName) {
        return this.em.createNamedQuery(namedQueryName).getResultList();
    }

    /**
     * Find all results for a given query
     * @param namedQueryName - name of the named query
     * @param parameters - Map of <parameter name>, <parameter value>
     * @return list of entities
     */
    public List findWithNamedQuery(String namedQueryName, QueryParameter qp) {
        return findWithNamedQuery(namedQueryName, qp, 0, 0);
    }

    /**
     * Find a sub-part of results for a given query
     * @param queryName - name of the named query
     * @param start - first result to retrieve
     * @param resultLimit - maximum result to retrieve
     * @return list of entities
     */
    public List findWithNamedQuery(String queryName, int start, int resultLimit) {
        return this.em.createNamedQuery(queryName).
                setFirstResult(start).
                setMaxResults(resultLimit).
                getResultList();
    }

    /**
     * Find a sub-part of results for a given entity
     * @param namedQueryName - name of the named query
     * @param parameters - Map of <parameter name>, <parameter value>
     * @param start - first result to retrieve
     * @param resultLimit - maximum result to retrieve
     * @return list of entities
     */
    public List findWithNamedQuery(String namedQueryName, QueryParameter qp, int start, int resultLimit) {
        Set<Map.Entry<String, Object>> rawParameters = qp.parameters().entrySet();
        Query query = this.em.createNamedQuery(namedQueryName);
        if (start > 0) {
            query.setFirstResult(start);
        }
        if (resultLimit > 0) {
            query.setMaxResults(resultLimit);
        }
        for (Map.Entry<String, Object> entry : rawParameters) {
            query.setParameter(entry.getKey(), entry.getValue());
        }
        return query.getResultList();
    }

    /**
     * Count rows for a given query
     * NOTE: the query must be a SELECT COUNT(*)
     * @param namedQueryName the name of the named query
     * @return rows count (first result of the query) or throws exception if there is no result
     */
    public long countWithNamedQuery(String namedQueryName) {
        return (Long) this.em.createNamedQuery(namedQueryName).getSingleResult();
    }

    /**
     * Count rows for a given query
     * NOTE: the query must be a SELECT COUNT(*)
     * @param namedQueryName the name of the named query
     * @param qp parameters of the query
     * @return rows count (first result of the query) or throws exception if there is no result
     */
    public long countWithNamedQuery(String namedQueryName, QueryParameter qp) {
        Set<Map.Entry<String, Object>> rawParameters = qp.parameters().entrySet();
        Query query = this.em.createNamedQuery(namedQueryName);
        for (Map.Entry<String, Object> entry : rawParameters) {
            query.setParameter(entry.getKey(), entry.getValue());
        }
        return (Long) query.getSingleResult();
    }

    /**
     * Find the first result (if it exists) of a query
     * @param namedQueryName name of the named query
     * @return first result or null if there is no result
     */
    public <T extends BaseEntity> T findFirstResultWithNamedQuery(String namedQueryName) {
        List list = findWithNamedQuery(namedQueryName, 0, 2);
        if (list != null && list.size() > 0) {
            return (T) list.get(0);
        }
        return null;
    }

    /**
     * Find the first result (if it exists) of a query
     * @param namedQueryName name of the named query
     * @param qp parameters
     * @return first result or null if there is no result
     */
    public <T extends BaseEntity> T findFirstResultWithNamedQuery(String namedQueryName, QueryParameter qp) {
        List list = findWithNamedQuery(namedQueryName, qp, 0, 2);
        if (list != null && list.size() > 0) {
            return (T) list.get(0);
        }
        return null;
    }

    /**
     * Create a custom JPQL query
     * @param query query
     * @return list
     */
    public List createQuery(String query) {
        return em.createQuery(query).getResultList();
    }

}
