package de.dafuqs.spectrum.blocks.deeper_down;

import com.mojang.serialization.*;
import de.dafuqs.revelationary.api.revelations.*;
import de.dafuqs.spectrum.api.block.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.core.*;
import net.minecraft.resources.*;
import net.minecraft.sounds.*;
import net.minecraft.util.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.item.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.*;
import net.minecraft.world.level.block.state.properties.*;
import javax.annotation.*;

import java.util.*;
import java.util.function.*;

public class DragonboneBlock extends RotatedPillarBlock implements RevelationAware, MoonstoneStrikeableBlock {
	
	public static final MapCodec<DragonboneBlock> CODEC = simpleCodec(DragonboneBlock::new);
	
	public DragonboneBlock(Properties settings) {
		super(settings);
		RevelationAware.register(this);
	}
	
	@Override
	public MapCodec<? extends DragonboneBlock> codec() {
		return CODEC;
	}
	
	@Override
	public void onMoonstoneStrike(Level world, BlockPos pos, @Nullable LivingEntity striker) {
		crack(world, pos);
	}
	
	public void crack(Level world, BlockPos pos) {
		BlockState state = world.getBlockState(pos);
		if (state.getBlock() instanceof DragonboneBlock) {
			world.setBlockAndUpdate(pos, SpectrumBlocks.CRACKED_DRAGONBONE.get().defaultBlockState().setValue(RotatedPillarBlock.AXIS, state.getValue(RotatedPillarBlock.AXIS)));
			if (world.isClientSide()) {
				world.playSound(null, pos, SoundEvents.TURTLE_EGG_CRACK, SoundSource.BLOCKS, 1.0F, Mth.randomBetween(world.getRandom(), 0.8F, 1.2F));
			}
		}
	}
	
	@Override
	public boolean dropFromExplosion(Explosion explosion) {
		return false;
	}
	
	@Override
	protected void onExplosionHit(BlockState state, Level world, BlockPos pos, Explosion explosion, BiConsumer<ItemStack, BlockPos> stackMerger) {
		if (state.getBlock() instanceof RotatedPillarBlock) {
			world.setBlockAndUpdate(pos, SpectrumBlocks.CRACKED_DRAGONBONE.get().defaultBlockState().setValue(RotatedPillarBlock.AXIS, state.getValue(RotatedPillarBlock.AXIS)));
		}
	}
	
	@Override
	public ResourceLocation getCloakAdvancementIdentifier() {
		return SpectrumAdvancements.REVEAL_DRAGONBONE;
	}
	
	@Override
	public Map<BlockState, BlockState> getBlockStateCloaks() {
		Map<BlockState, BlockState> map = new Hashtable<>();
		for (Direction.Axis axis : BlockStateProperties.AXIS.getPossibleValues()) {
			map.put(this.defaultBlockState().setValue(BlockStateProperties.AXIS, axis), Blocks.BONE_BLOCK.defaultBlockState().setValue(BlockStateProperties.AXIS, axis));
		}
		return map;
	}
	
	@Override
	public @Nullable Tuple<Item, Item> getItemCloak() {
		return new Tuple<>(this.asItem(), Blocks.BONE_BLOCK.asItem());
	}
	
}
