package de.dafuqs.spectrum.blocks.fusion_shrine;

import de.dafuqs.spectrum.InventoryHelper;
import de.dafuqs.spectrum.blocks.pedestal.PedestalBlock;
import de.dafuqs.spectrum.blocks.pedestal.PedestalBlockEntity;
import de.dafuqs.spectrum.progression.SpectrumAdvancementCriteria;
import de.dafuqs.spectrum.registries.SpectrumBlockEntityRegistry;
import de.dafuqs.spectrum.registries.SpectrumMultiblocks;
import net.fabricmc.fabric.mixin.transfer.BucketItemAccessor;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.ai.pathing.NavigationType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.Fluids;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.BucketItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.IntProperty;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.ActionResult;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.Hand;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import vazkii.patchouli.api.IMultiblock;
import vazkii.patchouli.api.PatchouliAPI;

public class FusionShrineBlock extends BlockWithEntity {

    public static final IntProperty LIGHT_LEVEL = IntProperty.of("light_level", 0, 15);
    protected static final VoxelShape SHAPE = Block.createCuboidShape(2.0D, 0.0D, 2.0D, 14.0D, 15.0D, 14.0D);

    public FusionShrineBlock(Settings settings) {
        super(settings);
        setDefaultState(getStateManager().getDefaultState().with(LIGHT_LEVEL, 0));
    }

    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(LIGHT_LEVEL);
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new FusionShrineBlockEntity(pos, state);
    }

    public boolean canPathfindThrough(BlockState state, BlockView world, BlockPos pos, NavigationType type) {
        return false;
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if(world.isClient) {
            return ActionResult.SUCCESS;
        } else {
            ItemStack itemStack = player.getStackInHand(hand);

            if (itemStack.getItem() instanceof BucketItem) {
                BlockEntity blockEntity = world.getBlockEntity(pos);
                if (blockEntity instanceof FusionShrineBlockEntity fusionShrineBlockEntity) {
                    Fluid storedFluid = fusionShrineBlockEntity.getFluid();
                    Fluid bucketFluid = ((BucketItemAccessor) itemStack.getItem()).fabric_getFluid();
                    if (storedFluid == Fluids.EMPTY && bucketFluid != Fluids.EMPTY) {
                        fusionShrineBlockEntity.setFluid(bucketFluid);
                        if (!player.isCreative()) {
                            player.setStackInHand(hand, new ItemStack(Items.BUCKET));
                        }
                    } else if (storedFluid != Fluids.EMPTY && bucketFluid == Fluids.EMPTY) {
                        fusionShrineBlockEntity.setFluid(Fluids.EMPTY);
                        world.setBlockState(pos, world.getBlockState(pos).with(LIGHT_LEVEL, 0));
                        if (!player.isCreative()) {
                            player.setStackInHand(hand, new ItemStack(storedFluid.getBucketItem()));
                        }
                    }
                }
            } else {
                if(verifyStructure(world, pos.down(), (ServerPlayerEntity) player)) {
                    FusionShrineBlockEntity fusionShrineBlockEntity = ((FusionShrineBlockEntity) world.getBlockEntity(pos));
                    // if the structure is valid the player can put / retrieve blocks into the shrine
                    if(player.isSneaking()) {
                        ItemStack retrievedStack = ItemStack.EMPTY;
                        Inventory inventory = fusionShrineBlockEntity.getInventory();
                        for(int i = 0; i < inventory.size(); i++) {
                            retrievedStack = inventory.removeStack(i);
                            if(!retrievedStack.isEmpty()) {
                                break;
                            }
                        }
                        if(!retrievedStack.isEmpty()) {
                            player.giveItemStack(retrievedStack);
                            fusionShrineBlockEntity.updateInClientWorld();
                        }
                    } else {
                        ItemStack remainingStack = InventoryHelper.addToInventory(itemStack, fusionShrineBlockEntity.getInventory(), null);
                        player.setStackInHand(hand, remainingStack);
                        fusionShrineBlockEntity.updateInClientWorld();
                    }
                }
            }
            return ActionResult.CONSUME;
        }
    }

    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return SHAPE;
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return checkType(type, SpectrumBlockEntityRegistry.FUSION_SHRINE, FusionShrineBlockEntity::tick);
    }

    public boolean verifyStructure(World world, BlockPos blockPos, ServerPlayerEntity serverPlayerEntity) {
        IMultiblock multiblock = SpectrumMultiblocks.MULTIBLOCKS.get(SpectrumMultiblocks.FUSION_SHRINE_IDENTIFIER);
        boolean valid = multiblock.validate(world, blockPos, BlockRotation.NONE);

        if(valid) {
            SpectrumAdvancementCriteria.COMPLETED_MULTIBLOCK.trigger(serverPlayerEntity, multiblock);
        } else {
            IMultiblock currentMultiBlock = PatchouliAPI.get().getCurrentMultiblock();
            if(currentMultiBlock == multiblock) {
                PatchouliAPI.get().clearMultiblock();
            } else {
                PatchouliAPI.get().showMultiblock(multiblock, new TranslatableText("multiblock.spectrum.fusion_shrine.structure"), blockPos.down(), BlockRotation.NONE);
                scatterContents(world, blockPos);
            }
        }

        return valid;
    }

    // drop all currently stored items
    public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
        if(!newState.getBlock().equals(this)) { // happens when filling with fluid
            scatterContents(world, pos);
        }
        super.onStateReplaced(state, world, pos, newState, moved);
    }

    public void scatterContents(World world, BlockPos pos) {
        BlockEntity blockEntity = world.getBlockEntity(pos);
        if (blockEntity instanceof FusionShrineBlockEntity) {
            ItemScatterer.spawn(world, pos, ((FusionShrineBlockEntity) blockEntity).getInventory());
            world.updateComparators(pos, this);
        }
    }

}
