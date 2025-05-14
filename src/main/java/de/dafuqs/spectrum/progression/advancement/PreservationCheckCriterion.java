package de.dafuqs.spectrum.progression.advancement;

import com.mojang.serialization.*;
import com.mojang.serialization.codecs.*;
import de.dafuqs.spectrum.*;
import net.minecraft.advancements.critereon.*;
import net.minecraft.resources.*;
import net.minecraft.server.level.*;

import java.util.*;

public class PreservationCheckCriterion extends SimpleCriterionTrigger<PreservationCheckCriterion.Conditions> {
	
	public static final ResourceLocation ID = SpectrumCommon.locate("preservation_check");
	
	public void trigger(ServerPlayer player, String checkName, boolean checkPassed) {
		this.trigger(player, (conditions) -> conditions.matches(checkName, checkPassed));
	}
	
	@Override
	public Codec<Conditions> codec() {
		return Conditions.CODEC;
	}
	
	public record Conditions(
			Optional<ContextAwarePredicate> player,
			Optional<String> checkName,
			Optional<Boolean> checkPassed
	) implements SimpleCriterionTrigger.SimpleInstance {
		
		public static final Codec<Conditions> CODEC = RecordCodecBuilder.create(instance -> instance.group(
				ContextAwarePredicate.CODEC.optionalFieldOf("player").forGetter(Conditions::player),
				Codec.STRING.optionalFieldOf("check_name").forGetter(Conditions::checkName),
				Codec.BOOL.optionalFieldOf("check_passed").forGetter(Conditions::checkPassed)
		).apply(instance, Conditions::new));
		
		public boolean matches(String name, boolean checkPassed) {
			return (this.checkPassed.isEmpty() || this.checkPassed.get() == checkPassed)
					&& (this.checkName.isEmpty() || this.checkName.get().equals(name));
		}
	}
	
}
