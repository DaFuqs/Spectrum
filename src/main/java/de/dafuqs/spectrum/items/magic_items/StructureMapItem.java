package de.dafuqs.spectrum.items.magic_items;

import com.google.common.collect.Iterables;
import com.google.common.collect.LinkedHashMultiset;
import com.google.common.collect.Multiset;
import com.google.common.collect.Multisets;
import com.mojang.datafixers.util.Pair;
import de.dafuqs.spectrum.items.map.StructureMapState;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.MapColor;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.item.FilledMapItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.item.map.MapIcon;
import net.minecraft.item.map.MapState;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.Registry;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.structure.StructureStart;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import net.minecraft.world.Heightmap;
import net.minecraft.world.World;
import net.minecraft.world.chunk.WorldChunk;
import net.minecraft.world.gen.structure.Structure;
import org.jetbrains.annotations.Nullable;

public class StructureMapItem extends FilledMapItem {

    public StructureMapItem(Settings settings) {
        super(settings);
    }

    private static void createAndSetState(ItemStack stack, ServerWorld world, int centerX, int centerZ, @Nullable StructureStart target, @Nullable Identifier targetId) {
        NbtCompound nbt = stack.getOrCreateNbt();

        int id;
        if (nbt.contains("map")) {
            id = nbt.getInt("map");
        } else {
            id = world.getNextMapId();
            nbt.putInt("map", id);
        }

        StructureMapState state = new StructureMapState(centerX, centerZ, (byte) 1, true, true, false, world.getRegistryKey());
        setTarget(stack, state, target, targetId);
        world.putMapState(getMapName(id), state);
    }

    private static void setTarget(ItemStack stack, StructureMapState structureState, @Nullable StructureStart target, @Nullable Identifier targetId) {
        structureState.setTarget(target);
        structureState.setTargetId(targetId);
        StructureMapState.removeDecorationsNbt(stack, "target");
        if (target != null) {
            StructureMapState.addDecorationsNbt(stack, target.getPos().getCenterAtY(0), "target", MapIcon.Type.TARGET_POINT);
        }
    }

    private void updateTarget(ItemStack stack, World world, Entity entity, MapState state) {
        if (!(state instanceof StructureMapState structureState && world instanceof ServerWorld serverWorld)) {
            return;
        }
        if (!structureState.isTargetDirty()) {
            return;
        }

        Registry<Structure> registry = StructureMapState.getStructureRegistry(serverWorld);
        Identifier targetId = structureState.getTargetId();
        if (registry != null && targetId != null) {
            StructureStart start = StructureMapState.locateNearestStructure(serverWorld, targetId, entity.getBlockPos(), 50);
            if (start != null) {
                setTarget(stack, structureState, start, targetId);
            }
        }
        structureState.markTargetClean();
    }

    @Override
    public void updateColors(World world, Entity entity, MapState state) {
        if (world.getRegistryKey() != state.dimension || !(entity instanceof PlayerEntity playerEntity) || !(state instanceof StructureMapState structureState)) {
            return;
        }

        boolean hasCeiling = world.getDimension().hasCeiling();
        int scale = 1 << state.scale;
        Vec3d displayedCenter = structureState.getDisplayedCenter();

        MapState.PlayerUpdateTracker playerUpdateTracker = state.getPlayerSyncData(playerEntity);
        playerUpdateTracker.field_131++;

        for(int x = 0; x < 128; x++) {
            double previousHeight = 0.0;

            for(int z = -1; z < 128; z++) {
                int blockX = ((int) displayedCenter.getX() / scale + x - 64) * scale;
                int blockZ = ((int) displayedCenter.getZ() / scale + z - 64) * scale;

                Multiset<MapColor> multiset = LinkedHashMultiset.create();
                WorldChunk chunk = world.getChunk(ChunkSectionPos.getSectionCoord(blockX), ChunkSectionPos.getSectionCoord(blockZ));
                if (chunk.isEmpty()) {
                    continue;
                }

                int fluidDepth = 0;
                double height = 0.0;
                if (hasCeiling) {
                    int hash = blockX + blockZ * 231871;
                    hash = hash * hash * 31287121 + hash * 11;
                    if ((hash >> 20 & 1) == 0) {
                        multiset.add(Blocks.DIRT.getDefaultState().getMapColor(world, BlockPos.ORIGIN), 10);
                    } else {
                        multiset.add(Blocks.STONE.getDefaultState().getMapColor(world, BlockPos.ORIGIN), 100);
                    }

                    height = 100.0;
                } else {
                    for(int sampleX = 0; sampleX < scale; sampleX++) {
                        for(int sampleZ = 0; sampleZ < scale; sampleZ++) {
                            BlockPos.Mutable samplePos = new BlockPos.Mutable(blockX + sampleX, 0, blockZ + sampleZ);
                            int sampleY = chunk.sampleHeightmap(Heightmap.Type.WORLD_SURFACE, samplePos.getX(), samplePos.getZ()) + 1;

                            BlockState blockState;
                            if (sampleY <= world.getBottomY() + 1) {
                                blockState = Blocks.BEDROCK.getDefaultState();
                            } else {
                                do {
                                    sampleY--;
                                    samplePos.setY(sampleY);
                                    blockState = chunk.getBlockState(samplePos);
                                } while(blockState.getMapColor(world, samplePos) == MapColor.CLEAR && sampleY > world.getBottomY());

                                if (sampleY > world.getBottomY() && !blockState.getFluidState().isEmpty()) {
                                    int fluidY = sampleY - 1;
                                    BlockPos.Mutable fluidPos = samplePos.mutableCopy();

                                    BlockState fluidBlockState;
                                    do {
                                        fluidPos.setY(fluidY--);
                                        fluidBlockState = chunk.getBlockState(fluidPos);
                                        fluidDepth++;
                                    } while(fluidY > world.getBottomY() && !fluidBlockState.getFluidState().isEmpty());

                                    blockState = this.getFluidStateIfVisible(world, blockState, samplePos);
                                }
                            }

                            state.removeBanner(world, samplePos.getX(), samplePos.getZ());
                            height += (double) sampleY / (double) (scale * scale);
                            multiset.add(blockState.getMapColor(world, samplePos));
                        }
                    }
                }

                fluidDepth /= scale * scale;
                MapColor color = Iterables.getFirst(Multisets.copyHighestCountFirst(multiset), MapColor.CLEAR);
                MapColor.Brightness brightness;

                int odd = x + z & 1;
                if (color == MapColor.WATER_BLUE) {
                    double depth = (double) fluidDepth * 0.1 + (double) odd * 0.2;
                    if (depth < 0.5) {
                        brightness = MapColor.Brightness.HIGH;
                    } else if (depth > 0.9) {
                        brightness = MapColor.Brightness.LOW;
                    } else {
                        brightness = MapColor.Brightness.NORMAL;
                    }
                } else {
                    double f = (height - previousHeight) * 4.0 / (double) (scale + 4) + ((double) odd - 0.5) * 0.4;
                    if (f > 0.6) {
                        brightness = MapColor.Brightness.HIGH;
                    } else if (f < -0.6) {
                        brightness = MapColor.Brightness.LOW;
                    } else {
                        brightness = MapColor.Brightness.NORMAL;
                    }
                }

                previousHeight = height;
                if (z >= 0) {
                    state.putColor(x, z, color.getRenderColorByte(brightness));
                }
            }
        }
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        MapState state = getMapState(stack, world);
        if (state != null) {
            this.updateTarget(stack, world, entity, state);
        }
        super.inventoryTick(stack, world, entity, slot, selected);
    }

    private BlockState getFluidStateIfVisible(World world, BlockState state, BlockPos pos) {
        FluidState fluidState = state.getFluidState();
        return !fluidState.isEmpty() && !state.isSideSolidFullSquare(world, pos, Direction.UP) ? fluidState.getBlockState() : state;
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        if (!context.getWorld().isClient() && context.getWorld() instanceof ServerWorld serverWorld && context.getPlayer() instanceof ServerPlayerEntity serverPlayerEntity) {
            ItemStack stack = serverPlayerEntity.getStackInHand(context.getHand());
            if (serverPlayerEntity.isSneaking()) {
                Vec3d hitPos = context.getHitPos();
                BlockPos blockPos = BlockPos.ofFloored(hitPos.getX(), hitPos.getY(), hitPos.getZ());
                Pair<Identifier, StructureStart> pair = StructureMapState.locateAnyStructureAtBlock(serverWorld, blockPos);
                if (pair != null) {
                    createAndSetState(stack, serverWorld, (int) serverPlayerEntity.getX(), (int) serverPlayerEntity.getZ(), pair.getSecond(), pair.getFirst());
                }
            }
        }

        return ActionResult.success(context.getWorld().isClient());
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack stack = user.getStackInHand(hand);

        if (!world.isClient() && world instanceof ServerWorld serverWorld && user instanceof ServerPlayerEntity serverPlayerEntity) {
            if (user.isSneaking()) {
                createAndSetState(stack, serverWorld, (int) serverPlayerEntity.getX(), (int) serverPlayerEntity.getZ(), null, null);
            }
        }

        return TypedActionResult.success(stack, world.isClient());
    }

}
