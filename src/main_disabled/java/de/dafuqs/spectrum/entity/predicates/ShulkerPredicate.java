package de.dafuqs.spectrum.entity.predicates;

import com.mojang.serialization.*;
import com.mojang.serialization.codecs.*;
import de.dafuqs.spectrum.entity.*;
import net.minecraft.advancements.critereon.*;
import net.minecraft.server.level.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.monster.*;
import net.minecraft.world.item.*;
import net.minecraft.world.phys.*;
import org.jetbrains.annotations.*;

import java.util.*;

public record ShulkerPredicate(Optional<DyeColor> color) implements EntitySubPredicate {

	public static final MapCodec<ShulkerPredicate> CODEC = RecordCodecBuilder.mapCodec((instance) -> instance.group(
			DyeColor.CODEC.optionalFieldOf("color").forGetter(ShulkerPredicate::color)
	).apply(instance, ShulkerPredicate::new));

	@Override
	public boolean matches(Entity entity, ServerLevel world, @Nullable Vec3 pos) {
		if (!(entity instanceof Shulker shulkerEntity)) {
			return false;
		} else {
			return (this.color.isEmpty() || this.color.get() == shulkerEntity.getColor());
		}
	}

	@Override
	public MapCodec<ShulkerPredicate> codec() {
		return SpectrumEntitySubPredicateTypes.SHULKER;
	}

}
