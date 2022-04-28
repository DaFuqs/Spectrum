package de.dafuqs.spectrum.blocks.mob_blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public abstract class MobBlock extends Block {
	
	public MobBlock(Settings settings) {
		super(settings);
	}
	
	@Override
	public void appendTooltip(ItemStack stack, @Nullable BlockView world, List<Text> tooltip, TooltipContext options) {
		super.appendTooltip(stack, world, tooltip, options);
		tooltip.add(new TranslatableText( "block.spectrum.mob_block.tooltip").formatted(Formatting.GRAY));
	}
	
	@Override
	public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
		if(!world.isClient) {
			trigger((ServerWorld) world, pos, state, player, hit.getSide());
		}
		return super.onUse(state, world, pos, player, hand, hit);
	}
	
	@Override
	public void onSteppedOn(World world, BlockPos pos, BlockState state, Entity entity) {
		super.onSteppedOn(world, pos, state, entity);
		if(!world.isClient) {
			trigger((ServerWorld) world, pos, state, entity, Direction.DOWN);
		}
	}
	
	public void onProjectileHit(World world, BlockState state, BlockHitResult hit, ProjectileEntity projectile) {
		if(!world.isClient) {
			trigger((ServerWorld) world, hit.getBlockPos(), state, projectile.getOwner(), hit.getSide());
		}
	}
	
	public abstract void trigger(ServerWorld world, BlockPos blockPos, BlockState state, @Nullable Entity entity, Direction side);
	
	
}
