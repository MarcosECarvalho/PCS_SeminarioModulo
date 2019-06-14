package br.eic.sca.modules.mtr.dao;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import br.eic.sca.modules.mtr.domain.TurmaMonitoria;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
({
	"file:src/main/webapp/WEB-INF/spring-servlet.xml"
})
public class TurmaMonitoriaDaoTest 
{
	@Autowired
	TurmaMonitoriaDao tmDao;
		
	@Test
	public void testDao()
	{
		Assert.assertNotNull(tmDao);
		
		List<TurmaMonitoria> tms = tmDao.retrieveAll();
		
		Assert.assertFalse(tms.isEmpty());
		
		for (TurmaMonitoria tm : tms)
		{
			System.out.println(tm);
		}
	}
}
