package de.dafuqs.spectrum.blocks.ink.gen;

import de.dafuqs.spectrum.api.ink.color.*;
import de.dafuqs.spectrum.api.ink.storage.*;
import de.dafuqs.spectrum.config.*;
import de.dafuqs.spectrum.inventories.*;
import de.dafuqs.spectrum.networking.s2c_payloads.*;
import de.dafuqs.spectrum.particle.effect.*;
import de.dafuqs.spectrum.recipe.color_picker.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.core.*;
import net.minecraft.network.*;
import net.minecraft.network.chat.*;
import net.minecraft.server.level.*;
import net.minecraft.sounds.*;
import net.minecraft.world.*;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.state.*;
import net.minecraft.world.phys.*;
import org.jspecify.annotations.*;

import java.util.*;

public class ColorPickerBlockEntity extends InkGeneratorBlockEntity implements MenuProvider {
	
	public static final int INPUT_SLOT_ID = 1;
	
	public static final long TICKS_PER_CONVERSION = 5;
	protected @Nullable RecipeHolder<InkConvertingRecipe> cachedRecipe;
	
	public ColorPickerBlockEntity(BlockPos blockPos, BlockState blockState) {
		super(SpectrumBlockEntities.COLOR_PICKER.get(), blockPos, blockState, 3, 2);
	}
	
	@Override
	public boolean shouldTickLogic(Level level) {
		return level.getGameTime() % TICKS_PER_CONVERSION == 0;
	}
	
	@Override
	public boolean tickLogic(Level level) {
		return tryConvertPigmentToEnergy((ServerLevel) level);
	}
	
	@Override
	protected Component getDefaultName() {
		return Component.translatable("block.spectrum.color_picker");
	}
	
	@Override
	protected AbstractContainerMenu createMenu(int syncId, Inventory playerInventory) {
		return new ColorPickerScreenHandler(syncId, playerInventory, this, this.selectedColor);
	}
	
	@Override
	public void writeClientSideData(AbstractContainerMenu menu, RegistryFriendlyByteBuf buffer) {
		ColorPickerScreenHandler.ScreenOpeningData.STREAM_CODEC.encode(buffer, new ColorPickerScreenHandler.ScreenOpeningData(this.worldPosition, this.selectedColor));
	}
	
	protected boolean tryConvertPigmentToEnergy(ServerLevel world) {
		InkConvertingRecipe recipe = getInkConvertingRecipe(world);
		if (recipe != null) {
			InkColor inkColor = recipe.getInkColor();
			long amount = recipe.getInkAmount();
			if (amount <= this.inkStorage.getRoom(inkColor)) {
				inventory.get(INPUT_SLOT_ID).shrink(1);
				this.inkStorage.addEnergy(inkColor, amount);
				
				if (SpectrumConfig.CONFIG.BlockSoundVolume.get() > 0) {
					level.playSound(null, worldPosition, SpectrumSoundEvents.COLOR_PICKER_PROCESSING, SoundSource.BLOCKS, SpectrumConfig.CONFIG.BlockSoundVolume.get().floatValue() / 3F, 1.0F);
				}
				PlayParticleWithRandomOffsetAndVelocityPayload.playParticleWithRandomOffsetAndVelocity(world,
						new Vec3(worldPosition.getX() + 0.5, worldPosition.getY() + 0.7, worldPosition.getZ() + 0.5),
						ColoredFluidRisingParticleEffect.of(inkColor.getColorInt()),
						5,
						new Vec3(0.22, 0.0, 0.22),
						new Vec3(0.0, 0.1, 0.0)
				);
				
				setChanged();
				
				return true;
			}
		}
		return false;
	}
	
	protected @Nullable InkConvertingRecipe getInkConvertingRecipe(Level world) {
		// is the current stack empty?
		ItemStack inputStack = inventory.get(INPUT_SLOT_ID);
		
		// search matching recipe
		Optional<RecipeHolder<InkConvertingRecipe>> recipe = world.getRecipeManager().getRecipeFor(SpectrumRecipeTypes.INK_CONVERTING, new SingleRecipeInput(inputStack), world, cachedRecipe);
		if (recipe.isPresent()) {
			this.cachedRecipe = recipe.get();
			return this.cachedRecipe.value();
		} else {
			this.cachedRecipe = null;
			return null;
		}
	}
	
	@Override
	public boolean canPlaceItem(int slot, ItemStack stack) {
		if (slot == INPUT_SLOT_ID) {
			return InkConvertingRecipe.isInput(stack.getItem());
		}
		return super.canPlaceItem(slot, stack);
	}
	
}
