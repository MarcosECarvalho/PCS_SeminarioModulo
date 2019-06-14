package br.eic.sca.modules.sie.service;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import br.eic.sca.modules.sie.dao.impl.InscricaoDaoImpl;
import br.eic.sca.modules.sie.domain.Aluno;
import br.eic.sca.modules.sie.domain.Inscricao;
import br.eic.sca.modules.sie.domain.Turma;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
({
	"file:src/main/webapp/WEB-INF/spring-servlet.xml"	
})
public class InscricaoDaoTest 
{
	@Autowired
	InscricaoDaoImpl dao;
		
	@Test
	public void testDao()
	{
		Assert.assertNotNull(dao);
		
		List<Inscricao> inscricoes = dao.retrieveAll();		
		Assert.assertFalse(inscricoes.isEmpty());	
		
		Turma turma = inscricoes.get(0).getTurma();
		List<Aluno> alunos = dao.retrieveAlunosByTurma(turma.getId());
		
		System.out.println(alunos);
	}
}
