package de.dafuqs.spectrum.blocks.idols;

import com.mojang.serialization.*;
import net.minecraft.core.*;
import net.minecraft.core.particles.*;
import net.minecraft.network.chat.*;
import net.minecraft.server.level.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.item.*;
import net.minecraft.world.entity.monster.piglin.*;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.state.*;
import net.minecraft.world.level.storage.loot.*;
import net.minecraft.world.level.storage.loot.parameters.*;
import net.minecraft.world.phys.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class PiglinTradeIdolBlock extends IdolBlock {
	
	public PiglinTradeIdolBlock(Properties settings, ParticleOptions particleEffect) {
		super(settings, particleEffect);
	}

	@Override
	public MapCodec<? extends PiglinTradeIdolBlock> codec() {
		//TODO: Make the codec
		return null;
	}
	
	private static List<ItemStack> getBarteredStacks(@NotNull ServerLevel world, BlockPos blockPos) {
		Piglin piglin = new Piglin(EntityType.PIGLIN, world);
		piglin.setPosRaw(blockPos.getX(), blockPos.getY(), blockPos.getZ());
		
		LootTable lootTable = world.getServer().reloadableRegistries().getLootTable(BuiltInLootTables.PIGLIN_BARTERING);
		List<ItemStack> loot = lootTable.getRandomItems(new LootParams.Builder(world).withParameter(LootContextParams.THIS_ENTITY, piglin).create(LootContextParamSets.PIGLIN_BARTER));
		
		piglin.discard();
		
		return loot;
	}
	
	@Override
	public boolean trigger(ServerLevel world, BlockPos blockPos, BlockState state, @Nullable Entity entity, Direction side) {
		if (entity instanceof ItemEntity itemEntity) {
			ItemStack stack = itemEntity.getItem();
			if (stack.is(PiglinAi.BARTERING_ITEM)) {
				int newAmount = stack.getCount() - 1;
				if (newAmount <= 0) {
					itemEntity.discard();
				} else {
					stack.shrink(1);
				}
				
				outputLoot(world, blockPos, side);
				return true;
			}
		} else if (entity instanceof Player player) {
			for (ItemStack handStack : player.getHandSlots()) {
				if (handStack.is(PiglinAi.BARTERING_ITEM)) {
					handStack.shrink(1);
					
					outputLoot(world, blockPos, side);
					return true;
				}
			}
		}
		return false;
	}
	
	private void outputLoot(ServerLevel world, BlockPos blockPos, Direction side) {
		Vec3 outputLocation = getOutputLocation(blockPos, side);
		for (ItemStack barteredStack : getBarteredStacks(world, blockPos)) {
			ItemEntity itemEntity = new ItemEntity(world, outputLocation.x(), outputLocation.y(), outputLocation.z(), barteredStack);
			itemEntity.push(side.getStepX() * 0.25, side.getStepY() * 0.25 + 0.03, side.getStepZ() * 0.25);
			world.addFreshEntity(itemEntity);
		}
	}

	@Override
	public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltip, TooltipFlag type) {
		super.appendHoverText(stack, context, tooltip, type);
		tooltip.add(Component.translatable("block.spectrum.piglin_trade_idol.tooltip"));
	}
	
}
