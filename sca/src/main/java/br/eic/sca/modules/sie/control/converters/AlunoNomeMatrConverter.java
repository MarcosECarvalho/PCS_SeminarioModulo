package br.eic.sca.modules.sie.control.converters;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.eic.sca.modules.sie.domain.Aluno;
import br.eic.sca.modules.sie.service.AlunoService;

@Component
public class AlunoNomeMatrConverter implements Converter 
{
	@Autowired
	AlunoService alunoService;
	
	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String alunoLabel) 
	{
		if (alunoLabel==null || alunoLabel.isEmpty()) 
		{
			return null;
		}

		String matricula = StringUtils.substringBetween(alunoLabel,"(",")");

		try 
		{
			Aluno aluno = alunoService.retrieveByMatricula(matricula);
			
			if (aluno==null)
				throw new Exception("Não foi possível recuperar um aluno pela matricula "+matricula);
			
			return aluno;
		}
		catch (Exception e) 
		{
			throw new ConverterException("Matrícula inválida para o aluno - "+matricula);
		}		
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object alunoValue) 
	{
		if (alunoValue == null) 
		{
			return "";
		}

		Aluno aluno = (Aluno)alunoValue;
		return aluno.getNome() + "("+aluno.getMatricula()+")";
	}
}