package br.eic.sca.modules.sie.dao.impl;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.TreeMap;
import java.util.TreeSet;

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
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import br.eic.sca.modules.sie.dao.DisciplinaDao;
import br.eic.sca.modules.sie.dao.VersaoCursoDao;
import br.eic.sca.modules.sie.domain.Curso;
import br.eic.sca.modules.sie.domain.Disciplina;
import br.eic.sca.modules.sie.domain.VersaoCurso;
import br.eic.sca.modules.sie.filters.DisciplinaFilter;
import br.eic.sca.modules.sie.filters.VersaoCursoFilter;

@Primary
@Component
public class DisciplinaDaoImpl extends _DaoSieAbstract<Disciplina> implements DisciplinaDao
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
	VersaoCursoDao versaoCursoDao;
	
	//
	// Atributos
	//
	private static HashMap<Integer,Disciplina> disciplinasIdMap = new HashMap<Integer,Disciplina>();
	private static HashMap<String,Disciplina> disciplinasCodMap = new HashMap<String,Disciplina>();
	
	//
	// Inicialização
	//
	@PostConstruct
	private void init() 
	{
		LOG.debug("----------------------");
		LOG.debug("Carregando Disciplinas");
		LOG.debug("----------------------");
		
		// Estrutura auxiliar para minimizar logging de erros
		TreeSet<String> loggedErrors = new TreeSet<String>();
		
		// Corrige o path se necessário
		if (!siefolderPath.endsWith("\\"))
			siefolderPath=siefolderPath+"\\";
		
		//
		// 11.02.01.99.0X - Currículos dos Cursos (IMPLEMENTAÇÃO REGULAR)
		//
		{
			// Metadados do carregamento
			int records = 0;
			long timestamp = System.currentTimeMillis();
			
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
						String codigo= "",nome= "",codCurso= "",verCurso= "";
						Integer creditos=null,ch=null,periodo=null;
						boolean optativa = false;
						
						Iterator<Cell> cells = rows.next().cellIterator();
						do  
						{
							Cell cell = cells.next();
							
							switch (headerMap.get(cell.getColumnIndex())) 
							{
								case "COD_DISCIPLINA":
									codigo = cell.getStringCellValue();
									break;
								case "NOME_DISCIPLINA":
									nome = cell.getStringCellValue();
									break;
								case "COD_CURSO":
									codCurso = cell.getStringCellValue();
									break;
								case "NUM_VERSAO":
									verCurso = cell.getCellTypeEnum() == CellType.NUMERIC ? String.valueOf((int)cell.getNumericCellValue()) : cell.getStringCellValue();
									break;
								case "CH_TOTAL":
									ch = (int)cell.getNumericCellValue();
									break;
								case "CREDITOS":
									creditos = (int)cell.getNumericCellValue();
									break;
								case "PERIODO_IDEAL":
									periodo = (int)cell.getNumericCellValue();
									break;
								case "TIPO_DISCIPLINA":
									optativa = cell.getStringCellValue().equals("Optativa");
									break;
							}
						}
						while (cells.hasNext());
							
						if (validate(codigo,nome,codCurso,verCurso) && !disciplinasCodMap.containsKey(codigo))
						{
							List<VersaoCurso> vCursos = versaoCursoDao.retrieveByVersaoCursoFilter(new VersaoCursoFilter("",codCurso,verCurso));
							
							if (vCursos==null || vCursos.size()==0)
							{
								if (!loggedErrors.contains(verCurso+"-"+codCurso))
								{
									LOG.warn("Não foi possível encontrar a versão "+verCurso+" do curso "+codCurso+". Verificar os DAO's de Curso e VersaoCurso.");
									loggedErrors.add(verCurso+"-"+codCurso);
								}
							}
							else
							{
								if (vCursos.size()==1)
								{
									VersaoCurso vCurso  = vCursos.get(0);
									Disciplina disciplina = new Disciplina(codigo, nome, creditos, ch, vCurso, periodo, optativa);
									disciplinasIdMap.put(disciplina.getId(),disciplina);
									disciplinasCodMap.put(disciplina.getCodigo(),disciplina);
									records++;
								}
								else
								{
									LOG.warn("Foram encontradas ambiguidades para a versão "+verCurso+" do curso "+codCurso+". Verificar os DAO's de Curso e VersaoCurso. Resultado -> "+vCursos);
								}
							}	
						}							
					}
					while (rows.hasNext());
					
					workbook.close();
					inputStream.close();
				} 			
				
				LOG.info("Disciplinas Encontradas - "+records+" ("+(System.currentTimeMillis()-timestamp)+"ms)");
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
		// 11.02.05.99.60 - Currículos dos Alunos por Curso (GAMBIARRA)
		// 11.02.04.99.18.02 - Solicitações de Matrícula (GAMBIARRA)
		//
//		{
//			// Metadados do carregamento
//			int records = 0;
//			long timestamp = System.currentTimeMillis();
//			
//			File dir1 = new File(siefolderPath + "11.02.05.99.60 - Currículos dos Alunos por Curso");
//			File arquivos1[] = dir1.listFiles();
//			File dir2 = new File(siefolderPath + "11.02.04.99.18.02 - Solicitações de Matrícula");
//			File arquivos2[] = dir2.listFiles();
//			
//			File arquivos[] = Stream.of(arquivos1, arquivos2).flatMap(Stream::of).toArray(File[]::new);
//			
//			// Verifica se há planilha(s)
//			if (arquivos==null || arquivos.length==0)
//				throw new IllegalStateException("Não foi possível encontrar planilhas na pasta "+dir1+" ou "+dir2+". Verifique o arquivo de configuração 'sca.properties' e reinicie o servidor.");
//			
//			try 
//			{	
//				for(File arquivo : arquivos) 
//				{
//					FileInputStream inputStream = new FileInputStream(arquivo);
//					Workbook 		workbook 	= new HSSFWorkbook(inputStream);
//					Sheet 			firstSheet 	= workbook.getSheetAt(0);
//					
//					Iterator<Row> rows = firstSheet.iterator();
//					
//					// Indexa as colunas pelo cabeçalho
//					TreeMap<Integer,String> headerMap = new TreeMap<Integer,String>();
//					Iterator<Cell> headerCells = rows.next().cellIterator();
//					do
//					{
//						Cell headerCell = headerCells.next();
//						headerMap.put(headerCell.getColumnIndex(), headerCell.getStringCellValue());
//					}
//					while (headerCells.hasNext());
//					
//					// Processo o conteúdo do arquivo
//					do
//					{
//						String codigo= "",nome= "",codCurso= "",verCurso= "",tipo = "";
//						Integer creditos=null,ch=null,periodo=null;
//						
//						Iterator<Cell> cells = rows.next().cellIterator();
//						
//						if (!cells.hasNext())
//							continue;
//						
//						do  
//						{
//							Cell cell = cells.next();
//							
//							switch (headerMap.get(cell.getColumnIndex())) 
//							{
//								case "COD_DISCIPLINA":
//									codigo = cell.getStringCellValue();
//									break;
//								case "NOME_DISCIPLINA":
//									nome = cell.getStringCellValue();
//									break;
//								case "COD_CURSO":
//									codCurso = cell.getStringCellValue();
//									break;
//								case "NUM_VERSAO":
//								case "VERSAO_CURSO_ALUNO":
//									verCurso = cell.getStringCellValue();
//									break;
//								case "CH_TOTAL": 
//									ch = (int)cell.getNumericCellValue();
//									break;
//								case "CREDITOS": 
//									creditos = (int)cell.getNumericCellValue();
//									break;
//								case "PERIODO_IDEAL":
//									periodo = (int)cell.getNumericCellValue();
//									break;								
//							}
//						}
//						while (cells.hasNext());
//							
//						if (validate(codigo,nome,codCurso,verCurso) && !disciplinasCodMap.containsKey(codigo))
//						{
//							if (verCurso.contains("."))
//								verCurso = verCurso.substring(0, 4);
//							if (verCurso.contains("-"))
//								verCurso = verCurso.substring(0, 4);
//							
//							List<VersaoCurso> vCursos = versaoCursoDao.retrieveByVersaoCursoFilter(new VersaoCursoFilter(codCurso,verCurso));
//							
//							if (vCursos==null || vCursos.size()==0)
//							{
//								if (!loggedErrors.contains(verCurso+"-"+codCurso+"-"+codigo))
//								{
//									LOG.warn("Não foi possível encontrar a versão "+verCurso+" do curso "+codCurso+" para a disciplina "+codigo+" - "+nome);
//									loggedErrors.add(verCurso+"-"+codCurso+"-"+codigo);
//								}
//							}
//							else
//							{
//								if (vCursos.size()==1)
//								{
//									VersaoCurso vCurso  = vCursos.get(0);
//									Disciplina disciplina = new Disciplina(codigo, nome, creditos, ch, vCurso, periodo, tipo);
//									disciplinasIdMap.put(disciplina.getId(),disciplina);
//									disciplinasCodMap.put(disciplina.getCodigo(),disciplina);
//									records++;
//								}
//								else
//								{
//									LOG.warn("Foram encontradas ambiguidades para a versão "+verCurso+" do curso "+codCurso+". Verificar os DAO's de Curso e VersaoCurso. Resultado -> "+vCursos);
//								}
//							}	
//						}
//						else
//						{
//							Disciplina disciplina = disciplinasCodMap.get(codigo);
//							
//							if (disciplina!=null)
//							{								
//								if (disciplina.getCargaHoraria()==null && ch!=null)
//								{
//									disciplina.setCargaHoraria(ch);
//								}
//								
//								if (disciplina.getCreditos()==null && creditos!=null)
//								{
//									disciplina.setCreditos(creditos);
//								}
//								
//								if (disciplina.getPeriodo()==null && periodo!=null)
//								{
//									disciplina.setPeriodo(periodo);
//								}			
//							}
//						}
//					}
//					while (rows.hasNext());
//					
//					workbook.close();
//					inputStream.close();
//				} 			
//				
//				LOG.info(records+" registros indiretamente inferidos com sucesso ("+(System.currentTimeMillis()-timestamp)+"ms)");
//			}
//			catch (IllegalStateException e) 
//			{
//				throw e;
//			}
//			catch (Exception e) 
//			{
//				throw new IllegalStateException("Não foi possível ler a(s) planilha(s) em "+dir1+" ou "+dir2+".",e);
//			}
//		}
	}
		
	//
	// Operações
	//
	@Override
	public Disciplina retrieveById(Integer id) 
	{
		return disciplinasIdMap.get(id);
	}
	
	@Override
	public Disciplina retrieveByCodigo(String codigo) 
	{
		return disciplinasCodMap.get(codigo);
	}

	@Override
	public List<Disciplina> retrieveAll() 
	{
		return new ArrayList<Disciplina>(disciplinasIdMap.values());
	}
	
	@Override
	public List<Disciplina> retrieveByScanning(String filter) 
	{
		List<Disciplina> match = new ArrayList<Disciplina>();
		
		if (filter==null || filter.isEmpty())
			return match;
		else
			filter = StringUtils.stripAccents(filter.toLowerCase());
		
		for (Disciplina disciplina : retrieveAll()) 
		{
			String nome = StringUtils.stripAccents(disciplina.getNome()).toLowerCase();
			String codigo = StringUtils.stripAccents(disciplina.getCodigo()).toLowerCase();
			
			if(nome.contains(filter) || codigo.startsWith(filter))
			{	
				match.add(disciplina);
			}
		}
		
		return match;
	}
	
	@Override
	public List<Disciplina> retrieveByDisciplinaFilter(DisciplinaFilter filter)
	{
		String nome 		  = filter.getNome();
		String nomeLowerCase  = nome.toLowerCase();
		String nomeSemAcento  = StringUtils.stripAccents(nomeLowerCase);
		Integer versaoCursoId = filter.getVersaoCurso()!=null?filter.getVersaoCurso().getId():null;	
		
		List<Curso> cursos = filter.getCursos();
		
		List<Disciplina> all = retrieveAll();
		List<Disciplina> match = new ArrayList<Disciplina>();
	
		for (Disciplina disciplina : all) 
		{
			// Ignora disciplinas fora dos cursos alvo da pesquisa
			if (cursos!=null && !cursos.isEmpty() && !cursos.contains(disciplina.getVersaoCurso().getCurso()))
				continue;
			
			String disciplinaNome = disciplina.getNome();
			String disciplinaLowerCase = disciplinaNome.toLowerCase();
			String disciplinaSemAcento = StringUtils.stripAccents(disciplinaLowerCase);
			
			if(nome.isEmpty() || disciplinaSemAcento.startsWith(nomeSemAcento))
			{
				if (versaoCursoId == null || disciplina.getVersaoCurso().getId().equals(versaoCursoId))
				{
					match.add(disciplina);
				}					
			}				
		}
		
		return match;
	}
}


