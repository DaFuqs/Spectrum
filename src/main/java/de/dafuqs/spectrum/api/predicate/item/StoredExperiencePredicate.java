package de.dafuqs.spectrum.api.predicate.item;

import com.mojang.serialization.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.advancements.critereon.*;
import net.minecraft.core.component.*;
import net.minecraft.world.item.*;
import javax.annotation.*;

public record StoredExperiencePredicate(MinMaxBounds.Ints experience) implements SingleComponentItemPredicate<Integer> {
	
	public static final Codec<StoredExperiencePredicate> CODEC = MinMaxBounds.Ints.CODEC.xmap(StoredExperiencePredicate::new, StoredExperiencePredicate::experience);
	
	public StoredExperiencePredicate(MinMaxBounds.Ints experience) {
		this.experience = experience;
	}
	
	@Override
	public DataComponentType<Integer> componentType() {
		return SpectrumDataComponentTypes.STORED_EXPERIENCE.get();
	}
	
	@Override
	public boolean matches(ItemStack stack, Integer component) {
		return experience.matches(component);
	}
	
}
