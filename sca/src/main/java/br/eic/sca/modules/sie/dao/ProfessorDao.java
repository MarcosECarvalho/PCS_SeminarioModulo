package br.eic.sca.modules.sie.dao;

import java.util.List;

import br.eic.sca.modules.sie.domain.Professor;
import br.eic.sca.modules.sie.filters.ProfessorFilter;

public interface ProfessorDao extends _DaoSie<Professor>
{
	public Professor retrieveByMatricula(String matricula);
	public List<Professor> retrieveByNome(String filtroNome);
	public List<Professor> retrieveByProfessorFilter(ProfessorFilter filter);
}
