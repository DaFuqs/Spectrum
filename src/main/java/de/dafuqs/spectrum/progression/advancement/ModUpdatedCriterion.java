package de.dafuqs.spectrum.progression.advancement;

import com.mojang.serialization.*;
import com.mojang.serialization.codecs.*;
import de.dafuqs.spectrum.api.*;
import net.minecraft.advancements.critereon.*;
import net.minecraft.server.level.*;

import java.util.*;

public class ModUpdatedCriterion extends SimpleCriterionTrigger<ModUpdatedCriterion.Conditions> {
	
	public static final String NAME = "mod_updated";
	
	public void trigger(ServerPlayer player, int previousVersion, int currentVersion) {
		this.trigger(player, (conditions) -> conditions.matches(previousVersion, currentVersion));
	}
	
	@Override
	public Codec<Conditions> codec() {
		return Conditions.CODEC;
	}
	
	public record Conditions(Optional<ContextAwarePredicate> player, MinMaxBounds.Ints previousVersion, MinMaxBounds.Ints currentVersion) implements SimpleInstance {
		
		public static final Codec<ModUpdatedCriterion.Conditions> CODEC = RecordCodecBuilder.create(instance -> instance.group(
				EntityPredicate.ADVANCEMENT_CODEC.optionalFieldOf("player").forGetter(ModUpdatedCriterion.Conditions::player),
				MinMaxBounds.Ints.CODEC.fieldOf("previous_version").forGetter(ModUpdatedCriterion.Conditions::previousVersion),
				MinMaxBounds.Ints.CODEC.fieldOf("current_version").forGetter(ModUpdatedCriterion.Conditions::currentVersion)
		).apply(instance, Conditions::new));
		
		public boolean matches(int previousVersion, int currentVersion) {
			if(!this.currentVersion.matches(currentVersion)) {
				return false;
			}
			return this.previousVersion.matches(previousVersion);
		}
	}
	
}
