package net.minecraft.client.gui;

import java.awt.FileDialog;
import java.io.File;

final class GuiLevelDialog extends Thread {
	private GuiLoadLevel guiLoadLevel;

	GuiLevelDialog(GuiLoadLevel var1) {
		this.guiLoadLevel = var1;
	}

	public final void run() {
		try {
			FileDialog var1 = this.guiLoadLevel.d();
			File var2 = new File(this.guiLoadLevel.mc.mcDataDir, "saves");
			var2.mkdir();
			String var5 = var2.toString();
			if(!var5.endsWith(File.separator)) {
				var5 = var5 + File.separator;
			}

			var1.setDirectory(var5);
			FileNameExtensionFilter var6 = new FileNameExtensionFilter(this);
			var1.setFilenameFilter(var6);
			var1.setLocationRelativeTo(this.guiLoadLevel.mc.appletCanvas);
			var1.setVisible(true);
			if(var1.getFile() != null) {
				var5 = var1.getDirectory();
				if(!var5.endsWith(File.separator)) {
					var5 = var5 + File.separator;
				}

				GuiLoadLevel.a(this.guiLoadLevel, new File(var5 + var1.getFile()));
			}
		} finally {
			GuiLoadLevel.a(this.guiLoadLevel, false);
		}

	}
}
