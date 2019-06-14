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
import br.eic.sca.modules.sie.domain.Inscricao;
import br.eic.sca.modules.sie.filters.InscricaoFilter;
import br.eic.sca.modules.sie.service.InscricaoService;

@Controller
@Scope("session")
public class InscricaoBean extends _Bean
{             
	//
	// Dependências
	//
	@Autowired
	protected SessionBean sessionBean;
	@Autowired
	protected InscricaoService inscricaoService;
	
	//
	// Dados da Página (Devem possuir métodos de acesso)
	//
	private String filtroNomeDisc;
	private String filtroCodDisc;
	private String filtroCodTurm;
	private String filtroNomeAlun;
	private String filtroMatrAlun;	
	private List<Inscricao> entities = new ArrayList<Inscricao>();
	
	//
	// Inicialização dos Dados (Roda na construção do bean e no recarregamento da página)
	//
	@PostConstruct
	public void refresh() 
	{
		filtroNomeDisc = "";
		filtroCodDisc = "";		
		filtroCodTurm = "";
		filtroNomeAlun = "";
		filtroMatrAlun = "";          
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
		InscricaoFilter filter = new InscricaoFilter(filtroNomeDisc, filtroCodDisc, filtroCodTurm, filtroNomeAlun, filtroMatrAlun);

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
		entities.addAll(inscricaoService.retrieveByInscricaoFilter(filter));					
	}
	
	public int getTotalEntities()
	{
		return entities.size();
	}
			
	//
	// Métodos de Acesso
	//		
	public List<Inscricao> getEntities() 
	{
		return entities;  
	}
	
	public String getFiltroNomeDisc()
	{
		return filtroNomeDisc;
	}

	public void setFiltroNomeDisc(String filtroNomeDisc)
	{
		this.filtroNomeDisc = filtroNomeDisc;
	}

	public String getFiltroCodDisc()
	{
		return filtroCodDisc;
	}

	public void setFiltroCodDisc(String filtroCodDisc)
	{
		this.filtroCodDisc = filtroCodDisc;
	}

	public String getFiltroCodTurm()
	{
		return filtroCodTurm;
	}

	public void setFiltroCodTurm(String filtroCodTurm)
	{
		this.filtroCodTurm = filtroCodTurm;
	}

	public String getFiltroNomeAlun()
	{
		return filtroNomeAlun;
	}

	public void setFiltroNomeAlun(String filtroNomeAlun)
	{
		this.filtroNomeAlun = filtroNomeAlun;
	}

	public String getFiltroMatrAlun()
	{
		return filtroMatrAlun;
	}

	public void setFiltroMatrAlun(String filtroMatrAlun)
	{
		this.filtroMatrAlun = filtroMatrAlun;
	}
}  