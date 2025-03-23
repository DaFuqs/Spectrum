package de.dafuqs.spectrum.blocks.structure;

import de.dafuqs.spectrum.blocks.*;
import de.dafuqs.spectrum.registries.*;
import de.dafuqs.spectrum.status_effects.*;
import net.minecraft.block.*;
import net.minecraft.block.entity.*;
import net.minecraft.entity.player.*;
import net.minecraft.sound.*;
import net.minecraft.util.*;
import net.minecraft.util.hit.*;
import net.minecraft.util.math.*;
import net.minecraft.util.shape.*;
import net.minecraft.world.*;
import org.jetbrains.annotations.*;

public class TreasureItemBowlBlock extends Block implements BlockEntityProvider {

	protected static final VoxelShape SHAPE = Block.createCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 11.0D, 16.0D);

	public TreasureItemBowlBlock(Settings settings) {
		super(settings);
	}

	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		return SHAPE;
	}

	@Override
	public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
		var entity = world.getBlockEntity(pos);

		if (!(entity instanceof PlayerTrackerBlockEntity bowl))
			return ActionResult.PASS;

		if (bowl.hasTaken(player) || !canInteract(player))
			return ActionResult.FAIL;

		world.playSoundAtBlockCenter(pos, SoundEvents.BLOCK_ENCHANTMENT_TABLE_USE, SoundCategory.BLOCKS, 1F, 1F, true);
		player.getInventory().offerOrDrop(SpectrumItems.AETHER_GRACED_NECTAR_GLOVES.getDefaultStack());
		bowl.markTaken(player);

		return ActionResult.CONSUME;
	}

	public static boolean canInteract(PlayerEntity player) {
		return player.hasStatusEffect(SpectrumStatusEffects.FATAL_SLUMBER);
	}

	@Nullable
	@Override
	public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
		return new PlayerTrackerBlockEntity(pos, state);
	}
}
