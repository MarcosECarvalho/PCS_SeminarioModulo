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
import br.eic.sca.modules.sie.domain.VersaoCurso;
import br.eic.sca.modules.sie.filters.VersaoCursoFilter;
import br.eic.sca.modules.sie.service.VersaoCursoService;

@Controller
@Scope("session")
public class VersaoCursoBean extends _Bean
{
	//
	// Dependências
	//
	@Autowired
	private SessionBean sessionBean;
	
	@Autowired
	protected VersaoCursoService versaoCursoService;
	
	//
	// Dados da Página (Devem possuir métodos de acesso)
	//
	private String filtroNome;
	private String filtroSigla;
	private String filtroVersao;
	private List<VersaoCurso> entities = new ArrayList<VersaoCurso>();
	
	//
	// Inicialização dos Dados (Roda na construção do bean e no recarregamento da página)
	//
	@PostConstruct
	public void refresh() 
	{		
		filtroNome = "";          
		filtroSigla = "";
		filtroVersao = "";
		entities.clear();
	}
	
	//
	// Operações da Página
	//
	@SuppressWarnings("unchecked")
	public void retrieve()
	{
		// Limpa os resultados da tela
		entities.clear();		
		
		// Reseta os critérios de ordenação da tabela para o padrão definido na página
		resetDataTableUI("pageForm:table");
		
		// Cria objeto para filtrar os novos resultados
		VersaoCursoFilter filter = new VersaoCursoFilter(filtroNome, filtroSigla, filtroVersao);
		
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
		entities.addAll(versaoCursoService.retrieveByVersaoCursoFilter(filter));			
	}
	

	public int getTotalEntities()
	{
		return entities.size();
	}
			
	//
	// Métodos de Acesso
	//		
	public List<VersaoCurso> getEntities() 
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
	
	public String getFiltroSigla() 
	{
		return filtroSigla;
	}
	
	public void setFiltroSigla(String filtroSigla) 
	{
		this.filtroSigla = filtroSigla;
	}

	public String getFiltroVersao() 
	{
		return filtroVersao;
	}

	public void setFiltroVersao(String filtroVersao) 
	{
		this.filtroVersao = filtroVersao;
	}
	
	public void setSessionBean(SessionBean sessionBean) 
	{
		this.sessionBean = sessionBean;
	}
}