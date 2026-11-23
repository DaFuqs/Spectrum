package de.dafuqs.spectrum.registries;

import de.dafuqs.spectrum.api.ink.color.*;
import de.dafuqs.spectrum.api.interaction.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.item.*;
import org.jspecify.annotations.*;

import java.util.*;

public class SpectrumEntityColorProcessors {
	
	public static void register() {
		// VANILLA
		EntityColorProcessorRegistry.register(() -> EntityType.SHEEP, (entity, color, player) -> {
			if (color.isEmpty()) {
				return false;
			}
			Optional<DyeColor> dyeColor = color.get().getDyeColor();
			
			if (dyeColor.isEmpty() || entity.getColor() == dyeColor.get()) {
				return false;
			}
			entity.setColor(dyeColor.get());
			return true;
		});
		EntityColorProcessorRegistry.register(() -> EntityType.WOLF, (entity, color, player) -> {
			if (color.isEmpty()) {
				return false;
			}
			if (!entity.isTame() || !entity.isOwnedBy(player)) {
				return false;
			}
			Optional<DyeColor> dyeColor = color.get().getDyeColor();
			if (dyeColor.isEmpty() || entity.getCollarColor() == dyeColor.get()) {
				return false;
			}
			
			entity.setCollarColor(dyeColor.get());
			return true;
		});
		EntityColorProcessorRegistry.register(() -> EntityType.CAT, (entity, color, player) -> {
			if (color.isEmpty()) {
				return false;
			}
			if (!entity.isTame() || !entity.isOwnedBy(player)) {
				return false;
			}
			Optional<DyeColor> dyeColor = color.get().getDyeColor();
			if (dyeColor.isEmpty() || entity.getCollarColor() == dyeColor.get()) {
				return false;
			}
			entity.setCollarColor(dyeColor.get());
			return true;
		});
		EntityColorProcessorRegistry.register(() -> EntityType.SHULKER, (entity, color, player) -> {
			Optional<DyeColor> shulkerColor = entity.getVariant();
			if (shulkerColor.isPresent() && color.isEmpty()) {
				return false;
			}
			if(color.isEmpty()) {
				entity.setVariant(Optional.empty());
				return true;
			}
			Optional<DyeColor> dyeColor = color.get().getDyeColor();
			if (dyeColor.isEmpty() || shulkerColor.equals(dyeColor)) {
				return false;
			}
			entity.setVariant(dyeColor);
			return true;
		});
		
		// SPECTRUM
		EntityColorProcessorRegistry.register(() -> SpectrumEntityTypes.EGG_LAYING_WOOLY_PIG.get(), (entity, color, player) -> {
			if (color.isEmpty() || entity.getColor() == color) {
				return false;
			}
			entity.setColor(color);
			return true;
		});
		EntityColorProcessorRegistry.register(() -> SpectrumEntityTypes.INK_PROJECTILE.get(), (entity, color, player) -> {
			if (color.isEmpty() || entity.getInkColor() == color.get()) {
				return false;
			}
			entity.setColor(color.get());
			return true;
		});
	}
	
}
