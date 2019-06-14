package br.eic.sca.modules.sie.service;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import br.eic.sca.modules.sie.dao.impl.VersaoCursoDaoImpl;
import br.eic.sca.modules.sie.domain.VersaoCurso;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
({
	"file:src/main/webapp/WEB-INF/spring-data.xml",
	"file:src/main/webapp/WEB-INF/spring-servlet.xml"
})
public class VersaoCursoDaoTest 
{
	@Autowired
	VersaoCursoDaoImpl dao;
		
	@Test
	public void testDao()
	{
		Assert.assertNotNull(dao);
		List<VersaoCurso> vCursos = dao.retrieveAll();
		
		Assert.assertFalse(vCursos.isEmpty());
		
		for (VersaoCurso v: vCursos)
		{
			System.out.println(v);
		}
	}
}
