package de.dafuqs.spectrum.registries;

import de.dafuqs.spectrum.api.interaction.*;
import de.dafuqs.spectrum.entity.*;
import net.minecraft.entity.*;

public class SpectrumEntityColorProcessors {

	public static void register() {
		EntityColorProcessorRegistry.register(EntityType.SHEEP, (entity, dyeColor) -> {
			if (entity.getColor() == dyeColor) {
				return false;
			}
			entity.setColor(dyeColor);
			return true;
		});
		EntityColorProcessorRegistry.register(EntityType.WOLF, (entity, dyeColor) -> {
			if (entity.getCollarColor() == dyeColor) {
				return false;
			}
			entity.setCollarColor(dyeColor);
			return true;
		});
		EntityColorProcessorRegistry.register(EntityType.CAT, (entity, dyeColor) -> {
			if (entity.getCollarColor() == dyeColor) {
				return false;
			}
			entity.setCollarColor(dyeColor);
			return true;
		});
		EntityColorProcessorRegistry.register(EntityType.SHULKER, (entity, dyeColor) -> {
			if (entity.getColor() == dyeColor) {
				return false;
			}
			entity.setColor(dyeColor);
			return true;
		});
		
		EntityColorProcessorRegistry.register(SpectrumEntityTypes.EGG_LAYING_WOOLY_PIG, (entity, dyeColor) -> {
			if (entity.getColor() == dyeColor) {
				return false;
			}
			entity.setColor(dyeColor);
			return true;
		});
	}

}
