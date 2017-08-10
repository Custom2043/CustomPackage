package util;

import java.util.ArrayList;

public class IDList<Type> 
{
	private ArrayList<Type> objets = new ArrayList<Type>();
	private ArrayList<Integer> ids = new ArrayList<Integer>();
	
	public void add(Type o, int id)
	{
		this.objets.add(o);
		this.ids.add(id);
	}
	public Type getObjectForID(int id)
	{
		for (int i=0;i<ids.size();i++)
			if (ids.get(i) == id)
				return objets.get(i);
		return null;
	}
	public int getIDForObject(Type o)
	{
		for (int i=0;i<objets.size();i++)
			if (objets.get(i).equals(o))
				return ids.get(i);
		return 0;
	}
	
}
