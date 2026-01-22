package net.minecraft.client;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiLoadLevel;
import net.minecraft.client.gui.GuiNewLevel;
import net.minecraft.client.gui.GuiOptions;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.render.Tessellator;
import net.mobmod.macohi.Constants;
import net.mobmod.macohi.MenuTitleButton;

import org.lwjgl.opengl.GL11;
import util.MathHelper;

public final class GuiMainTitle extends GuiScreen {
	private float updateCounter = 0.0F;

	public final void updateScreen() {
		this.updateCounter += 0.01F;
	}

	protected final void keyTyped(char var1, int var2) {
	}

	public final void initGui() {
		this.controlList.clear();

		Integer i = 0;
		for (MenuTitleButton object : Constants.MENU_TITLE_BUTTONS()) {

			GuiButton button = new GuiButton(
				i, 
				this.width / 2 - 100, 
				this.height / 4 + (48 + (24 * i)), 
				object.text, 
				200
			);

			this.controlList.add(button);
			i++;
		}
	}

	protected final void actionPerformed(GuiButton var1) {
	}

	public final void drawScreen(int var1, int var2) {
		GL11.glDisable(GL11.GL_LIGHTING);
		GL11.glDisable(GL11.GL_FOG);
		Tessellator var3 = Tessellator.instance;
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.mc.renderEngine.getTexture("/dirt.png"));
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		var3.startDrawingQuads();
		var3.setColorOpaque_I(4210752);
		var3.addVertexWithUV(0.0F, (float) this.height, 0.0F, 0.0F, (float) this.height / 32.0F + this.updateCounter);
		var3.addVertexWithUV((float) this.width, (float) this.height, 0.0F, (float) this.width / 32.0F,
				(float) this.height / 32.0F + this.updateCounter);
		var3.addVertexWithUV((float) this.width, 0.0F, 0.0F, (float) this.width / 32.0F, 0.0F + this.updateCounter);
		var3.addVertexWithUV(0.0F, 0.0F, 0.0F, 0.0F, 0.0F + this.updateCounter);
		var3.draw();
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.mc.renderEngine.getTexture("/gui/logo.png"));
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		var3.setColorOpaque_I(16777215);
		this.drawTexturedModalRect((this.width - 256) / 2, 30, 0, 0, 256, 49);
		GL11.glPushMatrix();
		GL11.glTranslatef((float) (this.width / 2 + 90), 70.0F, 0.0F);
		GL11.glRotatef(-20.0F, 0.0F, 0.0F, 1.0F);
		float var4 = 1.8F - MathHelper.abs(
				MathHelper.sin((float) (System.currentTimeMillis() % 1000L) / 1000.0F * (float) Math.PI * 2.0F) * 0.1F);
		GL11.glScalef(var4, var4, var4);
		drawCenteredString(this.fontRenderer, "Pre-beta!", 0, -8, 16776960);
		GL11.glPopMatrix();
		String var5 = "Copyright Mojang Specifications. Do not distribute.";
		b(this.fontRenderer, var5, this.width - this.fontRenderer.getStringWidth(var5) - 2, this.height - 10, 16777215);
		super.drawScreen(var1, var2);
	}
}
