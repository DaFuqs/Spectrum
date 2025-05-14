package de.dafuqs.spectrum.commands;

import com.mojang.brigadier.tree.*;
import de.dafuqs.spectrum.*;
import net.minecraft.commands.*;
import net.minecraft.network.chat.*;


public class PrintConfigCommand {
	
	public static void register(LiteralCommandNode<CommandSourceStack> root) {
		LiteralCommandNode<CommandSourceStack> config = Commands.literal("config").executes((context) -> execute(context.getSource())).build();
		root.addChild(config);
	}
	
	private static int execute(CommandSourceStack source) {
		send(source, "EndermanHoldingEnderTreasureChance: " + SpectrumCommon.CONFIG.EndermanHoldingEnderTreasureChance + " (" + SpectrumCommon.CONFIG.EndermanHoldingEnderTreasureInEndChance + " in the End)");
		
		send(source, "ShootingStarWorlds: " + SpectrumCommon.CONFIG.ShootingStarWorlds);
		send(source, "StormStonesWorlds: " + SpectrumCommon.CONFIG.StormStonesWorlds);
		send(source, "StormStonesChance: " + SpectrumCommon.CONFIG.StormStonesChance);
		send(source, "ShootingStarChance: " + SpectrumCommon.CONFIG.ShootingStarChance);
		send(source, "VanillaRecipeCraftingTimeTicks: " + SpectrumCommon.CONFIG.VanillaRecipeCraftingTimeTicks);
		
		send(source, "Decay tick rates: " + SpectrumCommon.CONFIG.FadingDecayTickRate + ", " + SpectrumCommon.CONFIG.FailingDecayTickRate + ", " + SpectrumCommon.CONFIG.RuinDecayTickRate + ", " + SpectrumCommon.CONFIG.ForfeitureDecayTickRate);
		send(source, "Decay pickup ability: " + SpectrumCommon.CONFIG.CanBottleUpFading + ", " + SpectrumCommon.CONFIG.CanBottleUpFailing + ", " + SpectrumCommon.CONFIG.CanBottleUpRuin + ", " + SpectrumCommon.CONFIG.CanBottleUpForfeiture);
		send(source, "Decay can destroy block entities: " + SpectrumCommon.CONFIG.FadingCanDestroyBlockEntities + ", " + SpectrumCommon.CONFIG.FailingCanDestroyBlockEntities + ", " + SpectrumCommon.CONFIG.RuinCanDestroyBlockEntities + ", " + SpectrumCommon.CONFIG.ForfeitureCanDestroyBlockEntities);
		
		send(source, "GlowVisionGogglesDuration: " + SpectrumCommon.CONFIG.GlowVisionGogglesDuration);
		send(source, "OmniAcceleratorPvP: " + SpectrumCommon.CONFIG.OmniAcceleratorPvP);
		
		send(source, "Bedrock Armor Protection: " + SpectrumCommon.CONFIG.BedrockArmorHelmetProtection + ", " + SpectrumCommon.CONFIG.BedrockArmorLeggingsProtection + ", " + SpectrumCommon.CONFIG.BedrockArmorChestplateProtection + ", " + SpectrumCommon.CONFIG.BedrockArmorBootsProtection + " (Toughness: " + SpectrumCommon.CONFIG.BedrockArmorToughness + ", Knockback Resistance: " + SpectrumCommon.CONFIG.BedrockArmorKnockbackResistance + ")");
		send(source, "Gemstone Armor Protection: " + SpectrumCommon.CONFIG.GemstoneArmorHelmetProtection + ", " + SpectrumCommon.CONFIG.GemstoneArmorChestplateProtection + ", " + SpectrumCommon.CONFIG.GemstoneArmorLeggingsProtection + ", " + SpectrumCommon.CONFIG.GemstoneArmorBootsProtection + " (Toughness: " + SpectrumCommon.CONFIG.GemstoneArmorToughness + ", Knockback Resistance: " + SpectrumCommon.CONFIG.GemstoneArmorKnockbackResistance + ")");
		send(source, "Bedrock Armor Effect Amplifiers: " + SpectrumCommon.CONFIG.GemstoneArmorWeaknessAmplifier + ", " + SpectrumCommon.CONFIG.GemstoneArmorSlownessAmplifier + ", " + SpectrumCommon.CONFIG.GemstoneArmorAbsorptionAmplifier + ", " + SpectrumCommon.CONFIG.GemstoneArmorResistanceAmplifier + ", " + SpectrumCommon.CONFIG.GemstoneArmorRegenerationAmplifier + ", " + SpectrumCommon.CONFIG.GemstoneArmorSpeedAmplifier + ")");
		
		return 0;
	}
	
	private static void send(CommandSourceStack source, String s) {
		source.sendSuccess(() -> Component.literal(s), false);
	}
	
	
}
