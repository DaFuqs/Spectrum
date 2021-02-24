package de.dafuqs.pigment.items;

import de.dafuqs.pigment.PigmentCommon;
import jdk.jfr.internal.LogLevel;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.jmx.Server;

public class EnderOreItem extends Item {

    public EnderOreItem(Item.Settings settings) {
        super(settings);
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        if(world != null && entity != null) {
            // don't affect creative/spectator/... players or immune boss mobs
            if(entity.isAttackable()) {
                // 49 items let the player hover basically
                // ... but with no way to get down :D
                double additionalYVelocity = Math.log(stack.getCount()) * 0.02;
                PigmentCommon.log(Level.INFO, +additionalYVelocity + " (total: " + entity.getVelocity().y + ")");
                entity.addVelocity(0, additionalYVelocity, 0);

                // if falling v ery slowly => no fall damage
                if (entity.getVelocity().y < 0 && entity.getVelocity().y > -0.4) {
                    entity.fallDistance = 0;
                    PigmentCommon.log(Level.INFO, "no fall damage");
                }
            }
        }
    }

}
