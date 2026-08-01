package de.dafuqs.spectrum.items.food;

import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.helpers.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.*;
import net.minecraft.network.chat.*;
import net.minecraft.resources.*;
import net.minecraft.util.*;
import net.minecraft.world.effect.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.item.*;
import net.minecraft.world.level.*;

import java.util.*;

public class StarCandyItem extends Item {
	
	public static final ResourceLocation UNLOCK_IDENTIFIER = SpectrumCommon.locate("unlocks/food/star_candy");
	
	protected Rarity rarity;
	
	public StarCandyItem(Properties settings, Rarity rarity) {
		super(settings);
		this.rarity = rarity;
	}
	
	@Override
	public Component getName(ItemStack stack) {
		if(this.rarity == Rarity.MAGNIFICENT) {
			return Component.translatable(this.getDescriptionId(stack)).withStyle(ChatFormatting.GOLD);
		} else {
			return super.getName(stack);
		}
	}
	
	@Override
	public ItemStack finishUsingItem(ItemStack stack, Level world, LivingEntity user) {
		ItemStack itemStack = super.finishUsingItem(stack, world, user);
		
		switch(this.rarity) {
			case GLEAMING ->  {
				if (!world.isClientSide()) {
					MobEffectHelper.clearRandomEffect(user, instance -> instance.getEffect().value().getCategory() == MobEffectCategory.HARMFUL);
				}
			}
			case ENCHANTED ->  {
				if (!world.isClientSide()) {
					MobEffectHelper.clearEffects(user, instance -> instance.getEffect().value().getCategory() == MobEffectCategory.HARMFUL);
				}
			}
			case MAGNIFICENT -> {
				user.heal(user.getMaxHealth());
				if (!world.isClientSide()) {
					MobEffectHelper.clearEffects(user, instance -> instance.getEffect().value().getCategory() == MobEffectCategory.HARMFUL);
				}
				if (user instanceof Player player) {
					player.getFoodData().eat(1000, 1.0F);
				}
			}
		}
		
		return itemStack;
	}

	@Override
	public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag type) {
		super.appendHoverText(stack, context, tooltip, type);
		
		switch(this.rarity) {
			case SUGARY ->  {
				tooltip.add(Component.translatable("item.spectrum.star_candy.tooltip.sugary").withStyle(ChatFormatting.GRAY));
			}
			case GLEAMING ->  {
				tooltip.add(Component.translatable("item.spectrum.star_candy.tooltip.cure_single_effect").withStyle(ChatFormatting.GRAY));
			}
			case ENCHANTED ->  {
				tooltip.add(Component.translatable("item.spectrum.star_candy.tooltip.cure_all_effects").withStyle(ChatFormatting.GRAY));
			}
			case MAGNIFICENT -> {
				tooltip.add(Component.translatable("item.spectrum.star_candy.tooltip.cure_all_effects").withStyle(ChatFormatting.GRAY));
				tooltip.add(Component.translatable("item.spectrum.star_candy.tooltip.fully_heal_and_hunger").withStyle(ChatFormatting.GRAY));
			}
		}
	}
	
	@Override
	public boolean isFoil(ItemStack stack) {
		return this.rarity.isFoil;
	}

    public enum Rarity {
        SUGARY(false, 0.0,   -0.20),
        MELLOW (false, 0.9,  -0.1),
		GLEAMING(false, 0.1,  0.05),
		ENCHANTED(true, 0.01,  0.02),
        MAGNIFICENT(true, 0.0,    0.01);
        
        private final boolean isFoil;
        private final double baseChance;
        private final double luckMod;
        
        Rarity(boolean isFoil, double baseChance, double luckMod) {
            this.isFoil = isFoil;
            this.baseChance = baseChance;
            this.luckMod = luckMod;
        }
        
        public double weight(double luck) {
            return Math.max(0.0, baseChance + luckMod * luck);
        }
		
		public Item getItem() {
			switch (this) {
				case SUGARY -> {
					return SpectrumItems.SUGARY_STAR_CANDY.get();
				}
				case MELLOW -> {
					return SpectrumItems.MELLOW_STAR_CANDY.get();
				}
				case GLEAMING -> {
					return SpectrumItems.GLEAMING_STAR_CANDY.get();
				}
				case ENCHANTED -> {
					return SpectrumItems.ENCHANTED_STAR_CANDY.get();
				}
				default -> {
					return SpectrumItems.MAGNIFICENT_STAR_CANDY.get();
				}
			}
		}
		
		// TODO: can we use a WeightedRandomList or similar instead?
		public static StarCandyItem.Rarity roll(RandomSource random, double luck) {
			StarCandyItem.Rarity[] values = StarCandyItem.Rarity.values();
			double[] weights = new double[values.length];
			
			double sum = 0.0;
			for (int i = 0; i < values.length; i++) {
				weights[i] = values[i].weight(luck);
				sum += weights[i];
			}
			
			double r = random.nextDouble() * sum;
			double cumulative = 0.0;
			for (int i = 0; i < values.length; i++) {
				cumulative += weights[i];
				if (r <= cumulative) {
					return values[i];
				}
			}
			
			// should never happen
			// but what do I know
			return Rarity.MELLOW;
		}
		
    }
	
}
