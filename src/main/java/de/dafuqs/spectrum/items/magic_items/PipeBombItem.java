package de.dafuqs.spectrum.items.magic_items;

import de.dafuqs.spectrum.blocks.present.PresentBlockEntity;
import de.dafuqs.spectrum.items.DamageAwareItem;
import de.dafuqs.spectrum.items.UnpackingSurprise;
import de.dafuqs.spectrum.items.TickAwareItem;
import de.dafuqs.spectrum.registries.SpectrumDamageSources;
import de.dafuqs.spectrum.registries.SpectrumSoundEvents;
import de.dafuqs.spectrum.sound.PipeBombChargingSoundInstance;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsage;
import net.minecraft.registry.tag.DamageTypeTags;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import net.minecraft.world.explosion.ExplosionBehavior;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class PipeBombItem extends Item implements DamageAwareItem, TickAwareItem, UnpackingSurprise {

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
        var owner = tryGetOwner(stack, world);

        target.ifPresent(entity -> entity.damage(SpectrumDamageSources.incandescence(world, owner instanceof LivingEntity living ? living : null), 200F));
        world.createExplosion(null, SpectrumDamageSources.incandescence(world), new ExplosionBehavior(), pos.getX(), pos.getY(), pos.getZ(), 7.5F, true, World.ExplosionSourceType.NONE);
    }

    public Entity tryGetOwner(ItemStack stack, ServerWorld world) {
        var nbt = stack.getOrCreateNbt();
        if (!nbt.contains("owner"))
            return null;

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
    public void unpackSurprise(ItemStack stack, PresentBlockEntity presentBlockEntity, ServerWorld world, BlockPos pos, Random random) {
        var nbt = stack.getOrCreateNbt();

        nbt.putBoolean("armed", true);
        nbt.putLong("timestamp", world.getTime() - 70);
        nbt.putUuid("owner", presentBlockEntity.getOwnerUUID());
        world.playSound(null, pos, SpectrumSoundEvents.INCANDESCENT_ARM, SoundCategory.BLOCKS, 2F, 0.9F);
    }
}
