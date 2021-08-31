package de.dafuqs.spectrum;

import de.dafuqs.spectrum.config.SpectrumConfig;
import de.dafuqs.spectrum.dimension.DeeperDownDimension;
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
import net.minecraft.server.MinecraftServer;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SpectrumCommon implements ModInitializer {

    public static final String MOD_ID = "spectrum";

    public static SpectrumConfig CONFIG;
    private static final Logger LOGGER = LogManager.getLogger(MOD_ID);

    public static MinecraftServer minecraftServer;

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
        SpectrumItemTags.getReferences();
        SpectrumBlockEntityRegistry.register();
        SpectrumEnchantments.register();
        SpectrumFeatures.register();
        SpectrumConfiguredFeatures.register();

        // Dimension
        DeeperDownDimension.setup();

        // Recipes
        SpectrumRecipeTypes.register();
        SpectrumLootConditionTypes.register();

        // GUI
        SpectrumContainers.register();
        SpectrumScreenHandlerTypes.register();

        // Default enchantments for some items
        SpectrumItemStackDamageImmunities.registerDefaultItemStackImmunities();
        SpectrumDefaultEnchantments.registerDefaultEnchantments();
        EnchantmentDrops.setup();

        SpectrumItems.registerFuelRegistry();
        SpectrumCommands.register();

        SpectrumC2SPackets.registerC2SReceivers();

        BlockCloakManager.setupCloaks();
        SpectrumMultiblocks.register();
        SpectrumFlammableBlocks.register();
        SpectrumComposting.register();
        SpectrumGameEvents.register();

        ServerWorldEvents.LOAD.register((minecraftServer, serverWorld) -> {
            SpectrumCommon.minecraftServer = minecraftServer;
        });
    }

}
