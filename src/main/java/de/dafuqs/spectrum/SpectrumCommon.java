package de.dafuqs.spectrum;

import de.dafuqs.spectrum.config.SpectrumConfig;
import de.dafuqs.spectrum.dimension.DeeperDownDimension;
import de.dafuqs.spectrum.entity.SpectrumEntityTypes;
import de.dafuqs.spectrum.events.SpectrumGameEvents;
import de.dafuqs.spectrum.inventories.SpectrumContainers;
import de.dafuqs.spectrum.inventories.SpectrumScreenHandlerTypes;
import de.dafuqs.spectrum.loot.EnchantmentDrops;
import de.dafuqs.spectrum.loot.SpectrumLootConditionTypes;
import de.dafuqs.spectrum.networking.SpectrumC2SPackets;
import de.dafuqs.spectrum.particle.SpectrumParticleTypes;
import de.dafuqs.spectrum.progression.BlockCloakManager;
import de.dafuqs.spectrum.progression.SpectrumAdvancementCriteria;
import de.dafuqs.spectrum.recipe.SpectrumRecipeTypes;
import de.dafuqs.spectrum.registries.*;
import de.dafuqs.spectrum.sound.SpectrumBlockSoundGroups;
import de.dafuqs.spectrum.sound.SpectrumSoundEvents;
import de.dafuqs.spectrum.worldgen.SpectrumConfiguredFeatures;
import de.dafuqs.spectrum.worldgen.SpectrumFeatures;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.JanksonConfigSerializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerWorldEvents;
import net.minecraft.block.Block;
import net.minecraft.block.FluidBlock;
import net.minecraft.fluid.Fluid;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.registry.Registry;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Iterator;

public class SpectrumCommon implements ModInitializer {

	public static final String MOD_ID = "spectrum";

	public static SpectrumConfig CONFIG;
	private static final Logger LOGGER = LogManager.getLogger(MOD_ID);

	public static MinecraftServer minecraftServer;
	/**
	 * Caches the luminance states from fluids as int
	 * for blocks that react to the light level of fluids
	 * like the fusion shrine lighting up with lava or liquid crystal
	 */
	public static HashMap<Fluid, Integer> fluidLuminance = new HashMap<>();

	public static void log(Level logLevel, String message) {
		LOGGER.log(logLevel, "[Spectrum] " + message);
	}

	@Override
	public void onInitialize() {
		//Set up config
		log(Level.INFO, "Loading config file...");
		AutoConfig.register(SpectrumConfig.class, JanksonConfigSerializer::new);
		CONFIG = AutoConfig.getConfigHolder(SpectrumConfig.class).getConfig();
		log(Level.INFO, "Finished loading config file.");

		// Register ALL the stuff
		SpectrumAdvancementCriteria.register();
		SpectrumParticleTypes.register();
		SpectrumSoundEvents.register();
		SpectrumBlockSoundGroups.register();
		SpectrumFluidTags.register();
		SpectrumBlockTags.getReferences();
		SpectrumFluids.register();
		
		SpectrumBlocks.register();
		SpectrumItems.register();
		
		// Tags
		SpectrumItemTags.getReferences();
		SpectrumBlockEntityRegistry.register();
		
		// Worldgen
		SpectrumFeatures.register();
		SpectrumConfiguredFeatures.register();

		// Dimension
		DeeperDownDimension.setup();

		// Recipes
		SpectrumRecipeTypes.registerSerializer();
		SpectrumLootConditionTypes.register();

		// GUI
		SpectrumContainers.register();
		SpectrumScreenHandlerTypes.register();

		// Default enchantments for some items
		SpectrumItemStackDamageImmunities.registerDefaultItemStackImmunities();
		SpectrumDefaultEnchantments.registerDefaultEnchantments();
		EnchantmentDrops.setup();

		SpectrumItems.registerFuelRegistry();
		
		SpectrumEnchantments.register();
		SpectrumEntityTypes.register();
		SpectrumCommands.register();

		SpectrumC2SPackets.registerC2SReceivers();

		BlockCloakManager.setupCloaks();
		SpectrumMultiblocks.register();
		SpectrumFlammableBlocks.register();
		SpectrumComposting.register();
		SpectrumGameEvents.register();
		
		SpectrumItemGroups.ITEM_GROUP_GENERAL.initialize();
		SpectrumItemGroups.ITEM_GROUP_BLOCKS.initialize();

		ServerWorldEvents.LOAD.register((minecraftServer, serverWorld) -> {
			SpectrumCommon.minecraftServer = minecraftServer;

			for (Iterator<Block> it = Registry.BLOCK.stream().iterator(); it.hasNext(); ) {
				Block block = it.next();
				if(block instanceof FluidBlock fluidBlock) {
					fluidLuminance.put(fluidBlock.getFluidState(fluidBlock.getDefaultState()).getFluid(), fluidBlock.getDefaultState().getLuminance());
				}
			}
		});

	}

}
