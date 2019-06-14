package br.eic.sca.modules.sie.filters;

import java.util.ArrayList;
import java.util.List;

import br.eic.sca.modules.sie.domain.Curso;
import br.eic.sca.modules.sie.domain.VersaoCurso;

public class DisciplinaFilter
{
	private String nome;
	private String periodo;
	private String codigo;
	private Integer optativa;
	private VersaoCurso versaoCurso;
	
	private List<Curso> cursos;
	
	public DisciplinaFilter(String nome, String periodo, String codigo, VersaoCurso versaoCurso, Integer optativa)
	{
		super();
		this.nome = nome;
		this.periodo = periodo;
		this.codigo = codigo;
		this.versaoCurso = versaoCurso;
		this.optativa = optativa;
		this.cursos = new ArrayList<Curso>();
	}

	public String getPeriodo() {
		return periodo;
	}

	public void setPeriodo(String periodo) {
		this.periodo = periodo;
	}

	public String getNome() 
	{
		return nome;
	}

	public void setNome(String nome) 
	{
		this.nome = nome;
	}
	
	public List<Curso> getCursos() 
	{
		return cursos;
	}
	
	public void setCursos(List<Curso> cursos) 
	{
		this.cursos = cursos;
	}

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public VersaoCurso getVersaoCurso() {
		return versaoCurso;
	}
	
	public void setVersaoCurso(VersaoCurso versaoCurso) {
		this.versaoCurso = versaoCurso;
	}

	public Integer getOptativa() {
		return optativa;
	}

	public void setOptativa(Integer optativa) {
		this.optativa = optativa;
	}
	
}