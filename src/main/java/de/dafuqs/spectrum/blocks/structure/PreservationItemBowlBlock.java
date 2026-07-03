package de.dafuqs.spectrum.blocks.structure;

import com.mojang.serialization.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.core.*;
import net.minecraft.sounds.*;
import net.minecraft.world.*;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.*;
import net.minecraft.world.level.block.state.*;
import net.minecraft.world.phys.*;
import net.minecraft.world.phys.shapes.*;
import org.jspecify.annotations.Nullable;

public class PreservationItemBowlBlock extends Block implements EntityBlock {
	
	public static final MapCodec<PreservationItemBowlBlock> CODEC = simpleCodec(PreservationItemBowlBlock::new);
	
	protected static final VoxelShape SHAPE = Block.box(0.0D, 0.0D, 0.0D, 16.0D, 11.0D, 16.0D);
	
	public PreservationItemBowlBlock(Properties settings) {
		super(settings);
	}
	
	@Override
	public MapCodec<? extends PreservationItemBowlBlock> codec() {
		return CODEC;
	}
	
	@Override
	public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
		return SHAPE;
	}
	
	@Override
	public InteractionResult useWithoutItem(BlockState state, Level world, BlockPos pos, Player player, BlockHitResult hit) {
		var entity = world.getBlockEntity(pos);
		
		if (!(entity instanceof PlayerTrackerBlockEntity bowl))
			return InteractionResult.PASS;
		
		if (bowl.hasTaken(player) || !canInteract(player))
			return InteractionResult.FAIL;
		
		// TODO: that really needs to get dehardcoded
		world.playLocalSound(pos, SoundEvents.ENCHANTMENT_TABLE_USE, SoundSource.BLOCKS, 1F, 1F, true);
		player.getInventory().placeItemBackInInventory(SpectrumItems.AETHER_GRACED_NECTAR_GLOVES.get().getDefaultInstance());
		bowl.markTaken(player);
		
		return InteractionResult.CONSUME;
	}
	
	public static boolean canInteract(Player player) {
		// TODO: that really needs to get dehardcoded
		return player.hasEffect(SpectrumMobEffects.FATAL_SLUMBER);
	}

	@Override
	public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new PlayerTrackerBlockEntity(pos, state);
	}
}
