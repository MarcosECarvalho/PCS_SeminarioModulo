package br.eic.sca.modules.sie.control;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.annotation.PostConstruct;

import org.hibernate.exception.ConstraintViolationException;
import org.primefaces.component.datagrid.DataGrid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import br.eic.sca.api.control._Bean;
import br.eic.sca.modules.sie.domain.Coordenacao;
import br.eic.sca.modules.sie.domain.Curso;
import br.eic.sca.modules.sie.domain.Professor;
import br.eic.sca.modules.sie.service.CoordenacaoService;
import br.eic.sca.modules.sie.service.CursoService;
import br.eic.sca.modules.sie.service.ProfessorService;

@Controller
@Scope("session")
public class CoordenacaoBean extends _Bean
{             
	//
	// Dependências
	//
	@Autowired
	protected CursoService cursoService;
	@Autowired
	protected ProfessorService professorService;
	@Autowired
	protected CoordenacaoService coordenacaoService;
	
	//
	// Dados da Página 
	//
	private Integer 	filtroCursoId;
	private List<Curso> filtroCursoOpcoes = new ArrayList<Curso>();	
	private List<Coordenacao> entities = new ArrayList<Coordenacao>();
	
	//
	// Dados do Diálogo
	//
	private String 	    	filtroDialogProfessorNomeMatr;
	private Integer 		filtroDialogCursoId;
	private List<Curso> 	cursosDisponiveis = new ArrayList<Curso>();    		 	 
	private List<Professor> professoresDisponiveis = new ArrayList<Professor>(); 	 
	
	// 
	// Inicialização dos Dados 
	//
	@PostConstruct
	public void refresh() 
	{
		refreshPage();
		refreshDialog();	
	}
	
	private void refreshPage()
	{
		filtroCursoId = 0;
		filtroCursoOpcoes = cursoService.retrieveAll();
		entities = coordenacaoService.retrieveAll();
		entities.sort(null);
	}
	
	private void refreshDialog()
	{
		filtroDialogProfessorNomeMatr = "";
		filtroDialogCursoId = 0;          
		cursosDisponiveis.clear();  
		professoresDisponiveis.clear();	
	}
	
	// =================== //
	// OPERAÇÕES DA PÁGINA //
	// =================== //
	public int getTotalEntities()
	{
		return entities.size();
	}
	
	public void retrieve()
	{
		// Limpa os resultados da tela
		entities.clear();
		
		// Reseta o DataGrid da página para as configurações básicas
		DataGrid dataGrid = (DataGrid)findUIComponent("pageForm:grid");
		dataGrid.setFirst(0);
		
		// Refaz a busca de acordo com os filtros disponíveis
		if (filtroCursoId==0)
		{			 
			entities.addAll(coordenacaoService.retrieveAll());
		}
		else
		{
			Curso curso = cursoService.retrieveById(filtroCursoId);
			List<Coordenacao> coord = coordenacaoService.retrieveByCurso(curso);
			if (coord != null)
				entities.addAll(coord);
		}			
		
		// Ordena os resultados da busca
		Collections.sort(entities);
	}
	
	public void create()
	{
		try
		{
			// Reseta os campos que serão apresentados no diálogo 
			refreshDialog();
			
			// Recupera os professores e cursos disponíveis para o diálogo
			cursosDisponiveis.clear();
			cursosDisponiveis.addAll(cursoService.retrieveAll());
			professoresDisponiveis.clear();
			professoresDisponiveis.addAll(professorService.retrieveAll());
			
			Collections.sort(cursosDisponiveis);
			Collections.sort(professoresDisponiveis);
			
			// Apresenta o diálogo 
			showDialog("coordEditDialog");
		}
		catch(Exception e)
		{
			handleError(e);
		}
	}
	
	public void delete(Coordenacao coordenacao)	
	{
		try
		{
			// Remove o item selecionado
			coordenacaoService.delete(coordenacao);
			
			// Refaz a busca original para filtrar o item removido
			retrieve();
			
			// Apresenta mensagem de sucesso
			popWarning("Registro Removido com Sucesso");
		}
		catch(Exception e)
		{
			handleError(e);
		}
	}
	
	// =================================================== //
	// OPERAÇÕES DO DIÁLOGO (coordenacoesEditDialog.xhtml) //
	// =================================================== //
	public List<Curso> getOpcoesCurso()
	{
		return cursoService.retrieveAll();
	}	
	
	public void filterProfessores()
	{
		professoresDisponiveis.clear();
		
		if (filtroDialogProfessorNomeMatr.isEmpty())
		{
			professoresDisponiveis.addAll(professorService.retrieveAll());
		}
		else
		{
			professoresDisponiveis.addAll(professorService.retrieveByNome(filtroDialogProfessorNomeMatr));
			
			Professor professor = professorService.retrieveByMatricula(filtroDialogProfessorNomeMatr);
			if (professor!=null)
				professoresDisponiveis.add(professor);									
		}
		
		Collections.sort(professoresDisponiveis);		
	}
	
	public void persist(Professor professor)	
	{
		try
		{
			// Recupera o curso selecionado
			Curso curso = cursoService.retrieveById(filtroDialogCursoId);		
			
			// Cria o objeto de coordenação
			Coordenacao coordenacao = new Coordenacao();
			coordenacao.setCurso(curso);
			coordenacao.setCursoId(curso.getId());
			coordenacao.setProfessor(professor);
			coordenacao.setProfessorId(professor.getId());
			
			// Persiste a nova coordenação
			coordenacaoService.persist(coordenacao);
			
			// Refaz a busca original para carregar o item inserido
			retrieve();
						
			// Remove o diálogo e apresenta mensagem de sucesso
			hideDialog("coordEditDialog");
			popInfo("Registro Atualizado com Sucesso");
		}
		catch(ConstraintViolationException e)
		{
			popWarning("Este professor já é coordenador do curso escolhido");
		}
		catch(Exception e)
		{
			handleError(e);
		}
	}
	
	//
	// Métodos de Acesso
	//		
	public List<Coordenacao> getEntities() {
		return entities;  
	}

	public List<Curso> getFiltroCursoOpcoes() {
		return filtroCursoOpcoes;
	}	

	public String getFiltroDialogProfessorNomeMatr() {
		return filtroDialogProfessorNomeMatr;
	}

	public void setFiltroDialogProfessorNomeMatr(String filtroDialogProfessorNomeMatr) {
		this.filtroDialogProfessorNomeMatr = filtroDialogProfessorNomeMatr;
	}

	public Integer getFiltroDialogCursoId() {
		return filtroDialogCursoId;
	}

	public void setFiltroDialogCursoId(Integer filtroDialogCursoId) {
		this.filtroDialogCursoId = filtroDialogCursoId;
	}

	public List<Professor> getProfessoresDisponiveis() {
		return professoresDisponiveis;
	}

	public Integer getFiltroCursoId() {
		return filtroCursoId;
	}

	public void setFiltroCursoId(Integer filtroCursoId) {
		this.filtroCursoId = filtroCursoId;
	}

	public List<Curso> getCursosDisponiveis() {
		return cursosDisponiveis;
	}
}  