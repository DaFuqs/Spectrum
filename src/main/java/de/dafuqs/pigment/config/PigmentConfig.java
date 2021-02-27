package de.dafuqs.pigment.config;

import me.sargunvohra.mcmods.autoconfig1u.ConfigData;
import me.sargunvohra.mcmods.autoconfig1u.annotation.Config;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

@Config(name = "Pigment")
public class PigmentConfig implements ConfigData {

    List<RegistryKey<World>> shootingStarWorlds = new ArrayList();

}
