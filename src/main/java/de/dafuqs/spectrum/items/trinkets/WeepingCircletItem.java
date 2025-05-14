package de.dafuqs.spectrum.items.trinkets;

import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.networking.s2c_payloads.*;
import de.dafuqs.spectrum.registries.*;
import dev.emi.trinkets.api.*;
import net.minecraft.*;
import net.minecraft.core.particles.*;
import net.minecraft.network.chat.*;
import net.minecraft.server.level.*;
import net.minecraft.world.effect.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.animal.axolotl.*;
import net.minecraft.world.item.*;
import net.minecraft.world.level.*;
import net.minecraft.world.phys.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class WeepingCircletItem extends SpectrumTrinketItem {
	
	private final static int TRIGGER_EVERY_X_TICKS = 40;
	private final static int EFFECT_DURATION = TRIGGER_EVERY_X_TICKS + 10;
	
	private final static int HEAL_AXOLOTLS_EVERY_X_TICKS = 160;
	private final static int MAX_AXOLOTL_DISTANCE = 12;
	private final static int AXOLOTL_HEALING = 2;
	
	public WeepingCircletItem(Properties settings) {
		super(settings, SpectrumCommon.locate("unlocks/trinkets/weeping_circlet"));
	}

	@Override
	public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag type) {
		super.appendHoverText(stack, context, tooltip, type);
		tooltip.add(Component.translatable("item.spectrum.weeping_circlet.tooltip").withStyle(ChatFormatting.GRAY));
		tooltip.add(Component.translatable("item.spectrum.weeping_circlet.tooltip2").withStyle(ChatFormatting.GRAY));
		tooltip.add(Component.translatable("item.spectrum.weeping_circlet.tooltip3").withStyle(ChatFormatting.GRAY));
	}

	@Override
	public void onEquip(ItemStack stack, SlotReference slot, LivingEntity entity) {
		super.onEquip(stack, slot, entity);
		doEffects(entity, true);
	}
	
	@Override
	public void tick(ItemStack stack, SlotReference slot, LivingEntity entity) {
		super.tick(stack, slot, entity);
		doEffects(entity, false);
	}
	
	private void doEffects(LivingEntity entity, boolean always) {
		Level world = entity.level();
		if (!world.isClientSide) {
			long time = entity.level().getGameTime();
			if (entity.isEyeInFluid(SpectrumFluidTags.ACTIVATES_WEEPING_CIRCLET)) {
				if (always || time % TRIGGER_EVERY_X_TICKS == 0) {
					entity.setAirSupply(entity.getMaxAirSupply());
					entity.addEffect(new MobEffectInstance(MobEffects.DOLPHINS_GRACE, EFFECT_DURATION, 1, true, true));
					entity.addEffect(new MobEffectInstance(MobEffects.CONDUIT_POWER, EFFECT_DURATION, 0, true, true));
				}
				if ((always || time % HEAL_AXOLOTLS_EVERY_X_TICKS == 0) && entity instanceof ServerPlayer serverPlayerEntity) {
					healLovingAxolotls(serverPlayerEntity);
				}
			}
		}
	}
	
	private void healLovingAxolotls(@NotNull ServerPlayer entity) {
		Level world = entity.level();
		List<Axolotl> nearbyAxolotls = entity.level().getEntities(EntityType.AXOLOTL, AABB.ofSize(entity.position(), MAX_AXOLOTL_DISTANCE, MAX_AXOLOTL_DISTANCE, MAX_AXOLOTL_DISTANCE), LivingEntity::isAlive);
		for (Axolotl axolotlEntity : nearbyAxolotls) {
			if (axolotlEntity.getHealth() < axolotlEntity.getMaxHealth() && axolotlEntity.getLoveCause() != null && axolotlEntity.getLoveCause().equals(entity)) {
				axolotlEntity.heal(AXOLOTL_HEALING);
				entity.playSound(SpectrumSoundEvents.BLOCK_CITRINE_BLOCK_CHIME, 1.0F, 0.9F + world.random.nextFloat() * 0.2F);
				PlayParticleWithRandomOffsetAndVelocityPayload.playParticleWithRandomOffsetAndVelocity((ServerLevel) axolotlEntity.level(), axolotlEntity.position(), ParticleTypes.WAX_OFF, 10, new Vec3(0.5, 0.5, 0.5), new Vec3(0, 0, 0));
			}
		}
	}
	
}
