package br.eic.sca.modules.sie.control;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import br.eic.sca.api.control._Bean;
import br.eic.sca.core.login.SessionBean;
import br.eic.sca.modules.sie.domain.Coordenacao;
import br.eic.sca.modules.sie.domain.Disciplina;
import br.eic.sca.modules.sie.domain.Professor;
import br.eic.sca.modules.sie.domain.VersaoCurso;
import br.eic.sca.modules.sie.filters.DisciplinaFilter;
import br.eic.sca.modules.sie.service.DisciplinaService;
import br.eic.sca.modules.sie.service.VersaoCursoService;

@Controller
@Scope("session")
public class DisciplinaBean extends _Bean
{             
	//
	// Dependências
	//
	@Autowired
	protected SessionBean sessionBean;
	@Autowired
	protected DisciplinaService disciplinaService;
	@Autowired
	protected VersaoCursoService versaoCursoService;
	
	//
	// Dados da Página (Devem possuir métodos de acesso)
	//
	private String filtroNome;
	private String filtroPeriodo;
	private String filtroCodigo;
	private Integer filtroOptativa;
	private VersaoCurso filtroVersaoCurso;
	private List<Disciplina> entities = new ArrayList<Disciplina>();
	
	//
	// Inicialização dos Dados (Roda na construção do bean e no recarregamento da página)
	//
	@PostConstruct
	public void refresh() 
	{
		filtroVersaoCurso = null;
		filtroNome = "";   
		filtroPeriodo = "";
		filtroCodigo = "";
		filtroOptativa = 0;
		entities.clear();  
	}
	
	//
	// Operações da Página
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
	
	@SuppressWarnings("unchecked")
	public void retrieve()
	{
		// Limpa os resultados da tela
		entities.clear();

		// Reseta os critérios de ordenação da tabela para o padrão definido na página
		resetDataTableUI("pageForm:table");
		
		// Cria objeto para filtrar os novos resultados
		DisciplinaFilter filter = new DisciplinaFilter(filtroNome, filtroPeriodo, filtroCodigo, filtroVersaoCurso, filtroOptativa);

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
		entities.addAll(disciplinaService.retrieveByDisciplinaFilter(filter));						
	}
	
	public int getTotalEntities()
	{
		return entities.size();
	}
			
	//
	// Métodos de Acesso
	//
	public List<Disciplina> getEntities() 
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

	public String getFiltroPeriodo() {
		return filtroPeriodo;
	}

	public void setFiltroPeriodo(String filtroPeriodo) {
		this.filtroPeriodo = filtroPeriodo;
	}

	public String getFiltroCodigo() {
		return filtroCodigo;
	}

	public void setFiltroCodigo(String filtroCodigo) {
		this.filtroCodigo = filtroCodigo;
	}

	public Integer getFiltroOptativa() {
		return filtroOptativa;
	}

	public void setFiltroOptativa(Integer filtroOptativa) {
		this.filtroOptativa = filtroOptativa;
	}	
	
	public VersaoCurso getFiltroVersaoCurso() {
		return filtroVersaoCurso;
	}
	
	public void setFiltroVersaoCurso(VersaoCurso filtroVersaoCurso) {
		this.filtroVersaoCurso = filtroVersaoCurso;
	}
	
}  