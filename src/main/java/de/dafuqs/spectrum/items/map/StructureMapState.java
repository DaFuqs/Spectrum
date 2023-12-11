package de.dafuqs.spectrum.items.map;

import net.minecraft.item.ItemStack;
import net.minecraft.item.map.MapIcon;
import net.minecraft.item.map.MapState;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class StructureMapState extends MapState {

    private final boolean showIcons;
    private final boolean unlimitedTracking;

    public StructureMapState(int centerX, int centerZ, byte scale, boolean showIcons, boolean unlimitedTracking, boolean locked, RegistryKey<World> dimension) {
        super(centerX, centerZ, scale, showIcons, unlimitedTracking, locked, dimension);
        this.showIcons = showIcons;
        this.unlimitedTracking = unlimitedTracking;
    }

    @Override
    public MapState zoomOut(int zoomOutScale) {
        return of(this.centerX, this.centerZ, (byte) MathHelper.clamp(this.scale + zoomOutScale, 0, 4), this.showIcons, this.unlimitedTracking, this.dimension);
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

}
