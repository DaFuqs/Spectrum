package de.dafuqs.spectrum.api.predicate.item;

import com.mojang.serialization.*;
import com.mojang.serialization.codecs.*;
import de.dafuqs.spectrum.components.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.advancements.critereon.*;
import net.minecraft.core.component.*;
import net.minecraft.world.item.*;

import java.util.*;

public record InfusedBeveragePredicate(Optional<String> variant) implements SingleComponentItemPredicate<InfusedBeverageComponent> {
	
	public static final Codec<InfusedBeveragePredicate> CODEC = RecordCodecBuilder.create(i -> i.group(
			Codec.STRING.optionalFieldOf("variant").forGetter(c -> c.variant)
	).apply(i, InfusedBeveragePredicate::new));
	
	@Override
	public DataComponentType<InfusedBeverageComponent> componentType() {
		return SpectrumDataComponentTypes.INFUSED_BEVERAGE;
	}
	
	@Override
	public boolean matches(ItemStack stack, InfusedBeverageComponent component) {
		return variant.isEmpty() || component.variant().equals(variant.get());
	}
}
