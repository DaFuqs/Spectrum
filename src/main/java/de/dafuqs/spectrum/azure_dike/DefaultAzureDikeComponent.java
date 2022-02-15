package de.dafuqs.spectrum.azure_dike;

import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import dev.onyxstudios.cca.api.v3.component.tick.ServerTickingComponent;
import dev.onyxstudios.cca.api.v3.entity.PlayerCopyCallback;
import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import org.jetbrains.annotations.NotNull;

public class DefaultAzureDikeComponent implements AzureDikeComponent, AutoSyncedComponent, PlayerCopyCallback {
	
	private final LivingEntity provider;
	
	private int protection = 0;
	private int maxProtection = 0;
	private float rechargeRate = 0;
	
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
	public float getRechargeRate() {
		return this.rechargeRate;
	}
	
	@Override
	public void damage(int usedProtection) {
		this.protection -= usedProtection;
	}
	
	@Override
	public void set(int maxProtection, float rechargeRate, boolean resetCharge) {
		this.maxProtection = maxProtection;
		this.rechargeRate = rechargeRate;
		if(resetCharge) {
			this.protection = 0;
		} else {
			this.protection = Math.min(this.protection, this.maxProtection);
		}
		
		AzureDikeProvider.AZURE_DIKE_COMPONENT.sync(provider);
	}
	
	@Override
	public void readFromNbt(NbtCompound tag) {
		this.protection = tag.getInt("protection");
		this.maxProtection = tag.getInt("max_protection");
		this.rechargeRate = tag.getFloat("recharge_rate");
	}
	
	@Override
	public void writeToNbt(NbtCompound tag) {
		tag.putInt("protection", this.protection);
		tag.putInt("max_protection", this.maxProtection);
		tag.putFloat("recharge_rate", this.rechargeRate);
	}
	
	@Override
	public void serverTick() {
		if(this.protection < this.maxProtection) {
			this.protection++;
			AzureDikeProvider.AZURE_DIKE_COMPONENT.sync(this.protection);
		}
	}
	
	@Override
	public void copyData(@NotNull ServerPlayerEntity original, @NotNull ServerPlayerEntity clone, boolean lossless) {
		AzureDikeComponent o = AzureDikeProvider.AZURE_DIKE_COMPONENT.get(original);
		AzureDikeComponent c = AzureDikeProvider.AZURE_DIKE_COMPONENT.get(clone);
		if(lossless) {
			c.set(o.getMaxProtection(), o.getRechargeRate(), true);
		} else {
			c.set(o.getProtection(), o.getMaxProtection(), false);
		}
	}
	
}
