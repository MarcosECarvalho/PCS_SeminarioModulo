package br.eic.sca.modules.sie.service;

import br.eic.sca.modules.sie.dao._DaoSie;

/**
 * @author Rafael Castaneda (rafael.ribeiro@cefet-rj.br)
 * <p>
 * Interface para definição de serviços baseados em leitura de 
 * arquivos excel. Deve ser utilizada para uniformizar a 
 * interface dos serviços de recuperação de dados acadêmicos 
 * contidos nos relatórios em XLS do sistema SIE.
 * <p>
 * Oferece apenas operações de leitura.
 * <p>
 * @param <ENTITY_TYPE> Tipo da classe de Domínio gerenciada pelo Serviço
 */ 
public interface _ServiceSie<ENTITY_TYPE> extends _DaoSie<ENTITY_TYPE>
{

}
