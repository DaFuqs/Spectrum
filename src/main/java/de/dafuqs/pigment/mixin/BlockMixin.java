package de.dafuqs.pigment.mixin;

import de.dafuqs.pigment.PigmentCommon;
import de.dafuqs.pigment.enchantments.AutoSmeltEnchantment;
import de.dafuqs.pigment.enchantments.PigmentEnchantments;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import org.apache.logging.log4j.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.List;

@Mixin(Block.class)
public abstract class BlockMixin {

    @Inject(method = "Lnet/minecraft/block/Block;getDroppedStacks(Lnet/minecraft/block/BlockState;Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/entity/BlockEntity;Lnet/minecraft/entity/Entity;Lnet/minecraft/item/ItemStack;)Ljava/util/List;", at = @At("RETURN"), cancellable = true)
    private static void getDroppedStacks(BlockState state, ServerWorld world, BlockPos pos, BlockEntity blockEntity, Entity entity, ItemStack stack, CallbackInfoReturnable<List<ItemStack>> cir) {
        PigmentCommon.LOGGER.log(Level.INFO, "test");

        List<ItemStack> originalStacks = cir.getReturnValue();

        if(originalStacks.size() > 0 && EnchantmentHelper.get(stack).containsKey(PigmentEnchantments.AUTO_SMELT)) {
            List<ItemStack> returnItemStacks = new ArrayList<>();

            for (ItemStack is : originalStacks) {
                ItemStack s = AutoSmeltEnchantment.applyAutoSmelt(is, world);
                while (s.getCount() > 0) {
                    int currentAmount = Math.min(s.getCount(), s.getItem().getMaxCount());
                    returnItemStacks.add(new ItemStack(s.getItem(), currentAmount));
                    s.setCount(s.getCount() - currentAmount);
                }
            }
            cir.setReturnValue(returnItemStacks);
        }
    }

}
