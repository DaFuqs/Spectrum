package de.dafuqs.spectrum.progression.advancement;

import com.mojang.serialization.*;
import com.mojang.serialization.codecs.*;
import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.api.predicate.block.*;
import net.minecraft.advancements.critereon.*;
import net.minecraft.resources.*;
import net.minecraft.server.level.*;
import net.minecraft.world.level.block.state.*;

import java.util.*;

public class BlockBrokenCriterion extends SimpleCriterionTrigger<BlockBrokenCriterion.Conditions> {
	
	public static final ResourceLocation ID = SpectrumCommon.locate("block_broken");
	
	public void trigger(ServerPlayer player, BlockState minedBlock) {
		this.trigger(player, (conditions) -> conditions.matches(minedBlock));
	}
	
	@Override
	public Codec<Conditions> codec() {
		return Conditions.CODEC;
	}
	
	public record Conditions(Optional<ContextAwarePredicate> player, Optional<BrokenBlockPredicate> blockPredicate) implements SimpleCriterionTrigger.SimpleInstance {
		
		public static final Codec<BlockBrokenCriterion.Conditions> CODEC = RecordCodecBuilder.create(instance -> instance.group(
				EntityPredicate.ADVANCEMENT_CODEC.optionalFieldOf("player").forGetter(BlockBrokenCriterion.Conditions::player),
				BrokenBlockPredicate.CODEC.optionalFieldOf("block").forGetter(BlockBrokenCriterion.Conditions::blockPredicate)
		).apply(instance, Conditions::new));
		
		public boolean matches(BlockState blockState) {
			if (blockPredicate.isEmpty()) {
				return true;
			}
			return this.blockPredicate.get().test(blockState);
		}
	}
	
}
