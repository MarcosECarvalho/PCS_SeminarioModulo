package br.eic.sca.modules.sie.dao;

import java.util.List;

import br.eic.sca.modules.sie.domain.Aluno;
import br.eic.sca.modules.sie.domain.Inscricao;
import br.eic.sca.modules.sie.domain.Turma;
import br.eic.sca.modules.sie.filters.InscricaoFilter;

public interface InscricaoDao extends _DaoSie<Inscricao>
{
	public List<Inscricao> retrieveByAluno(Integer alunoId);
	public List<Aluno> retrieveAlunosByTurma(Integer turmaId);
	public List<Turma> retrieveTurmasByAluno(Aluno aluno);
	public List<Inscricao> retrieveByInscricaoFilter(InscricaoFilter filter);
}