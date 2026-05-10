package de.dafuqs.spectrum.compat.REI.widgets;

import com.mojang.blaze3d.systems.*;
import me.shedaniel.math.*;
import me.shedaniel.rei.api.client.gui.widgets.*;
import net.minecraft.client.gui.*;
import net.minecraft.client.gui.components.events.*;
import net.minecraft.resources.*;
import net.minecraft.util.*;

import java.util.*;

public class AnimatedTexturedWidget extends BurningFire {
    
    private final Rectangle bounds;
	private final ResourceLocation texture;
    private final int animationCount;
    private final int textureWidth;
    private final int textureHeight;
    private double animationDurationMS = -1;
	
	public AnimatedTexturedWidget(ResourceLocation texture, Rectangle bounds, int textureWidth, int textureHeight) {
        this.texture = texture;
        this.animationCount = textureHeight / textureWidth;
        this.textureWidth = textureWidth;
        this.textureHeight = textureHeight;
        this.bounds = new Rectangle(bounds);
    }
    
    @Override
    public double getAnimationDuration() {
        return animationDurationMS;
    }
    
    @Override
    public void setAnimationDuration(double animationDurationMS) {
        this.animationDurationMS = animationDurationMS;
        if (this.animationDurationMS <= 0)
            this.animationDurationMS = -1;
    }
    
    @Override
	public void render(GuiGraphics graphics, int mouseX, int mouseY, float delta) {
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);

        if (getAnimationDuration() > 0) {
			int index = Mth.ceil((System.currentTimeMillis() / (animationDurationMS / animationCount) % animationCount));
			graphics.blit(texture, getX(), getY(), 0, index * 16, textureWidth, textureWidth, textureWidth, textureHeight);
        } else {
			graphics.blit(texture, getX(), getY(), 0, 0, textureWidth, textureWidth, textureWidth, textureHeight);
        }
    }
    
    @Override
    public Rectangle getBounds() {
        return bounds;
    }
    
    @Override
	public List<? extends GuiEventListener> children() {
        return Collections.emptyList();
    }
}