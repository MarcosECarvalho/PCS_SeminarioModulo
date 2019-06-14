package br.eic.sca.core.menu;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class ScaMenu 
{
	private Map<String,ScaMenuCategory> categoriesMap = new TreeMap<String,ScaMenuCategory>();
	
	public void addMenuItem(String categoryName,String itemName,String url)
	{
		ScaMenuCategory category = categoriesMap.get(categoryName);
		
		if (category == null)
		{
			category = new ScaMenuCategory(categoryName);
			categoriesMap.put(categoryName, category);
		}
		
		category.addItem(itemName, url);			
	}
	
	public List<ScaMenuCategory> getCategories()
	{
		return new ArrayList<ScaMenuCategory>(categoriesMap.values());
	}

}
