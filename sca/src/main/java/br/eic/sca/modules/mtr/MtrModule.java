package br.eic.sca.modules.mtr;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.DependsOn;

import br.eic.sca.api.ScaModule;
import br.eic.sca.core.security.Permission;

@DependsOn("sieModule")
@ComponentScan("br.eic.sca.modules.mtr")
public class MtrModule extends ScaModule
{
	@Override
	public String getName() 
	{
		return "Monitoria";
	}
	
	@Override
	public Permission[] providePermissions() 
	{
		return new Permission[] 
		{
			new Permission("Monitoria", "Turmas", "/mtr/turmaMonitoria.jsf" , "ADMIN")          			
		};
	}
}
