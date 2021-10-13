package de.dafuqs.spectrum.mixin;

import de.dafuqs.spectrum.registries.SpectrumEnchantments;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.CrossbowItem;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(CrossbowItem.class)
public class CrossbowItemMixin {

    @Inject(at = @At("HEAD"), method= "getSpeed(Lnet/minecraft/item/ItemStack;)F", cancellable = true)
    private static void getSpeed(ItemStack stack, CallbackInfoReturnable<Float> cir) {
        if(EnchantmentHelper.getLevel(SpectrumEnchantments.SNIPER, stack) > 0) {
            cir.setReturnValue(5.0F);
        }
    }

    // TODO: Make the shot invisible and unable to be picked up
    //@Inject(method = "shoot(Lnet/minecraft/world/World;Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/util/Hand;Lnet/minecraft/item/ItemStack;Lnet/minecraft/item/ItemStack;FZFFF)V", at = @At("HEAD"))
    /*@ModifyVariable(method = "shoot(Lnet/minecraft/world/World;Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/util/Hand;Lnet/minecraft/item/ItemStack;Lnet/minecraft/item/ItemStack;FZFFF)V", at = @At(value="INVOKE", target="Lnet/minecraft/world/ModifiableWorld;spawnEntity(Lnet/minecraft/entity/Entity;)Z"))
    private static Entity shoot(Entity entity) {
        if(entity instanceof ProjectileEntity projectileEntity) {
            projectileEntity.setInvisible(true);
        }
        return entity;
    }*/

}
