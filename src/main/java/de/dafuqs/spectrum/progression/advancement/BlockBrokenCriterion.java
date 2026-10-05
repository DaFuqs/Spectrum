package de.dafuqs.spectrum.progression.advancement;

import com.mojang.serialization.*;
import com.mojang.serialization.codecs.*;
import de.dafuqs.spectrum.api.predicate.block.*;
import net.minecraft.advancements.critereon.*;
import net.minecraft.core.*;
import net.minecraft.server.level.*;
import net.minecraft.world.item.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.state.*;
import net.minecraft.world.level.storage.loot.*;
import net.minecraft.world.level.storage.loot.parameters.*;

import java.util.*;

public class BlockBrokenCriterion extends SimpleCriterionTrigger<BlockBrokenCriterion.Conditions> {
	
	public static final String NAME = "block_broken";
	
	public void trigger(ServerPlayer player, BlockPos pos, ItemStack stack) {
		ServerLevel serverlevel = player.serverLevel();
		BlockState blockstate = serverlevel.getBlockState(pos);
		LootParams lootparams = new LootParams.Builder(serverlevel)
				.withParameter(LootContextParams.ORIGIN, pos.getCenter())
				.withParameter(LootContextParams.THIS_ENTITY, player)
				.withParameter(LootContextParams.BLOCK_STATE, blockstate)
				.withParameter(LootContextParams.TOOL, stack)
				.create(LootContextParamSets.ADVANCEMENT_LOCATION);
		LootContext lootcontext = new LootContext.Builder(lootparams).create(Optional.empty());
		this.trigger(player, (conditions) -> conditions.matches(lootcontext));
	}
	
	@Override
	public Codec<Conditions> codec() {
		return Conditions.CODEC;
	}
	
	public record Conditions(Optional<ContextAwarePredicate> player, Optional<ContextAwarePredicate> location) implements SimpleCriterionTrigger.SimpleInstance {
		
		public static final Codec<BlockBrokenCriterion.Conditions> CODEC = RecordCodecBuilder.create(instance -> instance.group(
				EntityPredicate.ADVANCEMENT_CODEC.optionalFieldOf("player").forGetter(BlockBrokenCriterion.Conditions::player),
				ContextAwarePredicate.CODEC.optionalFieldOf("location").forGetter(BlockBrokenCriterion.Conditions::location)
		).apply(instance, Conditions::new));
		
		public boolean matches(LootContext lootContext) {
			if (location.isEmpty()) {
				return true;
			}
			return this.location.get().matches(lootContext);
		}
	}
	
}
