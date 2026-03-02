package de.dafuqs.spectrum.api.predicate.item;

import com.mojang.serialization.*;
import com.mojang.serialization.codecs.*;
import de.dafuqs.spectrum.blocks.bottomless_bundle.*;
import de.dafuqs.spectrum.progression.advancement.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.advancements.critereon.*;
import net.minecraft.core.component.*;
import net.minecraft.world.item.*;
import org.jetbrains.annotations.*;

public record BottomlessStackPredicate(ItemPredicate template, LongRange count) implements SingleComponentItemPredicate<BottomlessItemHandlerComponent> {
	
	public static Codec<BottomlessStackPredicate> CODEC = RecordCodecBuilder.create(i -> i.group(
			ItemPredicate.CODEC.optionalFieldOf("variant", ItemPredicate.Builder.item().build()).forGetter(c -> c.template),
			LongRange.CODEC.optionalFieldOf("count", LongRange.ANY).forGetter(c -> c.count)
	).apply(i, BottomlessStackPredicate::new));
	
	@Override
	public @NotNull DataComponentType<BottomlessItemHandlerComponent> componentType() {
		return SpectrumDataComponentTypes.BOTTOMLESS_STACK.get();
	}
	
	@Override
	public boolean matches(@NotNull ItemStack stack, BottomlessItemHandlerComponent component) {
		return template.test(component.handler().variant()) && count.test(component.handler().count());
	}
	
}