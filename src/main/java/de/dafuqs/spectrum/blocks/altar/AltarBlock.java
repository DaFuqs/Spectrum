package de.dafuqs.spectrum.blocks.altar;

import de.dafuqs.spectrum.registries.SpectrumBlockEntityRegistry;
import de.dafuqs.spectrum.registries.SpectrumBlocks;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
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
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.Random;

public class AltarBlock extends BlockWithEntity {

    public static final EnumProperty<AltarState> STATE = EnumProperty.of("state", AltarState.class);
    public static final EnumProperty<AltarTier> TIER = EnumProperty.of("tier", AltarTier.class);

    public enum AltarState implements StringIdentifiable {
        DEFAULT("default"),
        REDSTONE("redstone");

        private final String name;

        private AltarState(String name) {
            this.name = name;
        }

        public String toString() {
            return this.name;
        }

        public String asString() {
            return this.name;
        }
    }

    public enum AltarTier implements StringIdentifiable {
        TIER1("tier1"),
        TIER2("tier2"),
        TIER3("tier3");

        private final String name;

        private AltarTier(String name) {
            this.name = name;
        }

        public String toString() {
            return this.name;
        }

        public String asString() {
            return this.name;
        }
    }

    public AltarBlock(Settings settings) {
        super(settings);
        setDefaultState(getStateManager().getDefaultState().with(STATE, AltarState.DEFAULT).with(TIER, AltarTier.TIER1));
    }

    public void onPlaced(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
        if(placer instanceof ServerPlayerEntity) {
            BlockEntity blockEntity = world.getBlockEntity(pos);
            if(blockEntity instanceof AltarBlockEntity) {
                ((AltarBlockEntity) blockEntity).setOwner((ServerPlayerEntity) placer);
                blockEntity.markDirty();
            }
        }
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> stateManager) {
        stateManager.add(STATE);
        stateManager.add(TIER);
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
        if (blockEntity instanceof AltarBlockEntity) {
            AltarBlockEntity altarBlockEntity = (AltarBlockEntity) blockEntity;

            if(!altarBlockEntity.hasOwner()) {
                altarBlockEntity.setOwner(player);
            }

            player.openHandledScreen((NamedScreenHandlerFactory)blockEntity);
        }
    }

    public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
        if(state.isOf(newState.getBlock())) {
            BlockEntity blockEntity = world.getBlockEntity(pos);
            if (blockEntity instanceof AltarBlockEntity) {
                AltarTier altarTier = newState.get(AltarBlock.TIER);
                if(((AltarBlockEntity) blockEntity).getTier() != altarTier) {
                    ((AltarBlockEntity) blockEntity).setTier(altarTier);
                }
            }
        } else {
            BlockEntity blockEntity = world.getBlockEntity(pos);
            if (blockEntity instanceof AltarBlockEntity) {
                ItemScatterer.spawn(world, pos, (Inventory)blockEntity);
                world.updateComparators(pos, this);
            }
            super.onStateReplaced(state, world, pos, newState, moved);
        }
    }

    /**
     * Sets altar to a new tier
     * while keeping the inventory and all other data
     */
    public static void upgradeToTier(World world, BlockPos blockPos, AltarBlock.AltarTier newAltarTier) {
        world.setBlockState(blockPos, world.getBlockState(blockPos).with(TIER, newAltarTier));
    }

    @Nullable
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new AltarBlockEntity(pos, state);
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
            return checkType(type, SpectrumBlockEntityRegistry.ALTAR, AltarBlockEntity::serverTick);
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
        world.setBlockState(pos, world.getBlockState(pos).with(AltarBlock.STATE, AltarState.REDSTONE));
    }

    public void unPower(World world, BlockPos pos) {
        world.setBlockState(pos, world.getBlockState(pos).with(AltarBlock.STATE, AltarState.DEFAULT));
    }

    @Environment(EnvType.CLIENT)
    public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
        if (world.getBlockState(pos).equals(SpectrumBlocks.ALTAR.getDefaultState().with(AltarBlock.STATE, AltarState.REDSTONE))) {
            Vec3f vec3f = new Vec3f(0.5F, 0.5F, 0.5F);
            float xOffset = random.nextFloat();
            float zOffset = random.nextFloat();
            world.addParticle(new DustParticleEffect(vec3f, 1.0F), pos.getX() + xOffset, pos.getY() + 1, pos.getZ() + zOffset, 0.0D, 0.0D, 0.0D);
        }
    }

    public BlockState getPlacementState(ItemPlacementContext ctx) {
        BlockState placementState = this.getDefaultState();

        Item placementItem = ctx.getStack().getItem();
        if(placementItem instanceof AltarBlockItem) {
            AltarTier tier = ((AltarBlockItem) placementItem).getAltarTier();
            if(tier != null) {
                placementState = placementState.with(TIER, tier);
            }
        }

        int power = ctx.getWorld().getReceivedRedstonePower(ctx.getBlockPos());
        if(power > 0) {
            placementState = placementState.with(STATE, AltarState.REDSTONE);
        }

        return placementState;
    }

}