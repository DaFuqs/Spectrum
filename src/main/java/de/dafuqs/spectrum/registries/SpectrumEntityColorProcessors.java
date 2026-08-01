package de.dafuqs.spectrum.registries;

import de.dafuqs.spectrum.api.ink.color.*;
import de.dafuqs.spectrum.api.interaction.*;
import de.dafuqs.spectrum.entity.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.item.*;
import org.jspecify.annotations.Nullable;

import java.util.*;

public class SpectrumEntityColorProcessors {
	
	public static void register() {
		// VANILLA
		EntityColorProcessorRegistry.register(() -> EntityType.SHEEP, (entity, dyeColor, player) -> {
			if (dyeColor.isEmpty()) {
				return false;
			}
			DyeColor color = dyeColor.get();
			
			if (entity.getColor() == color) {
				return false;
			}
			entity.setColor(color);
			return true;
		});
		EntityColorProcessorRegistry.register(() -> EntityType.WOLF, (entity, dyeColor, player) -> {
			if (dyeColor.isEmpty()) {
				return false;
			}
			if (!entity.isTame() || !entity.isOwnedBy(player)) {
				return false;
			}
			DyeColor color = dyeColor.get();
			if (entity.getCollarColor() == color) {
				return false;
			}
			
			entity.setCollarColor(color);
			return true;
		});
		EntityColorProcessorRegistry.register(() -> EntityType.CAT, (entity, dyeColor, player) -> {
			if (dyeColor.isEmpty()) {
				return false;
			}
			if (!entity.isTame() || !entity.isOwnedBy(player)) {
				return false;
			}
			DyeColor color = dyeColor.get();
			if (entity.getCollarColor() == color) {
				return false;
			}
			
			entity.setCollarColor(color);
			return true;
		});
		EntityColorProcessorRegistry.register(() -> EntityType.SHULKER, (entity, dyeColor, player) -> {
			@Nullable DyeColor shulkerColor = entity.getColor();
			if (shulkerColor == null && dyeColor.isEmpty()) {
				return false;
			}
			if (Optional.ofNullable(shulkerColor) == dyeColor) {
				return false;
			}
			entity.setVariant(dyeColor);
			return true;
		});
		
		// SPECTRUM
		EntityColorProcessorRegistry.register(() -> SpectrumEntityTypes.EGG_LAYING_WOOLY_PIG.get(), (entity, dyeColor, player) -> {
			if (dyeColor.isEmpty()) {
				return false;
			}
			DyeColor color = dyeColor.get();
			if (entity.getColor() == color) {
				return false;
			}
			entity.setColor(color);
			return true;
		});
		EntityColorProcessorRegistry.register(() -> SpectrumEntityTypes.INK_PROJECTILE.get(), (entity, dyeColor, player) -> {
			if (dyeColor.isEmpty()) {
				return false;
			}
			@Nullable InkColor inkColor = entity.getInkColor();
			if (inkColor == null || entity.getInkColor() == inkColor) {
				return false;
			}
			entity.setColor(inkColor);
			return true;
		});
	}
	
}
