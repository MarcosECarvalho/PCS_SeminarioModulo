package br.eic.sca.modules.sie;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.DependsOn;

import br.eic.sca.api.ScaModule;
import br.eic.sca.core.security.LoggedRole;
import br.eic.sca.core.security.Permission;
import br.eic.sca.modules.sie.domain.Aluno;
import br.eic.sca.modules.sie.domain.Coordenacao;
import br.eic.sca.modules.sie.domain.Professor;
import br.eic.sca.modules.sie.service.AlunoService;
import br.eic.sca.modules.sie.service.CoordenacaoService;
import br.eic.sca.modules.sie.service.InscricaoService;
import br.eic.sca.modules.sie.service.ProfessorService;

@DependsOn("coreModule")
@ComponentScan("br.eic.sca.modules.sie")
public class SieModule extends ScaModule 
{
	@Autowired
	private AlunoService alunoService;
	@Autowired
	private ProfessorService professorService;
	@Autowired
	private CoordenacaoService coordenacaoService;
	@Autowired
	@SuppressWarnings("unused")
	private InscricaoService inscricaoService;
		
	@Override
	public String getName() 
	{
		return "Acesso SIE";
	}
		
	@Override
	public String[] provideRoles() 
	{
		return new String[] {"ALUNO", "PROFESSOR", "COORDENADOR"};		
	}

	@Override
	public Permission[] providePermissions() 
	{
		return new Permission[] 
		{
			new Permission("Acesso SIE"   , "Relação de Cursos"     , "/sie/cursos.jsf"	     , "ADMIN", "COORDENADOR"),            
			new Permission("Acesso SIE"   , "Relação de Alunos"     , "/sie/alunos.jsf"      , "ADMIN", "COORDENADOR"),			
			new Permission("Acesso SIE"   , "Relação de Professores", "/sie/professores.jsf" , "ADMIN", "COORDENADOR"),			
			new Permission("Acesso SIE"   , "Relação de Disciplinas", "/sie/disciplinas.jsf" , "ADMIN", "COORDENADOR", "PROFESSOR"),
			new Permission("Acesso SIE"   , "Relação de Turmas"     , "/sie/turmas.jsf"      , "ADMIN", "COORDENADOR", "PROFESSOR"),
			new Permission("Acesso SIE"   , "Relação de Inscrições" , "/sie/inscricoes.jsf"  , "ADMIN", "COORDENADOR"),
			new Permission("Administração", "Coordenações de Curso" , "/sie/coordenacoes.jsf", "ADMIN", "COORDENADOR")
		};
	}

	@Override
	public List<LoggedRole> doLogin(String username, String password) 
	{
		List<LoggedRole> loggedRoles = new ArrayList<LoggedRole>();		

		//
		// Login de Aluno
		//
		Aluno aluno = alunoService.retrieveByMatricula(username); 
		if (aluno != null)
		{
			loggedRoles.add(new LoggedRole("ALUNO",aluno));
		}
		
		//
		// Login de Professor
		//
		Professor professor = professorService.retrieveByMatricula(username);
		if (professor != null)
		{
			loggedRoles.add(new LoggedRole("PROFESSOR",professor));
			
			//
			// Logins de Coordenação
			//
			List<Coordenacao> coordenacoes = coordenacaoService.retrieveByProfessor(professor);
			if (coordenacoes != null && !coordenacoes.isEmpty())
			{
				loggedRoles.add(new LoggedRole("COORDENADOR",coordenacoes));				
			}
		}				
		
		return loggedRoles;
	}
}
