package net.minecraft.client.render;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.awt.image.ImageObserver;
import java.io.File;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Random;
import javax.imageio.ImageIO;
import net.minecraft.client.Minecraft;
import net.minecraft.client.RenderHelper;
import net.minecraft.client.controller.PlayerControllerCreative;
import net.minecraft.client.effect.EffectRenderer;
import net.minecraft.client.effect.EntityRainFX;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.player.EntityPlayerSP;
import net.minecraft.game.entity.Entity;
import net.minecraft.game.level.World;
import net.minecraft.game.level.block.Block;
import net.minecraft.game.level.material.Material;
import net.minecraft.game.physics.AxisAlignedBB;
import net.minecraft.game.physics.MovingObjectPosition;
import net.minecraft.game.physics.Vec3D;
import org.lwjgl.BufferUtils;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;
import util.MathHelper;

public final class EntityRenderer {
	private Minecraft mc;
	private float fogColorMultiplier = 1.0F;
	private boolean anaglyphEnable = false;
	private float farPlaneDistance = 0.0F;
	public ItemRenderer itemRenderer;
	private int rendererUpdateCount;
	private Entity pointedEntity = null;
	private int entityRendererInt1;
	private int entityRendererInt2;
	private DecimalFormat entityDecimalFormat = new DecimalFormat("0000");
	private ByteBuffer entityByteBuffer;
	private FloatBuffer entityFloatBuffer = BufferUtils.createFloatBuffer(16);
	private Random random = new Random();
	private volatile int unusedInt0 = 0;
	private volatile int unusedInt1 = 0;
	private FloatBuffer fogColorBuffer = BufferUtils.createFloatBuffer(16);
	private float fogColorRed;
	private float fogColorGreen;
	private float fogColorBlue;
	private float prevFogColor;
	private float fogColor;

	public EntityRenderer(Minecraft var1) {
		this.mc = var1;
		this.itemRenderer = new ItemRenderer(var1);
	}

	public final void updateRenderer() {
		this.prevFogColor = this.fogColor;
		float var1 = this.mc.theWorld.getBlockLightValue((int)this.mc.thePlayer.posX, (int)this.mc.thePlayer.posY, (int)this.mc.thePlayer.posZ);
		float var2 = (float)(3 - this.mc.options.renderDistance) / 3.0F;
		var1 = var1 * (1.0F - var2) + var2;
		this.fogColor += (var1 - this.fogColor) * 0.1F;
		++this.rendererUpdateCount;
		this.itemRenderer.updateEquippedItem();
		if(this.mc.thirdPersonView) {
			EntityRenderer var13 = this;
			EntityPlayerSP var14 = this.mc.thePlayer;
			World var3 = this.mc.theWorld;
			int var4 = (int)var14.posX;
			int var5 = (int)var14.posY;
			int var15 = (int)var14.posZ;

			for(int var6 = 0; var6 < 50; ++var6) {
				int var7 = var4 + var13.random.nextInt(9) - 4;
				int var8 = var15 + var13.random.nextInt(9) - 4;
				int var9 = var3.getMapHeight(var7, var8);
				int var10 = var3.getBlockId(var7, var9 - 1, var8);
				if(var9 <= var5 + 4 && var9 >= var5 - 4) {
					float var11 = var13.random.nextFloat();
					float var12 = var13.random.nextFloat();
					if(var10 > 0) {
						var13.mc.effectRenderer.addEffect(new EntityRainFX(var3, (float)var7 + var11, (float)var9 + 0.1F - Block.blocksList[var10].minY, (float)var8 + var12));
					}
				}
			}
		}

	}

	private Vec3D orientCamera(float var1) {
		EntityPlayerSP var2 = this.mc.thePlayer;
		float var3 = var2.prevPosX + (var2.posX - var2.prevPosX) * var1;
		float var4 = var2.prevPosY + (var2.posY - var2.prevPosY) * var1;
		var1 = var2.prevPosZ + (var2.posZ - var2.prevPosZ) * var1;
		return new Vec3D(var3, var4, var1);
	}

	private void hurtCameraEffect(float var1) {
		EntityPlayerSP var2 = this.mc.thePlayer;
		float var3 = (float)var2.hurtTime - var1;
		if(var2.health <= 0) {
			var1 += (float)var2.deathTime;
			GL11.glRotatef(40.0F - 8000.0F / (var1 + 200.0F), 0.0F, 0.0F, 1.0F);
		}

		if(var3 >= 0.0F) {
			var3 /= (float)var2.maxHurtTime;
			var3 = MathHelper.sin(var3 * var3 * var3 * var3 * (float)Math.PI);
			var1 = var2.attackedAtYaw;
			GL11.glRotatef(-var1, 0.0F, 1.0F, 0.0F);
			GL11.glRotatef(-var3 * 14.0F, 0.0F, 0.0F, 1.0F);
			GL11.glRotatef(var1, 0.0F, 1.0F, 0.0F);
		}
	}

	private void setupViewBobbing(float var1) {
		EntityPlayerSP var2 = this.mc.thePlayer;
		float var3 = var2.distanceWalkedModified - var2.prevDistanceWalkedModified;
		var3 = var2.distanceWalkedModified + var3 * var1;
		float var4 = var2.prevCameraYaw + (var2.cameraYaw - var2.prevCameraYaw) * var1;
		var1 = var2.prevCameraPitch + (var2.cameraPitch - var2.prevCameraPitch) * var1;
		GL11.glTranslatef(MathHelper.sin(var3 * (float)Math.PI) * var4 * 0.5F, -Math.abs(MathHelper.cos(var3 * (float)Math.PI) * var4), 0.0F);
		GL11.glRotatef(MathHelper.sin(var3 * (float)Math.PI) * var4 * 3.0F, 0.0F, 0.0F, 1.0F);
		GL11.glRotatef(Math.abs(MathHelper.cos(var3 * (float)Math.PI + 0.2F) * var4) * 5.0F, 1.0F, 0.0F, 0.0F);
		GL11.glRotatef(var1, 1.0F, 0.0F, 0.0F);
	}

	public final void updateCameraAndRender(float var1) {
		if(this.anaglyphEnable && !Display.isActive()) {
			this.mc.displayInGameMenu();
		}

		this.anaglyphEnable = Display.isActive();
		int var5;
		int var6;
		if(this.mc.inventoryScreen) {
			Mouse.getDX();
			byte var2 = 0;
			Mouse.getDY();
			byte var3 = 0;
			this.mc.mouseHelper.b();
			byte var4 = 1;
			if(this.mc.options.invertMouse) {
				var4 = -1;
			}

			var5 = var2 + this.mc.mouseHelper.a;
			var6 = var3 - this.mc.mouseHelper.b;
			if(var2 != 0 || this.entityRendererInt1 != 0) {
				System.out.println("xxo: " + var2 + ", " + this.entityRendererInt1 + ": " + this.entityRendererInt1 + ", xo: " + var5);
			}

			if(this.entityRendererInt1 != 0) {
				this.entityRendererInt1 = 0;
			}

			if(this.entityRendererInt2 != 0) {
				this.entityRendererInt2 = 0;
			}

			if(var2 != 0) {
				this.entityRendererInt1 = var2;
			}

			if(var3 != 0) {
				this.entityRendererInt2 = var3;
			}

			float var10001 = (float)var5;
			float var11 = (float)(var6 * var4);
			float var9 = var10001;
			EntityPlayerSP var7 = this.mc.thePlayer;
			float var13 = var7.rotationPitch;
			float var14 = var7.rotationYaw;
			var7.rotationYaw = (float)((double)var7.rotationYaw + (double)var9 * 0.15D);
			var7.rotationPitch = (float)((double)var7.rotationPitch - (double)var11 * 0.15D);
			if(var7.rotationPitch < -90.0F) {
				var7.rotationPitch = -90.0F;
			}

			if(var7.rotationPitch > 90.0F) {
				var7.rotationPitch = 90.0F;
			}

			var7.prevRotationPitch += var7.rotationPitch - var13;
			var7.prevRotationYaw += var7.rotationYaw - var14;
		}

		ScaledResolution var8 = new ScaledResolution(this.mc.displayWidth, this.mc.displayHeight);
		int var10 = var8.getScaledWidth();
		int var12 = var8.getScaledHeight();
		var5 = Mouse.getX() * var10 / this.mc.displayWidth;
		var6 = var12 - Mouse.getY() * var12 / this.mc.displayHeight - 1;
		if(this.mc.theWorld != null) {
			this.e(var1);
			this.mc.ingameGUI.renderGameOverlay(var1);
		} else {
			GL11.glViewport(0, 0, this.mc.displayWidth, this.mc.displayHeight);
			GL11.glClearColor(0.0F, 0.0F, 0.0F, 0.0F);
			GL11.glClear(GL11.GL_DEPTH_BUFFER_BIT | GL11.GL_COLOR_BUFFER_BIT);
			GL11.glMatrixMode(GL11.GL_PROJECTION);
			GL11.glLoadIdentity();
			GL11.glMatrixMode(GL11.GL_MODELVIEW);
			GL11.glLoadIdentity();
			this.setupOverlayRendering();
		}

		if(this.mc.currentScreen != null) {
			GL11.glClear(GL11.GL_DEPTH_BUFFER_BIT);
			this.mc.currentScreen.drawScreen(var5, var6);
		}

		Thread.yield();
		Display.update();
	}

	public final void grabLargeScreenshot() {
		this.mc.loadingScreen.displayProgressMessage("Grabbing large screenshot");
		File var1 = new File(System.getProperty("user.home", "."));
		int var2 = 0;

		while(true) {
			File var3 = new File(var1, "mc_map_" + this.entityDecimalFormat.format((long)var2) + ".png");
			if(!var3.exists()) {
				var3 = var3.getAbsoluteFile();
				this.mc.loadingScreen.displayLoadingString("Rendering");
				this.mc.loadingScreen.setLoadingProgress(0);

				try {
					int var19 = (this.mc.theWorld.width << 4) + (this.mc.theWorld.length << 4);
					var2 = (this.mc.theWorld.height << 4) + var19 / 2;
					BufferedImage var4 = new BufferedImage(var19, var2, 1);
					Graphics var5 = var4.getGraphics();
					int var6 = this.mc.displayWidth;
					int var7 = this.mc.displayHeight;
					int var8 = (var19 / var6 + 1) * (var2 / var7 + 1);
					int var9 = 0;

					for(int var10 = 0; var10 < var19; var10 += var6) {
						for(int var11 = 0; var11 < var2; var11 += var7) {
							++var9;
							this.mc.loadingScreen.setLoadingProgress(var9 * 100 / var8);
							int var10001 = var10 - var19 / 2;
							int var10002 = var11 - var2 / 2;
							float var12 = 0.0F;
							int var14 = var10002;
							int var13 = var10001;
							if(this.entityByteBuffer == null) {
								this.entityByteBuffer = BufferUtils.createByteBuffer(this.mc.displayWidth * this.mc.displayHeight << 2);
							}

							EntityPlayerSP var15 = this.mc.thePlayer;
							World var16 = this.mc.theWorld;
							RenderGlobal var17 = this.mc.renderGlobal;
							GL11.glViewport(0, 0, this.mc.displayWidth, this.mc.displayHeight);
							this.updateFogColor(0.0F);
							GL11.glClear(GL11.GL_DEPTH_BUFFER_BIT | GL11.GL_COLOR_BUFFER_BIT);
							this.fogColorMultiplier = 1.0F;
							GL11.glEnable(GL11.GL_CULL_FACE);
							this.farPlaneDistance = (float)(512 >> (this.mc.options.renderDistance << 1));
							GL11.glMatrixMode(GL11.GL_PROJECTION);
							GL11.glLoadIdentity();
							GL11.glOrtho(0.0D, (double)this.mc.displayWidth, 0.0D, (double)this.mc.displayHeight, 10.0D, 10000.0D);
							GL11.glMatrixMode(GL11.GL_MODELVIEW);
							GL11.glLoadIdentity();
							GL11.glTranslatef((float)(-var13), (float)(-var14), -5000.0F);
							GL11.glScalef(16.0F, -16.0F, -16.0F);
							float var22 = 1.0F;
							var22 = 0.0F;
							var22 = 0.0F;
							var22 = 0.0F;
							var22 = 0.0F;
							var22 = 0.0F;
							var22 = 0.5F;
							var22 = 1.0F;
							var22 = 0.0F;
							var22 = -1.0F;
							var22 = 1.0F;
							var22 = 0.0F;
							var22 = 0.0F;
							var22 = 0.0F;
							var22 = -0.5F;
							var22 = 1.0F;
							this.entityFloatBuffer.clear();
							this.entityFloatBuffer.put(1.0F).put(-0.5F).put(0.0F).put(0.0F);
							this.entityFloatBuffer.put(0.0F).put(1.0F).put(-1.0F).put(0.0F);
							this.entityFloatBuffer.put(1.0F).put(0.5F).put(0.0F).put(0.0F);
							this.entityFloatBuffer.put(0.0F).put(0.0F).put(0.0F).put(1.0F);
							this.entityFloatBuffer.flip();
							GL11.glMultMatrix(this.entityFloatBuffer);
							GL11.glRotatef(0.0F, 0.0F, 1.0F, 0.0F);
							GL11.glTranslatef((float)(-var16.width) / 2.0F, (float)(-var16.height) / 2.0F, (float)(-var16.length) / 2.0F);
							Frustrum var24 = ClippingHelperImplementation.init();
							this.mc.renderGlobal.clipRenderersByFrustrum(var24);
							this.mc.renderGlobal.updateRenderers(var15);
							this.setupFog();
							GL11.glEnable(GL11.GL_FOG);
							GL11.glFogi(GL11.GL_FOG_MODE, GL11.GL_LINEAR);
							float var23 = (float)var16.height * 8.0F;
							GL11.glFogf(GL11.GL_FOG_START, 5000.0F - var23);
							GL11.glFogf(GL11.GL_FOG_END, 5000.0F + var23 * 8.0F);
							RenderHelper.enableStandardItemLighting();
							var17.renderEntities(this.orientCamera(0.0F), var24, 0.0F);
							RenderHelper.disableStandardItemLighting();
							GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.mc.renderEngine.getTexture("/terrain.png"));
							var17.sortAndRender(var15, 0);
							var17.oobGroundRenderer();
							if(var16.cloudHeight < var16.height) {
								var17.renderSky(0.0F);
							}

							GL11.glEnable(GL11.GL_BLEND);
							GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
							GL11.glColorMask(false, false, false, false);
							var13 = var17.sortAndRender(var15, 1);
							GL11.glColorMask(true, true, true, true);
							if(var13 > 0) {
								var17.renderAllRenderLists();
							}

							if(var16.getGroundLevel() >= 0) {
								var17.oobWaterRenderer();
							}

							GL11.glDepthMask(true);
							GL11.glDisable(GL11.GL_BLEND);
							GL11.glDisable(GL11.GL_FOG);
							this.entityByteBuffer.clear();
							GL11.glPixelStorei(GL11.GL_PACK_ALIGNMENT, 1);
							GL11.glReadPixels(0, 0, this.mc.displayWidth, this.mc.displayHeight, GL11.GL_RGB, GL11.GL_UNSIGNED_BYTE, (ByteBuffer)this.entityByteBuffer);
							BufferedImage var21 = screenshotBuffer(this.entityByteBuffer, var6, var7);
							var5.drawImage(var21, var10, var11, (ImageObserver)null);
						}
					}

					var5.dispose();
					this.mc.loadingScreen.displayLoadingString("Saving as " + var3.toString());
					this.mc.loadingScreen.setLoadingProgress(100);
					FileOutputStream var20 = new FileOutputStream(var3);
					ImageIO.write(var4, "png", var20);
					var20.close();
					return;
				} catch (Throwable var18) {
					var18.printStackTrace();
					return;
				}
			}

			++var2;
		}
	}

	private static BufferedImage screenshotBuffer(ByteBuffer var0, int var1, int var2) {
		var0.position(0).limit(var1 * var2 << 2);
		BufferedImage var3 = new BufferedImage(var1, var2, 1);
		int[] var4 = ((DataBufferInt)var3.getRaster().getDataBuffer()).getData();

		for(int var5 = 0; var5 < var1 * var2; ++var5) {
			int var6 = var0.get(var5 * 3) & 255;
			int var7 = var0.get(var5 * 3 + 1) & 255;
			int var8 = var0.get(var5 * 3 + 2) & 255;
			var4[var5] = var6 << 16 | var7 << 8 | var8;
		}

		return var3;
	}

	private void e(float var1) {
		EntityRenderer var7 = this;
		EntityPlayerSP var9 = this.mc.thePlayer;
		float var3 = var9.prevRotationPitch + (var9.rotationPitch - var9.prevRotationPitch) * var1;
		float var10 = var9.prevRotationYaw + (var9.rotationYaw - var9.prevRotationYaw) * var1;
		Vec3D var11 = this.orientCamera(var1);
		float var12 = MathHelper.cos(-var10 * ((float)Math.PI / 180.0F) - (float)Math.PI);
		float var13 = MathHelper.sin(-var10 * ((float)Math.PI / 180.0F) - (float)Math.PI);
		var10 = MathHelper.cos(-var3 * ((float)Math.PI / 180.0F));
		float var14 = MathHelper.sin(-var3 * ((float)Math.PI / 180.0F));
		float var4 = var13 * var10;
		float var6 = var12 * var10;
		float var15 = this.mc.playerController.getBlockReachDistance();
		Vec3D var2 = var11.addVector(var4 * var15, var14 * var15, var6 * var15);
		this.mc.objectMouseOver = this.mc.theWorld.rayTraceBlocks(var11, var2);
		float var5 = var15;
		var11 = this.orientCamera(var1);
		if(this.mc.objectMouseOver != null) {
			var5 = this.mc.objectMouseOver.hitVec.squaredDistanceTo(var11);
		}

		if(this.mc.playerController instanceof PlayerControllerCreative) {
			var15 = 32.0F;
		} else {
			if(var5 > 3.0F) {
				var5 = 3.0F;
			}

			var15 = var5;
		}

		var2 = var11.addVector(var4 * var15, var14 * var15, var6 * var15);
		this.pointedEntity = null;
		List var16 = this.mc.theWorld.entityMap.getEntitiesWithinAABBExcludingEntity(var9, var9.boundingBox.addCoord(var4 * var15, var14 * var15, var6 * var15));
		float var17 = 0.0F;

		for(int var18 = 0; var18 < var16.size(); ++var18) {
			Entity var20 = (Entity)var16.get(var18);
			if(var20.canBeCollidedWith()) {
				AxisAlignedBB var23 = var20.boundingBox.expand(0.1F, 0.1F, 0.1F);
				MovingObjectPosition var24 = var23.calculateIntercept(var11, var2);
				if(var24 != null) {
					var4 = var11.squaredDistanceTo(var24.hitVec);
					if(var4 < var17 || var17 == 0.0F) {
						var7.pointedEntity = var20;
						var17 = var4;
					}
				}
			}
		}

		if(var7.pointedEntity != null && !(var7.mc.playerController instanceof PlayerControllerCreative)) {
			var7.mc.objectMouseOver = new MovingObjectPosition(var7.pointedEntity);
		}

		for(int var19 = 0; var19 < 2; ++var19) {
			if(this.mc.options.anaglyph) {
				if(var19 == 0) {
					GL11.glColorMask(false, true, true, false);
				} else {
					GL11.glColorMask(true, false, false, false);
				}
			}

			EntityPlayerSP var21 = this.mc.thePlayer;
			World var25 = this.mc.theWorld;
			RenderGlobal var28 = this.mc.renderGlobal;
			EffectRenderer var27 = this.mc.effectRenderer;
			GL11.glViewport(0, 0, this.mc.displayWidth, this.mc.displayHeight);
			this.updateFogColor(var1);
			GL11.glClear(GL11.GL_DEPTH_BUFFER_BIT | GL11.GL_COLOR_BUFFER_BIT);
			this.fogColorMultiplier = 1.0F;
			GL11.glEnable(GL11.GL_CULL_FACE);
			this.farPlaneDistance = (float)(512 >> (this.mc.options.renderDistance << 1));
			GL11.glMatrixMode(GL11.GL_PROJECTION);
			GL11.glLoadIdentity();
			if(this.mc.options.anaglyph) {
				GL11.glTranslatef((float)(-((var19 << 1) - 1)) * 0.07F, 0.0F, 0.0F);
			}

			EntityPlayerSP var37 = this.mc.thePlayer;
			var13 = 70.0F;
			if(var37.isInsideOfMaterial()) {
				var13 = 60.0F;
			}

			if(var37.health <= 0) {
				var10 = (float)var37.deathTime + var1;
				var13 /= (1.0F - 500.0F / (var10 + 500.0F)) * 2.0F + 1.0F;
			}

			GLU.gluPerspective(var13, (float)this.mc.displayWidth / (float)this.mc.displayHeight, 0.05F, this.farPlaneDistance);
			GL11.glMatrixMode(GL11.GL_MODELVIEW);
			GL11.glLoadIdentity();
			if(this.mc.options.anaglyph) {
				GL11.glTranslatef((float)((var19 << 1) - 1) * 0.1F, 0.0F, 0.0F);
			}

			this.hurtCameraEffect(var1);
			if(this.mc.options.viewBobbing) {
				this.setupViewBobbing(var1);
			}

			var37 = this.mc.thePlayer;
			GL11.glTranslatef(0.0F, 0.0F, -0.1F);
			GL11.glRotatef(var37.prevRotationPitch + (var37.rotationPitch - var37.prevRotationPitch) * var1, 1.0F, 0.0F, 0.0F);
			GL11.glRotatef(var37.prevRotationYaw + (var37.rotationYaw - var37.prevRotationYaw) * var1, 0.0F, 1.0F, 0.0F);
			var13 = var37.prevPosX + (var37.posX - var37.prevPosX) * var1;
			var10 = var37.prevPosY + (var37.posY - var37.prevPosY) * var1;
			var14 = var37.prevPosZ + (var37.posZ - var37.prevPosZ) * var1;
			GL11.glTranslatef(-var13, -var10, -var14);
			Frustrum var31 = ClippingHelperImplementation.init();
			this.mc.renderGlobal.clipRenderersByFrustrum(var31);
			this.mc.renderGlobal.updateRenderers(var21);
			this.setupFog();
			GL11.glEnable(GL11.GL_FOG);
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.mc.renderEngine.getTexture("/terrain.png"));
			var28.sortAndRender(var21, 0);
			int var8;
			int var34;
			int var38;
			int var39;
			int var42;
			if(var25.isSolid(var21.posX, var21.posY, var21.posZ, 0.1F)) {
				var8 = (int)var21.posX;
				int var33 = (int)var21.posY;
				var34 = (int)var21.posZ;
				RenderBlocks var35 = new RenderBlocks(Tessellator.instance, var25);

				for(var38 = var8 - 1; var38 <= var8 + 1; ++var38) {
					for(int var40 = var33 - 1; var40 <= var33 + 1; ++var40) {
						for(var39 = var34 - 1; var39 <= var34 + 1; ++var39) {
							var42 = var25.getBlockId(var38, var40, var39);
							if(var42 > 0) {
								var35.renderBlockAllFaces(Block.blocksList[var42], var38, var40, var39);
							}
						}
					}
				}
			}

			RenderHelper.enableStandardItemLighting();
			var28.renderEntities(this.orientCamera(var1), var31, var1);
			RenderHelper.disableStandardItemLighting();
			this.setupFog();
			var27.renderParticles(var21, var1);
			var28.oobGroundRenderer();
			this.setupFog();
			var28.renderSky(var1);
			this.setupFog();
			if(this.mc.objectMouseOver != null) {
				GL11.glDisable(GL11.GL_ALPHA_TEST);
				var28.drawBlockBreaking(this.mc.objectMouseOver, 0, var21.inventory.getCurrentItem());
				var28.drawSelectionBox(this.mc.objectMouseOver, 0);
				GL11.glEnable(GL11.GL_ALPHA_TEST);
			}

			GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
			this.setupFog();
			var28.oobWaterRenderer();
			GL11.glEnable(GL11.GL_BLEND);
			GL11.glDisable(GL11.GL_CULL_FACE);
			GL11.glColorMask(false, false, false, false);
			var8 = var28.sortAndRender(var21, 1);
			GL11.glColorMask(true, true, true, true);
			if(this.mc.options.anaglyph) {
				if(var19 == 0) {
					GL11.glColorMask(false, true, true, false);
				} else {
					GL11.glColorMask(true, false, false, false);
				}
			}

			if(var8 > 0) {
				var28.renderAllRenderLists();
			}

			GL11.glDepthMask(true);
			GL11.glEnable(GL11.GL_CULL_FACE);
			GL11.glDisable(GL11.GL_BLEND);
			GL11.glDisable(GL11.GL_FOG);
			if(this.mc.thirdPersonView) {
				float var32 = var1;
				var7 = this;
				var9 = this.mc.thePlayer;
				World var22 = this.mc.theWorld;
				var34 = (int)var9.posX;
				int var36 = (int)var9.posY;
				var38 = (int)var9.posZ;
				Tessellator var41 = Tessellator.instance;
				GL11.glDisable(GL11.GL_CULL_FACE);
				GL11.glNormal3f(0.0F, 1.0F, 0.0F);
				GL11.glEnable(GL11.GL_BLEND);
				GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
				GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.mc.renderEngine.getTexture("/rain.png"));
				var39 = var34 - 5;

				while(true) {
					if(var39 > var34 + 5) {
						GL11.glEnable(GL11.GL_CULL_FACE);
						GL11.glDisable(GL11.GL_BLEND);
						break;
					}

					for(int var26 = var38 - 5; var26 <= var38 + 5; ++var26) {
						int var30 = var22.getMapHeight(var39, var26);
						int var29 = var36 - 5;
						var42 = var36 + 5;
						if(var29 < var30) {
							var29 = var30;
						}

						if(var42 < var30) {
							var42 = var30;
						}

						if(var29 != var42) {
							var5 = ((float)((var7.rendererUpdateCount + var39 * 3121 + var26 * 418711) % 32) + var32) / 32.0F;
							float var43 = (float)var39 + 0.5F - var9.posX;
							var17 = (float)var26 + 0.5F - var9.posZ;
							float var44 = MathHelper.sqrt_float(var43 * var43 + var17 * var17) / 5.0F;
							GL11.glColor4f(1.0F, 1.0F, 1.0F, (1.0F - var44 * var44) * 0.7F);
							var41.startDrawingQuads();
							var41.addVertexWithUV((float)var39, (float)var29, (float)var26, 0.0F, (float)var29 * 2.0F / 8.0F + var5 * 2.0F);
							var41.addVertexWithUV((float)(var39 + 1), (float)var29, (float)(var26 + 1), 2.0F, (float)var29 * 2.0F / 8.0F + var5 * 2.0F);
							var41.addVertexWithUV((float)(var39 + 1), (float)var42, (float)(var26 + 1), 2.0F, (float)var42 * 2.0F / 8.0F + var5 * 2.0F);
							var41.addVertexWithUV((float)var39, (float)var42, (float)var26, 0.0F, (float)var42 * 2.0F / 8.0F + var5 * 2.0F);
							var41.addVertexWithUV((float)var39, (float)var29, (float)(var26 + 1), 0.0F, (float)var29 * 2.0F / 8.0F + var5 * 2.0F);
							var41.addVertexWithUV((float)(var39 + 1), (float)var29, (float)var26, 2.0F, (float)var29 * 2.0F / 8.0F + var5 * 2.0F);
							var41.addVertexWithUV((float)(var39 + 1), (float)var42, (float)var26, 2.0F, (float)var42 * 2.0F / 8.0F + var5 * 2.0F);
							var41.addVertexWithUV((float)var39, (float)var42, (float)(var26 + 1), 0.0F, (float)var42 * 2.0F / 8.0F + var5 * 2.0F);
							var41.draw();
						}
					}

					++var39;
				}
			}

			GL11.glClear(GL11.GL_DEPTH_BUFFER_BIT);
			GL11.glLoadIdentity();
			if(this.mc.options.anaglyph) {
				GL11.glTranslatef((float)((var19 << 1) - 1) * 0.1F, 0.0F, 0.0F);
			}

			GL11.glPushMatrix();
			this.hurtCameraEffect(var1);
			if(this.mc.options.viewBobbing) {
				this.setupViewBobbing(var1);
			}

			this.itemRenderer.renderItemInFirstPerson(var1);
			GL11.glPopMatrix();
			this.itemRenderer.b(var1);
			this.hurtCameraEffect(var1);
			if(this.mc.options.viewBobbing) {
				this.setupViewBobbing(var1);
			}

			if(!this.mc.options.anaglyph) {
				return;
			}
		}

		GL11.glColorMask(true, true, true, false);
	}

	public final void setupOverlayRendering() {
		ScaledResolution var1 = new ScaledResolution(this.mc.displayWidth, this.mc.displayHeight);
		int var2 = var1.getScaledWidth();
		int var3 = var1.getScaledHeight();
		GL11.glClear(GL11.GL_DEPTH_BUFFER_BIT);
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		GL11.glOrtho(0.0D, (double)var2, (double)var3, 0.0D, 1000.0D, 3000.0D);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		GL11.glLoadIdentity();
		GL11.glTranslatef(0.0F, 0.0F, -2000.0F);
	}

	private void updateFogColor(float var1) {
		World var2 = this.mc.theWorld;
		EntityPlayerSP var3 = this.mc.thePlayer;
		float var4 = 1.0F / (float)(4 - this.mc.options.renderDistance);
		var4 = 1.0F - (float)Math.pow((double)var4, 0.25D);
		float var5 = (float)(var2.skyColor >> 16 & 255) / 255.0F;
		float var6 = (float)(var2.skyColor >> 8 & 255) / 255.0F;
		float var7 = (float)(var2.skyColor & 255) / 255.0F;
		this.fogColorRed = (float)(var2.fogColor >> 16 & 255) / 255.0F;
		this.fogColorGreen = (float)(var2.fogColor >> 8 & 255) / 255.0F;
		this.fogColorBlue = (float)(var2.fogColor & 255) / 255.0F;
		this.fogColorRed += (var5 - this.fogColorRed) * var4;
		this.fogColorGreen += (var6 - this.fogColorGreen) * var4;
		this.fogColorBlue += (var7 - this.fogColorBlue) * var4;
		this.fogColorRed *= this.fogColorMultiplier;
		this.fogColorGreen *= this.fogColorMultiplier;
		this.fogColorBlue *= this.fogColorMultiplier;
		Block var8 = Block.blocksList[var2.getBlockId((int)var3.posX, (int)(var3.posY + 0.12F), (int)var3.posZ)];
		if(var8 != null && var8.getBlockMaterial() != Material.air) {
			Material var9 = var8.getBlockMaterial();
			if(var9 == Material.water) {
				this.fogColorRed = 0.02F;
				this.fogColorGreen = 0.02F;
				this.fogColorBlue = 0.2F;
			} else if(var9 == Material.lava) {
				this.fogColorRed = 0.6F;
				this.fogColorGreen = 0.1F;
				this.fogColorBlue = 0.0F;
			}
		}

		float var10 = this.prevFogColor + (this.fogColor - this.prevFogColor) * var1;
		this.fogColorRed *= var10;
		this.fogColorGreen *= var10;
		this.fogColorBlue *= var10;
		if(this.mc.options.anaglyph) {
			var1 = (this.fogColorRed * 30.0F + this.fogColorGreen * 59.0F + this.fogColorBlue * 11.0F) / 100.0F;
			var10 = (this.fogColorRed * 30.0F + this.fogColorGreen * 70.0F) / 100.0F;
			float var11 = (this.fogColorRed * 30.0F + this.fogColorBlue * 70.0F) / 100.0F;
			this.fogColorRed = var1;
			this.fogColorGreen = var10;
			this.fogColorBlue = var11;
		}

		GL11.glClearColor(this.fogColorRed, this.fogColorGreen, this.fogColorBlue, 0.0F);
	}

	private void setupFog() {
		World var1 = this.mc.theWorld;
		EntityPlayerSP var2 = this.mc.thePlayer;
		int var10000 = GL11.GL_FOG_COLOR;
		float var3 = 1.0F;
		float var6 = this.fogColorBlue;
		float var5 = this.fogColorGreen;
		float var4 = this.fogColorRed;
		this.fogColorBuffer.clear();
		this.fogColorBuffer.put(var4).put(var5).put(var6).put(1.0F);
		this.fogColorBuffer.flip();
		GL11.glFog(var10000, this.fogColorBuffer);
		GL11.glNormal3f(0.0F, -1.0F, 0.0F);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		Block var7 = Block.blocksList[var1.getBlockId((int)var2.posX, (int)(var2.posY + 0.12F), (int)var2.posZ)];
		if(var7 != null && var7.getBlockMaterial() != Material.air) {
			Material var8 = var7.getBlockMaterial();
			GL11.glFogi(GL11.GL_FOG_MODE, GL11.GL_EXP);
			if(var8 == Material.water) {
				GL11.glFogf(GL11.GL_FOG_DENSITY, 0.1F);
			} else if(var8 == Material.lava) {
				GL11.glFogf(GL11.GL_FOG_DENSITY, 2.0F);
			}
		} else {
			GL11.glFogi(GL11.GL_FOG_MODE, GL11.GL_LINEAR);
			GL11.glFogf(GL11.GL_FOG_START, 0.0F);
			GL11.glFogf(GL11.GL_FOG_END, this.farPlaneDistance);
		}

		GL11.glEnable(GL11.GL_COLOR_MATERIAL);
		GL11.glColorMaterial(GL11.GL_FRONT, GL11.GL_AMBIENT);
	}
}
