package de.dafuqs.spectrum.blocks.decay;

import com.mojang.serialization.*;
import de.dafuqs.spectrum.config.*;
import de.dafuqs.spectrum.particle.effect.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.core.*;
import net.minecraft.core.particles.*;
import net.minecraft.sounds.*;
import net.minecraft.util.*;
import net.minecraft.util.valueproviders.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.item.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.*;
import net.minecraft.world.level.block.state.properties.*;
import org.jspecify.annotations.*;

public class FailingBlock extends DecayBlock {
	
	public static final MapCodec<FailingBlock> CODEC = simpleCodec(FailingBlock::new);
	
	public static final IntegerProperty AGE = BlockStateProperties.AGE_15; // failing may spread 15 blocks max. It consuming obsidian resets that value
	
	public FailingBlock(Properties settings) {
		super(settings, SpectrumConfig.CONFIG.FailingDecayTickRate.get(), SpectrumConfig.CONFIG.FailingCanDestroyBlockEntities.get(), 2, 2.5F, UniformInt.of(1, 2));
		registerDefaultState(getStateDefinition().any().setValue(CONVERSION, Conversion.NONE).setValue(AGE, 0));
	}
	
	@Override
	public MapCodec<? extends FailingBlock> codec() {
		return CODEC;
	}
	
	@Override
	public void setPlacedBy(Level world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
		super.setPlacedBy(world, pos, state, placer, itemStack);
		
		if (!world.isClientSide()) {
			world.playSound(null, pos, SpectrumSoundEvents.FAILING_PLACED, SoundSource.BLOCKS, 0.5F, 1.0F);
		} else {
			RandomSource random = world.getRandom();
			world.addParticle(ParticleTypes.EXPLOSION, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, ((-1.0F + random.nextFloat() * 2.0F) / 12.0F), 0.05, ((-1.0F + random.nextFloat() * 2.0F) / 12.0F));
			
			for (int i = 0; i < 20; i++) {
				world.addParticle(ColoredCraftingParticleEffect.GRAY, pos.getX() - 0.2 + random.nextFloat() * 1.4, pos.getY() + random.nextFloat(), pos.getZ() - 0.2 + random.nextFloat() * 1.4, ((-1.0F + random.nextFloat() * 2.0F) / 12.0F), 0.05, ((-1.0F + random.nextFloat() * 2.0F) / 12.0F));
			}
		}
	}
	
	@Override
	public boolean isRandomlyTicking(BlockState state) {
		return state.getValue(AGE) < BlockStateProperties.MAX_AGE_15;
	}
	
	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> stateManager) {
		super.createBlockStateDefinition(stateManager);
		stateManager.add(AGE);
	}
	
	@Override
	protected @Nullable BlockState getSpreadState(BlockState stateToSpreadFrom, BlockState stateToSpreadTo, Level world, BlockPos stateToSpreadToPos) {
		if (stateToSpreadFrom.getValue(AGE) >= BlockStateProperties.MAX_AGE_15) {
			return null;
		}
		if (stateToSpreadTo.getCollisionShape(world, stateToSpreadToPos).isEmpty() || stateToSpreadTo.is(SpectrumBlockTags.FAILING_SAFE)) {
			return null;
		}
		
		int age = stateToSpreadFrom.getValue(AGE);
		
		if (stateToSpreadTo.is(SpectrumBlockTags.FAILING_SPECIAL_CONVERSIONS)) {
			return this.defaultBlockState().setValue(CONVERSION, Conversion.SPECIAL).setValue(AGE, Math.max(0, age - 5));
		} else if (stateToSpreadTo.is(SpectrumBlockTags.FAILING_CONVERSIONS)) {
			return this.defaultBlockState().setValue(CONVERSION, Conversion.DEFAULT).setValue(AGE, Math.max(0, age - 2));
		}
		return stateToSpreadFrom.setValue(CONVERSION, Conversion.NONE).setValue(AGE, age + 1);
	}
	
}
