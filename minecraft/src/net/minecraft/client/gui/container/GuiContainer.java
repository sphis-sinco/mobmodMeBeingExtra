package net.minecraft.client.gui.container;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.RenderHelper;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.player.EntityPlayerSP;
import net.minecraft.client.render.entity.RenderItem;
import net.minecraft.game.IInventory;
import net.minecraft.game.item.ItemStack;
import org.lwjgl.opengl.GL11;

public abstract class GuiContainer extends GuiScreen {
	private static RenderItem itemRenderer = new RenderItem();
	private ItemStack itemStack = null;
	protected int xSize = 176;
	protected int ySize = 166;
	protected List inventorySlots = new ArrayList();

	public final void drawScreen(int var1, int var2) {
		drawGradientRect(0, 0, this.width, this.height, 1610941696, -1607454624);
		int var3 = (this.width - this.xSize) / 2;
		int var4 = (this.height - this.ySize) / 2;
		this.drawGuiContainerBackgroundLayer();
		GL11.glPushMatrix();
		GL11.glRotatef(180.0F, 1.0F, 0.0F, 0.0F);
		RenderHelper.enableStandardItemLighting();
		GL11.glPopMatrix();
		GL11.glPushMatrix();
		GL11.glTranslatef((float)var3, (float)var4, 0.0F);
		GL11.glEnable(GL11.GL_NORMALIZE);
		GL11.glEnable(GL11.GL_COLOR_MATERIAL);
		GL11.glPushMatrix();
		GL11.glTranslatef(52.0F, 73.0F, 24.0F);
		GL11.glScalef(24.0F, -24.0F, 24.0F);
		GL11.glRotatef(10.0F, 0.0F, 1.0F, 0.0F);
		GL11.glRotatef(10.0F, 1.0F, 0.0F, 0.0F);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.mc.renderGlobal.renderManager.renderEntityWithPosYaw(this.mc.thePlayer, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F);
		GL11.glPopMatrix();
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GL11.glEnable(GL11.GL_NORMALIZE);

		for(int var5 = 0; var5 < this.inventorySlots.size(); ++var5) {
			Slot var6 = (Slot)this.inventorySlots.get(var5);
			int var11 = var6.yPos;
			int var10 = var6.xPos;
			int var9 = var6.slotIndex;
			IInventory var8 = var6.inventory;
			ItemStack var13 = var8.getStackInSlot(var9);
			itemRenderer.a(this.mc.renderEngine, var13, var10, var11);
			itemRenderer.a(this.fontRenderer, var13, var10, var11);
			if(var6.isAtCursorPos(var1, var2)) {
				GL11.glDisable(GL11.GL_LIGHTING);
				GL11.glDisable(GL11.GL_DEPTH_TEST);
				int var7 = var6.xPos;
				int var12 = var6.yPos;
				drawGradientRect(var7, var12, var7 + 16, var12 + 16, -2130706433, -2130706433);
				GL11.glEnable(GL11.GL_LIGHTING);
				GL11.glEnable(GL11.GL_DEPTH_TEST);
			}
		}

		if(this.itemStack != null) {
			GL11.glTranslatef(0.0F, 0.0F, 32.0F);
			itemRenderer.a(this.mc.renderEngine, this.itemStack, var1 - var3 - 8, var2 - var4 - 8);
			itemRenderer.a(this.fontRenderer, this.itemStack, var1 - var3 - 8, var2 - var4 - 8);
		}

		GL11.glDisable(GL11.GL_NORMALIZE);
		RenderHelper.disableStandardItemLighting();
		GL11.glDisable(GL11.GL_LIGHTING);
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		this.drawGuiContainerForegroundLayer();
		GL11.glEnable(GL11.GL_LIGHTING);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glPopMatrix();
	}

	protected void drawGuiContainerForegroundLayer() {
	}

	protected abstract void drawGuiContainerBackgroundLayer();

	protected final void drawSlotInventory(int var1, int var2, int var3) {
		if(var3 == 0 || var3 == 1) {
			int var6 = var2;
			int var4 = var1;
			GuiContainer var5 = this;
			int var7 = 0;

			Slot var10000;
			while(true) {
				if(var7 >= var5.inventorySlots.size()) {
					var10000 = null;
					break;
				}

				Slot var8 = (Slot)var5.inventorySlots.get(var7);
				if(var8.isAtCursorPos(var4, var6)) {
					var10000 = var8;
					break;
				}

				++var7;
			}

			Slot var11 = var10000;
			if(var11 != null) {
				ItemStack var12 = var11.inventory.getStackInSlot(var11.slotIndex);
				if(var12 == null && this.itemStack == null) {
					return;
				}

				if(var12 != null && this.itemStack == null) {
					var6 = var3 == 0 ? var12.stackSize : 1;
					this.itemStack = var11.inventory.decrStackSize(var11.slotIndex, var6);
					if(var12.stackSize == 0) {
						var11.putStack((ItemStack)null);
					}

					var11.onPickupFromSlot();
				} else if(var12 == null && this.itemStack != null && var11.isItemValid()) {
					var6 = var3 == 0 ? this.itemStack.stackSize : 1;
					if(var6 > var11.inventory.getInventoryStackLimit()) {
						var6 = var11.inventory.getInventoryStackLimit();
					}

					var11.putStack(this.itemStack.splitStack(var6));
					if(this.itemStack.stackSize == 0) {
						this.itemStack = null;
					}
				} else {
					if(var12 == null || this.itemStack == null || !var11.isItemValid()) {
						return;
					}

					if(var12.itemID != this.itemStack.itemID) {
						if(this.itemStack.stackSize > var11.inventory.getInventoryStackLimit()) {
							return;
						}

						var11.putStack(this.itemStack);
						this.itemStack = var12;
					} else {
						if(var12.itemID != this.itemStack.itemID) {
							return;
						}

						ItemStack var9;
						if(var3 != 0) {
							if(var3 == 1) {
								var6 = 1;
								if(1 > var11.inventory.getInventoryStackLimit() - var12.stackSize) {
									var6 = var11.inventory.getInventoryStackLimit() - var12.stackSize;
								}

								var9 = this.itemStack;
								if(var6 > var9.getItem().c() - var12.stackSize) {
									var9 = this.itemStack;
									var6 = var9.getItem().c() - var12.stackSize;
								}

								this.itemStack.splitStack(var6);
								if(this.itemStack.stackSize == 0) {
									this.itemStack = null;
								}

								var12.stackSize += var6;
							}

							return;
						}

						var6 = this.itemStack.stackSize;
						if(var6 > var11.inventory.getInventoryStackLimit() - var12.stackSize) {
							var6 = var11.inventory.getInventoryStackLimit() - var12.stackSize;
						}

						var9 = this.itemStack;
						if(var6 > var9.getItem().c() - var12.stackSize) {
							var9 = this.itemStack;
							var6 = var9.getItem().c() - var12.stackSize;
						}

						this.itemStack.splitStack(var6);
						if(this.itemStack.stackSize == 0) {
							this.itemStack = null;
						}

						var12.stackSize += var6;
					}
				}
			} else if(this.itemStack != null) {
				int var13 = (this.width - this.xSize) / 2;
				var6 = (this.height - this.ySize) / 2;
				if(var1 < var13 || var2 < var6 || var1 >= var13 + this.xSize || var2 >= var6 + this.xSize) {
					EntityPlayerSP var10 = this.mc.thePlayer;
					if(var3 == 0) {
						var10.dropPlayerItemWithRandomChoice(this.itemStack);
						this.itemStack = null;
					}

					if(var3 == 1) {
						var10.dropPlayerItemWithRandomChoice(this.itemStack.splitStack(1));
						if(this.itemStack.stackSize == 0) {
							this.itemStack = null;
						}
					}
				}
			}
		}

	}

	protected final void keyTyped(char var1, int var2) {
		if(var2 == 1 || var2 == this.mc.options.keyBindInventory.keyCode) {
			this.mc.displayGuiScreen((GuiScreen)null);
		}

	}

	public void onGuiClosed() {
		if(this.itemStack != null) {
			this.mc.thePlayer.dropPlayerItemWithRandomChoice(this.itemStack);
		}

	}

	public void guiCraftingItemsCheck() {
	}
}
