package br.eic.sca.modules.admin.dao;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import br.eic.sca.modules.sie.dao.CoordenacaoDao;
import br.eic.sca.modules.sie.dao.CursoDao;
import br.eic.sca.modules.sie.domain.Coordenacao;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
({
	"file:src/main/webapp/WEB-INF/spring-servlet.xml"
})
public class CoordenacaoDaoTest 
{
	@Autowired
	CoordenacaoDao dao;
	
	@Autowired
	CursoDao cursoDao;
		
	@Test
	public void testDao()
	{
		Assert.assertNotNull(dao);
		List<Coordenacao> coordenacoes = dao.retrieveAll();
		
		Assert.assertFalse(coordenacoes.isEmpty());
		
		for (Coordenacao a: coordenacoes)
		{
			System.out.println(a);
		}
		
		Coordenacao cd = coordenacoes.get(0);
		cd.setCursoId(null);
		cd.setProfessorId(null);
		dao.persist(coordenacoes.get(0));
	}
}
