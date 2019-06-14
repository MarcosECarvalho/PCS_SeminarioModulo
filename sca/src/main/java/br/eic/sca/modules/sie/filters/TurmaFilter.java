package br.eic.sca.modules.sie.filters;

import java.util.ArrayList;
import java.util.List;

import br.eic.sca.modules.sie.domain.Curso;
import br.eic.sca.modules.sie.domain.Professor;
import br.eic.sca.modules.sie.domain.VersaoCurso;

public class TurmaFilter
{
	private String codTurma;
	private String codDisc;
	private String nomeDisc;
	private Integer ano;
	private Integer semestre;
	
	private VersaoCurso versaoCurso;	
	private Professor professor;
	private List<Curso> cursos;
	
	public TurmaFilter(String codTurma, Integer ano, Integer semestre, VersaoCurso versaoCurso) 
	{
		super();
		this.codTurma = codTurma;
		this.codDisc = "";
		this.nomeDisc = "";
		this.ano = ano;
		this.semestre = semestre;			
		this.versaoCurso = versaoCurso;
		this.cursos = new ArrayList<Curso>();
	}
	
	public TurmaFilter(String codTurma, String codDisc, String nomeDisc, Integer ano, Integer semestre,  VersaoCurso versaoCurso) 
	{
		this.codTurma = codTurma;
		this.codDisc = codDisc;
		this.nomeDisc = nomeDisc;
		this.ano = ano;
		this.semestre = semestre;
		this.versaoCurso = versaoCurso;
	}

	public String getCodTurma() {
		return codTurma;
	}

	public void setCodTurma(String codTurma) {
		this.codTurma = codTurma;
	}

	public String getCodDisc() {
		return codDisc;
	}

	public void setCodDisc(String codDisc) {
		this.codDisc = codDisc;
	}

	public String getNomeDisc() {
		return nomeDisc;
	}

	public void setNomeDisc(String nomeDisc) {
		this.nomeDisc = nomeDisc;
	}

	public Integer getAno() {
		return ano;
	}

	public void setAno(Integer ano) {
		this.ano = ano;
	}

	public Integer getSemestre() {
		return semestre;
	}

	public void setSemestre(Integer semestre) {
		this.semestre = semestre;
	}	
	
	public Professor getProfessor() {
		return professor;
	}
	
	public void setProfessor(Professor professor) {
		this.professor = professor;
	}
	
	public List<Curso> getCursos() 
	{
		return cursos;
	}
	
	public void setCursos(List<Curso> cursos) 
	{
		this.cursos = cursos;
	}

	public VersaoCurso getVersaoCurso() {
		return versaoCurso;
	}
	
	public void setVersaoCurso(VersaoCurso versaoCurso) {
		this.versaoCurso = versaoCurso;
	}
}