package de.dafuqs.spectrum.items.tools;

import de.dafuqs.spectrum.entity.entity.*;
import net.minecraft.entity.*;
import net.minecraft.entity.projectile.*;
import net.minecraft.item.*;
import net.minecraft.particle.*;
import net.minecraft.world.*;

public class GlassArrowItem extends ArrowItem {
	
	public final GlassArrowVariant variant;
	
	public GlassArrowItem(Settings settings, GlassArrowVariant variant, ParticleEffect particleEffect) {
		super(settings);
		this.variant = variant;
		variant.setData(this, particleEffect);
	}
	
	@Override
	public PersistentProjectileEntity createArrow(World world, ItemStack stack, LivingEntity shooter) {
		GlassArrowEntity entity = new GlassArrowEntity(world, shooter);
		entity.setVariant(variant);
		return entity;
	}
	
}
