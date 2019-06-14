package br.eic.sca.api.service.impl;

import java.util.List;

import javax.transaction.Transactional;

import org.hibernate.criterion.MatchMode;

import br.eic.sca.api.dao._DaoHibernate;
import br.eic.sca.api.dao.query.DaoLike;
import br.eic.sca.api.service._ServiceHibernate;

/**
 * @author Rafael Castaneda (rafael.ribeiro@cefet-rj.br)
 * <p>
 * Implementação abstrata da interface {@link br.eic.sca.api.service._ServiceHibernate _ServiceHibernate}.
 * É utilizada para facilitar a implementação dos Serviços baseados em Hibernate/JPA.
 * <p>
 * Todos os métodos implementados ou sobrescritos na camada de serviço devem conter a anotação 
 * '@Transactional' para que integrem corretamente o controle de transações do framework Spring.
 */
public abstract class _ServiceHibernateAbstract<ENTITY_TYPE> implements _ServiceHibernate<ENTITY_TYPE>   
{
	public abstract _DaoHibernate<ENTITY_TYPE> getBaseDao();
	
	@Override
	@Transactional
	public void flushSession() 
	{
		getBaseDao().flushSession();		
	}
	
	@Override
	@Transactional    
    public void persist(ENTITY_TYPE object)
    {
		getBaseDao().persist(object);
    }
	
	@Override
	@Transactional	    
	public void delete(ENTITY_TYPE object)
	{
		getBaseDao().delete(object);
	}
	
	@Override
	@Transactional
	public ENTITY_TYPE retrieveById(Integer id) 
	{
		return getBaseDao().retrieveById(id);
	}

	@Override
	@Transactional
	public List<ENTITY_TYPE> retrieveAll() 
	{
		return getBaseDao().retrieveAll();
	}	
	
	@Override
	@Transactional
	public List<ENTITY_TYPE> retrieveByLikeInSingleField(String field, String value, MatchMode matchMode) 
	{
		return getBaseDao().retrieveByLikeInSingleField(field, value, matchMode);
	}

	@Override
	@Transactional
	public List<ENTITY_TYPE> retrieveByLikeInManyFields(DaoLike... daoLikes) 
	{
		return getBaseDao().retrieveByLikeInManyFields(daoLikes);
	}
}
