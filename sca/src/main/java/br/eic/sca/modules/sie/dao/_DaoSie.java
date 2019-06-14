package br.eic.sca.modules.sie.dao;

import java.util.List;
 
/**
 * @author Rafael Castaneda (rafael.ribeiro@cefet-rj.br)
 * <p>
 * Interface para definição de DAO's baseados em leitura de 
 * arquivos excel. Deve ser utilizada para uniformizar a 
 * interface dos DAO's de recuperação de dados acadêmicos 
 * contidos nos relatórios em XLS do sistema SIE.
 * <p>
 * Oferece apenas operações de leitura.
 * <p>
 * @param <ENTITY_TYPE> Tipo da classe de Domínio gerenciada pelo DAO
 */
public interface _DaoSie<ENTITY_TYPE> 
{       
	/**
	 * Recupera um objeto pelo seu Id.
	 * <p>
	 * @param id Id do objeto 
	 * @return Objeto recuperado pelo Id ou NULL caso o ID não exista
	 */ 
	public ENTITY_TYPE retrieveById(Integer id);
	
	/**
	 * Recupera todos os objetos do tipo gerenciado pelo DAO
	 * <p>
	 * @return Lista com todos os objetos disponíveis
	 */        
	public List<ENTITY_TYPE> retrieveAll();
	
	/**
	 * Recupera todos os objetos do tipo gerenciado pelo DAO
	 * em que os campos de interesse atendam ao filtro fornecido.
	 * Nesta modalidade de busca um único argumento é utilizado 
	 * para buscar em múltiplos campos, este método pode ser utilizado
	 * para implementação de operações de "auto-complete" ou para
	 * buscas inteligentes. 
	 * 
	 * Deve-se tomar o cuidado de não invocar este método com filtros
	 * muito abrangentes, como strings com um ou dois caracterers, para
	 * não sobrecarregar a busca.
	 * 
	 * No caso do filtro ser nulo ou uma string vazia, uma coleção vazia
	 * será retornada.
	 * <p>
	 * @return Lista com todos os objetos disponíveis
	 */        
	public List<ENTITY_TYPE> retrieveByScanning(String filter);
}
