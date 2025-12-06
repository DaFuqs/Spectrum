package de.dafuqs.spectrum.mixin.compat.connector.absent;

import com.llamalad7.mixinextras.injector.*;
import com.llamalad7.mixinextras.injector.wrapoperation.*;
import de.dafuqs.spectrum.components.*;
import de.dafuqs.spectrum.helpers.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.core.*;
import net.minecraft.core.component.*;
import net.minecraft.tags.*;
import net.minecraft.world.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.*;
import net.minecraft.world.item.enchantment.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.state.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;

@Mixin(Player.class)
public abstract class PlayerEntityMixin extends LivingEntity {
	
	protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, Level world) {
        super(entityType, world);
    }
	
	@ModifyExpressionValue(method = "attack", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Player;distanceToSqr(Lnet/minecraft/world/entity/Entity;)D", shift = At.Shift.AFTER))
    protected double spectrum$increaseSweepMaxDistance(double original) {
		var stack = this.getItemInHand(InteractionHand.MAIN_HAND);
        if (stack.getItem() == SpectrumItems.DRACONIC_TWINSWORD) {
			int channeling = SpectrumEnchantmentHelper.getLevel(level().registryAccess(), Enchantments.CHANNELING, stack);
            return original * 3 * ((channeling + 1) * 1.5);
		}
        return original;
    }
	
	@WrapOperation(method = "getDestroySpeed", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Inventory;getDestroySpeed(Lnet/minecraft/world/level/block/state/BlockState;)F"))
	private float spectrum$modifygetBlockBreakingSpeed(Inventory inventory, BlockState state, Operation<Float> original) {
		ItemStack stack = inventory.items.get(inventory.selected);
		RegistryAccess drm = registryAccess();
		Tool tool = stack.get(DataComponents.TOOL);
		float speed = original.call(inventory, state);
		
		// RAZING GAMING
		int razingLevel = SpectrumEnchantmentHelper.getLevel(drm, SpectrumEnchantments.RAZING, stack);
		if (razingLevel > 0 && tool != null && tool.getMiningSpeed(state) > tool.defaultMiningSpeed()) {
			float hardness = state.getBlock().defaultDestroyTime();
			speed = (float) Math.max(1 + hardness, Math.pow(2, 1 + razingLevel / 8F));
		}
		
		// INERTIA GAMING
		// inertia mining speed calculation logic is capped at 5 levels.
		// Higher and the formula would do weird stuff
		int inertiaLevel = SpectrumEnchantmentHelper.getLevel(drm, SpectrumEnchantments.INERTIA, stack);
		inertiaLevel = Math.min(4, inertiaLevel);
		if (inertiaLevel > 0) {
			var inertia = stack.getOrDefault(SpectrumDataComponentTypes.INERTIA, InertiaComponent.DEFAULT);
			if (state.is(inertia.lastMined())) {
				var additionalSpeedPercent = 2.0 * Math.log(inertia.count()) / Math.log((6 - inertiaLevel) * (6 - inertiaLevel) + 1);
				speed *= 0.5F + (float) additionalSpeedPercent;
			} else {
				speed /= 4;
			}
		}
		
		return speed;
	}
	
	@ModifyConstant(method = "getDestroySpeed",
			slice = @Slice(from = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Player;hasEffect(Lnet/minecraft/core/Holder;)Z"),
					to = @At("TAIL")
			),
			constant = {@Constant(floatValue = 0.3F), @Constant(floatValue = 0.09F), @Constant(floatValue = 0.0027F), @Constant(floatValue = 8.1E-4F)}
	)
	public float applyInexorableEffects(float value) {
		if (isInexorableActive())
			return 1F;
		
		return value;
	}
	
	@ModifyReturnValue(method = "getDestroySpeed", at = @At("RETURN"))
	public float applyInexorableAntiSlowdowns(float original) {
		if (isInexorableActive()) {
			var player = (Player) (Object) this;
			var f = original;
			
			boolean hasAquaAffinity = SpectrumEnchantmentHelper.getEquipmentLevel(player.level().registryAccess(), Enchantments.AQUA_AFFINITY, player) > 0;
			if (player.isEyeInFluid(FluidTags.WATER) && !hasAquaAffinity)
				f *= 5;
			
			if (!player.onGround())
				f *= 5;
			
			return f;
		}
		
		return original;
		
	}
	
	@Unique
	private boolean isInexorableActive() {
		Player player = (Player) (Object) this;
		return SpectrumEnchantmentHelper.hasEnchantment(player.level().registryAccess(), SpectrumEnchantments.INEXORABLE, player.getItemInHand(player.getUsedItemHand()));
	}
}
