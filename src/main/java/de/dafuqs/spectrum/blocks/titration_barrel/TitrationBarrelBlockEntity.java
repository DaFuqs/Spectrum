package de.dafuqs.spectrum.blocks.titration_barrel;

import de.dafuqs.spectrum.helpers.*;
import de.dafuqs.spectrum.networking.*;
import de.dafuqs.spectrum.progression.*;
import de.dafuqs.spectrum.recipe.*;
import de.dafuqs.spectrum.recipe.titration_barrel.*;
import de.dafuqs.spectrum.registries.*;
import net.fabricmc.fabric.api.transfer.v1.fluid.*;
import net.fabricmc.fabric.api.transfer.v1.storage.base.*;
import net.minecraft.block.*;
import net.minecraft.block.entity.*;
import net.minecraft.entity.player.*;
import net.minecraft.inventory.*;
import net.minecraft.item.*;
import net.minecraft.nbt.*;
import net.minecraft.server.network.*;
import net.minecraft.sound.*;
import net.minecraft.text.*;
import net.minecraft.util.math.*;
import net.minecraft.world.*;
import net.minecraft.world.biome.*;
import org.jetbrains.annotations.*;

import java.util.*;

import static de.dafuqs.spectrum.blocks.titration_barrel.TitrationBarrelBlock.*;

public class TitrationBarrelBlockEntity extends BlockEntity {

	protected static final int INVENTORY_SIZE = 5;
	public static final int MAX_ITEM_COUNT = 64;
	protected SimpleInventory inventory = new SimpleInventory(INVENTORY_SIZE);

	public SingleVariantStorage<FluidVariant> fluidStorage = new SingleVariantStorage<>() {
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
			markDirty();
		}
	};

	// Times in milliseconds using the Date class
	protected long sealTime = -1;
	protected long tapTime = -1;

	protected String recipe;
	protected int extractedBottles = 0;

	public TitrationBarrelBlockEntity(BlockPos pos, BlockState state) {
		super(SpectrumBlockEntities.TITRATION_BARREL, pos, state);
	}
	
	protected void writeNbt(NbtCompound nbt) {
		super.writeNbt(nbt);
		nbt.put("Inventory", this.inventory.toNbtList());
		nbt.put("FluidVariant", this.fluidStorage.variant.toNbt());
		nbt.putLong("FluidAmount", this.fluidStorage.amount);
		nbt.putLong("SealTime", this.sealTime);
		nbt.putLong("TapTime", this.tapTime);
		nbt.putInt("ExtractedBottles", this.extractedBottles);
	}
	
	public void readNbt(NbtCompound nbt) {
		super.readNbt(nbt);

		this.inventory = new SimpleInventory(INVENTORY_SIZE);
		if (nbt.contains("Inventory", NbtElement.LIST_TYPE)) {
			this.inventory.readNbtList(nbt.getList("Inventory", NbtElement.COMPOUND_TYPE));
		}

		this.fluidStorage.variant = FluidVariant.fromNbt(nbt.getCompound("FluidVariant"));
		this.fluidStorage.amount = nbt.getLong("FluidAmount");
		this.sealTime = nbt.contains("SealTime", NbtElement.LONG_TYPE) ? nbt.getLong("SealTime") : -1;
		this.tapTime = nbt.contains("TapTime", NbtElement.LONG_TYPE) ? nbt.getLong("TapTime") : -1;
		this.extractedBottles = nbt.contains("ExtractedBottles", NbtElement.INT_TYPE) ? nbt.getInt("ExtractedBottles") : 0;
	}
	
	public Inventory getInventory() {
		return inventory;
	}
	
	public void seal() {
		this.sealTime = new Date().getTime();
		this.markDirty();
		world.getRecipeManager().getFirstMatch(SpectrumRecipeTypes.TITRATION_BARREL, this.inventory, world);
	}
	
	public void tap() {
		this.tapTime = new Date().getTime();
		this.markDirty();
	}
	
	public void reset(World world, BlockPos blockPos, BlockState state) {
		this.sealTime = -1;
		this.tapTime = -1;
		this.fluidStorage.amount = 0;
		this.extractedBottles = 0;
		this.inventory.clear();
		
		world.setBlockState(pos, state.with(BARREL_STATE, TitrationBarrelBlock.BarrelState.EMPTY));
		world.playSound(null, blockPos, SoundEvents.BLOCK_BARREL_OPEN, SoundCategory.BLOCKS, 1.0F, 1.0F);
		markDirty();
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
		return (int) getSealMilliseconds() / 1000 / 60 / 20;
	}
	
	public String getSealRealDays() {
		return Support.getWithOneDecimalAfterComma(getSealMilliseconds() / 1000 / 60 / 20 / 72);
	}
	
	private boolean isEmpty(float temperature, int extractedBottles, ITitrationBarrelRecipe recipe) {
		if (recipe.isEmpty() || !getFluidVariant().isOf(recipe.getFluidInput())) {
			return true;
		}
		return extractedBottles >= recipe.getOutputCountAfterAngelsShare(temperature, getSealSeconds());
	}
	
	public void addDayOfSealTime() {
		this.sealTime -= TimeHelper.EPOCH_DAY_MILLIS;
		this.markDirty();
	}
	
	public ItemStack tryHarvest(World world, BlockPos blockPos, BlockState blockState, ItemStack handStack, @Nullable PlayerEntity player) {
		ItemStack harvestedStack = ItemStack.EMPTY;
		Biome biome = world.getBiome(blockPos).value();
		
		Optional<ITitrationBarrelRecipe> optionalRecipe = getRecipeForInventory(world);
		if (optionalRecipe.isEmpty()) {
			if (player != null) {
				if (inventory.isEmpty() && getFluidVariant().isBlank()) {
					SpectrumS2CPacketSender.sendHudMessage((ServerPlayerEntity) player, Text.translatable("block.spectrum.titration_barrel.empty_when_tapping"), false);
				} else {
					SpectrumS2CPacketSender.sendHudMessage((ServerPlayerEntity) player, Text.translatable("block.spectrum.titration_barrel.invalid_recipe_when_tapping"), false);
				}
			}
		} else {
			ITitrationBarrelRecipe recipe = optionalRecipe.get();
			if (this.getFluidVariant().isOf(recipe.getFluidInput()) && recipe.canPlayerCraft(player)) {
				Item tappingItem = recipe.getTappingItem();
				
				boolean canTap = tappingItem == Items.AIR;
				if (!canTap) {
					if (handStack.isOf(tappingItem)) {
						handStack.decrement(1);
						canTap = true;
					} else if (player != null) {
						SpectrumS2CPacketSender.sendHudMessage((ServerPlayerEntity) player, Text.translatable("block.spectrum.titration_barrel.tapping_item_required").append(tappingItem.getName()), false);
					}
				}
				if (canTap) {
					long secondsFermented = (this.tapTime - this.sealTime) / 1000;
					harvestedStack = recipe.tap(this.inventory, secondsFermented, biome.getDownfall(), biome.getTemperature());
					this.extractedBottles += 1;
				}
			} else if (player != null) {
				if (getFluidVariant().isBlank()) {
					SpectrumS2CPacketSender.sendHudMessage((ServerPlayerEntity) player, Text.translatable("block.spectrum.titration_barrel.missing_liquid_when_tapping"), false);
				} else {
					SpectrumS2CPacketSender.sendHudMessage((ServerPlayerEntity) player, Text.translatable("block.spectrum.titration_barrel.invalid_recipe_when_tapping"), false);
				}
			}
		}
		
		if (player != null) {
			int daysSealed = getSealMinecraftDays();
			int inventoryCount = InventoryHelper.countItemsInInventory(this.inventory);
			SpectrumAdvancementCriteria.TITRATION_BARREL_TAPPING.trigger((ServerPlayerEntity) player, harvestedStack, daysSealed, inventoryCount);
		}
		
		if (optionalRecipe.isEmpty() || isEmpty(biome.getTemperature(), this.extractedBottles, optionalRecipe.get()) || !optionalRecipe.get().canPlayerCraft(player)) {
			reset(world, blockPos, blockState);
		}
		
		this.markDirty();
		
		return harvestedStack;
	}
	
	public Optional<ITitrationBarrelRecipe> getRecipeForInventory(World world) {
		return world.getRecipeManager().getFirstMatch(SpectrumRecipeTypes.TITRATION_BARREL, this.inventory, world);
	}
	
	public void giveRecipeRemainders(PlayerEntity player) {
		for (ItemStack stack : this.inventory.stacks) {
			Item item = stack.getItem();
			Item remainderItem = item.getRecipeRemainder();
			if (remainderItem != null) {
				ItemStack remainderStack = remainderItem.getDefaultStack();
				remainderStack.setCount(stack.getCount());
				player.getInventory().offerOrDrop(remainderStack);
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

}
