package br.eic.sca.modules.sie.domain;

public class Disciplina implements Comparable<Disciplina>
{
	//
	// Atributos
	//
	private Integer id;
	private String  nome;
	private String  codigo;
	private Integer creditos;
	private Integer cargaHoraria;
	private Integer periodo;
	private VersaoCurso versaoCurso;
	private boolean optativa;

	//
	// Construtor 
	//	
	public Disciplina(String codigo, String nome, Integer creditos, Integer cargaHoraria, VersaoCurso versaoCurso, Integer periodo, boolean optativa) 
	{
		this.nome = nome;
		this.codigo = codigo;
		this.id = hashCode();
		this.creditos = creditos;
		this.cargaHoraria = cargaHoraria;
		this.versaoCurso = versaoCurso;
		this.periodo = periodo;
		this.optativa = optativa;		
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
	
	public String getCodigo() {
		return codigo;
	}

	public Integer getCreditos() {
		return creditos;
	}
	
	public void setCreditos(Integer creditos) {
		this.creditos = creditos;
	}

	public Integer getCargaHoraria() {
		return cargaHoraria;
	}

	public void setCargaHoraria(Integer cargaHoraria) {
		this.cargaHoraria = cargaHoraria;
	}
	
	public VersaoCurso getVersaoCurso() {
		return versaoCurso;
	}

	public Integer getPeriodo() {
		return periodo;
	}
	
	public void setPeriodo(Integer periodo) {
		this.periodo = periodo;
	}
	
	public boolean isOptativa() {
		return optativa;
	}
	
	//
	// Métodos da Comparação
	//
	@Override
	public int hashCode() 
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((codigo == null) ? 0 : codigo.hashCode());		
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
		Disciplina other = (Disciplina) obj;
		if (codigo == null) {
			if (other.codigo != null)
				return false;
		} else if (!codigo.equals(other.codigo))
			return false;		
		return true;
	}
	
	@Override
	public int compareTo(Disciplina o) 
	{
		return this.codigo.compareTo(o.codigo);
	}

	@Override
	public String toString() 
	{
		return "Disciplina [id=" + id + ", codigo=" + codigo + ", creditos=" + creditos + ", cargaHoraria="
				+ cargaHoraria + ", periodo=" + periodo + ", optativa=" + optativa + ", nome=" + nome + ", versaoCurso="
				+ versaoCurso + "]";
	}		
}
