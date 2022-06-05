package de.dafuqs.spectrum.enums;

import de.dafuqs.spectrum.SpectrumCommon;
import de.dafuqs.spectrum.helpers.Support;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public enum PedestalRecipeTier {
	BASIC,
	SIMPLE,
	ADVANCED,
	COMPLEX;
	
	@Contract(pure = true)
	public static GemstoneColor[] getAvailableGemstoneDustColors(@NotNull PedestalRecipeTier pedestalRecipeTier) {
		switch (pedestalRecipeTier) {
			case COMPLEX -> {
				return BuiltinGemstoneColor.values();
			}
			case ADVANCED -> {
				return new GemstoneColor[]{BuiltinGemstoneColor.CYAN, BuiltinGemstoneColor.MAGENTA, BuiltinGemstoneColor.YELLOW, BuiltinGemstoneColor.BLACK};
			}
			default -> {
				return new GemstoneColor[]{BuiltinGemstoneColor.CYAN, BuiltinGemstoneColor.MAGENTA, BuiltinGemstoneColor.YELLOW};
			}
		}
	}
	
	@Contract(pure = true)
	public static Optional<PedestalRecipeTier> getHighestUnlockedRecipeTier(PlayerEntity playerEntity) {
		if (Support.hasAdvancement(playerEntity, new Identifier(SpectrumCommon.MOD_ID, "lategame/build_complex_pedestal_structure"))) {
			return Optional.of(PedestalRecipeTier.COMPLEX);
		} else if (Support.hasAdvancement(playerEntity, new Identifier(SpectrumCommon.MOD_ID, "midgame/build_advanced_pedestal_structure"))) {
			return Optional.of(PedestalRecipeTier.ADVANCED);
		} else if (Support.hasAdvancement(playerEntity, new Identifier(SpectrumCommon.MOD_ID, "build_basic_pedestal_structure"))) {
			return Optional.of(PedestalRecipeTier.SIMPLE);
		} else if (Support.hasAdvancement(playerEntity, new Identifier(SpectrumCommon.MOD_ID, "place_pedestal"))) {
			return Optional.of(PedestalRecipeTier.BASIC);
		}
		return Optional.empty();
	}
	
	public static boolean hasUnlockedRequiredTier(PlayerEntity playerEntity, @NotNull PedestalRecipeTier pedestalRecipeTier) {
		switch (pedestalRecipeTier) {
			case BASIC -> {
				return Support.hasAdvancement(playerEntity, new Identifier(SpectrumCommon.MOD_ID, "place_pedestal"));
			}
			case SIMPLE -> {
				return Support.hasAdvancement(playerEntity, new Identifier(SpectrumCommon.MOD_ID, "build_basic_pedestal_structure"));
			}
			case ADVANCED -> {
				return Support.hasAdvancement(playerEntity, new Identifier(SpectrumCommon.MOD_ID, "midgame/build_advanced_pedestal_structure"));
			}
			case COMPLEX -> {
				return Support.hasAdvancement(playerEntity, new Identifier(SpectrumCommon.MOD_ID, "lategame/build_complex_pedestal_structure"));
			}
			default -> {
				return false;
			}
		}
	}
	
	public static Optional<PedestalRecipeTier> hasJustUnlockedANewRecipeTier(@NotNull Identifier advancementIdentifier) {
		if (advancementIdentifier.equals(new Identifier(SpectrumCommon.MOD_ID, "place_pedestal"))) {
			return Optional.of(PedestalRecipeTier.BASIC);
		} else if (advancementIdentifier.equals(new Identifier(SpectrumCommon.MOD_ID, "build_basic_pedestal_structure"))) {
			return Optional.of(PedestalRecipeTier.SIMPLE);
		} else if (advancementIdentifier.equals(new Identifier(SpectrumCommon.MOD_ID, "midgame/build_advanced_pedestal_structure"))) {
			return Optional.of(PedestalRecipeTier.ADVANCED);
		} else if (advancementIdentifier.equals(new Identifier(SpectrumCommon.MOD_ID, "lategame/build_complex_pedestal_structure"))) {
			return Optional.of(PedestalRecipeTier.COMPLEX);
		}
		return Optional.empty();
	}
	
}
