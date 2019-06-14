package br.eic.sca.modules.sie.dao.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.eic.sca.modules.sie.dao._DaoSie;

/**
 * @author Rafael Castaneda (rafael.ribeiro@cefet-rj.br)
 * <p>
 * Implementação abstrata da interface {@link br.eic.sca.modules.sie.dao._DaoSie _DaoXls}.
 * É utilizada para facilitar a implementação dos DAO's de recuperação de dados 
 * acadêmicos contidos nos relatórios em XLS do sistema SIE.
 * <p>
 */
public abstract class _DaoSieAbstract<ENTITY_TYPE> implements _DaoSie<ENTITY_TYPE>
{
	/**
	 * Define um Logger para ser herdado por cada DAO específico 
	 */
	protected Logger LOG = LoggerFactory.getLogger(getClass());
	
	/**
	 * Método utilitário que valida um conjunto de Strings como 
	 * sendo diferentes de nulas e não-vazias.
	 * 
	 * @param strings Strings a serem validadas
	 * @return true/false
	 */
	protected boolean validate(String... strings)
	{
		for(String string : strings)
		{
			if (string==null || string.isEmpty())
				return false;
		}
		
		return true;
	}
	
	/**
	 * Método utilitário que valida um conjunto de Strings como 
	 * sendo diferentes de nulas.
	 * 
	 * @param strings Strings a serem validadas
	 * @return true/false
	 */
	protected boolean validateNotNull(String... strings)
	{
		for(String string : strings)
		{
			if (string==null)
				return false;
		}
		
		return true;
	}
}
