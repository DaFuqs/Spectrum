package de.dafuqs.spectrum.mixin;

import de.dafuqs.spectrum.enchantments.SpectrumEnchantments;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {

    @Shadow @Nullable protected PlayerEntity attackingPlayer;

    @ModifyArg(method = "Lnet/minecraft/entity/LivingEntity;dropXp()V",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/entity/ExperienceOrbEntity;spawn(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/util/math/Vec3d;I)V"
        ),
        index = 2)
    protected int applyExuberance(int originalXP) {
        return (int) (originalXP * getExuberanceMod(this.attackingPlayer));
    }

    private float getExuberanceMod(PlayerEntity attackingPlayer) {
        float exuberanceMod = 1.0F;
        if(attackingPlayer != null) {
            int exuberanceLevel = EnchantmentHelper.getEquipmentLevel(SpectrumEnchantments.EXUBERANCE, attackingPlayer);
            exuberanceMod = 1 + 0.2F * exuberanceLevel;
        }
        return exuberanceMod;
    }

}