package net.minecraft.client.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.RenderHelper;
import net.minecraft.client.player.EntityPlayerSP;
import net.minecraft.game.item.ItemStack;
import net.minecraft.game.level.block.Block;
import org.lwjgl.opengl.GL11;
import util.MathHelper;

public final class ItemRenderer {
	private Minecraft mc;
	private ItemStack itemToRender = null;
	private float equippedProgress = 0.0F;
	private float prevEquippedProgress = 0.0F;
	private int swingProgress = 0;
	private boolean itemSwingState = false;
	private RenderBlocks renderBlocksInstance = new RenderBlocks(Tessellator.instance);

	public ItemRenderer(Minecraft var1) {
		this.mc = var1;
	}

	public final void renderItemInFirstPerson(float var1) {
		float var2 = this.prevEquippedProgress + (this.equippedProgress - this.prevEquippedProgress) * var1;
		EntityPlayerSP var3 = this.mc.thePlayer;
		GL11.glPushMatrix();
		GL11.glRotatef(var3.prevRotationPitch + (var3.rotationPitch - var3.prevRotationPitch) * var1, 1.0F, 0.0F, 0.0F);
		GL11.glRotatef(var3.prevRotationYaw + (var3.rotationYaw - var3.prevRotationYaw) * var1, 0.0F, 1.0F, 0.0F);
		RenderHelper.enableStandardItemLighting();
		GL11.glPopMatrix();
		GL11.glPushMatrix();
		float var4;
		float var5;
		float var6;
		if(this.itemSwingState) {
			var4 = ((float)this.swingProgress + var1) / 8.0F;
			var5 = MathHelper.sin(var4 * (float)Math.PI);
			var6 = MathHelper.sin(MathHelper.sqrt_float(var4) * (float)Math.PI);
			GL11.glTranslatef(-var6 * 0.4F, MathHelper.sin(MathHelper.sqrt_float(var4) * (float)Math.PI * 2.0F) * 0.2F, -var5 * 0.2F);
		}

		GL11.glTranslatef(0.56F, -0.52F - (1.0F - var2) * 0.6F, -0.71999997F);
		GL11.glRotatef(45.0F, 0.0F, 1.0F, 0.0F);
		GL11.glEnable(GL11.GL_NORMALIZE);
		if(this.itemSwingState) {
			var4 = ((float)this.swingProgress + var1) / 8.0F;
			var5 = MathHelper.sin(var4 * var4 * (float)Math.PI);
			var6 = MathHelper.sin(MathHelper.sqrt_float(var4) * (float)Math.PI);
			GL11.glRotatef(-var5 * 20.0F, 0.0F, 1.0F, 0.0F);
			GL11.glRotatef(-var6 * 20.0F, 0.0F, 0.0F, 1.0F);
			GL11.glRotatef(-var6 * 80.0F, 1.0F, 0.0F, 0.0F);
		}

		var4 = this.mc.theWorld.getBlockLightValue((int)var3.posX, (int)var3.posY, (int)var3.posZ);
		GL11.glColor4f(var4, var4, var4, 1.0F);
		if(this.itemToRender != null) {
			GL11.glScalef(0.4F, 0.4F, 0.4F);
			if(this.itemToRender.itemID < 256 && Block.blocksList[this.itemToRender.itemID].getRenderType() == 0) {
				GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.mc.renderEngine.getTexture("/terrain.png"));
				this.renderBlocksInstance.renderBlockOnInventory(Block.blocksList[this.itemToRender.itemID]);
			} else {
				if(this.itemToRender.itemID < 256) {
					GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.mc.renderEngine.getTexture("/terrain.png"));
				} else {
					GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.mc.renderEngine.getTexture("/gui/items.png"));
				}

				Tessellator var12 = Tessellator.instance;
				ItemStack var10 = this.itemToRender;
				var1 = (float)(var10.getItem().getItemStackLimit() % 16 << 4) / 256.0F;
				var10 = this.itemToRender;
				var2 = (float)((var10.getItem().getItemStackLimit() % 16 << 4) + 16) / 256.0F;
				var10 = this.itemToRender;
				float var9 = (float)(var10.getItem().getItemStackLimit() / 16 << 4) / 256.0F;
				var10 = this.itemToRender;
				var4 = (float)((var10.getItem().getItemStackLimit() / 16 << 4) + 16) / 256.0F;
				GL11.glEnable(GL11.GL_NORMALIZE);
				GL11.glTranslatef(0.0F, -0.3F, 0.0F);
				GL11.glScalef(1.5F, 1.5F, 1.5F);
				GL11.glRotatef(50.0F, 0.0F, 1.0F, 0.0F);
				GL11.glRotatef(335.0F, 0.0F, 0.0F, 1.0F);
				GL11.glTranslatef(-(15.0F / 16.0F), -(1.0F / 16.0F), 0.0F);
				Tessellator.setNormal(0.0F, 0.0F, 1.0F);
				var12.startDrawingQuads();
				var12.addVertexWithUV(0.0F, 0.0F, 0.0F, var2, var4);
				var12.addVertexWithUV(1.0F, 0.0F, 0.0F, var1, var4);
				var12.addVertexWithUV(1.0F, 1.0F, 0.0F, var1, var9);
				var12.addVertexWithUV(0.0F, 1.0F, 0.0F, var2, var9);
				var12.draw();
				Tessellator.setNormal(0.0F, 0.0F, -1.0F);
				var12.startDrawingQuads();
				var12.addVertexWithUV(0.0F, 1.0F, -(1.0F / 16.0F), var2, var9);
				var12.addVertexWithUV(1.0F, 1.0F, -(1.0F / 16.0F), var1, var9);
				var12.addVertexWithUV(1.0F, 0.0F, -(1.0F / 16.0F), var1, var4);
				var12.addVertexWithUV(0.0F, 0.0F, -(1.0F / 16.0F), var2, var4);
				var12.draw();
				Tessellator.setNormal(-1.0F, 0.0F, 0.0F);
				var12.startDrawingQuads();

				float var7;
				float var8;
				int var11;
				for(var11 = 0; var11 < 16; ++var11) {
					var7 = (float)var11 / 16.0F;
					var8 = var2 + (var1 - var2) * var7 - 0.001953125F;
					var7 *= 1.0F;
					var12.addVertexWithUV(var7, 0.0F, -(1.0F / 16.0F), var8, var4);
					var12.addVertexWithUV(var7, 0.0F, 0.0F, var8, var4);
					var12.addVertexWithUV(var7, 1.0F, 0.0F, var8, var9);
					var12.addVertexWithUV(var7, 1.0F, -(1.0F / 16.0F), var8, var9);
				}

				var12.draw();
				Tessellator.setNormal(1.0F, 0.0F, 0.0F);
				var12.startDrawingQuads();

				for(var11 = 0; var11 < 16; ++var11) {
					var7 = (float)var11 / 16.0F;
					var8 = var2 + (var1 - var2) * var7 - 0.001953125F;
					var7 = var7 * 1.0F + 1.0F / 16.0F;
					var12.addVertexWithUV(var7, 1.0F, -(1.0F / 16.0F), var8, var9);
					var12.addVertexWithUV(var7, 1.0F, 0.0F, var8, var9);
					var12.addVertexWithUV(var7, 0.0F, 0.0F, var8, var4);
					var12.addVertexWithUV(var7, 0.0F, -(1.0F / 16.0F), var8, var4);
				}

				var12.draw();
				Tessellator.setNormal(0.0F, 1.0F, 0.0F);
				var12.startDrawingQuads();

				for(var11 = 0; var11 < 16; ++var11) {
					var7 = (float)var11 / 16.0F;
					var8 = var4 + (var9 - var4) * var7 - 0.001953125F;
					var7 = var7 * 1.0F + 1.0F / 16.0F;
					var12.addVertexWithUV(0.0F, var7, 0.0F, var2, var8);
					var12.addVertexWithUV(1.0F, var7, 0.0F, var1, var8);
					var12.addVertexWithUV(1.0F, var7, -(1.0F / 16.0F), var1, var8);
					var12.addVertexWithUV(0.0F, var7, -(1.0F / 16.0F), var2, var8);
				}

				var12.draw();
				Tessellator.setNormal(0.0F, -1.0F, 0.0F);
				var12.startDrawingQuads();

				for(var11 = 0; var11 < 16; ++var11) {
					var7 = (float)var11 / 16.0F;
					var8 = var4 + (var9 - var4) * var7 - 0.001953125F;
					var7 *= 1.0F;
					var12.addVertexWithUV(1.0F, var7, 0.0F, var1, var8);
					var12.addVertexWithUV(0.0F, var7, 0.0F, var2, var8);
					var12.addVertexWithUV(0.0F, var7, -(1.0F / 16.0F), var2, var8);
					var12.addVertexWithUV(1.0F, var7, -(1.0F / 16.0F), var1, var8);
				}

				var12.draw();
				GL11.glDisable(GL11.GL_NORMALIZE);
			}
		} else {
			GL11.glScalef(1.0F, -1.0F, -1.0F);
			GL11.glTranslatef(0.0F, 0.2F, 0.0F);
			GL11.glRotatef(-120.0F, 0.0F, 0.0F, 1.0F);
			GL11.glScalef(1.0F, 1.0F, 1.0F);
		}

		GL11.glDisable(GL11.GL_NORMALIZE);
		GL11.glPopMatrix();
		RenderHelper.disableStandardItemLighting();
	}

	public final void b(float var1) {
		int var2;
		Tessellator var3;
		if(this.mc.thePlayer.fire > 0) {
			var2 = this.mc.renderEngine.getTexture("/terrain.png");
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, var2);
			var3 = Tessellator.instance;
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 0.9F);
			GL11.glEnable(GL11.GL_BLEND);
			GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

			for(var2 = 0; var2 < 2; ++var2) {
				GL11.glPushMatrix();
				int var4 = Block.fire.blockIndexInTexture + (var2 << 4);
				int var5 = (var4 & 15) << 4;
				var4 &= 240;
				float var6 = (float)var5 / 256.0F;
				float var9 = ((float)var5 + 15.99F) / 256.0F;
				float var7 = (float)var4 / 256.0F;
				float var8 = ((float)var4 + 15.99F) / 256.0F;
				GL11.glTranslatef((float)(-((var2 << 1) - 1)) * 0.24F, -0.3F, 0.0F);
				GL11.glRotatef((float)((var2 << 1) - 1) * 10.0F, 0.0F, 1.0F, 0.0F);
				var3.startDrawingQuads();
				var3.addVertexWithUV(-0.5F, -0.5F, -0.5F, var9, var8);
				var3.addVertexWithUV(0.5F, -0.5F, -0.5F, var6, var8);
				var3.addVertexWithUV(0.5F, 0.5F, -0.5F, var6, var7);
				var3.addVertexWithUV(-0.5F, 0.5F, -0.5F, var9, var7);
				var3.draw();
				GL11.glPopMatrix();
			}

			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			GL11.glDisable(GL11.GL_BLEND);
		}

		if(this.mc.thePlayer.isInsideOfMaterial()) {
			var2 = this.mc.renderEngine.getTexture("/water.png");
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, var2);
			var3 = Tessellator.instance;
			var1 = this.mc.thePlayer.getEntityBrightness(var1);
			GL11.glColor4f(var1, var1, var1, 0.5F);
			GL11.glEnable(GL11.GL_BLEND);
			GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
			GL11.glPushMatrix();
			var3.startDrawingQuads();
			var3.addVertexWithUV(-1.0F, -1.0F, -0.5F, 4.0F, 4.0F);
			var3.addVertexWithUV(1.0F, -1.0F, -0.5F, 0.0F, 4.0F);
			var3.addVertexWithUV(1.0F, 1.0F, -0.5F, 0.0F, 0.0F);
			var3.addVertexWithUV(-1.0F, 1.0F, -0.5F, 4.0F, 0.0F);
			var3.draw();
			GL11.glPopMatrix();
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			GL11.glDisable(GL11.GL_BLEND);
		}

	}

	public final void updateEquippedItem() {
		this.prevEquippedProgress = this.equippedProgress;
		if(this.itemSwingState) {
			++this.swingProgress;
			if(this.swingProgress == 8) {
				this.swingProgress = 0;
				this.itemSwingState = false;
			}
		}

		EntityPlayerSP var1 = this.mc.thePlayer;
		ItemStack var3 = var1.inventory.getCurrentItem();
		float var2 = var3 == this.itemToRender ? 1.0F : 0.0F;
		var2 -= this.equippedProgress;
		if(var2 < -0.4F) {
			var2 = -0.4F;
		}

		if(var2 > 0.4F) {
			var2 = 0.4F;
		}

		this.equippedProgress += var2;
		if(this.equippedProgress < 0.1F) {
			this.itemToRender = var3;
		}

	}

	public final void equipAnimationSpeed() {
		this.equippedProgress = 0.0F;
	}

	public final void equippedItemRender() {
		this.swingProgress = -1;
		this.itemSwingState = true;
	}

	public final void resetEquippedProgress() {
		this.equippedProgress = 0.0F;
	}
}
