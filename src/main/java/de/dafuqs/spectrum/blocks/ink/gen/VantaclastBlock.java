package de.dafuqs.spectrum.blocks.ink.gen;

import com.mojang.serialization.*;
import de.dafuqs.spectrum.blocks.ink.*;
import de.dafuqs.spectrum.particle.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.*;
import net.minecraft.core.*;
import net.minecraft.network.chat.*;
import net.minecraft.sounds.*;
import net.minecraft.util.*;
import net.minecraft.world.item.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.*;
import net.minecraft.world.level.block.state.*;
import net.minecraft.world.phys.shapes.*;
import org.jspecify.annotations.Nullable;

import javax.annotation.*;
import java.util.*;

public class VantaclastBlock extends BaseInkBlock {
	
	public static final MapCodec<VantaclastBlock> CODEC = simpleCodec(VantaclastBlock::new);
	protected static final VoxelShape SHAPE = Block.box(1.0D, 0.0D, 1.0D, 15.0D, 10.0D, 15.0D);
	
	public VantaclastBlock(Properties settings) {
		super(settings);
	}
	
	@Override
	public MapCodec<? extends VantaclastBlock> codec() {
		return CODEC;
	}
	
	@Override
	public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
		return SHAPE;
	}
	
	@Override
	public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltip, TooltipFlag type) {
		super.appendHoverText(stack, context, tooltip, type);
		tooltip.add(Component.translatable("block.spectrum.vantaclast.tooltip").withStyle(ChatFormatting.GRAY));
		tooltip.add(Component.translatable("block.spectrum.vantaclast.tooltip2").withStyle(ChatFormatting.GRAY));
	}
	
	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new VantaclastBlockEntity(pos, state);
	}
	
	@Override
	public <T extends BlockEntity> @Nullable BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType) {
		return createInkBlockTicker(level, blockEntityType, SpectrumBlockEntities.VANTACLAST.get());
	}
	
	@Override
	public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
		super.animateTick(state, level, pos, random);
		
		float r = random.nextFloat();
		if (r <  0.01) {
			level.addParticle(SpectrumParticleTypes.VOID_FOG,
					pos.getX() + 0.3125 + random.nextDouble() * 0.375,
					pos.getY() + 0.5,
					pos.getZ() + 0.3125 + random.nextDouble() * 0.375,
					0, random.nextDouble() * 0.1, 0);
			
			if(r < 0.025) {
				level.playLocalSound(pos.getX(), pos.getY(), pos.getZ(), SpectrumSoundEvents.MIDNIGHT_SOLUTION_AMBIENT, SoundSource.BLOCKS, 0.2F + random.nextFloat() * 0.2F, 0.9F + random.nextFloat() * 0.15F, false);
			}
		}
	}
	
}
