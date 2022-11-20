package de.dafuqs.spectrum.blocks.titration_barrel;

import de.dafuqs.spectrum.helpers.InventoryHelper;
import de.dafuqs.spectrum.helpers.TimeHelper;
import de.dafuqs.spectrum.progression.SpectrumAdvancementCriteria;
import de.dafuqs.spectrum.recipe.SpectrumRecipeTypes;
import de.dafuqs.spectrum.recipe.titration_barrel.ITitrationBarrelRecipe;
import de.dafuqs.spectrum.registries.SpectrumBlockEntities;
import net.fabricmc.fabric.mixin.transfer.BucketItemAccessor;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.Fluids;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.*;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Date;
import java.util.Optional;

import static de.dafuqs.spectrum.blocks.titration_barrel.TitrationBarrelBlock.BARREL_STATE;

public class TitrationBarrelBlockEntity extends BlockEntity {
	
	protected static final int INVENTORY_SIZE = 5;
	public static final int MAX_ITEM_COUNT = 64;
	protected SimpleInventory inventory = new SimpleInventory(INVENTORY_SIZE);
	protected @NotNull Fluid storedFluid = Fluids.EMPTY;
	
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
		nbt.putString("Fluid", Registry.FLUID.getId(this.storedFluid).toString());
		nbt.putLong("SealTime", this.sealTime);
		nbt.putLong("TapTime", this.tapTime);
		nbt.putInt("ExtractedBottles", this.extractedBottles);
	}
	
	public void readNbt(NbtCompound nbt) {
		super.readNbt(nbt);
		
		this.inventory = new SimpleInventory(INVENTORY_SIZE);
		if(nbt.contains("Inventory", NbtElement.LIST_TYPE)) {
			this.inventory.readNbtList(nbt.getList("Inventory", NbtElement.COMPOUND_TYPE));
		}
		
		this.storedFluid = Registry.FLUID.get(Identifier.tryParse(nbt.getString("Fluid")));
		this.sealTime = nbt.contains("SealTime", NbtElement.LONG_TYPE) ? nbt.getLong("SealTime") : -1;
		this.tapTime = nbt.contains("TapTime", NbtElement.LONG_TYPE) ? nbt.getLong("TapTime") : -1;
		this.extractedBottles = nbt.contains("ExtractedBottles", NbtElement.INT_TYPE) ? nbt.getInt("ExtractedBottles") : 0;
	}
	
	public Inventory getInventory() {
		return inventory;
	}
	
	public boolean useBucket(World world, BlockPos pos, BlockState state, ItemStack bucketStack, PlayerEntity player, Hand hand) {
		Fluid bucketFluid = ((BucketItemAccessor) bucketStack.getItem()).fabric_getFluid();
		if(this.storedFluid == Fluids.EMPTY && bucketFluid != Fluids.EMPTY) {
			player.setStackInHand(hand, ItemUsage.exchangeStack(bucketStack, player, BucketItem.getEmptiedStack(bucketStack, player)));
			
			Optional<SoundEvent> soundEvent = bucketFluid.getBucketFillSound();
			soundEvent.ifPresent(event -> world.playSound(null, this.pos, event, SoundCategory.PLAYERS, 0.8F, 0.8F + world.random.nextFloat() * 0.6F));
			
			this.storedFluid = bucketFluid;
			this.markDirty();
			
			if (state.get(BARREL_STATE) == TitrationBarrelBlock.BarrelState.EMPTY) {
				world.setBlockState(pos, state.with(BARREL_STATE, TitrationBarrelBlock.BarrelState.FILLED));
			}
			
			return true;
		} else if(this.sealTime == -1 && this.storedFluid != Fluids.EMPTY && bucketFluid == Fluids.EMPTY) {
			player.setStackInHand(hand, ItemUsage.exchangeStack(bucketStack, player, this.storedFluid.getBucketItem().getDefaultStack()));
			
			Optional<SoundEvent> soundEvent = storedFluid.getBucketFillSound();
			soundEvent.ifPresent(event -> world.playSound(null, this.pos, event, SoundCategory.PLAYERS, 0.8F, 0.8F + world.random.nextFloat() * 0.6F));
			
			if (state.get(BARREL_STATE) == TitrationBarrelBlock.BarrelState.FILLED && inventory.isEmpty()) {
				world.setBlockState(pos, state.with(BARREL_STATE, TitrationBarrelBlock.BarrelState.EMPTY));
			}
			
			this.storedFluid = Fluids.EMPTY;
			this.markDirty();
			return true;
		}
		return false;
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
		this.storedFluid = Fluids.EMPTY;
		this.extractedBottles = 0;
		this.inventory.clear();
		
		world.setBlockState(pos, state.with(BARREL_STATE, TitrationBarrelBlock.BarrelState.EMPTY));
		world.playSound(null, blockPos, SoundEvents.BLOCK_BARREL_OPEN, SoundCategory.BLOCKS, 1.0F, 1.0F);
	}
	
	public long getSealMilliseconds() {
		if(this.sealTime == -1) {
			return 0;
		}
		
		long tapTime;
		if(this.tapTime == -1) {
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
	
	private boolean isEmpty(float temperature, int extractedBottles, ITitrationBarrelRecipe recipe) {
		if(recipe.isEmpty() || recipe.getFluid() != this.storedFluid) {
			return true;
		}
		return extractedBottles >= recipe.getOutputCountAfterAngelsShare(temperature, getSealSeconds());
	}
	
	public int getExtractableBottleCount(World world, BlockPos blockPos, ITitrationBarrelRecipe recipe) {
		Biome biome = world.getBiome(blockPos).value();
		return recipe.getOutputCountAfterAngelsShare(biome.getTemperature(), getSealSeconds());
	}
	
	public void addDayOfSealTime() {
		this.sealTime -= TimeHelper.EPOCH_DAY_MILLIS;
		this.markDirty();
	}
	
	public ItemStack tryHarvest(World world, BlockPos blockPos, BlockState blockState, ItemStack handStack, @Nullable PlayerEntity player) {
		ItemStack harvestedStack = ItemStack.EMPTY;
		Biome biome = world.getBiome(blockPos).value();
		
		Optional<ITitrationBarrelRecipe> optionalRecipe = world.getRecipeManager().getFirstMatch(SpectrumRecipeTypes.TITRATION_BARREL, this.inventory, world);
		if (optionalRecipe.isEmpty()) {
			if (player != null) {
				if(inventory.isEmpty() && storedFluid == Fluids.EMPTY) {
					player.sendMessage(new TranslatableText("block.spectrum.titration_barrel.empty_when_tapping"), false);
				} else {
					player.sendMessage(new TranslatableText("block.spectrum.titration_barrel.invalid_recipe_when_tapping"), false);
				}
			}
		} else {
			ITitrationBarrelRecipe recipe = optionalRecipe.get();
			if (this.storedFluid == recipe.getFluid() && recipe.canPlayerCraft(player)) {
				Item tappingItem = recipe.getTappingItem();
				
				boolean canTap = tappingItem == Items.AIR;
				if(!canTap) {
					if(handStack.isOf(tappingItem)) {
						handStack.decrement(1);
						canTap = true;
					} else if (player != null) {
						player.sendMessage(new TranslatableText("block.spectrum.titration_barrel.tapping_item_required").append(tappingItem.getName()), false);
					}
				}
				if (canTap) {
					long secondsFermented = (this.tapTime - this.sealTime) / 1000;
					harvestedStack = recipe.tap(this.inventory, secondsFermented, biome.getDownfall(), biome.getTemperature());
					this.extractedBottles += 1;
				}
			} else if (player != null) {
				if (this.storedFluid == Fluids.EMPTY) {
					player.sendMessage(new TranslatableText("block.spectrum.titration_barrel.missing_water_when_tapping"), false);
				} else {
					player.sendMessage(new TranslatableText("block.spectrum.titration_barrel.invalid_recipe_when_tapping"), false);
				}
			}
		}
		
		if(player != null) {
			int daysSealed = getSealMinecraftDays();
			int inventoryCount = InventoryHelper.countItemsInInventory(this.inventory);
			SpectrumAdvancementCriteria.TITRATION_BARREL_TAPPING.trigger((ServerPlayerEntity) player, harvestedStack, daysSealed, inventoryCount);
		}
		
		if(optionalRecipe.isEmpty() || isEmpty(biome.getTemperature(), this.extractedBottles, optionalRecipe.get()) || !optionalRecipe.get().canPlayerCraft(player)) {
			reset(world, blockPos, blockState);
		}

		this.markDirty();
		
		return harvestedStack;
	}
	
}
