package de.dafuqs.spectrum.mixin.compat.connector.present;

import de.dafuqs.spectrum.helpers.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.world.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.item.enchantment.*;
import net.minecraft.world.level.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;

@Mixin(Player.class)
public abstract class PlayerEntityMixin extends LivingEntity {
	
	protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, Level world) {
        super(entityType, world);
    }

    @ModifyVariable(method = "attack", name = "entityReachSq", at = @At(value = "STORE"))
    protected double spectrum$increaseSweepMaxDistance(double original) {
		var stack = this.getItemInHand(InteractionHand.MAIN_HAND);
        if (stack.getItem() == SpectrumItems.DRACONIC_TWINSWORD) {
			int channeling = SpectrumEnchantmentHelper.getLevel(level().registryAccess(), Enchantments.CHANNELING, stack);
			return original * 3 * ((channeling + 1) * 1.5);
		}
        return original;
    }
}
