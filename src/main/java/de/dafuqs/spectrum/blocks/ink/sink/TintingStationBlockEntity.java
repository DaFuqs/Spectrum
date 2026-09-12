package de.dafuqs.spectrum.blocks.ink.sink;

import de.dafuqs.spectrum.api.ink.color.*;
import de.dafuqs.spectrum.api.ink.storage.*;
import de.dafuqs.spectrum.blocks.ink.*;
import de.dafuqs.spectrum.blocks.ink.gen.*;
import de.dafuqs.spectrum.components.*;
import de.dafuqs.spectrum.config.*;
import de.dafuqs.spectrum.helpers.*;
import de.dafuqs.spectrum.inventories.*;
import de.dafuqs.spectrum.networking.s2c_payloads.*;
import de.dafuqs.spectrum.particle.effect.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.core.*;
import net.minecraft.core.component.*;
import net.minecraft.nbt.*;
import net.minecraft.network.*;
import net.minecraft.network.chat.*;
import net.minecraft.server.level.*;
import net.minecraft.sounds.*;
import net.minecraft.tags.*;
import net.minecraft.util.*;
import net.minecraft.world.*;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.*;
import net.minecraft.world.item.alchemy.*;
import net.minecraft.world.item.component.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.state.*;
import net.minecraft.world.phys.*;

import java.util.*;

public class TintingStationBlockEntity extends InkSinkBlockEntity implements MenuProvider {
	
	public static final int INPUT_SLOT_ID = 0;
	public static final int OUTPUT_SLOT_ID = 1;
	
	public static final long TICKS_PER_CONVERSION = 5;
	public static final long ITEM_COLORING_COST = 10;
	
	public TintingStationBlockEntity(BlockPos blockPos, BlockState blockState) {
		super(SpectrumBlockEntities.TINTING_STATION.get(), blockPos, blockState, 4, 2, INPUT_SLOT_ID);
	}
	
	public boolean shouldTickLogic(Level world) {
		return world.getGameTime() % TICKS_PER_CONVERSION == 0;
	}
	
	public boolean tickLogic(Level level) {
		ItemStack input = this.getItem(INPUT_SLOT_ID);
		if (!input.isEmpty()) {
			Optional<Holder<InkColor>> inkColorHolder = this.getSelectedColor();
			InkStorage inkStorage = this.getInkStorage();
			
			if (inkColorHolder.isPresent()) {
				InkColor selectedInkColor = inkColorHolder.get().value();
				if (inkStorage.getEnergy(selectedInkColor) >= ITEM_COLORING_COST) {
					ItemStack output = this.getItem(OUTPUT_SLOT_ID);
					if (output.getCount() < output.getMaxStackSize()) {
						ItemStack resultStack = this.colorStack(input, selectedInkColor);
						if (resultStack.isEmpty()) {
							resultStack = tintStack(input, selectedInkColor);
						}
						if (resultStack.isEmpty()) {
							resultStack = tintPotion(input, selectedInkColor);
						}
						
						if (!resultStack.isEmpty()) {
							if (output.isEmpty()) {
								input.shrink(1);
								this.setItem(OUTPUT_SLOT_ID, resultStack);
							} else if (ItemStack.isSameItemSameComponents(output, resultStack)) {
								input.shrink(1);
								output.grow(1);
							}
							
							inkStorage.addEnergy(selectedInkColor, -ITEM_COLORING_COST);
							
							if (SpectrumConfig.CONFIG.BlockSoundVolume.get() > 0) {
								level.playSound(null, this.getBlockPos(), SpectrumSoundEvents.COLOR_PICKER_PROCESSING, SoundSource.BLOCKS, SpectrumConfig.CONFIG.BlockSoundVolume.get().floatValue() / 3F, 1.0F);
								
								PlayParticleWithRandomOffsetAndVelocityPayload.playParticleWithRandomOffsetAndVelocity((ServerLevel) this.getLevel(),
										new Vec3(this.getBlockPos().getX() + 0.5, this.getBlockPos().getY() + 0.7, this.getBlockPos().getZ() + 0.5),
										ColoredFluidRisingParticleEffect.of(selectedInkColor.getColorInt()),
										5,
										new Vec3(0.22, 0.0, 0.22),
										new Vec3(0.0, 0.1, 0.0)
								);
							}
							return true;
						}
					}
				}
			}
		}
		return false;
	}
	
	// Cleaned up version of net.minecraft.world.item.component.DyedItemColor
	// modified to work with InkColor
	protected static ItemStack tintStack(ItemStack stack, InkColor inkColor) {
		if (!stack.is(ItemTags.DYEABLE)) {
			return ItemStack.EMPTY;
		}
		
		ItemStack result = stack.copyWithCount(1);
		int i = 0;
		int j = 0;
		int k = 0;
		int prevMax = 0;
		int i1 = 0;
		DyedItemColor dyeditemcolor = result.get(DataComponents.DYED_COLOR);
		if (dyeditemcolor != null) {
			int prevRed = FastColor.ARGB32.red(dyeditemcolor.rgb());
			int prevGreen = FastColor.ARGB32.green(dyeditemcolor.rgb());
			int prevBlue = FastColor.ARGB32.blue(dyeditemcolor.rgb());
			prevMax += Math.max(prevRed, Math.max(prevGreen, prevBlue));
			i += prevRed;
			j += prevGreen;
			k += prevBlue;
			i1++;
		}
		
		int j3 = inkColor.getColorInt();
		int i2 = FastColor.ARGB32.red(j3);
		int j2 = FastColor.ARGB32.green(j3);
		int k2 = FastColor.ARGB32.blue(j3);
		prevMax += Math.max(i2, Math.max(j2, k2));
		i += i2;
		j += j2;
		k += k2;
		i1++;
		
		int finalRed = i / i1;
		int finalGreen = j / i1;
		int finalBlue = k / i1;
		float f = (float)prevMax / (float)i1;
		float f1 = (float)Math.max(finalRed, Math.max(finalGreen, finalBlue));
		finalRed = (int)((float)finalRed * f / f1);
		finalGreen = (int)((float)finalGreen * f / f1);
		finalBlue = (int)((float)finalBlue * f / f1);
		int finalColor = FastColor.ARGB32.color(0, finalRed, finalGreen, finalBlue);
		boolean flag = dyeditemcolor == null || dyeditemcolor.showInTooltip();
		result.set(DataComponents.DYED_COLOR, new DyedItemColor(finalColor, flag));
		return result;
	}
	
	protected static ItemStack tintPotion(ItemStack stack, InkColor inkColor) {
		PotionContents pc = stack.get(DataComponents.POTION_CONTENTS);
		if(pc == null) {
			return ItemStack.EMPTY;
		}
		int newColor = inkColor.getColorInt();
		if(pc.customColor().isPresent() && pc.customColor().get() == newColor) {
			return ItemStack.EMPTY;
		}
		
		ItemStack result = stack.copyWithCount(1);
		PotionContents newContents = new PotionContents(pc.potion(), Optional.of(newColor), pc.customEffects());
		result.set(DataComponents.POTION_CONTENTS, newContents);
		return result;
	}
	
	public ItemStack colorStack(ItemStack stack, InkColor inkColor) {
		Item resultItem = VariantHelper.getColoredItem(stack, inkColor);
		if(resultItem == null) {
			return ItemStack.EMPTY;
		}
		
		ItemStack resultStack = resultItem.getDefaultInstance();
		resultStack.applyComponents(stack.getComponents());
		return resultStack;
	}
	
	@Override
	protected Component getDefaultName() {
		return Component.translatable("block.spectrum.tinting_station");
	}
	
	@Override
	protected AbstractContainerMenu createMenu(int syncId, Inventory playerInventory) {
		return new TintingStationScreenHandler(syncId, playerInventory, this, this.selectedColor);
	}
	
	@Override
	public boolean canPlaceItem(int slot, ItemStack stack) {
		if (slot == OUTPUT_SLOT_ID) {
			return stack.getItem() instanceof InkStorageItem<?>;
		}
		return true;
	}
	
}
