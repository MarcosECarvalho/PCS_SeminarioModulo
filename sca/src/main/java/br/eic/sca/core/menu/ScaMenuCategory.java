package br.eic.sca.core.menu;

import java.util.ArrayList;
import java.util.List;

public class ScaMenuCategory 
{
	private String name;
	private List<ScaMenuItem> items;
	
	public ScaMenuCategory(String name)
	{
		this.name = name;
		this.items = new ArrayList<ScaMenuItem>();
	}
	
	//
	// Operações
	//
	public void addItem(String itemName, String url)
	{
		for (ScaMenuItem sessionMenuItem : items) 
		{
			if (sessionMenuItem.getName().equals(itemName))
			{
				if (!sessionMenuItem.getUrl().equals(url))
				{
					throw new IllegalArgumentException("Duplicate name for SessionMenuItem "+itemName+" with two differents URL's: (1) "+url+" and (2) "+sessionMenuItem.getUrl());
				}					
			}				
		}
		
		this.items.add(new ScaMenuItem(itemName, url));
	}
	
	//
	// Métodos de Acesso
	//
	public String getName() 
	{
		return name;
	}
	
	public List<ScaMenuItem> getItems() {
		return items;
	}
	
}
