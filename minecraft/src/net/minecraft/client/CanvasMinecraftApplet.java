package net.minecraft.client;

import java.awt.Canvas;

final class CanvasMinecraftApplet extends Canvas {
	private MinecraftApplet applet;

	CanvasMinecraftApplet(MinecraftApplet var1) {
		this.applet = var1;
	}

	public final synchronized void addNotify() {
		super.addNotify();
		this.applet.startMainThread();
	}

	public final synchronized void removeNotify() {
		this.applet.shutdown();
		super.removeNotify();
	}
}
