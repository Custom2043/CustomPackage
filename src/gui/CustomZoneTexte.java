package gui;

import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.KeyEvent;
import java.io.IOException;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.newdawn.slick.AngelCodeFont;

import util.CustomTimer;
import util.Logger;
import util.ScreenCoor;
import drawer.CustomDrawer;

public abstract class CustomZoneTexte extends CustomBouton
{
	/**
	 * State of the caps lock at the beginning of the program
	 */
	public static boolean caps = Toolkit.getDefaultToolkit().getLockingKeyState(KeyEvent.VK_CAPS_LOCK);

	public static final byte ECART = 3;
	public CustomTimer timer = new CustomTimer();
	public boolean visbleCurseur;
	/**
	 * Si user a cliqué dedans
	 */
	public int curseur, debut;
	public String texte;
	public CustomZoneTexte(String n, int i, ScreenCoor c, boolean isActiv)
	{
		super("TexteZone : "+i, i, c, isActiv);
		this.visbleCurseur = isActiv;
		this.texte = n;
		this.debut = this.curseur = this.texte.length();
	}
	public CustomZoneTexte(String n, int i, ScreenCoor c, String texte, boolean isActiv)
	{
		super(n,i,c, isActiv);
		this.visbleCurseur = isActiv;
		this.texte = texte;
		this.debut = this.curseur = this.texte.length();
	}
	@Override
	public void activ()
	{
		this.isActiv = this.visbleCurseur = true;
		this.timer.set0();
	}
	@Override
	public void desactiv()
	{
		this.isActiv = false;
		this.visbleCurseur = false;
		this.debut = this.curseur;
	}
	protected void switchCurseurState(){this.visbleCurseur =!this.visbleCurseur;}
	protected int getDebut()
	{
		return Math.min(this.curseur, this.debut);
	}
	protected int getFin()
	{
		return Math.max(this.curseur, this.debut);
	}
	protected boolean isSelected()
	{
		return this.debut != this.curseur;
	}
	private int getPosition(int x)
	{
		int pos;
		for (int i=0;i<this.texte.length();i++)
		{
			pos = (int)(ECART*CustomDrawer.getGuiSize()) + this.getFont().getWidth(this.texte.substring(0, i+1));
			if (pos > x)
			{
				if (pos-(this.getFont().getWidth(this.texte.substring(i, i+1))/2)>x)
					return i;
				return i+1;
			}
		}
		return this.texte.length();
	}
	public abstract AngelCodeFont getFont();
	private static String getClipboardContents()
	{
	    Transferable t = Toolkit.getDefaultToolkit().getSystemClipboard().getContents(null);
	    try {
	    	if (t != null && t.isDataFlavorSupported(DataFlavor.stringFlavor)) {
	    		String text = (String)t.getTransferData(DataFlavor.stringFlavor);
	    		return text;
	        }
	    }
	    catch (UnsupportedFlavorException | IOException e) {Logger.error(e);}
	    return null;
	}
	public static void setClipboardContents(String copy)
	{
		StringSelection selection = new StringSelection(copy);
	    Toolkit.getDefaultToolkit().getSystemClipboard().setContents(selection, selection);
	}
	private void eraseSelection()
	{
		int min = this.getDebut();
		this.texte = this.texte.substring(0, min) + this.texte.substring(this.getFin(), this.texte.length());
		this.curseur = min;
		this.debut = this.curseur;
	}
	private void avanceCurseur(int avance)
	{
		if (!this.isSelected())
			this.debut += avance;
		this.curseur += avance;
		this.timer.set0();
		this.visbleCurseur = true;
	}
	private void setCursor(int place)
	{
		if (!this.isSelected())
			this.debut = place;
		this.curseur = place;
		this.timer.set0();
		this.visbleCurseur = true;
	}
	private String getSelectedText()
	{
		return this.texte.substring(this.getDebut(), this.getFin());
	}
	private void writeText(String texte)
	{
		if (this.isSelected())
			this.eraseSelection();
		this.texte = this.texte.substring(0, this.curseur) + texte + this.texte.substring(this.curseur, this.texte.length());
		this.avanceCurseur(texte.length());
	}
	@Override
	public void keyTyped(char carac, int keyCode)
	{
		if (this.isActiv && Keyboard.getEventKeyState())
			if (keyCode == Keyboard.KEY_CAPITAL)
				caps = !caps;
			else if (keyCode == Keyboard.KEY_RIGHT && this.curseur < this.texte.length())
			{
				this.avanceCurseur(1);
				this.debut = this.curseur;
			}
			else if (keyCode == Keyboard.KEY_LEFT && this.curseur > 0)
			{
				this.avanceCurseur(-1);
				this.debut = this.curseur;
			}
			else if (keyCode == Keyboard.KEY_DELETE)
			{
				if (this.isSelected())
					this.eraseSelection();
				else if (this.curseur < this.texte.length())
					this.texte = this.texte.substring(0, this.curseur) + this.texte.substring(this.curseur+1, this.texte.length());
			}
			else if (keyCode == Keyboard.KEY_BACK)
			{
				if (this.isSelected())
					this.eraseSelection();
				else if (this.curseur > 0)
				{
					this.texte = this.texte.substring(0, this.curseur-1) + this.texte.substring(this.curseur, this.texte.length());
					this.avanceCurseur(-1);
				}
			}
			else if (this.isCtrlPressed())
			{
				if (keyCode == Keyboard.KEY_V)
					if (getClipboardContents() != null)
						if (this.isStringAcceptable(getClipboardContents()))
							this.writeText(getClipboardContents());
				if (keyCode == Keyboard.KEY_A)
				{
					this.debut = 0;
					this.setCursor(this.texte.length());
				}
				if (keyCode == Keyboard.KEY_C)
					if (this.isSelected())
						setClipboardContents(this.getSelectedText());
				if (keyCode == Keyboard.KEY_X)
				{
					if (this.isSelected())
					{
						setClipboardContents(this.getSelectedText());
						this.eraseSelection();
					}

				} else
					this.addChar(carac);
			} else
				this.addChar(carac);
	}
	private void addChar(char carac)
	{
		int code = Character.codePointAt(new char[]{carac},0);
		boolean maj =  caps || this.isShiftPressed();
		if (caps && this.isShiftPressed())
			maj = false;
		if (code > 10 && code < 256)
			if (maj)
			{
				if (this.isStringAcceptable(String.valueOf(Character.toUpperCase(carac))))
					this.writeText(String.valueOf(Character.toUpperCase(carac)));
			} else if (this.isStringAcceptable(String.valueOf(carac)))
				this.writeText(String.valueOf(carac));
	}
	private boolean isCtrlPressed()
	{
		return Keyboard.isKeyDown(Keyboard.KEY_LCONTROL) || Keyboard.isKeyDown(Keyboard.KEY_RCONTROL);
	}
	private boolean isShiftPressed()
	{
		return Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT);
	}
	private String getStringPlus(String added)
	{
		if (this.isSelected())
			return this.texte.substring(0, this.getDebut())+this.texte.substring(this.getFin(), this.texte.length()) + added;
		return this.texte.substring(0, this.curseur) + added + this.texte.substring(this.curseur, this.texte.length());
	}
	private boolean isStringAcceptable(String added)
	{
		String test = this.getStringPlus(added);
		return this.getFont().getWidth(test) < this.coor.getWidth()-ECART*CustomDrawer.getGuiSize() && this.getFont().getHeight(test)< this.coor.getHeight();
	}
	@Override
	public abstract void draw();
	@Override
	public void click(CustomBouton boutonOn, boolean appuie, int clicID, int X, int Y)
	{
		if (clicID == 0)
			if (appuie)
				if (boutonOn == this)
				{
					this.activ();
					this.debut = this.curseur = this.getPosition(X);
				}
				else
					this.desactiv();
		if (Mouse.isButtonDown(0))
			if (this.isActiv)
				this.curseur = this.getPosition(X);
	}
}
