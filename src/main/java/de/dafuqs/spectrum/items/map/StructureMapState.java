package de.dafuqs.spectrum.items.map;

import de.dafuqs.spectrum.mixin.accessors.MapStateAccessor;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.map.MapBannerMarker;
import net.minecraft.item.map.MapIcon;
import net.minecraft.item.map.MapState;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.registry.RegistryKey;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import org.jetbrains.annotations.Nullable;

public class StructureMapState extends MapState {

    private final MapStateAccessor accessor;
    private Vec3d displayedCenter;

    public StructureMapState(double centerX, double centerZ, byte scale, boolean showIcons, boolean unlimitedTracking, boolean locked, RegistryKey<World> dimension) {
        super((int) centerX, (int) centerZ, scale, showIcons, unlimitedTracking, locked, dimension);
        this.accessor = (MapStateAccessor) this;
        this.displayedCenter = new Vec3d(centerX, 0, centerZ);
    }

    @Override
    public MapState zoomOut(int zoomOutScale) {
        return of(this.centerX, this.centerZ, (byte) MathHelper.clamp(this.scale + zoomOutScale, 0, 4), accessor.getShowIcons(), accessor.getUnlimitedTracking(), this.dimension);
    }

    @Override
    public void update(PlayerEntity player, ItemStack stack) {
        this.displayedCenter = player.getPos();
        accessor.getIcons().clear();
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
        } else if (type == MapIcon.Type.PLAYER) {
            if (Math.abs(scaledX) < 320.0F && Math.abs(scaledZ) < 320.0F) {
                type = MapIcon.Type.PLAYER_OFF_MAP;
            } else if (accessor.getUnlimitedTracking()) {
                type = MapIcon.Type.PLAYER_OFF_LIMITS;
            } else {
                return;
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

    public static void removeDecorationsNbt(ItemStack stack, BlockPos pos, String id) {
        NbtCompound nbt = stack.getNbt();
        if (nbt != null && nbt.contains("Decorations", NbtElement.LIST_TYPE)) {
            NbtList decorations = nbt.getList("Decorations", NbtElement.COMPOUND_TYPE);
            for (int i = 0; i < decorations.size(); i++) {
                NbtCompound decoration = decorations.getCompound(i);
                String decorationId = decoration.contains("id", NbtElement.STRING_TYPE) ? decoration.getString("id") : null;
                Double x = decoration.contains("x", NbtElement.DOUBLE_TYPE) ? decoration.getDouble("x") : null;
                Double z = decoration.contains("z", NbtElement.DOUBLE_TYPE) ? decoration.getDouble("z") : null;
                if ((decorationId == null || decorationId.equals(id)) && (x == null || x == pos.getX()) && (z == null || z == pos.getZ())) {
                    decorations.remove(i);
                    i--;
                }
            }
        }
    }

    public Vec3d getDisplayedCenter() {
        return this.displayedCenter;
    }

}
