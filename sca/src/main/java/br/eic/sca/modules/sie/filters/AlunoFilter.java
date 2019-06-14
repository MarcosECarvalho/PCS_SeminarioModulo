package br.eic.sca.modules.sie.filters;

import java.util.ArrayList;
import java.util.List;

import br.eic.sca.modules.sie.domain.Curso;
import br.eic.sca.modules.sie.domain.VersaoCurso;

public class AlunoFilter 
{
	private String nome;
	private String cpf;
	private String matr;
	private VersaoCurso versaoCurso;
	private List<Curso> cursos;
	
	public AlunoFilter(String nome, String cpf, String matr, VersaoCurso versaoCurso) 
	{
		super();
		this.nome = nome;
		this.cpf = cpf;
		this.matr = matr;
		this.versaoCurso = versaoCurso;
		this.cursos = new ArrayList<Curso>();
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}

	public String getMatr() {
		return matr;
	}

	public void setMatr(String matr) {
		this.matr = matr;
	}

	public VersaoCurso getVersaoCurso() {
		return versaoCurso;
	}
	
	public void setVersaoCurso(VersaoCurso versaoCurso) {
		this.versaoCurso = versaoCurso;
	}
	
	public List<Curso> getCursos() {
		return cursos;
	}
	
	public void setCursos(List<Curso> cursos) {
		this.cursos = cursos;
	}
}
