package de.dafuqs.spectrum.compat.travelersbackpack;

import com.tiviacz.travelersbackpack.api.fluids.*;
import com.tiviacz.travelersbackpack.fluids.*;
import com.tiviacz.travelersbackpack.inventory.*;
import de.dafuqs.spectrum.compat.*;
import de.dafuqs.spectrum.helpers.*;
import de.dafuqs.spectrum.registries.*;
import net.fabricmc.api.*;
import net.minecraft.world.effect.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.material.*;

import java.util.*;

public class TravelersBackpackCompat extends SpectrumIntegrationPacks.ModIntegrationPack {
	
	public abstract static class SpectrumEffectFluid extends EffectFluid {
		
		public SpectrumEffectFluid(String id, Fluid fluid) {
			super(id, fluid, 81000);
		}
		
		public boolean canExecuteEffect(FluidVariantWrapper stack, Level world, Entity entity) {
			return stack.getAmount() >= this.amountRequired;
		}
		
	}
	
	@Override
	public void register() {
		EffectFluidRegistry.registerFluidEffect(new SpectrumEffectFluid("spectrum:goo", SpectrumFluids.GOO.getSource()) {
			@Override
			public void affectDrinker(FluidVariantWrapper fluidVariantWrapper, Level world, Entity entity) {
				if (entity instanceof LivingEntity livingEntity) {
					livingEntity.addEffect(new MobEffectInstance(MobEffects.CONFUSION, 200));
					livingEntity.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 400, 2));
					livingEntity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 600, 3));
				}
			}
			
		});
		
		EffectFluidRegistry.registerFluidEffect(new SpectrumEffectFluid("spectrum:liquid_crystal", SpectrumFluids.LIQUID_CRYSTAL.getSource()) {
			@Override
			public void affectDrinker(FluidVariantWrapper fluidStack, Level world, Entity entity) {
				if (entity instanceof Player player) {
					player.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 400, 1));
				}
			}
		});
		
		EffectFluidRegistry.registerFluidEffect(new SpectrumEffectFluid("spectrum:midnight_solution", SpectrumFluids.MIDNIGHT_SOLUTION.getSource()) {
			@Override
			public void affectDrinker(FluidVariantWrapper fluidStack, Level world, Entity entity) {
				if (entity instanceof Player player) {
					player.giveExperiencePoints(-20);
					
					// disenchant random enchanted item
					List<ItemStack> equipment = new ArrayList<>();
					player.getAllSlots().forEach(equipment::add);
					Collections.shuffle(equipment);
					
					for (ItemStack equip : equipment) {
						ItemEnchantments enchants = EnchantmentHelper.getEnchantmentsForCrafting(equip);
						if (!enchants.isEmpty()) {
							var enchantments = enchants.keySet();
							var enchantment = enchantments.stream().toList().get(new Random().nextInt(enchantments.size()));
							SpectrumEnchantmentHelper.removeEnchantments(equip, enchantment);
						}
					}
				}
			}
		});
		
		EffectFluidRegistry.registerFluidEffect(new SpectrumEffectFluid("spectrum:dragonrot", SpectrumFluids.DRAGONROT.getSource()) {
			@Override
			public void affectDrinker(FluidVariantWrapper fluidStack, Level world, Entity entity) {
				if (entity instanceof LivingEntity livingEntity) {
					livingEntity.addEffect(new MobEffectInstance(SpectrumStatusEffects.LIFE_DRAIN, 600, 3));
					livingEntity.hurt(SpectrumDamageTypes.dragonrot(world), 1000); // 💀
				}
			}
		});
	}
	
	@Environment(EnvType.CLIENT)
	@Override
	public void registerClient() {
	
	}
	
}
