package de.dafuqs.spectrum.blocks.crystallarieum;

import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.api.block.*;
import de.dafuqs.spectrum.api.energy.*;
import de.dafuqs.spectrum.api.energy.storage.*;
import de.dafuqs.spectrum.blocks.*;
import de.dafuqs.spectrum.helpers.*;
import de.dafuqs.spectrum.particle.*;
import de.dafuqs.spectrum.progression.*;
import de.dafuqs.spectrum.recipe.crystallarieum.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.block.*;
import net.minecraft.entity.*;
import net.minecraft.entity.player.*;
import net.minecraft.item.*;
import net.minecraft.nbt.*;
import net.minecraft.particle.*;
import net.minecraft.recipe.*;
import net.minecraft.server.network.*;
import net.minecraft.server.world.*;
import net.minecraft.sound.*;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import net.minecraft.world.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class CrystallarieumBlockEntity extends InWorldInteractionBlockEntity implements PlayerOwned, InkStorageBlockEntity<IndividualCappedInkStorage> {
	
	protected final static int CATALYST_SLOT_ID = 0;
	protected final static int INK_STORAGE_STACK_SLOT_ID = 1;
	protected final static int INVENTORY_SIZE = 2;
	
	public static final long INK_STORAGE_SIZE = 64 * 64 * 100;
	
	protected IndividualCappedInkStorage inkStorage;
	protected boolean inkDirty;
	
	@Nullable
	protected UUID ownerUUID;
	
	@Nullable
	protected CrystallarieumRecipe currentRecipe;
	protected CrystallarieumCatalyst currentCatalyst = CrystallarieumCatalyst.EMPTY;
	
	// for performance reasons, the crystallarieum only processes recipe logic all 20 ticks
	public static final int SECOND = 20;
	protected TickLooper tickLooper = new TickLooper(SECOND);
	
	protected int currentGrowthStageTicks;
	protected boolean canWork;
	
	public CrystallarieumBlockEntity(BlockPos pos, BlockState state) {
		super(SpectrumBlockEntities.CRYSTALLARIEUM, pos, state, INVENTORY_SIZE);
		this.inkStorage = new IndividualCappedInkStorage(INK_STORAGE_SIZE);
		this.canWork = true;
	}
	
	public static void clientTick(@NotNull World world, BlockPos blockPos, BlockState blockState, CrystallarieumBlockEntity crystallarieum) {
		if (crystallarieum.canWork && crystallarieum.currentRecipe != null) {
			ParticleEffect particleEffect = SpectrumParticleTypes.getSparkleRisingParticle(crystallarieum.currentRecipe.getInkColor().getDyeColor());
			
			int amount = 1 + crystallarieum.currentRecipe.getInkPerSecond();
			if (Support.getIntFromDecimalWithChance(amount / 80.0, world.random) > 0) {
				double randomX = world.getRandom().nextDouble() * 0.8;
				double randomZ = world.getRandom().nextDouble() * 0.8;
				world.addImportantParticle(particleEffect, blockPos.getX() + 0.1 + randomX, blockPos.getY() + 1, blockPos.getZ() + 0.1 + randomZ, 0.0D, 0.03D, 0.0D);
			}
		}
	}
	
	public static void serverTick(@NotNull World world, BlockPos blockPos, BlockState blockState, CrystallarieumBlockEntity crystallarieum) {
		if (crystallarieum.canWork) {
			transferInk(crystallarieum);
			
			if (crystallarieum.currentRecipe != null) {
				crystallarieum.tickLooper.tick();
				if (crystallarieum.tickLooper.reachedCap()) {
					tickRecipe(world, blockPos, crystallarieum);
					crystallarieum.tickLooper.reset();
				}
			}
		}
	}
	
	/**
	 * Progress the recipe
	 * gets called 1/second
	 */
	private static void tickRecipe(@NotNull World world, BlockPos blockPos, CrystallarieumBlockEntity crystallarieum) {
		if (crystallarieum.currentCatalyst == CrystallarieumCatalyst.EMPTY && !crystallarieum.currentRecipe.growsWithoutCatalyst()) {
			return;
		}
		
		// advance growing
		int consumedInk = (int) (crystallarieum.currentRecipe.getInkPerSecond() * crystallarieum.currentCatalyst.growthAccelerationMod * crystallarieum.currentCatalyst.inkConsumptionMod);
		if (crystallarieum.inkStorage.drainEnergy(crystallarieum.currentRecipe.getInkColor(), consumedInk) < consumedInk) {
			crystallarieum.canWork = false;
			crystallarieum.setInkDirty();
			crystallarieum.updateInClientWorld();
			return;
		}
		
		crystallarieum.setInkDirty();
		crystallarieum.currentGrowthStageTicks += SECOND * crystallarieum.currentCatalyst.growthAccelerationMod;
		
		// check if a catalyst should get used up
		if (world.random.nextFloat() < crystallarieum.currentCatalyst.consumeChancePerSecond) {
			ItemStack catalystStack = crystallarieum.getStack(CATALYST_SLOT_ID);
			catalystStack.decrement(1);
			crystallarieum.updateInClientWorld();
			if (catalystStack.isEmpty()) {
				crystallarieum.currentCatalyst = CrystallarieumCatalyst.EMPTY;
				if (!crystallarieum.currentRecipe.growsWithoutCatalyst()) {
					crystallarieum.canWork = false;
				}
			}
		}
		
		// advanced enough? grow!
		if (crystallarieum.currentGrowthStageTicks >= crystallarieum.currentRecipe.getSecondsPerGrowthStage() * SECOND) {
			BlockPos topPos = blockPos.up();
			BlockState topState = world.getBlockState(topPos);
			for (Iterator<BlockState> it = crystallarieum.currentRecipe.getGrowthStages().iterator(); it.hasNext(); ) {
				BlockState state = it.next();
				if (state.equals(topState)) {
					if (it.hasNext()) {
						BlockState targetState = it.next();
						world.setBlockState(topPos, targetState);
						
						// if the stone on top can not grow any further: pause
						if (!it.hasNext()) {
							crystallarieum.canWork = false;
						}
						
						ServerPlayerEntity owner = (ServerPlayerEntity) crystallarieum.getOwnerIfOnline();
						if (owner != null) {
							SpectrumAdvancementCriteria.CRYSTALLARIEUM_GROWING.trigger(owner, (ServerWorld) world, topPos, crystallarieum.getStack(CATALYST_SLOT_ID));
						}
					}
				}
			}
			crystallarieum.currentGrowthStageTicks = 0;
		}
	}
	
	private static void transferInk(CrystallarieumBlockEntity crystallarieum) {
		ItemStack inkStorageStack = crystallarieum.getStack(INK_STORAGE_STACK_SLOT_ID);
		if (inkStorageStack.getItem() instanceof InkStorageItem<?> inkStorageItem) {
			InkStorage itemInkStorage = inkStorageItem.getEnergyStorage(inkStorageStack);
			long transferredAmount = InkStorage.transferInk(itemInkStorage, crystallarieum.inkStorage);
			if (transferredAmount > 0) {
				inkStorageItem.setEnergyStorage(inkStorageStack, itemInkStorage);
			}
		}
	}
	
	@Override
	public void inventoryChanged() {
		this.currentCatalyst = this.currentRecipe == null ? CrystallarieumCatalyst.EMPTY : this.currentRecipe.getCatalyst(getStack(CATALYST_SLOT_ID));
		this.canWork = true;
		super.inventoryChanged();
	}
	
	@Override
	public void readNbt(NbtCompound nbt) {
		super.readNbt(nbt);
		
		if (nbt.contains("InkStorage", NbtElement.COMPOUND_TYPE)) {
			this.inkStorage = IndividualCappedInkStorage.fromNbt(nbt.getCompound("InkStorage"));
		}
		if (nbt.contains("Looper", NbtElement.COMPOUND_TYPE)) {
			this.tickLooper.readNbt(nbt.getCompound("Looper"));
		}
		this.canWork = nbt.getBoolean("CanWork");
		
		this.ownerUUID = PlayerOwned.readOwnerUUID(nbt);
		
		this.currentRecipe = null;
		this.currentCatalyst = CrystallarieumCatalyst.EMPTY;
		if (nbt.contains("CurrentRecipe")) {
			this.currentGrowthStageTicks = nbt.getInt("CurrentGrowthStageDuration");
			String recipeString = nbt.getString("CurrentRecipe");
			if (!recipeString.isEmpty() && SpectrumCommon.minecraftServer != null) {
				Optional<? extends Recipe<?>> optionalRecipe = SpectrumCommon.minecraftServer.getRecipeManager().get(new Identifier(recipeString));
				if (optionalRecipe.isPresent() && (optionalRecipe.get() instanceof CrystallarieumRecipe crystallarieumRecipe)) {
					this.currentRecipe = crystallarieumRecipe;
					this.currentCatalyst = this.currentRecipe.getCatalyst(getStack(CATALYST_SLOT_ID));
				}
			}
		} else {
			this.currentGrowthStageTicks = 0;
		}
	}
	
	@Override
	public void writeNbt(NbtCompound nbt) {
		super.writeNbt(nbt);
		nbt.put("InkStorage", this.inkStorage.toNbt());
		nbt.put("Looper", this.tickLooper.toNbt());
		
		nbt.putBoolean("CanWork", this.canWork);
		PlayerOwned.writeOwnerUUID(nbt, this.ownerUUID);
		if (this.currentRecipe != null) {
			nbt.putString("CurrentRecipe", this.currentRecipe.getId().toString());
			nbt.putInt("CurrentGrowthStageDuration", this.currentGrowthStageTicks);
		}
	}
	
	@Override
	public @Nullable UUID getOwnerUUID() {
		return this.ownerUUID;
	}
	
	@Override
	public void setOwner(PlayerEntity playerEntity) {
		this.ownerUUID = playerEntity.getUuid();
		markDirty();
	}
	
	/**
	 * Searches recipes for a valid one using itemStack and plants the first block of that recipe on top
	 *
	 * @param itemStack stack that is tried to plant on top, if a valid recipe
	 */
	public void acceptStack(ItemStack itemStack, boolean creative, @Nullable UUID player) {
		boolean changed = false;
		
		if (itemStack.getItem() instanceof InkStorageItem<?> inkStorageItem && inkStorageItem.getDrainability().canDrain(false)) {
			ItemStack currentInkStorageStack = getStack(INK_STORAGE_STACK_SLOT_ID);
			if (currentInkStorageStack.isEmpty()) {
				setStack(INK_STORAGE_STACK_SLOT_ID, itemStack.copy());
				if (!creative) {
					itemStack.setCount(0);
				}
				changed = true;
			}
		} else if (world.getBlockState(pos.up()).isAir()) {
			CrystallarieumRecipe recipe = CrystallarieumRecipe.getRecipeForStack(itemStack);
			if (recipe != null) {
				if (!creative) {
					itemStack.decrement(1);
				}
				BlockState placedState = recipe.getGrowthStages().get(0);
				world.setBlockState(pos.up(), placedState);
				onTopBlockChange(placedState, recipe);
				changed = true;
			}
		} else if (this.currentRecipe != null) {
			ItemStack currentCatalystStack = getStack(CATALYST_SLOT_ID);
			if (currentCatalystStack.isEmpty()) {
				CrystallarieumCatalyst catalyst = this.currentRecipe.getCatalyst(itemStack);
				if (catalyst != CrystallarieumCatalyst.EMPTY) {
					setStack(CATALYST_SLOT_ID, itemStack.copy());
					if (!creative) {
						itemStack.setCount(0);
					}
					this.currentCatalyst = catalyst;
					changed = true;
				}
			} else if (ItemStack.canCombine(currentCatalystStack, itemStack)) {
				InventoryHelper.combineStacks(currentCatalystStack, itemStack);
				changed = true;
			}
		}
		
		if (changed) {
			world.playSound(null, pos, SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.BLOCKS, 0.8F, 0.8F + world.random.nextFloat() * 0.6F);
			if (player != null) {
				this.ownerUUID = player;
			}
			inventoryChanged();
		}
	}
	
	/**
	 * Triggered when the block on top of the crystallarieum has changed
	 * Sets the new recipe matching that block state
	 *
	 * @param newState the new block state on top
	 * @param recipe   optionally the matching CrystallarieumRecipe. If null is passed it will be calculated
	 */
	public void onTopBlockChange(BlockState newState, @Nullable CrystallarieumRecipe recipe) {
		if (newState.isAir()) { // fast fail
			this.currentRecipe = null;
			this.canWork = false;
			markDirty();
			updateInClientWorld();
			
			world.setBlockState(pos, world.getBlockState(pos).with(CrystallarieumBlock.COLOR, NullableDyeColor.NONE));
		} else {
			this.currentRecipe = recipe == null ? CrystallarieumRecipe.getRecipeForState(newState) : recipe;
			
			if (this.currentRecipe == null) {
				world.setBlockState(pos, world.getBlockState(pos).with(CrystallarieumBlock.COLOR, NullableDyeColor.NONE));
			} else {
				world.setBlockState(pos, world.getBlockState(pos).with(CrystallarieumBlock.COLOR, NullableDyeColor.get(this.currentRecipe.getInkColor().getDyeColor())));
				
				ItemStack catalystStack = getStack(CATALYST_SLOT_ID);
				if (!catalystStack.isEmpty()) {
					this.currentCatalyst = this.currentRecipe.getCatalyst(catalystStack);
					if (this.currentCatalyst == CrystallarieumCatalyst.EMPTY) {
						ItemEntity itemEntity = new ItemEntity(world, this.getPos().getX() + 0.5, this.getPos().getY() + 1, this.getPos().getZ() + 0.5, catalystStack);
						this.setStack(CATALYST_SLOT_ID, ItemStack.EMPTY);
						world.spawnEntity(itemEntity);
					}
				}
			}
			
			inventoryChanged();
		}
	}
	
	@Override
	public IndividualCappedInkStorage getEnergyStorage() {
		return this.inkStorage;
	}
	
	@Override
	public void setInkDirty() {
		this.inkDirty = true;
	}
	
	@Override
	public boolean getInkDirty() {
		return this.inkDirty;
	}
	
	@Override
	public boolean isValid(int slot, ItemStack stack) {
		if (slot == INK_STORAGE_STACK_SLOT_ID) {
			return stack.getItem() instanceof InkStorageItem;
		} else if (this.currentRecipe != null) {
			return this.currentRecipe.getCatalyst(stack) != CrystallarieumCatalyst.EMPTY;
		}
		return false;
	}
	
}
