package de.dafuqs.spectrum.items.map;

import com.mojang.datafixers.util.Pair;
import de.dafuqs.spectrum.mixin.accessors.*;
import net.minecraft.entity.player.*;
import net.minecraft.item.*;
import net.minecraft.item.map.*;
import net.minecraft.nbt.*;
import net.minecraft.registry.*;
import net.minecraft.server.world.*;
import net.minecraft.structure.*;
import net.minecraft.text.*;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import net.minecraft.world.*;
import net.minecraft.world.gen.structure.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class ArtisansAtlasState extends MapState {

    private final MapStateAccessor accessor;
    private final Set<StructureStart> targets;
    private BlockPos displayedCenter;
    private Identifier targetId;
    @Nullable
    private Vec3i displayDelta;
    @Nullable
    private StructureLocatorAsync locator;

    public ArtisansAtlasState(byte scale, boolean locked, RegistryKey<World> dimension) {
        this(0, 0, scale, false, false, locked, dimension);
    }

    public ArtisansAtlasState(double centerX, double centerZ, byte scale, boolean showIcons, boolean unlimitedTracking, boolean locked, RegistryKey<World> dimension) {
        super((int) centerX, (int) centerZ, scale, showIcons, unlimitedTracking, locked, dimension);
        this.accessor = (MapStateAccessor) this;
        this.targets = new HashSet<>();
        this.displayedCenter = new BlockPos((int) centerX, 0, (int) centerZ);
        this.displayDelta = null;
        this.locator = null;
    }

    public ArtisansAtlasState(double centerX, double centerZ, byte scale, boolean showIcons, boolean unlimitedTracking, boolean locked, RegistryKey<World> dimension, NbtCompound nbt) {
        this((int) centerX, (int) centerZ, scale, showIcons, unlimitedTracking, locked, dimension);
	
		// We'll use the colors from nbt
		this.displayDelta = Vec3i.ZERO;
	
		if (nbt.contains("targetId", NbtElement.STRING_TYPE)) {
			this.targetId = new Identifier(nbt.getString("targetId"));
		} else {
			this.targetId = null;
		}
	
		int xDisplay = nbt.contains("displayX", NbtElement.NUMBER_TYPE) ? nbt.getInt("displayX") : this.displayedCenter.getX();
		int zDisplay = nbt.contains("displayZ", NbtElement.NUMBER_TYPE) ? nbt.getInt("displayZ") : this.displayedCenter.getZ();
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

    @Override
    public NbtCompound writeNbt(NbtCompound nbt) {
        nbt = super.writeNbt(nbt);

        nbt.putBoolean("isArtisansAtlas", true);

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

        for (StructureStart target : this.targets) {
            addTargetIcon(player.getWorld(), target);
        }
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

    private void addTargetIcon(WorldAccess world, StructureStart target) {
        if (target != null) {
            addIcon(MapIcon.Type.TARGET_POINT, world, getTargetKey(target), target.getPos().getCenterX(), target.getPos().getCenterZ(), 180, null);
        }
    }

    private String getTargetKey(StructureStart start) {
        return String.format("target-%d-%d", start.getPos().x, start.getPos().z);
    }

    public void startLocator(ServerWorld world) {
        if (targetId == null) return;
        this.locator = new StructureLocatorAsync(world, this::addTarget, this.targetId, new ChunkPos(this.displayedCenter), 32);
    }

    public void cancelLocator() {
        if (this.locator != null) {
            this.locator.cancel();
        }
    }

    public BlockPos getDisplayedCenter() {
        return this.displayedCenter;
    }

    public void addTarget(WorldAccess world, StructureStart target) {
        this.targets.add(target);
        addTargetIcon(world, target);
    }
    
    public void setTargetId(@Nullable Identifier targetId) {
        if (this.targetId != targetId) {
            this.targetId = targetId;
            this.markDirty();
        }
    }
    
    public @Nullable Identifier getTargetId() {
        return this.targetId;
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

    public void updateDimension(RegistryKey<World> dimension) {
        if (!this.dimension.equals(dimension)) {
            this.dimension = dimension;
            this.displayDelta = null;
            this.targets.clear();
            this.targetId = null;
            this.markDirty();
        }
    }

}
