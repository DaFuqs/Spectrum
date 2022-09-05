package de.dafuqs.spectrum.commands;

import com.mojang.brigadier.CommandDispatcher;
import de.dafuqs.spectrum.SpectrumCommon;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.LiteralText;

public class PrintConfigCommand {
	
	public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
		dispatcher.register((CommandManager.literal("spectrum_config").executes((context) -> {
			return execute(context.getSource());
		})));
	}
	
	private static int execute(ServerCommandSource source) {
		send(source, "Citrine Geode AboveBottom: " + SpectrumCommon.CONFIG.CitrineGeodeMinAboveBottomGenerationHeight + " FixedMax: " + SpectrumCommon.CONFIG.CitrineGeodeFixedMaxGenerationHeight);
		send(source, "Topaz Geode MinFixed: " + SpectrumCommon.CONFIG.TopazGeodeMinFixedGenerationHeight + " MaxBelow: " + SpectrumCommon.CONFIG.TopazGeodeMaxBelowTopGenerationHeight);
		send(source, "Geode Chunk Chances: Topaz: " + SpectrumCommon.CONFIG.TopazGeodeChunkChance + " Citrine: " + SpectrumCommon.CONFIG.CitrineGeodeChunkChance + " Moonstone: " + SpectrumCommon.CONFIG.MoonstoneGeodeChunkChance);
		
		send(source, "ColoredTreePatchChanceChunk: " + SpectrumCommon.CONFIG.ColoredTreePatchChanceChunk);
		send(source, "EndermanHoldingEnderTreasureChance: " + SpectrumCommon.CONFIG.EndermanHoldingEnderTreasureChance + " (" + SpectrumCommon.CONFIG.EndermanHoldingEnderTreasureInEndChance + " in the End)");
		
		send(source, "ShootingStarWorlds: " + SpectrumCommon.CONFIG.ShootingStarWorlds);
		send(source, "LightningStonesWorlds: " + SpectrumCommon.CONFIG.LightningStonesWorlds);
		send(source, "LightningStonesChance: " + SpectrumCommon.CONFIG.LightningStonesChance);
		send(source, "ShootingStarChance: " + SpectrumCommon.CONFIG.ShootingStarChance);
		send(source, "QuitoxicReedsGenerationBiomes: " + SpectrumCommon.CONFIG.QuitoxicReedsGenerationBiomes);
		send(source, "VanillaRecipeCraftingTimeTicks: " + SpectrumCommon.CONFIG.VanillaRecipeCraftingTimeTicks);
		
		send(source, "Decay tick rates: " + SpectrumCommon.CONFIG.FadingDecayTickRate + ", " + SpectrumCommon.CONFIG.FailingDecayTickRate + ", " + SpectrumCommon.CONFIG.RuinDecayTickRate + ", " + SpectrumCommon.CONFIG.TerrorDecayTickRate);
		
		String disabledEnchantments = "";
		if(!SpectrumCommon.CONFIG.AutoSmeltEnchantmentEnabled) { disabledEnchantments += "Foundry "; }
		if(!SpectrumCommon.CONFIG.ExuberanceEnchantmentEnabled) { disabledEnchantments += "Exuberance "; }
		if(!SpectrumCommon.CONFIG.InventoryInsertionEnchantmentEnabled) { disabledEnchantments += "Inventory Insertion "; }
		if(!SpectrumCommon.CONFIG.PestControlEnchantmentEnabled) { disabledEnchantments += "Pest Control "; }
		if(!SpectrumCommon.CONFIG.TreasureHunterEnchantmentEnabled) { disabledEnchantments += "Treasure Hunter "; }
		if(!SpectrumCommon.CONFIG.DisarmingEnchantmentEnabled) { disabledEnchantments += "Disarming "; }
		if(!SpectrumCommon.CONFIG.FirstStrikeEnchantmentEnabled) { disabledEnchantments += "First Strike "; }
		if(!SpectrumCommon.CONFIG.ImprovedCriticalEnchantmentEnabled) { disabledEnchantments += "Improved Critical "; }
		if(!SpectrumCommon.CONFIG.InertiaEnchantmentEnabled) { disabledEnchantments += "Inertia "; }
		if(!SpectrumCommon.CONFIG.CloversFavorEnchantmentEnabled) { disabledEnchantments += "Clovers Favor "; }
		if(!SpectrumCommon.CONFIG.SniperEnchantmentEnabled) { disabledEnchantments += "Sniping "; }
		if(!SpectrumCommon.CONFIG.TightGripEnchantmentEnabled) { disabledEnchantments += "Tight Grip "; }
		if(!SpectrumCommon.CONFIG.SteadfastEnchantmentEnabled) { disabledEnchantments += "Steadfast "; }
		if(!SpectrumCommon.CONFIG.IndestructibleEnchantmentEnabled) { disabledEnchantments += "Indestructible "; }
		if(disabledEnchantments.length() > 0) {
			send(source, "Disabled Enchantments: " + disabledEnchantments);
		} else {
			send(source, "Disabled Enchantments: none");
		}
		
		boolean anyEnchantmentSettingsChanged = false;
		if(SpectrumCommon.CONFIG.TreasureHunterMaxLevel != 3) { anyEnchantmentSettingsChanged = true; send(source, "Enchantment settings changed: TreasureHunter MaxLevel " + SpectrumCommon.CONFIG.TreasureHunterMaxLevel); }
		if(SpectrumCommon.CONFIG.DisarmingMaxLevel != 2) { anyEnchantmentSettingsChanged = true; send(source, "Enchantment settings changed: Disarming MaxLevel " + SpectrumCommon.CONFIG.DisarmingMaxLevel + "(Mobs : " + SpectrumCommon.CONFIG.DisarmingChancePerLevelMobs + " Players: " + SpectrumCommon.CONFIG.DisarmingChancePerLevelMobs + ")"); }
		if(SpectrumCommon.CONFIG.FirstStrikeMaxLevel != 2) { anyEnchantmentSettingsChanged = true; send(source, "Enchantment settings changed: FirstStrike MaxLevel " + SpectrumCommon.CONFIG.FirstStrikeMaxLevel + ("Damage per level: " + SpectrumCommon.CONFIG.FirstStrikeDamagePerLevel + ")")); }
		if(SpectrumCommon.CONFIG.ImprovedCriticalMaxLevel != 2) { anyEnchantmentSettingsChanged = true; send(source, "Enchantment settings changed: ImprovedCritical MaxLevel " + SpectrumCommon.CONFIG.ImprovedCriticalMaxLevel + "(" + SpectrumCommon.CONFIG.ImprovedCriticalExtraDamageMultiplierPerLevel + " multiplier per level)"); }
		if(SpectrumCommon.CONFIG.InertiaMaxLevel != 3) { anyEnchantmentSettingsChanged = true; send(source, "Enchantment settings changed: Inertia MaxLevel " + SpectrumCommon.CONFIG.InertiaMaxLevel); }
		if(SpectrumCommon.CONFIG.CloversFavorMaxLevel != 3) { anyEnchantmentSettingsChanged = true; send(source, "Enchantment settings changed: CloversFavor MaxLevel " + SpectrumCommon.CONFIG.CloversFavorMaxLevel); }
		if(SpectrumCommon.CONFIG.TightGripMaxLevel != 2) { anyEnchantmentSettingsChanged = true; send(source, "Enchantment settings changed: TightGrip MaxLevel " + SpectrumCommon.CONFIG.TightGripMaxLevel); }
		if(SpectrumCommon.CONFIG.ExuberanceMaxLevel != 5) { anyEnchantmentSettingsChanged = true; send(source, "Enchantment settings changed: Exuberance MaxLevel " + SpectrumCommon.CONFIG.ExuberanceMaxLevel); }
		if(SpectrumCommon.CONFIG.TreasureHunterMaxLevel != 3) { anyEnchantmentSettingsChanged = true; send(source, "Enchantment settings changed: TreasureHunterMaxLevel " + SpectrumCommon.CONFIG.TreasureHunterMaxLevel); }
		if(!anyEnchantmentSettingsChanged) {
			send(source, "Enchantment settings changed: none");
		}
		
		send(source, "GlowVisionGogglesDuration: " + SpectrumCommon.CONFIG.GlowVisionGogglesDuration);
		
		send(source, "Bedrock Armor Protection: " + SpectrumCommon.CONFIG.BedrockArmorHelmetProtection + ", "  + SpectrumCommon.CONFIG.BedrockArmorLeggingsProtection + ", " + SpectrumCommon.CONFIG.BedrockArmorChestplateProtection + ", " + SpectrumCommon.CONFIG.BedrockArmorBootsProtection + " (Toughness: " + SpectrumCommon.CONFIG.BedrockArmorToughness + ", Knockback Resistance: " + SpectrumCommon.CONFIG.BedrockArmorKnockbackResistance + ")");
		send(source, "Gemstone Armor Protection: " + SpectrumCommon.CONFIG.GemstoneArmorHelmetProtection + ", "  + SpectrumCommon.CONFIG.GemstoneArmorChestplateProtection + ", " + SpectrumCommon.CONFIG.GemstoneArmorLeggingsProtection + ", " + SpectrumCommon.CONFIG.GemstoneArmorBootsProtection + " (Toughness: " + SpectrumCommon.CONFIG.GemstoneArmorToughness + ", Knockback Resistance: " + SpectrumCommon.CONFIG.GemstoneArmorKnockbackResistance+ ")");
		send(source, "Bedrock Armor Effect Amplifiers: " + SpectrumCommon.CONFIG.GemstoneArmorWeaknessAmplifier + ", "  + SpectrumCommon.CONFIG.GemstoneArmorSlownessAmplifier + ", " + SpectrumCommon.CONFIG.GemstoneArmorAbsorptionAmplifier + ", " + SpectrumCommon.CONFIG.GemstoneArmorResistanceAmplifier + ", "  + SpectrumCommon.CONFIG.GemstoneArmorRegenerationAmplifier + ", " + SpectrumCommon.CONFIG.GemstoneArmorSpeedAmplifier+ ")");
		
		return 0;
	}
	
	private static void send(ServerCommandSource source, String s) {
		source.sendFeedback(new LiteralText(s), false);
	}
	
	
}
