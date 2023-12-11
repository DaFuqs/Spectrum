package de.dafuqs.spectrum.items.map;

import net.minecraft.item.map.MapState;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class StructureMapState extends MapState {

    private static final int MAP_SIZE = 128;
    private final boolean showIcons;
    private final boolean unlimitedTracking;

    private StructureMapState(int centerX, int centerZ, byte scale, boolean showIcons, boolean unlimitedTracking, boolean locked, RegistryKey<World> dimension) {
        super(centerX, centerZ, scale, showIcons, unlimitedTracking, locked, dimension);
        this.showIcons = showIcons;
        this.unlimitedTracking = unlimitedTracking;
    }

    public static MapState of(double centerX, double centerZ, byte scale, boolean showIcons, boolean unlimitedTracking, RegistryKey<World> dimension) {
        return new StructureMapState((int) centerX, (int) centerZ, scale, showIcons, unlimitedTracking, false, dimension);
    }

    @Override
    public MapState zoomOut(int zoomOutScale) {
        return of(this.centerX, this.centerZ, (byte) MathHelper.clamp(this.scale + zoomOutScale, 0, 4), this.showIcons, this.unlimitedTracking, this.dimension);
    }

    // update
    // addIcon
    // addBanner
}
