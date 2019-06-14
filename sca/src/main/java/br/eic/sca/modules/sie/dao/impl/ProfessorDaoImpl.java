package br.eic.sca.modules.sie.dao.impl;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import br.eic.sca.modules.sie.dao.ProfessorDao;
import br.eic.sca.modules.sie.domain.Curso;
import br.eic.sca.modules.sie.domain.Professor;
import br.eic.sca.modules.sie.filters.ProfessorFilter;

@Component
public class ProfessorDaoImpl extends _DaoSieAbstract<Professor> implements ProfessorDao 
{	
	//
	// Propriedades
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
	private static HashMap<Integer, Professor> professorIdMap = new HashMap<Integer, Professor>();
	private static SortedMap<String, Professor> professorMatrMap = new TreeMap<String, Professor>();
	
	//
	// Inicialização
	//
	@PostConstruct
	private void init() 
	{
		LOG.debug("----------------------");
		LOG.debug("Carregando Professores");
		LOG.debug("----------------------");
		
		// Metadados do carregamento
		int records = 0;
		long timestamp = System.currentTimeMillis();
		
		// Corrige o path se necessário
		if (!siefolderPath.endsWith("\\"))
			siefolderPath=siefolderPath+"\\";
		
		File dir = new File(siefolderPath + "11.02.03.99.19 - Ofertas de Disciplinas");
		File arquivos[] = dir.listFiles();
		
		// Verifica se há planilha(s)		
		if (arquivos==null || arquivos.length==0)
			throw new IllegalStateException("Não foi possível encontrar planilhas na pasta "+dir+". Verifique o arquivo de configuração 'sca.properties' e reinicie o servidor.");
				
		try 
		{						
			for(File arquivo : arquivos) 
			{
				FileInputStream inputStream = new FileInputStream(arquivo);
				Workbook workbook = new HSSFWorkbook(inputStream);
				Sheet firstSheet = workbook.getSheetAt(0);
				
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
					String  codCurso = "";
					String  matricula = null;
					String  nome = "";
										
					Iterator<Cell> cells = rows.next().cellIterator();
					do
					{
						Cell cell = cells.next();
						
						switch (headerMap.get(cell.getColumnIndex())) 
						{
							case "COD_CURSO":  
								codCurso = cell.getStringCellValue();
								break;
							case "MATR_EXTERNA":
								matricula = String.valueOf(((int)cell.getNumericCellValue()));
								break;
							case "NOME_DOCENTE":
								nome = cell.getStringCellValue();
								break;
						}					
					}
					while (cells.hasNext());
					
					if (validate(codCurso,matricula,nome))
					{
						Curso curso = cursoDao.retrieveBySigla(codCurso);	
						
						Professor professor = professorMatrMap.get(matricula);
						
						if (professor==null)
						{
							professor = new Professor(matricula, nome);
							professorIdMap.put(professor.getId(), professor);
							professorMatrMap.put(professor.getMatricula(), professor);
							records++;
						}
												
						professor.getCursos().add(curso);
					}					
				}
				while (rows.hasNext()); 
	
				workbook.close();
				inputStream.close();			
			}
			
			LOG.info("Professores Encontrados - "+records+" ("+(System.currentTimeMillis()-timestamp)+"ms)");
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
	
	//
	// Operações
	//	
	@Override
	public List<Professor> retrieveAll() 
	{			
		return new ArrayList<Professor>(professorIdMap.values());		
	}
		
	@Override
	public Professor retrieveById(Integer id) 
	{
		return professorIdMap.get(id);
	}
	
	@Override
	public Professor retrieveByMatricula(String matricula)
	{
		return professorMatrMap.get(matricula);
	}
	
	@Override
	public List<Professor> retrieveByNome(String nome)
	{
		List<Professor> matches = new ArrayList<Professor>();
		
		for(Professor professor : professorIdMap.values()) 
		{
			if(professor.getNome().toLowerCase().startsWith(nome.toLowerCase()))
				matches.add(professor);
		}
		
		return matches;	
	}
	
	@Override
	public List<Professor> retrieveByScanning(String filter) 
	{
		List<Professor> match = new ArrayList<Professor>();
		
		if (filter==null || filter.isEmpty())
			return match;
		else
			filter = StringUtils.stripAccents(filter.toLowerCase());
		
		for (Professor professor : retrieveAll()) 
		{
			String nome = StringUtils.stripAccents(professor.getNome()).toLowerCase();
			String matr = StringUtils.stripAccents(professor.getMatricula()).toLowerCase();
			
			if(nome.contains(filter) || matr.startsWith(filter))
			{	
				match.add(professor);
			}
		}
		
		return match;
	}
	
	@Override
	public List<Professor> retrieveByProfessorFilter(ProfessorFilter filter)
	{
		String nome = filter.getNome();
		String matr = filter.getMatr();
		
		List<Curso> cursos = filter.getCursos();
		
		List<Professor> all = retrieveAll();
		List<Professor> match = new ArrayList<Professor>();
		
		for(Professor professor : all)
		{
			// Ignora professores fora dos cursos alvo da pesquisa
			if (cursos!=null && !cursos.isEmpty())
			{
				boolean inCoord = false;
				
				for (Curso cursoProf : professor.getCursos())
				{
					if (cursos.contains(cursoProf))
						inCoord=true;
				}
				
				if (!inCoord)
					continue;
			}	
						
			if(nome.isEmpty() || StringUtils.stripAccents(professor.getNome().toLowerCase()).startsWith((StringUtils.stripAccents(nome.toLowerCase()))))
			{
				if(matr.isEmpty() || StringUtils.stripAccents(professor.getMatricula().toLowerCase()).startsWith((StringUtils.stripAccents(matr.toLowerCase()))))
				{
					match.add(professor);
				}
			}
		}
		return match;
	}
}
