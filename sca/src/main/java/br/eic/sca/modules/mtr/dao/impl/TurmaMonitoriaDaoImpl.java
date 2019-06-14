package br.eic.sca.modules.mtr.dao.impl;

import org.springframework.stereotype.Repository;

import br.eic.sca.api.dao.impl._DaoHibernateAbstract;
import br.eic.sca.modules.mtr.dao.TurmaMonitoriaDao;
import br.eic.sca.modules.mtr.domain.TurmaMonitoria;

@Repository
public class TurmaMonitoriaDaoImpl extends _DaoHibernateAbstract<TurmaMonitoria> implements TurmaMonitoriaDao
{
	@Override
	public Class<TurmaMonitoria> getEntityClass() 
	{
		return TurmaMonitoria.class;
	}
}
