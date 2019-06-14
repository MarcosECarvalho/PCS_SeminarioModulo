package br.eic.sca.modules.sie.service;

import java.util.List;

import br.eic.sca.modules.sie.domain.Disciplina;
import br.eic.sca.modules.sie.filters.DisciplinaFilter;

public interface DisciplinaService extends _ServiceSie<Disciplina>  
{
	public Disciplina retrieveByCodigo(String codigo);
	public List<Disciplina> retrieveByDisciplinaFilter(DisciplinaFilter filter);		
}