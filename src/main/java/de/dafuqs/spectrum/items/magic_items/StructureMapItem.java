package de.dafuqs.spectrum.items.magic_items;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.FilledMapItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.item.map.MapIcon;
import net.minecraft.item.map.MapState;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.structure.StructureStart;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.List;

public class StructureMapItem extends FilledMapItem {

    public StructureMapItem(Settings settings) {
        super(settings);
    }

    private static void createAndSetState(ItemStack stack, ServerWorld world, double x, double z) {
        NbtCompound nbt = stack.getOrCreateNbt();

        int id;
        if (nbt.contains("map")) {
            id = nbt.getInt("map");
        } else {
            id = world.getNextMapId();
            nbt.putInt("map", id);
            MapState.addDecorationsNbt(stack, new BlockPos((int) x, 0, (int) z), "+", MapIcon.Type.RED_X);
        }

        MapState state = MapState.of(x, z, (byte) 1, true, true, world.getRegistryKey());
        world.putMapState(getMapName(id), state);
        fillExplorationMap(world, stack);
    }

    private static void setTarget(ItemStack stack, ServerWorld world, StructureStart start) {
        BlockPos pos = start.getPos().getCenterAtY(0);
        createAndSetState(stack, world, start.getPos().getCenterX(), start.getPos().getCenterZ());

        NbtCompound nbt = stack.getNbt();
        if (nbt != null) {
            NbtCompound mapTarget = new NbtCompound();
            mapTarget.putInt("x", pos.getX());
            mapTarget.putInt("z", pos.getZ());
            nbt.put("mapTarget", mapTarget);
        }
    }

    private static void clearTarget(ItemStack stack, ServerWorld world, ServerPlayerEntity player) {
        createAndSetState(stack, world, player.getX(), player.getZ());

        NbtCompound nbt = stack.getNbt();
        if (nbt != null) {
            nbt.remove("mapTarget");
        }
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        if (!context.getWorld().isClient() && context.getWorld() instanceof ServerWorld serverWorld && context.getPlayer() instanceof ServerPlayerEntity serverPlayerEntity) {
            ItemStack stack = serverPlayerEntity.getStackInHand(context.getHand());
            if (serverPlayerEntity.isSneaking()) {
                Vec3d hitPos = context.getHitPos();
                BlockPos blockPos = BlockPos.ofFloored(hitPos.getX(), hitPos.getY(), hitPos.getZ());
                ChunkPos chunkPos = serverWorld.getChunk(blockPos).getPos();
                List<StructureStart> starts = serverWorld.getStructureAccessor().getStructureStarts(chunkPos, o -> true);
                for (StructureStart start : starts) {
                    if (start.getBoundingBox().contains(blockPos)) {
                        setTarget(stack, serverWorld, start);
                        break;
                    }
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
                clearTarget(stack, serverWorld, serverPlayerEntity);
            }
        }

        return TypedActionResult.success(stack, world.isClient());
    }
}
