package br.eic.sca.modules.sie.service.impl;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.eic.sca.modules.sie.dao.CursoDao;
import br.eic.sca.modules.sie.dao._DaoSie;
import br.eic.sca.modules.sie.domain.Curso;
import br.eic.sca.modules.sie.filters.CursoFilter;
import br.eic.sca.modules.sie.service.CursoService;

@Service
public class CursoServiceImpl extends _ServiceSieAbstract<Curso> implements CursoService 
{
	//
	// Injeção do DAO base
	//
	@Autowired
	CursoDao cursoDao;
		
	@Override	
	public _DaoSie<Curso> getBaseDao() 
	{
		return cursoDao;
	}
	
	//
	// Implementação
	//	
	@Override
	@Transactional
	public List<Curso> retrieveByFilter(CursoFilter filtro) 
	{
		return cursoDao.retrieveByFilter(filtro);
	}

	@Override
	public Curso retrieveBySigla(String sigla) 
	{
		return cursoDao.retrieveBySigla(sigla);
	}	
}