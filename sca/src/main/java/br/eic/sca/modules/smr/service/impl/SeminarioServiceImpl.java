package br.eic.sca.modules.smr.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.eic.sca.api.dao._DaoHibernate;
import br.eic.sca.api.service.impl._ServiceHibernateAbstract;
import br.eic.sca.modules.smr.dao.SeminarioDao;
import br.eic.sca.modules.smr.domain.Seminario;
import br.eic.sca.modules.smr.service.SeminarioService;

@Service
public class SeminarioServiceImpl extends _ServiceHibernateAbstract<Seminario> implements SeminarioService
{
	@Autowired
	SeminarioDao seminarioDao;
	
	@Override
	public _DaoHibernate<Seminario> getBaseDao() 
	{
		return seminarioDao;
	}
}
