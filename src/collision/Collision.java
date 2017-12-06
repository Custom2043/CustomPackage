package collision;

import java.util.ArrayList;

import org.lwjgl.util.vector.Vector2f;

import util.Logger;

public class Collision
{
	public Trait fix;
	public Vector2f fixVec;

	public ArrayList<Touche> touches = new ArrayList<Touche>();

	public Collision(){}
	public Collision(Trait f, Vector2f v)
	{
		this.fix = f; this.fixVec = v;
	}

	public void checkAABBandCollision(Trait t, Vector2f v)
	{
		if (this.fix.getLowX() + Math.min(0, this.fixVec.x) <= Math.max(0, v.x) +t.getHighX()
		&&  this.fix.getLowY() + Math.min(0, this.fixVec.y) <= Math.max(0, v.y) +t.getHighY()
		&&  this.fix.getHighX()+ Math.max(0, this.fixVec.x) >= Math.min(0, v.x) + t.getLowX()
		&&  this.fix.getHighY()+ Math.max(0, this.fixVec.y) >= Math.min(0, v.y) + t.getLowY())
			this.checkCollision(t,v);
	}

	public void checkCollision(Trait moving, Vector2f movingVec)
	{
		Vector2f difVec = new Vector2f(movingVec.x - this.fixVec.x, movingVec.y - this.fixVec.y);
		float det = this.fix.w * difVec.y -  this.fix.h * difVec.x;
		Matrix2f mat;
		if (det != 0)
			mat = new Matrix2f(this.fix.w,this.fix.h,difVec.x,difVec.y,det, new Vector2f(this.fix.x, this.fix.y));
		else
			mat = new Matrix2f(this.fix.w, this.fix.h, -this.fix.h, this.fix.w, this.fix.w * this.fix.w + this.fix.h * this.fix.h, new Vector2f(this.fix.x, this.fix.y));

		Trait multedFix = mat.multiply(this.fix), multedMoving = mat.multiply(moving);
		if (det != 0) // fix non parall�le au vecteur
        {
            Carre car = new Carre(multedFix.x, multedFix.y, 1, -1);
            if (car.touch(multedMoving))
				// On cherche le plus haut y de multedMoving entre car.getLowX() et car.getLowY()
            	if (multedMoving.h == 0) // Parall�le � l'axe x
            		this.addTouche(-(multedMoving.y - car.getHighY()), Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY, moving, movingVec);
            	else if (multedMoving.w == 0) // Parall�le � l'axe y
            		this.addTouche(-(Math.min(multedMoving.getHighY(), car.getHighY()) - car.getHighY()), multedMoving.y == multedMoving.getHighY() ? 0 : 1, multedMoving.x, moving, movingVec);
            	else if (multedMoving.h * multedMoving.w > 0) // Ici, x haut = y haut; On cherche donc le plus grand x dans le carr�
            	{
            		if (multedMoving.getHighX() >= car.getHighX())// Passe par l'axe x=1
            			this.addTouche(-(Math.min(multedMoving.getYPosAtX(car.getHighX()),car.getHighY()) - car.getHighY()), (Math.min(multedMoving.getYPosAtX(car.getHighX()),car.getHighY()) - multedMoving.y) / multedMoving.h , multedMoving.getXPosAtY(Math.min(multedMoving.getYPosAtX(car.getHighX()),car.getHighY())) - car.getLowX(), moving, movingVec);
            		else//Le segment finit dans le carré
            			this.addTouche(-(Math.min(multedMoving.getHighY(),car.getHighY()) - car.getHighY()), (Math.min(multedMoving.getHighY(),car.getHighY()) - multedMoving.y) / multedMoving.h , multedMoving.getXPosAtY(Math.min(multedMoving.getHighY(),car.getHighY())), moving, movingVec);
            	} else if (multedMoving.getLowX() <= car.getLowX()) // Passe par l'axe x=0
					this.addTouche(-(Math.min(multedMoving.getYPosAtX(car.getLowX()),car.getHighY()) - car.getHighY()), (Math.min(multedMoving.getYPosAtX(car.getLowX()),car.getHighY()) - multedMoving.y) / multedMoving.h ,  multedMoving.getXPosAtY(Math.min(multedMoving.getYPosAtX(car.getLowX()),car.getHighY())) - car.getLowX(), moving, movingVec);
				else // Le segment commence dans le carr�
					this.addTouche(-(Math.min(multedMoving.getHighY(),car.getHighY()) - car.getHighY()), (Math.min(multedMoving.getHighY(),car.getHighY()) - multedMoving.y) / multedMoving.h , multedMoving.getXPosAtY(Math.min(multedMoving.getHighY(),car.getHighY())), moving, movingVec);
        }
		else
		{
			Vector2f multedVec = mat.multiply(difVec);
			if (multedMoving.getLowY() <= multedFix.getHighY() && multedMoving.getHighY() >= multedFix.getLowY())
			{
				float xTouche = multedMoving.getXPosAtY(multedFix.y);
				System.out.println(xTouche);
				if (!Float.isNaN(xTouche))
					if (Float.isInfinite(xTouche)) // Si fix // vec // moving
					{

						if (multedMoving.getHighX() >= multedFix.getLowX() && multedMoving.getLowX() <= multedFix.getHighX())
							this.addTouche(0, Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY, moving, movingVec); // Si t touche fix d�s le début
						else if (multedMoving.getHighX() < multedFix.getLowX() && multedMoving.getHighX() >= multedFix.getLowX() - multedVec.x) // Si t avant fix sur l'axe X
							this.addTouche((multedFix.getLowX() - multedMoving.getHighX()) / multedVec.x, 1, 0, moving, movingVec);
						else if (multedMoving.getLowX() > multedFix.getHighX() && multedMoving.getLowX() <= multedFix.getHighX() - multedVec.x)// Si t apr�s fix sur X
							this.addTouche((multedFix.getHighX() - multedMoving.getLowX()) / multedVec.x, 0, 1, moving, movingVec);
					} else if (xTouche >= multedFix.getLowX() && xTouche <= multedFix.getHighX())// Si le traverse
						this.addTouche(0, (0 - multedMoving.y) / multedMoving.h, xTouche, moving, movingVec);
					else if (xTouche < multedFix.getLowX() && xTouche >= multedFix.getLowX() - multedVec.x) // Si moving avant fix
						this.addTouche((multedFix.getLowX() - xTouche) / multedVec.x, (0 - multedMoving.y) / multedMoving.h, 0, moving, movingVec);
					else if (xTouche > multedFix.getHighX() && xTouche <= multedFix.getHighX() - multedVec.x)// Si t après fix sur X
						this.addTouche((multedFix.getHighX() - xTouche) / multedVec.x, (0 - multedMoving.y) / multedMoving.h, 1, moving, movingVec);
			}
		}
	}
	private void addTouche(float a, float mA, float fA, Trait mouvant, Vector2f vec)
	{
		if (a >= 0 && a <= 1)
		{
			Logger.debug("Add new Touche : " + new Touche(vec, mouvant, a, mA, fA).toString());
			this.touches.add(new Touche(vec, mouvant, a, mA, fA));
		}
	}
	public boolean willTouchSomething()
	{
		return this.touches.size() != 0;
	}
	public float getFirstTouchAvance()
	{
		if (!this.willTouchSomething())
			return Float.NaN;
		float a = 2;
		for (Touche t : this.touches)
			if (a > t.avance)
				a = t.avance;
		return a;
	}
	public Touche getFirstTouch()
	{
		if (!this.willTouchSomething())
			return null;
		Touche a = null;
		for (Touche t : this.touches)
			if (a == null || a.avance > t.avance)
				a = t;
		return a;
	}
	public Touche getTouch(int i)
	{
		if (i >= this.touches.size() || i < 0)
			return null;
		Touche[] t = new Touche[this.touches.size()];
		for (int j=0;j<t.length;j++)
		{
			Touche a = null;
			for (Touche tt : this.touches)
				if (a == null || a.avance > tt.avance)
				{
					boolean in = false;
					for (Touche ttt : t)
						if (ttt == tt)
							in = true;
					if (!in)
						a = tt;
				}
			t[i] = a;
		}
		return t[i];
	}
	public float getAngle()
	{
		float vecAngle = (float) Math.atan2(this.fixVec.y, this.fixVec.x);
		if (!this.willTouchSomething())
			return Float.NaN;
		if (this.touches.size() == 1)
			return (float)this.getFirstTouch().mouvant.getAngle();
		Trait haut = null; Trait bas = null;
		//Touche last, active;
		//last = this.getFirstTouch();
		float firstAvance = this.getFirstTouchAvance();
		float dif, dif2;
		for (int i=0;i<this.touches.size();i++)
		{
			Touche act = this.touches.get(i);
			if (act.avance * .9999f <= firstAvance)
			{
				if (act.board())
				{
					dif = (float) (- Math.PI/2);
					dif2 = (float) (Math.PI/2);
				}
				else
				{
					dif = this.getRegularAngle(vecAngle - act.mouvant.getAngle());
					dif2 = this.getRegularAngle(dif + Math.PI);
				}
				if (dif > 0 && (haut == null || this.getRegularAngle(vecAngle - haut.getAngle()) > dif))
					haut = act.mouvant;

				if (dif2 > 0 && (haut == null || this.getRegularAngle(vecAngle - haut.getAngle()) > dif2))
					haut = act.mouvant;

				if (dif < 0 && (bas == null || this.getRegularAngle(vecAngle - bas.getAngle()) < dif))
					bas = act.mouvant;

				if (dif2 < 0 && (bas == null || this.getRegularAngle(vecAngle - bas.getAngle()) < dif2))
					bas = act.mouvant;
			}
		}
		if (bas == null)
			return (float) haut.getAngle();
		else if (haut == null)
			return (float) bas.getAngle();
		else
			return  (float) (((bas.getAngle()+haut.getAngle())/2) + Math.PI/2);
	}
	public float getRegularAngle(double dif)
	{
		if (dif > Math.PI)
			dif -= Math.PI * 2;
		if (dif <= -Math.PI)
			dif += Math.PI * 2;
		return (float)dif;
	}
	public int wallNumber()
	{
		if (!this.willTouchSomething())
			return 0;
		float firstAvance = this.getFirstTouchAvance();
		ArrayList<Trait> trait = new ArrayList<Trait>();
		for (Touche t : this.touches)
			if (!trait.contains(t.mouvant) && t.avance * 0.99f <= firstAvance)
			{
				System.out.println(t.mouvant);
				trait.add(t.mouvant);
			}
		System.out.println(trait.size());
		return trait.size();
	}
}
