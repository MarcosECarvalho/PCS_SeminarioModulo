package br.eic.sca.modules.sie.service.impl;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.eic.sca.api.dao._DaoHibernate;
import br.eic.sca.api.service.impl._ServiceHibernateAbstract;
import br.eic.sca.modules.sie.dao.CoordenacaoDao;
import br.eic.sca.modules.sie.dao.CursoDao;
import br.eic.sca.modules.sie.dao.ProfessorDao;
import br.eic.sca.modules.sie.domain.Coordenacao;
import br.eic.sca.modules.sie.domain.Curso;
import br.eic.sca.modules.sie.domain.Professor;
import br.eic.sca.modules.sie.service.CoordenacaoService;

@Service
public class CoordenacaoServiceImpl extends _ServiceHibernateAbstract<Coordenacao> implements CoordenacaoService 
{
	//
	// Dependências
	//
	@Autowired
	CoordenacaoDao coordenacaoDao;
	
	@Autowired
	CursoDao cursoDao;
	
	@Autowired
	ProfessorDao professorDao;
	
	//
	// Injeção do DAO base
	//
	@Override	
	public _DaoHibernate<Coordenacao> getBaseDao() 
	{
		return coordenacaoDao;
	}
	
	//
	// Operações Próprias
	//
	@Override
	@Transactional
	public List<Coordenacao> retrieveByProfessor(Professor professor) 
	{
		return coordenacaoDao.retrieveByProfessor(professor);
	}

	@Override
	@Transactional
	public List<Coordenacao> retrieveByCurso(Curso curso) 
	{
		return coordenacaoDao.retrieveByCurso(curso);
	}	
}
