package de.dafuqs.spectrum.items.tools;

import de.dafuqs.spectrum.entity.entity.GlassArrowEntity;
import de.dafuqs.spectrum.registries.SpectrumItems;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ArrowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import java.util.Locale;

public class GlassArrowItem extends ArrowItem {
	
	public enum Variant {
		MALACHITE,
		TOPAZ,
		AMETHYST,
		CITRINE,
		ONYX,
		MOONSTONE;

		public static Variant fromString(String string) {
			return valueOf(string.toUpperCase(Locale.ROOT));
		}
		
		public ItemStack getStack() {
			switch (this) {
				case TOPAZ -> {
					return SpectrumItems.TOPAZ_GLASS_ARROW.getDefaultStack();
				}
				case AMETHYST -> {
					return SpectrumItems.AMETHYST_GLASS_ARROW.getDefaultStack();
				}
				case CITRINE -> {
					return SpectrumItems.CITRINE_GLASS_ARROW.getDefaultStack();
				}
				case ONYX -> {
					return SpectrumItems.ONYX_GLASS_ARROW.getDefaultStack();
				}
				case MOONSTONE -> {
					return SpectrumItems.MOONSTONE_GLASS_ARROW.getDefaultStack();
				}
				default -> {
					return SpectrumItems.MALACHITE_GLASS_ARROW.getDefaultStack();
				}
			}
		}
	}
	
	public Variant variant;
	
	public GlassArrowItem(Settings settings, Variant variant) {
		super(settings);
		this.variant = variant;
	}
	
	public PersistentProjectileEntity createArrow(World world, ItemStack stack, LivingEntity shooter) {
		GlassArrowEntity entity = new GlassArrowEntity(world, shooter);
		entity.setVariant(variant);
		return entity;
	}
	
}
