package br.eic.sca.modules.sie.service;

import java.util.List;

import br.eic.sca.modules.sie.domain.Curso;
import br.eic.sca.modules.sie.filters.CursoFilter;

public interface CursoService extends _ServiceSie<Curso>  
{
	public List<Curso> retrieveByFilter(CursoFilter filtro);
	public Curso retrieveBySigla(String sigla);
}