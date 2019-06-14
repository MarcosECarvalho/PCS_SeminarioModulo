package br.eic.sca.modules.sie.dao;

import java.util.List;

import br.eic.sca.modules.sie.domain.VersaoCurso;
import br.eic.sca.modules.sie.filters.VersaoCursoFilter;

public interface VersaoCursoDao extends _DaoSie<VersaoCurso>   
{
	public List<VersaoCurso> retrieveByVersaoCursoFilter(VersaoCursoFilter filter);	
}
