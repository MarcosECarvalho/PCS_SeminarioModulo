package br.eic.sca.core.security.filters;

import java.io.IOException;
import java.util.Collection;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import javax.faces.application.ViewExpiredException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import br.eic.sca.api.ScaModule;
import br.eic.sca.core.login.SessionBean;
import br.eic.sca.core.security.Permission;
import br.eic.sca.core.security.SecurityError;

/**
 * @author Rafael Castaneda (rafael.ribeiro@cefet-rj.br)
 * <p>
 * Classe de controle de segurança e acesso que filtra todas as 
 * requisições WEB para páginas JSF. O acesso é conferido a qualquer
 * página para usuários com perfil ADMIN. Para os outros perfis, é 
 * necessário que haja uma regra adicionada, conectando o perfil a
 * URL do recurso.  
 */
public class LoginFilter implements Filter 
{
	// LOGGER
	private static Logger LOG = LoggerFactory.getLogger(LoginFilter.class);
	
	//
	// Atributos
	//
	private static TreeMap<String,SecurityRuleSet> ruleMap = new TreeMap<String,SecurityRuleSet>();
			
	/*
	 * Cria as regras de filtragem para os diferentes perfis do SCA
	 */
	public void init(FilterConfig config) throws ServletException 
	{
		String cxPath = config.getServletContext().getContextPath();
		
		WebApplicationContext springContext = WebApplicationContextUtils.getWebApplicationContext(config.getServletContext());
		Collection<ScaModule> scaModules = springContext.getBeansOfType(ScaModule.class).values();		
		
		for (ScaModule scaModule : scaModules) 
		{
			for (Permission permission : scaModule.providePermissions())
			{
				for (String role : permission.getRoles())
				{
					addRule(role,cxPath+permission.getUrl());
				}					
			}
		}		
	}

	/*
	 * Filtra as requisições feitas para páginas JSF.
	 */
	public void doFilter(ServletRequest req, ServletResponse res,FilterChain filterChain) throws IOException
	{
		HttpServletRequest request = (HttpServletRequest)req;
		HttpServletResponse response = (HttpServletResponse)res;
		
		// Tenta evitar o armazenamento de informações em cache dos navegadores
		response.addHeader("Cache-Control", "no-cache");
		response.addHeader("Cache-Control", "no-store");
        response.addHeader("Cache-Control", "must-revalidate");
		response.setHeader("Pragma", "no-cache"); 
		response.setDateHeader("Expires", 0); 		
		
		try
		{
			// Recupera a URL de requisição
			String url = request.getRequestURI();
			
			// Recupera o Bean que guarda informações de login			
			SessionBean sessionBean = (SessionBean)request.getSession().getAttribute("sessionBean");
						
			// SE a requisição atual busca recursos estáticos ou a página de Login
			if (url.startsWith(request.getContextPath()+"/javax.faces.resource") ||
				url.startsWith(request.getContextPath()+"/_resources")			 ||
				url.startsWith(request.getContextPath()+"/login.jsf"))
			{
				// ACESSO AUTORIZADO - Mantém o fluxo original da requisição
				filterChain.doFilter(request, response);
				return;
			}				
			
			// SENÃO, SE o usuário estiver deslogado
			else if (sessionBean==null || sessionBean.getUsername()==null)
			{
				// ACESSO NEGADO - Redireciona para a página de login
				LOG.warn("Tentativa de acesso do IP "+request.getRemoteHost()+" não autenticado na página "+url);
				request.getSession().setAttribute("preRenderMessage",SecurityError.NOT_AUTHENTICATED.getDescription());
				response.sendRedirect(request.getContextPath()+"/login.jsf");
				return;
			}
			
			// SENÃO, testa o acesso do usuário logado contra as regras de segurança 
			else
			{
				// SE a página desejada for a página de index 
				if (url.startsWith(request.getContextPath()+"/index.jsf"))
				{
					// ACESSO AUTORIZADO - Mantém o fluxo original da requisição
					filterChain.doFilter(request, response);
					return;
				}	
				// SENÃO, testa as regras de segurança para os perfil do usuário
				else
				{
					// Percorre todos perfis que o usuário atual possui
					for(String role : sessionBean.getRoles())
					{
						// Para cada perfil, recupera o seu conjunto de regras 
						SecurityRuleSet ruleSet = ruleMap.get(role);
						
						// SE o conjunto de regras atual suportar o acesso do perfil atual a URL desejada
						if (ruleSet != null && ruleSet.supports(role, url))
						{
							// ACESSO AUTORIZADO - Mantém o fluxo original da requisição
							filterChain.doFilter(request, response);
							return;
						}
					}
					
					/*
					 * Ao chegar aqui todas as possibilidades de autorização se esgotaram,
					 * mas como o usuário está logado, ao invés de redirecioná-lo para a
					 * página de login, o sistema redireciona-o para a página de index.
					 */
					LOG.warn("Tentativa de acesso não autorizado do usuário "+sessionBean.getUsername()+" na página "+url);
					request.getSession().setAttribute("preRenderMessage",SecurityError.NOT_AUTHORIZED.getDescription());
					response.sendRedirect(request.getContextPath()+"/index.jsf");	
					return;
				}
			}		
		}
		catch(Exception e)
		{
			// Trata casos de sessão expirada 
			if (e.getCause()!=null && e.getCause() instanceof ViewExpiredException)
			{
				// TODO - Não apresenta a mensagem...
				LOG.warn("Redirecionando acesso expirado/inválido para página de login: "+request.getRemoteHost());
				request.getSession().setAttribute("preRenderMessage",SecurityError.SESSION_EXPIRED.getDescription());
				response.sendRedirect(request.getContextPath()+"/login.jsf");
				return;
			}
			// Trata outros casos de erro como erros da página
			else
			{
				LOG.warn("Erro de codificação na página/bean acessados, verificar o LOG do sistema.");				
				e.printStackTrace();

				// Se o usuário estiver logado, volta para index.jsf, senão volta para login.jsf
				request.getSession().setAttribute("preRenderMessage","Erro de codificação na página acessada");
				
				SessionBean sessionBean = (SessionBean)request.getSession().getAttribute("sessionBean");
				
				if (sessionBean!=null && sessionBean.getUsername()!=null)
				{
					response.sendRedirect(request.getContextPath()+"/index.jsf");
					return;
				}
				else
				{
					response.sendRedirect(request.getContextPath()+"/login.jsf");
					return;
				}
			}		
		}
	}

	@Override
	public void destroy() 
	{
	}
	
	private void addRule(String role,String url)
	{
		SecurityRuleSet ruleSet = ruleMap.get(role);
		
		if (ruleSet == null)
		{
			ruleSet = new SecurityRuleSet(role);
			ruleMap.put(role, ruleSet);
		}
				
		ruleSet.addUrl(url);				
	}
}

/**
 * @author Rafael Castaneda (rafael.ribeiro@cefet-rj.br)
 * <p>
 * Classe que representa um conjunto de regras de acesso a 
 * diferentes URL's. Agrega um determinado perfil de acesso
 * a um conjunto de Strings com URL's do sistema.  
 */
class SecurityRuleSet 
{
	private String 		role;
	private Set<String> urls;
	
	public SecurityRuleSet(String role) 
	{
		super();
		this.role = role;
		this.urls = new TreeSet<String>();
	}
	
	public void addUrl(String url)
	{
		this.urls.add(url.toLowerCase());
	}
	
	public boolean supports(String role, String url)
	{
		if (role.equals(this.role))
		{
			url = url.toLowerCase();
			
			for (String ruleUrl : urls) 
			{
				if (url.startsWith(ruleUrl))
				{
					return true;
				}
			}
		}
		
		return false;			
	}
}
