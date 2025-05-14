package de.dafuqs.spectrum.entity.predicates;

import com.mojang.serialization.*;
import com.mojang.serialization.codecs.*;
import de.dafuqs.spectrum.entity.*;
import de.dafuqs.spectrum.entity.entity.*;
import de.dafuqs.spectrum.entity.variants.*;
import net.minecraft.advancements.critereon.*;
import net.minecraft.server.level.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.phys.*;
import org.jetbrains.annotations.*;

import java.util.*;

public record KindlingPredicate(Optional<Boolean> clipped, Optional<Boolean> angry, Optional<KindlingVariant> variant) implements EntitySubPredicate {

	public static final MapCodec<KindlingPredicate> CODEC = RecordCodecBuilder.mapCodec((instance) -> instance.group(
			Codec.BOOL.optionalFieldOf("clipped").forGetter(KindlingPredicate::clipped),
			Codec.BOOL.optionalFieldOf("angry").forGetter(KindlingPredicate::angry),
			KindlingVariant.CODEC.optionalFieldOf("variant").forGetter(KindlingPredicate::variant)
	).apply(instance, KindlingPredicate::new));

	@Override
	public boolean matches(Entity entity, ServerLevel world, @Nullable Vec3 pos) {
		if (!(entity instanceof KindlingEntity kindling)) {
			return false;
		} else {
			return (this.clipped.isEmpty() || this.clipped.get() == kindling.isClipped())
					&& (this.angry.isEmpty() || this.angry.get() == (kindling.getRemainingPersistentAngerTime() == 0)
					&& (this.variant.isEmpty() || this.variant.get() == kindling.getKindlingVariant()));
		}
	}

	@Override
	public MapCodec<KindlingPredicate> codec() {
		return SpectrumEntitySubPredicateTypes.KINDLING;
	}

}
