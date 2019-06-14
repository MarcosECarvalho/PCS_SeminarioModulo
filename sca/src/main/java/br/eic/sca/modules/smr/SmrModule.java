package br.eic.sca.modules.smr;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.DependsOn;

import br.eic.sca.api.ScaModule;
import br.eic.sca.core.security.Permission;

@DependsOn("sieModule")
@ComponentScan("br.eic.sca.modules.smr")
public class SmrModule extends ScaModule
{
	@Override
	public String getName() 
	{
		return "Seminario";
	}
	
	@Override
	public Permission[] providePermissions() 
	{
		return new Permission[] 
		{
			new Permission("Seminario", "Seminarios", "/smr/seminario.jsf" , "ADMIN")          			
		};
	}
}
