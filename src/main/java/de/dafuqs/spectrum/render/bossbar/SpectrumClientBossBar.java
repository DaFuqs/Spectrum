package de.dafuqs.spectrum.render.bossbar;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.hud.ClientBossBar;
import net.minecraft.text.Text;

import java.util.UUID;

@Environment(EnvType.CLIENT)
public class SpectrumClientBossBar extends ClientBossBar {
	
	protected boolean serpentMusic;
	
	public SpectrumClientBossBar(UUID uuid, Text name, float percent, Color color, Style style, boolean darkenSky, boolean dragonMusic, boolean thickenFog, boolean serpentMusic) {
		super(uuid, name, percent, color, style, darkenSky, dragonMusic, thickenFog);
		this.serpentMusic = serpentMusic;
	}
	
	public void setSerpentMusic(boolean serpentMusic) {
		this.serpentMusic = serpentMusic;
	}
	
	public boolean hasSerpentBossMusic() {
		return this.serpentMusic;
	}
	
}
