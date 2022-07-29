package de.dafuqs.spectrum.items.energy;

import de.dafuqs.spectrum.energy.InkStorageBlockEntity;
import de.dafuqs.spectrum.energy.InkStorageItem;
import de.dafuqs.spectrum.energy.storage.CreativeInkStorage;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class CreativeInkAssortmentItem extends Item implements InkStorageItem<CreativeInkStorage> {
	
	public CreativeInkAssortmentItem(Settings settings) {
		super(settings);
	}
	
	@Override
	public ActionResult useOnBlock(ItemUsageContext context) {
		World world = context.getWorld();
		if (!world.isClient) {
			BlockEntity blockEntity = world.getBlockEntity(context.getBlockPos());
			if (blockEntity instanceof InkStorageBlockEntity inkStorageBlockEntity) {
				inkStorageBlockEntity.getEnergyStorage().fillCompletely();
				inkStorageBlockEntity.setInkDirty();
				blockEntity.markDirty();
			}
		}
		return super.useOnBlock(context);
	}
	
	@Override
	public Drainability getDrainability() {
		return Drainability.ALWAYS;
	}
	
	@Override
	public CreativeInkStorage getEnergyStorage(ItemStack itemStack) {
		NbtCompound compound = itemStack.getNbt();
		if (compound != null && compound.contains("EnergyStore")) {
			return CreativeInkStorage.fromNbt(compound.getCompound("EnergyStore"));
		}
		return new CreativeInkStorage();
	}
	
	// Omitting this would crash outside the dev env o.O
	@Override
	public ItemStack getDefaultStack() {
		return super.getDefaultStack();
	}
	
	@Override
	public void setEnergyStorage(ItemStack itemStack, CreativeInkStorage storage) {
	}
	
	@Override
	@Environment(EnvType.CLIENT)
	public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
		super.appendTooltip(stack, world, tooltip, context);
		getEnergyStorage(stack).addTooltip(tooltip, true);
	}
	
}