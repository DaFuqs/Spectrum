package de.dafuqs.spectrum.items.map;

import com.mojang.datafixers.util.Pair;
import de.dafuqs.spectrum.mixin.accessors.MapStateAccessor;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.map.MapBannerMarker;
import net.minecraft.item.map.MapIcon;
import net.minecraft.item.map.MapState;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.entry.RegistryEntryList;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.structure.StructurePiece;
import net.minecraft.structure.StructureStart;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.*;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.gen.structure.Structure;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class StructureMapState extends MapState {

    private final MapStateAccessor accessor;
    private Vec3d displayedCenter;
    @Nullable
    private StructureStart target;
    private Identifier targetId;
    private boolean displayNeedsUpdate;

    public StructureMapState(double centerX, double centerZ, byte scale, boolean showIcons, boolean unlimitedTracking, boolean locked, RegistryKey<World> dimension) {
        super((int) centerX, (int) centerZ, scale, showIcons, unlimitedTracking, locked, dimension);
        this.accessor = (MapStateAccessor) this;
        this.displayedCenter = new Vec3d(centerX, 0, centerZ);
        this.displayNeedsUpdate = true;
    }

    public StructureMapState(double centerX, double centerZ, byte scale, boolean showIcons, boolean unlimitedTracking, boolean locked, RegistryKey<World> dimension, NbtCompound nbt) {
        this((int) centerX, (int) centerZ, scale, showIcons, unlimitedTracking, locked, dimension);

        if (nbt.contains("targetId", NbtElement.STRING_TYPE)) {
            this.targetId = new Identifier(nbt.getString("targetId"));
        } else {
            this.targetId = null;
        }
        this.target = null;

        double xDisplay = nbt.contains("displayX", NbtElement.DOUBLE_TYPE) ? nbt.getDouble("displayX") : this.displayedCenter.getX();
        double zDisplay = nbt.contains("displayZ", NbtElement.DOUBLE_TYPE) ? nbt.getDouble("displayZ") : this.displayedCenter.getZ();
        this.displayedCenter = new Vec3d(xDisplay, 0, zDisplay);
    }

    public static @Nullable Pair<Identifier, StructureStart> locateAnyStructureAtBlock(ServerWorld world, BlockPos pos) {
        Registry<Structure> registry = world.getRegistryManager().getOptional(RegistryKeys.STRUCTURE).orElse(null);
        if (registry != null) {
            for (Structure structure : registry.stream().toList()) {
                Identifier id = registry.getId(structure);
                StructureStart start = world.getStructureAccessor().getStructureContaining(pos, structure);
                if (start != StructureStart.DEFAULT && id != null) {
                    return new Pair<>(id, start);
                }
            }
        }
        return null;
    }

    public static @Nullable StructureStart locateNearestStructure(ServerWorld world, Identifier structureId, BlockPos center, int radius) {
        Registry<Structure> registry = getStructureRegistry(world);
        if (registry != null) {
            return locateNearestStructure(world, registry.get(structureId), center, radius);
        }
        return null;
    }

    public static @Nullable StructureStart locateNearestStructure(ServerWorld world, Structure structure, BlockPos center, int radius) {
        if (world.getServer().getSaveProperties().getGeneratorOptions().shouldGenerateStructures()) {
            Registry<Structure> registry = getStructureRegistry(world);
            if (registry != null) {
                RegistryEntryList<Structure> entryList = new RegistryEntryList.Direct<>(List.of(registry.getEntry(structure)));
                Pair<BlockPos, RegistryEntry<Structure>> pair = world.getChunkManager().getChunkGenerator().locateStructure(world, entryList, center, radius, false);
                if (pair != null) {
                    BlockPos pos = pair.getFirst();
                    return locateStructureAtBlock(world, structure, pos);
                }
            }
        }
        return null;
    }

    public static @Nullable StructureStart locateStructureAtBlock(ServerWorld world, Structure structure, BlockPos pos) {
        Registry<Structure> registry = getStructureRegistry(world);
        if (registry != null) {
            for (StructureStart start : world.getStructureAccessor().getStructureStarts(ChunkSectionPos.from(pos), structure)) {
                if (start == StructureStart.DEFAULT) continue;
                for (StructurePiece piece : start.getChildren()) {
                    BlockBox box = piece.getBoundingBox();
                    if (box.getMinX() <= pos.getX() && pos.getX() <= box.getMaxX() && box.getMinZ() <= pos.getZ() && pos.getZ() <= box.getMaxZ()) {
                        return start;
                    }
                }
            }
        }
        return null;
    }

    public static @Nullable Registry<Structure> getStructureRegistry(ServerWorld world) {
        return world.getRegistryManager().getOptional(RegistryKeys.STRUCTURE).orElse(null);
    }

    @Override
    public NbtCompound writeNbt(NbtCompound nbt) {
        nbt = super.writeNbt(nbt);

        nbt.putBoolean("isSpectrumMap", true);

        nbt.putDouble("displayX", displayedCenter.getX());
        nbt.putDouble("displayZ", displayedCenter.getZ());

        if (this.targetId != null) {
            nbt.putString("targetId", targetId.toString());
        }

        return nbt;
    }

    @Override
    public MapState zoomOut(int zoomOutScale) {
        return of(this.centerX, this.centerZ, (byte) MathHelper.clamp(this.scale + zoomOutScale, 0, 4), accessor.getShowIcons(), accessor.getUnlimitedTracking(), this.dimension);
    }

    @Override
    public void update(PlayerEntity player, ItemStack stack) {
        BlockPos oldBlockPos = getDisplayedBlockPos();
        BlockPos newBlockPos = player.getBlockPos();
        if (oldBlockPos.getX() != newBlockPos.getX() || oldBlockPos.getZ() != newBlockPos.getZ()) {
            this.displayNeedsUpdate = true;
            this.displayedCenter = player.getPos();
            accessor.getIcons().clear();
        }
        super.update(player, stack);
    }

    @Override
    public void addIcon(MapIcon.Type type, @Nullable WorldAccess world, String key, double x, double z, double rotation, @Nullable Text text) {
        int scale = 1 << this.scale;

        float scaledX = (float)(x - this.displayedCenter.getX()) / scale;
        float scaledZ = (float)(z - this.displayedCenter.getZ()) / scale;

        byte pixelX = (byte)(scaledX * 2.0F + 0.5F);
        byte pixelZ = (byte)(scaledZ * 2.0F + 0.5F);
        byte rotationByte = 0;

        if (scaledX >= -63.0F && scaledZ >= -63.0F && scaledX <= 63.0F && scaledZ <= 63.0F) {
            rotation += rotation < 0.0 ? -8.0 : 8.0;
            rotationByte = (byte)(rotation * 16.0 / 360.0);
            if (this.dimension == World.NETHER && world != null) {
                int light = (int)(world.getLevelProperties().getTimeOfDay() / 10L);
                rotationByte = (byte)(light * light * 34187121 + light * 121 >> 15 & 15);
            }
        } else {
            if (type == MapIcon.Type.PLAYER && Math.abs(scaledX) < 320.0F && Math.abs(scaledZ) < 320.0F) {
                type = MapIcon.Type.PLAYER_OFF_MAP;
            }

            if (scaledX <= -63.0F) {
                pixelX = -128;
            }

            if (scaledZ <= -63.0F) {
                pixelZ = -128;
            }

            if (scaledX >= 63.0F) {
                pixelX = 127;
            }

            if (scaledZ >= 63.0F) {
                pixelZ = 127;
            }
        }

        MapIcon icon = new MapIcon(type, pixelX, pixelZ, rotationByte, text);
        MapIcon previousIcon = accessor.getIcons().put(key, icon);
        if (!icon.equals(previousIcon)) {
            if (previousIcon != null && previousIcon.getType().shouldUseIconCountLimit()) {
                accessor.setIconCount(accessor.getIconCount() - 1);
            }

            if (type.shouldUseIconCountLimit()) {
                accessor.setIconCount(accessor.getIconCount() + 1);
            }

            accessor.invokeMarkIconsDirty();
        }
    }

    @Override
    public boolean addBanner(WorldAccess world, BlockPos pos) {
        double x = pos.getX() + 0.5;
        double z = pos.getZ() + 0.5;

        int scale = 1 << this.scale;
        double scaledX = (x - this.displayedCenter.getX()) / scale;
        double scaledZ = (z - this.displayedCenter.getZ()) / scale;

        if (scaledX >= -63.0 && scaledZ >= -63.0 && scaledX <= 63.0 && scaledZ <= 63.0) {
            MapBannerMarker marker = MapBannerMarker.fromWorldBlock(world, pos);
            if (marker == null) {
                return false;
            }

            String key = marker.getKey();

            if (accessor.getBanners().remove(key, marker)) {
                accessor.invokeRemoveIcon(marker.getKey());
                return true;
            }

            if (!this.iconCountNotLessThan(256)) {
                accessor.getBanners().put(key, marker);
                this.addIcon(marker.getIconType(), world, key, x, z, 180.0, marker.getName());
                return true;
            }
        }

        return false;
    }

    public static void removeDecorationsNbt(ItemStack stack, String id) {
        NbtCompound nbt = stack.getNbt();
        if (nbt != null && nbt.contains("Decorations", NbtElement.LIST_TYPE)) {
            NbtList decorations = nbt.getList("Decorations", NbtElement.COMPOUND_TYPE);
            for (int i = 0; i < decorations.size(); i++) {
                NbtCompound decoration = decorations.getCompound(i);
                if (decoration.contains("id", NbtElement.STRING_TYPE)) {
                    String decorationId = decoration.getString("id");
                    if (decorationId.equals(id)) {
                        decorations.remove(i);
                        break;
                    }
                }
            }
        }
    }

    public Vec3d getDisplayedCenter() {
        return this.displayedCenter;
    }

    public BlockPos getDisplayedBlockPos() {
        return BlockPos.ofFloored(this.displayedCenter.getX(), this.displayedCenter.getY(), this.displayedCenter.getZ());
    }

    public @Nullable StructureStart getTarget() {
        return this.target;
    }

    public void setTarget(@Nullable StructureStart target) {
        this.target = target;
    }

    public @Nullable Identifier getTargetId() {
        return this.targetId;
    }

    public void setTargetId(@Nullable Identifier targetId) {
        if (this.targetId != targetId) {
            this.targetId = targetId;
            this.markDirty();
        }
    }

    public boolean displayNeedsUpdate() {
        return this.displayNeedsUpdate;
    }

    public void markDisplayUpdated() {
        this.displayNeedsUpdate = false;
    }

}
