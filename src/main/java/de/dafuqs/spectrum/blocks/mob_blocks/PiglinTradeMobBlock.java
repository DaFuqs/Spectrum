package de.dafuqs.spectrum.blocks.mob_blocks;

import net.minecraft.block.BlockState;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.mob.PiglinBrain;
import net.minecraft.entity.mob.PiglinEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.LootTables;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.loot.context.LootContextTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class PiglinTradeMobBlock extends MobBlock {
	
	public PiglinTradeMobBlock(Settings settings) {
		super(settings);
	}
	
	@Override
	public void trigger(ServerWorld world, BlockPos blockPos, BlockState state, @Nullable Entity entity, Direction side) {
		if(entity instanceof PlayerEntity player) {
			for(ItemStack handStack : player.getItemsHand()) {
				if(handStack.isOf(PiglinBrain.BARTERING_ITEM)) {
					handStack.decrement(1);
					for(ItemStack barteredStack : getBarteredItem(world)) {
						BlockPos offsetPos = blockPos.offset(side);
						ItemEntity itemEntity = new ItemEntity(world, offsetPos.getX() + 0.5, offsetPos.getY() + 0.5, offsetPos.getX() + 0.5, barteredStack);
						itemEntity.addVelocity(side.getOffsetX() * 0.5, side.getOffsetY() * 0.5 + 0.2, side.getOffsetZ() * 0.5);
						world.spawnEntity(itemEntity);
					}
					return;
				}
			}
		}
	}
	
	private static List<ItemStack> getBarteredItem(@NotNull ServerWorld world) {
		LootTable lootTable = world.getServer().getLootManager().getTable(LootTables.PIGLIN_BARTERING_GAMEPLAY);
		return lootTable.generateLoot(new LootContext.Builder(world).parameter(LootContextParameters.THIS_ENTITY, null).random(world.random).build(LootContextTypes.BARTER));
	}
	
	@Override
	public void appendTooltip(ItemStack stack, @Nullable BlockView world, List<Text> tooltip, TooltipContext options) {
		super.appendTooltip(stack, world, tooltip, options);
		tooltip.add(new TranslatableText( "block.spectrum.piglin_trade_mob_block.tooltip"));
	}
	
}
