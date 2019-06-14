package br.eic.sca.modules.sie.dao;

import java.util.List;

import br.eic.sca.modules.sie.domain.Curso;
import br.eic.sca.modules.sie.filters.CursoFilter;

public interface CursoDao extends _DaoSie<Curso>   
{
	public Curso retrieveBySigla(String filtroNome);	
	public List<Curso> retrieveByFilter(CursoFilter filter);
}
