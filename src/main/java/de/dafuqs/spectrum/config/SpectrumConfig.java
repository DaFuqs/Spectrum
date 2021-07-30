package de.dafuqs.spectrum.config;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

@Config(name = "Spectrum")
public class SpectrumConfig implements ConfigData {

    List<RegistryKey<World>> shootingStarWorlds = new ArrayList();

}
