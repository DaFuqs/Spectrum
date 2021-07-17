package de.dafuqs.pigment.items.misc;

import de.dafuqs.pigment.PigmentClient;
import de.dafuqs.pigment.sound.NaturesStaffUseSoundInstance;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class AnimatedWandItem extends Item {

    public AnimatedWandItem(Settings settings) {
        super(settings);
    }

    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        if(world.isClient) {
            PigmentClient.minecraftClient.getSoundManager().play(new NaturesStaffUseSoundInstance(user));
        }
        return ItemUsage.consumeHeldItem(world, user, hand);
    }

    public UseAction getUseAction(ItemStack stack) {
        return UseAction.BLOCK;
    }

    public void usageTick(World world, LivingEntity user, ItemStack stack, int remainingUseTicks) {
        // trigger the items usage action every 10 ticks
        if(remainingUseTicks % 10 == 0) {
            if(MinecraftClient.getInstance().crosshairTarget.getType() == net.minecraft.util.hit.HitResult.Type.BLOCK) {
                MinecraftClient.getInstance().interactionManager.interactBlock(
                        MinecraftClient.getInstance().player,
                        MinecraftClient.getInstance().world,
                        MinecraftClient.getInstance().player.getActiveHand(),
                        (BlockHitResult) MinecraftClient.getInstance().crosshairTarget
                );
            }
        }
    }

    public ActionResult useOnBlock(ItemUsageContext context) {
        PlayerEntity user = context.getPlayer();

        if(user != null && user.getItemUseTime() > 2) {
            World world = context.getWorld();
            BlockPos blockPos = context.getBlockPos();

            if (world.isClient) {
                float randomX = world.getRandom().nextFloat() * 0.4F - 0.2F;
                float randomZ = world.getRandom().nextFloat() * 0.4F - 0.2F;
                BlockPos particleBlockPos = blockPos.offset(context.getSide());
                world.addParticle(ParticleTypes.HAPPY_VILLAGER, particleBlockPos.getX(), particleBlockPos.getY(), particleBlockPos.getZ(), randomX, 0, randomZ);
            }

            BlockState blockState = world.getBlockState(blockPos);

            // fertilizable? => grow
            if (BoneMealItem.useOnFertilizable(context.getStack(), world, blockPos)) {
                world.syncWorldEvent(2005, blockPos, 0);
                return ActionResult.success(false);
                // random tickable? => tick
            } else if (blockState.hasRandomTicks()) {
                if (world instanceof ServerWorld) {
                    blockState.randomTick((ServerWorld) world, blockPos, world.random);
                }
                world.syncWorldEvent(2005, blockPos, 0);

                return ActionResult.success(false);
            } else {
                BlockPos blockPos2 = blockPos.offset(context.getSide());
                boolean bl = blockState.isSideSolidFullSquare(world, blockPos, context.getSide());
                if (bl && BoneMealItem.useOnGround(context.getStack(), world, blockPos2, context.getSide())) {
                    world.syncWorldEvent(2005, blockPos2, 0);
                    return ActionResult.success(false);
                }
            }
        }

        return ActionResult.PASS;
    }

}
