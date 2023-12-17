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
    private BlockPos displayedCenter;
    @Nullable
    private StructureStart target;
    private Identifier targetId;
    @Nullable
    private Vec3i displayDelta;
    @Nullable
    private StructureLocator locator;

    public StructureMapState(double centerX, double centerZ, byte scale, boolean showIcons, boolean unlimitedTracking, boolean locked, RegistryKey<World> dimension) {
        super((int) centerX, (int) centerZ, scale, showIcons, unlimitedTracking, locked, dimension);
        this.accessor = (MapStateAccessor) this;
        this.displayedCenter = new BlockPos((int) centerX, 0, (int) centerZ);
        this.displayDelta = null;
        this.locator = null;
    }

    public StructureMapState(double centerX, double centerZ, byte scale, boolean showIcons, boolean unlimitedTracking, boolean locked, RegistryKey<World> dimension, NbtCompound nbt) {
        this((int) centerX, (int) centerZ, scale, showIcons, unlimitedTracking, locked, dimension);

        // We'll use the colors from nbt
        this.displayDelta = Vec3i.ZERO;

        if (nbt.contains("targetId", NbtElement.STRING_TYPE)) {
            this.targetId = new Identifier(nbt.getString("targetId"));
        } else {
            this.targetId = null;
        }
        this.target = null;

        int xDisplay = nbt.contains("displayX", NbtElement.INT_TYPE) ? nbt.getInt("displayX") : this.displayedCenter.getX();
        int zDisplay = nbt.contains("displayZ", NbtElement.INT_TYPE) ? nbt.getInt("displayZ") : this.displayedCenter.getZ();
        this.displayedCenter = new BlockPos(xDisplay, 0, zDisplay);
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

        nbt.putInt("displayX", displayedCenter.getX());
        nbt.putInt("displayZ", displayedCenter.getZ());

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
        if (this.displayDelta != null) {
            if (this.locator == null && this.targetId != null && player.getWorld() instanceof ServerWorld world) {
                startLocator(world);
            }

            this.displayDelta = player.getBlockPos().subtract(this.displayedCenter);
        } else {
            this.displayedCenter = player.getBlockPos();
        }

        this.accessor.getIcons().clear();

        super.update(player, stack);

        addTargetIcon(player.getWorld());
    }

    @Override
    public void addIcon(MapIcon.Type type, @Nullable WorldAccess world, String key, double x, double z, double rotation, @Nullable Text text) {
        int scale = 1 << this.scale;

        float scaledX = (float)(x - this.displayedCenter.getX()) / scale;
        float scaledZ = (float)(z - this.displayedCenter.getZ()) / scale;

        byte pixelX = (byte)(scaledX * 2.0F + 0.5F);
        byte pixelZ = (byte)(scaledZ * 2.0F + 0.5F);

        rotation += rotation < 0.0 ? -8.0 : 8.0;
        byte rotationByte = (byte)(rotation * 16.0 / 360.0);
        if (this.dimension == World.NETHER && world != null) {
            int light = (int)(world.getLevelProperties().getTimeOfDay() / 10L);
            rotationByte = (byte)(light * light * 34187121 + light * 121 >> 15 & 15);
        }

        if (scaledX < -63.0F || scaledZ < -63.0F || scaledX > 63.0F || scaledZ > 63.0F) {
            double borderRotation;
            if (scaledZ >= 63.0F) {
                pixelZ = 127;
                if (scaledX <= -63.0F) {
                    pixelX = -128;
                    borderRotation = -135.0F;
                } else if (scaledX >= 63.0F) {
                    pixelX = 127;
                    borderRotation = 135.0F;
                } else {
                    borderRotation = 180.0F;
                }
            } else if (scaledZ <= -63.0F) {
                pixelZ = -128;
                if (scaledX <= -63.0F) {
                    pixelX = -128;
                    borderRotation = -45.0F;
                } else if (scaledX >= 63.0F) {
                    pixelX = 127;
                    borderRotation = 45.0F;
                } else {
                    borderRotation = 0;
                }
            } else if (scaledX <= -63.0F) {
                pixelX = -128;
                borderRotation = -90.0F;
            } else {
                pixelX = 127;
                borderRotation = 90.0F;
            }

            if (type == MapIcon.Type.PLAYER) {
                type = MapIcon.Type.PLAYER_OFF_MAP;
                rotationByte = 0;
            } else if (type == MapIcon.Type.TARGET_POINT) {
                borderRotation += borderRotation < 0.0 ? -8.0 : 8.0;
                rotationByte = (byte)(borderRotation * 16.0 / 360.0);
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

    private void addTargetIcon(WorldAccess world) {
        if (target != null) {
            addIcon(MapIcon.Type.TARGET_POINT, world, "target", target.getPos().getCenterX(), target.getPos().getCenterZ(), 180, null);
        }
    }

    public void startLocator(ServerWorld world) {
        if (targetId == null) return;
        this.locator = new StructureLocatorAsync(world, this::setTarget, this.targetId, new ChunkPos(this.displayedCenter), 3);
    }

    public BlockPos getDisplayedCenter() {
        return this.displayedCenter;
    }

    public @Nullable StructureStart getTarget() {
        return this.target;
    }

    public void setTarget(WorldAccess world, @Nullable StructureStart target) {
        this.target = target;

        accessor.invokeRemoveIcon("target");
        addTargetIcon(world);
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

    @Nullable
    public Vec3i getDisplayDelta() {
        return this.displayDelta;
    }

    public void clearDisplayDelta() {
        if (this.displayDelta != null) {
            int sampleSize = 1 << this.scale;

            Vec3i remainder = new Vec3i(this.displayDelta.getX() % sampleSize, 0, this.displayDelta.getZ() % sampleSize);
            Vec3i delta = this.displayDelta.subtract(remainder);
            BlockPos newDisplayedCenter = this.displayedCenter.add(delta);

            if (this.locator != null) {
                ChunkSectionPos startChunk = ChunkSectionPos.from(this.displayedCenter);
                ChunkSectionPos endChunk = ChunkSectionPos.from(newDisplayedCenter);
                this.locator.move(endChunk.getX() - startChunk.getX(), endChunk.getZ() - startChunk.getZ());
            }

            this.displayDelta = remainder;
            this.displayedCenter = newDisplayedCenter;
        } else {
            this.displayDelta = Vec3i.ZERO;
        }
    }

}
