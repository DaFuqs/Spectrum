package de.dafuqs.spectrum.items.magic_items;

import de.dafuqs.spectrum.api.item.*;
import de.dafuqs.spectrum.registries.*;
import de.dafuqs.spectrum.sound.*;
import net.fabricmc.api.*;
import net.minecraft.client.*;
import net.minecraft.client.item.*;
import net.minecraft.client.world.*;
import net.minecraft.entity.*;
import net.minecraft.entity.damage.*;
import net.minecraft.entity.player.*;
import net.minecraft.item.*;
import net.minecraft.nbt.*;
import net.minecraft.registry.tag.*;
import net.minecraft.server.world.*;
import net.minecraft.text.*;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import net.minecraft.world.*;
import net.minecraft.world.explosion.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class PipeBombItem extends Item implements DamageAwareItem, TickAwareItem {

    public PipeBombItem(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        if (world.isClient) {
            startSoundInstance(user);
        }
        return ItemUsage.consumeHeldItem(world, user, hand);
    }

    @Environment(EnvType.CLIENT)
    public void startSoundInstance(PlayerEntity user) {
        MinecraftClient.getInstance().getSoundManager().play(new PipeBombChargingSoundInstance(user));
    }

    @Override
    public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
        var nbt = stack.getOrCreateNbt();

        nbt.putBoolean("armed", true);
        nbt.putLong("timestamp", world.getTime());
        nbt.putUuid("owner", user.getUuid());
        user.playSound(SpectrumSoundEvents.INCANDESCENT_ARM, 2F, 0.9F);
        return stack;
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        if (world.isClient())
            return;

        var nbt = stack.getOrCreateNbt();
        if (!nbt.contains("armed"))
            return;

        if (tryGetOwner(stack, (ServerWorld) world) == entity && world.getTime() - nbt.getLong("timestamp") < 100)
            return;

        explode(stack, (ServerWorld) world, entity.getPos(), Optional.of(entity));
    }

    @Override
    public void onItemEntityTicked(ItemEntity itemEntity) {
        var world = itemEntity.getWorld();
        var stack = itemEntity.getStack();
        var nbt = stack.getOrCreateNbt();

        if (world.isClient() || !nbt.contains("armed"))
            return;

        if (world.getTime() - nbt.getLong("timestamp") > 100)
            explode(stack, (ServerWorld) world, itemEntity.getEyePos(), Optional.empty());
    }

    @Override
    public void onItemEntityDamaged(DamageSource source, float amount, ItemEntity itemEntity) {
        if ((source.isIn(DamageTypeTags.IS_FIRE) || source.isIn(DamageTypeTags.IS_EXPLOSION)) && !itemEntity.getWorld().isClient()) {
            explode(itemEntity.getStack(), (ServerWorld) itemEntity.getWorld(), itemEntity.getPos(), Optional.empty());
        }
    }

    private void explode(ItemStack stack, ServerWorld world, Vec3d pos, Optional<Entity> target) {
        stack.decrement(1);
		Entity owner = tryGetOwner(stack, world);

        target.ifPresent(entity -> entity.damage(SpectrumDamageTypes.incandescence(world, owner instanceof LivingEntity living ? living : null), 200F));
        world.createExplosion(null, SpectrumDamageTypes.incandescence(world), new ExplosionBehavior(), pos.getX(), pos.getY(), pos.getZ(), 7.5F, true, World.ExplosionSourceType.NONE);
    }

    public Entity tryGetOwner(ItemStack stack, ServerWorld world) {
        NbtCompound nbt = stack.getNbt();

        if(nbt == null || !nbt.contains("owner")) {
            return null;
        }

        return world.getEntity(nbt.getUuid("owner"));
    }

    @Override
    public int getMaxUseTime(ItemStack stack) {
        return 55;
    }
    
    public static float isArmed(ItemStack stack, @Nullable ClientWorld world, @Nullable LivingEntity entity, int seed) {
        var nbt = stack.getOrCreateNbt();
        if (!nbt.contains("armed"))
            return 0F;

        return nbt.getBoolean("armed") ? 1F : 0F;
    }

    @Override
    public UseAction getUseAction(ItemStack stack) {
        return UseAction.BOW;
    }
@Override
	public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        super.appendTooltip(stack, world, tooltip, context);
        tooltip.add(Text.translatable("item.spectrum.pipe_bomb.tooltip").formatted(Formatting.GRAY));
        tooltip.add(Text.translatable("item.spectrum.pipe_bomb.tooltip2").formatted(Formatting.GRAY));
    }

}
