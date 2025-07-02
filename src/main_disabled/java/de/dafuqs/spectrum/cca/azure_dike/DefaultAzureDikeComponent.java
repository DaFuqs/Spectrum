package de.dafuqs.spectrum.cca.azure_dike;

import de.dafuqs.spectrum.progression.*;
import net.minecraft.nbt.*;
import net.minecraft.core.*;
import net.minecraft.server.level.*;
import net.minecraft.world.entity.*;
import org.jetbrains.annotations.*;
import org.ladysnake.cca.api.v3.component.sync.*;

public class DefaultAzureDikeComponent implements AzureDikeComponent, AutoSyncedComponent {
	
	public final static int BASE_RECHARGE_DELAY_TICKS = 40;
	public final static int BASE_RECHARGE_DELAY_TICKS_AFTER_DAMAGE = 200;
	
	private final LivingEntity provider;
	
	private float maxProtection = 0;
	private int ticksPerPointOfRecharge = 0;
	private int rechargeDelayTicksAfterGettingHit = 0;
	
	private float currentProtection = 0;
	private int currentRechargeDelay = 0;

	public DefaultAzureDikeComponent(LivingEntity entity) {
		this.provider = entity;
	}
	
	@Override
	public float getCurrentProtection() {
		return this.currentProtection;
	}
	
	@Override
	public float getMaxProtection() {
		return this.maxProtection;
	}
	
	@Override
	public int getTicksPerPointOfRecharge() {
		return this.ticksPerPointOfRecharge;
	}
	
	@Override
	public int getCurrentRechargeDelay() {
		return this.currentRechargeDelay;
	}
	
	@Override
	public int getRechargeDelayTicksAfterGettingHit() {
		return this.rechargeDelayTicksAfterGettingHit;
	}
	
	@Override
	public float absorbDamage(float incomingDamage) {
		this.currentRechargeDelay = this.rechargeDelayTicksAfterGettingHit;
		if (this.currentProtection > 0) {
			float absorbedDamage = Math.min(currentProtection, incomingDamage);
			this.currentProtection -= absorbedDamage;
			
			if (absorbedDamage > 0) {
				AzureDikeProvider.AZURE_DIKE_COMPONENT.sync(provider);
				if (provider instanceof ServerPlayer serverPlayerEntity) {
					SpectrumAdvancementCriteria.AZURE_DIKE_CHARGE.trigger(serverPlayerEntity, this.currentProtection, this.ticksPerPointOfRecharge, -absorbedDamage);
				}
			}
			
			return incomingDamage - absorbedDamage;
		} else {
			return incomingDamage;
		}
	}
	
	@Override
	public void set(float maxProtection, int rechargeDelayDefault, int fasterRechargeAfterDamageTicks, boolean resetCharge) {
		this.maxProtection = maxProtection;
		this.ticksPerPointOfRecharge = rechargeDelayDefault;
		this.rechargeDelayTicksAfterGettingHit = fasterRechargeAfterDamageTicks;
		this.currentRechargeDelay = this.ticksPerPointOfRecharge;
		if (resetCharge) {
			this.currentProtection = 0;
		} else {
			this.currentProtection = Math.min(this.currentProtection, this.maxProtection);
		}
		
		AzureDikeProvider.AZURE_DIKE_COMPONENT.sync(provider);
	}
	
	@Override
	public void readFromNbt(CompoundTag tag, HolderLookup.@NotNull Provider wrapperLookup) {
		this.currentProtection = tag.getInt("protection");
		this.currentRechargeDelay = tag.getInt("current_recharge_delay");
		
		this.maxProtection = tag.getInt("max_protection");
		this.ticksPerPointOfRecharge = tag.getInt("recharge_delay_default");
		this.rechargeDelayTicksAfterGettingHit = tag.getInt("recharge_delay_after_damage");
	}
	
	@Override
	public void writeToNbt(CompoundTag tag, HolderLookup.@NotNull Provider wrapperLookup) {
		tag.putFloat("protection", this.currentProtection);
		tag.putInt("current_recharge_delay", this.currentRechargeDelay);
		
		tag.putFloat("max_protection", this.maxProtection);
		tag.putInt("recharge_delay_default", this.ticksPerPointOfRecharge);
		tag.putInt("recharge_delay_after_damage", this.rechargeDelayTicksAfterGettingHit);
	}
	
	@Override
	public void serverTick() {
		if (this.currentRechargeDelay > 0) {
			this.currentRechargeDelay--;
		} else if (this.currentProtection < this.maxProtection) {
			currentProtection = Math.min(maxProtection, currentProtection + 1);
			this.currentRechargeDelay = this.ticksPerPointOfRecharge;
			AzureDikeProvider.AZURE_DIKE_COMPONENT.sync(provider);
			if (provider instanceof ServerPlayer serverPlayerEntity) {
				SpectrumAdvancementCriteria.AZURE_DIKE_CHARGE.trigger(serverPlayerEntity, this.currentProtection, this.ticksPerPointOfRecharge, 1);
			}
		}
	}
	
}
