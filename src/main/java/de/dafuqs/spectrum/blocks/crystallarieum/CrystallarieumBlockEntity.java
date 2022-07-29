package de.dafuqs.spectrum.blocks.crystallarieum;

import de.dafuqs.spectrum.SpectrumCommon;
import de.dafuqs.spectrum.energy.InkStorageBlockEntity;
import de.dafuqs.spectrum.energy.storage.IndividualCappedInkStorage;
import de.dafuqs.spectrum.events.SpectrumGameEvents;
import de.dafuqs.spectrum.helpers.InventoryHelper;
import de.dafuqs.spectrum.helpers.Support;
import de.dafuqs.spectrum.interfaces.PlayerOwned;
import de.dafuqs.spectrum.particle.SpectrumParticleTypes;
import de.dafuqs.spectrum.recipe.crystallarieum.CrystallarieumCatalyst;
import de.dafuqs.spectrum.recipe.crystallarieum.CrystallarieumRecipe;
import de.dafuqs.spectrum.recipe.enchanter.EnchanterRecipe;
import de.dafuqs.spectrum.recipe.enchantment_upgrade.EnchantmentUpgradeRecipe;
import de.dafuqs.spectrum.registries.SpectrumBlockEntities;
import de.dafuqs.spectrum.registries.SpectrumBlockTags;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.LootableContainerBlockEntity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.network.Packet;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.recipe.Recipe;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Iterator;
import java.util.Optional;
import java.util.UUID;

public class CrystallarieumBlockEntity extends LootableContainerBlockEntity implements PlayerOwned, InkStorageBlockEntity<IndividualCappedInkStorage> {
	
	protected final static int INVENTORY_SIZE = 2;
	protected final static int INK_PROVIDER_STACK_SLOT_ID = 0;
	protected final static int CATALYST_SLOT_ID = 1;
	
	protected DefaultedList<ItemStack> inventory;
	
	public static final long INK_STORAGE_SIZE = 64*64*100;
	protected IndividualCappedInkStorage inkStorage;
	protected boolean inkDirty;
	
	@Nullable protected UUID ownerUUID;
	
	@Nullable protected CrystallarieumRecipe currentRecipe;
	protected CrystallarieumCatalyst currentCatalyst;
	protected int currentGrowthStageDuration;
	protected boolean canWork;
	
	public CrystallarieumBlockEntity(BlockPos pos, BlockState state) {
		super(SpectrumBlockEntities.CRYSTALLARIEUM, pos, state);
		this.inventory = DefaultedList.ofSize(INVENTORY_SIZE, ItemStack.EMPTY);
		this.inkStorage = new IndividualCappedInkStorage(INK_STORAGE_SIZE);
		this.canWork = true;
	}
	
	public static void clientTick(@NotNull World world, BlockPos blockPos, BlockState blockState, CrystallarieumBlockEntity crystallarieum) {
		if(crystallarieum.canWork && crystallarieum.currentRecipe != null) {
			int amount = crystallarieum.currentRecipe.getInkPerSecond();
			if (amount > 0) {
				ParticleEffect particleEffect = SpectrumParticleTypes.getSparkleRisingParticle(crystallarieum.currentRecipe.getInkColor().getDyeColor());
				
				if(amount > 199 || Support.getIntFromDecimalWithChance(amount / 200.0, world.random) > 0) {
					double randomX = world.getRandom().nextDouble() * 0.8;
					double randomZ = world.getRandom().nextDouble() * 0.8;
					world.addParticle(particleEffect, blockPos.getX() + 0.1 + randomX, blockPos.getY() + 1, blockPos.getZ() + 0.1 + randomZ, 0.0D, 0.03D, 0.0D);
				}
			}
		}
	}
	
	public static void serverTick(@NotNull World world, BlockPos blockPos, BlockState blockState, CrystallarieumBlockEntity crystallarieum) {
		if(world.getTime() % 20 == 0 && crystallarieum.canWork && crystallarieum.currentRecipe != null) {
			// advance growing
			if(crystallarieum.currentCatalyst == null) {
				if(!crystallarieum.currentRecipe.growsWithoutCatalyst()) {
					return;
				}
				
				// running without catalyst
				int consumedInk = crystallarieum.currentRecipe.getInkPerSecond();
				if(crystallarieum.inkStorage.drainEnergy(crystallarieum.currentRecipe.getInkColor(), consumedInk) < consumedInk) {
					crystallarieum.setInkDirty();
					crystallarieum.canWork = false;
					crystallarieum.updateInClientWorld();
					return;
				}
				
				crystallarieum.setInkDirty();
				crystallarieum.currentGrowthStageDuration += 20;
			} else {
				// running with catalyst
				int consumedInk = (int) Math.ceil(crystallarieum.currentRecipe.getInkPerSecond() * crystallarieum.currentCatalyst.inkConsumptionMod);
				if(crystallarieum.inkStorage.drainEnergy(crystallarieum.currentRecipe.getInkColor(), consumedInk) < consumedInk) {
					crystallarieum.setInkDirty();
					crystallarieum.canWork = false;
					crystallarieum.updateInClientWorld();
					return;
				}
				
				crystallarieum.setInkDirty();
				crystallarieum.currentGrowthStageDuration += 20 * crystallarieum.currentCatalyst.growthAccelerationMod;
				
				// check if a catalyst should get used up
				if(world.random.nextFloat() < crystallarieum.currentCatalyst.consumeChancePerSecond) {
					ItemStack catalystStack = crystallarieum.getStack(CATALYST_SLOT_ID);
					catalystStack.decrement(1);
					if(catalystStack.isEmpty()) {
						crystallarieum.currentCatalyst = null;
						if(!crystallarieum.currentRecipe.growsWithoutCatalyst()) {
							crystallarieum.canWork = false;
						}
					}
				}
			}
			
			// advanced enough? grow!
			if(crystallarieum.currentGrowthStageDuration >= crystallarieum.currentRecipe.getSecondsPerGrowthStage() * 20) {
				BlockPos topPos = blockPos.up();
				BlockState topState = world.getBlockState(topPos);
				for (Iterator<BlockState> it = crystallarieum.currentRecipe.getGrowthStages().iterator(); it.hasNext(); ) {
					BlockState state = it.next();
					if(state.equals(topState)) {
						if(it.hasNext()) {
							BlockState targetState = it.next();
							world.setBlockState(topPos, targetState);
							if (targetState.isIn(SpectrumBlockTags.CRYSTAL_APOTHECARY_HARVESTABLE)) {
								world.emitGameEvent(SpectrumGameEvents.CRYSTAL_APOTHECARY_HARVESTABLE_GROWN, topPos);
							}
							
							// if the stone on top can not grow any further: pause
							if(!it.hasNext()) {
								crystallarieum.canWork = false;
								crystallarieum.updateInClientWorld();
							}
						}
					}
				}
				crystallarieum.currentGrowthStageDuration = 0;
			}
			
		}
	}
	
	@Override
	public void readNbt(NbtCompound nbt) {
		super.readNbt(nbt);
		
		Inventories.readNbt(nbt, this.inventory);
		if(nbt.contains("InkStorage", NbtElement.COMPOUND_TYPE)) {
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
			String recipeString = nbt.getString("CurrentRecipe");
			if (!recipeString.isEmpty()) {
				Optional<? extends Recipe> optionalRecipe = SpectrumCommon.minecraftServer.getRecipeManager().get(new Identifier(recipeString));
				if (optionalRecipe.isPresent() && (optionalRecipe.get() instanceof CrystallarieumRecipe crystallarieumRecipe)) {
					this.currentRecipe = crystallarieumRecipe;
				}
			}
		}
	}
	
	@Override
	public void writeNbt(NbtCompound nbt) {
		super.writeNbt(nbt);
		Inventories.writeNbt(nbt, this.inventory);
		nbt.put("InkStorage", this.inkStorage.toNbt());
		nbt.putShort("CraftingTime", (short) this.currentGrowthStageDuration);
		nbt.putBoolean("CanWork", this.canWork);
		if (this.ownerUUID != null) {
			nbt.putUuid("OwnerUUID", this.ownerUUID);
		}
		if (this.currentRecipe != null) {
			nbt.putString("CurrentRecipe", this.currentRecipe.getId().toString());
		}
	}
	
	@Override
	public UUID getOwnerUUID() {
		return this.ownerUUID;
	}
	
	@Override
	public void setOwner(PlayerEntity playerEntity) {
		this.ownerUUID = playerEntity.getUuid();
	}
	
	@Override
	protected Text getContainerName() {
		return new TranslatableText("block.spectrum.crystallarieum");
	}
	
	@Override
	protected ScreenHandler createScreenHandler(int syncId, PlayerInventory playerInventory) {
		return null;
	}
	
	@Override
	public int size() {
		return INVENTORY_SIZE;
	}
	
	@Override
	protected DefaultedList<ItemStack> getInvStackList() {
		return this.inventory;
	}
	
	@Override
	protected void setInvStackList(DefaultedList<ItemStack> list) {
		this.inventory = list;
	}
	
	@Nullable
	public CrystallarieumRecipe getCurrentRecipe() {
		return this.currentRecipe;
	}
	
	/**
	 * Searches recipes for a valid one using itemStack and plants the first block of that recipe on top
	 * @param itemStack stack that is tried to plant on top, if a valid recipe
	 */
	public void acceptStack(ItemStack itemStack, boolean creative) {
		if(world.getBlockState(pos.up()).isAir()) {
			CrystallarieumRecipe recipe = CrystallarieumRecipe.getRecipeForStack(itemStack);
			if (recipe != null) {
				if(!creative) {
					itemStack.decrement(1);
				}
				BlockState placedState = recipe.getBlockStates().get(0);
				world.setBlockState(pos.up(), placedState);
				onTopBlockChange(placedState, recipe);
				world.playSound(null, pos, SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.BLOCKS, 0.8F, 0.8F + world.random.nextFloat() * 0.6F);
				return;
			}
		}
		if(this.currentRecipe != null) {
			ItemStack currentCatalystStack = getStack(CATALYST_SLOT_ID);
			if(currentCatalystStack.isEmpty()) {
				Optional<CrystallarieumCatalyst> optionalCatalyst = this.currentRecipe.getCatalyst(itemStack);
				if(optionalCatalyst.isPresent()) {
					setStack(CATALYST_SLOT_ID, itemStack.copy());
					if(!creative) {
						itemStack.setCount(0);
					}
					this.currentCatalyst = optionalCatalyst.get();
					this.canWork = true;
					updateInClientWorld();
					markDirty();
				}
			} else if(ItemStack.canCombine(currentCatalystStack, itemStack)) {
				InventoryHelper.combineStacks(currentCatalystStack, itemStack);
				world.playSound(null, pos, SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.BLOCKS, 0.8F, 0.8F + world.random.nextFloat() * 0.6F);
				this.canWork = true;
				updateInClientWorld();
				markDirty();
			}
		}
	}
	
	public ItemStack popCatalyst() {
		ItemStack catalystStack = getStack(CATALYST_SLOT_ID);
		setStack(CATALYST_SLOT_ID, ItemStack.EMPTY);
		this.currentCatalyst = null;
		updateInClientWorld();
		return catalystStack;
	}
	
	// Called when the chunk is first loaded to initialize this be
	public NbtCompound toInitialChunkDataNbt() {
		NbtCompound nbtCompound = new NbtCompound();
		this.writeNbt(nbtCompound);
		return nbtCompound;
	}
	
	@Nullable
	@Override
	public Packet<ClientPlayPacketListener> toUpdatePacket() {
		return BlockEntityUpdateS2CPacket.create(this);
	}
	
	public void updateInClientWorld() {
		((ServerWorld) world).getChunkManager().markForUpdate(pos);
	}
	
	/**
	 * Triggered when the block on top of the crystallarieum has changed
	 * Sets the new recipe matching that block state
	 * @param newState the new block state on top
	 * @param recipe optionally the matching CrystallarieumRecipe. If null is passed it will be calculated
	 */
	public void onTopBlockChange(BlockState newState, @Nullable CrystallarieumRecipe recipe) {
		if(newState.isAir()) { // fast fail
			this.currentRecipe = null;
			this.canWork = false;
			markDirty();
			updateInClientWorld();
		} else {
			this.currentRecipe = recipe == null ? CrystallarieumRecipe.getRecipeForState(newState) : recipe;
			
			ItemStack catalystStack = getStack(CATALYST_SLOT_ID);
			if(!catalystStack.isEmpty()) {
				Optional<CrystallarieumCatalyst> newCatalyst = this.currentRecipe.getCatalyst(catalystStack);
				if(newCatalyst.isPresent()) {
					this.currentCatalyst = newCatalyst.get();
				} else {
					this.currentCatalyst = null;
					ItemEntity itemEntity = new ItemEntity(world, this.getPos().getX() + 0.5, this.getPos().getY() + 1, this.getPos().getZ() + 0.5, catalystStack);
					this.setStack(CATALYST_SLOT_ID, ItemStack.EMPTY);
					world.spawnEntity(itemEntity);
				}
			}
			this.canWork = true;
			markDirty();
			updateInClientWorld();
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
	
}
