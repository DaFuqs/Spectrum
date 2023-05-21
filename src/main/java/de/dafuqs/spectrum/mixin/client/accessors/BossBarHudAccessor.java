package de.dafuqs.spectrum.mixin.client.accessors;

import net.minecraft.client.gui.hud.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.gen.*;

import java.util.*;

@Mixin(BossBarHud.class)
public interface BossBarHudAccessor {
	
	@Accessor(value = "bossBars")
	Map<UUID, ClientBossBar> getBossBars();
	
}