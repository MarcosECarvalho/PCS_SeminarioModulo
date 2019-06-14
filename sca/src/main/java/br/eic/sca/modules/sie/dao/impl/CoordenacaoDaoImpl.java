package br.eic.sca.modules.sie.dao.impl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import br.eic.sca.api.dao.impl._DaoHibernateAbstract;
import br.eic.sca.modules.sie.dao.CoordenacaoDao;
import br.eic.sca.modules.sie.domain.Coordenacao;
import br.eic.sca.modules.sie.domain.Curso;
import br.eic.sca.modules.sie.domain.Professor;

@Repository
public class CoordenacaoDaoImpl extends _DaoHibernateAbstract<Coordenacao> implements CoordenacaoDao 
{
	//
	// Injeção da classe alvo 
	//
	@Override	
	public Class<Coordenacao> getEntityClass() 
	{
		return Coordenacao.class;
	}

	@Override
	@SuppressWarnings({ "deprecation", "unchecked" })
	public List<Coordenacao> retrieveByProfessor(Professor professor) 
	{
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Coordenacao.class);
        
        if (professor != null && professor.getId()!=null)
        	criteria.add(Restrictions.eq("professorId", professor.getId()));
        else
        	return null;
        	
        List<Coordenacao> coordenacaoList = criteria.list();
        
        if (coordenacaoList.isEmpty())
        	return null;
        else
        	return coordenacaoList;
	}

	@Override
	@SuppressWarnings({ "deprecation", "unchecked" })
	public List<Coordenacao> retrieveByCurso(Curso curso) 
	{
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Coordenacao.class);
        
        if (curso != null && curso.getId()!=null)
        	criteria.add(Restrictions.eq("cursoId", curso.getId()));
        else
        	return null;
        	
        List<Coordenacao> coordenacaoList = criteria.list();
        
        if (coordenacaoList.isEmpty())
        	return null;
        else
        	return coordenacaoList;
	}

	@Override
	public List<Coordenacao> retrieveNew()
	{
		return (List<Coordenacao>)hibernateTemplate.loadAll(getEntityClass());
	}	
}
