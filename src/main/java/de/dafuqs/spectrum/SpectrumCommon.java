package de.dafuqs.spectrum;

import de.dafuqs.spectrum.config.*;
import me.shedaniel.autoconfig.*;
import me.shedaniel.autoconfig.serializer.*;
import net.minecraft.resources.*;
import net.minecraft.server.*;
import net.minecraft.tags.*;
import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.*;
import net.neoforged.bus.api.*;
import net.neoforged.fml.common.*;
import org.jetbrains.annotations.*;
import org.slf4j.*;

import java.util.*;

@Mod(SpectrumCommon.MOD_ID)
public class SpectrumCommon {
	
	public static final String MOD_ID = "spectrum";
	
	public static final Logger LOGGER = LoggerFactory.getLogger("Spectrum");
	public static final Map<ResourceLocation, TagKey<Item>> CACHED_ITEM_TAG_MAP = new HashMap<>();
	public static SpectrumConfig CONFIG;

	public static void logInfo(String message) {
		LOGGER.info("[Spectrum] {}", message);
	}
	
	public static void logWarning(String message) {
		LOGGER.warn("[Spectrum] {}", message);
	}
	
	public static void logError(String message) {
		LOGGER.error("[Spectrum] {}", message);
	}
	
	public static ResourceLocation locate(String name) {
		return ResourceLocation.fromNamespaceAndPath(MOD_ID, name);
	}
	
	/**
	 * This is the Spectrum analogue of Identifier.of, but instead of defaulting to the namespace 'minecraft', it defaults to 'spectrum'.
	 *
	 * @param id The stringified identifier to parse
	 * @return The parsed identifier
	 */
	public static ResourceLocation ofSpectrumDefaulted(String id) {
		int i = id.indexOf(':');
		String path = id.substring(i + 1);
		String namespace = i > 0 ? id.substring(0, i) : SpectrumCommon.MOD_ID;
		return ResourceLocation.fromNamespaceAndPath(namespace, path);
	}
	
	// Will be null when playing on a dedicated server!
	@Nullable
	public static MinecraftServer minecraftServer;
	
	static {
		//Set up config
		logInfo("Loading config file...");
		AutoConfig.register(SpectrumConfig.class, JanksonConfigSerializer::new);
		CONFIG = AutoConfig.getConfigHolder(SpectrumConfig.class).getConfig();
		logInfo("Finished loading config file.");
	}
	
	public SpectrumCommon(IEventBus modBus) {
		logInfo("Starting Common Startup");
		
	}
	
	/**
	 * When initializing a block entity, world can still be null
	 * Therefore we use the RecipeManager reference from MinecraftServer
	 * This in turn does not work on clients connected to dedicated servers, though
	 * since SpectrumCommon.minecraftServer is null
	 */
	public static Optional<RecipeManager> getRecipeManager(@Nullable Level world) {
		return world == null ? minecraftServer == null ? Optional.empty() : Optional.of(minecraftServer.getRecipeManager()) : Optional.of(world.getRecipeManager());
	}
	
}
