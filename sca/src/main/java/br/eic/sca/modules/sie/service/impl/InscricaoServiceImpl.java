package br.eic.sca.modules.sie.service.impl;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.eic.sca.modules.sie.dao.InscricaoDao;
import br.eic.sca.modules.sie.dao._DaoSie;
import br.eic.sca.modules.sie.domain.Aluno;
import br.eic.sca.modules.sie.domain.Inscricao;
import br.eic.sca.modules.sie.filters.InscricaoFilter;
import br.eic.sca.modules.sie.service.InscricaoService;

@Service
public class InscricaoServiceImpl extends _ServiceSieAbstract<Inscricao> implements InscricaoService 
{
	//
	// Injeção do DAO base
	//
	@Autowired
	InscricaoDao inscricaoDao;

	@Override	
	public _DaoSie<Inscricao> getBaseDao() 
	{
		return inscricaoDao;
	}
	
	//
	// Implementação
	//
	@Override
	@Transactional
	public List<Aluno> retrieveAlunosByTurma(Integer turmaId) 
	{
		return inscricaoDao.retrieveAlunosByTurma(turmaId);
	}
	
	@Override
	@Transactional
	public List<Inscricao> retrieveByInscricaoFilter(InscricaoFilter filter) 
	{
		return inscricaoDao.retrieveByInscricaoFilter(filter);
	}

	@Override
	@Transactional
	public List<Inscricao> retrieveByAluno(Integer alunoId) 
	{
		return inscricaoDao.retrieveByAluno(alunoId);
	}
}