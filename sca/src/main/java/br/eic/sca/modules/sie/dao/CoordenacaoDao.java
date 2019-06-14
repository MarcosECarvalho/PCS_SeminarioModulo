package br.eic.sca.modules.sie.dao;

import java.util.List;

import br.eic.sca.api.dao._DaoHibernate;
import br.eic.sca.modules.sie.domain.Coordenacao;
import br.eic.sca.modules.sie.domain.Curso;
import br.eic.sca.modules.sie.domain.Professor;

public interface CoordenacaoDao extends _DaoHibernate<Coordenacao>   
{
	public List<Coordenacao> retrieveByProfessor(Professor professor);
	public List<Coordenacao> retrieveByCurso(Curso curso);
	public List<Coordenacao> retrieveNew();
}
