package br.eic.sca.modules.sie.filters;

import java.util.ArrayList;
import java.util.List;

import br.eic.sca.modules.sie.domain.Curso;

public class ProfessorFilter
{
	private String nome;
	private String matr;
	
	private List<Curso> cursos;
	
	public ProfessorFilter(String nome, String matr) 
	{
		super();
		this.nome = nome;
		this.matr = matr;
		this.cursos = new ArrayList<Curso>();
	}
	
	public String getNome() 
	{
		return nome;
	}
	
	public void setNome(String nome) 
	{
		this.nome = nome;
	}
	
	public String getMatr() 
	{
		return matr;
	}
	
	public void setMatr(String matr) 
	{
		this.matr = matr;
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
