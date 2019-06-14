package br.eic.sca.modules.sie.domain;

public final class VersaoCurso implements Comparable<VersaoCurso>
{
	//
	// Atributos
	//
	private Integer id;	
	private String  versao;
	private Curso   curso;

	//
	// Construtor
	//
	public VersaoCurso(String versao, Curso curso) 
	{
		this.curso = curso;
		this.versao = versao;		
		this.id = hashCode();
	}

	//
	// Métodos de Acesso
	//
	public Integer getId() {
		return id;
	}

	public String getVersao() {
		return versao;
	}
	
	public Curso getCurso() {
		return curso;
	}
		
	public String getLabel() {
		return curso.getSigla() + " - " + versao;
	}

	//
	// Métodos de Comparação
	//
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((versao == null) ? 0 : versao.hashCode());
		result = prime * result + ((curso == null) ? 0 : curso.hashCode());
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
		VersaoCurso other = (VersaoCurso) obj;
		if (versao == null) {
			if (other.versao != null)
				return false;
		} else if (!versao.equals(other.versao))
			return false;
		if (curso == null) {
			if (other.curso != null)
				return false;
		} else if (!curso.equals(other.curso))
			return false;
		return true;
	}

	@Override
	public int compareTo(VersaoCurso o) 
	{
		if (this.curso.getSigla().compareTo(o.curso.getSigla())<0)
		{
		    return -1;
		}
		else if (this.curso.getSigla().compareTo(o.curso.getSigla())>0)
		{
		    return 1;
		}
		else 
		{
		    if(this.versao.compareTo(o.versao) < 0) 
		    {
		    	return -1;
		    } 
		    else if(this.versao.compareTo(o.versao) > 0) 
		    {
			    return 1;
			}
		    else 
		    {
		    	return 0;
			}
		}				
	}
	
	@Override
	public String toString() {
		return "VersaoCurso [versao=" + this.versao + "; curso=" + this.curso.toString() + "]";
	}
}