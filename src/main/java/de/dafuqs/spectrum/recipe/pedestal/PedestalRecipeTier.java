package de.dafuqs.spectrum.recipe.pedestal;

import de.dafuqs.revelationary.api.advancements.*;
import de.dafuqs.spectrum.api.item.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.entity.player.*;
import net.minecraft.text.*;
import net.minecraft.util.*;
import org.jetbrains.annotations.*;

import java.util.*;

public enum PedestalRecipeTier {
	BASIC(SpectrumAdvancements.PLACED_PEDESTAL, new GemstoneColor[]{BuiltinGemstoneColor.CYAN, BuiltinGemstoneColor.MAGENTA, BuiltinGemstoneColor.YELLOW}),
	SIMPLE(SpectrumAdvancements.BUILD_BASIC_PEDESTAL_STRUCTURE, new GemstoneColor[]{BuiltinGemstoneColor.CYAN, BuiltinGemstoneColor.MAGENTA, BuiltinGemstoneColor.YELLOW}),
	ADVANCED(SpectrumAdvancements.BUILD_ADVANCED_PEDESTAL_STRUCTURE, new GemstoneColor[]{BuiltinGemstoneColor.CYAN, BuiltinGemstoneColor.MAGENTA, BuiltinGemstoneColor.YELLOW, BuiltinGemstoneColor.BLACK}),
	COMPLEX(SpectrumAdvancements.BUILD_COMPLEX_PEDESTAL_STRUCTURE, BuiltinGemstoneColor.values());
	
	private final Identifier unlockAdvancementId;
	private final GemstoneColor[] gemstoneColors;
	
	PedestalRecipeTier(Identifier unlockAdvancementId, GemstoneColor[] gemstoneColors) {
		this.unlockAdvancementId = unlockAdvancementId;
		this.gemstoneColors = gemstoneColors;
	}
	
	@Contract(pure = true)
	public int getPowderSlotCount() {
		return this.gemstoneColors.length;
	}
	
	@Contract(pure = true)
	public GemstoneColor[] getAvailableGemstoneColors() {
		return gemstoneColors;
	}
	
	@Contract(pure = true)
	public static Optional<PedestalRecipeTier> getHighestUnlockedRecipeTier(PlayerEntity playerEntity) {
		if (AdvancementHelper.hasAdvancement(playerEntity, COMPLEX.unlockAdvancementId)) {
			return Optional.of(PedestalRecipeTier.COMPLEX);
		} else if (AdvancementHelper.hasAdvancement(playerEntity, ADVANCED.unlockAdvancementId)) {
			return Optional.of(PedestalRecipeTier.ADVANCED);
		} else if (AdvancementHelper.hasAdvancement(playerEntity, SIMPLE.unlockAdvancementId)) {
			return Optional.of(PedestalRecipeTier.SIMPLE);
		} else if (AdvancementHelper.hasAdvancement(playerEntity, BASIC.unlockAdvancementId)) {
			return Optional.of(PedestalRecipeTier.BASIC);
		}
		return Optional.empty();
	}
	
	public boolean hasUnlocked(PlayerEntity playerEntity) {
		return AdvancementHelper.hasAdvancement(playerEntity, unlockAdvancementId);
	}
	
	public static Optional<PedestalRecipeTier> hasJustUnlockedANewRecipeTier(@NotNull Identifier advancementIdentifier) {
		if (advancementIdentifier.equals(BASIC.unlockAdvancementId)) {
			return Optional.of(PedestalRecipeTier.BASIC);
		} else if (advancementIdentifier.equals(SIMPLE.unlockAdvancementId)) {
			return Optional.of(PedestalRecipeTier.SIMPLE);
		} else if (advancementIdentifier.equals(ADVANCED.unlockAdvancementId)) {
			return Optional.of(PedestalRecipeTier.ADVANCED);
		} else if (advancementIdentifier.equals(COMPLEX.unlockAdvancementId)) {
			return Optional.of(PedestalRecipeTier.COMPLEX);
		}
		return Optional.empty();
	}
	
	
	@Contract(pure = true)
	public @Nullable Identifier getStructureID(PlayerEntity player) {
		switch (this) {
			case COMPLEX -> {
				if (AdvancementHelper.hasAdvancement(player, SpectrumAdvancements.BUILD_COMPLEX_PEDESTAL_STRUCTURE_WITHOUT_MOONSTONE)) {
					return SpectrumMultiblocks.PEDESTAL_COMPLEX;
				} else {
					return SpectrumMultiblocks.PEDESTAL_COMPLEX_WITHOUT_MOONSTONE;
				}
			}
			case ADVANCED -> {
				return SpectrumMultiblocks.PEDESTAL_ADVANCED;
			}
			case SIMPLE -> {
				return SpectrumMultiblocks.PEDESTAL_SIMPLE;
			}
			default -> {
				return null;
			}
		}
	}
	
	public @Nullable Text getStructureText() {
		switch (this) {
			case COMPLEX -> {
				return SpectrumMultiblocks.PEDESTAL_COMPLEX_TEXT;
			}
			case ADVANCED -> {
				return SpectrumMultiblocks.PEDESTAL_ADVANCED_TEXT;
			}
			case SIMPLE -> {
				return SpectrumMultiblocks.PEDESTAL_SIMPLE_TEXT;
			}
			default -> {
				return null;
			}
		}
	}
	
}
