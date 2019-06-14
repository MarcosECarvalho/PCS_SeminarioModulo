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
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import br.eic.sca.modules.sie.dao.VersaoCursoDao;
import br.eic.sca.modules.sie.domain.Curso;
import br.eic.sca.modules.sie.domain.VersaoCurso;
import br.eic.sca.modules.sie.filters.VersaoCursoFilter;

@Component
public class VersaoCursoDaoImpl extends _DaoSieAbstract<VersaoCurso> implements VersaoCursoDao
{
	//
	// Parâmetros
	//
	@Value("${sca.siefolder}")
	String siefolderPath;
	
	//
	// Dependências
	//
	@Autowired
	CursoDaoImpl cursoDao;
	
	//
	// Atributos
	//
	private static HashMap<Integer,VersaoCurso> versaoCursosMap = new HashMap<Integer,VersaoCurso>();
	
	//
	// Inicialização
	//	
	@PostConstruct
	private void init() 
	{
		LOG.debug("---------------------------");
		LOG.debug("Carregando Versões de Curso");
		LOG.debug("---------------------------");
		
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
						String sigla  = "";
						String nome   = "";
						String versao = ""; 
						
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
								case "NUM_VERSAO":
									versao = cell.getCellTypeEnum() == CellType.NUMERIC ? String.valueOf((int)cell.getNumericCellValue()) : cell.getStringCellValue();
									break;
							}												
						}
						while (cells.hasNext());
						
						if (validate(sigla,nome,versao))
						{
							versao = versao.replaceAll("/",".");
							
							Curso curso = cursoDao.retrieveBySigla(sigla);
							
							if (curso!=null)
							{
								VersaoCurso vCurso = new VersaoCurso(versao,curso);
								
								if (!versaoCursosMap.containsKey(vCurso.getId()))
								{
									versaoCursosMap.put(vCurso.getId(),vCurso);
									records++;
								}
							}
							else
							{
								LOG.warn("Não foi possível encontrar o curso "+sigla+" verifique o carregamento das planilhas do DAO de Cursos");
							}
						}
					}
					while (rows.hasNext()); 
						
					workbook.close();
					inputStream.close();
				}					
				
				LOG.info("Versões de Curso Encontradas - "+records+" ("+(System.currentTimeMillis()-timestamp)+"ms)");
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
	public VersaoCurso retrieveById(Integer id) 
	{
		return versaoCursosMap.get(id);
	}
	
	@Override
	public List<VersaoCurso> retrieveAll() 
	{
		return new ArrayList<VersaoCurso>(versaoCursosMap.values());
	}
	
	@Override
	public List<VersaoCurso> retrieveByScanning(String filter) 
	{
		List<VersaoCurso> match = new ArrayList<VersaoCurso>();
		
		if (filter==null || filter.isEmpty())
			return match;
		else
			filter = StringUtils.stripAccents(filter.toLowerCase());
				
		for (VersaoCurso versaoCurso : retrieveAll()) 
		{
			String 	nome   = StringUtils.stripAccents(versaoCurso.getCurso().getNome()).toLowerCase();
			String 	sigla  = StringUtils.stripAccents(versaoCurso.getCurso().getSigla()).toLowerCase();
			String 	versao = StringUtils.stripAccents(versaoCurso.getVersao()).toLowerCase();
			
			if(nome.contains(filter) || sigla.startsWith(filter) || versao.startsWith(filter))
			{	
				match.add(versaoCurso);
			}				
		}
		
		return match;
	}
	
	@Override
	public List<VersaoCurso> retrieveByVersaoCursoFilter(VersaoCursoFilter filter)
	{
		String 	nome = filter.getNome();
		String 	sigla = filter.getSigla();
		String 	versao = filter.getVersao();
		List<Curso> cursos = filter.getCursos();
		
		List<VersaoCurso> match = new ArrayList<VersaoCurso>();
	
		for (VersaoCurso versaoCurso : retrieveAll()) 
		{
			// Ignora versões de curso fora dos cursos alvo da pesquisa
			if (cursos!=null && !cursos.isEmpty() && !cursos.contains(versaoCurso.getCurso()))
				continue;
			
			// Aplica os filtros da pesquisa
			if(nome.isEmpty() || StringUtils.stripAccents(versaoCurso.getCurso().getNome().toLowerCase()).contains((StringUtils.stripAccents(nome.toLowerCase()))))
			{	
				if(sigla.isEmpty() || StringUtils.stripAccents(versaoCurso.getCurso().getSigla().toLowerCase()).startsWith((StringUtils.stripAccents(sigla.toLowerCase()))))
				{	
					if(versao.isEmpty() || versaoCurso.getVersao().startsWith(versao))
					{						
						match.add(versaoCurso);						
					}				
				}	
			}
		}
		
		return match;
	}
}
