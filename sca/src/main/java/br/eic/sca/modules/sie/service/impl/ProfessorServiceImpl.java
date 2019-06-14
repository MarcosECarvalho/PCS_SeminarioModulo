package br.eic.sca.modules.sie.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.eic.sca.modules.sie.dao.ProfessorDao;
import br.eic.sca.modules.sie.dao._DaoSie;
import br.eic.sca.modules.sie.domain.Professor;
import br.eic.sca.modules.sie.filters.ProfessorFilter;
import br.eic.sca.modules.sie.service.ProfessorService;

@Service
public class ProfessorServiceImpl extends _ServiceSieAbstract<Professor> implements ProfessorService
{
	//
	// Injeção do DAO base
	//
	@Autowired
	ProfessorDao professorDao;

	@Override	
	public _DaoSie<Professor> getBaseDao() 
	{
		return professorDao;
	}
	
	//
	// Implementação
	//
	@Override
	public List<Professor> retrieveByNome(String filtroNome) 
	{
		return professorDao.retrieveByNome(filtroNome);
	}
	
	@Override
	public Professor retrieveByMatricula(String matricula) 
	{
		return professorDao.retrieveByMatricula(matricula);
	}
	
	@Override
	public List<Professor> retrieveByProfessorFilter(ProfessorFilter filter) 
	{
		return professorDao.retrieveByProfessorFilter(filter);
	}	
}
