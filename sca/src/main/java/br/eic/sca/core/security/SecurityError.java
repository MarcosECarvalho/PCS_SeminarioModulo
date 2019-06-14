package br.eic.sca.core.security;

public enum SecurityError 
{
	INVALID_CREDENTIALS("Credenciais inválidas."),
	NOT_AUTHENTICATED("Este acesso requer autenticação."),
	NOT_AUTHORIZED("Acesso não autorizado."),
	SESSION_EXPIRED("Sessão expirada, faça login novamente.");	
	
	private String description;
	
	private SecurityError(String description)
	{
		this.description = description;
	}
	
	public String getDescription() {
		return description;
	}
}
