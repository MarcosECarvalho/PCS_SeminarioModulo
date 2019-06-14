package br.eic.sca.modules.sie.dao;

import java.util.List;

import br.eic.sca.modules.sie.domain.Disciplina;
import br.eic.sca.modules.sie.filters.DisciplinaFilter;

public interface DisciplinaDao extends _DaoSie<Disciplina>   
{
	public Disciplina retrieveByCodigo(String codigo);
	public List<Disciplina> retrieveByDisciplinaFilter(DisciplinaFilter filter);
}
