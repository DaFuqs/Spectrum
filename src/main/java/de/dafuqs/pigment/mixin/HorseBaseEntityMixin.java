package de.dafuqs.pigment.mixin;

import de.dafuqs.pigment.interfaces.GravitableItem;
import net.minecraft.entity.passive.AbstractDonkeyEntity;
import net.minecraft.entity.passive.HorseBaseEntity;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HorseBaseEntity.class)
public class HorseBaseEntityMixin {

    @Shadow protected SimpleInventory items;

    @Inject(at = @At("HEAD"), method = "Lnet/minecraft/entity/passive/HorseBaseEntity;tick()V")
    public void tick(CallbackInfo callbackInfo) {
        if((Object) this instanceof AbstractDonkeyEntity) {
            AbstractDonkeyEntity thisEntity = (AbstractDonkeyEntity)(Object) this;

            if(thisEntity.hasChest()) {
                SimpleInventory var1 = this.items;

                for(int i = 0; i < var1.size(); i++) {
                    ItemStack itemStack = var1.getStack(i);
                    if (!itemStack.isEmpty() && (itemStack.getItem() instanceof GravitableItem)) {
                        ((GravitableItem) itemStack.getItem()).applyGravityEffect(itemStack, thisEntity.getEntityWorld(), thisEntity);
                    }
                }
            }
        }
    }


}
