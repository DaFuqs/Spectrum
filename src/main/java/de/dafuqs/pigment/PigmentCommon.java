package de.dafuqs.pigment;

import de.dafuqs.pigment.config.PigmentConfig;
import de.dafuqs.pigment.enchantments.PigmentEnchantments;
import de.dafuqs.pigment.inventories.PigmentContainers;
import de.dafuqs.pigment.inventories.PigmentScreenHandlerTypes;
import de.dafuqs.pigment.items.PigmentItems;
import de.dafuqs.pigment.recipe.PigmentRecipeTypes;
import de.dafuqs.pigment.sounds.PigmentSoundEvents;
import de.dafuqs.pigment.worldgen.PigmentFeatures;
import me.sargunvohra.mcmods.autoconfig1u.AutoConfig;
import me.sargunvohra.mcmods.autoconfig1u.serializer.JanksonConfigSerializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerWorldEvents;
import net.minecraft.server.MinecraftServer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class PigmentCommon implements ModInitializer {

    public static final String MOD_ID = "pigment";

    public static PigmentConfig PIGMENT_CONFIG;
    public static final Logger LOGGER = LogManager.getLogger(MOD_ID);

    public static MinecraftServer minecraftServer;

    private static PigmentBlockCloaker pigmentBlockCloaker;

    @Override
    public void onInitialize() {
        //Set up config
        LOGGER.info("Loading config file...");
        AutoConfig.register(PigmentConfig.class, JanksonConfigSerializer::new);
        PIGMENT_CONFIG = AutoConfig.getConfigHolder(PigmentConfig.class).getConfig();
        LOGGER.info("Finished loading config file.");

        PigmentSoundEvents.register();
        PigmentFluidTags.register();
        PigmentBlockTags.register();
        PigmentFluids.register();
        PigmentBlocks.register();
        PigmentItems.register();
        PigmentBlockEntityType.register();
        PigmentEnchantments.register();
        PigmentFeatures.register();

        PigmentRecipeTypes.register();

        PigmentContainers.register();
        PigmentScreenHandlerTypes.register();

        pigmentBlockCloaker = new PigmentBlockCloaker();

        ServerWorldEvents.LOAD.register((minecraftServer, serverWorld) -> {
            PigmentCommon.minecraftServer = minecraftServer;
        });
    }

    public static PigmentBlockCloaker getModelSwapper() {
        return pigmentBlockCloaker;
    }

}
