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
import org.springframework.stereotype.Component;

import br.eic.sca.modules.sie.dao.DisciplinaDao;
import br.eic.sca.modules.sie.dao.ProfessorDao;
import br.eic.sca.modules.sie.dao.TurmaDao;
import br.eic.sca.modules.sie.domain.Curso;
import br.eic.sca.modules.sie.domain.Disciplina;
import br.eic.sca.modules.sie.domain.Professor;
import br.eic.sca.modules.sie.domain.Turma;
import br.eic.sca.modules.sie.filters.TurmaFilter;

@Component
public class TurmaDaoImpl extends _DaoSieAbstract<Turma> implements TurmaDao
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
	DisciplinaDao disciplinaDao;
	@Autowired
	ProfessorDao professorDao;
	
	//
	// Atributos
	//
	private static HashMap<Integer,Turma> turmasIdMap = new HashMap<Integer,Turma>();
			
	//
	// Inicialização
	//
	@PostConstruct
	private void init()
	{
		LOG.debug("-----------------");
		LOG.debug("Carregando Turmas");
		LOG.debug("-----------------");
		
		// Estrutura auxiliar para minimizar logging de erros
		TreeSet<String> loggedErrors = new TreeSet<String>();
		
		// Corrige o path se necessário
		if (!siefolderPath.endsWith("\\"))
			siefolderPath=siefolderPath+"\\";
		
		//
		// 11.02.03.99.19 - Ofertas de Disciplinas (IMPLEMENTAÇÃO REGULAR)
		//
		{
			// Metadados do carregamento
			int records = 0;
			long timestamp = System.currentTimeMillis();
			
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
						String codDisciplina = "", nomeDisciplina = "", codigo = "", curso = "", matriculaProf = "";
						int ano = 0, semestre = 0, idTurma = 0;
						
						Iterator<Cell> cells = rows.next().cellIterator();
						do  
						{
							Cell cell = cells.next();
							
							switch (headerMap.get(cell.getColumnIndex())) 
							{
								case "COD_DISCIPLINA":
									codDisciplina = cell.getStringCellValue();
									break;
								case "NOME_DISCIPLINA":
									nomeDisciplina = cell.getStringCellValue();
									break;
								case "COD_TURMA":
									codigo = cell.getCellTypeEnum() == CellType.NUMERIC ? String.valueOf((int)cell.getNumericCellValue()) : cell.getStringCellValue();
									break;
								case "COD_CURSO":
									curso = cell.getStringCellValue();
									break;
								case "ANO":
									ano = (int)cell.getNumericCellValue();
									break;
								case "PERIODO":
									semestre = Integer.parseInt(cell.getStringCellValue().substring(0, 1));
									break;
								case "ID_TURMA":
									idTurma = (int)cell.getNumericCellValue();
									break;
								case "MATR_EXTERNA":
									matriculaProf = String.valueOf(((int)cell.getNumericCellValue()));
									break;
							}												
						}
						while (cells.hasNext());
						
						if (validate(codigo,codDisciplina))
						{
							Disciplina disc = disciplinaDao.retrieveByCodigo(codDisciplina);
							if (disc != null) 
							{
								Professor prof = professorDao.retrieveByMatricula(matriculaProf);
								if (prof!=null)
								{
									Turma turma = new Turma(idTurma, codigo, ano, semestre, disc, prof);																		
									
									if (!turmasIdMap.containsKey(turma.getId()))
									{
										turmasIdMap.put(turma.getId(),turma);
										records++;
									}
								}
								else
								{
									Turma turma = new Turma(idTurma, codigo, ano, semestre, disc, null);
									
									if (!turmasIdMap.containsKey(turma.getId()))
									{
										turmasIdMap.put(turma.getId(),turma);
										records++;
									}
									
									if (!loggedErrors.contains(matriculaProf+"-"+idTurma))
									{
										LOG.debug("Não foi possível encontrar o professor ["+matriculaProf+"] para a turma "+turma);
										loggedErrors.add(matriculaProf+"-"+idTurma);
									}
								}
							}
							else
							{
								if (!loggedErrors.contains(curso+"-"+codDisciplina))
								{
									LOG.warn("Não foi possível encontrar a disciplina "+curso+" - "+codDisciplina+" - "+nomeDisciplina);
									loggedErrors.add(curso+"-"+codDisciplina);
								}
							}
						}					
					}
					while (rows.hasNext());
					
					workbook.close();
					inputStream.close();
				}
				
				LOG.info("Turmas Encontradas - "+records+" ("+(System.currentTimeMillis()-timestamp)+"ms)");
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
//						String codDisciplina = "", nomeDisciplina = "", codigo = "", curso = "";
//						int ano = 0, semestre = 0, idTurma = 0;
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
//									codDisciplina = cell.getStringCellValue();
//									break;
//								case "NOME_DISCIPLINA":
//									nomeDisciplina = cell.getStringCellValue();
//									break;
//								case "COD_TURMA":
//									codigo = cell.getStringCellValue();
//									break;
//								case "COD_CURSO":
//									curso = cell.getStringCellValue();
//									break;
//								case "ANO":
//									ano = (int)cell.getNumericCellValue();
//									break;
//								case "PERIODO":
//									semestre = Integer.parseInt(cell.getStringCellValue().substring(0, 1));
//									break;
//							}												
//						}
//						while (cells.hasNext());
//						
//						if (validate(codigo,codDisciplina))
//						{
//							Disciplina disc = disciplinaDao.retrieveByCodigo(codDisciplina);
//							if (disc != null) 
//							{
//								Turma turma = new Turma(idTurma, codigo, ano, semestre, disc, null);
//								
//								if (!turmasIdMap.containsKey(turma.getId()))
//								{
//									turmasIdMap.put(turma.getId(),turma);
//									records++;
//								}
//							}
//							else
//							{
//								if (!loggedErrors.contains(curso+"-"+codDisciplina))
//								{
//									LOG.warn("Não foi possível encontrar a disciplina "+curso+" - "+codDisciplina+" - "+nomeDisciplina);
//									loggedErrors.add(curso+"-"+codDisciplina);
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
	public Turma retrieveById(Integer id) 
	{		
		return turmasIdMap.get(id);
	}
	
	@Override
	public List<Turma> retrieveAll() 
	{
		return new ArrayList<Turma>(turmasIdMap.values());
	}
	
	@Override
	public List<Turma> retrieveByScanning(String filter) 
	{
		List<Turma> match = new ArrayList<Turma>();
		
		if (filter==null || filter.isEmpty())
			return match;
		else
			filter = StringUtils.stripAccents(filter.toLowerCase());
		
		for (Turma turma : retrieveAll()) 
		{
			String nome = StringUtils.stripAccents(turma.getDisciplina().getNome()).toLowerCase();
			String codigoTurma = StringUtils.stripAccents(turma.getCodigo()).toLowerCase();
			String codigoDisc  = StringUtils.stripAccents(turma.getDisciplina().getCodigo()).toLowerCase();
			
			if(nome.contains(filter) || codigoTurma.startsWith(filter) || codigoDisc.startsWith(filter))
			{	
				match.add(turma);
			}
		}
		
		return match;
	}
	
	@Override
	public List<Turma> retrieveByTurmaFilter(TurmaFilter filter) 
	{				
		String codDisc = filter.getCodDisc();
		String nome = filter.getNomeDisc();
		String turm = filter.getCodTurma();		
		Integer semestre = filter.getSemestre();
		Integer ano = filter.getAno();
		Integer versaoCursoId = filter.getVersaoCurso()!=null?filter.getVersaoCurso().getId():null;
		
		Professor professor = filter.getProfessor();
		
		List<Curso> cursos = filter.getCursos();
		
		List<Turma> all = retrieveAll();
		List<Turma> match = new ArrayList<Turma>();				
		
		for(Turma turma : all)
		{
			// Ignora disciplinas não ministradas pelo professor alvo da pesquisa
			if (professor!=null && !professor.equals(turma.getProfessor()))
				continue;
			
			// Ignora disciplinas fora dos cursos alvo da pesquisa
			if (cursos!=null && !cursos.isEmpty() && !cursos.contains(turma.getDisciplina().getVersaoCurso().getCurso()))
				continue;
			
			if(nome.isEmpty() || StringUtils.stripAccents(turma.getDisciplina().getNome().toLowerCase()).startsWith((StringUtils.stripAccents(nome.toLowerCase()))))
			{
				if(turm.isEmpty() || StringUtils.stripAccents(turma.getCodigo().toLowerCase()).startsWith((StringUtils.stripAccents(turm.toLowerCase()))))
				{
					if(codDisc.isEmpty() || StringUtils.stripAccents(turma.getDisciplina().getCodigo().toLowerCase()).startsWith((StringUtils.stripAccents(codDisc.toLowerCase()))))
					{
						if(semestre==null || semestre == turma.getSemestre())
						{
							if(ano==null || ano == turma.getAno())
							{
								if (versaoCursoId == null || turma.getDisciplina().getVersaoCurso().getId().equals(versaoCursoId))
								{
									match.add(turma);
								}									
							}
						}
					}
				}
			}
		}
		return match;
	}
	
	@Override
	public List<Turma> retrieveByTurmaFilterExactCodigo(TurmaFilter filter) 
	{				
		String codDisc = filter.getCodDisc();
		String nome = filter.getNomeDisc();
		String turm = filter.getCodTurma();
		Integer semestre = filter.getSemestre();
		Integer ano = filter.getAno();
		Integer versaoCursoId = filter.getVersaoCurso()!=null?filter.getVersaoCurso().getId():null;
		Professor professor = filter.getProfessor();
		
		List<Curso> cursos = filter.getCursos();
		
		List<Turma> all = retrieveAll();
		List<Turma> match = new ArrayList<Turma>();				
		
		for(Turma turma : all)
		{
			// Ignora disciplinas fora dos cursos alvo da pesquisa
			if (cursos!=null && !cursos.contains(turma.getDisciplina().getVersaoCurso().getCurso()))
				continue;
						
			if(nome.isEmpty() || StringUtils.stripAccents(turma.getDisciplina().getNome().toLowerCase()).startsWith((StringUtils.stripAccents(nome.toLowerCase()))))
			{
				if(turm.isEmpty() || StringUtils.stripAccents(turma.getCodigo().toLowerCase()).equals((StringUtils.stripAccents(turm.toLowerCase()))))
				{
					if(codDisc.isEmpty() || StringUtils.stripAccents(turma.getDisciplina().getCodigo().toLowerCase()).equals((StringUtils.stripAccents(codDisc.toLowerCase()))))
					{
						if(semestre==null || semestre == turma.getSemestre())
						{
							if(ano==null || ano == turma.getAno())
							{
								if (versaoCursoId == null || turma.getDisciplina().getVersaoCurso().getId().equals(versaoCursoId))
								{
									if (professor == null || turma.getProfessor().equals(professor))
									{
										match.add(turma);
									}										
								}									
							}
						}
					}
				}
			}
		}
		return match;
	}

	@Override
	public List<Turma> retrieveByProfessor(Professor professor) 
	{
		List<Turma> all = retrieveAll();
		List<Turma> match = new ArrayList<Turma>();				
		
		for(Turma turma : all)
		{
			if (professor == null || turma.getProfessor().equals(professor))
			{
				match.add(turma);
			}	
		}
		
		return match;
	}
}
