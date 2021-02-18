package de.dafuqs.pigment;

import de.dafuqs.pigment.config.PigmentConfig;
import de.dafuqs.pigment.enchantments.PigmentEnchantments;
import de.dafuqs.pigment.inventories.PigmentContainers;
import de.dafuqs.pigment.inventories.PigmentScreenHandlerTypes;
import de.dafuqs.pigment.recipe.PigmentRecipeTypes;
import de.dafuqs.pigment.sounds.PigmentSoundEvents;
import de.dafuqs.pigment.worldgen.PigmentFeatures;
import me.sargunvohra.mcmods.autoconfig1u.AutoConfig;
import me.sargunvohra.mcmods.autoconfig1u.serializer.JanksonConfigSerializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerWorldEvents;
import net.minecraft.enchantment.Enchantments;
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

        // Register ALL the stuff
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

        registerDefaultEnchants();

        pigmentBlockCloaker = new PigmentBlockCloaker();

        ServerWorldEvents.LOAD.register((minecraftServer, serverWorld) -> {
            PigmentCommon.minecraftServer = minecraftServer;
        });
    }

    private void registerDefaultEnchants() {
        DefaultEnchants.addDefaultEnchantment(PigmentItems.LOOTING_FALCHION, Enchantments.LOOTING, 3);
        DefaultEnchants.addDefaultEnchantment(PigmentItems.SILKER_PICKAXE, Enchantments.SILK_TOUCH, 1);
        DefaultEnchants.addDefaultEnchantment(PigmentItems.FORTUNE_PICKAXE, Enchantments.FORTUNE, 3);
    }

    public static PigmentBlockCloaker getModelSwapper() {
        return pigmentBlockCloaker;
    }

}
