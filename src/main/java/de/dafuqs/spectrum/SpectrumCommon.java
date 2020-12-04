package de.dafuqs.spectrum;

import de.dafuqs.spectrum.blocks.SpectrumBlocks;
import de.dafuqs.spectrum.config.SpectrumConfig;
import de.dafuqs.spectrum.enchantments.SpectrumEnchantments;
import de.dafuqs.spectrum.items.SpectrumItems;
import me.sargunvohra.mcmods.autoconfig1u.AutoConfig;
import me.sargunvohra.mcmods.autoconfig1u.serializer.JanksonConfigSerializer;
import net.fabricmc.api.ModInitializer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SpectrumCommon implements ModInitializer {

    public static final String MOD_ID = "spectrum";
    public static SpectrumConfig GLOBAL_SPAWN_CONFIG;
    public static final Logger LOGGER = LogManager.getLogger(MOD_ID);

    @Override
    public void onInitialize() {

        //Set up config
        LOGGER.info("Loading config file...");
        AutoConfig.register(SpectrumConfig.class, JanksonConfigSerializer::new);
        GLOBAL_SPAWN_CONFIG = AutoConfig.getConfigHolder(SpectrumConfig.class).getConfig();
        LOGGER.info("Finished loading config file.");

        SpectrumBlocks.register();
        SpectrumItems.register();
        SpectrumEnchantments.register();
    }

}
