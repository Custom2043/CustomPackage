package util;

import java.util.ArrayList;

public class UpdateList<Type>
{
	private Object LOCK = new Object();
	private ArrayList<Type> buf = new ArrayList<Type>(); // Utilisé pendant que l'opérateur utilise getList();
	private ArrayList<Type> vrai = new ArrayList<Type>();
	/**
	 * Used by thread 1
	 */
	public void add(Type t)
	{
		synchronized(this.LOCK){
		this.buf.add(t);}
	}
	/**
	 * Used by thread 2
	 */
	public void update()
	{
		synchronized(this.LOCK){
		this.vrai.addAll(this.buf);
		this.buf.clear();}
	}
	/**
	 * Used by thread 2
	 */
	public ArrayList<Type> getList()
	{
		this.update();
		return this.vrai;
	}
	/**
	 * Used by thread 1
	 */
	public ArrayList<Type> getImage()
	{
		ArrayList<Type> image = new ArrayList<Type>();
		synchronized(this.LOCK){
		image.addAll(this.vrai);
		image.addAll(this.buf);}
		return image;
	}
}
