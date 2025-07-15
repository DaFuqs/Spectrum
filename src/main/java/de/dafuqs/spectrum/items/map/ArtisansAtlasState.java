package de.dafuqs.spectrum.items.map;

import com.mojang.datafixers.util.*;
import de.dafuqs.spectrum.helpers.*;
import de.dafuqs.spectrum.mixin.accessors.*;
import net.minecraft.core.*;
import net.minecraft.core.registries.*;
import net.minecraft.nbt.*;
import net.minecraft.network.chat.*;
import net.minecraft.resources.*;
import net.minecraft.server.level.*;
import net.minecraft.util.*;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.item.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.levelgen.structure.*;
import net.minecraft.world.level.saveddata.maps.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class ArtisansAtlasState extends MapItemSavedData {
	
	private final MapStateAccessor accessor;
	private Set<BlockPos> targets;
	private BlockPos displayedCenter;
	private ResourceLocation targetId;
	@Nullable
	private Vec3i displayDelta;
	@Nullable
	private StructureLocatorAsync locator;
	
	public ArtisansAtlasState(byte scale, boolean locked, ResourceKey<Level> dimension) {
		this(0, 0, scale, false, false, locked, dimension);
	}
	
	public ArtisansAtlasState(double centerX, double centerZ, byte scale, boolean showIcons, boolean unlimitedTracking, boolean locked, ResourceKey<Level> dimension) {
		super((int) centerX, (int) centerZ, scale, showIcons, unlimitedTracking, locked, dimension);
		this.accessor = (MapStateAccessor) this;
		this.targets = new HashSet<>();
		this.displayedCenter = new BlockPos((int) centerX, 0, (int) centerZ);
		this.displayDelta = null;
		this.locator = null;
	}
	
	public ArtisansAtlasState(double centerX, double centerZ, byte scale, boolean showIcons, boolean unlimitedTracking, boolean locked, ResourceKey<Level> dimension, CompoundTag nbt) {
		this((int) centerX, (int) centerZ, scale, showIcons, unlimitedTracking, locked, dimension);
		
		// We'll use the colors from nbt
		this.displayDelta = Vec3i.ZERO;
		
		if (nbt.contains("targetId", Tag.TAG_STRING)) {
			this.targetId = ResourceLocation.parse(nbt.getString("targetId"));
		} else {
			this.targetId = null;
		}
		
		int xDisplay = nbt.contains("displayX", Tag.TAG_ANY_NUMERIC) ? nbt.getInt("displayX") : this.displayedCenter.getX();
		int zDisplay = nbt.contains("displayZ", Tag.TAG_ANY_NUMERIC) ? nbt.getInt("displayZ") : this.displayedCenter.getZ();
		this.displayedCenter = new BlockPos(xDisplay, 0, zDisplay);
		
		this.targets = new HashSet<>(CodecHelper.fromNbt(BlockPos.CODEC.listOf(), nbt.get("targets"), List.of()));
	}
	
	public static @Nullable Pair<ResourceLocation, StructureStart> locateAnyStructureAtBlock(ServerLevel world, BlockPos pos) {
		Registry<Structure> registry = world.registryAccess().registry(Registries.STRUCTURE).orElse(null);
		if (registry != null) {
			for (Structure structure : registry.stream().toList()) {
				ResourceLocation id = registry.getKey(structure);
				StructureStart start = world.structureManager().getStructureWithPieceAt(pos, structure);
				if (start != StructureStart.INVALID_START && id != null) {
					return new Pair<>(id, start);
				}
			}
		}
		return null;
	}
	
	@Override
	public CompoundTag save(CompoundTag nbt, HolderLookup.Provider lookup) {
		nbt = super.save(nbt, lookup);
		
		nbt.putBoolean("isArtisansAtlas", true);
		
		nbt.putInt("displayX", displayedCenter.getX());
		nbt.putInt("displayZ", displayedCenter.getZ());
		
		if (this.targetId != null)
			nbt.putString("targetId", targetId.toString());
		
		CodecHelper.writeNbt(nbt, "targets", BlockPos.CODEC.listOf(), targets.stream().toList());
		
		return nbt;
	}
	
	@Override
	public MapItemSavedData scaled() {
		return createFresh(this.centerX, this.centerZ, (byte) Mth.clamp(this.scale + 1, 0, 4), accessor.getTrackingPosition(), accessor.getUnlimitedTracking(), this.dimension);
	}
	
	@Override
	public void tickCarriedBy(Player player, ItemStack stack) {
		if (this.displayDelta != null) {
			if (this.locator == null && this.targetId != null && player.level() instanceof ServerLevel world)
				startLocator(world);
			
			this.displayDelta = player.blockPosition().subtract(this.displayedCenter);
		} else {
			this.displayedCenter = player.blockPosition();
		}
		
		this.accessor.getDecorations().clear();
		
		super.tickCarriedBy(player, stack);
		
		for (BlockPos target : this.targets)
			addTargetIcon(player.level(), target);
	}
	
	@Override
	@SuppressWarnings("deprecation")
	public void addDecoration(Holder<MapDecorationType> type, @Nullable LevelAccessor world, String key, double x, double z, double rotation, @Nullable Component text) {
		int scale = 1 << this.scale;
		
		float scaledX = (float) (x - this.displayedCenter.getX()) / scale;
		float scaledZ = (float) (z - this.displayedCenter.getZ()) / scale;
		
		float squaredDistance = scaledX * scaledX + scaledZ * scaledZ;
		byte scaleByte = (byte) Math.clamp(-16f * ((0.5f * Math.log(squaredDistance) / Math.log(2)) - 7), -100, 0);
		
		byte pixelX = (byte) (scaledX * 2.0F + 0.5F);
		byte pixelZ = (byte) (scaledZ * 2.0F + 0.5F);
		
		rotation += rotation < 0.0 ? -8.0 : 8.0;
		byte rotationByte = (byte) (rotation * 16.0 / 360.0);
		if (this.dimension == Level.NETHER && world != null) {
			int light = (int) (world.getLevelData().getDayTime() / 10L);
			rotationByte = (byte) (light * light * 34187121 + light * 121 >> 15 & 15);
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
			
			if (type.is(MapDecorationTypes.PLAYER)) {
				type = MapDecorationTypes.PLAYER_OFF_MAP;
				rotationByte = 0;
			} else if (type.is(MapDecorationTypes.TARGET_POINT)) {
				borderRotation += borderRotation < 0.0 ? -8.0 : 8.0;
				rotationByte = (byte) (borderRotation * 16.0 / 360.0);
			}
		}
		
		MapDecoration icon = new MapDecoration(type, pixelX, pixelZ, rotationByte, Optional.ofNullable(text));
		icon.spectrum$setScale(scaleByte);
		
		MapDecoration previousIcon = accessor.getDecorations().put(key, icon);
		if (!icon.equals(previousIcon)) {
			if (previousIcon != null && previousIcon.type().value().trackCount())
				accessor.setTrackedDecorationCount(accessor.getTrackedDecorationCount() - 1);
			
			if (type.value().trackCount())
				accessor.setTrackedDecorationCount(accessor.getTrackedDecorationCount() + 1);
			
			accessor.invokeSetDecorationsDirty();
		}
	}
	
	@Override
	public boolean toggleBanner(LevelAccessor world, BlockPos pos) {
		double x = pos.getX() + 0.5;
		double z = pos.getZ() + 0.5;
		
		int scale = 1 << this.scale;
		double scaledX = (x - this.displayedCenter.getX()) / scale;
		double scaledZ = (z - this.displayedCenter.getZ()) / scale;
		
		if (scaledX >= -63.0 && scaledZ >= -63.0 && scaledX <= 63.0 && scaledZ <= 63.0) {
			MapBanner marker = MapBanner.fromWorld(world, pos);
			if (marker == null) {
				return false;
			}
			
			String key = marker.getId();
			
			if (accessor.getBannerMarkers().remove(key, marker)) {
				accessor.invokeRemoveDecoration(marker.getId());
				return true;
			}
			
			if (!this.isTrackedCountOverLimit(256)) {
				accessor.getBannerMarkers().put(key, marker);
				this.addDecoration(marker.getDecoration(), world, key, x, z, 180.0, marker.name().orElse(null));
				return true;
			}
		}
		
		return false;
	}
	
	private void addTargetIcon(LevelAccessor world, BlockPos target) {
		if (target != null) {
			addDecoration(MapDecorationTypes.TARGET_POINT, world, getTargetKey(target), target.getX(), target.getZ(), 180, null);
		}
	}
	
	private String getTargetKey(BlockPos start) {
		return String.format("target-%d-%d-%d", start.getX(), start.getY(), start.getZ());
	}
	
	public void startLocator(ServerLevel world) {
		if (targetId == null) return;
		this.locator = new StructureLocatorAsync(world, this.targetId, 5 * 1000);
	}
	
	public void cancelLocator() {
		this.locator = null;
	}
	
	public BlockPos getDisplayedCenter() {
		return this.displayedCenter;
	}
	
	public void addTarget(LevelAccessor world, BlockPos pos) {
		this.targets.add(pos);
		addTargetIcon(world, pos);
	}
	
	public void setTargetId(@Nullable ResourceLocation targetId) {
		if (this.targetId != targetId) {
			this.targetId = targetId;
			this.setDirty();
		}
	}
	
	public @Nullable ResourceLocation getTargetId() {
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
			this.displayedCenter = this.displayedCenter.offset(delta);
			this.displayDelta = remainder;
			
			if (this.locator != null) {
				this.locator.ping(displayedCenter, this::addTarget);
			}
		} else {
			this.displayDelta = Vec3i.ZERO;
		}
	}
	
	public void updateDimension(ResourceKey<Level> dimension) {
		if (!this.dimension.equals(dimension)) {
			this.dimension = dimension;
			this.displayDelta = null;
			this.targets.clear();
			this.targetId = null;
			this.setDirty();
		}
	}
	
}
