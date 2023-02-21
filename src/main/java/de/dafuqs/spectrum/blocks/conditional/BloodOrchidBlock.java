package de.dafuqs.spectrum.blocks.conditional;

import de.dafuqs.revelationary.api.revelations.*;
import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.helpers.*;
import de.dafuqs.spectrum.progression.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.block.*;
import net.minecraft.entity.effect.*;
import net.minecraft.entity.player.*;
import net.minecraft.item.*;
import net.minecraft.server.network.*;
import net.minecraft.server.world.*;
import net.minecraft.sound.*;
import net.minecraft.state.*;
import net.minecraft.state.property.Properties;
import net.minecraft.state.property.*;
import net.minecraft.util.*;
import net.minecraft.util.hit.*;
import net.minecraft.util.math.*;
import net.minecraft.world.*;

import java.util.*;

public class BloodOrchidBlock extends FlowerBlock implements RevelationAware {
	
	public static final IntProperty AGE = Properties.AGE_5;
	
	public BloodOrchidBlock(StatusEffect suspiciousStewEffect, int effectDuration, Settings settings) {
		super(suspiciousStewEffect, effectDuration, settings);
		this.setDefaultState(this.stateManager.getDefaultState().with(AGE, 0));
		RevelationAware.register(this);
	}
	
	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		builder.add(AGE);
	}
	
	@Override
	public void randomTick(BlockState state, ServerWorld world, BlockPos pos, net.minecraft.util.math.random.Random random) {
		int age = state.get(AGE);
		if (age < Properties.AGE_5_MAX) {
			if (random.nextFloat() > 0.25) {
				return;
			}
			BlockState newState = state.with(AGE, age + 1);
			world.setBlockState(pos, newState);
			world.playSound(null, pos, state.getSoundGroup().getPlaceSound(), SoundCategory.BLOCKS, 1.0F, 1.0F);
		}
	}
	
	@Override
	public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
		int age = state.get(AGE);
		if (age > 0) {
			if (world.isClient) {
				return ActionResult.SUCCESS;
			} else {
				world.setBlockState(pos, state.with(AGE, age - 1));
				Support.givePlayer(player, SpectrumItems.BLOOD_ORCHID_PETAL.getDefaultStack());
				world.playSound(null, pos, SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.BLOCKS, 1.0F, 0.9F + world.random.nextFloat() * 0.2F);
				if (player instanceof ServerPlayerEntity serverPlayerEntity) {
					SpectrumAdvancementCriteria.BLOOD_ORCHID_PLUCKING.trigger(serverPlayerEntity);
				}
				return ActionResult.CONSUME;
			}
		}
		return ActionResult.PASS;
	}
	
	@Override
	public Identifier getCloakAdvancementIdentifier() {
		return SpectrumCommon.locate("midgame/collect_blood_orchid_petal");
	}
	
	@Override
	public Map<BlockState, BlockState> getBlockStateCloaks() {
		Hashtable<BlockState, BlockState> hashtable = new Hashtable<>();
		for (int i = 0; i <= Properties.AGE_5_MAX; i++) {
			hashtable.put(this.getDefaultState().with(AGE, i), Blocks.RED_TULIP.getDefaultState());
		}
		return hashtable;
	}
	
	@Override
	public Pair<Item, Item> getItemCloak() {
		return new Pair<>(this.asItem(), Blocks.RED_TULIP.asItem());
	}
	
}
