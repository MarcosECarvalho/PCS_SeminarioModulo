package br.eic.sca.modules.sie.service;

import java.util.List;

import br.eic.sca.modules.sie.domain.Aluno;
import br.eic.sca.modules.sie.domain.Inscricao;
import br.eic.sca.modules.sie.filters.InscricaoFilter;

public interface InscricaoService extends _ServiceSie<Inscricao>  
{
	public List<Aluno> retrieveAlunosByTurma(Integer turmaId);
	public List<Inscricao> retrieveByAluno(Integer alunoId);
	public List<Inscricao> retrieveByInscricaoFilter(InscricaoFilter filter);	
}