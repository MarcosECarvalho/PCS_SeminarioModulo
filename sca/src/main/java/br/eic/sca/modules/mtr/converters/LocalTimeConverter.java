package br.eic.sca.modules.mtr.converters;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;

import org.springframework.stereotype.Component;

@Component
public class LocalTimeConverter implements Converter 
{
	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String stringValue) 
	{
		if (null == stringValue || stringValue.isEmpty()) 
		{
			return null;
		}

		LocalTime localTime;

		try 
		{
			localTime = LocalTime.parse(stringValue,DateTimeFormatter.ofPattern("HH:mm"));
		}
		catch (DateTimeParseException e) 
		{
			throw new ConverterException("O horário deve estar no formato HH:mm (24horas)");
		}

		return localTime;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object localTimeValue) 
	{
		if (null == localTimeValue) 
		{
			return "";
		}

		return ((LocalTime) localTimeValue).format(DateTimeFormatter.ofPattern("HH:mm"));
	}
}