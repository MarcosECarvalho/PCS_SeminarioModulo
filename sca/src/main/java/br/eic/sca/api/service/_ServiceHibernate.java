package br.eic.sca.api.service;

import br.eic.sca.api.dao._DaoHibernate;

/**
 * @author Rafael Castaneda (rafael.ribeiro@cefet-rj.br)
 * <p>
 * Interface para definição de Serviços baseados em Hibernate/JPA.
 * Utilizada para uniformizar os Serviços de Banco de Dados do 
 * sistema SCA. 
 * <p>
 * Pré-define todas as operações de CRUD.  
 * <p>
 * @param <ENTITY_TYPE> Tipo da classe de Domínio gerenciada pelo Serviço
 */
public interface _ServiceHibernate<ENTITY_TYPE> extends _DaoHibernate<ENTITY_TYPE>
{

}
