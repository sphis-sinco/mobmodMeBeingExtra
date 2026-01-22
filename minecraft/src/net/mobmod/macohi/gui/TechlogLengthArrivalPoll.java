package net.mobmod.macohi.gui;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiSmallButton;

public final class TechlogLengthArrivalPoll extends GuiScreen {
	private GuiScreen prevGui;

	public TechlogLengthArrivalPoll(GuiScreen var1) {
		this.prevGui = var1;
	}

	public final void initGui() {
		this.controlList.clear();
		
		this.controlList.add(new GuiSmallButton(1, this.width / 2 - 200, this.height / 4 + 48, "Longer, less frequent"));
		this.controlList.add(new GuiSmallButton(1, this.width / 2 + 50, this.height / 4 + 48, "Shorter, more frequent"));
		
		this.controlList.add(new GuiButton(0, this.width / 2 - 100, this.height / 4 + 48 + (24 * 4), "Back"));
	}

	protected final void actionPerformed(GuiButton var1) {
		if (var1.id == 0)
		{
			this.mc.displayGuiScreen(this.prevGui);
		}
	}

	public final void drawScreen(int var1, int var2) {
		drawGradientRect(0, 0, this.width, this.height, 1610941696, -1607454624);
		drawCenteredString(this.fontRenderer, "TECHLOG LENGTH AND ARRIVAL POLL", this.width / 2, 40, 16777215);
		super.drawScreen(var1, var2);
	}
}
