package br.eic.sca.modules.sie.service;

import java.util.List;

import br.eic.sca.api.service._ServiceHibernate;
import br.eic.sca.modules.sie.domain.Coordenacao;
import br.eic.sca.modules.sie.domain.Curso;
import br.eic.sca.modules.sie.domain.Professor;

public interface CoordenacaoService extends _ServiceHibernate<Coordenacao>
{
	public List<Coordenacao> retrieveByCurso(Curso curso);
	public List<Coordenacao> retrieveByProfessor(Professor professor);			
}
