package br.eic.sca.core.security.filters;

import java.io.IOException;
import java.util.Enumeration;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

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
public class WipeCacheFilter implements Filter 
{
	public void doFilter(ServletRequest arg0, ServletResponse arg1,FilterChain arg2) throws IOException, ServletException 
	{
		HttpServletRequest request = (HttpServletRequest)arg0;		
		
		if(request.getParameter("refresh") != null)
		{
			Enumeration<String> beans = request.getSession().getAttributeNames();
			
			while (beans.hasMoreElements())
			{
				String bean = beans.nextElement();
				
				if (bean.endsWith("Bean") && !bean.equals("sessionBean"))
				{
					request.getSession().removeAttribute(bean);
				}
			}			
		}

		arg2.doFilter(arg0, arg1);
	}

	public void destroy() 
	{	
	}
	
	public void init(FilterConfig arg0) throws ServletException 
	{
	}
}