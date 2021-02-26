package de.dafuqs.pigment.gravity;

import de.dafuqs.pigment.interfaces.GravitableItem;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class GravityBlockItem extends BlockItem implements GravitableItem {

    private final float gravityMod;

    public GravityBlockItem(Block block, Settings settings, float gravityMod) {
        super(block, settings);
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
