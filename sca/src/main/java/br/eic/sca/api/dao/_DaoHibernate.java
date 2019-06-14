package br.eic.sca.api.dao;

import java.util.List;

import org.hibernate.criterion.MatchMode;

import br.eic.sca.api.dao.query.DaoLike;

/**
 * @author Rafael Castaneda (rafael.ribeiro@cefet-rj.br)
 * <p>
 * Interface para definição de DAO's baseados em Hibernate/JPA.
 * Utilizada para uniformizar os DAO's de Banco de Dados do 
 * sistema SCA. 
 * <p>
 * Pré-define todas as operações de CRUD.  
 * <p>
 * @param <ENTITY_TYPE> Tipo da classe de Domínio gerenciada pelo DAO
 */
public interface _DaoHibernate<ENTITY_TYPE>
{
	/**
	 * Força a aplicação de quaisquer modificações pendentes na transação atual.
	 */
	public void flushSession();
	
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
	 * Busca todos os objetos do tipo gerenciado pelo DAO que correspondam
	 * ao <em>'like'</em> de um campo específico. O modo como o <em>'like'</em> é 
	 * realizado depende do argumento MatchMode, dentro das seguintes possibilidades:
	 * <p>
	 * <ul>
	 * <li><b>MatchMode.EXACT</b> - Testa a correspondência exata ao termo de busca
	 * <li><b>MatchMode.START</b> - Testa correspondências que comecem como o termo de busca
	 * <li><b>MatchMode.END</b> - Testa correspondências que terminem como o termo de busca
	 * <li><b>MatchMode.ANYWHERE</b> - Testa correspondências que contenham o termo de busca
	 * </ul>
	 * <p>
	 * Exemplo de utilização:
	 * <p>
	 * retrieveByLikeInSingleField("CAMPO","termoDeBusca",MatchMode.START);
	 * <p>
	 * @param field Nome do campo do objeto a ser filtrado 
	 * @param value Valor de critério da filtragem 
	 * @param matchMode Modo de filtragem 
	 * <p>
	 * @return Lista com todos os objetos encontrados
	 */
	public List<ENTITY_TYPE> retrieveByLikeInSingleField(String field, String value, MatchMode matchMode);

	/**
	 * Busca todos os objetos do tipo gerenciado pelo DAO que correspondam
	 * a um conjunto de <em>'likes'</em>. Funciona como o método {@link #retrieveByLikeInSingleField(String field, String value, MatchMode matchMode) retrieveByLikeInSingleField},
	 * mas recebe como argumento um ou mais objetos do tipo {@link br.eic.sca.api.dao.query.DaoLike DaoLike},
	 * cada um encapsulando um critério adicional de <em>'like'</em>.
	 * <p>
	 * Exemplo de utilização: 
	 * <p>
	 * retrieveByLikeInManyFields(new DaoLike("CAMPO_1", "termoDeBusca1", MatchMode.EXACT), new DaoLike("CAMPO_2", "termoDeBusca2", MatchMode.START));  
	 * <p>
	 * @return Lista com todos os objetos encontrados
	 */
	public List<ENTITY_TYPE> retrieveByLikeInManyFields(DaoLike... daoLikes);
	
	/**
     * Persiste um objeto. Caso o objeto não exista um novo registro é inserido 
     * no banco de dados. Caso o objeto já exista suas inforamções são atualizadas.
     * A diferenciação entre objetos novos ou pré-existentes é feita consultando-se
     * a o valor do atributo de identificação do objeto.  
     * <p>
     * @param object O objeto a ser persitido
     */	    
    public void persist(ENTITY_TYPE object);     	    	    
	
    /**
     * Remove um objeto. O ID do objeto deve estar preenchido para que a operação 
     * funcione corretamente.
     * <p>
     * @param object O objeto a ser removido
     */		    
	public void delete(ENTITY_TYPE object);	
}
