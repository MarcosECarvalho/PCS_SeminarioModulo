package br.eic.sca.modules.sie.service.impl;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.eic.sca.modules.sie.dao.DisciplinaDao;
import br.eic.sca.modules.sie.dao._DaoSie;
import br.eic.sca.modules.sie.domain.Disciplina;
import br.eic.sca.modules.sie.filters.DisciplinaFilter;
import br.eic.sca.modules.sie.service.DisciplinaService;

@Service
public class DisciplinaServiceImpl extends _ServiceSieAbstract<Disciplina> implements DisciplinaService 
{
	//
	// Injeção do DAO base
	//
	@Autowired
	DisciplinaDao disciplinaDao;
	
	@Override	
	public _DaoSie<Disciplina> getBaseDao() 
	{
		return disciplinaDao;
	}
	
	//
	// Implementação
	//
	@Override
	@Transactional
	public Disciplina retrieveByCodigo(String codigo) 
	{
		return disciplinaDao.retrieveByCodigo(codigo);
	}
	
	@Override
	@Transactional
	public List<Disciplina> retrieveByDisciplinaFilter(DisciplinaFilter filter) 
	{
		return disciplinaDao.retrieveByDisciplinaFilter(filter);
	}

	
	
}