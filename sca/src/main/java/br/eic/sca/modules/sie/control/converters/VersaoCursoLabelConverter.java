package br.eic.sca.modules.sie.control.converters;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.eic.sca.modules.sie.domain.VersaoCurso;
import br.eic.sca.modules.sie.filters.VersaoCursoFilter;
import br.eic.sca.modules.sie.service.VersaoCursoService;

@Component
public class VersaoCursoLabelConverter implements Converter 
{
	@Autowired
	VersaoCursoService versaoCursoService;
	
	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String versaoCursoLabel) 
	{
		if (versaoCursoLabel==null || versaoCursoLabel.isEmpty()) 
		{
			return null;
		}

		try 
		{
			String sigla = versaoCursoLabel.split(" - ")[0];
			String versao = versaoCursoLabel.split(" - ")[1];
			VersaoCursoFilter versaoCursoFilter = new VersaoCursoFilter("", sigla, versao);
			
			// TODO - Fazer operação específica no DAO para isso
			VersaoCurso versaoCurso = versaoCursoService.retrieveByVersaoCursoFilter(versaoCursoFilter).get(0);
			
			if (versaoCurso==null)
				throw new Exception("Não foi possível recuperar uma Versão de Curso pelo label "+versaoCursoLabel);
			
			return versaoCurso;
		}
		catch (Exception e) 
		{
			throw new ConverterException("Versão Curso com label inválido - "+versaoCursoLabel);
		}		
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object versaoCursoValue) 
	{
		if (versaoCursoValue == null) 
		{
			return "";
		}

		VersaoCurso versaoCurso = (VersaoCurso)versaoCursoValue;
		return versaoCurso.getLabel();
	}
}