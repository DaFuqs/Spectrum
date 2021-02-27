package de.dafuqs.pigment.blocks.gravity;

import de.dafuqs.pigment.interfaces.GravitableItem;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class GravityItem extends Item implements GravitableItem {

    private final float gravityMod;

    public GravityItem(Settings settings, float gravityMod) {
        super(settings);
        this.gravityMod = gravityMod;
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        applyGravityEffect(stack, world, entity);
    }

    @Override
    public float getGravityMod() {
        return gravityMod;
    }

}
