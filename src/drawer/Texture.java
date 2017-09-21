/*
 * Copyright (C) 2016 Team Ubercube
 *
 * This file is part of Ubercube.
 *
 *     Ubercube is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     Ubercube is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with Ubercube.  If not, see http://www.gnu.org/licenses/.
 */

package drawer;

import static org.lwjgl.opengl.GL11.*;

/**
 * Created by Marccspro on 1 avr. 2016.
 */
public class Texture
{
	private int	id;
	private int	width, height;

	public Texture(int id, int width, int height)
	{
		this.id = id;
		this.width = width;
		this.height = height;
	}

	public int getWidth()
	{
		return this.width;
	}

	public int getHeight()
	{
		return this.height;
	}

	public void bind()
	{
		glBindTexture(GL_TEXTURE_2D, this.id);
	}

	public static void unbind()
	{
		glBindTexture(GL_TEXTURE_2D, 0);
	}
}
