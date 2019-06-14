package br.eic.sca.modules.sie.service.impl;

import java.util.List;

import javax.transaction.Transactional;

import br.eic.sca.modules.sie.dao._DaoSie;
import br.eic.sca.modules.sie.service._ServiceSie;

/**
 * @author Rafael Castaneda (rafael.ribeiro@cefet-rj.br)
 * <p>
 * Implementação abstrata da interface {@link br.eic.sca.modules.sie.service._ServiceSie _ServiceXls}.
 * É utilizada para facilitar a implementação dos serviços de recuperação de dados 
 * acadêmicos contidos nos relatórios em XLS do sistema SIE.
 * <p>
 */
public abstract class _ServiceSieAbstract<ENTITY_TYPE> implements _ServiceSie<ENTITY_TYPE>
{
	public abstract _DaoSie<ENTITY_TYPE> getBaseDao();
	
	@Override
	@Transactional
	public ENTITY_TYPE retrieveById(Integer id) 
	{
		return getBaseDao().retrieveById(id);
	}

	@Override
	@Transactional
	public List<ENTITY_TYPE> retrieveAll() 
	{
		return getBaseDao().retrieveAll();
	}			
	
	@Override
	@Transactional
	public List<ENTITY_TYPE> retrieveByScanning(String filter)
	{
		return getBaseDao().retrieveByScanning(filter);
	}
}