package de.dafuqs.spectrum.blocks.decay;

import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.compat.claims.*;
import de.dafuqs.spectrum.registries.*;
import net.fabricmc.api.*;
import net.minecraft.block.*;
import net.minecraft.block.piston.*;
import net.minecraft.enchantment.*;
import net.minecraft.entity.*;
import net.minecraft.particle.*;
import net.minecraft.server.world.*;
import net.minecraft.state.*;
import net.minecraft.state.property.*;
import net.minecraft.registry.tag.*;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.*;
import net.minecraft.world.tick.TickPriority;
import org.jetbrains.annotations.*;

import java.util.*;

public abstract class DecayBlock extends Block {
	
	public enum Conversion implements StringIdentifiable {
		NONE("none"),
		DEFAULT("default"),
		SPECIAL("special");
		
		private final String name;
		
		Conversion(String name) {
			this.name = name;
		}
		
		@Override
		public String toString() {
			return this.name;
		}
		
		@Override
		public String asString() {
			return this.name;
		}
	}
	
	public static final EnumProperty<Conversion> CONVERSION = EnumProperty.of("conversion", Conversion.class);
	
	private static final boolean CREATE_DD_PORTALS = true;
	
	/**
	 * Since Tag is not comparable we can not use a SortedMap for decayConversions
	 * here and therefore have to use an additional list for check order
	 */
	protected final Map<TagKey<Block>, BlockState> decayConversions = new LinkedHashMap<>();
	
	/**
	 * Decay can only convert those blocks to more decay
	 */
	protected final TagKey<Block> whiteListBlockTag;
	/**
	 * Decay is blocked by those blocks and can't jump over to them
	 */
	protected final TagKey<Block> blackListBlockTag;
	protected final float damageOnTouching;
	protected final int tier;
	
	public DecayBlock(Settings settings, TagKey<Block> whiteListBlockTag, TagKey<Block> blackListBlockTag, int tier, float damageOnTouching) {
		super(settings);
		this.whiteListBlockTag = whiteListBlockTag;
		this.blackListBlockTag = blackListBlockTag;
		this.damageOnTouching = damageOnTouching;
		this.tier = tier;
	}
	
	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> stateManager) {
		stateManager.add(CONVERSION);
	}
	
	/**
	 * If the decay jumps to sourceBlockState it will not place decay there, but destinationBlockState instead
	 * If a source block is not in one of those tags it will just be replaced with default decay
	 *
	 * @param sourceBlockTag  The block tag checked for a conversion through decay
	 * @param conversionState The block state the source block is converted to
	 */
	public void addDecayConversion(TagKey<Block> sourceBlockTag, BlockState conversionState) {
		this.decayConversions.put(sourceBlockTag, conversionState);
	}
	
	@Override
	public void onSteppedOn(World world, BlockPos pos, BlockState state, Entity entity) {
		if (entity instanceof LivingEntity && !entity.isFireImmune() && !EnchantmentHelper.hasFrostWalker((LivingEntity) entity)) {
			entity.damage(SpectrumDamageSources.decay(world), damageOnTouching);
		}
		super.onSteppedOn(world, pos, state, entity);
	}
	
	@Override
	@Environment(EnvType.CLIENT)
	public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
		if (state.get(CONVERSION).equals(Conversion.SPECIAL)) {
			world.addParticle(new BlockStateParticleEffect(ParticleTypes.BLOCK, state), pos.getX() + random.nextFloat(), pos.getY() + 1, pos.getZ() + random.nextFloat(), 0.0D, 0.0D, 0.0D);
		}
	}
	
	// jump to neighboring blocks
	@Override
	public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
		if (canSpread(state)) {
			float spreadChance = getSpreadChance();
			if (spreadChance < 1.0F) {
				if (random.nextFloat() > spreadChance) {
					return;
				}
			}
			
			Direction randomDirection = Direction.random(random);
			tryConvert(world, state, pos, randomDirection);
		}
	}
	
	protected boolean tryConvert(@NotNull World world, BlockState state, @NotNull BlockPos originPos, Direction direction) {
		BlockPos targetBlockPos = originPos.offset(direction);
		
		BlockState targetBlockState = world.getBlockState(targetBlockPos);
		if (canSpreadTo(world, targetBlockPos, targetBlockState)) {
			world.setBlockState(targetBlockPos, getConversionFor(state, targetBlockState));
			return true;
		}
		return false;
	}
	
	public boolean canSpreadTo(World world, BlockPos targetBlockPos, BlockState stateAtTargetPos) {
		if (SpectrumCommon.CONFIG.DecayIsStoppedByClaimMods && GenericClaimModsCompat.isProtected(world, targetBlockPos, null)) {
			return false;
		}
		
		return (canSpreadToBlockEntities() || world.getBlockEntity(targetBlockPos) == null)
				&& (!(stateAtTargetPos.getBlock() instanceof DecayBlock decayBlock) || this.tier > decayBlock.tier) // decay can convert decay of a lower tier
				&& (whiteListBlockTag == null || stateAtTargetPos.isIn(whiteListBlockTag))
				&& (blackListBlockTag == null || !stateAtTargetPos.isIn(blackListBlockTag))
				&& (stateAtTargetPos.getBlock() == Blocks.BEDROCK || (stateAtTargetPos.getBlock().getHardness() > -1.0F && stateAtTargetPos.getBlock().getBlastResistance() < 10000.0F));
	}
	
	public BlockState getConversionFor(BlockState stateToSpread, BlockState stateToSpreadTo) {
		for (Map.Entry<TagKey<Block>, BlockState> conversion : this.decayConversions.entrySet()) {
			if (stateToSpreadTo.isIn(conversion.getKey())) {
				return conversion.getValue();
			}
		}
		return getSpreadState(stateToSpread);
	}
	
	@Override
	public PistonBehavior getPistonBehavior(BlockState state) {
		return PistonBehavior.BLOCK;
	}
	
	
	/**
	 * If a neighboring block is updated (placed by a player?), and that can be converted
	 * schedule a tick to convert it faster. => User gets quick reaction
	 */
	@Override
	public void neighborUpdate(BlockState state, World world, BlockPos pos, Block previousBlock, BlockPos fromPos, boolean notify) {
		super.neighborUpdate(state, world, pos, previousBlock, fromPos, notify);
		
		if (previousBlock == Blocks.AIR) {
			Block newBlock = world.getBlockState(fromPos).getBlock();
			
			if (canSpread(state) && !(newBlock instanceof DecayBlock) && !(newBlock instanceof DecayAwayBlock)) {
				for (Map.Entry<TagKey<Block>, BlockState> conversion : this.decayConversions.entrySet()) {
					if (state.isIn(conversion.getKey())) {
						world.scheduleBlockTick(pos, this, 40 + world.random.nextInt(200), TickPriority.EXTREMELY_LOW);
						break;
					}
				}
			}
		}
	}
	
	@Override
	public boolean hasRandomTicks(BlockState state) {
		return this.canSpread(state);
	}
	
	@Override
	public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
		this.randomTick(state, world, pos, random);
		
		spreadToNeighboringBlock(state, world, pos);
	}
	
	private void spreadToNeighboringBlock(BlockState state, ServerWorld world, BlockPos pos) {
		if (canSpread(state)) {
			List<Direction> directions = new ArrayList<>(List.of(Direction.values()));
			Collections.shuffle(directions);
			
			for (Direction direction : directions) {
				boolean converted = tryConvert(world, state, pos, direction);
				if (converted) {
					break;
				}
			}
		}
	}
	
	protected abstract float getSpreadChance();
	protected abstract boolean canSpread(BlockState blockState);
	protected abstract boolean canSpreadToBlockEntities();
	protected abstract BlockState getSpreadState(BlockState previousState);
	
}
