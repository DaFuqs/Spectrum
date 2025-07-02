package de.dafuqs.spectrum.entity.predicates;

import com.mojang.serialization.*;
import com.mojang.serialization.codecs.*;
import de.dafuqs.spectrum.entity.*;
import de.dafuqs.spectrum.entity.entity.*;
import net.minecraft.advancements.critereon.*;
import net.minecraft.server.level.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.item.*;
import net.minecraft.world.phys.*;
import org.jetbrains.annotations.*;

import java.util.*;

public record EggLayingWoolyPigPredicate(Optional<DyeColor> color, Optional<Boolean> hatless, Optional<Boolean> sheared) implements EntitySubPredicate {

	public static final MapCodec<EggLayingWoolyPigPredicate> CODEC = RecordCodecBuilder.mapCodec((instance) -> instance.group(
			DyeColor.CODEC.optionalFieldOf("color").forGetter(EggLayingWoolyPigPredicate::color),
			Codec.BOOL.optionalFieldOf("hatless").forGetter(EggLayingWoolyPigPredicate::hatless),
			Codec.BOOL.optionalFieldOf("sheared").forGetter(EggLayingWoolyPigPredicate::sheared)
	).apply(instance, EggLayingWoolyPigPredicate::new));

	@Override
	public boolean matches(Entity entity, ServerLevel world, @Nullable Vec3 pos) {
		if (!(entity instanceof EggLayingWoolyPigEntity wooly)) {
			return false;
		} else {
			return (this.color.isEmpty() || this.color.get() == wooly.getColor())
					&& (this.hatless.isEmpty() || this.hatless.get() == wooly.isHatless())
					&& (this.sheared.isEmpty() || this.sheared.get() == wooly.isSheared());
		}
	}

	@Override
	public MapCodec<EggLayingWoolyPigPredicate> codec() {
		return SpectrumEntitySubPredicateTypes.EGG_LAYING_WOOLY_PIG;
	}

}
