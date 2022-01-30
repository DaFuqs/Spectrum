package de.dafuqs.spectrum.blocks.decay;

import de.dafuqs.spectrum.Support;
import de.dafuqs.spectrum.registries.SpectrumBlockTags;
import de.dafuqs.spectrum.registries.SpectrumDamageSources;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.tag.Tag;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public abstract class DecayBlock extends Block {

	/**
	 * Since Tag is not comparable we can not use a SortedMap for decayConversions
	 * here and therefore have to use an additional list for check order
	 */
	protected final List<Tag<Block>> decayConversionsList = new ArrayList<>();
	/**
	 * Decay can only convert those blocks to more decay
	 */
	protected final HashMap<Tag<Block>, BlockState> decayConversions = new HashMap<>();

	protected final Tag<Block> whiteListBlockTag;
	/**
	 * Decay is blocked by those blocks and can't jump over to them
	 */
	protected final Tag<Block> blackListBlockTag;
	protected final float damageOnTouching;
	protected final int tier;

	public DecayBlock(Settings settings, Tag<Block> whiteListBlockTag, Tag<Block> blackListBlockTag, int tier, float damageOnTouching) {
		super(settings);
		this.whiteListBlockTag = whiteListBlockTag;
		this.blackListBlockTag = blackListBlockTag;
		this.damageOnTouching = damageOnTouching;
		this.tier = tier;
	}

	/**
	 * If the decay jumps to sourceBlockState it will not place decay there, but destinationBlockState instead
	 * If a source block is not in one of those tags it will just be replaced with default decay
	 * @param sourceBlockTag The block tag checked for a conversion through decay
	 * @param conversionState The block state the source block is converted to
	 */
	public void addDecayConversion(Tag<Block> sourceBlockTag, BlockState conversionState) {
		this.decayConversionsList.add(sourceBlockTag);
		this.decayConversions.put(sourceBlockTag, conversionState);
	}

	public void onSteppedOn(World world, BlockPos pos, BlockState state, Entity entity) {
		if (entity instanceof LivingEntity && !entity.isFireImmune() && !EnchantmentHelper.hasFrostWalker((LivingEntity)entity)) {
			entity.damage(SpectrumDamageSources.DECAY, damageOnTouching);
		}
		super.onSteppedOn(world, pos, state, entity);
	}

	// jump to neighboring blocks
	public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
		if(canSpread(state)) {
			float spreadChance = getSpreadChance();
			if (spreadChance < 1.0F) {
				if (random.nextFloat() > spreadChance) {
					return;
				}
			}
			
			Direction randomDirection = Direction.random(random);
			BlockPos targetBlockPos = pos.offset(randomDirection, 1);
			BlockState currentBlockState = world.getBlockState(targetBlockPos);
			BlockEntity blockEntity = world.getBlockEntity(targetBlockPos);
			
			if (blockEntity == null && !Support.hasBlockTag(currentBlockState, SpectrumBlockTags.DECAY)  // decay doesn't jump to other decay. Maybe: if tier is smaller it should still be converted?
					&& (whiteListBlockTag == null || Support.hasBlockTag(currentBlockState, whiteListBlockTag))
					&& (blackListBlockTag == null || !Support.hasBlockTag(currentBlockState, blackListBlockTag))
					// bedrock is ok, but not other modded unbreakable blocks
					&& (currentBlockState.getBlock() == Blocks.BEDROCK || (currentBlockState.getBlock().getHardness() > -1.0F && currentBlockState.getBlock().getBlastResistance() < 3600000.0F))) {
				
				BlockState destinationBlockState = getSpreadState(state);
				for (Tag<Block> currentCheckTag : this.decayConversionsList) {
					BlockState targetState = decayConversions.get(currentCheckTag);
					if (Support.hasBlockTag(currentBlockState, currentCheckTag)) {
						destinationBlockState = targetState;
						break;
					}
				}
				
				world.setBlockState(targetBlockPos, destinationBlockState);
			}
		}
	}

	protected abstract float getSpreadChance();
	
	protected abstract boolean canSpread(BlockState blockState);
	
	protected abstract BlockState getSpreadState(BlockState previousState);

}
