package de.dafuqs.spectrum.items.tools;

import de.dafuqs.spectrum.items.Preenchanted;
import de.dafuqs.spectrum.items.SpectrumBannerPatternItem;
import de.dafuqs.spectrum.registries.SpectrumBannerPatterns;
import io.github.fablabsmc.fablabs.api.bannerpattern.v1.LoomPattern;
import io.github.fablabsmc.fablabs.api.bannerpattern.v1.LoomPatternProvider;
import net.minecraft.block.BlockState;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.*;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;

public class MultiToolItem extends SpectrumPickaxeItem implements Preenchanted, LoomPatternProvider {
	
	public MultiToolItem(ToolMaterial material, int attackDamage, float attackSpeed, Settings settings) {
		super(material, attackDamage, attackSpeed, settings);
	}
	
	@Override
	public Map<Enchantment, Integer> getDefaultEnchantments() {
		return Map.of(Enchantments.EFFICIENCY, 1);
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
	
	@Override
	public boolean isSuitableFor(BlockState state) {
		return true;
	}
	
	@Override
	public float getMiningSpeedMultiplier(ItemStack stack, BlockState state) {
		return miningSpeed;
	}
	
	/**
	 * Invoke shovel, axe and hoe right click actions (in this order)
	 * Like stripping logs, tilling grass paths etc.
	 * To get tilled earth it has to converted to path and then tilled again
	 */
	public ActionResult useOnBlock(ItemUsageContext context) {
		ActionResult actionResult = Items.IRON_SHOVEL.useOnBlock(context);
		if (actionResult == ActionResult.PASS) {
			actionResult = Items.IRON_AXE.useOnBlock(context);
			if (actionResult == ActionResult.PASS) {
				actionResult = Items.IRON_HOE.useOnBlock(context);
			}
		}
		return actionResult;
	}
	
	@Override
	public LoomPattern getPattern() {
		return SpectrumBannerPatterns.MULTITOOL;
	}
	
	@Override
	public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
		super.appendTooltip(stack, world, tooltip, context);
		SpectrumBannerPatternItem.addBannerPatternProviderTooltip(tooltip);
	}
	
}