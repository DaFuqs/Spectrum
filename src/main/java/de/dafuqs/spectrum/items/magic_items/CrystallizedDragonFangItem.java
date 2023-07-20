package de.dafuqs.spectrum.items.magic_items;

import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.helpers.*;
import net.minecraft.entity.player.*;
import net.minecraft.item.*;
import net.minecraft.server.world.*;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import net.minecraft.world.*;

public class CrystallizedDragonFangItem extends Item {

    public CrystallizedDragonFangItem(Settings settings) {
        super(settings);
    }
    
    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        var stack = user.getStackInHand(hand);
        if (!world.isClient) {

            var eyes = user.getEyePos();
            var yaw = Math.toRadians(user.getYaw());
            var pitch = Math.toRadians(user.getPitch());
            var magnitude = 8.0;

            var target = new Vec3d(
                    magnitude * Math.sin(-yaw) * Math.cos(-pitch) + eyes.x,
                    magnitude * Math.sin(-yaw) * Math.sin(-pitch) + eyes.y,
                    magnitude * Math.cos(-yaw) + eyes.z
            );

            var laser = new VectorCast(user.getEyePos(), target, 0);

            var collisions = laser.castForEntities((ServerWorld) world, entity -> true, user);

            SpectrumCommon.logError("!!!!!!!!!");
            SpectrumCommon.logError("Empty? " + collisions.isEmpty());
            SpectrumCommon.logError("Size " + collisions.size());
            SpectrumCommon.logError("!!!!!!!!!");

            collisions.forEach(entityCollisionResult -> {
                entityCollisionResult.collision().damage(world.getDamageSources().magic(), 5);
            });
        }
        
        return user.isCreative() ? super.use(world, user, hand) : TypedActionResult.consume(stack);
    }

}
