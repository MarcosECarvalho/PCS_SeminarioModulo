package br.eic.sca.core;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.ComponentScan;

import br.eic.sca.api.ScaModule;
import br.eic.sca.core.security.LoggedRole;

@ComponentScan("br.eic.sca.core")
public class CoreModule extends ScaModule 
{	
	@Override
	public String getName() 
	{
		return "Core";
	}
	
	@Override
	public String[] provideRoles() 
	{
		return new String[] {"ADMIN"};		
	}

	@Override
	public List<LoggedRole> doLogin(String username, String password) 
	{
		List<LoggedRole> loggedRoles = new ArrayList<LoggedRole>();
		
		if (username.equals("admin"))
		{			
			loggedRoles.add(new LoggedRole("ADMIN",null));
		}
			
		return loggedRoles;
	}
}
