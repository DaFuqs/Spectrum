package de.dafuqs.spectrum.api.predicate.item;

import com.mojang.serialization.*;
import de.dafuqs.spectrum.components.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.advancements.critereon.*;
import net.minecraft.core.component.*;
import net.minecraft.world.item.*;

public record SweetenedPredicate(boolean sweetened) implements SingleComponentItemPredicate<JadeWineComponent> {
	
	public static final Codec<SweetenedPredicate> CODEC = Codec.BOOL.xmap(SweetenedPredicate::new, SweetenedPredicate::sweetened);
	
	@Override
	public DataComponentType<JadeWineComponent> componentType() {
		return SpectrumDataComponentTypes.JADE_WINE;
	}
	
	@Override
	public boolean matches(ItemStack stack, JadeWineComponent component) {
		return component.sweetened() == sweetened;
	}
	
}
