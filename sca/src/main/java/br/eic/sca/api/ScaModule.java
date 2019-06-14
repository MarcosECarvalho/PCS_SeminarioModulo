package br.eic.sca.api;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;

import br.eic.sca.core.security.LoggedRole;
import br.eic.sca.core.security.Permission;

public abstract class ScaModule implements InitializingBean  
{
	protected Logger LOG = LoggerFactory.getLogger(getClass().getSuperclass());
	
	public ScaModule()
	{
		String msg = "== Inicializando Módulo "+getName()+" ==";
		String bar = StringUtils.repeat("=", msg.length());
		
		LOG.info(bar);
		LOG.info(msg);
		LOG.info(bar);
		
		for (String role : provideRoles())
		{
			LOG.info("Perfil Adicionado - "+role);
		}
		
		for (Permission permission : providePermissions())
		{
			LOG.info("Regra Adicionada - "+permission.getUrl()+" -> "+permission.getRoles());    			
		}			
	}
	
	@Override
	public void afterPropertiesSet() throws Exception 
	{
		init();
	}	
	
	public abstract String getName();
	
	public void init()
	{	
	}
	
	public String[] provideRoles()
	{
		return new String[]{};
	}
	
	public Permission[] providePermissions()
	{
		return new Permission[]{};
	}
	
	public List<LoggedRole> doLogin(String username, String password)
	{
		return new ArrayList<LoggedRole>();
	}	
}
