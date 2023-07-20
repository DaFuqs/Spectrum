/*package de.dafuqs.spectrum.items.magic_items.ampoules;

import de.dafuqs.spectrum.SpectrumCommon;
import de.dafuqs.spectrum.entity.entity.LightShardEntity;
import de.dafuqs.spectrum.helpers.VectorCast;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

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
                entityCollisionResult.collision().damage(DamageSource.MAGIC, 5);
            });
        }
        
        return user.isCreative() ? super.use(world, user, hand) : TypedActionResult.consume(stack);
    }

}*/
