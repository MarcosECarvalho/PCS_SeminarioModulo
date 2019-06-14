package br.eic.sca.modules.sie.dao.impl;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.TreeMap;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import br.eic.sca.modules.sie.dao.CursoDao;
import br.eic.sca.modules.sie.domain.Curso;
import br.eic.sca.modules.sie.filters.CursoFilter;

@Primary
@Component
public class CursoDaoImpl extends _DaoSieAbstract<Curso> implements CursoDao
{
	//
	// Propriedades
	//
	@Value("${sca.siefolder}")
	String siefolderPath;
	
	//
	// Atributos
	//
	private static HashMap<Integer,Curso> cursosIdMap = new HashMap<Integer,Curso>();
	private static TreeMap<String,Curso> cursosSiglaMap = new TreeMap<String,Curso>();
	
	//
	// Inicialização
	//
	@PostConstruct
	private void init() 
	{
		LOG.debug("-----------------");
		LOG.debug("Carregando Cursos");
		LOG.debug("-----------------");
		
		// Metadados do carregamento
		int records = 0;
		long timestamp = System.currentTimeMillis();
		
		// Corrige o path se necessário
		if (!siefolderPath.endsWith("\\"))
			siefolderPath=siefolderPath+"\\";		
		
		//
		// 11.02.01.99.0X
		//
		{
			File dir = new File(siefolderPath + "11.02.01.99.0X - Currículos dos Cursos");
			File arquivos[] = dir.listFiles();
			
			// Verifica se há planilha(s)
			if (arquivos==null || arquivos.length==0)
				throw new IllegalStateException("Não foi possível encontrar planilhas na pasta "+dir+". Verifique o arquivo de configuração 'sca.properties' e reinicie o servidor.");
			
			try 
			{	
				for(File arquivo : arquivos) 
				{
					FileInputStream inputStream = new FileInputStream(arquivo);
					Workbook 		workbook 	= new HSSFWorkbook(inputStream);
					Sheet 			firstSheet 	= workbook.getSheetAt(0);
					
					Iterator<Row> rows = firstSheet.iterator();
					
					// Indexa as colunas pelo cabeçalho
					TreeMap<Integer,String> headerMap = new TreeMap<Integer,String>();
					Iterator<Cell> headerCells = rows.next().cellIterator();
					do
					{
						Cell headerCell = headerCells.next();
						headerMap.put(headerCell.getColumnIndex(), headerCell.getStringCellValue());
					}
					while (headerCells.hasNext());
					
					// Processo o conteúdo do arquivo
					do
					{
						String sigla = "";
						String nome  = "";
						
						Iterator<Cell> cells = rows.next().cellIterator();
						do  
						{
							Cell cell = cells.next();
							
							switch (headerMap.get(cell.getColumnIndex())) 
							{
								case "COD_CURSO":
									sigla = cell.getStringCellValue();
									break;
								case "NOME_UNIDADE":
									nome = cell.getStringCellValue();
									break;
							}												
						}
						while (cells.hasNext());
						
						if (validate(sigla,nome) && !cursosSiglaMap.containsKey(sigla))
						{
							Curso curso = new Curso (sigla, nome);
							cursosIdMap.put(curso.getId(),curso);
							cursosSiglaMap.put(sigla,curso);
							records++;
						}
					}
					while (rows.hasNext()); 
						
					workbook.close();
					inputStream.close();
				}					
				
				LOG.info("Cursos Encontrados - "+records+" ("+(System.currentTimeMillis()-timestamp)+"ms)");
			} 
			catch (IllegalStateException e) 
			{
				throw e;
			}
			catch (Exception e) 
			{
				throw new IllegalStateException("Não foi possível ler a(s) planilha(s) em "+dir+".",e);
			}
		}		
	}
	
	//
	// Operações
	//	
	@Override
	public Curso retrieveById(Integer id) 
	{
		return cursosIdMap.get(id);
	}

	@Override
	public Curso retrieveBySigla(String sigla) 
	{
		for (Curso c : cursosIdMap.values())
		{
			if (c.getSigla().equals(sigla))
				return c;
		}
		return null;
	}
	
	@Override
	public List<Curso> retrieveAll() 
	{
		return new ArrayList<Curso>(cursosIdMap.values());
	}
		
	@Override
	public List<Curso> retrieveByScanning(String filter) 
	{
		List<Curso> match = new ArrayList<Curso>();
		
		if (filter==null || filter.isEmpty())
			return match;
		else
			filter = StringUtils.stripAccents(filter.toLowerCase());
		
		for (Curso curso : retrieveAll()) 
		{
			String nome = StringUtils.stripAccents(curso.getNome()).toLowerCase();
			String sigla = StringUtils.stripAccents(curso.getSigla()).toLowerCase();
			
			if(nome.contains(filter) || sigla.startsWith(filter))
			{	
				match.add(curso);
			}
		}
		
		return match;
	}
	
	@Override
	public List<Curso> retrieveByFilter(CursoFilter filter)
	{
		String nome = filter.getNome();
		String sigla = filter.getSigla();
		List<Curso> matches = new ArrayList<Curso>();

		for(Curso curso : cursosIdMap.values())
		{
			if(nome.isEmpty() || StringUtils.stripAccents(curso.getNome().toLowerCase()).startsWith(StringUtils.stripAccents(nome.toLowerCase())))
			{
				if(sigla.isEmpty() || curso.getSigla().toLowerCase().equals(sigla.toLowerCase()))
				{
					matches.add(curso);
				}
			}
		}

		return matches;
	}
}
