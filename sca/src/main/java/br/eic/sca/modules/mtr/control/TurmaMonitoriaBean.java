package br.eic.sca.modules.mtr.control;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import br.eic.sca.api.control._Bean;
import br.eic.sca.modules.mtr.domain.TurmaMonitoria;
import br.eic.sca.modules.mtr.service.TurmaMonitoriaService;

@Controller
@Scope("session")
public class TurmaMonitoriaBean extends _Bean 
{
	//
	// Dependências
	//
	@Autowired
	TurmaMonitoriaService turmaMonitoriaService;
	
	//
	// Dados da Página
	//
	List<TurmaMonitoria> turmas = new ArrayList<TurmaMonitoria>();
	TurmaMonitoria turmaEditable = new TurmaMonitoria();
	
	//
	// Inicialização dos Dados (Roda na construção do bean e no recarregamento da página)
	//
	@PostConstruct
	public void refresh() 
	{			
		turmas = turmaMonitoriaService.retrieveAll();
	}
	
	//
	// Operações da Página
	//
	public void create()
	{
		// Cria uma instância limpa para edição
		this.turmaEditable = new TurmaMonitoria();
		
		// Apresenta o diálogo 
    	showDialog("turmaMonitoriaEditDialog");
	}
	
	public void update(TurmaMonitoria turmaEditable)
	{
		// Guarda a instância que será editada
		this.turmaEditable = turmaEditable;
		
		// Apresenta o diálogo 
    	showDialog("turmaMonitoriaEditDialog");
	}
	
	public void delete(TurmaMonitoria turma)
	{
		try
		{
			turmaMonitoriaService.delete(turma);
			refresh();
		}
		catch(Exception e)
		{
			handleError(e);
		}
	}
	
	public void persist()
	{
		try
		{
			// Validações Complexas
			if (turmaEditable.getHoraInicio().isAfter(turmaEditable.getHoraFim()))
			{
				popWarning("A hora de término deve ser após a hora de início");
				return;
			}
			
			// Periste o objeto
			turmaMonitoriaService.persist(turmaEditable);
			
			// Atualizações de interface
			hideDialog("turmaMonitoriaEditDialog");
			popInfo("Registro Atualizado com Sucesso");
			refresh();
		}
		catch(Exception e)
		{
			handleError(e);
		}
	}
	
	//
	// Métodos de Acesso
	//
	public List<TurmaMonitoria> getTurmas() {
		return turmas;
	}
	
	public void setTurmas(List<TurmaMonitoria> turmas) {
		this.turmas = turmas;
	}	
	
	public TurmaMonitoria getTurmaEditable() {
		return turmaEditable;
	}
	
	public void setTurmaEditable(TurmaMonitoria turmaEditable) {
		this.turmaEditable = turmaEditable;
	}
}








