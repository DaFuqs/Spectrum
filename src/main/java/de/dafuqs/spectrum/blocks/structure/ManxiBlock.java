package de.dafuqs.spectrum.blocks.structure;

import de.dafuqs.spectrum.registries.*;
import net.minecraft.block.*;
import net.minecraft.block.entity.*;
import net.minecraft.entity.player.*;
import net.minecraft.item.*;
import net.minecraft.sound.*;
import net.minecraft.state.*;
import net.minecraft.util.*;
import net.minecraft.util.hit.*;
import net.minecraft.util.math.*;
import net.minecraft.world.*;
import org.jetbrains.annotations.*;

public class ManxiBlock extends HorizontalFacingBlock implements BlockEntityProvider {

	public ManxiBlock(Settings settings) {
		super(settings);
	}

	@Nullable
	@Override
	public BlockState getPlacementState(ItemPlacementContext ctx) {
		return this.getDefaultState().with(FACING, ctx.getHorizontalPlayerFacing().getOpposite());
	}

	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		builder.add(FACING);
	}

	@Override
	public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
		var entity = world.getBlockEntity(pos);

		if (!(entity instanceof ManxiBlockEntity manxi))
			return ActionResult.PASS;

		if (manxi.hasTaken(player))
			return ActionResult.FAIL;

		world.playSoundAtBlockCenter(pos, SoundEvents.BLOCK_CHISELED_BOOKSHELF_PICKUP_ENCHANTED, SoundCategory.BLOCKS, 1F, 1F, true);
		player.getInventory().offerOrDrop(SpectrumItems.POISONERS_HANDBOOK.getDefaultStack());
		manxi.markTaken(player);

		return ActionResult.CONSUME;
	}

	@Nullable
	@Override
	public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
		return new ManxiBlockEntity(pos, state);
	}
}