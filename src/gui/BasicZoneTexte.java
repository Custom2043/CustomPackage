package gui;

import static drawer.CustomDrawer.drawRect;

import org.newdawn.slick.AngelCodeFont;
import org.newdawn.slick.Color;

import util.ScreenCoor;
import drawer.CustomDrawer;

public class BasicZoneTexte extends CustomZoneTexte
{

	public BasicZoneTexte(String n, int i, ScreenCoor c, boolean isActiv) {
		super(n, i, c, isActiv);
	}

	public BasicZoneTexte(String n, int i, ScreenCoor c, String texte, boolean isActiv)
	{
		super(n,i,c,texte,isActiv);
	}

	@Override
	public void draw()
	{
		CustomDrawer.drawRect(this.coor.addGui(-1, -1, 2, 2), Color.white);
		CustomDrawer.drawRect(this.coor, Color.black);
		if (this.visbleCurseur)
			if (this.isSelected())
				drawRect(this.coor.addXGui(ECART).addXFlat(-1 + this.getFont().getWidth(this.texte.substring(0, this.curseur))).setY(0, -1, this.coor.getMiddleY() - this.getFont().getLineHeight()/2).setW(0,2,0).setH(0,2,this.getFont().getLineHeight()), new Color(100,100,100));
			else
				drawRect(this.coor.addXGui(ECART).addXFlat(-1 + this.getFont().getWidth(this.texte.substring(0, this.curseur))).setY(0, -1, this.coor.getMiddleY() - this.getFont().getLineHeight()/2).setW(0,1,0).setH(0,2,this.getFont().getLineHeight()), Color.white);

		if (this.isSelected())
		{
			CustomDrawer.drawString(this.coor.addXGui(ECART).getStartX(), this.coor.getMiddleY() - this.getFont().getLineHeight()/2, false, false, this.texte.substring(0,this.getDebut()), this.getFont(), Color.white);

			CustomDrawer.drawRect(ScreenCoor.flat(this.coor.addXGui(ECART).addXFlat(this.getFont().getWidth(this.texte.substring(0,this.getDebut()))).getStartX(), this.coor.getMiddleY()-this.getFont().getLineHeight()/2 - this.coor.getGuiSize(), this.getFont().getWidth(this.texte.substring(this.getDebut(),this.getFin())) - 1, this.getFont().getLineHeight() + 2*this.coor.getGuiSize()), Color.white);

			CustomDrawer.drawString(this.coor.addXGui(ECART).addXFlat(this.getFont().getWidth(this.texte.substring(0,this.getDebut() + 1)) - this.getFont().getWidth(this.texte.substring(this.getDebut(),this.getDebut() + 1))).getStartX(), this.coor.getMiddleY() - this.getFont().getLineHeight()/2, false, false, this.texte.substring(this.getDebut(),this.getFin()), this.getFont(), Color.blue);
			if (this.getFin() == this.texte.length())
				CustomDrawer.drawString(this.coor.addXGui(ECART).addXFlat(this.getFont().getWidth(this.texte.substring(0,this.getFin()))).getStartX(), this.coor.getMiddleY() - this.getFont().getLineHeight()/2, false, false, this.texte.substring(this.getFin(),this.texte.length()), this.getFont(), Color.white);
			else
				CustomDrawer.drawString(this.coor.addXGui(ECART).addXFlat(this.getFont().getWidth(this.texte.substring(0,this.getFin() + 1)) - this.getFont().getWidth(this.texte.substring(this.getFin(),this.getFin() + 1))).getStartX(), this.coor.getMiddleY() - this.getFont().getLineHeight()/2, false, false, this.texte.substring(this.getFin(),this.texte.length()), this.getFont(), Color.white);
		}

		else
			CustomDrawer.drawString(this.coor.addXGui(ECART).getStartX(), this.coor.getMiddleY() - this.getFont().getLineHeight()/2, false, false, this.texte, this.getFont(), Color.white);

		if (this.isActiv)
			if (this.timer.getNumberOfTicks(500) > 0)
			{
				this.switchCurseurState();
				this.timer.resetUnderATick(500);
			}
	}
	@Override
	public AngelCodeFont getFont() {
		return BasicBouton.font;
	}

}
