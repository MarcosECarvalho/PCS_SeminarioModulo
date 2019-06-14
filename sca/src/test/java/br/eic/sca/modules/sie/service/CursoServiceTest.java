package br.eic.sca.modules.sie.service;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import br.eic.sca.modules.sie.domain.Curso;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
({
	"file:src/main/webapp/WEB-INF/spring-servlet.xml"	
})
public class CursoServiceTest 
{
	@Autowired
	CursoService service;
		
	@Test
	public void testDao()
	{
		Assert.assertNotNull(service);
		for (Curso c : service.retrieveAll())
		{
			System.out.println(c);
		}
	}
}
