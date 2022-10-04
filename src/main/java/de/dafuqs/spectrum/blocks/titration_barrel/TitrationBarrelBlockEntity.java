package de.dafuqs.spectrum.blocks.titration_barrel;

import de.dafuqs.spectrum.recipe.SpectrumRecipeTypes;
import de.dafuqs.spectrum.recipe.titration_barrel.ITitrationBarrelRecipe;
import de.dafuqs.spectrum.registries.SpectrumBlockEntities;
import dev.architectury.fluid.FluidStack;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Date;

public class TitrationBarrelBlockEntity extends BlockEntity {
	
	protected static final int CONTENT_SIZE = 5;
	protected SimpleInventory content = new SimpleInventory(CONTENT_SIZE);
	
	protected static final long MAX_WATER = FluidStack.bucketAmount() * 10;
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
		nbt.put("Inventory", this.content.toNbtList());
		nbt.putLong("SealTime", this.sealTime);
		nbt.putLong("TapTime", this.tapTime);
		nbt.putInt("WaterAmount", this.waterAmount);
		nbt.putInt("ExtractedBottles", this.extractedBottles);
	}
	
	public void readNbt(NbtCompound nbt) {
		super.readNbt(nbt);
		
		this.content = new SimpleInventory(CONTENT_SIZE);
		if(nbt.contains("Inventory", NbtElement.LIST_TYPE)) {
			this.content.readNbtList(nbt.getList("Inventory", NbtElement.COMPOUND_TYPE));
		}
		
		this.sealTime = nbt.contains("SealTime", NbtElement.LONG_TYPE) ? nbt.getLong("SealTime") : -1;
		this.tapTime = nbt.contains("TapTime", NbtElement.LONG_TYPE) ? nbt.getLong("TapTime") : -1;
		this.waterAmount = nbt.contains("WaterAmount", NbtElement.INT_TYPE) ? nbt.getInt("WaterAmount") : 0;
		this.extractedBottles = nbt.contains("ExtractedBottles", NbtElement.INT_TYPE) ? nbt.getInt("ExtractedBottles") : 0;
	}
	
	public Inventory getContent() {
		return content;
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
		
		world.getRecipeManager().getFirstMatch(SpectrumRecipeTypes.TITRATION_BARREL, this.content, world);
	}
	
	public void tap() {
		this.tapTime = new Date().getTime();
		markDirty();
	}
	
	public void reset() {
		this.sealTime = -1;
		this.tapTime = -1;
		this.extractedBottles = 0;
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
	
	public boolean isEmpty(World world, BlockPos pos) {
		return getExtractableBottleCount(world, pos) <= this.extractedBottles;
	}
	
	public int getExtractableBottleCount(World world, BlockPos pos) {
		long sealSeconds = getSealSeconds();
		if(sealSeconds > 0) {
			float localTemperature = world.getBiome(pos).value().getTemperature();
			return ITitrationBarrelRecipe.getYieldBottles(this.waterAmount, sealSeconds, localTemperature);
		}
		return 0;
	}
	
}
