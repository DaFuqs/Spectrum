package de.dafuqs.spectrum.mixin.compat;

import net.fabricmc.loader.api.FabricLoader;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import java.util.List;
import java.util.Set;

public final class Plugin implements IMixinConfigPlugin {
	private static final String COMPAT_PACKAGE_ROOT;
	private static final String COMPAT_PRESENT_KEY = "present";
	private static final FabricLoader LOADER = FabricLoader.getInstance();

	static {
		// Shorthand getting the plugin package to ensure not making trouble with other mixins
		COMPAT_PACKAGE_ROOT = Plugin.class.getPackageName();
	}
	
	@Override
	public void onLoad(String mixinPackage) {
	}
	
	@Override
	public String getRefMapperConfig() {
		return null;
	}
	
	@Override
	public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
		if (!mixinClassName.startsWith(COMPAT_PACKAGE_ROOT)) {
			return true; // We do not meddle with the others' work
		}
		String[] compatRoot = COMPAT_PACKAGE_ROOT.split("\\.");
		String[] mixinPath = mixinClassName.split("\\.");
		// The id of the mod the mixin depends on
		String compatModId = mixinPath[compatRoot.length];
		// Whether the mixin is for when the mod is loaded or not
		boolean isPresentMixin = mixinPath[compatRoot.length+1].equals(COMPAT_PRESENT_KEY);

		if (isPresentMixin) {
			// We only load the mixin if the mod we want to be present is found
			return LOADER.isModLoaded(compatModId);
		}
		return !LOADER.isModLoaded(compatModId);
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