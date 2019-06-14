package br.eic.sca.modules.sie.filters;

import java.util.ArrayList;
import java.util.List;

import br.eic.sca.modules.sie.domain.Curso;

public class InscricaoFilter 
{
	private String disciplina;
	private String codigoDisciplina;
	private String codigoTurma;
	private String aluno;
	private String matricula;
	
	private List<Curso> cursos;
	
	public InscricaoFilter(String disciplina, String codigoDisciplina, String codigoTurma, String aluno, String matricula) 
	{
		super();
		this.disciplina = disciplina;
		this.codigoDisciplina = codigoDisciplina;
		this.codigoTurma = codigoTurma;
		this.aluno = aluno;
		this.matricula = matricula;
		this.cursos = new ArrayList<Curso>();
	}

	public String getDisciplina() {
		return disciplina;
	}

	public void setDisciplina(String disciplina) {
		this.disciplina = disciplina;
	}

	public String getCodigoDisciplina() {
		return codigoDisciplina;
	}

	public void setCodigoDisciplina(String codigoDisciplina) {
		this.codigoDisciplina = codigoDisciplina;
	}

	public String getCodigoTurma() {
		return codigoTurma;
	}

	public void setCodigoTurma(String codigoTurma) {
		this.codigoTurma = codigoTurma;
	}

	public String getAluno() {
		return aluno;
	}

	public void setAluno(String aluno) {
		this.aluno = aluno;
	}

	public String getMatricula() {
		return matricula;
	}

	public void setMatricula(String matricula) {
		this.matricula = matricula;
	}
	
	public List<Curso> getCursos() 
	{
		return cursos;
	}
	
	public void setCursos(List<Curso> cursos) 
	{
		this.cursos = cursos;
	}
}
