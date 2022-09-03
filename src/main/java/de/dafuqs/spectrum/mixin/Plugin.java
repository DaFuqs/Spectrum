package de.dafuqs.spectrum.mixin;

import com.llamalad7.mixinextras.MixinExtrasBootstrap;
import net.fabricmc.loader.api.FabricLoader;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import java.util.List;
import java.util.Set;

public final class Plugin implements IMixinConfigPlugin {
	
	@Override
	public void onLoad(String mixinPackage) {
		MixinExtrasBootstrap.init();
	}
	
	@Override
	public String getRefMapperConfig() {
		return null;
	}
	
	@Override
	public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
		if (mixinClassName.equals("de.dafuqs.spectrum.mixin.ColoredLeavesBlockMixin")) {
			return FabricLoader.getInstance().isModLoaded("botania");
		} else if (mixinClassName.equals("de.dafuqs.spectrum.mixin.client.ClientWorldReverbMixin")) {
			return FabricLoader.getInstance().isModLoaded("limlib");
		} else {
			return true;
		}
	}
	
	@Override
	public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) {
	}
	
	@Override
	public List<String> getMixins() {
		return List.of();
	}
	
	@Override
	public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {
	}
	
	@Override
	public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {
	}
	
}