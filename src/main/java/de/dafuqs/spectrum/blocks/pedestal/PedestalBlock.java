package de.dafuqs.spectrum.blocks.pedestal;

import de.dafuqs.spectrum.registries.SpectrumBlockEntityRegistry;
import de.dafuqs.spectrum.registries.SpectrumBlocks;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.AutomaticItemPlacementContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.DustParticleEffect;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.StringIdentifiable;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3f;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.Random;

public class PedestalBlock extends BlockWithEntity {

    private final PedestalVariant variant;
    public static final EnumProperty<PedestalState> STATE = EnumProperty.of("state", PedestalState.class);

    public enum PedestalState implements StringIdentifiable {
        UNPOWERED("unpowered"),
        POWERED("powered");

        private final String name;

        private PedestalState(String name) {
            this.name = name;
        }

        public String toString() {
            return this.name;
        }

        public String asString() {
            return this.name;
        }
    }

    public enum PedestalVariant {
        BASIC_TOPAZ,
        BASIC_AMETHYST,
        BASIC_CITRINE,
        ALL_BASIC,
        ONYX,
        MOONSTONE
    }

    public PedestalBlock(Settings settings, PedestalVariant variant) {
        super(settings);
        this.variant = variant;
        setDefaultState(getStateManager().getDefaultState().with(STATE, PedestalState.UNPOWERED));
    }

    public void onPlaced(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
        if(placer instanceof ServerPlayerEntity) {
            BlockEntity blockEntity = world.getBlockEntity(pos);
            if(blockEntity instanceof PedestalBlockEntity) {
                ((PedestalBlockEntity) blockEntity).setOwner((ServerPlayerEntity) placer);
                blockEntity.markDirty();
            }
        }
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> stateManager) {
        stateManager.add(STATE);
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
        if (blockEntity instanceof PedestalBlockEntity) {
            PedestalBlockEntity pedestalBlockEntity = (PedestalBlockEntity) blockEntity;

            if(!pedestalBlockEntity.hasOwner()) {
                pedestalBlockEntity.setOwner(player);
            }

            player.openHandledScreen((NamedScreenHandlerFactory)blockEntity);
        }
    }

    public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
        if(newState.getBlock() instanceof PedestalBlock) {
            if (!state.getBlock().equals(newState.getBlock())) {
                // pedestal is getting upgraded. Keep the blockEntity with it's contents
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
            if (blockEntity instanceof PedestalBlockEntity) {
                ItemScatterer.spawn(world, pos, (Inventory)blockEntity);
                world.updateComparators(pos, this);
            }
            super.onStateReplaced(state, world, pos, newState, moved);
        }
    }

    /**
     * Sets pedestal to a new tier
     * while keeping the inventory and all other data
     */
    public static void upgradeToVariant(World world, BlockPos blockPos, PedestalVariant newPedestalVariant) {
        world.setBlockState(blockPos, getPedestalBlockForVariant(newPedestalVariant).getPlacementState(new AutomaticItemPlacementContext(world, blockPos, Direction.DOWN, null, Direction.UP)));
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

    public int getComparatorOutput(BlockState state, World world, BlockPos pos) {
        return ScreenHandler.calculateComparatorOutput(world.getBlockEntity(pos));
    }

    @Override
    @Nullable
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        if(world.isClient) {
            return null;
        } else {
            return checkType(type, SpectrumBlockEntityRegistry.PEDESTAL, PedestalBlockEntity::serverTick);
        }
    }

    @Override
    public void neighborUpdate(BlockState state, World world, BlockPos pos, Block block, BlockPos fromPos, boolean notify) {
        if (!world.isClient) {
            if(this.isGettingPowered(world, pos)) {
                this.power(world, pos);
            } else {
                this.unPower(world, pos);
            }
        }
    }

    public boolean isGettingPowered(World world, BlockPos pos) {
        Direction[] var4 = Direction.values();
        int var5 = var4.length;

        int var6;
        for(var6 = 0; var6 < var5; ++var6) {
            Direction direction = var4[var6];
            if (world.isEmittingRedstonePower(pos.offset(direction), direction)) {
                return true;
            }
        }

        if (world.isEmittingRedstonePower(pos, Direction.DOWN)) {
            return true;
        } else {
            BlockPos blockPos = pos.up();
            Direction[] var10 = Direction.values();
            var6 = var10.length;

            for(int var11 = 0; var11 < var6; ++var11) {
                Direction direction2 = var10[var11];
                if (direction2 != Direction.DOWN && world.isEmittingRedstonePower(blockPos.offset(direction2), direction2)) {
                    return true;
                }
            }
            return false;
        }
    }

    public void power(World world, BlockPos pos) {
        world.setBlockState(pos, world.getBlockState(pos).with(PedestalBlock.STATE, PedestalState.POWERED));
    }

    public void unPower(World world, BlockPos pos) {
        world.setBlockState(pos, world.getBlockState(pos).with(PedestalBlock.STATE, PedestalState.UNPOWERED));
    }

    @Environment(EnvType.CLIENT)
    public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
        if (state.get(PedestalBlock.STATE).equals(PedestalState.POWERED)) {
            Vec3f vec3f = new Vec3f(0.5F, 0.5F, 0.5F);
            float xOffset = random.nextFloat();
            float zOffset = random.nextFloat();
            world.addParticle(new DustParticleEffect(vec3f, 1.0F), pos.getX() + xOffset, pos.getY() + 1, pos.getZ() + zOffset, 0.0D, 0.0D, 0.0D);
        }
    }

    public BlockState getPlacementState(ItemPlacementContext ctx) {
        BlockState placementState = this.getDefaultState();

        int power = ctx.getWorld().getReceivedRedstonePower(ctx.getBlockPos());
        if(power > 0) {
            placementState = placementState.with(STATE, PedestalState.POWERED);
        }

        return placementState;
    }

    public PedestalVariant getVariant() {
        return this.variant;
    }

    public static Block getPedestalBlockForVariant(PedestalVariant variant) {
        switch (variant) {
            case BASIC_TOPAZ -> {
                return SpectrumBlocks.PEDESTAL_BASIC_TOPAZ;
            }
            case BASIC_AMETHYST -> {
                return SpectrumBlocks.PEDESTAL_BASIC_AMETHYST;
            }
            case BASIC_CITRINE -> {
                return SpectrumBlocks.PEDESTAL_BASIC_CITRINE;
            }
            case ALL_BASIC -> {
                return SpectrumBlocks.PEDESTAL_ALL_BASIC;
            }
            case ONYX -> {
                return SpectrumBlocks.PEDESTAL_ONYX;
            }
            default -> {
                return SpectrumBlocks.PEDESTAL_MOONSTONE;
            }
        }
    }

}