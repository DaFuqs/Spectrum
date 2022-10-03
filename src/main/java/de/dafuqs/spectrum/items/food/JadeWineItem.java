package de.dafuqs.spectrum.items.food;

import de.dafuqs.spectrum.registries.SpectrumItems;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;

import java.util.List;

public class JadeWineItem extends Item {
	
	public record JadeWineProperties(long ageDays, float alcPercent, float bloominess, boolean sweetened) { }
	
	public JadeWineItem(Settings settings) {
		super(settings);
	}
	
	public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
		ItemStack itemStack = super.finishUsing(stack, world, user);
		// TODO: do stuff here
		
		return user instanceof PlayerEntity && ((PlayerEntity)user).getAbilities().creativeMode ? itemStack : new ItemStack(Items.GLASS_BOTTLE);
	}
	
	public static ItemStack getWineStack(JadeWineProperties properties) {
		ItemStack stack = SpectrumItems.JADE_WINE.getDefaultStack();
		NbtCompound compound = stack.getOrCreateNbt();
		compound.putFloat("AgeDays", properties.ageDays);
		compound.putFloat("AlcPercent", properties.alcPercent);
		compound.putFloat("Bloominess", properties.bloominess);
		compound.putBoolean("Sweetened", properties.sweetened);
		stack.setNbt(compound);
		return stack;
	}
	
	public static JadeWineProperties getProperties(ItemStack itemStack) {
		long agedSeconds = 0;
		float alcPercent = 0;
		float bloominess = 0;
		boolean sweetened = false;
		
		NbtCompound nbtCompound = itemStack.getNbt();
		if(nbtCompound != null) {
			agedSeconds = nbtCompound.contains("AgedMinutes") ? nbtCompound.getLong("AgedMinutes") : 0;
			alcPercent = nbtCompound.contains("AlcPercent") ? nbtCompound.getFloat("AlcPercent") : 0;
			bloominess = nbtCompound.contains("Bloominess") ? nbtCompound.getFloat("Bloominess") : 0;
			sweetened = nbtCompound.contains("Sweetened") && nbtCompound.getBoolean("Sweetened");
		}
		
		return new JadeWineProperties(agedSeconds, alcPercent, bloominess, sweetened);
	}
	
	@Override
	public void appendTooltip(ItemStack itemStack, World world, List<Text> tooltip, TooltipContext tooltipContext) {
		super.appendTooltip(itemStack, world, tooltip, tooltipContext);
		
		JadeWineProperties properties = getProperties(itemStack);
		tooltip.add(new TranslatableText("item.spectrum.jade_wine.tooltip.alc_percent", properties.alcPercent).formatted(Formatting.GRAY));
		tooltip.add(new TranslatableText("item.spectrum.jade_wine.tooltip.bloominess", properties.bloominess).formatted(Formatting.GRAY));
		tooltip.add(new TranslatableText("item.spectrum.jade_wine.tooltip.age_days", properties.ageDays).formatted(Formatting.GRAY));
		if(properties.sweetened) {
			tooltip.add(new TranslatableText("item.spectrum.jade_wine.tooltip.sweetened").formatted(Formatting.GRAY).formatted(Formatting.ITALIC));
		}
	}
	
}
