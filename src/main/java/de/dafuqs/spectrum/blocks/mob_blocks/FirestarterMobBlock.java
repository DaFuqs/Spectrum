package de.dafuqs.spectrum.blocks.mob_blocks;

import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.block.*;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.FlintAndSteelItem;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.property.Properties;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class FirestarterMobBlock extends MobBlock {
	
	public FirestarterMobBlock(Settings settings) {
		super(settings);
	}
	
	@Override
	public void trigger(ServerWorld world, BlockPos blockPos, BlockState state, @Nullable Entity entity, Direction side) {
		for(Direction direction : Direction.values()) {
			causeFire(world, blockPos.offset(direction), direction);
		}
	}
	
	public static boolean causeFire(ServerWorld world, BlockPos blockPos, Direction side) {
		BlockState blockState = world.getBlockState(blockPos);
		if (!CampfireBlock.canBeLit(blockState) && !CandleBlock.canBeLit(blockState) && !CandleCakeBlock.canBeLit(blockState)) {
			BlockPos blockPos2 = blockPos.offset(side);
			if (AbstractFireBlock.canPlaceAt(world, blockPos2, side)) {
				world.playSound(null, blockPos2, SoundEvents.ITEM_FLINTANDSTEEL_USE, SoundCategory.BLOCKS, 1.0F, world.getRandom().nextFloat() * 0.4F + 0.8F);
				BlockState blockState2 = AbstractFireBlock.getState(world, blockPos2);
				world.setBlockState(blockPos2, blockState2, 11);
				world.emitGameEvent(null, GameEvent.BLOCK_PLACE, blockPos);
				return true;
			} else {
				return false;
			}
		} else {
			world.playSound(null, blockPos, SoundEvents.ITEM_FLINTANDSTEEL_USE, SoundCategory.BLOCKS, 1.0F, world.getRandom().nextFloat() * 0.4F + 0.8F);
			world.setBlockState(blockPos, blockState.with(Properties.LIT, true), 11);
			world.emitGameEvent(null, GameEvent.BLOCK_PLACE, blockPos);
			return true;
		}
	}
	
	@Override
	public void appendTooltip(ItemStack stack, @Nullable BlockView world, List<Text> tooltip, TooltipContext options) {
		super.appendTooltip(stack, world, tooltip, options);
		tooltip.add(new TranslatableText( "block.spectrum.firestarter_mob_block.tooltip"));
	}
	
}
