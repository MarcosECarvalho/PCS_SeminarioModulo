package br.eic.sca.api.dao.impl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.orm.hibernate5.HibernateTemplate;

import br.eic.sca.api.dao._DaoHibernate;
import br.eic.sca.api.dao.query.DaoLike;

/**
 * @author Rafael Castaneda (rafael.ribeiro@cefet-rj.br)
 * <p>
 * Implementação abstrata da interface {@link br.eic.sca.api.dao._DaoHibernate _DaoHibernate}.
 * É utilizada para facilitar a implementação dos DAO's baseados em Hibernate/JPA.
 * <p>
 */
public abstract class _DaoHibernateAbstract<ENTITY_TYPE> implements _DaoHibernate<ENTITY_TYPE> 
{
	//
	// Dependências
	//
	@Autowired
	protected SessionFactory sessionFactory;
	@Autowired
	protected HibernateTemplate hibernateTemplate;
	
	//
	// Métodos Auxiliares
	//
	public abstract Class<ENTITY_TYPE> getEntityClass();
	
	@Override
	public void flushSession() 
	{
		sessionFactory.getCurrentSession().flush();
	}
	
	//
	// Operações
	//
	@Override
	public ENTITY_TYPE retrieveById(Integer id) 
    {
    	ENTITY_TYPE entity = (ENTITY_TYPE)sessionFactory.getCurrentSession().get(getEntityClass(), id);
        return entity;
    }		
	
    @Override
	public List<ENTITY_TYPE> retrieveAll() 
	{		
		return (List<ENTITY_TYPE>)hibernateTemplate.loadAll(getEntityClass());		
	}
	
	@Override
	@SuppressWarnings({ "deprecation", "unchecked" })
	public List<ENTITY_TYPE> retrieveByLikeInSingleField(String field,String value,MatchMode matchMode) 
	{
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(getEntityClass());
		
		if (field == null || field.isEmpty())
			throw new IllegalArgumentException("Field argument cannot be null or empty");

		if (value != null && !value.isEmpty())
			criteria.add(Restrictions.ilike(field,value,matchMode));
			
		List<ENTITY_TYPE> result = criteria.list();
		return result;	
	}
	
	@Override
	@SuppressWarnings({ "deprecation", "unchecked" })
	public List<ENTITY_TYPE> retrieveByLikeInManyFields(DaoLike... daoLikes)
	{
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(getEntityClass());
		
		for (DaoLike daoLike : daoLikes) 
		{
			String    field = daoLike.getField();
			String    value = daoLike.getValue();
			MatchMode mmode = daoLike.getMatchMode();
			
			if (field == null || field.isEmpty())
				throw new IllegalArgumentException("Field argument cannot be null or empty");

			if (value != null && !value.isEmpty())
				criteria.add(Restrictions.ilike(field,value,mmode));			
		}
		
		List<ENTITY_TYPE> result = criteria.list();
		return result;	
	}
	
	@Override
    public void persist(ENTITY_TYPE object) throws DataAccessException 
    {
    	sessionFactory.getCurrentSession().saveOrUpdate(object);    	
    }
          
    	
    @Override
	public void delete(ENTITY_TYPE object) 
	{
		sessionFactory.getCurrentSession().delete(object);
	}
	
}