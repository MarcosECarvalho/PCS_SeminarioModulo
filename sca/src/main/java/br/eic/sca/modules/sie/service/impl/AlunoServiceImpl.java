package br.eic.sca.modules.sie.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.eic.sca.modules.sie.dao.AlunoDao;
import br.eic.sca.modules.sie.dao._DaoSie;
import br.eic.sca.modules.sie.domain.Aluno;
import br.eic.sca.modules.sie.filters.AlunoFilter;
import br.eic.sca.modules.sie.service.AlunoService;

@Service
public class AlunoServiceImpl extends _ServiceSieAbstract<Aluno> implements AlunoService 
{
	//
	// Injeção do DAO base
	//
	@Autowired
	AlunoDao alunoDao;
	
	@Override
	public _DaoSie<Aluno> getBaseDao() 
	{
		return alunoDao;
	}
	
	//
	// Implementação
	//
	@Override	
	public Aluno retrieveByMatricula(String matricula) 
	{
		return alunoDao.retrieveByMatricula(matricula);
	}
	
	@Override
	public List<Aluno> retrieveByNome(String nome) 
	{
		return alunoDao.retrieveByNome(nome);
	}	
	
	@Override
	public List<Aluno> retrieveByAlunoFilter(AlunoFilter filter)
	{
		return alunoDao.retrieveByAlunoFilter(filter);
	}
}