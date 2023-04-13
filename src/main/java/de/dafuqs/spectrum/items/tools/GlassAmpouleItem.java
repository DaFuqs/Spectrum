package de.dafuqs.spectrum.items.tools;

import de.dafuqs.spectrum.entity.entity.LightShardEntity;
import de.dafuqs.spectrum.particle.SpectrumParticleTypes;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.Optional;

public class GlassAmpouleItem extends Item {

    public GlassAmpouleItem(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        var stack = user.getStackInHand(hand);
        summonBarrage(world, user, null);

        return user.isCreative() ? super.use(world, user, hand) : TypedActionResult.consume(stack);
    }

    @Override
    public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        summonBarrage(attacker.getWorld(), attacker, target);

        if (attacker instanceof PlayerEntity player && player.isCreative())
            return super.postHit(stack, target, attacker);

        stack.decrement(1);
        return super.postHit(stack, target, attacker);
    }

    public void summonBarrage(World world, LivingEntity user, @Nullable Entity target) {
        var random = user.getRandom();
        var projectiles = MathHelper.nextGaussian(random, 13, 5);
        var pos = user.getPos().add(0, user.getHeight() / 2, 0);

        user.playSound(SoundEvents.BLOCK_RESPAWN_ANCHOR_DEPLETE, 1, 1.2F + random.nextFloat() * 0.6F);

        for (int i = 0; i < projectiles; i++) {
            var shard = new LightShardEntity(world, user, Optional.ofNullable(target), 0.5F, 1);

            var velocityY = 0.0;

            if (user.isOnGround()) {
                velocityY = random.nextFloat() * 0.75;
            }
            else {
                velocityY = random.nextFloat() - 0.5;
            }

            shard.setPosition(pos);
            shard.setInitialVelocity(new Vec3d(
                    random.nextFloat() * 2 - 1,
                    velocityY,
                    random.nextFloat() * 2 - 1
            ));
            world.spawnEntity(shard);
        }

        for (int i = 0; i < projectiles * 3; i++) {

            var velocityY = 0.0;

            if (user.isOnGround()) {
                velocityY = random.nextFloat() * 0.75;
            }
            else {
                velocityY = random.nextFloat() - 0.5;
            }

            world.addParticle(SpectrumParticleTypes.SHOOTING_STAR, pos.x, pos.y, pos.z,
                    random.nextFloat() * 0.8F - 0.4F,
                    velocityY * 2,
                    random.nextFloat() * 0.8F - 0.4F
            );
        }
    }
}
