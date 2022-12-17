package de.dafuqs.spectrum.entity.entity;

import de.dafuqs.spectrum.entity.SpectrumEntityTypes;
import de.dafuqs.spectrum.entity.SpectrumTrackedDataHandlerRegistry;
import de.dafuqs.spectrum.items.tools.MalachiteArrowItem;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.world.World;

public class MalachiteArrowEntity extends PersistentProjectileEntity {
	
	private static final TrackedData<MalachiteArrowItem.Variant> VARIANT = DataTracker.registerData(MalachiteArrowEntity.class, SpectrumTrackedDataHandlerRegistry.MALACHITE_ARROW_VARIANT);
	
	public MalachiteArrowEntity(EntityType entityType, World world) {
		super(entityType, world);
	}
	
	public MalachiteArrowEntity(World world, LivingEntity owner) {
		super(SpectrumEntityTypes.MALACHITE_ARROW, owner, world);
	}
	
	public MalachiteArrowEntity(World world, double x, double y, double z) {
		super(SpectrumEntityTypes.MALACHITE_ARROW, x, y, z, world);
	}
	
	public void tick() {
		super.tick();
		if (this.world.isClient && !this.inGround) {
			this.world.addParticle(ParticleTypes.INSTANT_EFFECT, this.getX(), this.getY(), this.getZ(), 0.0, 0.0, 0.0);
		}
	}
	
	protected ItemStack asItemStack() {
		return new ItemStack(Items.SPECTRAL_ARROW);
	}
	
	protected void onHit(LivingEntity target) {
		super.onHit(target);
	}
	
	public void readCustomDataFromNbt(NbtCompound nbt) {
		super.readCustomDataFromNbt(nbt);
	}
	
	public void writeCustomDataToNbt(NbtCompound nbt) {
		super.writeCustomDataToNbt(nbt);
	}
	
	public void setVariant(MalachiteArrowItem.Variant variant) {
		this.dataTracker.set(VARIANT, variant);
	}
	
	public MalachiteArrowItem.Variant getVariant() {
		return this.dataTracker.get(VARIANT);
	}
	
}
