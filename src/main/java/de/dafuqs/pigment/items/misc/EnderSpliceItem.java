package de.dafuqs.pigment.items.misc;

import de.dafuqs.pigment.EnderSpliceChargingSoundInstance;
import de.dafuqs.pigment.PigmentClient;
import de.dafuqs.pigment.Support;
import de.dafuqs.pigment.registries.PigmentSoundEvents;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.loader.util.sat4j.core.Vec;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.client.sound.MovingMinecartSoundInstance;
import net.minecraft.client.sound.MovingSoundInstance;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.vehicle.AbstractMinecartEntity;
import net.minecraft.entity.vehicle.MinecartEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsage;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.packet.s2c.play.PlaySoundIdS2CPacket;
import net.minecraft.server.command.PlaySoundCommand;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class EnderSpliceItem extends Item {

    public EnderSpliceItem(Settings settings) {
        super(settings);
    }

    @Override
    public ItemStack finishUsing(ItemStack itemStack, World world, LivingEntity user) {

        PlayerEntity playerEntity = user instanceof PlayerEntity ? (PlayerEntity)user : null;
        if (playerEntity instanceof ServerPlayerEntity) {
            Criteria.CONSUME_ITEM.trigger((ServerPlayerEntity)playerEntity, itemStack);
        }

        CompoundTag compoundTag = itemStack.getTag();
        if (playerEntity != null && compoundTag != null && compoundTag.contains("PosX") && compoundTag.contains("PosY") && compoundTag.contains("PosZ") && compoundTag.contains("Dimension")) {
            String dimensionKeyString = compoundTag.getString("Dimension");
            if (user.getEntityWorld().getRegistryKey().getValue().toString().equals(dimensionKeyString)) {

                double x = compoundTag.getDouble("PosX");
                double y = compoundTag.getDouble("PosY");
                double z = compoundTag.getDouble("PosZ");
                if(!world.isClient) {
                    Vec3d pos = playerEntity.getPos();

                    world.playSound(playerEntity, pos.getX(), pos.getY(), pos.getZ(), PigmentSoundEvents.PLAYER_TELEPORTS, SoundCategory.PLAYERS, 1.0F, 1.0F);
                    user.requestTeleport(x, y + 0.25, z); // +0.25 makes it look way more lively
                    world.playSound(playerEntity, x, y, z, PigmentSoundEvents.PLAYER_TELEPORTS, SoundCategory.PLAYERS, 1.0F, 1.0F);

                    // make sure the sound plays even when the player currently teleports
                    if(playerEntity instanceof ServerPlayerEntity) {
                        ((ServerPlayerEntity) playerEntity).networkHandler.sendPacket(new PlaySoundIdS2CPacket(PigmentSoundEvents.PLAYER_TELEPORTS_ID, SoundCategory.PLAYERS, playerEntity.getPos(), 1.0F, 1.0F));
                        ((ServerPlayerEntity) playerEntity).networkHandler.sendPacket(new PlaySoundIdS2CPacket(SoundEvents.BLOCK_GLASS_BREAK.getId(), SoundCategory.PLAYERS, playerEntity.getPos(), 1.0F, 1.0F));
                    }
                }
            }

            playerEntity.incrementStat(Stats.USED.getOrCreateStat(this));
            if (!playerEntity.getAbilities().creativeMode) {
                itemStack.decrement(1);
            }

            return itemStack;
        } else {
            if(world.isClient) {
                if(playerEntity != null) {
                }
            } else {
                Vec3d pos = user.getPos();

                if (compoundTag == null) {
                    compoundTag = new CompoundTag();
                }

                compoundTag.putDouble("PosX", pos.getX());
                compoundTag.putDouble("PosY", pos.getY());
                compoundTag.putDouble("PosZ", pos.getZ());
                compoundTag.putString("Dimension", user.getEntityWorld().getRegistryKey().getValue().toString());
                itemStack.setTag(compoundTag);

                ((ServerPlayerEntity) playerEntity).networkHandler.sendPacket(new PlaySoundIdS2CPacket(PigmentSoundEvents.ENDER_SPLICE_BOUND.getId(), SoundCategory.PLAYERS, playerEntity.getPos(), 1.0F, 1.0F));
            }
        }

        return itemStack;
    }

    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        PigmentClient.minecraftClient.getSoundManager().play(new EnderSpliceChargingSoundInstance(user));
        return ItemUsage.consumeHeldItem(world, user, hand);
    }

    public int getMaxUseTime(ItemStack stack) {
        return 48;
    }

    public UseAction getUseAction(ItemStack stack) {
        return UseAction.BLOCK;
    }

    @Environment(EnvType.CLIENT)
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        CompoundTag compoundTag = stack.getTag();
        if(compoundTag != null && compoundTag.contains("PosX") && compoundTag.contains("PosY") && compoundTag.contains("PosZ") && compoundTag.contains("Dimension")) {
            String dimensionKeyString = compoundTag.getString("Dimension");
            String dimensionDisplayString = Support.getReadableDimensionString(dimensionKeyString);
            int x = (int) compoundTag.getDouble("PosX");
            int y = (int) compoundTag.getDouble("PosY");
            int z = (int) compoundTag.getDouble("PosZ");

            tooltip.add(new TranslatableText("item.pigment.ender_splice.tooltip.bound", x, y, z, dimensionDisplayString));
        } else {
            tooltip.add(new TranslatableText("item.pigment.ender_splice.tooltip.unbound"));
        }
    }


}
