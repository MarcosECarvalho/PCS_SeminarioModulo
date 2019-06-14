package br.eic.sca.core.security.filters;

import java.io.IOException;

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

import br.eic.sca.core.security.SecurityError;

/**
 * @author Rafael Castaneda (rafael.ribeiro@cefet-rj.br)
 * <p>
 * Classe que filtra as requisições a páginas JSF para limpar
 * os Beans da sessão do usuário caso o parâmetro <em>refresh</em>
 * exista na requisição. Este filtro cumpre o propósito de manter
 * o conteúdo das páginas sempre limpo a cada vez que o usuário 
 * executa um comando de 'refresh' no navegador, ou a cada vez que
 * acessa um novo item no menu do sistema. 
 * 
 */
public class HideXhtmlFilter implements Filter 
{
	private static Logger LOG = LoggerFactory.getLogger(LoginFilter.class);
	
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain filterChain) throws IOException, ServletException 
	{
		// Recupera a URL de requisição
		HttpServletRequest request = (HttpServletRequest)req;
		HttpServletResponse response = (HttpServletResponse)res;
		String url = request.getRequestURI();
		
		// Se a requisição contiver URL's que acessem arquivos XHTML, invalida a requisição 
		if (url.contains(".xhtml"))
		{
			LOG.warn("Tentativa de acesso não autorizado a recurso XHTML pelo IP "+request.getRemoteHost()+" na URL "+url);
			request.getSession().setAttribute("preRenderMessage",SecurityError.NOT_AUTHORIZED.getDescription());
			response.sendRedirect(request.getContextPath()+"/login.jsf");	
			return;
		}
			
		filterChain.doFilter(req, res);
	}

	public void destroy() 
	{	
	}
	
	public void init(FilterConfig arg0) throws ServletException 
	{
	}
}