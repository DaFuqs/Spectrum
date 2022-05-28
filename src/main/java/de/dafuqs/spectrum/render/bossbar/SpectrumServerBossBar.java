package de.dafuqs.spectrum.render.bossbar;

import de.dafuqs.spectrum.networking.SpectrumS2CPacketSender;
import net.minecraft.entity.boss.BossBar;
import net.minecraft.entity.boss.ServerBossBar;
import net.minecraft.text.Text;

public class SpectrumServerBossBar extends ServerBossBar {
	
	protected boolean serpentMusic;
	
	public SpectrumServerBossBar(Text displayName, Color color, Style style) {
		super(displayName, color, style);
	}
	
	public BossBar setSerpentMusic(boolean serpentMusic) {
		if (serpentMusic != this.serpentMusic) {
			this.serpentMusic = serpentMusic;
			SpectrumS2CPacketSender.sendBossBarUpdatePropertiesPacket(this.getUuid(), this.serpentMusic, this.getPlayers());
		}
		this.serpentMusic = serpentMusic;
		return this;
	}
	
}
