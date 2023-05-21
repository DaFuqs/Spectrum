package de.dafuqs.spectrum.blocks.mob_blocks;

import net.minecraft.block.*;
import net.minecraft.client.item.*;
import net.minecraft.entity.*;
import net.minecraft.entity.mob.*;
import net.minecraft.entity.player.*;
import net.minecraft.item.*;
import net.minecraft.loot.*;
import net.minecraft.loot.context.*;
import net.minecraft.particle.*;
import net.minecraft.server.world.*;
import net.minecraft.text.*;
import net.minecraft.util.math.*;
import net.minecraft.world.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class PiglinTradeMobBlock extends MobBlock {
	
	public PiglinTradeMobBlock(Settings settings, ParticleEffect particleEffect) {
		super(settings, particleEffect);
	}
	
	private static List<ItemStack> getBarteredStacks(@NotNull ServerWorld world, BlockPos blockPos) {
		PiglinEntity piglin = new PiglinEntity(EntityType.PIGLIN, world);
		piglin.setPos(blockPos.getX(), blockPos.getY(), blockPos.getZ());
		
		LootTable lootTable = world.getServer().getLootManager().getTable(LootTables.PIGLIN_BARTERING_GAMEPLAY);
		List<ItemStack> loot = lootTable.generateLoot(new LootContext.Builder(world).parameter(LootContextParameters.THIS_ENTITY, piglin).random(world.random).build(LootContextTypes.BARTER));
		
		piglin.discard();
		
		return loot;
	}
	
	@Override
	public boolean trigger(ServerWorld world, BlockPos blockPos, BlockState state, @Nullable Entity entity, Direction side) {
		if (entity instanceof ItemEntity itemEntity) {
			ItemStack stack = itemEntity.getStack();
			if (stack.isOf(PiglinBrain.BARTERING_ITEM)) {
				int newAmount = stack.getCount() - 1;
				if (newAmount <= 0) {
					itemEntity.discard();
				} else {
					stack.decrement(1);
				}
				
				outputLoot(world, blockPos, side);
				return true;
			}
		} else if (entity instanceof PlayerEntity player) {
			for (ItemStack handStack : player.getHandItems()) {
				if (handStack.isOf(PiglinBrain.BARTERING_ITEM)) {
					handStack.decrement(1);
					
					outputLoot(world, blockPos, side);
					return true;
				}
			}
		}
		return false;
	}
	
	private void outputLoot(ServerWorld world, BlockPos blockPos, Direction side) {
		Position outputLocation = getOutputLocation(new BlockPointerImpl(world, blockPos), side);
		for (ItemStack barteredStack : getBarteredStacks(world, blockPos)) {
			ItemEntity itemEntity = new ItemEntity(world, outputLocation.getX(), outputLocation.getY(), outputLocation.getZ(), barteredStack);
			itemEntity.addVelocity(side.getOffsetX() * 0.25, side.getOffsetY() * 0.25 + 0.03, side.getOffsetZ() * 0.25);
			world.spawnEntity(itemEntity);
		}
	}
	
	@Override
	public void appendTooltip(ItemStack stack, @Nullable BlockView world, List<Text> tooltip, TooltipContext options) {
		super.appendTooltip(stack, world, tooltip, options);
		tooltip.add(Text.translatable("block.spectrum.piglin_trade_mob_block.tooltip"));
	}
	
}
