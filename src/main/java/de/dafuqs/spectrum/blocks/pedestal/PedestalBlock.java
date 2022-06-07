package de.dafuqs.spectrum.blocks.pedestal;

import de.dafuqs.spectrum.SpectrumCommon;
import de.dafuqs.spectrum.blocks.RedstonePoweredBlock;
import de.dafuqs.spectrum.enums.PedestalRecipeTier;
import de.dafuqs.spectrum.particle.SpectrumParticleTypes;
import de.dafuqs.spectrum.registries.SpectrumBlockEntityRegistry;
import de.dafuqs.spectrum.registries.SpectrumMultiblocks;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.AutomaticItemPlacementContext;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.DustParticleEffect;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.util.*;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3f;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import vazkii.patchouli.api.IMultiblock;
import vazkii.patchouli.api.PatchouliAPI;

import java.util.Random;

public class PedestalBlock extends BlockWithEntity implements RedstonePoweredBlock {
	
	public static final Identifier UNLOCK_IDENTIFIER = new Identifier(SpectrumCommon.MOD_ID, "place_pedestal");
	public static final BooleanProperty POWERED = BooleanProperty.of("powered");
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
	
	public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
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
	
	public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
		if (newState.getBlock() instanceof PedestalBlock) {
			if (!state.getBlock().equals(newState.getBlock())) {
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
			BlockEntity blockEntity = world.getBlockEntity(pos);
			if (blockEntity instanceof PedestalBlockEntity pedestalBlockEntity) {
				ItemScatterer.spawn(world, pos, pedestalBlockEntity);
				world.updateComparators(pos, this);
			}
			super.onStateReplaced(state, world, pos, newState, moved);
		}
	}
	
	@Nullable
	public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
		return new PedestalBlockEntity(pos, state);
	}
	
	public BlockRenderType getRenderType(BlockState state) {
		return BlockRenderType.MODEL;
	}
	
	public boolean hasComparatorOutput(BlockState state) {
		return true;
	}
	
	public int getComparatorOutput(BlockState state, @NotNull World world, BlockPos pos) {
		return ScreenHandler.calculateComparatorOutput(world.getBlockEntity(pos));
	}
	
	@Override
	@Nullable
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(@NotNull World world, BlockState state, BlockEntityType<T> type) {
		if (world.isClient) {
			return checkType(type, SpectrumBlockEntityRegistry.PEDESTAL, PedestalBlockEntity::clientTick);
		} else {
			return checkType(type, SpectrumBlockEntityRegistry.PEDESTAL, PedestalBlockEntity::serverTick);
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
	
	@Environment(EnvType.CLIENT)
	public void randomDisplayTick(@NotNull BlockState state, World world, BlockPos pos, Random random) {
		if (state.get(PedestalBlock.POWERED)) {
			Vec3f vec3f = new Vec3f(0.5F, 0.5F, 0.5F);
			float xOffset = random.nextFloat();
			float zOffset = random.nextFloat();
			world.addParticle(new DustParticleEffect(vec3f, 1.0F), pos.getX() + xOffset, pos.getY() + 1, pos.getZ() + zOffset, 0.0D, 0.0D, 0.0D);
		}
	}
	
	@Override
	public void onBroken(WorldAccess world, BlockPos pos, BlockState state) {
		if (world.isClient()) {
			clearCurrentlyRenderedMultiBlock((World) world);
		}
	}
	
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
	
}