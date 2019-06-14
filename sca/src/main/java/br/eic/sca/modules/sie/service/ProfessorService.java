package br.eic.sca.modules.sie.service;

import java.util.List;

import br.eic.sca.modules.sie.domain.Professor;
import br.eic.sca.modules.sie.filters.ProfessorFilter;

public interface ProfessorService extends _ServiceSie<Professor>
{
	public Professor retrieveByMatricula(String matricula);
	public List<Professor> retrieveByNome(String filtroNome);
	public List<Professor> retrieveByProfessorFilter(ProfessorFilter filter);
}
