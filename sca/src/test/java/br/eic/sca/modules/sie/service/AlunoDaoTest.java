package br.eic.sca.modules.sie.service;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import br.eic.sca.modules.sie.dao.impl.AlunoDaoImpl;
import br.eic.sca.modules.sie.domain.Aluno;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
({
	"file:src/main/webapp/WEB-INF/spring-data.xml",
	"file:src/main/webapp/WEB-INF/spring-servlet.xml"
})
public class AlunoDaoTest 
{
	@Autowired
	AlunoDaoImpl dao;
		
	@Test
	public void testDao()
	{
		Assert.assertNotNull(dao);
		List<Aluno> alunos = dao.retrieveAll();
		
		Assert.assertFalse(alunos.isEmpty());
		
		for (Aluno a: alunos)
		{
			System.out.println(a.getId());
			System.out.println(a);
		}
	}
}
