package br.eic.sca.modules.sie.domain;

public class Aluno implements Comparable<Aluno>
{
	//
	// Atributos
	//
	private Integer id;
	private String 	cpf;
	private String 	matricula;
	private String 	nome;
	
	private VersaoCurso versaoCurso;
	
	//
	// Construtor
	//
	public Aluno(String nome, String cpf, String matricula, VersaoCurso versaoCurso) 
	{
		this.nome=nome;
		this.cpf=cpf;
		this.matricula=matricula;
		this.versaoCurso=versaoCurso;
		this.id = hashCode();
	}

	//
	// Métodos de Acesso
	//
	public Integer getId() {
		return id;
	}
	
	public String getNome() {
		return nome;
	}

	public String getCpf() {
		return cpf;
	}

	public String getMatricula() {
		return matricula;
	}
	
	public VersaoCurso getVersaoCurso()	{
		return versaoCurso;
	}

	//
	// Métodos de Comparação
	//
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((matricula == null) ? 0 : matricula.hashCode());
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
		Aluno other = (Aluno) obj;
		if (matricula == null) {
			if (other.matricula != null)
				return false;
		} else if (!matricula.equals(other.matricula))
			return false;
		return true;
	}
	
	@Override
	public int compareTo(Aluno o) 
	{
		return this.nome.compareTo(o.nome);
	}	

	@Override
	public String toString() 
	{
		return "Aluno [matricula=" + matricula + ", cpf=" + cpf + ", nome=" + nome + ", versaoCurso=" + versaoCurso+ "]";
	}
}