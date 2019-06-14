package br.eic.sca.modules.sie.aspects;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Set;
import java.util.TreeSet;

import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import br.eic.sca.modules.sie.annotations.SieOrm;
import br.eic.sca.modules.sie.dao._DaoSie;

@Component
@Aspect
public class SieOrmAspect 
{
	@Autowired 
	private ApplicationContext appCtx;
	
	@Pointcut("within(br.eic.sca.api.dao._DaoHibernate+)")
	public void isDaoMethod() {}
	
	@Pointcut("execution(public * *(..))")
	public void isPublicMethod() {}
	
	@Pointcut("execution(public * persist(..))")
	public void isPersistMethod() {}
	
	@Pointcut("isDaoMethod() && isPublicMethod() && !isPersistMethod()")
	public void isDaoRetrieveMethod() {}
	
	@Pointcut("isDaoMethod() && isPublicMethod() && isPersistMethod()")
	public void isDaoPersistMethod() {}
	
	@Before("isDaoPersistMethod()")
	public void advicePersist(JoinPoint joinPoint)
	{
		// Recupera objeto que está sendo persistido
		Object object = joinPoint.getArgs()[0];
		
		// Se existe objeto
		if (object!=null)
		{
			// Processa o objeto atribuindo Id's do Sie em função de objetos do Sie 
			proccessObjectToId(object,new TreeSet<Integer>());
		}
	}
	
	@AfterReturning(pointcut="isDaoRetrieveMethod()", returning="returnValue")
    public void adviceRetrieve(Object returnValue) 
	{
		if (returnValue==null)
			return;
		
		// Se for uma coleção de objetos sendo recuperados
        if (returnValue instanceof Collection<?>)
        {
        	Collection<?> values = (Collection<?>)returnValue;
        	TreeSet<Integer> history = new TreeSet<Integer>();
        	
        	// Para cada objeto
        	for (Object object : values) 
        	{
    			// Processa o objeto atribuindo objetos do Sie em função dos Id's do Sie        		
        		proccessIdToObject(object,history);
			}
        }
        // Se for apenas um objeto simples sendo recuperado
        else
        {
        	// Processa o objeto atribuindo objetos do Sie em função dos Id's do Sie
        	proccessIdToObject(returnValue,new TreeSet<Integer>());
        }
    }
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void proccessObjectToId(Object object,Set<Integer> history)
	{
		if (object==null)
			return;
		
		// Verifica se o objeto já foi processado
		int objHash = System.identityHashCode(object);
		if (history.contains(objHash))
			return;
		else
			history.add(objHash);
		
		try
		{
			// Recupera todos os atributos do objeto
			Class clazz = object.getClass();
			Field[] fields = clazz.getDeclaredFields();
			
			// Para cada campo do objeto
			for (Field field : fields) 
			{
				// Se o campo possuir a anotação MANY-TO-ONE
				if (field.isAnnotationPresent(ManyToOne.class))
				{
					// Tenta recuperar o valor do campo invocando seu método get
					String fieldName = field.getName();
					Method fieldGetMethod = clazz.getMethod("get"+fieldName.substring(0, 1).toUpperCase()+fieldName.substring(1));
					Object fieldValue = fieldGetMethod.invoke(object);
					
					// Processa também o objeto referenciado
					proccessObjectToId(fieldValue,history);
				}
				
				// Se o campo possuir a anotação ONE-TO-MANY
				if (field.isAnnotationPresent(OneToMany.class))
				{
					// Tenta recuperar o valor do campo invocando seu método get
					String fieldName = field.getName();
					Method fieldGetMethod = clazz.getMethod("get"+fieldName.substring(0, 1).toUpperCase()+fieldName.substring(1));
					Object fieldValue = fieldGetMethod.invoke(object);
					
					// Verifica se o campo é de fato uma coleção
					if (fieldValue instanceof Collection<?>)
					{
						// Itera sobre a coleção e processa todos os valores
						Collection<?> fieldsValues = (Collection<?>)fieldValue; 
						
						for (Object fieldValueIter : fieldsValues) 
						{
							// Processa também o objeto referenciado
							proccessObjectToId(fieldValueIter,history);
						}						
					}
				}
				
				// Se o campo possuir a anotação SIE
				if(field.isAnnotationPresent(SieOrm.class))
				{
					// Tenta recuperar o valor do campo invocando seu método get
					String fieldName = field.getName();
					Method fieldGetMethod = clazz.getMethod("get"+fieldName.substring(0, 1).toUpperCase()+fieldName.substring(1));
					Object fieldValue = fieldGetMethod.invoke(object);
					
					// Se o campo possuir valor
					if (fieldValue != null)
					{
						// Recupera a classe do campo anotado
						Class fieldClazz = fieldValue.getClass();
						
						// Recupera o id do campo anotado pelo método getID 
						Method fieldGetIdMethod = fieldClazz.getMethod("getId");
						Integer id = (Integer) fieldGetIdMethod.invoke(fieldValue);
						
						// Seta o id no campo chave indicado pelo método set correspondente
						String  idFieldName = field.getAnnotation(SieOrm.class).id();
						Method  idFieldSetMethod = clazz.getMethod("set"+idFieldName.substring(0, 1).toUpperCase()+idFieldName.substring(1),Integer.class);
						idFieldSetMethod.invoke(object, id);						
					}
				}
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}		
	}
	
	@SuppressWarnings({"rawtypes", "unchecked"})
	private void proccessIdToObject(Object object,Set<Integer> history)
	{
		if (object==null)
			return;
		
		// Verifica se o objeto já foi processado
		int objHash = System.identityHashCode(object);
		if (history.contains(objHash))
			return;
		else
			history.add(objHash);
		
		try
		{
			// Recupera todos os atributos do objeto
			Class clazz = object.getClass();
			Field[] fields = clazz.getDeclaredFields();
			
			// Para cada campo do objeto
			for (Field field : fields) 
			{
				// Se o campo possuir a anotação MANY-TO-ONE
				if (field.isAnnotationPresent(ManyToOne.class))
				{
					// Tenta recuperar o valor do campo invocando seu método get
					String fieldName = field.getName();
					Method fieldGetMethod = clazz.getMethod("get"+fieldName.substring(0, 1).toUpperCase()+fieldName.substring(1));
					Object fieldValue = fieldGetMethod.invoke(object);
					
					// Processa também o objeto referenciado
					proccessIdToObject(fieldValue,history);
				}
				
				// Se o campo possuir a anotação ONE-TO-MANY
				if (field.isAnnotationPresent(OneToMany.class))
				{
					// Tenta recuperar o valor do campo invocando seu método get
					String fieldName = field.getName();
					Method fieldGetMethod = clazz.getMethod("get"+fieldName.substring(0, 1).toUpperCase()+fieldName.substring(1));
					Object fieldValue = fieldGetMethod.invoke(object);
					
					// Verifica se o campo é de fato uma coleção
					if (fieldValue instanceof Collection<?>)
					{
						// Itera sobre a coleção e processa todos os valores
						Collection<?> fieldsValues = (Collection<?>)fieldValue; 
						
						for (Object fieldValueIter : fieldsValues) 
						{
							// Processa também o objeto referenciado
							proccessIdToObject(fieldValueIter,history);
						}						
					}
				}
				
				// Se o campo possuir a anotação SIE
				if(field.isAnnotationPresent(SieOrm.class))
				{
					// Tenta recuperar o valor do campo invocando seu método get
					String fieldName = field.getName();
					Method fieldGetMethod = clazz.getMethod("get"+fieldName.substring(0, 1).toUpperCase()+fieldName.substring(1));
					Object fieldValue = fieldGetMethod.invoke(object);
					
					// Se o valor do campo estiver nulo
					if (fieldValue == null)
					{
						// Tenta recuperar o id indicado pela anotação SIE
						String  idFieldName = field.getAnnotation(SieOrm.class).id();
						Method  idFieldGetMethod = clazz.getMethod("get"+idFieldName.substring(0, 1).toUpperCase()+idFieldName.substring(1));
						Integer id = (Integer) idFieldGetMethod.invoke(object);

						// Se houver ID
						if (id != null)
						{
							// Tenta recuperar o DAO deste campo
							Class  fieldType = field.getType();
							String fieldTypeName = fieldType.getSimpleName();
							String daoName = fieldTypeName+"Dao";
							Class  daoClass = Class.forName("br.eic.sca.modules.sie.dao."+daoName);
							_DaoSie<?> dao = (_DaoSie<?>)appCtx.getBean(daoClass);
							
							// Tenta recuperar o valor do campo
							fieldValue = dao.retrieveById(id);
						
							// Se o valor foi recuperado com sucesso
							if (fieldValue != null)
							{
								// Tenta setar o valor no objeto
								Method fieldSetMethod = clazz.getMethod("set"+fieldName.substring(0, 1).toUpperCase()+fieldName.substring(1),fieldType);
								fieldSetMethod.invoke(object, fieldValue);
							}
						}
					}
				}
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
}
