package de.dafuqs.spectrum.blocks.crystallarieum;

import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.blocks.*;
import de.dafuqs.spectrum.energy.*;
import de.dafuqs.spectrum.energy.storage.*;
import de.dafuqs.spectrum.helpers.*;
import de.dafuqs.spectrum.interfaces.*;
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
	
	protected final static int INVENTORY_SIZE = 2;
	protected final static int CATALYST_SLOT_ID = 0;
	protected final static int INK_STORAGE_STACK_SLOT_ID = 1;
	
	public static final long INK_STORAGE_SIZE = 64 * 64 * 100;
	protected IndividualCappedInkStorage inkStorage;
	protected boolean inkDirty;
	
	@Nullable
	protected UUID ownerUUID;
	
	@Nullable
	protected CrystallarieumRecipe currentRecipe;
	protected CrystallarieumCatalyst currentCatalyst;
	protected int currentGrowthStageDuration;
	protected boolean canWork;
	
	public CrystallarieumBlockEntity(BlockPos pos, BlockState state) {
		super(SpectrumBlockEntities.CRYSTALLARIEUM, pos, state, INVENTORY_SIZE);
		this.inkStorage = new IndividualCappedInkStorage(INK_STORAGE_SIZE);
		this.canWork = true;
	}
	
	public static void clientTick(@NotNull World world, BlockPos blockPos, BlockState blockState, CrystallarieumBlockEntity crystallarieum) {
		if (crystallarieum.canWork && crystallarieum.currentRecipe != null) {
			int amount = crystallarieum.currentRecipe.getInkPerSecond();
			if (amount > 0) {
				ParticleEffect particleEffect = SpectrumParticleTypes.getSparkleRisingParticle(crystallarieum.currentRecipe.getInkColor().getDyeColor());
				
				if (amount > 199 || Support.getIntFromDecimalWithChance(amount / 200.0, world.random) > 0) {
					double randomX = world.getRandom().nextDouble() * 0.8;
					double randomZ = world.getRandom().nextDouble() * 0.8;
					world.addParticle(particleEffect, blockPos.getX() + 0.1 + randomX, blockPos.getY() + 1, blockPos.getZ() + 0.1 + randomZ, 0.0D, 0.03D, 0.0D);
				}
			}
		}
	}
	
	public static void serverTick(@NotNull World world, BlockPos blockPos, BlockState blockState, CrystallarieumBlockEntity crystallarieum) {
		if (world.getTime() % 20 == 0 && crystallarieum.canWork && crystallarieum.currentRecipe != null) {
			// transfer ink
			ItemStack inkStorageStack = crystallarieum.getStack(INK_STORAGE_STACK_SLOT_ID);
			if(inkStorageStack.getItem() instanceof InkStorageItem inkStorageItem) {
				InkStorage itemInkStorage = inkStorageItem.getEnergyStorage(inkStorageStack);
				long transferredAmount = InkStorage.transferInk(itemInkStorage, crystallarieum.inkStorage);
				if (transferredAmount > 0) {
					inkStorageItem.setEnergyStorage(inkStorageStack, itemInkStorage);
				}
			}
			
			// advance growing
			if (crystallarieum.currentCatalyst == null) {
				if (!crystallarieum.currentRecipe.growsWithoutCatalyst()) {
					return;
				}
				
				// running without catalyst
				int consumedInk = crystallarieum.currentRecipe.getInkPerSecond();
				if (crystallarieum.inkStorage.drainEnergy(crystallarieum.currentRecipe.getInkColor(), consumedInk) < consumedInk) {
					crystallarieum.canWork = false;
					crystallarieum.setInkDirty();
					crystallarieum.updateInClientWorld();
					return;
				}
				
				crystallarieum.setInkDirty();
				crystallarieum.currentGrowthStageDuration += 20;
			} else {
				// running with catalyst
				int consumedInk = (int) Math.ceil(crystallarieum.currentRecipe.getInkPerSecond() * crystallarieum.currentCatalyst.inkConsumptionMod);
				if (crystallarieum.inkStorage.drainEnergy(crystallarieum.currentRecipe.getInkColor(), consumedInk) < consumedInk) {
					crystallarieum.canWork = false;
					crystallarieum.setInkDirty();
					crystallarieum.updateInClientWorld();
					return;
				}
				
				crystallarieum.setInkDirty();
				crystallarieum.currentGrowthStageDuration += 20 * crystallarieum.currentCatalyst.growthAccelerationMod;
				
				// check if a catalyst should get used up
				if (world.random.nextFloat() < crystallarieum.currentCatalyst.consumeChancePerSecond) {
					ItemStack catalystStack = crystallarieum.getStack(CATALYST_SLOT_ID);
					catalystStack.decrement(1);
					if (catalystStack.isEmpty()) {
						crystallarieum.currentCatalyst = null;
						if (!crystallarieum.currentRecipe.growsWithoutCatalyst()) {
							crystallarieum.canWork = false;
						}
					}
				}
			}
			
			// advanced enough? grow!
			if (crystallarieum.currentGrowthStageDuration >= crystallarieum.currentRecipe.getSecondsPerGrowthStage() * 20) {
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
								crystallarieum.updateInClientWorld();
							}
							
							ServerPlayerEntity owner = (ServerPlayerEntity) crystallarieum.getOwnerIfOnline();
							if (owner != null) {
								SpectrumAdvancementCriteria.CRYSTALLARIEUM_GROWING.trigger(owner, (ServerWorld) world, topPos, crystallarieum.getStack(CATALYST_SLOT_ID));
							}
						}
					}
				}
				crystallarieum.currentGrowthStageDuration = 0;
			}
			
		}
	}
	
	@Override
	public void inventoryChanged() {
		if(this.currentRecipe != null) {
			Optional<CrystallarieumCatalyst> optionalCatalyst = this.currentRecipe.getCatalyst(getStack(CATALYST_SLOT_ID));
			optionalCatalyst.ifPresent(crystallarieumCatalyst -> this.currentCatalyst = crystallarieumCatalyst);
		}
		
		this.canWork = true;
		super.inventoryChanged();
	}
	
	@Override
	public void readNbt(NbtCompound nbt) {
		super.readNbt(nbt);
		
		if (nbt.contains("InkStorage", NbtElement.COMPOUND_TYPE)) {
			this.inkStorage = IndividualCappedInkStorage.fromNbt(nbt.getCompound("InkStorage"));
		}
		this.currentGrowthStageDuration = nbt.getShort("CraftingTime");
		this.canWork = nbt.getBoolean("CanWork");
		
		if (nbt.contains("OwnerUUID")) {
			this.ownerUUID = nbt.getUuid("OwnerUUID");
		} else {
			this.ownerUUID = null;
		}
		
		this.currentRecipe = null;
		if (nbt.contains("CurrentRecipe")) {
			this.currentGrowthStageDuration = nbt.getInt("CurrentGrowthStageDuration");
			String recipeString = nbt.getString("CurrentRecipe");
			if (!recipeString.isEmpty() && SpectrumCommon.minecraftServer != null) {
				Optional<? extends Recipe> optionalRecipe = SpectrumCommon.minecraftServer.getRecipeManager().get(new Identifier(recipeString));
				if (optionalRecipe.isPresent() && (optionalRecipe.get() instanceof CrystallarieumRecipe crystallarieumRecipe)) {
					this.currentRecipe = crystallarieumRecipe;
					Optional<CrystallarieumCatalyst> oc = this.currentRecipe.getCatalyst(getStack(CATALYST_SLOT_ID));
					oc.ifPresent(crystallarieumCatalyst -> this.currentCatalyst = crystallarieumCatalyst);
				}
			}
		} else {
			this.currentGrowthStageDuration = 0;
			this.currentRecipe = null;
			this.currentCatalyst = null;
		}
	}
	
	@Override
	public void writeNbt(NbtCompound nbt) {
		super.writeNbt(nbt);
		nbt.put("InkStorage", this.inkStorage.toNbt());
		nbt.putShort("CraftingTime", (short) this.currentGrowthStageDuration);
		nbt.putBoolean("CanWork", this.canWork);
		if (this.ownerUUID != null) {
			nbt.putUuid("OwnerUUID", this.ownerUUID);
		}
		if (this.currentRecipe != null) {
			nbt.putString("CurrentRecipe", this.currentRecipe.getId().toString());
			nbt.putInt("CurrentGrowthStageDuration", this.currentGrowthStageDuration);
		}
	}
	
	@Override
	public @Nullable UUID getOwnerUUID() {
		return this.ownerUUID;
	}
	
	@Override
	public void setOwner(PlayerEntity playerEntity) {
		this.ownerUUID = playerEntity.getUuid();
	}
	
	@Nullable
	public CrystallarieumRecipe getCurrentRecipe() {
		return this.currentRecipe;
	}
	
	/**
	 * Searches recipes for a valid one using itemStack and plants the first block of that recipe on top
	 *
	 * @param itemStack stack that is tried to plant on top, if a valid recipe
	 */
	public void acceptStack(ItemStack itemStack, boolean creative) {
		if (itemStack.getItem() instanceof InkStorageItem) {
			ItemStack currentInkStorageStack = getStack(INK_STORAGE_STACK_SLOT_ID);
			if (currentInkStorageStack.isEmpty()) {
				setStack(INK_STORAGE_STACK_SLOT_ID, currentInkStorageStack.copy());
				if (!creative) {
					itemStack.setCount(0);
				}
				world.playSound(null, pos, SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.BLOCKS, 0.8F, 0.8F + world.random.nextFloat() * 0.6F);
			}
			return;
		}
		
		if (world.getBlockState(pos.up()).isAir()) {
			CrystallarieumRecipe recipe = CrystallarieumRecipe.getRecipeForStack(itemStack);
			if (recipe != null) {
				if (!creative) {
					itemStack.decrement(1);
				}
				BlockState placedState = recipe.getGrowthStages().get(0);
				world.setBlockState(pos.up(), placedState);
				onTopBlockChange(placedState, recipe);
				world.playSound(null, pos, SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.BLOCKS, 0.8F, 0.8F + world.random.nextFloat() * 0.6F);
				return;
			}
		}
		if (this.currentRecipe != null) {
			ItemStack currentCatalystStack = getStack(CATALYST_SLOT_ID);
			if (currentCatalystStack.isEmpty()) {
				Optional<CrystallarieumCatalyst> optionalCatalyst = this.currentRecipe.getCatalyst(itemStack);
				if (optionalCatalyst.isPresent()) {
					setStack(CATALYST_SLOT_ID, itemStack.copy());
					if (!creative) {
						itemStack.setCount(0);
					}
					this.currentCatalyst = optionalCatalyst.get();
					inventoryChanged();
				}
			} else if (ItemStack.canCombine(currentCatalystStack, itemStack)) {
				InventoryHelper.combineStacks(currentCatalystStack, itemStack);
				world.playSound(null, pos, SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.BLOCKS, 0.8F, 0.8F + world.random.nextFloat() * 0.6F);
				inventoryChanged();
			}
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
		} else {
			this.currentRecipe = recipe == null ? CrystallarieumRecipe.getRecipeForState(newState) : recipe;
			
			ItemStack catalystStack = getStack(CATALYST_SLOT_ID);
			if (!catalystStack.isEmpty()) {
				Optional<CrystallarieumCatalyst> newCatalyst = this.currentRecipe.getCatalyst(catalystStack);
				if (newCatalyst.isPresent()) {
					this.currentCatalyst = newCatalyst.get();
				} else {
					this.currentCatalyst = null;
					ItemEntity itemEntity = new ItemEntity(world, this.getPos().getX() + 0.5, this.getPos().getY() + 1, this.getPos().getZ() + 0.5, catalystStack);
					this.setStack(CATALYST_SLOT_ID, ItemStack.EMPTY);
					world.spawnEntity(itemEntity);
				}
			}
			this.canWork = true;
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
			return this.currentRecipe.getCatalyst(stack).isPresent();
		}
		return false;
	}
	
}
