package br.eic.sca.modules.sie.service;

import java.util.List;

import br.eic.sca.modules.sie.domain.VersaoCurso;
import br.eic.sca.modules.sie.filters.VersaoCursoFilter;

public interface VersaoCursoService extends _ServiceSie<VersaoCurso>
{
	public List<VersaoCurso> retrieveByVersaoCursoFilter(VersaoCursoFilter filter);	
}
