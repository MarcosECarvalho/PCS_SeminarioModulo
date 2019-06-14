package br.eic.sca.modules.sie.service;

import java.util.List;

import br.eic.sca.modules.sie.domain.Aluno;
import br.eic.sca.modules.sie.filters.AlunoFilter;

public interface AlunoService extends _ServiceSie<Aluno>  
{
	public Aluno retrieveByMatricula(String matricula);
	public List<Aluno> retrieveByNome(String nome);
	public List<Aluno> retrieveByAlunoFilter(AlunoFilter filter);	
}