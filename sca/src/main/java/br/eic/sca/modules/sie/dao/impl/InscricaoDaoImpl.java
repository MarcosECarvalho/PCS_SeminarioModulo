package br.eic.sca.modules.sie.dao.impl;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
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

import br.eic.sca.modules.sie.dao.AlunoDao;
import br.eic.sca.modules.sie.dao.InscricaoDao;
import br.eic.sca.modules.sie.dao.TurmaDao;
import br.eic.sca.modules.sie.domain.Aluno;
import br.eic.sca.modules.sie.domain.Curso;
import br.eic.sca.modules.sie.domain.Inscricao;
import br.eic.sca.modules.sie.domain.Turma;
import br.eic.sca.modules.sie.filters.InscricaoFilter;
import br.eic.sca.modules.sie.filters.TurmaFilter;

@Primary
@Component
public class InscricaoDaoImpl extends _DaoSieAbstract<Inscricao> implements InscricaoDao
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
	AlunoDao alunoDao;
	@Autowired
	TurmaDao turmaDao;

	//
	// Atributos
	//
	private static HashMap<Integer, Inscricao> inscricaoIdMap = new HashMap<Integer, Inscricao>();
	
	//
	// Inicialização
	//
	@PostConstruct
	private void init() 
	{
		LOG.debug("---------------------");
		LOG.debug("Carregando Inscrições");
		LOG.debug("---------------------");
		
		// Metadados do carregamento
		int records = 0;
		long timestamp = System.currentTimeMillis();
		
		// Estrutura auxiliar para minimizar logging de erros
		TreeSet<String> loggedErrors = new TreeSet<String>();
		
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
					String matricula = "";
					String codTurma = "";
					String codDisc = "";
					int ano = 0, semestre = 0;
					String situacao = "";

					Iterator<Cell> cells = rows.next().cellIterator();
					
					if (!cells.hasNext())
						continue;
					
					do
					{
						Cell cell = cells.next();
						
						switch (headerMap.get(cell.getColumnIndex())) 
						{
							case "MATR_ALUNO":
								matricula = cell.getStringCellValue();
								break;
							case "COD_TURMA":
								codTurma = cell.getCellTypeEnum() == CellType.NUMERIC ? String.valueOf((int)cell.getNumericCellValue()) : cell.getStringCellValue();
								break;
							case "COD_DISCIPLINA":
								codDisc = cell.getStringCellValue();
								break;
							case "ANO":
								ano = (int)cell.getNumericCellValue();
								break;
							case "PERIODO":
								semestre = Integer.parseInt(cell.getStringCellValue().substring(0, 1));
								break;
							case "SITUACAO":
								situacao = cell.getStringCellValue();
								break;

						}
					}
					while (cells.hasNext());
					
					if (validate(matricula,codTurma))
					{
						List<Turma> turmas = turmaDao.retrieveByTurmaFilterExactCodigo(new TurmaFilter(codTurma,codDisc,"",ano,semestre,null));
						
						if (turmas==null || turmas.size()==0)
						{
							if (!loggedErrors.contains(codTurma+"-"+codDisc+"-"+ano+"-"+semestre))
							{
								LOG.warn("Não foi possível encontrar a turma "+codTurma+" ("+codDisc+") do periodo "+ano+"-"+semestre);
								loggedErrors.add(codTurma+"-"+codDisc+"-"+ano+"-"+semestre);
							}
						}
						else
						{
							if (turmas.size()==1)
							{
								Turma turma = turmas.get(0);
								Aluno aluno = alunoDao.retrieveByMatricula(matricula);
								
								if (aluno == null)
								{
									if (!loggedErrors.contains(matricula))
									{
										LOG.warn("Não foi possível encontrar o aluno "+matricula);
										loggedErrors.add(matricula);
									}																		
								}
								else
								{
									Inscricao inscricao = new Inscricao(aluno, turma, situacao);
									
									if (!inscricaoIdMap.containsKey(inscricao.getId()))
									{
										inscricaoIdMap.put(inscricao.getId(),inscricao);
										records++;
									}
								}
							}
							else
							{
								LOG.warn("Foram encontradas ambiguidades para turma "+codTurma+" do periodo "+ano+"-"+semestre+". Verificar o DAO de Turmas. Resultado -> "+turmas);
							}
						}
					}
				}
				while (rows.hasNext());
				
				workbook.close();
				inputStream.close();
			} 
			
			LOG.info("Inscrições Encontradas - "+records+" ("+(System.currentTimeMillis()-timestamp)+"ms)");
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
	public Inscricao retrieveById(Integer id) 
	{
		return inscricaoIdMap.get(id);
	}

	@Override
	public List<Inscricao> retrieveAll() 
	{
		return new ArrayList<Inscricao>(inscricaoIdMap.values());
	}

	@Override
	public List<Aluno> retrieveAlunosByTurma(Integer turmaId) 
	{
		Set<Aluno> alunos = new HashSet<Aluno>();
		
		for(Inscricao inscricao : inscricaoIdMap.values()) 
		{
			if(inscricao.getTurma().getId() == turmaId)
			{
				alunos.add(inscricao.getAluno());
			}
		}
		
		return new ArrayList<Aluno>(alunos);
	}
	
	@Override
	public List<Turma> retrieveTurmasByAluno(Aluno aluno) 
	{
		Set<Turma> turmas = new HashSet<Turma>();
		
		for(Inscricao inscricao : inscricaoIdMap.values()) 
		{
			if(inscricao.getAluno().getId() == aluno.getId())
			{
				turmas.add(inscricao.getTurma());
			}
		}
		
		return new ArrayList<Turma>(turmas);
	}
	
	public List<Inscricao> retrieveByAluno(Integer alunoId)
	{
		List<Inscricao> inscricoes = new ArrayList<Inscricao>();

		for(Inscricao inscricao : inscricaoIdMap.values()) 
		{
			if (inscricao.getAluno().getId() == alunoId)
				inscricoes.add(inscricao);
		}

		return inscricoes;
	}
	
	@Override
	public List<Inscricao> retrieveByScanning(String filter) 
	{
		List<Inscricao> match = new ArrayList<Inscricao>();
		
		if (filter==null || filter.isEmpty())
			return match;
		else
			filter = StringUtils.stripAccents(filter.toLowerCase());
		
		for (Inscricao inscricao : retrieveAll()) 
		{
			String nomeAluno = StringUtils.stripAccents(inscricao.getAluno().getNome()).toLowerCase();
			String matrAluno = StringUtils.stripAccents(inscricao.getAluno().getMatricula()).toLowerCase();
			String nomeDisc  = StringUtils.stripAccents(inscricao.getTurma().getDisciplina().getNome()).toLowerCase();
			String codigoTurma = StringUtils.stripAccents(inscricao.getTurma().getCodigo()).toLowerCase();
			String codigoDisc  = StringUtils.stripAccents(inscricao.getTurma().getDisciplina().getCodigo()).toLowerCase();
			
			if(nomeAluno.contains(filter) || matrAluno.startsWith(filter)  ||
			   nomeDisc.contains(filter)  || codigoDisc.startsWith(filter) || codigoTurma.startsWith(filter))
			{	
				match.add(inscricao);
			}
		}
		
		return match;
	}
	

	public List<Inscricao> retrieveByInscricaoFilter(InscricaoFilter filter)
	{
		String disciplina		= filter.getDisciplina();
		String codigoDisciplina = filter.getCodigoDisciplina();
		String codigoTurma		= filter.getCodigoTurma();
		String aluno			= filter.getAluno();
		String matricula		= filter.getMatricula();
		
		List<Curso> cursos = filter.getCursos();
		
		List<Inscricao> all = retrieveAll();
		List<Inscricao> match = new  ArrayList<Inscricao>();
	
		for (Inscricao inscricao : all) 
		{
			// Ignora inscrições fora dos cursos alvo da pesquisa
			if (cursos!=null && !cursos.isEmpty() && !cursos.contains(inscricao.getTurma().getDisciplina().getVersaoCurso().getCurso()))
				continue;
									
			if(disciplina.isEmpty() || StringUtils.stripAccents(inscricao.getTurma().getDisciplina().getNome().toLowerCase()).startsWith(StringUtils.stripAccents(disciplina.toLowerCase())))
			{
				if(codigoDisciplina.isEmpty() || inscricao.getTurma().getDisciplina().getCodigo().toLowerCase().startsWith(codigoDisciplina.toLowerCase()))
				{
					if(codigoTurma.isEmpty() || StringUtils.stripAccents(inscricao.getTurma().getCodigo().toLowerCase()).startsWith(StringUtils.stripAccents(codigoTurma.toLowerCase())))
					{
						if (aluno.isEmpty() || StringUtils.stripAccents(inscricao.getAluno().getNome().toLowerCase()).startsWith(StringUtils.stripAccents(aluno.toLowerCase())))
						{
							if(matricula.isEmpty() || StringUtils.stripAccents(inscricao.getAluno().getMatricula().toLowerCase()).startsWith(StringUtils.stripAccents(matricula.toLowerCase())))
							{
								match.add(inscricao);
							}
						}
					}
				}
			}				
		}
		
		return match;
	}
}
