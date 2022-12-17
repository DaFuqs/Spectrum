package de.dafuqs.spectrum.items.tools;

import de.dafuqs.spectrum.entity.entity.MalachiteArrowEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ArrowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class MalachiteArrowItem extends ArrowItem {
	
	public enum Variant {
		GLASS,
		CYAN,
		MAGENTA,
		YELLOW,
		BLACK,
		WHITE
	}
	
	public Variant variant;
	
	public MalachiteArrowItem(Settings settings, Variant variant) {
		super(settings);
		this.variant = variant;
	}
	
	public PersistentProjectileEntity createArrow(World world, ItemStack stack, LivingEntity shooter) {
		MalachiteArrowEntity entity = new MalachiteArrowEntity(world, shooter);
		entity.setVariant(variant);
		return entity;
	}
	
}
