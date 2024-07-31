package de.dafuqs.spectrum.cca.azure_dike;

import de.dafuqs.spectrum.progression.*;
import dev.onyxstudios.cca.api.v3.component.sync.*;
import dev.onyxstudios.cca.api.v3.entity.*;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.*;
import net.minecraft.nbt.*;
import net.minecraft.server.network.*;
import org.jetbrains.annotations.*;

public class DefaultAzureDikeComponent implements AzureDikeComponent, AutoSyncedComponent, PlayerCopyCallback {
	
	public final static int BASE_RECHARGE_RATE_DELAY_TICKS_DEFAULT = 40;
	public final static int BASE_RECHARGE_RATE_DELAY_TICKS_AFTER_DAMAGE = 200;
	
	private final LivingEntity provider;
	
	private float protection = 0;
	private int currentRechargeDelay = 0;
	
	private float maxProtection = 0;
	private int rechargeDelayDefault = 0;
	private int rechargeDelayTicksAfterDamage = 0;

	public DefaultAzureDikeComponent(LivingEntity entity) {
		this.provider = entity;
	}
	
	@Override
	public float getProtection() {
		return this.protection;
	}
	
	@Override
	public float getMaxProtection() {
		return this.maxProtection;
	}
	
	@Override
	public int getRechargeDelayDefault() {
		return this.rechargeDelayDefault;
	}
	
	@Override
	public int getCurrentRechargeDelay() {
		return this.currentRechargeDelay;
	}
	
	@Override
	public int getRechargeDelayTicksAfterDamage() {
		return this.rechargeDelayTicksAfterDamage;
	}
	
	@Override
	public float absorbDamage(float incomingDamage) {
		this.currentRechargeDelay = this.rechargeDelayTicksAfterDamage;
		if (this.protection > 0) {
			float absorbedDamage = Math.min(protection, incomingDamage);
			float consumedDike = absorbedDamage / 2; //Make dike have a bit more mileage, still a fairly fragile form of hp but it doesn't get smoked entirely.
			this.protection -= consumedDike;
			
			if (consumedDike > 0) {
				AzureDikeProvider.AZURE_DIKE_COMPONENT.sync(provider);
				if (provider instanceof ServerPlayerEntity serverPlayerEntity) {
					SpectrumAdvancementCriteria.AZURE_DIKE_CHARGE.trigger(serverPlayerEntity, this.protection, this.rechargeDelayDefault, -consumedDike);
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
		this.rechargeDelayDefault = rechargeDelayDefault;
		this.rechargeDelayTicksAfterDamage = fasterRechargeAfterDamageTicks;
		this.currentRechargeDelay = this.rechargeDelayDefault;
		if (resetCharge) {
			this.protection = 0;
		} else {
			this.protection = Math.min(this.protection, this.maxProtection);
		}
		
		AzureDikeProvider.AZURE_DIKE_COMPONENT.sync(provider);
	}
	
	@Override
	public void readFromNbt(NbtCompound tag) {
		this.protection = tag.getInt("protection");
		this.currentRechargeDelay = tag.getInt("current_recharge_delay");
		
		this.maxProtection = tag.getInt("max_protection");
		this.rechargeDelayDefault = tag.getInt("recharge_delay_default");
		this.rechargeDelayTicksAfterDamage = tag.getInt("recharge_delay_after_damage");
	}
	
	@Override
	public void writeToNbt(NbtCompound tag) {
		tag.putFloat("protection", this.protection);
		tag.putInt("current_recharge_delay", this.currentRechargeDelay);
		
		tag.putFloat("max_protection", this.maxProtection);
		tag.putInt("recharge_delay_default", this.rechargeDelayDefault);
		tag.putInt("recharge_delay_after_damage", this.rechargeDelayTicksAfterDamage);
	}
	
	@Override
	public void serverTick() {
		if (this.currentRechargeDelay > 0) {
			this.currentRechargeDelay--;
		} else if (this.protection < this.maxProtection) {
			protection = Math.min(maxProtection, protection + 1);
			this.currentRechargeDelay = this.rechargeDelayDefault;
			AzureDikeProvider.AZURE_DIKE_COMPONENT.sync(provider);
			if (provider instanceof ServerPlayerEntity serverPlayerEntity) {
				SpectrumAdvancementCriteria.AZURE_DIKE_CHARGE.trigger(serverPlayerEntity, this.protection, this.rechargeDelayDefault, 1);
			}
		}
	}
	
	@Override
	public void copyData(@NotNull ServerPlayerEntity original, @NotNull ServerPlayerEntity clone, boolean lossless) {
		AzureDikeComponent o = AzureDikeProvider.AZURE_DIKE_COMPONENT.get(original);
		AzureDikeComponent c = AzureDikeProvider.AZURE_DIKE_COMPONENT.get(clone);
		c.set(o.getMaxProtection(), o.getRechargeDelayDefault(), o.getRechargeDelayTicksAfterDamage(), lossless);
	}
	
}
