package de.dafuqs.spectrum.registries;

import de.dafuqs.spectrum.api.interaction.*;
import de.dafuqs.spectrum.entity.*;
import de.dafuqs.spectrum.entity.entity.*;
import net.minecraft.entity.*;
import net.minecraft.entity.mob.*;
import net.minecraft.entity.passive.*;

import java.util.*;

public class SpectrumEntityColorProcessors {

	public static void register() {
		EntityColorProcessor.register(EntityType.SHEEP, (EntityColorProcessor<SheepEntity>) (entity, dyeColor) -> {
			if (entity.getColor() == dyeColor) {
				return false;
			}
			entity.setColor(dyeColor);
			return true;
		});
		EntityColorProcessor.register(EntityType.SHULKER, (EntityColorProcessor<ShulkerEntity>) (entity, dyeColor) -> {
			if (entity.getColor() == dyeColor) {
				return false;
			}
			entity.setVariant(Optional.of(dyeColor));
			return true;
		});
		EntityColorProcessor.register(SpectrumEntityTypes.EGG_LAYING_WOOLY_PIG, (EntityColorProcessor<EggLayingWoolyPigEntity>) (entity, dyeColor) -> {
			if (entity.getColor() == dyeColor) {
				return false;
			}
			entity.setColor(dyeColor);
			return true;
		});
	}

}
