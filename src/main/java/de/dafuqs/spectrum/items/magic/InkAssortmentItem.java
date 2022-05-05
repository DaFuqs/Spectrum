package de.dafuqs.spectrum.items.magic;

import de.dafuqs.spectrum.SpectrumCommon;
import de.dafuqs.spectrum.energy.PigmentEnergyStorageItem;
import de.dafuqs.spectrum.energy.storage.ArtistsPaletteEnergyStorage;
import de.dafuqs.spectrum.energy.storage.IndividualCappedSimplePigmentEnergyStorage;
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

public class InkAssortmentItem extends SpectrumTrinketItem implements PigmentEnergyStorageItem<IndividualCappedSimplePigmentEnergyStorage> {
	
	private final long maxEnergy;
	
	public InkAssortmentItem(Settings settings, long maxEnergy) {
		super(settings, new Identifier(SpectrumCommon.MOD_ID, "progression/unlock_pigment_flask"));
		this.maxEnergy = maxEnergy;
	}
	
	@Override
	public IndividualCappedSimplePigmentEnergyStorage getEnergyStorage(ItemStack itemStack) {
		NbtCompound compound = itemStack.getNbt();
		if(compound != null && compound.contains("EnergyStore")) {
			return IndividualCappedSimplePigmentEnergyStorage.fromNbt(compound.getCompound("EnergyStore"));
		}
		return new IndividualCappedSimplePigmentEnergyStorage(this.maxEnergy);
	}
	
	@Override
	public void setEnergyStorage(ItemStack itemStack, IndividualCappedSimplePigmentEnergyStorage storage) {
		NbtCompound compound = itemStack.getOrCreateNbt();
		compound.put("EnergyStore", storage.toNbt());
	}
	
	@Override
	@Environment(EnvType.CLIENT)
	public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
		super.appendTooltip(stack, world, tooltip, context);
		getEnergyStorage(stack).addTooltip(world, tooltip, context);
	}
	
}