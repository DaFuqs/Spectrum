package de.dafuqs.spectrum.items.trinkets;

import de.dafuqs.spectrum.SpectrumCommon;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class RadiancePinItem extends SpectrumTrinketItem {
	
	public static final int RANGE = 16;
	public static final int EFFECT_DURATION = 240;
	public static final long COOLDOWN_TICKS = 160;
	
    private final Identifier UNLOCK_IDENTIFIER = new Identifier(SpectrumCommon.MOD_ID, "midgame/spectrum_midgame");

	public RadiancePinItem(Settings settings) {
		super(settings);
	}
	
	@Override
    protected Identifier getUnlockIdentifier() {
		return UNLOCK_IDENTIFIER;
	}
	
	@Override
	public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
		super.appendTooltip(stack, world, tooltip, context);
		tooltip.add(new TranslatableText("item.spectrum.radiance_pin.tooltip").formatted(Formatting.GRAY));
	}
	
}