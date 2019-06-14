package br.eic.sca.modules.sie.domain;

public class Curso implements Comparable<Curso>
{
	//
	// Atributos
	// 
	private Integer id;
	private String 	sigla;	
	private String 	nome;
	
	//
	// Construtor
	//
	public Curso(String sigla, String nome) 
	{
		this.sigla = sigla;
		this.nome = nome;	
		this.id = hashCode();
	}

	//
	// Métodos de Acesso
	//
	public Integer getId() {
		return id;
	}
	
	public String getSigla() {
		return sigla;
	}
	
	public String getNome() {
		return nome;
	}

	//
	// Métodos de Comparação
	//	
	@Override
	public int hashCode() 
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((sigla == null) ? 0 : sigla.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) 
	{
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Curso other = (Curso) obj;
		if (sigla == null) {
			if (other.sigla != null)
				return false;
		} else if (!sigla.equals(other.sigla))
			return false;
		return true;
	}

	@Override
	public int compareTo(Curso o) 
	{
		return this.sigla.compareTo(o.sigla);
	}
	
	@Override
	public String toString() 
	{
		return sigla;
	}
}
