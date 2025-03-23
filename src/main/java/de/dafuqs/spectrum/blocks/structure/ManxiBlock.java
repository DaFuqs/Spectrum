package de.dafuqs.spectrum.blocks.structure;

import de.dafuqs.spectrum.registries.*;
import net.minecraft.block.*;
import net.minecraft.block.entity.*;
import net.minecraft.entity.player.*;
import net.minecraft.item.*;
import net.minecraft.sound.*;
import net.minecraft.state.*;
import net.minecraft.text.*;
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

		if (!(entity instanceof PlayerTrackerBlockEntity manxi))
			return ActionResult.PASS;

		if (manxi.hasTaken(player))
			return ActionResult.FAIL;

		world.playSoundAtBlockCenter(pos, SoundEvents.BLOCK_CHISELED_BOOKSHELF_PICKUP_ENCHANTED, SoundCategory.BLOCKS, 1F, 1F, true);
		player.getInventory().offerOrDrop(SpectrumItems.POISONERS_HANDBOOK.getDefaultStack());
		manxi.markTaken(player);

		return ActionResult.CONSUME;
	}

	@Override
	public void onBlockBreakStart(BlockState state, World world, BlockPos pos, PlayerEntity player) {
		if (!world.isClient() && !player.getAbilities().creativeMode) {
			player.sendMessage(Text.translatable("block.spectrum.manxi.nope").styled(s -> s.withColor(SpectrumStatusEffects.ETERNAL_SLUMBER_COLOR)), true);
			world.playSoundAtBlockCenter(pos, SpectrumSoundEvents.DEEP_CRYSTAL_RING, SoundCategory.BLOCKS, 1, 1.5F, true);
			player.damage(SpectrumDamageTypes.sleep(world, null), 6);
			player.takeKnockback(2, player.getX() - (pos.getX() + 0.5), player.getZ() - (pos.getZ() + 0.5));
		}
	}

	@Nullable
	@Override
	public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
		return new PlayerTrackerBlockEntity(pos, state);
	}
}