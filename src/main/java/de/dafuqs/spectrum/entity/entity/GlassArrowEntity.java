package de.dafuqs.spectrum.entity.entity;

import de.dafuqs.spectrum.entity.SpectrumEntityTypes;
import de.dafuqs.spectrum.entity.SpectrumTrackedDataHandlerRegistry;
import de.dafuqs.spectrum.items.tools.GlassArrowItem;
import net.minecraft.block.BlockState;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class GlassArrowEntity extends PersistentProjectileEntity {
	
	private static final TrackedData<GlassArrowItem.Variant> VARIANT = DataTracker.registerData(GlassArrowEntity.class, SpectrumTrackedDataHandlerRegistry.GLASS_ARROW_VARIANT);
	
	public GlassArrowEntity(EntityType entityType, World world) {
		super(entityType, world);
	}
	
	public GlassArrowEntity(World world, LivingEntity owner) {
		super(SpectrumEntityTypes.GLASS_ARROW, owner, world);
	}
	
	public GlassArrowEntity(World world, double x, double y, double z) {
		super(SpectrumEntityTypes.GLASS_ARROW, x, y, z, world);
	}
	
	@Override
	public void tick() {
		super.tick();
		if (this.world.isClient && !this.inGround) {
			this.world.addParticle(ParticleTypes.INSTANT_EFFECT, this.getX(), this.getY(), this.getZ(), 0.0, 0.0, 0.0);
		}
	}
	
	@Override
	protected ItemStack asItemStack() {
		return dataTracker.get(VARIANT).getStack();
	}
	
	@Override
	protected void onHit(LivingEntity target) {
		super.onHit(target);
	}
	
	/**
	 * Glass Arrows pass through translucent blocks as if it were air
	 */
	@Override
	protected void onCollision(HitResult hitResult) {
		HitResult.Type type = hitResult.getType();
		if (type == HitResult.Type.BLOCK) {
			BlockPos hitPos = ((BlockHitResult) hitResult).getBlockPos();
			BlockState state = this.world.getBlockState(hitPos);
			if(state.isTranslucent(world, hitPos)) {
				return;
			}
		}
		super.onCollision(hitResult);
	}
	
	/**
	 * Glass Arrows pass through water almost effortlessly
	 */
	@Override
	protected float getDragInWater() {
		return 0.1F;
	}
	
	@Override
	protected void initDataTracker() {
		super.initDataTracker();
		this.dataTracker.startTracking(VARIANT, GlassArrowItem.Variant.GLASS);
	}
	
	public void setVariant(GlassArrowItem.Variant variant) {
		this.dataTracker.set(VARIANT, variant);
	}
	
	public GlassArrowItem.Variant getVariant() {
		return this.dataTracker.get(VARIANT);
	}
	
	@Override
	public void writeCustomDataToNbt(NbtCompound nbt) {
		super.writeCustomDataToNbt(nbt);
		nbt.putString("Variant", this.getVariant().toString());
	}
	
	@Override
	public void readCustomDataFromNbt(NbtCompound nbt) {
		super.readCustomDataFromNbt(nbt);
		this.setVariant(GlassArrowItem.Variant.fromString(nbt.getString("Variant")));
	}
	
}
