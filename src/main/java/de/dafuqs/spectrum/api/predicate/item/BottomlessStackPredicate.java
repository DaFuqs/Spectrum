package de.dafuqs.spectrum.api.predicate.item;

import com.mojang.serialization.*;
import com.mojang.serialization.codecs.*;
import de.dafuqs.spectrum.blocks.bottomless_bundle.*;
import de.dafuqs.spectrum.progression.advancement.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.advancements.critereon.*;
import net.minecraft.core.component.*;
import net.minecraft.world.item.*;

public record BottomlessStackPredicate(ItemPredicate template, LongRange count) implements SingleComponentItemPredicate<BottomlessBundleItem.BottomlessStack> {
	
	public static Codec<BottomlessStackPredicate> CODEC = RecordCodecBuilder.create(i -> i.group(
			ItemPredicate.CODEC.optionalFieldOf("variant", ItemPredicate.Builder.item().build()).forGetter(c -> c.template),
			LongRange.CODEC.optionalFieldOf("count", LongRange.ANY).forGetter(c -> c.count)
	).apply(i, BottomlessStackPredicate::new));
	
	@Override
	public DataComponentType<BottomlessBundleItem.BottomlessStack> componentType() {
		// SpectrumDataComponentTypes.BOTTOMLESS_STACK is a Supplier-like holder; call get() to obtain the actual type.
		return SpectrumDataComponentTypes.BOTTOMLESS_STACK.get();
	}
	
	@Override
	public boolean matches(ItemStack stack, BottomlessBundleItem.BottomlessStack component) {
		// component.variant() already returns an ItemStack, so pass it directly to ItemPredicate.test(...)
		return template.test(component.variant()) && count.test(component.count());
	}
	
}