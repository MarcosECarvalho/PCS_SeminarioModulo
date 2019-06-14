package br.eic.sca.modules.sie.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import br.eic.sca.modules.sie.annotations.SieOrm;

@Table(name="admin_coordenacao")
@Entity(name="Coordenacao")
public class Coordenacao implements Comparable<Coordenacao> 
{
	//
	// Atributos Mapeados
	//
	@Id    
	@GeneratedValue(strategy= GenerationType.AUTO)
	private Integer id;
		
	//
	// Atributos SIE
	//
	@Column(name="fk_xls_curso")
	private Integer cursoId;
		
	@Transient
	@SieOrm(id="cursoId")
	private Curso curso;
	
	@Column(name="fk_xls_prof")
	private Integer professorId;
	
	@Transient
	@SieOrm(id="professorId")
	private Professor professor;
		
	//
	// Métodos de Acesso
	//
	public Integer getId() {
		return id;
	}
	
	public void setId(Integer id) {
		this.id = id;
	}
	
	public Integer getCursoId() {
		return cursoId;
	}

	public void setCursoId(Integer cursoId) {
		this.cursoId = cursoId;
	}

	public Curso getCurso() {
		return curso;
	}

	public void setCurso(Curso curso) {
		this.curso = curso;
	}

	public Integer getProfessorId() {
		return professorId;
	}

	public void setProfessorId(Integer professorId) {
		this.professorId = professorId;
	}

	public Professor getProfessor() {
		return professor;
	}

	public void setProfessor(Professor professor) {
		this.professor = professor;
	}
	
	//
	// Métodos Auxiliares
	//
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
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
		Coordenacao other = (Coordenacao) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public int compareTo(Coordenacao o) 
	{
		if (this.curso.getSigla().compareTo(o.curso.getSigla())<0)
		{
		    return -1;
		}
		else if (this.curso.getSigla().compareTo(o.curso.getSigla())>0)
		{
		    return 1;
		}
		else 
		{
		    if(this.professor.getNome().compareTo(o.professor.getNome()) < 0) 
		    {
		    	return -1;
		    } 
		    else if(this.professor.getNome().compareTo(o.professor.getNome()) > 0) 
		    {
			    return 1;
			}
		    else 
		    {
		    	return 0;
			}
		}
	}

	@Override
	public String toString() {
		return "Coordenacao [cursoId=" + cursoId + ", professorId=" + professorId + ", curso=" + curso + ", professor="
				+ professor + "]";
	}
	
	
}
