package de.dafuqs.spectrum.blocks.pedestal;

import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.blocks.*;
import de.dafuqs.spectrum.enums.*;
import de.dafuqs.spectrum.networking.*;
import de.dafuqs.spectrum.particle.*;
import de.dafuqs.spectrum.recipe.*;
import de.dafuqs.spectrum.registries.*;
import net.fabricmc.api.*;
import net.minecraft.block.*;
import net.minecraft.block.entity.*;
import net.minecraft.client.*;
import net.minecraft.entity.*;
import net.minecraft.entity.player.*;
import net.minecraft.item.*;
import net.minecraft.particle.*;
import net.minecraft.screen.*;
import net.minecraft.server.network.*;
import net.minecraft.state.*;
import net.minecraft.state.property.*;
import net.minecraft.util.*;
import net.minecraft.util.hit.*;
import net.minecraft.util.math.*;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.*;
import net.minecraft.world.*;
import org.jetbrains.annotations.*;
import org.joml.*;
import vazkii.patchouli.api.*;

public class PedestalBlock extends BlockWithEntity implements RedstonePoweredBlock, PaintbrushTriggered {
	
	public static final Identifier UNLOCK_IDENTIFIER = SpectrumCommon.locate("place_pedestal");
	public static final BooleanProperty POWERED = BooleanProperty.of("powered");
	private static final VoxelShape SHAPE;
	private final PedestalVariant variant;
	
	public PedestalBlock(Settings settings, PedestalVariant variant) {
		super(settings);
		this.variant = variant;
		setDefaultState(getStateManager().getDefaultState().with(POWERED, false));
	}
	
	/**
	 * Sets pedestal to a new tier
	 * while keeping the inventory and all other data
	 */
	public static void upgradeToVariant(@NotNull World world, BlockPos blockPos, PedestalVariant newPedestalVariant) {
		world.setBlockState(blockPos, newPedestalVariant.getPedestalBlock().getPlacementState(new AutomaticItemPlacementContext(world, blockPos, Direction.DOWN, null, Direction.UP)));
	}
	
	public static void clearCurrentlyRenderedMultiBlock(World world) {
		if (world.isClient) {
			IMultiblock currentlyRenderedMultiBlock = PatchouliAPI.get().getCurrentMultiblock();
			if (currentlyRenderedMultiBlock != null
					&& (currentlyRenderedMultiBlock.getID().equals(SpectrumMultiblocks.PEDESTAL_SIMPLE_STRUCTURE_IDENTIFIER_CHECK)
					|| currentlyRenderedMultiBlock.getID().equals(SpectrumMultiblocks.PEDESTAL_ADVANCED_STRUCTURE_IDENTIFIER_CHECK)
					|| currentlyRenderedMultiBlock.getID().equals(SpectrumMultiblocks.PEDESTAL_COMPLEX_STRUCTURE_WITHOUT_MOONSTONE_IDENTIFIER_CHECK)
					|| currentlyRenderedMultiBlock.getID().equals(SpectrumMultiblocks.PEDESTAL_COMPLEX_STRUCTURE_IDENTIFIER_CHECK))) {
				
				PatchouliAPI.get().clearMultiblock();
			}
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
		World world = MinecraftClient.getInstance().world;
		Random random = world.getRandom();
		
		switch (newPedestalRecipeTier) {
			case COMPLEX -> {
				ParticleEffect particleEffect = SpectrumParticleTypes.getCraftingParticle(DyeColor.WHITE);
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
				ParticleEffect particleEffect = SpectrumParticleTypes.getCraftingParticle(DyeColor.BLACK);
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
				ParticleEffect particleEffectC = SpectrumParticleTypes.getCraftingParticle(DyeColor.CYAN);
				ParticleEffect particleEffectM = SpectrumParticleTypes.getCraftingParticle(DyeColor.MAGENTA);
				ParticleEffect particleEffectY = SpectrumParticleTypes.getCraftingParticle(DyeColor.YELLOW);
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
		}
	}
	
	@Override
	public void onPlaced(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
		if (placer instanceof ServerPlayerEntity) {
			BlockEntity blockEntity = world.getBlockEntity(pos);
			if (blockEntity instanceof PedestalBlockEntity pedestalBlockEntity) {
				pedestalBlockEntity.setOwner((ServerPlayerEntity) placer);
				if (itemStack.hasCustomName()) {
					pedestalBlockEntity.setCustomName(itemStack.getName());
				}
				blockEntity.markDirty();
			}
		}
	}
	
	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> stateManager) {
		stateManager.add(POWERED);
	}
	
	@Override
	public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
		ActionResult actionResult = checkAndDoPaintbrushTrigger(state, world, pos, player, hand, hit);
		if (actionResult.isAccepted()) {
			return actionResult;
		}
		
		if (world.isClient) {
			return ActionResult.SUCCESS;
		} else {
			this.openScreen(world, pos, player);
			return ActionResult.CONSUME;
		}
	}
	
	protected void openScreen(World world, BlockPos pos, PlayerEntity player) {
		BlockEntity blockEntity = world.getBlockEntity(pos);
		if (blockEntity instanceof PedestalBlockEntity pedestalBlockEntity) {
			pedestalBlockEntity.setOwner(player);
			player.openHandledScreen((NamedScreenHandlerFactory) blockEntity);
		}
	}
	
	@Override
	public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
		if (newState.getBlock() instanceof PedestalBlock) {
			if (!state.isOf(newState.getBlock())) {
				// pedestal is getting upgraded. Keep the blockEntity with its contents
				BlockEntity blockEntity = world.getBlockEntity(pos);
				if (blockEntity instanceof PedestalBlockEntity) {
					if (state.getBlock().equals(newState.getBlock())) {
						PedestalVariant newVariant = ((PedestalBlock) newState.getBlock()).getVariant();
						((PedestalBlockEntity) blockEntity).setVariant(newVariant);
					}
				}
			}
		} else {
			super.onStateReplaced(state, world, pos, newState, moved);
		}
	}
	
	@Override
	@Nullable
	public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
		return new PedestalBlockEntity(pos, state);
	}
	
	@Override
	public BlockRenderType getRenderType(BlockState state) {
		return BlockRenderType.MODEL;
	}
	
	@Override
	public boolean hasComparatorOutput(BlockState state) {
		return true;
	}
	
	@Override
	public int getComparatorOutput(BlockState state, @NotNull World world, BlockPos pos) {
		return ScreenHandler.calculateComparatorOutput(world.getBlockEntity(pos));
	}
	
	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		return SHAPE;
	}
	
	@Override
	@Nullable
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(@NotNull World world, BlockState state, BlockEntityType<T> type) {
		if (world.isClient) {
			return checkType(type, SpectrumBlockEntities.PEDESTAL, PedestalBlockEntity::clientTick);
		} else {
			return checkType(type, SpectrumBlockEntities.PEDESTAL, PedestalBlockEntity::serverTick);
		}
	}
	
	@Override
	public void neighborUpdate(BlockState state, @NotNull World world, BlockPos pos, Block block, BlockPos fromPos, boolean notify) {
		if (!world.isClient) {
			if (this.checkGettingPowered(world, pos)) {
				this.power(world, pos);
			} else {
				this.unPower(world, pos);
			}
		}
	}
	
	@Override
	@Environment(EnvType.CLIENT)
	public void randomDisplayTick(@NotNull BlockState state, World world, BlockPos pos, Random random) {
		if (state.get(PedestalBlock.POWERED)) {
			Vector3f color = new Vector3f(0.5F, 0.5F, 0.5F);
			float xOffset = random.nextFloat();
			float zOffset = random.nextFloat();
			world.addParticle(new DustParticleEffect(color, 1.0F), pos.getX() + xOffset, pos.getY() + 1, pos.getZ() + zOffset, 0.0D, 0.0D, 0.0D);
		}
	}
	
	@Override
	public void onBroken(WorldAccess world, BlockPos pos, BlockState state) {
		if (world.isClient()) {
			clearCurrentlyRenderedMultiBlock((World) world);
		}
	}
	
	@Override
	public BlockState getPlacementState(@NotNull ItemPlacementContext ctx) {
		BlockState placementState = this.getDefaultState();
		
		if (ctx.getWorld().getReceivedRedstonePower(ctx.getBlockPos()) > 0) {
			placementState = placementState.with(POWERED, true);
		}
		
		return placementState;
	}
	
	public PedestalVariant getVariant() {
		return this.variant;
	}
	
	static {
		var foot = Block.createCuboidShape(3, 0, 3, 13, 3, 13);
		var neck = Block.createCuboidShape(5, 3, 5, 11, 12, 11);
		var head = Block.createCuboidShape(0, 12, 0, 16, 16, 16);
		foot = VoxelShapes.union(foot, neck);
		SHAPE = VoxelShapes.union(foot, head);
	}
	
	@Override
	public ActionResult onPaintBrushTrigger(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
		BlockEntity blockEntity = world.getBlockEntity(pos);
		if (blockEntity instanceof PedestalBlockEntity pedestalBlockEntity) {
			if (pedestalBlockEntity.craftingTime > 0) {
				return ActionResult.FAIL;
			}
			if (pedestalBlockEntity.currentRecipe == null) {
				return ActionResult.FAIL;
			}
			if (pedestalBlockEntity.currentRecipe instanceof GatedRecipe gatedRecipe && !gatedRecipe.canPlayerCraft(player)) {
				return ActionResult.FAIL;
			}
			
			if (!world.isClient) {
				pedestalBlockEntity.shouldCraft = true;
				SpectrumS2CPacketSender.spawnPedestalStartCraftingParticles(pedestalBlockEntity);
			}
			
			return ActionResult.success(world.isClient);
		}
		return ActionResult.FAIL;
	}
	
}