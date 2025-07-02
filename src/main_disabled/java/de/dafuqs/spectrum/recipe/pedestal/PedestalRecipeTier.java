package de.dafuqs.spectrum.recipe.pedestal;

import com.mojang.serialization.*;
import de.dafuqs.revelationary.api.advancements.*;
import de.dafuqs.spectrum.api.item.*;
import de.dafuqs.spectrum.helpers.*;
import de.dafuqs.spectrum.registries.*;
import io.netty.buffer.*;
import net.minecraft.network.chat.*;
import net.minecraft.network.codec.*;
import net.minecraft.resources.*;
import net.minecraft.util.*;
import net.minecraft.world.entity.player.*;
import org.jetbrains.annotations.*;

import java.util.*;

public enum PedestalRecipeTier implements StringRepresentable {
	BASIC(SpectrumAdvancements.PLACED_PEDESTAL, new GemstoneColor[]{BuiltinGemstoneColor.CYAN, BuiltinGemstoneColor.MAGENTA, BuiltinGemstoneColor.YELLOW}),
	SIMPLE(SpectrumAdvancements.BUILD_BASIC_PEDESTAL_STRUCTURE, new GemstoneColor[]{BuiltinGemstoneColor.CYAN, BuiltinGemstoneColor.MAGENTA, BuiltinGemstoneColor.YELLOW}),
	ADVANCED(SpectrumAdvancements.BUILD_ADVANCED_PEDESTAL_STRUCTURE, new GemstoneColor[]{BuiltinGemstoneColor.CYAN, BuiltinGemstoneColor.MAGENTA, BuiltinGemstoneColor.YELLOW, BuiltinGemstoneColor.BLACK}),
	COMPLEX(SpectrumAdvancements.BUILD_COMPLEX_PEDESTAL_STRUCTURE, BuiltinGemstoneColor.values());
	
	private final ResourceLocation unlockAdvancementId;
	private final GemstoneColor[] gemstoneColors;
	
	public static final Codec<PedestalRecipeTier> CODEC = StringRepresentable.fromEnum(PedestalRecipeTier::values);
	public static final StreamCodec<ByteBuf, PedestalRecipeTier> PACKET_CODEC = PacketCodecHelper.enumOf(PedestalRecipeTier::values);
	
	PedestalRecipeTier(ResourceLocation unlockAdvancementId, GemstoneColor[] gemstoneColors) {
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
	public static Optional<PedestalRecipeTier> getHighestUnlockedRecipeTier(Player playerEntity) {
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
	
	public boolean hasUnlocked(Player playerEntity) {
		return AdvancementHelper.hasAdvancement(playerEntity, unlockAdvancementId);
	}
	
	public static Optional<PedestalRecipeTier> hasJustUnlockedANewRecipeTier(@NotNull ResourceLocation advancementIdentifier) {
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
	public @Nullable ResourceLocation getStructureID(Player player) {
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
	
	public @Nullable Component getStructureText() {
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
	
	@Override
	public String getSerializedName() {
		return name().toLowerCase();
	}
}
