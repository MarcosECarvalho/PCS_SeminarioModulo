package br.eic.sca.modules.sie.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.eic.sca.modules.sie.dao.VersaoCursoDao;
import br.eic.sca.modules.sie.dao._DaoSie;
import br.eic.sca.modules.sie.domain.VersaoCurso;
import br.eic.sca.modules.sie.filters.VersaoCursoFilter;
import br.eic.sca.modules.sie.service.VersaoCursoService;

@Service
public class VersaoCursoServiceImpl extends _ServiceSieAbstract<VersaoCurso> implements VersaoCursoService
{
	@Autowired
	VersaoCursoDao versaoCursoDao;
	
	//
	// Injeção do DAO base
	//
	@Override	
	public _DaoSie<VersaoCurso> getBaseDao() 
	{
		return versaoCursoDao;
	}
	
	//
	// Implementação
	//
	@Override
	public List<VersaoCurso> retrieveByVersaoCursoFilter(VersaoCursoFilter filter)
	{
		return versaoCursoDao.retrieveByVersaoCursoFilter(filter);
	}
}
