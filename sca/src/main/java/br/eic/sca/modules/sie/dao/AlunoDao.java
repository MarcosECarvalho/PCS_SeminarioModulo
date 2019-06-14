package br.eic.sca.modules.sie.dao;

import java.util.List;

import br.eic.sca.modules.sie.domain.Aluno;
import br.eic.sca.modules.sie.filters.AlunoFilter;

public interface AlunoDao extends _DaoSie<Aluno>
{
	public Aluno retrieveByMatricula(String matricula);
	public List<Aluno> retrieveByNome(String nome);
	public List<Aluno> retrieveByAlunoFilter(AlunoFilter filter);
}
