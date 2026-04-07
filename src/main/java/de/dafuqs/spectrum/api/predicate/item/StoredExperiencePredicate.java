package de.dafuqs.spectrum.api.predicate.item;

import com.mojang.serialization.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.advancements.critereon.*;
import net.minecraft.core.component.*;
import net.minecraft.world.item.*;
import org.jetbrains.annotations.*;

public record StoredExperiencePredicate(MinMaxBounds.Ints experience) implements SingleComponentItemPredicate<Integer> {
	
	public static final Codec<StoredExperiencePredicate> CODEC = MinMaxBounds.Ints.CODEC.xmap(StoredExperiencePredicate::new, StoredExperiencePredicate::experience);
	
	public StoredExperiencePredicate(MinMaxBounds.Ints experience) {
		this.experience = experience;
	}
	
	@Override
	public @NotNull DataComponentType<Integer> componentType() {
		return SpectrumDataComponentTypes.STORED_EXPERIENCE.get();
	}
	
	@Override
	public boolean matches(@NotNull ItemStack stack, @NotNull Integer component) {
		return experience.matches(component);
	}
	
}
