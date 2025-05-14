package de.dafuqs.spectrum.blocks.titration_barrel;

import de.dafuqs.spectrum.api.block.*;
import de.dafuqs.spectrum.helpers.*;
import de.dafuqs.spectrum.mixin.accessors.*;
import de.dafuqs.spectrum.progression.*;
import de.dafuqs.spectrum.recipe.*;
import de.dafuqs.spectrum.recipe.titration_barrel.*;
import de.dafuqs.spectrum.registries.*;
import net.fabricmc.fabric.api.transfer.v1.fluid.*;
import net.fabricmc.fabric.api.transfer.v1.storage.base.*;
import net.minecraft.core.*;
import net.minecraft.nbt.*;
import net.minecraft.network.chat.*;
import net.minecraft.server.level.*;
import net.minecraft.sounds.*;
import net.minecraft.world.*;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.biome.*;
import net.minecraft.world.level.block.entity.*;
import net.minecraft.world.level.block.state.*;
import net.minecraft.world.level.material.*;
import org.jetbrains.annotations.*;

import java.util.*;

import static de.dafuqs.spectrum.blocks.titration_barrel.TitrationBarrelBlock.*;

public class TitrationBarrelBlockEntity extends BlockEntity implements FluidStackInventory {
	
	protected static final int INVENTORY_SIZE = 5;
	public static final int MAX_ITEM_COUNT = 64;
	protected NonNullList<ItemStack> items;
	protected SingleVariantStorage<FluidVariant> fluidStorage = new SingleVariantStorage<>() {
		@Override
		protected FluidVariant getBlankVariant() {
			return FluidVariant.blank();
		}
		
		@Override
		protected long getCapacity(FluidVariant variant) {
			return FluidConstants.BUCKET;
		}
		
		@Override
		protected void onFinalCommit() {
			super.onFinalCommit();
			inventoryChanged();
		}
	};
	
	@Override
	public NonNullList<ItemStack> getItems() {
		return this.items;
	}
	
	@Override
	public SingleVariantStorage<FluidVariant> getFluidStorage() {
		return this.fluidStorage;
	}
	
	// Times in milliseconds using the Date class
	protected long sealTime = -1;
	protected long tapTime = -1;
	
	protected String recipe;
	protected int extractedBottles = 0;
	
	public TitrationBarrelBlockEntity(BlockPos pos, BlockState state) {
		super(SpectrumBlockEntities.TITRATION_BARREL, pos, state);
		this.items = NonNullList.withSize(INVENTORY_SIZE, ItemStack.EMPTY);
	}
	
	@Override
	protected void saveAdditional(CompoundTag nbt, HolderLookup.Provider registryLookup) {
		super.saveAdditional(nbt, registryLookup);
		ContainerHelper.saveAllItems(nbt, items, registryLookup);
		CodecHelper.writeNbt(nbt, "FluidVariant", FluidVariant.CODEC, this.fluidStorage.variant);
		nbt.putLong("FluidAmount", this.fluidStorage.amount);
		nbt.putLong("SealTime", this.sealTime);
		nbt.putLong("TapTime", this.tapTime);
		nbt.putInt("ExtractedBottles", this.extractedBottles);
	}
	
	@Override
	public void loadAdditional(CompoundTag nbt, HolderLookup.Provider registryLookup) {
		super.loadAdditional(nbt, registryLookup);
		
		this.items = NonNullList.withSize(INVENTORY_SIZE, ItemStack.EMPTY);
		ContainerHelper.loadAllItems(nbt, items, registryLookup);
		this.fluidStorage.variant = CodecHelper.fromNbt(FluidVariant.CODEC, nbt.get("FluidVariant"), FluidVariant.blank());
		this.fluidStorage.amount = nbt.getLong("FluidAmount");
		this.sealTime = nbt.contains("SealTime", Tag.TAG_LONG) ? nbt.getLong("SealTime") : -1;
		this.tapTime = nbt.contains("TapTime", Tag.TAG_LONG) ? nbt.getLong("TapTime") : -1;
		this.extractedBottles = nbt.contains("ExtractedBottles", Tag.TAG_ANY_NUMERIC) ? nbt.getInt("ExtractedBottles") : 0;
	}
	
	public void seal() {
		this.sealTime = new Date().getTime();
		this.setChanged();
	}
	
	public void tap() {
		this.tapTime = new Date().getTime();
		this.setChanged();
	}
	
	public void reset(Level world, BlockPos blockPos, BlockState state) {
		this.sealTime = -1;
		this.tapTime = -1;
		this.fluidStorage.variant = FluidVariant.blank();
		this.fluidStorage.amount = 0;
		this.extractedBottles = 0;
		this.getItems().clear();
		
		world.setBlockAndUpdate(worldPosition, state.setValue(BARREL_STATE, TitrationBarrelBlock.BarrelState.EMPTY));
		world.playSound(null, blockPos, SoundEvents.BARREL_OPEN, SoundSource.BLOCKS, 1.0F, 1.0F);
		setChanged();
	}
	
	public long getSealMilliseconds() {
		if (this.sealTime == -1) {
			return 0;
		}
		
		long tapTime;
		if (this.tapTime == -1) {
			tapTime = new Date().getTime();
		} else {
			tapTime = this.tapTime;
		}
		return tapTime - this.sealTime;
	}
	
	public long getSealSeconds() {
		return getSealMilliseconds() / 1000;
	}
	
	public int getSealMinecraftDays() {
		return (int) (getSealMilliseconds() / 1000 / 60 / 20);
	}
	
	public String getSealRealDays() {
		return Support.getWithOneDecimalAfterComma(getSealMilliseconds() / 1000F / 60 / 20 / 72);
	}
	
	private boolean isEmpty(float temperature, int extractedBottles, ITitrationBarrelRecipe recipe) {
		if (level == null || !recipe.getFluidInput().test(getFluidVariant())) {
			return true;
		}
		return extractedBottles >= recipe.getOutputCountAfterAngelsShare(this.level, temperature, getSealSeconds());
	}
	
	public void addOneDayOfSealTime() {
		this.sealTime -= TimeHelper.EPOCH_DAY_MILLIS;
		this.setChanged();
	}
	
	public ItemStack tryHarvest(Level world, BlockPos blockPos, BlockState blockState, ItemStack handStack, @Nullable Player player) {
		ItemStack harvestedStack = ItemStack.EMPTY;
		Biome biome = world.getBiome(blockPos).value();
		
		boolean shouldReset = false;
		Component message = null;
		
		int daysSealed = getSealMinecraftDays();
		int inventoryCount = InventoryHelper.countItemsInInventory(this.getItems());
		
		Optional<RecipeHolder<ITitrationBarrelRecipe>> optionalRecipe = getRecipeForInventory(world);
		if (optionalRecipe.isEmpty()) {
			if (getItems().isEmpty() && getFluidVariant().isBlank()) {
				message = Component.translatable("block.spectrum.titration_barrel.empty_when_tapping");
			} else {
				message = Component.translatable("block.spectrum.titration_barrel.invalid_recipe_when_tapping");
			}
			shouldReset = true;
		} else {
			ITitrationBarrelRecipe recipe = optionalRecipe.get().value();
			
			long secondsFermented = (this.tapTime - this.sealTime) / 1000;
			var output = recipe.getOutputCountAfterAngelsShare(world, biome.getBaseTemperature(), secondsFermented);
			
			if (recipe.getFluidInput().test(this.getFluidVariant())) {
				if (recipe.canPlayerCraft(player)) {
					boolean canTap = true;
					Item tappingItem = recipe.getTappingItem();
					if (tappingItem != Items.AIR) {
						if (handStack.is(tappingItem)) {
							output = Math.min(output, handStack.getCount());
							handStack.shrink(output);
						} else {
							message = Component.translatable("block.spectrum.titration_barrel.tapping_item_required").append(tappingItem.getDescription());
							canTap = false;
						}
					}
					if (canTap) {
						float downfall = ((BiomeAccessor) (Object) biome).getClimateSettings().downfall();
						harvestedStack = recipe.getTitrationResult(this, secondsFermented, downfall);
						harvestedStack.setCount(output);
						
						this.extractedBottles += output;
						shouldReset = isEmpty(biome.getBaseTemperature(), this.extractedBottles, recipe);
					}
				} else {
					message = Component.translatable("block.spectrum.titration_barrel.recipe_not_unlocked");
				}
			} else {
				if (getFluidVariant().isBlank()) {
					message = Component.translatable("block.spectrum.titration_barrel.missing_liquid_when_tapping");
				} else {
					message = Component.translatable("block.spectrum.titration_barrel.invalid_recipe_when_tapping");
				}
				shouldReset = true;
			}
		}
		
		if (player != null) {
			SpectrumAdvancementCriteria.TITRATION_BARREL_TAPPING.trigger((ServerPlayer) player, harvestedStack, daysSealed, inventoryCount);
			
			if (message != null) {
				player.displayClientMessage(message, true);
			}
		}
		
		if (shouldReset) {
			reset(world, blockPos, blockState);
		}
		
		this.setChanged();
		
		return harvestedStack;
	}
	
	public Optional<RecipeHolder<ITitrationBarrelRecipe>> getRecipeForInventory(Level world) {
		return world.getRecipeManager().getRecipeFor(SpectrumRecipeTypes.TITRATION_BARREL, getRecipeInput(), world);
	}
	
	public StorageRecipeInput<SingleVariantStorage<FluidVariant>> getRecipeInput() {
		return new StorageRecipeInput<>(items, fluidStorage);
	}
	
	public void giveRecipeRemainders(Player player) {
		for (ItemStack stack : this.getItems()) {
			ItemStack remainder = stack.getRecipeRemainder();
			if (!remainder.isEmpty()) {
				player.getInventory().placeItemBackInInventory(remainder);
			}
		}
	}
	
	public @NotNull FluidVariant getFluidVariant() {
		if (this.fluidStorage.amount > 0) {
			return this.fluidStorage.variant;
		} else {
			return FluidVariant.blank();
		}
	}
	
	public boolean canBeSealed(Player player) {
		int itemCount = InventoryHelper.countItemsInInventory(getItems());
		Fluid fluid = fluidStorage.variant.getFluid();
		if (itemCount == 0 && fluid == Fluids.EMPTY) {
			return true; // tap empty barrel advancement
		}
		
		if (level != null) {
			Optional<RecipeHolder<ITitrationBarrelRecipe>> optionalRecipe = getRecipeForInventory(level);
			return optionalRecipe.isPresent()
					&& optionalRecipe.get().value().canPlayerCraft(player)
					&& optionalRecipe.get().value().getFluidInput().test(this.getFluidVariant().getFluid());
		}
		
		return false;
	}
	
}
