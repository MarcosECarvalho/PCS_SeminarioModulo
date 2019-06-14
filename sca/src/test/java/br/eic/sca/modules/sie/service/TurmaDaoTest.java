package br.eic.sca.modules.sie.service;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import br.eic.sca.modules.sie.dao.impl.TurmaDaoImpl;
import br.eic.sca.modules.sie.domain.Turma;
import br.eic.sca.modules.sie.filters.TurmaFilter;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
({
	"file:src/main/webapp/WEB-INF/spring-data.xml",
	"file:src/main/webapp/WEB-INF/spring-servlet.xml"
})
public class TurmaDaoTest 
{
	@Autowired
	TurmaDaoImpl dao;
		
	@Test
	public void testDao()
	{
		Assert.assertNotNull(dao);		
		
		List<Turma> turmas = dao.retrieveByTurmaFilterExactCodigo(new TurmaFilter("600004","GTSI1412","",null,null,null));
		
		for (Turma turma : turmas) {
			System.out.println(turma);
		}			
	}
}
