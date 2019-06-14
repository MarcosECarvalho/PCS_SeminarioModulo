package br.eic.sca.modules.sie.filters;

import java.util.ArrayList;
import java.util.List;

import br.eic.sca.modules.sie.domain.Curso;

public class VersaoCursoFilter 
{
	private String nome;
	private String sigla;
	private String versao;
	
	private List<Curso> cursos;
	
	public VersaoCursoFilter(String nome, String sigla, String versao) 
	{
		super();
		this.nome = nome;
		this.sigla = sigla;
		this.versao = versao;
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
	
	public String getSigla() 
	{
		return sigla;
	}

	public void setSigla(String sigla) 
	{
		this.sigla = sigla;
	}

	public String getVersao() 
	{
		return versao;
	}
	
	public void setVersao(String versao) 
	{
		this.versao = versao;
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
