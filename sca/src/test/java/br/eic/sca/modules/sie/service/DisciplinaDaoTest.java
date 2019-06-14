package br.eic.sca.modules.sie.service;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import br.eic.sca.modules.sie.dao.impl.DisciplinaDaoImpl;
import br.eic.sca.modules.sie.domain.Disciplina;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
({
	"file:src/main/webapp/WEB-INF/spring-data.xml",
	"file:src/main/webapp/WEB-INF/spring-servlet.xml"
})
public class DisciplinaDaoTest 
{
	@Autowired
	DisciplinaDaoImpl dao;
		
	@Test
	public void testDao()
	{
		Assert.assertNotNull(dao);
		for (Disciplina d : dao.retrieveAll()) 
		{
			System.out.println(d.getId());
			System.out.println(d);
		}		
	}
}
