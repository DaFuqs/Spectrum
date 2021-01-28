package de.dafuqs.spectrum;

import de.dafuqs.spectrum.blocks.SpectrumBlockEntityType;
import de.dafuqs.spectrum.blocks.SpectrumBlockTags;
import de.dafuqs.spectrum.blocks.SpectrumBlocks;
import de.dafuqs.spectrum.inventories.SpectrumContainers;
import de.dafuqs.spectrum.inventories.SpectrumScreenHandlerTypes;
import de.dafuqs.spectrum.config.SpectrumConfig;
import de.dafuqs.spectrum.enchantments.SpectrumEnchantments;
import de.dafuqs.spectrum.fluid.SpectrumFluidTags;
import de.dafuqs.spectrum.fluid.SpectrumFluids;
import de.dafuqs.spectrum.items.SpectrumItems;
import de.dafuqs.spectrum.recipe.SpectrumRecipeTypes;
import de.dafuqs.spectrum.sounds.SpectrumSoundEvents;
import de.dafuqs.spectrum.worldgen.SpectrumFeatures;
import me.sargunvohra.mcmods.autoconfig1u.AutoConfig;
import me.sargunvohra.mcmods.autoconfig1u.serializer.JanksonConfigSerializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerWorldEvents;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SpectrumCommon implements ModInitializer {

    public static final String MOD_ID = "spectrum";
    public static SpectrumConfig GLOBAL_SPAWN_CONFIG;
    public static final Logger LOGGER = LogManager.getLogger(MOD_ID);

    public static MinecraftServer minecraftServer;
    public static ServerWorld serverWorld;

    @Override
    public void onInitialize() {
        //Set up config
        LOGGER.info("Loading config file...");
        AutoConfig.register(SpectrumConfig.class, JanksonConfigSerializer::new);
        GLOBAL_SPAWN_CONFIG = AutoConfig.getConfigHolder(SpectrumConfig.class).getConfig();
        LOGGER.info("Finished loading config file.");

        SpectrumSoundEvents.register();
        SpectrumFluidTags.register();
        SpectrumBlockTags.register();
        SpectrumFluids.register();
        SpectrumBlocks.register();
        SpectrumItems.register();
        SpectrumBlockEntityType.register();
        SpectrumEnchantments.register();
        SpectrumFeatures.register();

        SpectrumRecipeTypes.register();

        SpectrumContainers.register();
        SpectrumScreenHandlerTypes.register();

        ServerWorldEvents.LOAD.register((minecraftServer, serverWorld) -> {
            SpectrumCommon.minecraftServer = minecraftServer;
            SpectrumCommon.serverWorld = serverWorld;
        });
    }

}
