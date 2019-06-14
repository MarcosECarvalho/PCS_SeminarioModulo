package br.eic.sca.core.login;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.primefaces.model.menu.DefaultMenuItem;
import org.primefaces.model.menu.DefaultSubMenu;
import org.primefaces.model.menu.DynamicMenuModel;
import org.primefaces.model.menu.MenuModel;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import br.eic.sca.api.control._Bean;
import br.eic.sca.core.menu.ScaMenu;
import br.eic.sca.core.menu.ScaMenuCategory;
import br.eic.sca.core.menu.ScaMenuItem;
import br.eic.sca.core.security.LoggedRole;

/**
 * @author Rafael Castaneda (rafael.ribeiro@cefet-rj.br)
 * <p>
 * ManagedBean que centraliza o armazenamento de dados de sessão.
 * <p>
 * Este ManagedBean não controla especificamente uma página JSF 
 * do sistema. Ele pode ser acessado por qualquer página ou até
 * mesmo por outros ManagedBean que necessitem de informações 
 * a respeito do usuário autenticado na sessão atual.
 */
@Controller
@Scope("session")
public class SessionBean extends _Bean
{             
	private String alias;
	private String username;
	
	private MenuModel menu;
	
	private List<String> 		   roles    = new ArrayList<String>();
	private Map<String,LoggedRole> rolesMap = new TreeMap<String,LoggedRole>();	
		
	/*
     * Possibilita às páginas apresentarem mensagens de pop-up no momento
     * de sua renderização após um redirecionamento advindo de Servlets ou 
     * Filtros. Para que a mensagem seja exibida, o Filtro ou Servlet deve
     * executar o seguinte código: <br/>
     * request.getSession().setAttribute("preRenderMessage","MESSAGE");<br/>
	 * response.sendRedirect(request.getContextPath()+"/[DESTINO].jsf");
     */
    public void preRender()
    {
    	Object preRenderMessage = getHttpSession().getAttribute("preRenderMessage");
    	
    	if (preRenderMessage != null)
    	{
    		popWarning((String)preRenderMessage);    		
    		getHttpSession().removeAttribute("preRenderMessage");
    	}
    } 
	
	/**
	 * Recupera o alias (nickname) do usuário autenticado.
	 * <p>
	 * @return Alias do usuário autenticado
	 */
	public String getAlias() 
	{
		return alias;
	}
	
	/**
	 * Recupera o username do usuário autenticado.
	 * <p>
	 * @return Username do usuário autenticado
	 */
	public String getUsername() 
	{
		return username;
	}
	
	/**
	 * Recupera o menu do usuário autenticado.
	 * <p>
	 * @return Menu do usuário autenticado
	 */	
	public MenuModel getMenu()
	{
		return menu;
	}
	
	/**
	 * Recupera todos perfis do usuário autenticado
	 * <p>
	 * @return Perfis do usuário autenticado
	 */
	public List<String> getRoles() 
	{
		return roles;
	}	
		
	/**
	 * Testa se o usuário autenticado possui entre os seus
	 * perfis o perfil passado como argumento. 
	 * <p>
	 * @param queryLoggedRole Perfil a ser testado 
	 * @return true/false
	 */
	public boolean hasRole(String roleQuery) 
	{
		if (rolesMap.containsKey(roleQuery))
			return true;
		else
			return false;
	}
	
	/**
	 * Testa se o usuário autenticado possui pelo menos
	 * um dos perfis passados como argumento. Os perfis
	 * são passados como Strings que devem corresponder 
	 * aos perfis criados pelos módulos do SCA
	 * <p>
	 * @param roleStrings Perfis a serem testados 
	 * @return true/false
	 */
	public boolean hasAnyRole(String... rolesQuery) 
	{
		for (String roleQuery : rolesQuery) 
		{
			if (rolesMap.containsKey(roleQuery))
				return true;		
		}
		
		return false;
	}
			
	/**
	 * Recupera o objeto de dados adicionais para um 
	 * determinado perfil do usuário autenticado. 
	 * <p>
	 * @param role Perfil do usuário autenticado
	 * @return
	 */
	public Object getUserData(String role) 
	{
		if (rolesMap.containsKey(role))
		{
			return rolesMap.get(role).getUserData();
		}
		else
		{
			return null;
		}	
	}
			
	//
	// Métodos Protegidos (Apenas para Acesso pelo LoginBean)
	//
	/*
	 * Modifica o alias do usuário autenticado.  
	 */
	void setAlias(String alias) 
	{
		this.alias = alias;
	}
	
	/*
	 * Modifica o username do usuário autenticado.  
	 */
	void setUsername(String username) 
	{
		this.username = username;
	}
	
	/*
	 * Modifica o menu do usuário autenticado.  
	 */
	void setMenu(ScaMenu scaMenu) 
	{
		this.menu = new DynamicMenuModel();
		
		for (ScaMenuCategory scaCategory : scaMenu.getCategories())
		{
			DefaultSubMenu menuElement = new DefaultSubMenu(scaCategory.getName());
			menu.addElement(menuElement);
			
			for (ScaMenuItem scaItem : scaCategory.getItems())
			{
				DefaultMenuItem menuItem = new DefaultMenuItem(scaItem.getName(), null, scaItem.getUrl()+"?refresh=true");
				menuElement.addElement(menuItem);
			}
		}		
	}
	
	/**
	 * Adiciona à sessão do usuário um novo perfil,
	 * e o seu correspondente objeto de dados adicionais. 
	 * <p>
	 * @param role Novo perfil do usuário
	 * @param userData Objeto de dados adicionais do usuário
	 */
	void addLoggedRole(LoggedRole loggedRole) 
	{
		String roleString = loggedRole.getRole();
		
		if (!rolesMap.containsKey(roleString))
		{
			rolesMap.put(roleString,loggedRole);			
		}		
		
		roles.clear();
		
		for (LoggedRole lr : rolesMap.values())
		{
			roles.add(lr.getRole());
		}
	}
		
}  