package br.eic.sca.core.security;

import java.util.Arrays;
import java.util.List;

/**
 * @author Rafael Castaneda
 *
 */
public class Permission 
{
	//
	// Atributos
	//
	private String category;
	private String resource;
	private String url;
	private List<String> roles;
	
	//
	// Construtor
	//
	public Permission(String category,String resource,String url,String... roles) 
	{
		super();
		this.category = category;
		this.resource = resource;
		this.url = url;
		this.roles = Arrays.asList(roles);		
	}

	//
	// Operações
	//
	public boolean supports(String role) {
		return roles.contains(role);
	}
	
	//
	// Métodos de Acesso
	//
	public String getCategory() {
		return category;
	}
	
	public String getResource() {
		return resource;
	}
	
	public String getUrl() {
		return url;
	}
	
	public List<String> getRoles() {
		return roles;
	}
}
