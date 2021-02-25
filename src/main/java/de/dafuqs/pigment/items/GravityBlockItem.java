package de.dafuqs.pigment.items;

import de.dafuqs.pigment.PigmentCommon;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.apache.logging.log4j.Level;

public class GravityBlockItem extends BlockItem {

    private final float gravityMod;

    public GravityBlockItem(Block block, Settings settings, float gravityMod) {
        super(block, settings);
        this.gravityMod = gravityMod;
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        if(world != null && entity != null) {
            // don't affect creative/spectator/... players or immune boss mobs
            if(entity.isAttackable()) {
                double additionalYVelocity = Math.log(stack.getCount()) * gravityMod;
                PigmentCommon.log(Level.INFO, additionalYVelocity + " (total: " + entity.getVelocity().y + ")");
                entity.addVelocity(0, additionalYVelocity, 0);

                // if falling very slowly => no fall damage
                if (additionalYVelocity < 0 && entity.getVelocity().y < 0 && entity.getVelocity().y > -0.4) {
                    entity.fallDistance = 0;
                    PigmentCommon.log(Level.INFO, "no fall damage");
                }
            }
        }
    }

}
