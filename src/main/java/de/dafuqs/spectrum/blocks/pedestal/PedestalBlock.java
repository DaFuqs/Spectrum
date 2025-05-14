package de.dafuqs.spectrum.blocks.pedestal;

import com.mojang.serialization.*;
import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.api.block.*;
import de.dafuqs.spectrum.api.recipe.*;
import de.dafuqs.spectrum.blocks.*;
import de.dafuqs.spectrum.compat.modonomicon.*;
import de.dafuqs.spectrum.networking.s2c_payloads.*;
import de.dafuqs.spectrum.particle.effect.*;
import de.dafuqs.spectrum.recipe.pedestal.*;
import de.dafuqs.spectrum.registries.*;
import net.fabricmc.api.*;
import net.minecraft.client.*;
import net.minecraft.core.*;
import net.minecraft.core.particles.*;
import net.minecraft.resources.*;
import net.minecraft.server.level.*;
import net.minecraft.util.*;
import net.minecraft.world.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.*;
import net.minecraft.world.item.context.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.*;
import net.minecraft.world.level.block.state.*;
import net.minecraft.world.level.block.state.properties.*;
import net.minecraft.world.phys.*;
import net.minecraft.world.phys.shapes.*;
import org.jetbrains.annotations.*;
import org.joml.*;

import java.util.*;

public class PedestalBlock extends BaseEntityBlock implements RedstonePoweredBlock, PaintbrushTriggered {
	
	public static final ResourceLocation UNLOCK_IDENTIFIER = SpectrumCommon.locate("place_pedestal");
	private static final VoxelShape SHAPE;
	private final PedestalVariant variant;
	
	public PedestalBlock(Properties settings, PedestalVariant variant) {
		super(settings);
		this.variant = variant;
		registerDefaultState(getStateDefinition().any().setValue(BlockStateProperties.POWERED, false));
	}
	
	@Override
	public MapCodec<? extends PedestalBlock> codec() {
		//TODO: Make the codec
		return null;
	}
	
	/**
	 * Sets pedestal to a new tier
	 * while keeping the inventory and all other data
	 */
	public static void upgradeToVariant(@NotNull Level world, BlockPos blockPos, PedestalVariant newPedestalVariant) {
		world.setBlockAndUpdate(blockPos, newPedestalVariant.getPedestalBlock().getStateForPlacement(new DirectionalPlaceContext(world, blockPos, Direction.DOWN, null, Direction.UP)));
	}
	
	public static void clearCurrentlyRenderedMultiBlock(Level world) {
		if (world.isClientSide) {
			ModonomiconHelper.clearRenderedMultiblock(SpectrumMultiblocks.get(SpectrumMultiblocks.PEDESTAL_SIMPLE));
			ModonomiconHelper.clearRenderedMultiblock(SpectrumMultiblocks.get(SpectrumMultiblocks.PEDESTAL_ADVANCED));
			ModonomiconHelper.clearRenderedMultiblock(SpectrumMultiblocks.get(SpectrumMultiblocks.PEDESTAL_COMPLEX));
			ModonomiconHelper.clearRenderedMultiblock(SpectrumMultiblocks.get(SpectrumMultiblocks.PEDESTAL_COMPLEX_WITHOUT_MOONSTONE));
		}
	}
	
	/**
	 * Called when a pedestal is upgraded to a new tier
	 * (like amethyst to the cmy variant). Spawns lots of matching particles.
	 *
	 * @param newPedestalRecipeTier The tier the pedestal has been upgraded to
	 */
	@Environment(EnvType.CLIENT)
	public static void spawnUpgradeParticleEffectsForTier(BlockPos blockPos, @NotNull PedestalRecipeTier newPedestalRecipeTier) {
		Minecraft client = Minecraft.getInstance();
		Level world = client.level;
		if (world == null) return;
		RandomSource random = world.getRandom();
		
		switch (newPedestalRecipeTier) {
			case COMPLEX -> {
				ParticleOptions particleEffect = ColoredCraftingParticleEffect.WHITE;
				for (int i = 0; i < 25; i++) {
					float randomZ = random.nextFloat() * 1.2F;
					world.addParticle(particleEffect, blockPos.getX() + 1.1, blockPos.getY(), blockPos.getZ() + randomZ, 0.0D, 0.03D, 0.0D);
				}
				for (int i = 0; i < 25; i++) {
					float randomZ = random.nextFloat() * 1.2F;
					world.addParticle(particleEffect, blockPos.getX() - 0.1, blockPos.getY(), blockPos.getZ() + randomZ, 0.0D, 0.03D, 0.0D);
				}
				for (int i = 0; i < 25; i++) {
					float randomX = random.nextFloat() * 1.2F;
					world.addParticle(particleEffect, blockPos.getX() + randomX, blockPos.getY(), blockPos.getZ() + 1.1, 0.0D, 0.03D, 0.0D);
				}
				for (int i = 0; i < 25; i++) {
					float randomX = random.nextFloat() * 1.2F;
					world.addParticle(particleEffect, blockPos.getX() + randomX, blockPos.getY(), blockPos.getZ() - 0.1, 0.0D, 0.03D, 0.0D);
				}
			}
			case ADVANCED -> {
				ParticleOptions particleEffect = ColoredCraftingParticleEffect.BLACK;
				for (int i = 0; i < 25; i++) {
					float randomZ = random.nextFloat() * 1.2F;
					world.addParticle(particleEffect, blockPos.getX() + 1.1, blockPos.getY(), blockPos.getZ() + randomZ, 0.0D, 0.03D, 0.0D);
				}
				for (int i = 0; i < 25; i++) {
					float randomZ = random.nextFloat() * 1.2F;
					world.addParticle(particleEffect, blockPos.getX() - 0.1, blockPos.getY(), blockPos.getZ() + randomZ, 0.0D, 0.03D, 0.0D);
				}
				for (int i = 0; i < 25; i++) {
					float randomX = random.nextFloat() * 1.2F;
					world.addParticle(particleEffect, blockPos.getX() + randomX, blockPos.getY(), blockPos.getZ() + 1.1, 0.0D, 0.03D, 0.0D);
				}
				for (int i = 0; i < 25; i++) {
					float randomX = random.nextFloat() * 1.2F;
					world.addParticle(particleEffect, blockPos.getX() + randomX, blockPos.getY(), blockPos.getZ() - 0.1, 0.0D, 0.03D, 0.0D);
				}
			}
			case SIMPLE -> {
				ParticleOptions particleEffectC = ColoredCraftingParticleEffect.CYAN;
				ParticleOptions particleEffectM = ColoredCraftingParticleEffect.MAGENTA;
				ParticleOptions particleEffectY = ColoredCraftingParticleEffect.YELLOW;
				for (int i = 0; i < 25; i++) {
					float randomZ = random.nextFloat() * 1.2F;
					world.addParticle(particleEffectY, blockPos.getX() + 1.1, blockPos.getY() + 0.1, blockPos.getZ() + randomZ, 0.0D, 0.05D, 0.0D);
				}
				for (int i = 0; i < 25; i++) {
					float randomZ = random.nextFloat() * 1.2F;
					world.addParticle(particleEffectC, blockPos.getX() - 0.1, blockPos.getY() + 0.1, blockPos.getZ() + randomZ, 0.0D, 0.05D, 0.0D);
				}
				for (int i = 0; i < 25; i++) {
					float randomX = random.nextFloat() * 1.2F;
					world.addParticle(particleEffectM, blockPos.getX() + randomX, blockPos.getY() + 0.1, blockPos.getZ() + 1.1, 0.0D, 0.05D, 0.0D);
				}
				for (int i = 0; i < 25; i++) {
					float randomX = random.nextFloat() * 1.2F;
					world.addParticle(particleEffectM, blockPos.getX() + randomX, blockPos.getY() + 0.1, blockPos.getZ() - 0.1, 0.0D, 0.05D, 0.0D);
				}
			}
			case BASIC -> {
			}
		}
	}
	
	@Override
	public void setPlacedBy(Level world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
		if (placer instanceof ServerPlayer) {
			BlockEntity blockEntity = world.getBlockEntity(pos);
			if (blockEntity instanceof PedestalBlockEntity pedestalBlockEntity) {
				pedestalBlockEntity.setOwner((ServerPlayer) placer);
				blockEntity.setChanged();
			}
		}
	}
	
	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> stateManager) {
		stateManager.add(BlockStateProperties.POWERED);
	}
	
	@Override
	public ItemInteractionResult useItemOn(ItemStack handStack, BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
		ItemInteractionResult actionResult = checkAndDoPaintbrushTrigger(state, world, pos, player, hand, hit);
		if (actionResult.consumesAction()) {
			return actionResult;
		}
		
		if (world.isClientSide) {
			return ItemInteractionResult.SUCCESS;
		} else {
			this.openScreen(world, pos, player);
			return ItemInteractionResult.CONSUME;
		}
	}
	
	protected void openScreen(Level world, BlockPos pos, Player player) {
		Optional<PedestalBlockEntity> blockEntity = world.getBlockEntity(pos, SpectrumBlockEntities.PEDESTAL);
		if (blockEntity.isPresent()) {
			PedestalBlockEntity pedestalBlockEntity = blockEntity.get();
			pedestalBlockEntity.setOwner(player);
			player.openMenu(pedestalBlockEntity);
		}
	}
	
	@Override
	public void onRemove(BlockState state, Level world, BlockPos pos, BlockState newState, boolean moved) {
		if (newState.getBlock() instanceof PedestalBlock newStateBlock) {
			if (!state.is(newStateBlock)) {
				// pedestal is getting upgraded. Keep the blockEntity with its contents
				BlockEntity blockEntity = world.getBlockEntity(pos);
				if (blockEntity instanceof PedestalBlockEntity pedestalBlockEntity) {
					if (state.getBlock().equals(newStateBlock)) {
						PedestalVariant newVariant = newStateBlock.getVariant();
						pedestalBlockEntity.setVariant(newVariant);
					}
				}
			}
		} else {
			InWorldInteractionBlock.scatterContents(world, pos);
			super.onRemove(state, world, pos, newState, moved);
		}
	}
	
	@Override
	@Nullable
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new PedestalBlockEntity(pos, state);
	}
	
	@Override
	public RenderShape getRenderShape(BlockState state) {
		return RenderShape.MODEL;
	}
	
	@Override
	public boolean hasAnalogOutputSignal(BlockState state) {
		return true;
	}
	
	@Override
	public int getAnalogOutputSignal(BlockState state, @NotNull Level world, BlockPos pos) {
		return AbstractContainerMenu.getRedstoneSignalFromBlockEntity(world.getBlockEntity(pos));
	}
	
	@Override
	public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
		return SHAPE;
	}
	
	@Override
	@Nullable
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(@NotNull Level world, BlockState state, BlockEntityType<T> type) {
		return createTickerHelper(type, SpectrumBlockEntities.PEDESTAL, world.isClientSide ? PedestalBlockEntity::clientTick : PedestalBlockEntity::serverTick);
	}
	
	@Override
	public void neighborChanged(BlockState state, @NotNull Level world, BlockPos pos, Block block, BlockPos fromPos, boolean notify) {
		if (!world.isClientSide) {
			if (this.checkGettingPowered(world, pos)) {
				this.power(world, pos);
			} else {
				this.unPower(world, pos);
			}
		}
	}
	
	@Override
	@Environment(EnvType.CLIENT)
	public void animateTick(@NotNull BlockState state, Level world, BlockPos pos, RandomSource random) {
		if (state.getValue(BlockStateProperties.POWERED)) {
			Vector3f color = new Vector3f(0.5F, 0.5F, 0.5F);
			float xOffset = random.nextFloat();
			float zOffset = random.nextFloat();
			world.addParticle(new DustParticleOptions(color, 1.0F), pos.getX() + xOffset, pos.getY() + 1, pos.getZ() + zOffset, 0.0D, 0.0D, 0.0D);
		}
	}
	
	@Override
	public void destroy(LevelAccessor world, BlockPos pos, BlockState state) {
		if (world.isClientSide()) {
			clearCurrentlyRenderedMultiBlock((Level) world);
		}
	}
	
	@Override
	public BlockState getStateForPlacement(@NotNull BlockPlaceContext ctx) {
		BlockState placementState = this.defaultBlockState();
		
		if (ctx.getLevel().getBestNeighborSignal(ctx.getClickedPos()) > 0) {
			placementState = placementState.setValue(BlockStateProperties.POWERED, true);
		}
		
		return placementState;
	}
	
	public PedestalVariant getVariant() {
		return this.variant;
	}
	
	static {
		var foot = Block.box(3, 0, 3, 13, 3, 13);
		var neck = Block.box(5, 3, 5, 11, 12, 11);
		var head = Block.box(0, 12, 0, 16, 16, 16);
		foot = Shapes.or(foot, neck);
		SHAPE = Shapes.or(foot, head);
	}
	
	@Override
	public ItemInteractionResult onPaintBrushTrigger(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
		BlockEntity blockEntity = world.getBlockEntity(pos);
		if (blockEntity instanceof PedestalBlockEntity pedestalBlockEntity) {
			if (pedestalBlockEntity.propertyDelegate.craftingTime > 0) {
				return ItemInteractionResult.FAIL;
			}
			if (pedestalBlockEntity.currentRecipe == null) {
				return ItemInteractionResult.FAIL;
			}
			if (pedestalBlockEntity.currentRecipe.value() instanceof GatedRecipe<?> gatedRecipe && !gatedRecipe.canPlayerCraft(player)) {
				return ItemInteractionResult.FAIL;
			}
			
			if (!world.isClientSide) {
				pedestalBlockEntity.shouldCraft = true;
				PlayPedestalStartCraftingParticlePayload.spawnPedestalStartCraftingParticles(pedestalBlockEntity);
			}
			
			return ItemInteractionResult.sidedSuccess(world.isClientSide);
		}
		return ItemInteractionResult.FAIL;
	}
	
}
