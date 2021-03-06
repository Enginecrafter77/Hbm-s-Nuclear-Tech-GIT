package com.hbm.render.misc;

import org.lwjgl.opengl.GL11;

import com.hbm.main.ResourceManager;

import net.minecraft.client.Minecraft;

public class ErrorPronter {
	
	public static void prontError() {

		GL11.glDisable(GL11.GL_CULL_FACE);

		GL11.glScaled(2, 2, 2);
		
		GL11.glColor3d(Math.sin(System.currentTimeMillis() % 1000 / 1000D * Math.PI) * 0.5 + 0.5, 0.0, 0.0);
		Minecraft.getMinecraft().getTextureManager().bindTexture(ResourceManager.turbofan_blades_tex);
		ResourceManager.error.renderAll();

		GL11.glEnable(GL11.GL_CULL_FACE);
	}

}
