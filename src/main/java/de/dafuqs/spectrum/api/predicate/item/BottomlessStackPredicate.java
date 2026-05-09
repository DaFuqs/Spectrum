package de.dafuqs.spectrum.api.predicate.item;

import com.mojang.serialization.*;
import com.mojang.serialization.codecs.*;
import de.dafuqs.spectrum.blocks.bottomless_bundle.*;
import de.dafuqs.spectrum.progression.advancement.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.advancements.critereon.*;
import net.minecraft.core.component.*;
import net.minecraft.world.item.*;
import javax.annotation.*;

public record BottomlessStackPredicate(ItemPredicate template, LongRange count) implements SingleComponentItemPredicate<BottomlessComponent> {
	
	public static Codec<BottomlessStackPredicate> CODEC = RecordCodecBuilder.create(i -> i.group(
			ItemPredicate.CODEC.optionalFieldOf("variant", ItemPredicate.Builder.item().build()).forGetter(c -> c.template),
			LongRange.CODEC.optionalFieldOf("count", LongRange.ANY).forGetter(c -> c.count)
	).apply(i, BottomlessStackPredicate::new));
	
	@Override
	public DataComponentType<BottomlessComponent> componentType() {
		return SpectrumDataComponentTypes.BOTTOMLESS_STACK.get();
	}
	
	@Override
	public boolean matches(ItemStack stack, BottomlessComponent component) {
		return template.test(component.handler().variant()) && count.test(component.handler().count());
	}
	
}