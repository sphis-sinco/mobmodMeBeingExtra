package net.minecraft.client;

import java.awt.AWTException;
import java.awt.Component;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Robot;
import java.nio.IntBuffer;
import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Cursor;
import org.lwjgl.input.Mouse;

public final class MouseHelper {
	private Component c;
	private Robot d;
	private int e;
	private int f;
	private Cursor g;
	public int a;
	public int b;
	private int h = 10;

	public MouseHelper(Component var1) {
		this.c = var1;

		try {
			this.d = new Robot();
		} catch (AWTException var4) {
			var4.printStackTrace();
		}

		IntBuffer var5 = BufferUtils.createIntBuffer(1);
		var5.put(0);
		var5.flip();
		IntBuffer var2 = BufferUtils.createIntBuffer(1024);

		try {
			this.g = new Cursor(32, 32, 16, 16, 1, var2, var5);
		} catch (LWJGLException var3) {
			var3.printStackTrace();
		}
	}

	public final void a() {
		try {
			Mouse.setNativeCursor(this.g);
		} catch (LWJGLException var2) {
			var2.printStackTrace();
		}

		this.b();
		this.a = 0;
		this.b = 0;
	}

	public final void b() {
		Point var1 = MouseInfo.getPointerInfo().getLocation();
		Point var2 = this.c.getLocationOnScreen();
		this.d.mouseMove(this.e, this.f);
		this.e = var2.x + this.c.getWidth() / 2;
		this.f = var2.y + this.c.getHeight() / 2;
		if(this.h == 0) {
			this.a = var1.x - this.e;
			this.b = var1.y - this.f;
		} else {
			this.a = this.b = 0;
			--this.h;
		}
	}
}
