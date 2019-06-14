package br.eic.sca.modules.sie.dao;

import java.util.List;

import br.eic.sca.modules.sie.domain.Professor;
import br.eic.sca.modules.sie.domain.Turma;
import br.eic.sca.modules.sie.filters.TurmaFilter;

public interface TurmaDao extends _DaoSie<Turma>
{
	public List<Turma> retrieveByProfessor(Professor professor);
	public List<Turma> retrieveByTurmaFilter(TurmaFilter filter);
	public List<Turma> retrieveByTurmaFilterExactCodigo(TurmaFilter filter);
}
