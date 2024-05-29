package de.dafuqs.spectrum.compat.travelersbackpack;

import com.tiviacz.travelersbackpack.fluids.*;
import de.dafuqs.spectrum.compat.*;
import de.dafuqs.spectrum.helpers.*;
import de.dafuqs.spectrum.registries.*;
import net.fabricmc.fabric.api.transfer.v1.fluid.*;
import net.fabricmc.fabric.api.transfer.v1.storage.*;
import net.minecraft.enchantment.*;
import net.minecraft.entity.*;
import net.minecraft.entity.effect.*;
import net.minecraft.entity.player.*;
import net.minecraft.fluid.*;
import net.minecraft.item.*;
import net.minecraft.world.*;

import java.util.*;

public class TravelersBackpackCompat extends SpectrumIntegrationPacks.ModIntegrationPack {
	
	@Override
	public void register() {
		EffectFluidRegistry.registerFluidEffect(new SpectrumEffectFluid("spectrum:mud", SpectrumFluids.MUD.getStill()) {
			@Override
			public void affectDrinker(StorageView<FluidVariant> fluidStack, World world, Entity entity) {
				if (entity instanceof LivingEntity livingEntity) {
					livingEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.NAUSEA, 200));
					livingEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.RESISTANCE, 400, 2));
					livingEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 600, 3));
				}
			}
		});
		
		EffectFluidRegistry.registerFluidEffect(new SpectrumEffectFluid("spectrum:liquid_crystal", SpectrumFluids.LIQUID_CRYSTAL.getStill()) {
			@Override
			public void affectDrinker(StorageView<FluidVariant> fluidStack, World world, Entity entity) {
				if (entity instanceof PlayerEntity player) {
					player.addStatusEffect(new StatusEffectInstance(StatusEffects.REGENERATION, 400, 1));
				}
			}
		});
		
		EffectFluidRegistry.registerFluidEffect(new SpectrumEffectFluid("spectrum:midnight_solution", SpectrumFluids.MIDNIGHT_SOLUTION.getStill()) {
			@Override
			public void affectDrinker(StorageView<FluidVariant> fluidStack, World world, Entity entity) {
				if (entity instanceof PlayerEntity player) {
					player.addExperience(-20);
					
					// disenchant random enchanted item
					List<ItemStack> equipment = new ArrayList<>();
					player.getItemsEquipped().forEach(equipment::add);
					Collections.shuffle(equipment);
					
					for (ItemStack equip : equipment) {
						Map<Enchantment, Integer> enchants = EnchantmentHelper.get(equip);
						if (!enchants.isEmpty()) {
							Set<Enchantment> enchantments = enchants.keySet();
							Enchantment enchantment = enchantments.stream().skip(new Random().nextInt(enchantments.size())).findFirst().orElse(null);
							SpectrumEnchantmentHelper.removeEnchantments(equip, enchantment);
						}
					}
				}
			}
		});
		
		EffectFluidRegistry.registerFluidEffect(new SpectrumEffectFluid("spectrum:dragonrot", SpectrumFluids.DRAGONROT.getStill()) {
			@Override
			public void affectDrinker(StorageView<FluidVariant> fluidStack, World world, Entity entity) {
				if (entity instanceof LivingEntity livingEntity) {
					livingEntity.addStatusEffect(new StatusEffectInstance(SpectrumStatusEffects.LIFE_DRAIN, 600, 3));
					livingEntity.damage(SpectrumDamageTypes.dragonrot(world), 1000); // 💀
				}
			}
		});
	}
	
	@Override
	public void registerClient() {
	
	}
	
	public abstract static class SpectrumEffectFluid extends EffectFluid {
		
		public SpectrumEffectFluid(String id, Fluid fluid) {
			super(id, fluid, 81000L);
		}
		
		public abstract void affectDrinker(StorageView<FluidVariant> fluidStack, World world, Entity entity);
		
		public boolean canExecuteEffect(StorageView<FluidVariant> stack, World world, Entity entity) {
			return stack.getAmount() >= this.amountRequired;
		}
		
	}
}
