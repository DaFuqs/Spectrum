package de.dafuqs.spectrum.mixin.client.accessors;

import net.minecraft.client.gui.hud.BossBarHud;
import net.minecraft.client.gui.hud.ClientBossBar;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Map;
import java.util.UUID;

@Mixin(BossBarHud.class)
public interface BossBarHudAccessor {
	
	@Accessor(value = "bossBars")
	Map<UUID, ClientBossBar> getBossBars();
	
}