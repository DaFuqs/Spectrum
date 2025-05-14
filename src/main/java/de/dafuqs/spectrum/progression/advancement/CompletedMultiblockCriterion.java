package de.dafuqs.spectrum.progression.advancement;

import com.klikli_dev.modonomicon.api.multiblock.*;
import com.mojang.serialization.*;
import com.mojang.serialization.codecs.*;
import de.dafuqs.spectrum.*;
import net.minecraft.advancements.critereon.*;
import net.minecraft.resources.*;
import net.minecraft.server.level.*;

import java.util.*;

public class CompletedMultiblockCriterion extends SimpleCriterionTrigger<CompletedMultiblockCriterion.Conditions> {
	
	public static final ResourceLocation ID = SpectrumCommon.locate("completed_multiblock");
	
	public void trigger(ServerPlayer player, Multiblock iMultiblock) {
		this.trigger(player, (conditions) -> conditions.matches(iMultiblock));
	}
	
	@Override
	public Codec<Conditions> codec() {
		return Conditions.CODEC;
	}
	
	public record Conditions(Optional<ContextAwarePredicate> player, Optional<ResourceLocation> identifier) implements SimpleCriterionTrigger.SimpleInstance {
		
		public static final Codec<Conditions> CODEC = RecordCodecBuilder.create(instance -> instance.group(
				ContextAwarePredicate.CODEC.optionalFieldOf("player").forGetter(Conditions::player),
				ResourceLocation.CODEC.optionalFieldOf("multiblock_identifier").forGetter(Conditions::identifier)
		).apply(instance, Conditions::new));
		
		public boolean matches(Multiblock multiblock) {
			return identifier.isEmpty() || multiblock.getId().equals(identifier.get());
		}
	}
	
}
