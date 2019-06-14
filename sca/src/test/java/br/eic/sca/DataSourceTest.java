package br.eic.sca;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.sql.DataSource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
({
	"file:src/main/webapp/WEB-INF/spring-data.xml"	
})
public class DataSourceTest 
{
	@Autowired
	DataSource dataSource;
		
	@Test
	public void testDataSource() throws Exception
	{
		Connection conn = dataSource.getConnection();
		Statement stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery("select * from temp");
		
		while (rs.next());			
		
		rs.close();
		stmt.close();
	}
}
