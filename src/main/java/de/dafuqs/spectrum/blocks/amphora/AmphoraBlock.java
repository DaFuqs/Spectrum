package de.dafuqs.spectrum.blocks.amphora;

import net.minecraft.block.BarrelBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.PiglinBrain;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.stat.Stats;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class AmphoraBlock extends BarrelBlock {
	
	public AmphoraBlock(Settings settings) {
		super(settings);
	}
	
	public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
		if (world.isClient) {
			return ActionResult.SUCCESS;
		} else {
			BlockEntity blockEntity = world.getBlockEntity(pos);
			if (blockEntity instanceof AmphoraBlockEntity amphoraBlockEntity) {
				player.openHandledScreen(amphoraBlockEntity);
				player.incrementStat(Stats.OPEN_BARREL);
				PiglinBrain.onGuardedBlockInteracted(player, true);
			}
			
			return ActionResult.CONSUME;
		}
	}
	
	@Nullable
	public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
		return new AmphoraBlockEntity(pos, state);
	}
	
	public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
		BlockEntity blockEntity = world.getBlockEntity(pos);
		if (blockEntity instanceof AmphoraBlockEntity amphoraBlockEntity) {
			amphoraBlockEntity.tick();
		}
	}
	
	public void onPlaced(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
		if (itemStack.hasCustomName()) {
			BlockEntity blockEntity = world.getBlockEntity(pos);
			if (blockEntity instanceof AmphoraBlockEntity amphoraBlockEntity) {
				amphoraBlockEntity.setCustomName(itemStack.getName());
			}
		}
	}
	
}
