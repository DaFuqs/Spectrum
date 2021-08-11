package de.dafuqs.spectrum.items.misc;

import de.dafuqs.spectrum.SpectrumClient;
import de.dafuqs.spectrum.Support;
import de.dafuqs.spectrum.sound.EnderSpliceChargingSoundInstance;
import de.dafuqs.spectrum.sound.SpectrumSoundEvents;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsage;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.packet.s2c.play.PlaySoundIdS2CPacket;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
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

        NbtCompound nbtCompound = itemStack.getTag();
        if (playerEntity != null && nbtCompound != null && nbtCompound.contains("PosX") && nbtCompound.contains("PosY") && nbtCompound.contains("PosZ") && nbtCompound.contains("Dimension")) {
            String dimensionKeyString = nbtCompound.getString("Dimension");
            if (user.getEntityWorld().getRegistryKey().getValue().toString().equals(dimensionKeyString)) {

                double x = nbtCompound.getDouble("PosX");
                double y = nbtCompound.getDouble("PosY");
                double z = nbtCompound.getDouble("PosZ");
                if(!world.isClient) {
                    Vec3d pos = playerEntity.getPos();

                    world.playSound(playerEntity, pos.getX(), pos.getY(), pos.getZ(), SpectrumSoundEvents.PLAYER_TELEPORTS, SoundCategory.PLAYERS, 1.0F, 1.0F);
                    user.requestTeleport(x, y + 0.25, z); // +0.25 makes it look way more lively
                    world.playSound(playerEntity, x, y, z, SpectrumSoundEvents.PLAYER_TELEPORTS, SoundCategory.PLAYERS, 1.0F, 1.0F);

                    // make sure the sound plays even when the player currently teleports
                    if(playerEntity instanceof ServerPlayerEntity) {
                        ((ServerPlayerEntity) playerEntity).networkHandler.sendPacket(new PlaySoundIdS2CPacket(SpectrumSoundEvents.PLAYER_TELEPORTS.getId(), SoundCategory.PLAYERS, playerEntity.getPos(), 1.0F, 1.0F));
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
            if(!world.isClient) {
                Vec3d pos = user.getPos();

                if (nbtCompound == null) {
                    nbtCompound = new NbtCompound();
                }

                nbtCompound.putDouble("PosX", pos.getX());
                nbtCompound.putDouble("PosY", pos.getY());
                nbtCompound.putDouble("PosZ", pos.getZ());
                nbtCompound.putString("Dimension", user.getEntityWorld().getRegistryKey().getValue().toString());
                itemStack.setTag(nbtCompound);

                ((ServerPlayerEntity) playerEntity).networkHandler.sendPacket(new PlaySoundIdS2CPacket(SpectrumSoundEvents.ENDER_SPLICE_BOUND.getId(), SoundCategory.PLAYERS, playerEntity.getPos(), 1.0F, 1.0F));
            }
        }

        return itemStack;
    }

    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        if(world.isClient) {
            SpectrumClient.minecraftClient.getSoundManager().play(new EnderSpliceChargingSoundInstance(user));
        }
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
        NbtCompound nbtCompound = stack.getTag();
        if(nbtCompound != null && nbtCompound.contains("PosX") && nbtCompound.contains("PosY") && nbtCompound.contains("PosZ") && nbtCompound.contains("Dimension")) {
            String dimensionKeyString = nbtCompound.getString("Dimension");
            String dimensionDisplayString = Support.getReadableDimensionString(dimensionKeyString);
            int x = (int) nbtCompound.getDouble("PosX");
            int y = (int) nbtCompound.getDouble("PosY");
            int z = (int) nbtCompound.getDouble("PosZ");

            tooltip.add(new TranslatableText("item.spectrum.ender_splice.tooltip.bound", x, y, z, dimensionDisplayString));
        } else {
            tooltip.add(new TranslatableText("item.spectrum.ender_splice.tooltip.unbound"));
        }
    }


}
