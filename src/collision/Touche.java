package collision;

import org.lwjgl.util.vector.Vector2f;

public class Touche 
{
	public Vector2f mouvantVec;
	public Trait mouvant;
	
	public float avance;
	
	/**
	 * Positive Infinity means both Traits touches at least at 2 different points
	 * mA == POSITIVE_INFINITY <=> fA == POSITIVE INFINITY
	 */
	public float mouvantAvance, fixAvance;
	
	public Touche(Vector2f mF, Trait m, float a, float mA, float fA)
	{
		this.mouvantVec = mF;
		this.mouvant = m;
		this.avance = a;
		this.mouvantAvance = mA;
		this.fixAvance = fA;
	}
	@Override
	public String toString()
	{
		return "With Trait : "+this.mouvant.toString() + ", Avance : "+this.avance+", Avance mouvant : "+this.mouvantAvance+", Avance fix : "+this.fixAvance;
	}
	public boolean board()
	{
		return mouvantAvance == 0 ||  mouvantAvance == 1;
	}
}
