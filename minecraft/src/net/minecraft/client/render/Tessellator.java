package net.minecraft.client.render;

import java.nio.IntBuffer;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;

public final class Tessellator {
	private IntBuffer byteBuffer = BufferUtils.createIntBuffer(2097152);
	private int[] rawBuffer = new int[2097152];
	private int vertexCount = 0;
	private float textureU;
	private float textureV;
	private int color;
	private boolean hasColor = false;
	private boolean hasTexture = false;
	private int colors = 3;
	private int addedVertices = 0;
	private int rawBufferIndex = 0;
	private boolean drawMode = false;
	public static Tessellator instance = new Tessellator();

	public final void draw() {
		if(this.vertexCount > 0) {
			this.byteBuffer.clear();
			this.byteBuffer.put(this.rawBuffer, 0, this.addedVertices);
			this.byteBuffer.flip();
			if(this.hasTexture && this.hasColor) {
				GL11.glInterleavedArrays(GL11.GL_T2F_C4UB_V3F, 0, (IntBuffer)this.byteBuffer);
			} else if(this.hasTexture) {
				GL11.glInterleavedArrays(GL11.GL_T2F_V3F, 0, (IntBuffer)this.byteBuffer);
			} else if(this.hasColor) {
				GL11.glInterleavedArrays(GL11.GL_C4UB_V3F, 0, (IntBuffer)this.byteBuffer);
			} else {
				GL11.glInterleavedArrays(GL11.GL_V3F, 0, (IntBuffer)this.byteBuffer);
			}

			GL11.glEnableClientState(GL11.GL_VERTEX_ARRAY);
			if(this.hasTexture) {
				GL11.glEnableClientState(GL11.GL_TEXTURE_COORD_ARRAY);
			}

			if(this.hasColor) {
				GL11.glEnableClientState(GL11.GL_COLOR_ARRAY);
			}

			GL11.glDrawArrays(GL11.GL_QUADS, GL11.GL_POINTS, this.vertexCount);
			GL11.glDisableClientState(GL11.GL_VERTEX_ARRAY);
			if(this.hasTexture) {
				GL11.glDisableClientState(GL11.GL_TEXTURE_COORD_ARRAY);
			}

			if(this.hasColor) {
				GL11.glDisableClientState(GL11.GL_COLOR_ARRAY);
			}
		}

		this.reset();
		if(this.hasColor) {
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		}

	}

	private void reset() {
		this.vertexCount = 0;
		this.byteBuffer.clear();
		this.addedVertices = 0;
		this.rawBufferIndex = 0;
		this.colors = 3;
	}

	public final void startDrawingQuads() {
		if(this.vertexCount > 0) {
			new RuntimeException("OMG ALREADY VERTICES!");
		}

		this.reset();
		this.hasColor = false;
		this.hasTexture = false;
		this.drawMode = false;
	}

	public final void setColorOpaque_F(float var1, float var2, float var3) {
		this.setColorOpaque((int)(var1 * 255.0F), (int)(var2 * 255.0F), (int)(var3 * 255.0F));
	}

	private void setColorOpaque(int var1, int var2, int var3) {
		if(!this.drawMode) {
			if(!this.hasColor) {
				++this.colors;
			}

			if(var1 > 255) {
				var1 = 255;
			}

			if(var2 > 255) {
				var2 = 255;
			}

			if(var3 > 255) {
				var3 = 255;
			}

			if(var1 < 0) {
				var1 = 0;
			}

			if(var2 < 0) {
				var2 = 0;
			}

			if(var3 < 0) {
				var3 = 0;
			}

			this.hasColor = true;
			this.color = -16777216 | var3 << 16 | var2 << 8 | var1;
		}
	}

	public final void addVertexWithUV(float var1, float var2, float var3, float var4, float var5) {
		if(!this.hasTexture) {
			this.colors += 2;
		}

		this.hasTexture = true;
		this.textureU = var4;
		this.textureV = var5;
		this.addVertex(var1, var2, var3);
	}

	public final void addVertex(float var1, float var2, float var3) {
		++this.rawBufferIndex;
		if(this.hasTexture) {
			this.rawBuffer[this.addedVertices++] = Float.floatToRawIntBits(this.textureU);
			this.rawBuffer[this.addedVertices++] = Float.floatToRawIntBits(this.textureV);
		}

		if(this.hasColor) {
			this.rawBuffer[this.addedVertices++] = this.color;
		}

		this.rawBuffer[this.addedVertices++] = Float.floatToRawIntBits(var1);
		this.rawBuffer[this.addedVertices++] = Float.floatToRawIntBits(var2);
		this.rawBuffer[this.addedVertices++] = Float.floatToRawIntBits(var3);
		++this.vertexCount;
		if(this.vertexCount % 4 == 0 && this.addedVertices >= 2097152 - (this.colors << 2)) {
			this.draw();
		}

	}

	public final void setColorOpaque_I(int var1) {
		int var2 = var1 >> 16 & 255;
		int var3 = var1 >> 8 & 255;
		var1 &= 255;
		this.setColorOpaque(var2, var3, var1);
	}

	public final void disableColor() {
		this.drawMode = true;
	}

	public static void setNormal(float var0, float var1, float var2) {
		GL11.glNormal3f(var0, var1, var2);
	}
}
