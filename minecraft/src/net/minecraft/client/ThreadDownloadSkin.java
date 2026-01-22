package net.minecraft.client;

import java.net.HttpURLConnection;
import java.net.URL;
import javax.imageio.ImageIO;

final class ThreadDownloadSkin extends Thread {
	private Minecraft mc;

	ThreadDownloadSkin(Minecraft var1) {
		this.mc = var1;
	}

	public final void run() {
		if(this.mc.session != null) {
			HttpURLConnection var1 = null;

			try {
				URL var2 = new URL("http://www.minecraft.net/skin/" + this.mc.session.username + ".png");
				var1 = (HttpURLConnection)var2.openConnection();
				var1.setDoInput(true);
				var1.setDoOutput(false);
				var1.connect();
				if(var1.getResponseCode() != 404) {
					ImageIO.read(var1.getInputStream());
					return;
				}
			} catch (Exception var5) {
				var5.printStackTrace();
				return;
			} finally {
				var1.disconnect();
			}

		}
	}
}
