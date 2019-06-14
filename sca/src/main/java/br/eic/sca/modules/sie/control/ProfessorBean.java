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
import br.eic.sca.modules.sie.domain.Professor;
import br.eic.sca.modules.sie.filters.ProfessorFilter;
import br.eic.sca.modules.sie.service.ProfessorService;

@Controller
@Scope("session")
public class ProfessorBean extends _Bean
{
	//
	// Dependências
	//
	@Autowired
	protected SessionBean sessionBean;
	@Autowired
	protected ProfessorService professorService;
	
	//
	// Dados da Página (Devem possuir métodos de acesso)
	//
	private String filtroNome;
	private String filtroMatr;
	private List<Professor> entities = new ArrayList<Professor>();
	
	//
	// Inicialização dos Dados (Roda na construção do bean e no recarregamento da página)
	//
	@PostConstruct
	public void refresh() 
	{
		filtroNome = "";
		filtroMatr = "";   		  
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
		ProfessorFilter filter = new ProfessorFilter(filtroNome, filtroMatr);

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
		entities.addAll(professorService.retrieveByProfessorFilter(filter));					
	}
	
	public int getTotalEntities()
	{
		return entities.size();
	}
			
	//
	// Métodos de Acesso
	//	
	public List<Professor> getEntities() 
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
	
	public String getFiltroMatr()
	{
		return filtroMatr;
	}

	public void setFiltroMatr(String filtroMatr)
	{
		this.filtroMatr = filtroMatr;
	}
}
