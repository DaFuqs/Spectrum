package de.dafuqs.pigment.mixin;

import de.dafuqs.pigment.entity.PigmentEntityTypes;
import de.dafuqs.pigment.items.PigmentItems;
import net.minecraft.entity.decoration.ItemFrameEntity;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemFrameEntity.class)
public class ItemFrameEntityMixin {

    @Inject(at=@At("HEAD"), method="Lnet/minecraft/entity/decoration/ItemFrameEntity;getAsItemStack()Lnet/minecraft/item/ItemStack;", cancellable = true)
    public void checkInvisibleItemFrameDrop(CallbackInfoReturnable<ItemStack> callbackInfoReturnable) {
        ItemFrameEntity thisEntity = (ItemFrameEntity) (Object) this;
        if(thisEntity.getType()  == PigmentEntityTypes.INVISIBLE_ITEM_FRAME) {
            callbackInfoReturnable.setReturnValue(new ItemStack(PigmentItems.INVISIBLE_ITEM_FRAME));
        }
    }
}
