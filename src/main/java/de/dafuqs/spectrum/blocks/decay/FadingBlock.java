package de.dafuqs.spectrum.blocks.decay;

import de.dafuqs.spectrum.SpectrumCommon;
import de.dafuqs.spectrum.particle.SpectrumParticleTypes;
import de.dafuqs.spectrum.registries.SpectrumBlockTags;
import de.dafuqs.spectrum.registries.SpectrumSoundEvents;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.BlockStateParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundCategory;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.tag.TagKey;
import net.minecraft.util.StringIdentifiable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.Random;

public class FadingBlock extends DecayBlock {
	
	public static final EnumProperty<DecayConversion> DECAY_STATE = EnumProperty.of("decay_state", DecayConversion.class);
	
	public FadingBlock(Settings settings, TagKey<Block> whiteListBlockTag, TagKey<Block> blackListBlockTag, int tier, float damageOnTouching) {
		super(settings, whiteListBlockTag, blackListBlockTag, tier, damageOnTouching);
		setDefaultState(getStateManager().getDefaultState().with(DECAY_STATE, DecayConversion.DEFAULT));
		
		BlockState destinationBlockState2 = this.getDefaultState().with(DECAY_STATE, DecayConversion.MAGIC_LEAVES);
		addDecayConversion(SpectrumBlockTags.FADING_SPECIAL_CONVERSIONS, destinationBlockState2);
		
		BlockState destinationBlockState = this.getDefaultState().with(DECAY_STATE, DecayConversion.LEAVES);
		addDecayConversion(SpectrumBlockTags.FADING_CONVERSIONS, destinationBlockState);
	}
	
	public void onPlaced(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
		super.onPlaced(world, pos, state, placer, itemStack);
		
		if (!world.isClient) {
			world.playSound(null, pos, SpectrumSoundEvents.FADING_PLACED, SoundCategory.BLOCKS, 0.5F, 1.0F);
		} else {
			Random random = world.getRandom();
			world.addParticle(ParticleTypes.POOF, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, ((-1.0F + random.nextFloat() * 2.0F) / 12.0F), 0.05, ((-1.0F + random.nextFloat() * 2.0F) / 12.0F));
			
			for (int i = 0; i < 10; i++) {
				world.addParticle(SpectrumParticleTypes.DECAY_PLACE, pos.getX() + random.nextFloat(), pos.getY() + 1, pos.getZ() + random.nextFloat(), ((-1.0F + random.nextFloat() * 2.0F) / 12.0F), 0.05, ((-1.0F + random.nextFloat() * 2.0F) / 12.0F));
			}
		}
	}
	
	@Override
	protected float getSpreadChance() {
		return SpectrumCommon.CONFIG.FadingDecayTickRate;
	}
	
	@Override
	protected boolean canSpread(BlockState blockState) {
		return true;
	}
	
	@Override
	protected BlockState getSpreadState(BlockState previousState) {
		return this.getDefaultState();
	}
	
	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> stateManager) {
		stateManager.add(DECAY_STATE);
	}
	
	@Environment(EnvType.CLIENT)
	public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
		if (state.get(FadingBlock.DECAY_STATE).equals(DecayConversion.MAGIC_LEAVES)) {
			float xOffset = random.nextFloat();
			float zOffset = random.nextFloat();
			world.addParticle(new BlockStateParticleEffect(ParticleTypes.BLOCK, state), pos.getX() + xOffset, pos.getY() + 1, pos.getZ() + zOffset, 0.0D, 0.0D, 0.0D);
		}
	}
	
	public enum DecayConversion implements StringIdentifiable {
		DEFAULT("default"),
		LEAVES("leaves"),
		MAGIC_LEAVES("magic_leaves");
		
		private final String name;
		
		DecayConversion(String name) {
			this.name = name;
		}
		
		public String toString() {
			return this.name;
		}
		
		public String asString() {
			return this.name;
		}
	}
	
}
