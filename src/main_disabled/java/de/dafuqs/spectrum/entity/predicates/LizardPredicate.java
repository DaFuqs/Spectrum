package de.dafuqs.spectrum.entity.predicates;

import com.mojang.serialization.*;
import com.mojang.serialization.codecs.*;
import de.dafuqs.spectrum.api.energy.color.*;
import de.dafuqs.spectrum.entity.*;
import de.dafuqs.spectrum.entity.entity.*;
import de.dafuqs.spectrum.entity.variants.*;
import net.minecraft.advancements.critereon.*;
import net.minecraft.server.level.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.phys.*;
import org.jetbrains.annotations.*;

import java.util.*;

public record LizardPredicate(Optional<InkColor> color, Optional<LizardFrillVariant> frills, Optional<LizardHornVariant> horns) implements EntitySubPredicate {

	public static final MapCodec<LizardPredicate> CODEC = RecordCodecBuilder.mapCodec((instance) -> instance.group(
			InkColor.CODEC.optionalFieldOf("color").forGetter(LizardPredicate::color),
			LizardFrillVariant.CODEC.optionalFieldOf("frills_variant").forGetter(LizardPredicate::frills),
			LizardHornVariant.CODEC.optionalFieldOf("horn_variant").forGetter(LizardPredicate::horns)
	).apply(instance, LizardPredicate::new));

	@Override
	public boolean matches(Entity entity, ServerLevel world, @Nullable Vec3 pos) {
		if (!(entity instanceof LizardEntity lizard)) {
			return false;
		} else {
			return (this.color.isEmpty() || this.color.get() == lizard.getColor())
					&& (this.frills.isEmpty() || this.frills.get() == lizard.getFrills())
					&& (this.horns.isEmpty() || this.horns.get() == lizard.getHorns());
		}
	}

	@Override
	public MapCodec<LizardPredicate> codec() {
		return SpectrumEntitySubPredicateTypes.LIZARD;
	}
	
}
