package de.dafuqs.spectrum.items.tools;

import de.dafuqs.spectrum.SpectrumCommon;
import de.dafuqs.spectrum.entity.entity.BedrockFishingBobberEntity;
import de.dafuqs.spectrum.entity.entity.MoltenFishingBobberEntity;
import de.dafuqs.spectrum.items.Preenchanted;
import de.dafuqs.spectrum.registries.SpectrumEnchantments;
import de.dafuqs.spectrum.registries.SpectrumFluidTags;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;

public class MoltenRodItem extends SpectrumFishingRodItem implements Preenchanted {
	
	public static final Identifier UNLOCK_IDENTIFIER = SpectrumCommon.locate("progression/unlock_molten_rod");
	
	public MoltenRodItem(Settings settings) {
		super(settings);
	}
	
	@Override
	public boolean canFishIn(FluidState fluidState) {
		return fluidState.isIn(SpectrumFluidTags.MOLTEN_ROD_FISHABLE_IN);
	}
	
	@Override
	public void spawnBobber(PlayerEntity user, World world, int luckOfTheSeaLevel, int lureLevel, int exuberanceLevel, int bigCatchLevel, boolean inventoryInsertion, boolean foundry) {
		world.spawnEntity(new MoltenFishingBobberEntity(user, world, luckOfTheSeaLevel, lureLevel, exuberanceLevel, bigCatchLevel, inventoryInsertion, foundry));
	}
	
	@Override
	public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
		super.appendTooltip(stack, world, tooltip, context);
		tooltip.add(new TranslatableText("item.spectrum.molten_rod.tooltip").formatted(Formatting.GRAY));
		tooltip.add(new TranslatableText("item.spectrum.molten_rod.tooltip2").formatted(Formatting.GRAY));
	}
	
	public Map<Enchantment, Integer> getDefaultEnchantments() {
		return Map.of(SpectrumEnchantments.FOUNDRY, 1);
	}
	
	@Override
	public ItemStack getDefaultStack() {
		return getDefaultEnchantedStack(this);
	}
	
	@Override
	public void appendStacks(ItemGroup group, DefaultedList<ItemStack> stacks) {
		if (this.isIn(group)) {
			stacks.add(getDefaultEnchantedStack(this));
		}
	}
	
}