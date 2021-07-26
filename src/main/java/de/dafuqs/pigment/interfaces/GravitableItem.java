package de.dafuqs.pigment.interfaces;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public interface GravitableItem {

    public float getGravityModInInventory();
    public double getGravityModForItemEntity();

    public default void applyGravityEffect(ItemStack stack, World world, Entity entity) {
        if(world != null && entity != null) {
            // don't affect creative/spectator/... players or immune boss mobs
            if(entity.isPushable() && !(entity).isSpectator()) {
                if (entity instanceof PlayerEntity && ((PlayerEntity) entity).isCreative()){
                    return;
                }

                double additionalYVelocity = Math.log(stack.getCount()) * getGravityModInInventory();
                entity.addVelocity(0, additionalYVelocity, 0);

                // if falling very slowly => no fall damage
                if (additionalYVelocity > 0 && entity.getVelocity().y > -0.4) {
                    entity.fallDistance = 0;
                }
            }
        }
    }

}
