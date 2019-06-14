package br.eic.sca.modules.sie.control;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import br.eic.sca.api.control._Bean;
import br.eic.sca.core.login.SessionBean;
import br.eic.sca.modules.sie.domain.Aluno;
import br.eic.sca.modules.sie.domain.Coordenacao;
import br.eic.sca.modules.sie.domain.Professor;
import br.eic.sca.modules.sie.domain.Turma;
import br.eic.sca.modules.sie.domain.VersaoCurso;
import br.eic.sca.modules.sie.filters.TurmaFilter;
import br.eic.sca.modules.sie.service.AlunoService;
import br.eic.sca.modules.sie.service.InscricaoService;
import br.eic.sca.modules.sie.service.TurmaService;
import br.eic.sca.modules.sie.service.VersaoCursoService;

@Controller
@Scope("session")
public class TurmaBean extends _Bean
{
	//
	// Dependências
	//
	@Autowired
	protected SessionBean sessionBean;
	@Autowired
	protected TurmaService turmaService;
	@Autowired
	protected InscricaoService inscricaoService;
	@Autowired
	protected AlunoService alunoService;
	@Autowired
	protected VersaoCursoService versaoCursoService;
	
	//
	// Dados da Página 
	//
	private String  filtroNome;
	private String  filtroCodTurm;
	private String  filtroCodDisc;
	private Integer filtroSemestre;
	private Integer filtroAno;
	private VersaoCurso filtroVersaoCurso;
	private List<Turma> entities = new ArrayList<Turma>();
	
	//
	// Dados do Diálogo
	//
	private List<Aluno> alunos = new ArrayList<Aluno>();
	
	//
	// Inicialização dos Dados (Roda na construção do bean e no recarregamento da página)
	//
	public List<VersaoCurso> completeCurso(String query) 
    {
    	List<VersaoCurso> result = new ArrayList<VersaoCurso>();
    	
    	if (query.isEmpty())
    	{
    		result.addAll(versaoCursoService.retrieveAll());
    	}
    	else
    	{
	    	result.addAll(versaoCursoService.retrieveByScanning(query));
    	}
    	    	
    	return result;
    }
	
	@PostConstruct
	public void refresh() 
	{
		filtroNome = "";
		filtroCodTurm = "";
		filtroCodDisc = "";
		filtroSemestre = null;          
		filtroAno = null;    	
		filtroVersaoCurso = null;
		entities.clear();
		alunos.clear();
	}
	
	//
	// Operações da Página
	//
	public int getTotalEntities()
	{
		return entities.size();
	}
	
	@SuppressWarnings("unchecked")
	public void retrieve()
	{		
		// Limpa os resultados da tela
		entities.clear();
		
		// Reseta os critérios de ordenação da tabela para o padrão definido na página
		resetDataTableUI("pageForm:table");

		// Cria objeto para filtrar os novos resultados
		TurmaFilter filter = new TurmaFilter(filtroCodTurm, filtroCodDisc, filtroNome, filtroAno, filtroSemestre, filtroVersaoCurso);

		// Se o usuário logado for coordenador, adiciona os cursos coordenados ao filtro
		if (sessionBean.hasRole("COORDENADOR"))
		{			
			List<Coordenacao> coordenacoes = (List<Coordenacao>)sessionBean.getUserData("COORDENADOR");
			
			for (Coordenacao coordenacao : coordenacoes) 
			{
				filter.getCursos().add(coordenacao.getCurso());
			}						
		}

		// Se o usuário logado for professor, adiciona os cursos em que há atuação ao filtro 
		if (sessionBean.hasRole("PROFESSOR"))
		{			
			Professor professor = (Professor)sessionBean.getUserData("PROFESSOR");
			filter.getCursos().addAll(professor.getCursos());
		}	
		
		// Realiza a busca e preenche os resultados
		entities.addAll(turmaService.retrieveByTurmaFilter(filter));								
	}
	
	public void listaAlunos(Turma turma) 
	{
		alunos = inscricaoService.retrieveAlunosByTurma(turma.getId());
		
		// Apresenta o diálogo 
    	showDialog("turmaAlunosDialog");
	}		
			
	//
	// Métodos de Acesso
	//	
	public List<Turma> getEntities() 
	{
		return entities;  
	}
	
	public String getFiltroNome() 
	{
		return filtroNome;
	}
	
	public void setFiltroNome(String filtroNome) 
	{
		this.filtroNome = filtroNome;
	}	
	
	public String getFiltroCodTurm()
	{
		return filtroCodTurm;
	}

	public void setFiltroCodTurm(String filtroCodTurm)
	{
		this.filtroCodTurm = filtroCodTurm;
	}

	public String getFiltroCodDisc()
	{
		return filtroCodDisc;
	}

	public void setFiltroCodDisc(String filtroCodDisc)
	{
		this.filtroCodDisc = filtroCodDisc;
	}

	public Integer getFiltroSemestre()
	{
		return filtroSemestre;
	}

	public void setFiltroSemestre(Integer filtroSemestre)
	{
		this.filtroSemestre = filtroSemestre;
	}

	public Integer getFiltroAno()
	{
		return filtroAno;
	}

	public void setFiltroAno(Integer filtroAno)
	{
		this.filtroAno = filtroAno;
	}
	
	public List<Aluno> getAlunos() {
		return alunos;
	}
	
	public void setAlunos(List<Aluno> alunos) {
		this.alunos = alunos;
	}
	
	public void setFiltroVersaoCurso(VersaoCurso filtroVersaoCurso) {
		this.filtroVersaoCurso = filtroVersaoCurso;
	}
	
	public VersaoCurso getFiltroVersaoCurso() {
		return filtroVersaoCurso;
	}
}
