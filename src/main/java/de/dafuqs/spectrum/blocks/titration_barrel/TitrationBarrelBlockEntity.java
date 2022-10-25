package de.dafuqs.spectrum.blocks.titration_barrel;

import de.dafuqs.spectrum.helpers.InventoryHelper;
import de.dafuqs.spectrum.helpers.TimeHelper;
import de.dafuqs.spectrum.progression.SpectrumAdvancementCriteria;
import de.dafuqs.spectrum.recipe.SpectrumRecipeTypes;
import de.dafuqs.spectrum.recipe.titration_barrel.ITitrationBarrelRecipe;
import de.dafuqs.spectrum.registries.SpectrumBlockEntities;
import dev.architectury.fluid.FluidStack;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import org.jetbrains.annotations.Nullable;

import java.util.Date;
import java.util.Optional;

import static de.dafuqs.spectrum.blocks.titration_barrel.TitrationBarrelBlock.BARREL_STATE;

public class TitrationBarrelBlockEntity extends BlockEntity {
	
	protected static final int CONTENT_SIZE = 5;
	public static final int MAX_ITEM_COUNT = 64;
	protected SimpleInventory inventory = new SimpleInventory(CONTENT_SIZE);
	
	protected static final long MAX_WATER = FluidStack.bucketAmount() * 8;
	protected int waterAmount = 0;
	
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
		nbt.putLong("SealTime", this.sealTime);
		nbt.putLong("TapTime", this.tapTime);
		nbt.putInt("WaterAmount", this.waterAmount);
		nbt.putInt("ExtractedBottles", this.extractedBottles);
	}
	
	public void readNbt(NbtCompound nbt) {
		super.readNbt(nbt);
		
		this.inventory = new SimpleInventory(CONTENT_SIZE);
		if(nbt.contains("Inventory", NbtElement.LIST_TYPE)) {
			this.inventory.readNbtList(nbt.getList("Inventory", NbtElement.COMPOUND_TYPE));
		}
		
		this.sealTime = nbt.contains("SealTime", NbtElement.LONG_TYPE) ? nbt.getLong("SealTime") : -1;
		this.tapTime = nbt.contains("TapTime", NbtElement.LONG_TYPE) ? nbt.getLong("TapTime") : -1;
		this.waterAmount = nbt.contains("WaterAmount", NbtElement.INT_TYPE) ? nbt.getInt("WaterAmount") : 0;
		this.extractedBottles = nbt.contains("ExtractedBottles", NbtElement.INT_TYPE) ? nbt.getInt("ExtractedBottles") : 0;
	}
	
	public Inventory getInventory() {
		return inventory;
	}
	
	public boolean addWaterBucket() {
		if(this.waterAmount < MAX_WATER) {
			this.waterAmount += FluidStack.bucketAmount();
			markDirty();
			return true;
		}
		return false;
	}
	
	public void seal() {
		this.sealTime = new Date().getTime();
		markDirty();
		world.getRecipeManager().getFirstMatch(SpectrumRecipeTypes.TITRATION_BARREL, this.inventory, world);
	}
	
	public void tap() {
		this.tapTime = new Date().getTime();
		markDirty();
	}
	
	public void reset(World world, BlockPos blockPos, BlockState state) {
		this.sealTime = -1;
		this.tapTime = -1;
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
	
	public boolean isEmpty(float localTemperature) {
		return getExtractableBottleCount(localTemperature) <= this.extractedBottles;
	}
	
	public int getExtractableBottleCount(World world, BlockPos blockPos) {
		Biome biome = world.getBiome(blockPos).value();
		return getExtractableBottleCount(biome.getTemperature());
	}
	
	public int getExtractableBottleCount(float localTemperature) {
		long sealSeconds = getSealSeconds();
		if(sealSeconds > 0) {
			return ITitrationBarrelRecipe.getYieldBottles(this.waterAmount, sealSeconds, localTemperature);
		}
		return 0;
	}
	
	public void addDayOfSealTime() {
		this.sealTime -= TimeHelper.EPOCH_DAY_MILLIS;
		this.markDirty();
	}
	
	public ItemStack tryHarvest(World world, BlockPos blockPos, BlockState blockState, ItemStack harvestingStack, @Nullable PlayerEntity player) {
		ItemStack harvestedStack = ItemStack.EMPTY;
		Biome biome = world.getBiome(blockPos).value();
		
		if(this.waterAmount == 0) {
			if(player != null) {
				player.sendMessage(new TranslatableText("block.spectrum.titration_barrel.missing_water_when_tapping"), false);
			}
		} else {
			Optional<ITitrationBarrelRecipe> recipe = world.getRecipeManager().getFirstMatch(SpectrumRecipeTypes.TITRATION_BARREL, this.inventory, world);
			if (recipe.isPresent()) {
				Item tappingItem = recipe.get().getTappingItem();
				if (tappingItem != Items.AIR) {
					if (harvestingStack.isOf(tappingItem)) {
						harvestingStack.decrement(1);
					} else {
						if (player != null) {
							player.sendMessage(new TranslatableText("block.spectrum.titration_barrel.tapping_item_required").append(tappingItem.getName()), false);
						}
						return ItemStack.EMPTY;
					}
				}
				
				long secondsFermented = (this.tapTime - this.sealTime) / 1000;
				harvestedStack = recipe.get().tap(this.inventory, (int) (waterAmount / FluidStack.bucketAmount()), secondsFermented, biome.getDownfall(), biome.getTemperature());
			} else {
				player.sendMessage(new TranslatableText("block.spectrum.titration_barrel.invalid_recipe_when_tapping"), false);
			}
			
			this.extractedBottles += 1;
		}
		
		if(player != null) {
			int daysSealed = getSealMinecraftDays();
			int inventoryCount = InventoryHelper.countItemsInInventory(this.inventory);
			SpectrumAdvancementCriteria.TITRATION_BARREL_TAPPING.trigger((ServerPlayerEntity) player, harvestedStack, daysSealed, inventoryCount);
		}
		
		if(isEmpty(biome.getTemperature())) {
			reset(world, blockPos, blockState);
		}

		this.markDirty();
		
		return harvestedStack;
	}
}
