package de.dafuqs.spectrum.config;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Config(name = "Spectrum")
public class SpectrumConfig implements ConfigData {

    // The heights where citrine and topaz geodes will spawn
    // By default citrine will generate slightly below sea level (y=35-55)
    // while topaz will generate at the top of mountains (everywhere from y=70+)
    // if the worldgen has lots of high mountains consider raising the TopazGeodeMinFixedGenerationHeight
    public int CitrineGeodeMinAboveBottomGenerationHeight = 35;
    public int CitrineGeodeFixedMaxGenerationHeight = 55;
    public int TopazGeodeMinFixedGenerationHeight = 70;
    public int TopazGeodeMaxBelowTopGenerationHeight = 0;

    // The chance that an Enderman is holding a special treasure block on spawn
    // Separate value for Endermen spawning in the end, since there are LOTS of them there
    // Those blocks do not gate progression, so it's not that drastic not finding any right away.
    // Better to let players stumble about them organically instead of forcing it.
    public float EndermanHoldingEnderTreasureChance = 0.05F;
    public float EndermanHoldingEnderTreasureInEndChance = 0.0005F;

    // worlds where shooting stars spawn for players
    // shooting stars will only spawn for players with sufficient progress in the mod
    public List<String> ShootingStarWorlds = Arrays.asList("minecraft:overworld", "starry_sky:starry_sky");

    // Shooting star spawns are checked every night between time 13000 and 22000, every 100 ticks (so 90 chances per night).
    // By default there is a 0.01 ^= 1 % chance at each of those check times. Making it ~1 shooting star spawn per night.
    public float ShootingStarChance = 0.01F;

    // The biomes where the biome specific plants are growing
    public List<String> MermaidsBrushGenerationBiomes = Arrays.asList("minecraft:deep_ocean", "minecraft:deep_cold_ocean", "minecraft:deep_frozen_ocean", "minecraft:deep_warm_ocean", "minecraft:deep_lukewarm_ocean");
    public List<String> QuitoxicReedsGenerationBiomes = Arrays.asList("minecraft:swamp", "minecraft:swamp_hills");

    // how fast decay will be spreading on random tick
    // can be used to slow down propagation speed of decay in the worlds
    // decay does use very few resources, but if your fear of someone letting decay
    // spread free or using high random tick rates you can limit the rate here
    // 1.0: every random tick (default)
    // 0.5: Every second random tick
    // 0.0: never (forbidden; players would be unable to progress)
    public float FadingDecayTickRate = 1.0F;
    public float FailingDecayTickRate = 1.0F;
    public float RuinDecayTickRate = 1.0F;

    // Enable or disable specific enchantments
    // Disabling the voiding enchantment will also disable the Oblivion Pickaxe
    public boolean AutoSmeltEnchantmentEnabled = true;
    public boolean ExuberanceEnchantmentEnabled = true;
    public boolean InventoryInsertionEnchantmentEnabled = true;
    public boolean PestControlEnchantmentEnabled = true;
    public boolean ResonanceEnchantmentEnabled = true;
    public boolean VoidingEnchantmentEnabled = true;
    public boolean TreasureHunterEnchantmentEnabled = true;
    public int TreasureHunterMaxLevel = 3;

    // Exuberance increases experience gained when killing mobs
    // With 20% bonus XP and 5 levels this would mean double XP on max level
    public int ExuberanceMaxLevel = 5;
    public float ExuberanceBonusExperiencePercentPerLevel = 0.2F;

    @Override
    public void validatePostLoad() throws ValidationException {
        if(FadingDecayTickRate <= 0) { FadingDecayTickRate = 1.0F; }
        if(FailingDecayTickRate <= 0) { FadingDecayTickRate = 1.0F; }
        if(RuinDecayTickRate <= 0) { RuinDecayTickRate = 1.0F; }
        if(ShootingStarChance <= 0) { ShootingStarChance = 0.01F; }
        if(EndermanHoldingEnderTreasureChance <= 0) { EndermanHoldingEnderTreasureChance = 0.05F; }
        if(TreasureHunterMaxLevel <= 0) { TreasureHunterMaxLevel = 3; }
        if(ExuberanceMaxLevel <= 0) { ExuberanceMaxLevel = 5; }
        if(ExuberanceBonusExperiencePercentPerLevel <= 0) { ExuberanceBonusExperiencePercentPerLevel = 0.2F; }
    }


}