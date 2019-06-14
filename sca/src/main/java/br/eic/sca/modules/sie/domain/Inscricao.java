package br.eic.sca.modules.sie.domain;

public class Inscricao implements Comparable<Inscricao>
{
	//
	// Atributos
	//
	private Integer id;
	private Aluno 	aluno;
	private Turma 	turma;
	private String  situacao;
		
	//
	// Construtor
	//
	public Inscricao(Aluno aluno,Turma turma,String situacao) 
	{
		super();
		this.aluno = aluno;
		this.turma = turma;
		this.situacao = situacao;
		this.id = hashCode();
	}

	//
	// Métodos de Acesso
	//
	public Integer getId() {
		return id;
	}
	
	public Aluno getAluno() {
		return aluno;
	}
	
	public Turma getTurma() {
		return turma;
	}
	
	public String getSituacao() {
		return situacao;
	}

	//
	// Métodos de Comparação
	//
	@Override
	public int hashCode() 
	{
		String idString = aluno.getMatricula()+turma.getId();
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
		Inscricao other = (Inscricao) obj;
		if (aluno == null) {
			if (other.aluno != null)
				return false;
		} else if (!aluno.equals(other.aluno))
			return false;
		if (turma == null) {
			if (other.turma != null)
				return false;
		} else if (!turma.equals(other.turma))
			return false;
		return true;
	}

	@Override
	public int compareTo(Inscricao o) 
	{
		if (this.turma.getDisciplina().getNome().compareTo(o.turma.getDisciplina().getNome())<0)
		{
		    return -1;
		}
		else if (this.turma.getDisciplina().getNome().compareTo(o.turma.getDisciplina().getNome())>0)
		{
		    return 1;
		}
		else 
		{
		    if(this.turma.getAno() < (o.turma.getAno())) 
		    {
		    	return -1;
		    } 
		    else if(this.turma.getAno() > (o.turma.getAno())) 
		    {
			    return 1;
			}
		    else 
		    {
		    	if(this.turma.getSemestre() < (o.turma.getSemestre())) 
			    {
			    	return -1;
			    } 
			    else if(this.turma.getSemestre() > (o.turma.getSemestre())) 
			    {
				    return 1;
				}
			    else 			    
			    {
			    	if (this.aluno.getNome().compareTo(o.aluno.getNome())<0)
					{
					    return -1;
					}
					else if (this.aluno.getNome().compareTo(o.aluno.getNome())>0)
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
	}
	
	@Override
	public String toString() 
	{
		return "Inscricao [aluno=" + aluno.getMatricula() + ", turma=" + turma + ", situacao=" + situacao + "]";
	}
	
	
}
