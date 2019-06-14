package br.eic.sca.modules.mtr.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.eic.sca.api.dao._DaoHibernate;
import br.eic.sca.api.service.impl._ServiceHibernateAbstract;
import br.eic.sca.modules.mtr.dao.TurmaMonitoriaDao;
import br.eic.sca.modules.mtr.domain.TurmaMonitoria;
import br.eic.sca.modules.mtr.service.TurmaMonitoriaService;

@Service
public class TurmaMonitoriaServiceImpl extends _ServiceHibernateAbstract<TurmaMonitoria> implements TurmaMonitoriaService
{
	@Autowired
	TurmaMonitoriaDao turmaMonitoriaDao;
	
	@Override
	public _DaoHibernate<TurmaMonitoria> getBaseDao() 
	{
		return turmaMonitoriaDao;
	}
}
