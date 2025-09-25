package de.dafuqs.spectrum.blocks.structure;

import com.mojang.serialization.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.core.*;
import net.minecraft.network.chat.*;
import net.minecraft.sounds.*;
import net.minecraft.world.*;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.item.context.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.*;
import net.minecraft.world.level.block.state.*;
import net.minecraft.world.phys.*;
import org.jetbrains.annotations.*;

public class ManxiBlock extends HorizontalDirectionalBlock implements EntityBlock {
	
	public static final MapCodec<ManxiBlock> CODEC = simpleCodec(ManxiBlock::new);
	
	public ManxiBlock(Properties settings) {
		super(settings);
	}
	
	@Override
	public MapCodec<? extends ManxiBlock> codec() {
		return CODEC;
	}
	
	@Nullable
	@Override
	public BlockState getStateForPlacement(BlockPlaceContext ctx) {
		return this.defaultBlockState().setValue(FACING, ctx.getHorizontalDirection().getOpposite());
	}
	
	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(FACING);
	}
	
	@Override
	public InteractionResult useWithoutItem(BlockState state, Level world, BlockPos pos, Player player, BlockHitResult hit) {
		var entity = world.getBlockEntity(pos);
		
		if (!(entity instanceof PlayerTrackerBlockEntity manxi))
			return InteractionResult.PASS;
		
		if (manxi.hasTaken(player))
			return InteractionResult.FAIL;
		
		world.playLocalSound(pos, SoundEvents.CHISELED_BOOKSHELF_PICKUP_ENCHANTED, SoundSource.BLOCKS, 1F, 1F, true);
		player.getInventory().placeItemBackInInventory(SpectrumItems.POISONERS_HANDBOOK.get().getDefaultInstance());
		manxi.markTaken(player);
		
		return InteractionResult.CONSUME;
	}
	
	@Override
	public void attack(BlockState state, Level world, BlockPos pos, Player player) {
		if (player.getAbilities().instabuild) {
			return;
		}
		
		if (world.isClientSide()) {
			player.knockback(2, player.getX() - (pos.getX() + 0.5), player.getZ() - (pos.getZ() + 0.5));
		} else {
			player.displayClientMessage(Component.translatable("block.spectrum.manxi.nope").withStyle(s -> s.withColor(SpectrumStatusEffects.ETERNAL_SLUMBER_COLOR)), true);
			world.playLocalSound(pos, SpectrumSoundEvents.DEEP_CRYSTAL_RING, SoundSource.BLOCKS, 1, 1.5F, true);
			player.hurt(world.damageSources().indirectMagic(player, player), 6);
		}
	}
	
	@Nullable
	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new PlayerTrackerBlockEntity(pos, state);
	}
}
