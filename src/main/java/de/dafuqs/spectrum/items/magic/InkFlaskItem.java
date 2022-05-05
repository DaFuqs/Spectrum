package de.dafuqs.spectrum.items.magic;

import de.dafuqs.spectrum.SpectrumCommon;
import de.dafuqs.spectrum.energy.PigmentEnergyStorageItem;
import de.dafuqs.spectrum.energy.color.CMYKColor;
import de.dafuqs.spectrum.energy.storage.SinglePigmentEnergyStorage;
import de.dafuqs.spectrum.items.trinkets.SpectrumTrinketItem;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class InkFlaskItem extends SpectrumTrinketItem implements PigmentEnergyStorageItem<SinglePigmentEnergyStorage> {
	
	private final long maxEnergy;
	
	public InkFlaskItem(Settings settings, long maxEnergy) {
		super(settings, new Identifier(SpectrumCommon.MOD_ID, "progression/unlock_pigment_flask"));
		this.maxEnergy = maxEnergy;
	}
	
	@Override
	public SinglePigmentEnergyStorage getEnergyStorage(ItemStack itemStack) {
		NbtCompound compound = itemStack.getNbt();
		if(compound != null && compound.contains("EnergyStore")) {
			return SinglePigmentEnergyStorage.fromNbt(compound.getCompound("EnergyStore"));
		}
		return new SinglePigmentEnergyStorage(this.maxEnergy);
	}
	
	@Override
	public void setEnergyStorage(ItemStack itemStack, SinglePigmentEnergyStorage storage) {
		NbtCompound compound = itemStack.getOrCreateNbt();
		compound.put("EnergyStore", storage.toNbt());
	}
	
	@Override
	@Environment(EnvType.CLIENT)
	public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
		super.appendTooltip(stack, world, tooltip, context);
		getEnergyStorage(stack).addTooltip(world, tooltip, context);
	}
	
	public ItemStack getFullStack(CMYKColor color) {
		ItemStack stack = this.getDefaultStack();
		SinglePigmentEnergyStorage storage = getEnergyStorage(stack);
		storage.fillCompletely();
		storage.convertColor(color);
		setEnergyStorage(stack, storage);
		return stack;
	}
	
}