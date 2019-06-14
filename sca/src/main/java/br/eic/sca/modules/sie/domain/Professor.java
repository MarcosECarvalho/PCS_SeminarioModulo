package br.eic.sca.modules.sie.domain;

import java.util.HashSet;
import java.util.Set;

public final class Professor implements Comparable<Professor>
{
	//
	// Atributos
	//
	private Integer id;
	private String  matricula;
	private String  nome;
	private String  email;
	
	private Set<Curso> cursos;
	
	//
	// Construtor
	//
	public Professor(String matricula, String nome) 
	{
		this.nome = nome;
		this.email = "";		
		this.matricula = matricula;
		this.id = hashCode();
		this.cursos = new HashSet<Curso>();
	}

	//
	// Métodos de Acesso
	//
	public Integer getId() {
		return id;
	}
	
	public String getEmail() {
		return email;
	}

	public String getNome() {
		return nome;
	}

	public String getMatricula() {
		return matricula;
	}
	
	public Set<Curso> getCursos() {
		return cursos;
	}

	public void setCursos(Set<Curso> cursos) {
		this.cursos = cursos;
	}
	
	//
	// Métodos de Comparação
	//
	@Override
	public int hashCode() {
		return Integer.parseInt(this.getMatricula());
	}
	
	@Override
	public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof Professor)) {
            return false;
        }
        Professor other = (Professor) obj;
        return (this.getMatricula().equals(other.getMatricula()));
    }

	@Override
	public int compareTo(Professor o) 
	{
		return this.nome.compareTo(o.nome);
	}

	@Override
	public String toString() {
		return "Professor [id=" + id + ", matricula=" + matricula + ", nome=" + nome + ", email=" + email + ", cursos="
				+ cursos + "]";
	}		
}