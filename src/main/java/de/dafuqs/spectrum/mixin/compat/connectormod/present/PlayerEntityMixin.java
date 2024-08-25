package de.dafuqs.spectrum.mixin.compat.connectormod.present;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import de.dafuqs.spectrum.registries.SpectrumItems;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity {

    protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @Unique
    protected int getChanneling(ItemStack stack) {
        return EnchantmentHelper.getLevel(Enchantments.CHANNELING, stack);
    }

    @SuppressWarnings({"MixinAnnotationTarget", "InvalidInjectorMethodSignature"})
    @ModifyVariable(method = "attack", name = "entityReachSq", at = @At(value = "STORE"))
    protected double spectrum$increaseSweepMaxDistance(double original) {
        var stack = this.getStackInHand(Hand.MAIN_HAND);
        if (stack.getItem() == SpectrumItems.DRACONIC_TWINSWORD)
            return original * 3 * ((getChanneling(stack) + 1) * 1.5);
        return original;
    }
}
