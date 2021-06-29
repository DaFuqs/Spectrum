package de.dafuqs.pigment;

import de.dafuqs.pigment.config.PigmentConfig;
import de.dafuqs.pigment.dimension.DeeperDownDimension;
import de.dafuqs.pigment.items.PigmentItems;
import de.dafuqs.pigment.loot.EnchantmentDrops;
import de.dafuqs.pigment.loot.PigmentLootConditionTypes;
import de.dafuqs.pigment.particle.PigmentParticleTypes;
import de.dafuqs.pigment.enchantments.PigmentEnchantments;
import de.dafuqs.pigment.inventories.PigmentContainers;
import de.dafuqs.pigment.inventories.PigmentScreenHandlerTypes;
import de.dafuqs.pigment.recipe.PigmentRecipeTypes;
import de.dafuqs.pigment.registries.*;
import de.dafuqs.pigment.sound.PigmentSoundEvents;
import de.dafuqs.pigment.worldgen.PigmentConfiguredFeatures;
import de.dafuqs.pigment.worldgen.PigmentFeatures;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.JanksonConfigSerializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerWorldEvents;
import net.minecraft.server.MinecraftServer;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class PigmentCommon implements ModInitializer {

    public static final String MOD_ID = "pigment";

    public static PigmentConfig PIGMENT_CONFIG;
    public static final Logger LOGGER = LogManager.getLogger(MOD_ID);

    public static MinecraftServer minecraftServer;

    public static void log(Level logLevel, String message) {
        LOGGER.log(logLevel, "[Pigment] " + message);
    }

    @Override
    public void onInitialize() {
        //Set up config
        LOGGER.info("Loading config file...");
        AutoConfig.register(PigmentConfig.class, JanksonConfigSerializer::new);
        PIGMENT_CONFIG = AutoConfig.getConfigHolder(PigmentConfig.class).getConfig();
        LOGGER.info("Finished loading config file.");

        // Register ALL the stuff
        PigmentSoundEvents.register();
        PigmentFluidTags.register();
        PigmentBlockTags.register();
        PigmentFluids.register();
        PigmentBlocks.register();
        PigmentItems.register();
        PigmentBlockEntityRegistry.register();
        PigmentEnchantments.register();
        PigmentFeatures.register();
        PigmentConfiguredFeatures.register();

        // Dimension
        DeeperDownDimension.setup();

        // Recipes
        PigmentRecipeTypes.register();
        PigmentLootConditionTypes.register();

        // GUI
        PigmentContainers.register();
        PigmentScreenHandlerTypes.register();

        // Default enchantments for some items
        PigmentItemStackDamageImmunities.registerDefaultItemStackImmunities();
        PigmentDefaultEnchantments.registerDefaultEnchantments();
        EnchantmentDrops.setup();

        PigmentParticleTypes.register();

        ServerWorldEvents.LOAD.register((minecraftServer, serverWorld) -> {
            PigmentCommon.minecraftServer = minecraftServer;
        });
    }

}
