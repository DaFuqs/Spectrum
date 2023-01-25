package de.dafuqs.spectrum.items.tools;

import de.dafuqs.spectrum.helpers.*;
import de.dafuqs.spectrum.networking.*;
import de.dafuqs.spectrum.registries.*;
import de.dafuqs.spectrum.sound.*;
import net.fabricmc.api.*;
import net.minecraft.client.*;
import net.minecraft.client.item.*;
import net.minecraft.entity.*;
import net.minecraft.entity.player.*;
import net.minecraft.item.*;
import net.minecraft.nbt.*;
import net.minecraft.server.network.*;
import net.minecraft.sound.*;
import net.minecraft.text.*;
import net.minecraft.util.*;
import net.minecraft.world.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class GlassCrestCrossbowItem extends MalachiteCrossbowItem {

    private static final int OVERCHARGE_DURATION_MAX_TICKS = 20 * 6; // 6 seconds

    public GlassCrestCrossbowItem(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);
        if (user.isSneaking() && isCharged(itemStack) && !isOvercharged(itemStack)) {
            if (world.isClient) {
                startSoundInstance(user);
            }
            return ItemUsage.consumeHeldItem(world, user, hand);
        }
        return super.use(world, user, hand);
    }

    @Environment(EnvType.CLIENT)
    public void startSoundInstance(PlayerEntity user) {
        MinecraftClient.getInstance().getSoundManager().play(new OverchargingSoundInstance(user));
    }

    @Override
    public int getMaxUseTime(ItemStack stack) {
        return isCharged(stack) ? OVERCHARGE_DURATION_MAX_TICKS : super.getMaxUseTime(stack);
    }

    @Override
    public void usageTick(World world, LivingEntity user, ItemStack stack, int remainingUseTicks) {
        if (isCharged(stack) && remainingUseTicks <= 0) {
            if (remainingUseTicks % 4 == 0) {
                world.playSoundFromEntity(null, user, SpectrumSoundEvents.BLOCK_MOONSTONE_CLUSTER_BREAK, SoundCategory.PLAYERS, 1.0F, 1.0F);
            }
        } else {
            super.usageTick(world, user, stack, remainingUseTicks);
        }
    }

    @Override
    public void onStoppedUsing(ItemStack stack, World world, LivingEntity user, int remainingUseTicks) {
        if (isCharged(stack)) {
            if (!world.isClient) {
                if (remainingUseTicks > 0) {
                    float overcharge = 1 - (float) remainingUseTicks / OVERCHARGE_DURATION_MAX_TICKS;
                    overcharge(stack, overcharge);
                    if (user instanceof ServerPlayerEntity serverPlayerEntity) {
                        SpectrumS2CPacketSender.sendHudMessage(serverPlayerEntity,
                                Text.translatable("item.spectrum.glass_crest_crossbow.message.charge", Support.df2.format(overcharge * 100)), false);
                    }
                }
            }
            return;
        }
        super.onStoppedUsing(stack, world, user, remainingUseTicks);
    }

    public static boolean isOvercharged(ItemStack stack) {
        return getOvercharge(stack) > 0;
    }

    public static float getOvercharge(ItemStack stack) {
        NbtCompound compound = stack.getNbt();
        if (compound == null) {
            return 0;
        }
        return compound.getFloat("Overcharged");
    }

    public static void overcharge(ItemStack stack, float percent) {
        NbtCompound compound = stack.getOrCreateNbt();
        compound.putFloat("Overcharged", percent);
    }

    public static void unOvercharge(ItemStack stack) {
        NbtCompound compound = stack.getNbt();
        if (compound != null) {
            compound.remove("Overcharged");
        }
    }

    @Override
    @Environment(EnvType.CLIENT)
    public void appendTooltip(ItemStack itemStack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        super.appendTooltip(itemStack, world, tooltip, context);
        float overcharge = getOvercharge(itemStack);
        if (overcharge == 0) {
            tooltip.add(Text.translatable("item.spectrum.glass_crest_crossbow.tooltip.how_to_overcharge"));
        } else {
            tooltip.add(Text.translatable("item.spectrum.glass_crest_crossbow.tooltip.overcharged", Support.df2.format(overcharge * 100)));
        }
    }

    @Override
    public float getProjectileVelocityModifier(ItemStack stack) {
        float parent = super.getProjectileVelocityModifier(stack);
        float overcharge = getOvercharge(stack);
        return overcharge == 0 ? parent : parent * (1 + overcharge * 0.5F);
    }

    @Override
    public float getDivergenceMod(ItemStack stack) {
        float parent = super.getDivergenceMod(stack);
        float overcharge = getOvercharge(stack);
        return overcharge == 0 ? parent : parent * (1 - overcharge * 0.5F);
    }

}
