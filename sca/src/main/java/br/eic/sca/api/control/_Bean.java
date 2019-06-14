package br.eic.sca.api.control;

import java.util.Locale;

import javax.el.ValueExpression;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.primefaces.PrimeFaces;
import org.primefaces.component.datatable.DataTable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Rafael Castaneda (rafael.ribeiro@cefet-rj.br)
 *  
 * Classe abstrata que fornece métodos utilitários 
 * para todos os ManagedBeans do sistema SCA.
 */
public abstract class _Bean 
{
	/**
	 * Define um Logger para ser herdado por cada Bean específico 
	 */
	protected Logger LOG = LoggerFactory.getLogger(getClass());
			
	/**
	 * Constrói o Bean forçando o Locale para Português (Brasil)
	 */
	public _Bean() 
	{
		Locale.setDefault(new Locale("pt","BR"));
	}
	
	/**
	 * Recupera o objeto da requisição HTTP
	 * <p>
	 * @return objeto HttpServletRequest
	 */
	public HttpServletRequest getHttpServletRequest()
	{
		return (HttpServletRequest)(FacesContext.getCurrentInstance().getExternalContext().getRequest());
	}
	
	/**
	 * Recupera o objeto de resposta HTTP
	 * <p>
	 * @return objeto HttpServletResponse
	 */
	public HttpServletResponse getHttpServletResponse() 
	{
		return (HttpServletResponse)(FacesContext.getCurrentInstance().getExternalContext().getResponse());
	}
	
	/**
	 * Recupera o objeto de sessão HTTP
	 * <p>
	 * @return objeto HttpSession
	 */
	public HttpSession getHttpSession()
	{
		return getHttpServletRequest().getSession();
	}
			
	/**
	 * Método utilitário para apresentar mensagens Pop-Up de nível informativo
	 * <p>
	 * @param message Mensagem informativa
	 */
	public void popInfo(String message)
	{
		FacesMessage fm = new FacesMessage(FacesMessage.SEVERITY_INFO, message, "");
		FacesContext.getCurrentInstance().addMessage(null, fm);
	}
	
	/**
	 * Método utilitário para apresentar mensagens Pop-Up de nível de alerta
	 * <p>
	 * @param message Mensagem de alerta
	 */
	public void popWarning(String message)
	{
		FacesMessage fm = new FacesMessage(FacesMessage.SEVERITY_WARN, message, "");
		FacesContext.getCurrentInstance().addMessage(null, fm);
	}
	
	/**
	 * Método utilitário para apresentar mensagens Pop-Up de nível de erro
	 * <p>
	 * @param message Mensagem de erro
	 */
	public void popError(String message)
	{
		FacesMessage fm = new FacesMessage(FacesMessage.SEVERITY_ERROR, message, "");
		FacesContext.getCurrentInstance().addMessage(null, fm);
	}
	
	/**
	 * Busca um componente da tela JSF que invocou o Bean pelo seu ID
	 * <p>
	 * @param componentID Mensagem de erro
	 */
	public UIComponent findUIComponent(String componentID)
	{
		return FacesContext.getCurrentInstance().getViewRoot().findComponent(componentID);
	}
	
	/**
	 * Método utilitário que reseta uma DataTable JSF para suas configurações 
	 * padrões pelo ID.
	 * <p>
	 * @param componentId ID da DataTable na página JSF.
	 */
	public void resetDataTableUI(String componentId)
	{
		DataTable dataTable = (DataTable)findUIComponent(componentId);
		dataTable.setValueExpression("sortBy", dataTable.getDefaultSortByVE());
		dataTable.setSortOrder(dataTable.getDefaultSortOrder());
		dataTable.setFirst(0);
	}
	
	/**
	 * Cria programaticamente uma ValueExpression do tipo #{EXPRESSION} 
	 * <p>
	 * @param expression O conteúdo da expressão
	 * @param expectedType O tipo esperado pela resolução da expressão 
	 * @return O objeto ValueExpression construído
	 */
	public ValueExpression valueExpression(String expression, Class<?> expectedType) 
	{
	    FacesContext context = FacesContext.getCurrentInstance();
	    return context.getApplication().getExpressionFactory().createValueExpression(context.getELContext(), expression, expectedType);
	}
	
	/**
	 * Realiza o tratamento mínimo para uma exceção que chega até a camada 
	 * de visão. Que consiste em adicionar uma entrada ao LOG do sistema e
	 * apresentar uma mensagem de popUp 
	 * <p>
	 * @param exception A exceção capturada
	 */
	public void handleError(Exception exception)
	{
		popError("ERRO: "+exception.getMessage());
		LOG.error("ERRO: "+exception,exception);
	}
	
	/**
	 * Apresenta um diálogo de pop-up 
	 * <p>
	 * @param dialog O nome do diálogo de acordo com o atributo "widgetVar"
	 */
	public void showDialog(String dialog)
	{
		PrimeFaces.current().executeScript("PF('"+dialog+"').show()");
	}
	
	/**
	 * Esconde um diálogo de pop-up 
	 * <p>
	 * @param dialog O nome do diálogo de acordo com o atributo "widgetVar"
	 */
	public void hideDialog(String dialog)
	{
		PrimeFaces.current().executeScript("PF('"+dialog+"').hide()");
	}
}
