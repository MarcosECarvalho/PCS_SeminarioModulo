package br.eic.sca.modules.sie.service.impl;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.eic.sca.modules.sie.dao.TurmaDao;
import br.eic.sca.modules.sie.dao._DaoSie;
import br.eic.sca.modules.sie.domain.Turma;
import br.eic.sca.modules.sie.filters.TurmaFilter;
import br.eic.sca.modules.sie.service.TurmaService;

@Service
public class TurmaServiceImpl extends _ServiceSieAbstract<Turma> implements TurmaService
{
	//
	// Injeção do DAO base
	//
	@Autowired
	TurmaDao turmaDao;

	@Override	
	public _DaoSie<Turma> getBaseDao() 
	{
		return turmaDao;
	}
	
	//
	// Implementação
	//
	@Override
	@Transactional
	public List<Turma> retrieveByTurmaFilter(TurmaFilter filter) 
	{
		return turmaDao.retrieveByTurmaFilter(filter);
	}	
}
