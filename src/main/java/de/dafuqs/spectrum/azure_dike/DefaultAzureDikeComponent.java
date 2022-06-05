package de.dafuqs.spectrum.azure_dike;

import de.dafuqs.spectrum.progression.SpectrumAdvancementCriteria;
import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import dev.onyxstudios.cca.api.v3.entity.PlayerCopyCallback;
import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import org.jetbrains.annotations.NotNull;

public class DefaultAzureDikeComponent implements AzureDikeComponent, AutoSyncedComponent, PlayerCopyCallback {
	
	public final static int BASE_RECHARGE_RATE_DELAY_TICKS_DEFAULT = 40;
	public final static int BASE_RECHARGE_RATE_DELAY_TICKS_AFTER_DAMAGE = 200;
	
	private final LivingEntity provider;
	
	private int protection = 0;
	private int currentRechargeDelay = 0;
	
	private int maxProtection = 0;
	private int rechargeDelayDefault = 0;
	private int rechargeDelayTicksAfterDamage = 0;
	
	public DefaultAzureDikeComponent(LivingEntity entity) {
		this.provider = entity;
	}
	
	@Override
	public int getProtection() {
		return this.protection;
	}
	
	@Override
	public int getMaxProtection() {
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
			int usedProtection = Math.min(protection, (int) incomingDamage);
			this.protection -= usedProtection;
			
			if (usedProtection > 0) {
				AzureDikeProvider.AZURE_DIKE_COMPONENT.sync(provider);
				if (provider instanceof ServerPlayerEntity serverPlayerEntity) {
					SpectrumAdvancementCriteria.AZURE_DIKE_CHARGE.trigger(serverPlayerEntity, this.protection, this.rechargeDelayDefault, -usedProtection);
				}
			}
			
			return incomingDamage - usedProtection;
		} else {
			return incomingDamage;
		}
	}
	
	@Override
	public void set(int maxProtection, int rechargeDelayDefault, int fasterRechargeAfterDamageTicks, boolean resetCharge) {
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
		tag.putInt("protection", this.protection);
		tag.putInt("current_recharge_delay", this.currentRechargeDelay);
		
		tag.putInt("max_protection", this.maxProtection);
		tag.putInt("recharge_delay_default", this.rechargeDelayDefault);
		tag.putInt("recharge_delay_after_damage", this.rechargeDelayTicksAfterDamage);
	}
	
	@Override
	public void serverTick() {
		if (this.currentRechargeDelay > 0) {
			this.currentRechargeDelay--;
		} else if (this.protection < this.maxProtection) {
			this.protection++;
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
