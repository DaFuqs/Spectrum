package de.dafuqs.spectrum.items;

import de.dafuqs.spectrum.graces.crystal.ColorPool;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class CrystalGraceItem extends ItemWithTooltip{

    public final ColorPool pool;

    public CrystalGraceItem(ColorPool pool, Settings settings, String tooltip) {
        super(settings, tooltip);
        this.pool = pool;
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        super.appendTooltip(stack, world, tooltip, context);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        return super.use(world, user, hand);
    }

    public static float tryGetAberration(ItemStack stack) {
        var root = stack.getOrCreateNbt();
        if (!root.contains("aberration"))
            createTags(stack);

        return root.getFloat("aberration");
    }

    public static float tryGetIrradiance(ItemStack stack) {
        var root = stack.getOrCreateNbt();
        if (!root.contains("irradiance"))
            createTags(stack);

        return root.getFloat("irradiance");
    }

    public static float tryGetAbsorption(ItemStack stack) {
        var root = stack.getOrCreateNbt();
        if (!root.contains("absorption"))
            createTags(stack);

        return root.getFloat("absorption");
    }

    public static void createTags(ItemStack stack) {
        var root = stack.getOrCreateNbt();
        root.putFloat("aberration", 0);
        root.putFloat("irradiance", 0);
        root.putFloat("absorption", 0);
        root.putBoolean("initialized", false);
    }

    public static void initialize(ItemStack stack) {
        var root = stack.getOrCreateNbt();
        if (!root.contains("initialized"))
            createTags(stack);


    }
}
