package de.dafuqs.spectrum.mixin.client;

import de.dafuqs.spectrum.SpectrumCommon;
import de.dafuqs.spectrum.helpers.Support;
import de.dafuqs.spectrum.mixin.client.accessors.BossBarHudAccessor;
import de.dafuqs.spectrum.progression.ClientAdvancements;
import de.dafuqs.spectrum.registries.SpectrumMusicType;
import de.dafuqs.spectrum.render.bossbar.SpectrumClientBossBar;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.BossBarHud;
import net.minecraft.client.gui.hud.ClientBossBar;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.sound.MusicSound;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Map;
import java.util.UUID;

@Mixin(MinecraftClient.class)
public abstract class MinecraftClientMixin {
	
	@Inject(at = @At("HEAD"), method = "disconnect(Lnet/minecraft/client/gui/screen/Screen;)V")
	public void spectrum$onLogout(Screen screen, CallbackInfo info) {
		ClientAdvancements.playerLogout();
	}
	
	@Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;getRegistryKey()Lnet/minecraft/util/registry/RegistryKey;"), method = "getMusicType()Lnet/minecraft/sound/MusicSound;", cancellable = true)
	public void spectrum$getMusicType(CallbackInfoReturnable<MusicSound> cir) {
		MinecraftClient thisClient = (MinecraftClient) (Object) this;
		if (thisClient.player.world.getRegistryKey() == SpectrumCommon.DEEPER_DOWN) {
			if (shouldPlaySerpentMusic(thisClient.inGameHud.getBossBarHud())) {
				cir.setReturnValue(SpectrumMusicType.BOSS_THEME);
			} else {
				if (Support.hasPlayerFinishedMod(MinecraftClient.getInstance().player)) {
					cir.setReturnValue(SpectrumMusicType.SPECTRUM_THEME);
				} else {
					cir.setReturnValue(SpectrumMusicType.DEEPER_DOWN_THEME);
				}
			}
		}
	}
	
	public boolean shouldPlaySerpentMusic(BossBarHud bossBarHud) {
		Map<UUID, ClientBossBar> bossBars = ((BossBarHudAccessor) bossBarHud).getBossBars();
		if (!bossBars.isEmpty()) {
			for (ClientBossBar clientBossBar : bossBars.values()) {
				if (clientBossBar instanceof SpectrumClientBossBar spectrumClientBossBar) {
					return spectrumClientBossBar.hasSerpentBossMusic();
				}
			}
		}
		return false;
	}
	
}