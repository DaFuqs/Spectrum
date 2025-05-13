package de.dafuqs.spectrum.compat.travelersbackpack;

import com.tiviacz.travelersbackpack.api.fluids.*;
import com.tiviacz.travelersbackpack.fluids.*;
import com.tiviacz.travelersbackpack.inventory.*;
import de.dafuqs.spectrum.compat.*;
import de.dafuqs.spectrum.helpers.*;
import de.dafuqs.spectrum.registries.*;
import net.fabricmc.api.*;
import net.minecraft.component.type.*;
import net.minecraft.enchantment.*;
import net.minecraft.entity.*;
import net.minecraft.entity.effect.*;
import net.minecraft.entity.player.*;
import net.minecraft.fluid.*;
import net.minecraft.item.*;
import net.minecraft.world.*;

import java.util.*;

public class TravelersBackpackCompat extends SpectrumIntegrationPacks.ModIntegrationPack {
	
	public abstract static class SpectrumEffectFluid extends EffectFluid {
		
		public SpectrumEffectFluid(String id, Fluid fluid) {
			super(id, fluid, 81000);
		}
		
		public boolean canExecuteEffect(FluidVariantWrapper stack, World world, Entity entity) {
			return stack.getAmount() >= this.amountRequired;
		}
		
	}
	
	@Override
	public void register() {
		EffectFluidRegistry.registerFluidEffect(new SpectrumEffectFluid("spectrum:goo", SpectrumFluids.GOO.getStill()) {
			@Override
			public void affectDrinker(FluidVariantWrapper fluidVariantWrapper, World world, Entity entity) {
				if (entity instanceof LivingEntity livingEntity) {
					livingEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.NAUSEA, 200));
					livingEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.RESISTANCE, 400, 2));
					livingEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 600, 3));
				}
			}
			
		});
		
		EffectFluidRegistry.registerFluidEffect(new SpectrumEffectFluid("spectrum:liquid_crystal", SpectrumFluids.LIQUID_CRYSTAL.getStill()) {
			@Override
			public void affectDrinker(FluidVariantWrapper fluidStack, World world, Entity entity) {
				if (entity instanceof PlayerEntity player) {
					player.addStatusEffect(new StatusEffectInstance(StatusEffects.REGENERATION, 400, 1));
				}
			}
		});
		
		EffectFluidRegistry.registerFluidEffect(new SpectrumEffectFluid("spectrum:midnight_solution", SpectrumFluids.MIDNIGHT_SOLUTION.getStill()) {
			@Override
			public void affectDrinker(FluidVariantWrapper fluidStack, World world, Entity entity) {
				if (entity instanceof PlayerEntity player) {
					player.addExperience(-20);
					
					// disenchant random enchanted item
					List<ItemStack> equipment = new ArrayList<>();
					player.getEquippedItems().forEach(equipment::add);
					Collections.shuffle(equipment);
					
					for (ItemStack equip : equipment) {
						ItemEnchantmentsComponent enchants = EnchantmentHelper.getEnchantments(equip);
						if (!enchants.isEmpty()) {
							var enchantments = enchants.getEnchantments();
							var enchantment = enchantments.stream().toList().get(new Random().nextInt(enchantments.size()));
							SpectrumEnchantmentHelper.removeEnchantments(equip, enchantment);
						}
					}
				}
			}
		});
		
		EffectFluidRegistry.registerFluidEffect(new SpectrumEffectFluid("spectrum:dragonrot", SpectrumFluids.DRAGONROT.getStill()) {
			@Override
			public void affectDrinker(FluidVariantWrapper fluidStack, World world, Entity entity) {
				if (entity instanceof LivingEntity livingEntity) {
					livingEntity.addStatusEffect(new StatusEffectInstance(SpectrumStatusEffects.LIFE_DRAIN, 600, 3));
					livingEntity.damage(SpectrumDamageTypes.dragonrot(world), 1000); // 💀
				}
			}
		});
	}
	
	@Environment(EnvType.CLIENT)
	@Override
	public void registerClient() {
	
	}
	
}
