/*package test;

import static drawer.CustomDrawer.*;
import static org.lwjgl.opengl.GL11.*;

import java.util.ArrayList;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;

import collision.*;
import drawer.CustomDrawer;

class CollisionTester
{
	public int i =0;
	public boolean transformed = false;
	public double x=0, y=0, zoom = 480;
	public Trait deux = new Trait(100,200, 10, 20);
	public Vec2d vec = new Vec2d(100f, 200f);

	public Forme2D[] murs = new Forme2D[]{new Forme2D(new Trait[]{
		new Trait(25,25,0,500),
		new Trait(25,25,1000,0),
		new Trait(000,350, 1000, 0),
		new Trait(700,350,0,-1000),},new Vec2d(0,0)),

		new Forme2D(new Trait[]{
		new Trait(200,300,-50,-150),
		new Trait(350,250,-150,50),
		new Trait(300,100,50,150),}, new Vec2d(0,0)),
	};
	public ArrayList<Trait> traits = new ArrayList<Trait>();

	public String getName() {return null;}

	public void draw()
	{
		loadGameOrtho(this.x, this.y, this.zoom);

		if (!this.transformed)
		{
			drawTrait(this.deux);
			for (Forme2D f : this.murs)
				for (Trait t : f.getTrait())
					drawTrait(t);
			for (Trait t : this.traits)
				drawTrait(t, 0, 1, 0);
			drawTrait(new Trait(this.deux.x, this.deux.y, this.vec.x, this.vec.y), 0, 0, 1);
		}
		else
		{
			double det = this.deux.w * -this.vec.y -  this.deux.h * -this.vec.x;
			Matrix2d mat;
			if (det != 0)
				mat = new Matrix2d(this.deux.w,this.deux.h,-this.vec.x,-this.vec.y,det);
			else
				mat = new Matrix2d(this.deux.w, this.deux.h, -this.deux.h, this.deux.w, this.deux.w * this.deux.w + this.deux.h * this.deux.h);
				drawTrait(mat.multiply(this.deux));
				for (Trait t : this.murs[0].getTrait())
					drawTrait(mat.multiply(t), 0, 1, 1);
				drawTrait(mat.multiply(this.murs[1].getTrait()[0]), 1, 1, 0);
				drawTrait(mat.multiply(this.murs[1].getTrait()[1]), 0, 1, 0);
				drawTrait(mat.multiply(this.murs[1].getTrait()[2]), 0, 0, 1);
				//for (Trait t : traits)
					//drawTrait(mul(mat,t));
				drawTrait(mat.multiply(new Trait(this.deux.x, this.deux.y, -this.vec.x, -this.vec.y)));
		}
		load2D();
	}
	public void type()
	{
		double shift = 1;
		if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT))
			shift *= 0.1f;
		if (Keyboard.isKeyDown(Keyboard.KEY_RSHIFT))
			shift *= 0.01f;
		if (Keyboard.isKeyDown(Keyboard.KEY_RCONTROL))
			shift *= 0.001f;
		if (Keyboard.isKeyDown(Keyboard.KEY_LCONTROL))
			shift *= 0.0001f;
		if (Keyboard.getEventKey() == Keyboard.KEY_UP)
			this.y+=10*shift;
		if (Keyboard.getEventKey() == Keyboard.KEY_LEFT)
			this.x-=10*shift;
		if (Keyboard.getEventKey() == Keyboard.KEY_RIGHT)
			this.x+=10*shift;
		if (Keyboard.getEventKey() == Keyboard.KEY_DOWN)
			this.y-=10*shift;
		if (Keyboard.getEventKey() == Keyboard.KEY_SUBTRACT)
			this.zoom *= shift == 1 ? 2f : 1.1f;
		if (Keyboard.getEventKey() == Keyboard.KEY_ADD)
			this.zoom *= shift == 1 ? 0.5f : 0.95f;
		if (Keyboard.getEventKey() == 28Entrée)
			this.transformed = !this.transformed;
		if (Keyboard.getEventKey() == Keyboard.KEY_NUMPAD0)
		{
			this.traits.clear();
			this.move(this.deux, this.vec, 1);
		}
		if (Keyboard.getEventKey() == Keyboard.KEY_NUMPAD2)
			this.murs[0].angle += 10;
		if (Keyboard.getEventKey() == Keyboard.KEY_NUMPAD3)
			this.murs[0].angle -= 10;
		if (Keyboard.getEventKey() == Keyboard.KEY_NUMPAD4)
			this.murs[1].angle += 10;
		if (Keyboard.getEventKey() == Keyboard.KEY_NUMPAD5)
			this.murs[1].angle -= 10;
	}
	public void move(Trait deux, Vec2d vec, double longueur)
	{
		Collision c = this.avance2(deux, new Vec2d(vec.x*longueur, vec.y*longueur));
		if (!c.collideNothing())
			c.setAvance(Math.max(0, c.getAvance() - (this.transformed ? 0.01f : 0.00001f)*c.getAvance()));

		this.traits.add(new Trait(deux.x+deux.w, deux.y+deux.h, vec.x*longueur*c.getAvance(), vec.y*longueur*c.getAvance()));
		this.traits.add(new Trait(deux.x, deux.y, vec.x*longueur*c.getAvance(), vec.y*longueur*c.getAvance()));

		deux.x += vec.x*longueur*c.getAvance(); deux.y += vec.y*longueur*c.getAvance();

		if (!c.collideNothing())
		{
			double angleVec = Math.atan2(vec.y, vec.x);
			double angleMur = c.getRebondAngle(angleVec);

			double length = vec.length();

			double sortie = 2 * angleMur - angleVec;

			vec.x = (Math.cos(sortie) * length);
			vec.y = (Math.sin(sortie) * length);
		}
	}
	public Collision avance2(Trait t, Vec2d v)
	{
		Collision c = new Collision(t, v);
		for (Forme2D f : this.murs)
			c.checkCollision(f);
		return c;
	}
	public static void loadGameOrtho(double x, double y, double zoom)
	{
		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();
		glOrtho(x, x+zoom, y, y+zoom, 1, -1);
		glMatrixMode(GL_MODELVIEW);
	}
	public static void drawTrait(Trait t)
	{
		glDisable(GL_TEXTURE);
		glColor4f(1,0,0,1);
		glBegin(GL_LINES);
		glVertex2d(t.x, t.y);
		glVertex2d(t.x+t.w, t.y+t.h);
		glEnd();
		glEnable(GL_TEXTURE);
	}
	public static void drawTrait(Trait t, float r, float v, float b)
	{
		glDisable(GL_TEXTURE);
		glColor4f(r,v,b,1);
		glBegin(GL_LINES);
		glVertex2d(t.x, t.y);
		glVertex2d(t.x+t.w, t.y+t.h);
		glEnd();
		glEnable(GL_TEXTURE);
	}
	public static void main(String[] args) throws LWJGLException
	{
		CollisionTester t = new CollisionTester();

		CustomDrawer.createDisplay(854, 480, "Collision Tester");

		Keyboard.enableRepeatEvents(true);

		load2D();

		while (!Display.isCloseRequested())
		{
			while (Keyboard.next())
				if (Keyboard.getEventKeyState())
					t.type();
			t.draw();
			update();
			Display.sync(60);
		}

		Display.destroy();
	}
}*/
