package de.dafuqs.spectrum.blocks.fusion_shrine;

import com.klikli_dev.modonomicon.api.multiblock.*;
import com.mojang.serialization.*;
import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.blocks.*;
import de.dafuqs.spectrum.blocks.upgrade.*;
import de.dafuqs.spectrum.compat.modonomicon.*;
import de.dafuqs.spectrum.helpers.*;
import de.dafuqs.spectrum.networking.s2c_payloads.*;
import de.dafuqs.spectrum.particle.effect.*;
import de.dafuqs.spectrum.progression.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.core.*;
import net.minecraft.resources.*;
import net.minecraft.server.level.*;
import net.minecraft.sounds.*;
import net.minecraft.util.*;
import net.minecraft.world.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.item.*;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.item.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.*;
import net.minecraft.world.level.block.state.*;
import net.minecraft.world.level.block.state.properties.*;
import net.minecraft.world.level.levelgen.*;
import net.minecraft.world.phys.*;
import net.minecraft.world.phys.shapes.*;
import net.neoforged.neoforge.fluids.*;
import net.neoforged.neoforge.fluids.capability.*;
import net.neoforged.neoforge.fluids.capability.templates.*;
import org.jspecify.annotations.Nullable;

import java.util.*;

@SuppressWarnings("UnstableApiUsage")
public class FusionShrineBlock extends InWorldInteractionBlock {
	
	public static final MapCodec<FusionShrineBlock> CODEC = simpleCodec(FusionShrineBlock::new);
	
	public static final ResourceLocation UNLOCK_IDENTIFIER = SpectrumCommon.locate("collect_all_basic_pigments_besides_brown");
	public static final IntegerProperty LIGHT_LEVEL = IntegerProperty.create("light_level", 0, 15);
	protected static final VoxelShape SHAPE;
	
	public static final List<Vec3i> UPGRADE_BLOCK_OFFSETS = List.of(
			new Vec3i(2, 0, 2),
			new Vec3i(-2, 0, 2),
			new Vec3i(2, 0, -2),
			new Vec3i(-2, 0, -2)
	);
	
	public FusionShrineBlock(Properties settings) {
		super(settings);
		registerDefaultState(getStateDefinition().any().setValue(LIGHT_LEVEL, 0));
		
		Upgradeable.registerUpgradePosOffsets(UPGRADE_BLOCK_OFFSETS);
	}
	
	@Override
	public MapCodec<? extends FusionShrineBlock> codec() {
		return CODEC;
	}
	
	public static void clearCurrentlyRenderedMultiBlock(Level world) {
		ModonomiconHelper.clearRenderedMultiblock(SpectrumMultiblocks.get(SpectrumMultiblocks.FUSION_SHRINE));
	}
	
	/*
		The shrine needs an air-like block just above and sky access all the rest of the way
	 */
	public static boolean verifySkyAccess(ServerLevel world, BlockPos shrinePos) {
		int x = shrinePos.getX();
		int z = shrinePos.getZ();
		int l = world.getHeight(Heightmap.Types.WORLD_SURFACE, x, z);
		BlockPos.MutableBlockPos blockPos = new BlockPos.MutableBlockPos();
		for (int m = shrinePos.getY() + 1; m <= l; ++m) {
			blockPos = blockPos.set(x, m, z);
			BlockState blockState = world.getBlockState(blockPos);
			if ((m == shrinePos.getY() + 1 && !blockState.isAir()) || !blockState.propagatesSkylightDown(world, blockPos)) {
				world.playSound(null, shrinePos, SpectrumSoundEvents.USE_FAIL, SoundSource.NEUTRAL, 1.0F, 1.0F);
				PlayParticleWithRandomOffsetAndVelocityPayload.playParticleWithRandomOffsetAndVelocity(world, blockPos.getCenter(), ColoredSparkleRisingParticleEffect.RED, 8, Vec3.ZERO, new Vec3(0.1, 0.1, 0.1));
				return false;
			}
		}
		return true;
	}
	
	public static boolean verifyStructure(Level world, BlockPos blockPos, @Nullable ServerPlayer serverPlayerEntity) {
		Multiblock multiblock = SpectrumMultiblocks.get(SpectrumMultiblocks.FUSION_SHRINE);
		boolean valid = multiblock.validate(world, blockPos.below(), Rotation.NONE);
		
		if (valid) {
			if (serverPlayerEntity != null) {
				SpectrumAdvancementCriteria.COMPLETED_MULTIBLOCK.trigger(serverPlayerEntity, multiblock);
			}
		} else {
            if (world.isClientSide()) {
                ModonomiconHelper.renderMultiblock(multiblock, SpectrumMultiblocks.FUSION_SHRINE_TEXT, blockPos.below(2), Rotation.NONE);
            } else if (world.getBlockEntity(blockPos) instanceof FusionShrineBlockEntity fusionShrineBlockEntity) {
                fusionShrineBlockEntity.scatterContents(world);
            }
		}
		
		return valid;
	}
	
	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(LIGHT_LEVEL);
	}

	@Override
	public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new FusionShrineBlockEntity(pos, state);
	}
	
	@Override
	public int getAnalogOutputSignal(BlockState state, Level world, BlockPos pos) {
		if (world.getBlockEntity(pos) instanceof FusionShrineBlockEntity blockEntity) {
			NonNullList<ItemStack> inventory = blockEntity.getItems();
			
			int i = 0;
			float f = 0.0f;
			for (int j = 0; j < inventory.size(); ++j) {
				ItemStack itemStack = blockEntity.getItem(j);
				if (itemStack.isEmpty()) continue;
				f += (float) itemStack.getCount() / (float) Math.min(blockEntity.getMaxStackSize(), itemStack.getMaxStackSize());
				++i;
			}
			
			FluidTank tank = blockEntity.getTank();
			if (!blockEntity.getTank().isEmpty()) {
				f += (float) tank.getFluidAmount() / (float) tank.getCapacity();
				++i;
			}
			
			return Mth.floor(f / ((float) inventory.size() + 1) * 14.0f) + (i > 0 ? 1 : 0);
		}
		
		return 0;
	}
	
	@Override
	public void destroy(LevelAccessor world, BlockPos pos, BlockState state) {
		if (world.isClientSide()) {
			clearCurrentlyRenderedMultiBlock((Level) world);
		}
	}
	
	@Override
	public void fallOn(Level world, BlockState state, BlockPos pos, Entity entity, float fallDistance) {
		if (!world.isClientSide()) {
			// Specially handle fluid items
			BlockEntity blockEntity = world.getBlockEntity(pos);
			if (entity instanceof ItemEntity itemEntity && blockEntity instanceof FusionShrineBlockEntity fusionShrineBlockEntity) {
				ItemStack itemStack = itemEntity.getItem();
				Optional<IFluidHandlerItem> fluidHandler = FluidUtil.getFluidHandler(itemStack);
				
				// We're not considering stacked fluid storages for the time being
				if (fluidHandler.isPresent()) {
					FluidUtil.tryFluidTransfer(fusionShrineBlockEntity.tank, fluidHandler.get(), 1000, true);
				} else {
					itemEntity.setItem(InventoryHelper.smartAddToInventory(itemStack, fusionShrineBlockEntity, null));
					fusionShrineBlockEntity.inventoryChanged();
					return;
				}
			}
			
			// do not pick up items that were results of crafting
			if (entity.position().x % 0.5 != 0 && entity.position().z % 0.5 != 0) {
				super.fallOn(world, state, pos, entity, fallDistance);
			}
		}
	}
	
	@Override
	public ItemInteractionResult useItemOn(ItemStack handStack, BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (world.isClientSide()) {
            verifyStructure(world, pos, null);
            return ItemInteractionResult.SUCCESS;
        } else {
            verifySkyAccess((ServerLevel) world, pos);

            // if the structure is valid the player can put / retrieve items and fluids into the shrine
            BlockEntity blockEntity = world.getBlockEntity(pos);
            if (blockEntity instanceof FusionShrineBlockEntity fusionShrineBlockEntity && verifyStructure(world, pos, (ServerPlayer) player)) {
                fusionShrineBlockEntity.setOwner(player);

                if (FluidUtil.interactWithFluidHandler(player, hand, fusionShrineBlockEntity.getTank())) {
                    fusionShrineBlockEntity.inventoryChanged();
                    return ItemInteractionResult.CONSUME;
                }
                if ((player.isShiftKeyDown() || handStack.isEmpty()) && retrieveLastStack(world, pos, player, hand, handStack, fusionShrineBlockEntity)) {
                    fusionShrineBlockEntity.inventoryChanged();
                    return ItemInteractionResult.CONSUME;
                }
                if (!handStack.isEmpty() && inputHandStack(world, player, hand, handStack, fusionShrineBlockEntity)) {
                    fusionShrineBlockEntity.inventoryChanged();
                    return ItemInteractionResult.CONSUME;
                }
            }

            return ItemInteractionResult.CONSUME;
        }
	}
	
	@Override
	public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
		return SHAPE;
	}

	@Override
	public <T extends BlockEntity> @Nullable BlockEntityTicker<T> getTicker(Level world, BlockState state, BlockEntityType<T> type) {
		return createTickerHelper(type, SpectrumBlockEntities.FUSION_SHRINE.get(), world.isClientSide() ? FusionShrineBlockEntity::clientTick : FusionShrineBlockEntity::serverTick);
	}
	
	static {
		VoxelShape neck = Block.box(2, 0, 2, 14, 12, 14);
		VoxelShape head = Block.box(1, 12, 1, 15, 15, 15);
		VoxelShape crystal = Block.box(6.5, 13, 6.5, 9.5, 23, 9.5);
		neck = Shapes.or(neck, head);
		SHAPE = Shapes.or(neck, crystal);
	}
}
