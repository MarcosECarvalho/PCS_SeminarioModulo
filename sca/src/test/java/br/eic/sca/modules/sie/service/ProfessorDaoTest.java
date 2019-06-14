package br.eic.sca.modules.sie.service;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import br.eic.sca.modules.sie.dao.impl.ProfessorDaoImpl;
import br.eic.sca.modules.sie.domain.Professor;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
({
	"file:src/main/webapp/WEB-INF/spring-data.xml",
	"file:src/main/webapp/WEB-INF/spring-servlet.xml"	
})
public class ProfessorDaoTest 
{
	@Autowired
	ProfessorDaoImpl dao;
		
	@Test
	public void testDao()
	{
		Assert.assertNotNull(dao);

		List<Professor> profs = dao.retrieveAll();
		Assert.assertFalse(profs.isEmpty());
		
		for (Professor p: profs)
		{
			System.out.println(p);
		}		
	}
}
