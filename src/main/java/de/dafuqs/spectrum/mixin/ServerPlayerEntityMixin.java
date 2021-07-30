package de.dafuqs.spectrum.mixin;

import de.dafuqs.spectrum.enchantments.SpectrumEnchantments;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayerEntity.class)
public class ServerPlayerEntityMixin {

    @Inject(at = @At("HEAD"), method = "Lnet/minecraft/server/network/ServerPlayerEntity;onDeath(Lnet/minecraft/entity/damage/DamageSource;)V")
    protected void dropPlayerHeadWithTreasureHunt(DamageSource source, CallbackInfo ci) {
        ServerPlayerEntity thisEntity = (ServerPlayerEntity)(Object) this;
        if (!thisEntity.isSpectator() && source.getAttacker() instanceof LivingEntity) {
            int damageSourceTreasureHunt = EnchantmentHelper.getEquipmentLevel(SpectrumEnchantments.TREASURE_HUNTER, (LivingEntity) source.getAttacker());
            if(damageSourceTreasureHunt > 0) {
                ServerWorld serverWorld = thisEntity.getServerWorld();
                boolean shouldDropHead = serverWorld.getRandom().nextFloat() < 0.2 * damageSourceTreasureHunt;
                if(shouldDropHead) {
                    ItemStack headItemStack = new ItemStack(Items.PLAYER_HEAD);

                    NbtCompound compoundTag  = new NbtCompound();
                    compoundTag.putString("SkullOwner", thisEntity.getName().getString());

                    headItemStack.setTag(compoundTag);

                    ItemEntity headEntity = new ItemEntity(serverWorld, thisEntity.getX(), thisEntity.getY(), thisEntity.getZ(), headItemStack);
                    serverWorld.spawnEntity(headEntity);
                }
            }
        }
    }

}