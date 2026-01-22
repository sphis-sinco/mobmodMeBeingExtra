package net.minecraft.client.gui;

import net.minecraft.client.Minecraft;
import org.lwjgl.opengl.GL11;

public class GuiButton extends Gui {
	private int width;
	private int height;
	private int x;
	private int y;
	public String displayString;
	public int id;
	public boolean enabled;
	public boolean visible;
	
	public GuiButton(int id, int x, int y, String displayString) {
		this(id, x, y, 200, 20, displayString);
	}

	protected GuiButton(int id, int x, int y, int width, int height, String displayString) {
		this.width = 200;
		this.height = 20;
		this.enabled = true;
		this.visible = true;
		this.id = id;
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.displayString = displayString;
	}

	public final void drawButton(Minecraft var1, int var2, int var3) {
		if(this.visible) {
			FontRenderer var4 = var1.fontRenderer;
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, var1.renderEngine.getTexture("/gui/gui.png"));
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			byte var5 = 1;
			boolean var6 = var2 >= this.x && var3 >= this.y && var2 < this.x + this.width && var3 < this.y + this.height;
			if(!this.enabled) {
				var5 = 0;
			} else if(var6) {
				var5 = 2;
			}

			this.drawTexturedModalRect(this.x, this.y, 0, 46 + var5 * 20, this.width / 2, this.height);
			this.drawTexturedModalRect(this.x + this.width / 2, this.y, 200 - this.width / 2, 46 + var5 * 20, this.width / 2, this.height);
			if(!this.enabled) {
				drawCenteredString(var4, this.displayString, this.x + this.width / 2, this.y + (this.height - 8) / 2, -6250336);
			} else if(var6) {
				drawCenteredString(var4, this.displayString, this.x + this.width / 2, this.y + (this.height - 8) / 2, 16777120);
			} else {
				drawCenteredString(var4, this.displayString, this.x + this.width / 2, this.y + (this.height - 8) / 2, 14737632);
			}
		}
	}

	public final boolean mousePressed(int var1, int var2) {
		return this.enabled && var1 >= this.x && var2 >= this.y && var1 < this.x + this.width && var2 < this.y + this.height;
	}
}
