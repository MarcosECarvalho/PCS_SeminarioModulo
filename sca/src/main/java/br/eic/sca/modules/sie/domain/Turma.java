package br.eic.sca.modules.sie.domain;

public class Turma implements Comparable<Turma>
{
	private Integer  	id;           
	private String  	codigo; 	  
	private Integer		ano;		  
	private Integer		semestre;	  
	private Disciplina 	disciplina;   
	private Professor 	professor;	  
	                                  	                                 
	//                                
	// Construtor
	//
	public Turma(Integer id, String codigo, Integer ano, Integer semestre, Disciplina disciplina, Professor professor) 
	{
		this.codigo = codigo;
		this.ano = ano;
		this.semestre = semestre;
		this.disciplina = disciplina;
		this.professor = professor;
		
		this.id = hashCode();
	}	

	//
	// Métodos de Acesso
	//
	public Integer getId() {
		return id;
	}
	
	public int getSemestre() {
		return semestre;
	}
	
	public int getAno() {
		return ano;
	}
	
	public String getCodigo() {
		return codigo;
	}			

	public Professor getProfessor() {
		return professor;
	}
				
	public Disciplina getDisciplina() {
		return disciplina;
	}
	
	public String getAnoSemestre() {
		return ano+"."+semestre;
	}
	
	public String getNomeProfessor() {
		if (professor!=null)
			return professor.getNome();
		else
			return "";
	}
	
	//
	// Métodos de Comparação
	//			
	@Override
	public int hashCode() 
	{
		String idString = disciplina.getCodigo()+codigo+ano+semestre;
		return idString.hashCode();
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
		Turma other = (Turma) obj;
		if (ano == null) {
			if (other.ano != null)
				return false;
		} else if (!ano.equals(other.ano))
			return false;
		if (codigo == null) {
			if (other.codigo != null)
				return false;
		} else if (!codigo.equals(other.codigo))
			return false;
		if (disciplina == null) {
			if (other.disciplina != null)
				return false;
		} else if (!disciplina.equals(other.disciplina))
			return false;
		if (semestre == null) {
			if (other.semestre != null)
				return false;
		} else if (!semestre.equals(other.semestre))
			return false;
		return true;
	}
	
	@Override
	public int compareTo(Turma o) 
	{
		if (this.disciplina.getNome().compareTo(o.disciplina.getNome())<0)
		{
		    return -1;
		}
		else if (this.disciplina.getNome().compareTo(o.disciplina.getNome())>0)
		{
		    return 1;
		}
		else 
		{
		    if(this.ano.compareTo(o.ano) < 0) 
		    {
		    	return -1;
		    } 
		    else if(this.ano.compareTo(o.ano) > 0) 
		    {
			    return 1;
			}
		    else 
		    {
		    	if(this.semestre.compareTo(o.semestre) < 0) 
			    {
			    	return -1;
			    } 
			    else if(this.semestre.compareTo(o.semestre) > 0) 
			    {
				    return 1;
				}
			    else 
			    {
			    	return 0;
				}
			}
		}				
	}
	
	@Override
	public String toString() {
		return "Turma [id=" +id+", codigo=" +codigo+", disciplina=" + disciplina.getCodigo() + ", ano=" + ano + ", periodo=" + semestre + "]";
	}
}



