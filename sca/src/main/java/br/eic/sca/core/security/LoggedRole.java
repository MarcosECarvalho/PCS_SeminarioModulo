package br.eic.sca.core.security;

public class LoggedRole implements Comparable<LoggedRole>
{
	private String role;
	private Object userData;
	
	public LoggedRole(String role, Object userData) 
	{
		super();
		this.role = role;
		this.userData = userData;
	}
	
	public boolean isOf(String otherRole) 
	{
		return role.equals(otherRole);
	}
	
	public String getRole() 
	{
		return role;
	}

	public Object getUserData() 
	{
		return userData;
	}
	
	//
	// Métodos Auxiliares
	//	
	@Override
	public int compareTo(LoggedRole o) 
	{
		return getRole().compareTo(o.getRole());
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((role == null) ? 0 : role.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		LoggedRole other = (LoggedRole) obj;
		if (role == null) {
			if (other.role != null)
				return false;
		} else if (!role.equals(other.role))
			return false;
		return true;
	}		
}
