package de.dafuqs.spectrum.items;

import de.dafuqs.spectrum.SpectrumCommon;
import de.dafuqs.spectrum.items.conditional.CloakedItem;
import de.dafuqs.spectrum.registries.SpectrumBannerPatterns;
import io.github.fablabsmc.fablabs.api.bannerpattern.v1.LoomPattern;
import io.github.fablabsmc.fablabs.api.bannerpattern.v1.LoomPatternProvider;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.text.Text;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class PigmentItem extends CloakedItem implements LoomPatternProvider {
	
	protected final DyeColor color;
	
	public PigmentItem(Settings settings, DyeColor color) {
		super(settings, new Identifier(SpectrumCommon.MOD_ID, "craft_colored_sapling"), getDyeItemForDyeColor(color));
		this.color = color;
	}
	
	public static Item getDyeItemForDyeColor(DyeColor dyeColor) {
		switch (dyeColor) {
			case BLACK -> {
				return Items.BLACK_DYE;
			}
			case BLUE -> {
				return Items.BLUE_DYE;
			}
			case BROWN -> {
				return Items.BROWN_DYE;
			}
			case CYAN -> {
				return Items.CYAN_DYE;
			}
			case GRAY -> {
				return Items.GRAY_DYE;
			}
			case GREEN -> {
				return Items.GREEN_DYE;
			}
			case LIGHT_BLUE -> {
				return Items.LIGHT_BLUE_DYE;
			}
			case LIGHT_GRAY -> {
				return Items.LIGHT_GRAY_DYE;
			}
			case LIME -> {
				return Items.LIME_DYE;
			}
			case MAGENTA -> {
				return Items.MAGENTA_DYE;
			}
			case ORANGE -> {
				return Items.ORANGE_DYE;
			}
			case PINK -> {
				return Items.PINK_DYE;
			}
			case PURPLE -> {
				return Items.PURPLE_DYE;
			}
			case RED -> {
				return Items.RED_DYE;
			}
			case WHITE -> {
				return Items.WHITE_DYE;
			}
			default -> {
				return Items.YELLOW_DYE;
			}
		}
	}
	
	public DyeColor getColor() {
		return this.color;
	}
	
	@Override
	public LoomPattern getPattern() {
		return SpectrumBannerPatterns.PIGMENT;
	}
	
	@Override
	public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
		super.appendTooltip(stack, world, tooltip, context);
		SpectrumBannerPatternItem.addBannerPatternProviderTooltip(tooltip);
	}
	
}
