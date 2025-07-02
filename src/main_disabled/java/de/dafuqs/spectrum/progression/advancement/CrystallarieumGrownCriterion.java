package de.dafuqs.spectrum.progression.advancement;

import com.mojang.serialization.*;
import com.mojang.serialization.codecs.*;
import de.dafuqs.spectrum.*;
import net.minecraft.advancements.critereon.*;
import net.minecraft.core.*;
import net.minecraft.resources.*;
import net.minecraft.server.level.*;
import net.minecraft.world.item.*;

import java.util.*;

public class CrystallarieumGrownCriterion extends SimpleCriterionTrigger<CrystallarieumGrownCriterion.Conditions> {
	
	public static final ResourceLocation ID = SpectrumCommon.locate("crystallarieum_growing");
	
	public void trigger(ServerPlayer player, ServerLevel world, BlockPos pos, ItemStack catalystStack) {
		this.trigger(player, (conditions) -> conditions.matches(world, pos, catalystStack));
	}
	
	@Override
	public Codec<Conditions> codec() {
		return Conditions.CODEC;
	}
	
	public record Conditions(
			Optional<ContextAwarePredicate> player,
			BlockPredicate blockPredicate,
			ItemPredicate catalystPredicate
	) implements SimpleCriterionTrigger.SimpleInstance {
		
		public static final Codec<CrystallarieumGrownCriterion.Conditions> CODEC = RecordCodecBuilder.create(instance -> instance.group(
				ContextAwarePredicate.CODEC.optionalFieldOf("player").forGetter(CrystallarieumGrownCriterion.Conditions::player),
				BlockPredicate.CODEC.optionalFieldOf("grown_block", BlockPredicate.Builder.block().build()).forGetter(CrystallarieumGrownCriterion.Conditions::blockPredicate),
				ItemPredicate.CODEC.optionalFieldOf("used_catalyst", ItemPredicate.Builder.item().build()).forGetter(CrystallarieumGrownCriterion.Conditions::catalystPredicate)
		).apply(instance, CrystallarieumGrownCriterion.Conditions::new));
		
		public boolean matches(ServerLevel world, BlockPos blockPos, ItemStack catalystStack) {
			return this.blockPredicate.matches(world, blockPos) && this.catalystPredicate.test(catalystStack);
		}
	}
	
}
