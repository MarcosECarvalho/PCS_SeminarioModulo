package br.eic.sca.modules.sie.service;

import java.util.List;

import br.eic.sca.modules.sie.domain.Turma;
import br.eic.sca.modules.sie.filters.TurmaFilter;

public interface TurmaService extends _ServiceSie<Turma>
{
	List<Turma> retrieveByTurmaFilter(TurmaFilter filter);
}
