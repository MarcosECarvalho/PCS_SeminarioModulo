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
import br.eic.sca.modules.sie.domain.Inscricao;
import br.eic.sca.modules.sie.domain.VersaoCurso;
import br.eic.sca.modules.sie.filters.AlunoFilter;
import br.eic.sca.modules.sie.service.AlunoService;
import br.eic.sca.modules.sie.service.InscricaoService;
import br.eic.sca.modules.sie.service.VersaoCursoService;

@Controller
@Scope("session")
public class AlunoBean extends _Bean
{             
	//
	// Dependências
	//
	@Autowired
	protected SessionBean sessionBean;
	@Autowired
	protected AlunoService alunoService;
	@Autowired
	protected VersaoCursoService versaoCursoService;
	@Autowired
	protected InscricaoService inscricaoService;
	
	//
	// Dados da Página (Devem possuir métodos de acesso)
	//
	private String filtroNome;
	private String filtroCPF;
	private String filtroMatr;
	private VersaoCurso filtroVersaoCurso;
	private List<Aluno> entities = new ArrayList<Aluno>();
	
	//
	// Dados do Diálogo
	//
	private List<Inscricao> inscricoes = new ArrayList<Inscricao>();
    		
	//
	// Inicialização dos Dados (Roda na construção do bean e no recarregamento da página)
	//
	@PostConstruct
	public void refresh() 
	{
		filtroVersaoCurso = null;
		filtroMatr = "";
		filtroCPF = "";
		filtroNome = "";
		entities.clear();
	}
	
	//
	// Operações da Página
	//
	public List<VersaoCurso> getOpcoesVersaoCurso()
	{
		return versaoCursoService.retrieveAll();
	}
	
	@SuppressWarnings("unchecked")
	public void retrieve()
	{
		// Limpa os resultados da tela
		entities.clear();
		
		// Reseta os critérios de ordenação da tabela para o padrão definido na página
		resetDataTableUI("pageForm:table");
		
		// Cria objeto para filtrar os novos resultados
		AlunoFilter filter = new AlunoFilter(filtroNome, filtroCPF, filtroMatr, filtroVersaoCurso);

		// Se o usuário logado for coordenador, adiciona os cursos coordenados ao filtro
		if (sessionBean.hasRole("COORDENADOR"))
		{			
			List<Coordenacao> coordenacoes = (List<Coordenacao>)sessionBean.getUserData("COORDENADOR");
			
			for (Coordenacao coordenacao : coordenacoes) 
			{
				filter.getCursos().add(coordenacao.getCurso());
			}						
		}

		// Realiza a busca e preenche os resultados
		entities.addAll(alunoService.retrieveByAlunoFilter(filter));		
	}
	
	public int getTotalEntities()
	{
		return entities.size();
	}
			
	public void listaHistoricoAlunos(Aluno aluno) 
	{
		//alunos = inscricaoService.retrieveAlunosByTurma(turma.getId());
		setInscricoes(inscricaoService.retrieveByAluno(aluno.getId()));
		
		// Apresenta o diálogo 
    	showDialog("alunosHistoricoDialog");
	}	
	
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
	
	//
	// Métodos de Acesso
	//	
	public List<Aluno> getEntities() 
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
		
	public String getFiltroCPF()
	{
		return filtroCPF;
	}

	public void setFiltroCPF(String filtroCPF)
	{
		this.filtroCPF = filtroCPF;
	}

	public String getFiltroMatr()
	{
		return filtroMatr;
	}

	public void setFiltroMatr(String filtroMatr)
	{
		this.filtroMatr = filtroMatr;
	}

	public List<Inscricao> getInscricoes() {
		return inscricoes;
	}

	public void setInscricoes(List<Inscricao> inscricoes) {
		this.inscricoes = inscricoes;
	}

	public VersaoCurso getFiltroVersaoCurso() {
		return filtroVersaoCurso;
	}
	
	public void setFiltroVersaoCurso(VersaoCurso filtroVersaoCurso) {
		this.filtroVersaoCurso = filtroVersaoCurso;
	}
	
	
}  