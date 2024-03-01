package de.dafuqs.spectrum.mixin.compat.modonomicon.present;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.klikli_dev.modonomicon.book.BookIcon;
import de.dafuqs.spectrum.helpers.NbtHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.JsonHelper;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(BookIcon.class)
public class BookIconMixin {

    @Shadow
    @Final
    private ItemStack itemStack;

    @Inject(method = "fromJson", at = @At(value = "NEW", target = "(Lnet/minecraft/item/ItemConvertible;)Lnet/minecraft/item/ItemStack;"), locals = LocalCapture.CAPTURE_FAILHARD, cancellable = true)
    private static void spectrum$fromJson(JsonElement jsonElement, CallbackInfoReturnable<BookIcon> cir, JsonObject jsonObject, Item item) {
        ItemStack itemStack = new ItemStack(item);
        if (jsonObject.has("nbt")) {
            NbtCompound nbt = NbtHelper.fromJsonObject(JsonHelper.getObject(jsonObject, "nbt"));
            itemStack.setNbt(nbt);
        }
        cir.setReturnValue(new BookIcon(itemStack));
    }

    @Inject(method = "fromNetwork", at = @At(value = "INVOKE", target = "Lnet/minecraft/network/PacketByteBuf;readIdentifier()Lnet/minecraft/util/Identifier;", ordinal = 1), cancellable = true)
    private static void spectrum$fromNetwork(PacketByteBuf buffer, CallbackInfoReturnable<BookIcon> cir) {
        ItemStack itemStack = buffer.readItemStack();
        cir.setReturnValue(new BookIcon(itemStack));
    }

    @Inject(method = "toNetwork", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;getItem()Lnet/minecraft/item/Item;"), cancellable = true)
    private void spectrum$toNetwork(PacketByteBuf buffer, CallbackInfo ci) {
        buffer.writeItemStack(itemStack);
        ci.cancel();
    }

}
