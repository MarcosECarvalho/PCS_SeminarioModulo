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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import br.eic.sca.modules.sie.dao.AlunoDao;
import br.eic.sca.modules.sie.domain.Aluno;
import br.eic.sca.modules.sie.domain.Curso;
import br.eic.sca.modules.sie.domain.VersaoCurso;
import br.eic.sca.modules.sie.filters.AlunoFilter;
import br.eic.sca.modules.sie.filters.VersaoCursoFilter;

@Primary
@Component
public class AlunoDaoImpl extends _DaoSieAbstract<Aluno> implements AlunoDao
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
	VersaoCursoDaoImpl versaoCursoDao;
	
	//
	// Atributos
	//
	private static HashMap<Integer,Aluno> alunosIdMap = new HashMap<Integer,Aluno>();
	private static TreeMap<String,Aluno> alunosMatrMap = new TreeMap<String,Aluno>();
	
	//
	// Inicialização
	//	
	@PostConstruct
	private void init() 
	{
		LOG.debug("-----------------");
		LOG.debug("Carregando Alunos");
		LOG.debug("-----------------");
		
		// Metadados do carregamento
		int records = 0;
		long timestamp = System.currentTimeMillis();
		
		// Corrige o path se necessário
		if (!siefolderPath.endsWith("\\"))
			siefolderPath=siefolderPath+"\\";		
		
		File dir = new File(siefolderPath + "11.02.05.99.60 - Currículos dos Alunos por Curso");
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
					String nome = "";
					String cpf = "";
					String matricula = "";
					String codCurso = "";
					String versaoCurso = "";
					
					Iterator<Cell> cells = rows.next().cellIterator();
					
					if (!cells.hasNext())
						continue;
					
					do
					{
						Cell cell = cells.next();
						
						switch (headerMap.get(cell.getColumnIndex())) 
						{
							case "COD_CURSO":  
								codCurso = cell.getStringCellValue();
								break;
							case "VERSAO_CURSO":
							case "VERSAO_CURSO_ALUNO": 
								versaoCurso = cell.getStringCellValue();
								break;
							case "CPF": 
								cpf = cell.getStringCellValue();
								break;
							case "MATR_ALUNO": 
								matricula = cell.getStringCellValue();
								break;
							case "NOME_PESSOA":
								nome = cell.getStringCellValue().toUpperCase();
								break;
						}						
					}
					while (cells.hasNext());					

					if (validate(codCurso,versaoCurso,matricula,nome) && !alunosMatrMap.containsKey(matricula))
					{
						List<VersaoCurso> vCursos = versaoCursoDao.retrieveByVersaoCursoFilter(new VersaoCursoFilter("", codCurso,versaoCurso));
						
						if (vCursos==null || vCursos.size()==0)
						{
							LOG.warn("Não foi possível encontrar a versão "+versaoCurso+" do curso "+codCurso+". Verificar os DAO's de Curso e VersaoCurso.");													
						}
						else
						{
							if (vCursos.size()==1)
							{
								VersaoCurso vCurso  = vCursos.get(0);
								Aluno aluno = new Aluno(nome, cpf, matricula, vCurso);
								alunosIdMap.put(aluno.getId(),aluno);
								alunosMatrMap.put(aluno.getMatricula(),aluno);
								records++;
							}
							else
							{
								LOG.warn("Foram encontradas ambiguidades para a versão "+versaoCurso+" do curso "+codCurso+". Verificar os DAO's de Curso e VersaoCurso. Resultado -> "+vCursos);
							}
						}												
					}
				}
				while (rows.hasNext());
				
				workbook.close();
				inputStream.close();
								
				LOG.info("Alunos Encontrados - "+records+" ("+(System.currentTimeMillis()-timestamp)+"ms)");
			}
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
	public List<Aluno> retrieveAll()
	{
		return new ArrayList<Aluno>(alunosIdMap.values());
	}
	
	@Override
	public Aluno retrieveById(Integer id) 
	{
		return alunosIdMap.get(id);
	}
	
	@Override
	public Aluno retrieveByMatricula(String matricula)
	{
		return alunosMatrMap.get(matricula);
	}
	
	@Override
	public List<Aluno> retrieveByNome(String nome) 
	{
		List<Aluno> all = retrieveAll();
		List<Aluno> match = new ArrayList<Aluno>();
		
		for (Aluno aluno : all) 
		{
			if(StringUtils.stripAccents(aluno.getNome().toLowerCase()).startsWith((StringUtils.stripAccents(nome.toLowerCase()))))
				match.add(aluno);
		}
		
		return match;
	}
	
	@Override
	public List<Aluno> retrieveByScanning(String filter) 
	{
		List<Aluno> match = new ArrayList<Aluno>();
		
		if (filter==null || filter.isEmpty())
			return match;
		else
			filter = StringUtils.stripAccents(filter.toLowerCase());
		
		for (Aluno aluno : retrieveAll()) 
		{
			String 	nome = StringUtils.stripAccents(aluno.getNome()).toLowerCase();
			String 	matr = StringUtils.stripAccents(aluno.getMatricula()).toLowerCase();
			String 	cpf  = StringUtils.stripAccents(aluno.getCpf()).toLowerCase();
			
			if(nome.contains(filter) || matr.startsWith(filter) || cpf.startsWith(filter))
			{	
				match.add(aluno);
			}
		}
		
		return match;
	}
			
	@Override
	public List<Aluno> retrieveByAlunoFilter(AlunoFilter filter)
	{
		String 	nome 		  = filter.getNome();
		String 	matr 		  = filter.getMatr();
		String 	cpf  		  = filter.getCpf();
		Integer versaoCursoId = filter.getVersaoCurso()!=null?filter.getVersaoCurso().getId():null;	
		
		List<Curso> cursos = filter.getCursos();
		
		List<Aluno> all = retrieveAll();
		List<Aluno> match = new ArrayList<Aluno>();
	
		for (Aluno aluno : all) 
		{
			// Ignora alunos fora dos cursos alvo da pesquisa
			if (cursos!=null && !cursos.isEmpty() && !cursos.contains(aluno.getVersaoCurso().getCurso()))
				continue;
			
			if(nome.isEmpty() || StringUtils.stripAccents(aluno.getNome().toLowerCase()).startsWith((StringUtils.stripAccents(nome.toLowerCase()))))
			{
				if(matr.isEmpty() || StringUtils.stripAccents(aluno.getMatricula().toLowerCase()).startsWith((StringUtils.stripAccents(matr.toLowerCase()))))
				{
					if(cpf.isEmpty() || aluno.getCpf().startsWith(cpf))
					{
						if (versaoCursoId == null || aluno.getVersaoCurso().getId().equals(versaoCursoId))
						{
							match.add(aluno);
						}
					}
				}
			}				
		}
		
		return match;
	}
}
