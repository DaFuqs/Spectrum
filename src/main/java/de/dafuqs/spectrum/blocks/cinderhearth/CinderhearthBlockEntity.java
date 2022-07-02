package de.dafuqs.spectrum.blocks.cinderhearth;

import de.dafuqs.spectrum.blocks.MultiblockCrafter;
import de.dafuqs.spectrum.blocks.upgrade.Upgradeable;
import de.dafuqs.spectrum.energy.InkStorage;
import de.dafuqs.spectrum.energy.InkStorageBlockEntity;
import de.dafuqs.spectrum.energy.InkStorageItem;
import de.dafuqs.spectrum.energy.color.InkColor;
import de.dafuqs.spectrum.energy.color.InkColors;
import de.dafuqs.spectrum.energy.storage.IndividualCappedInkStorage;
import de.dafuqs.spectrum.helpers.InventoryHelper;
import de.dafuqs.spectrum.helpers.Support;
import de.dafuqs.spectrum.interfaces.PlayerOwned;
import de.dafuqs.spectrum.inventories.CinderhearthScreenHandler;
import de.dafuqs.spectrum.inventories.ColorPickerScreenHandler;
import de.dafuqs.spectrum.items.ExperienceStorageItem;
import de.dafuqs.spectrum.networking.SpectrumS2CPacketSender;
import de.dafuqs.spectrum.recipe.GatedRecipe;
import de.dafuqs.spectrum.recipe.SpectrumRecipeTypes;
import de.dafuqs.spectrum.recipe.cinderhearth.CinderhearthRecipe;
import de.dafuqs.spectrum.registries.SpectrumBlockEntityRegistry;
import de.dafuqs.spectrum.registries.SpectrumSoundEvents;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.LockableContainerBlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.BlastingRecipe;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeMatcher;
import net.minecraft.recipe.RecipeType;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class CinderhearthBlockEntity extends LockableContainerBlockEntity implements MultiblockCrafter, Inventory, ExtendedScreenHandlerFactory, InkStorageBlockEntity<IndividualCappedInkStorage> {
	
	public static final int INVENTORY_SIZE = 11;
	public static final int INPUT_SLOT_ID = 0;
	public static final int INK_PROVIDER_SLOT_ID = 1;
	public static final int EXPERIENCE_STORAGE_ITEM_SLOT_ID = 2;
	public static final int FIRST_OUTPUT_SLOT_ID = 3;
	public static final int LAST_OUTPUT_SLOT_ID = 10;
	
	protected SimpleInventory inventory;
	protected boolean inventoryChanged;
	
	public static final long INK_STORAGE_SIZE = 64*100;
	protected IndividualCappedInkStorage inkStorage;
	
	private UUID ownerUUID;
	private Map<UpgradeType, Float> upgrades;
	private Recipe currentRecipe; // blasting & cinderhearth
	private int craftingTime;
	private int craftingTimeTotal;
	protected boolean paused;
	
	public CinderhearthBlockEntity(BlockPos pos, BlockState state) {
		super(SpectrumBlockEntityRegistry.CINDERHEARTH, pos, state);
		this.inventory = new SimpleInventory(INVENTORY_SIZE);
		this.inkStorage = new IndividualCappedInkStorage(INK_STORAGE_SIZE, Set.of(InkColors.ORANGE, InkColors.LIGHT_BLUE, InkColors.MAGENTA, InkColors.BLACK));
	}
	
	@Override
	public void resetUpgrades() {
		this.upgrades = null;
		this.markDirty();
	}
	
	@Override
	public void calculateUpgrades() {
		this.upgrades = Upgradeable.calculateUpgradeMods2(world, pos, Support.rotationFromDirection(world.getBlockState(pos).get(CinderhearthBlock.FACING)), 2, 1, 1, this.ownerUUID);
		this.markDirty();
	}
	
	@Override
	public UUID getOwnerUUID() {
		return this.ownerUUID;
	}
	
	@Override
	public void setOwner(PlayerEntity playerEntity) {
		this.ownerUUID = playerEntity.getUuid();
		this.markDirty();
	}
	
	@Override
	protected Text getContainerName() {
		return new TranslatableText("block.spectrum.cinderhearth");
	}
	
	@Override
	protected ScreenHandler createScreenHandler(int syncId, PlayerInventory playerInventory) {
		return new CinderhearthScreenHandler(syncId, playerInventory, this.pos);
	}
	
	@Override
	public void writeScreenOpeningData(ServerPlayerEntity player, PacketByteBuf buf) {
		buf.writeBlockPos(pos);
	}
	
	@Override
	public void readNbt(NbtCompound nbt) {
		super.readNbt(nbt);
		this.inventory = new SimpleInventory(INVENTORY_SIZE);
		this.inventory.readNbtList(nbt.getList("Inventory", 10));
		if(nbt.contains("InkStorage", NbtElement.COMPOUND_TYPE)) {
			this.inkStorage = IndividualCappedInkStorage.fromNbt(nbt.getCompound("InkStorage"));
		}
		this.craftingTime = nbt.getShort("CraftingTime");
		this.craftingTimeTotal = nbt.getShort("CraftingTimeTotal");
		this.paused = nbt.getBoolean("Paused");
		this.inventoryChanged = nbt.getBoolean("InventoryChanged");
		if (nbt.contains("OwnerUUID")) {
			this.ownerUUID = nbt.getUuid("OwnerUUID");
		} else {
			this.ownerUUID = null;
		}
		if (nbt.contains("CurrentRecipe")) {
			String recipeString = nbt.getString("CurrentRecipe");
			if (!recipeString.isEmpty()) {
				Optional<? extends Recipe> optionalRecipe = Optional.empty();
				if (world != null) {
					optionalRecipe = world.getRecipeManager().get(new Identifier(recipeString));
				}
				this.currentRecipe = optionalRecipe.orElse(null);
			} else {
				this.currentRecipe = null;
			}
		} else {
			this.currentRecipe = null;
		}
		if (nbt.contains("Upgrades", NbtElement.LIST_TYPE)) {
			this.upgrades = Upgradeable.fromNbt(nbt.getList("Upgrades", NbtElement.COMPOUND_TYPE));
		}
	}
	
	@Override
	public void writeNbt(NbtCompound nbt) {
		super.writeNbt(nbt);
		nbt.put("Inventory", this.inventory.toNbtList());
		nbt.put("InkStorage", this.inkStorage.toNbt());
		nbt.putShort("CraftingTime", (short) this.craftingTime);
		nbt.putShort("CraftingTimeTotal", (short) this.craftingTimeTotal);
		nbt.putBoolean("Paused", this.paused);
		nbt.putBoolean("InventoryChanged", this.inventoryChanged);
		if (this.upgrades != null) {
			nbt.put("Upgrades", Upgradeable.toNbt(this.upgrades));
		}
		if (this.ownerUUID != null) {
			nbt.putUuid("OwnerUUID", this.ownerUUID);
		}
		if (this.currentRecipe != null) {
			nbt.putString("CurrentRecipe", this.currentRecipe.getId().toString());
		}
	}
	
	public static void serverTick(World world, BlockPos blockPos, BlockState blockState, CinderhearthBlockEntity cinderhearthBlockEntity) {
		if (cinderhearthBlockEntity.upgrades == null) {
			cinderhearthBlockEntity.calculateUpgrades();
		}
		
		if (!cinderhearthBlockEntity.paused) {
			boolean didSomething = false;
			ItemStack stack = cinderhearthBlockEntity.inventory.getStack(INK_PROVIDER_SLOT_ID);
			if (stack.getItem() instanceof InkStorageItem inkStorageItem) {
				InkStorage itemStorage = inkStorageItem.getEnergyStorage(stack);
				didSomething = InkStorage.transferInk(itemStorage, cinderhearthBlockEntity.inkStorage) != 0;
			}
			if (didSomething) {
				cinderhearthBlockEntity.markDirty();
			} else {
				cinderhearthBlockEntity.paused = true;
			}
		}
		
		if (cinderhearthBlockEntity.inventoryChanged) {
			calculateRecipe(world, cinderhearthBlockEntity);
			cinderhearthBlockEntity.inventoryChanged = false;
		}
		
		if (cinderhearthBlockEntity.currentRecipe != null) {
			if (cinderhearthBlockEntity.craftingTime % 60 == 1) {
				if (!checkRecipeRequirements(world, blockPos, cinderhearthBlockEntity)) {
					cinderhearthBlockEntity.craftingTime = 0;
					SpectrumS2CPacketSender.sendCancelBlockBoundSoundInstance((ServerWorld) cinderhearthBlockEntity.world, cinderhearthBlockEntity.pos);
					return;
				}
			}
			
			if (cinderhearthBlockEntity.currentRecipe != null) {
				cinderhearthBlockEntity.craftingTime++;
				
				if (cinderhearthBlockEntity.craftingTime == cinderhearthBlockEntity.craftingTimeTotal) {
					if(cinderhearthBlockEntity.currentRecipe instanceof CinderhearthRecipe cinderhearthRecipe) {
						craftCinderhearthRecipe(world, cinderhearthBlockEntity, cinderhearthRecipe);
					} else if(cinderhearthBlockEntity.currentRecipe instanceof BlastingRecipe blastingRecipe) {
						craftBlastingRecipe(world, cinderhearthBlockEntity, blastingRecipe);
					}
				}
				
				cinderhearthBlockEntity.markDirty();
			}
		} else {
			SpectrumS2CPacketSender.sendCancelBlockBoundSoundInstance((ServerWorld) cinderhearthBlockEntity.world, cinderhearthBlockEntity.pos);
		}
	}
	
	
	private static void calculateRecipe(@NotNull World world, @NotNull CinderhearthBlockEntity cinderhearthBlockEntity) {
		// test the cached recipe => faster
		if (cinderhearthBlockEntity.currentRecipe != null) {
			if (cinderhearthBlockEntity.currentRecipe.matches(cinderhearthBlockEntity.inventory, world)) {
				return;
			}
		}
		
		// cached recipe did not match => calculate new
		ItemStack instillerStack = cinderhearthBlockEntity.inventory.getStack(0);
		if (!instillerStack.isEmpty()) {
			CinderhearthRecipe cinderhearthRecipe = world.getRecipeManager().getFirstMatch(SpectrumRecipeTypes.CINDERHEARTH, cinderhearthBlockEntity, world).orElse(null);
			if(cinderhearthRecipe == null) {
				BlastingRecipe blastingRecipe = world.getRecipeManager().getFirstMatch(RecipeType.BLASTING, cinderhearthBlockEntity, world).orElse(null);
				if(blastingRecipe == null) {
					cinderhearthBlockEntity.currentRecipe = null;
					cinderhearthBlockEntity.craftingTime = 0;
				} else {
					cinderhearthBlockEntity.currentRecipe = blastingRecipe;
					cinderhearthBlockEntity.craftingTimeTotal = (int) Math.ceil(blastingRecipe.getCookTime() / cinderhearthBlockEntity.upgrades.get(Upgradeable.UpgradeType.SPEED));
				}
			} else {
				cinderhearthBlockEntity.currentRecipe = cinderhearthRecipe;
				cinderhearthBlockEntity.craftingTimeTotal = (int) Math.ceil(cinderhearthRecipe.getCraftingTime() / cinderhearthBlockEntity.upgrades.get(Upgradeable.UpgradeType.SPEED));
			}
		}
	}
	
	private static boolean checkRecipeRequirements(World world, BlockPos blockPos, @NotNull CinderhearthBlockEntity cinderhearthBlockEntity) {
		PlayerEntity lastInteractedPlayer = PlayerOwned.getPlayerEntityIfOnline(cinderhearthBlockEntity.ownerUUID);
		if (lastInteractedPlayer == null) {
			return false;
		}
		
		if (!CinderhearthBlock.verifyStructure(world, blockPos, null)) {
			world.playSound(null, cinderhearthBlockEntity.getPos(), SpectrumSoundEvents.CRAFTING_ABORTED, SoundCategory.BLOCKS, 0.9F + cinderhearthBlockEntity.world.random.nextFloat() * 0.2F, 0.9F + cinderhearthBlockEntity.world.random.nextFloat() * 0.2F);
			return false;
		}
		
		if(cinderhearthBlockEntity.currentRecipe instanceof GatedRecipe gatedRecipe) {
			return gatedRecipe.canPlayerCraft(lastInteractedPlayer);
		}
		return true;
	}
	
	public static void craftBlastingRecipe(World world, @NotNull CinderhearthBlockEntity cinderhearthBlockEntity, @NotNull BlastingRecipe blastingRecipe) {
		// output
		ItemStack inputStack = cinderhearthBlockEntity.inventory.getStack(INPUT_SLOT_ID);
		ItemStack output = blastingRecipe.getOutput();
		
		InventoryHelper.addToInventory(cinderhearthBlockEntity, output, FIRST_OUTPUT_SLOT_ID, LAST_OUTPUT_SLOT_ID);
		Item remainder = inputStack.getItem().getRecipeRemainder();
		if(remainder != null) {
			InventoryHelper.addToInventory(cinderhearthBlockEntity, output, FIRST_OUTPUT_SLOT_ID, LAST_OUTPUT_SLOT_ID);
		}
		
		// use up input ingredient
		inputStack.decrement(1);
		
		// grant experience
		ExperienceStorageItem.addStoredExperience(cinderhearthBlockEntity.getStack(EXPERIENCE_STORAGE_ITEM_SLOT_ID), blastingRecipe.getExperience(), world.random);
		
		// effects
		playCraftingFinishedEffects(cinderhearthBlockEntity);
		
		// reset
		cinderhearthBlockEntity.craftingTime = 0;
		cinderhearthBlockEntity.inventoryChanged();
	}
	
	public static void craftCinderhearthRecipe(World world, @NotNull CinderhearthBlockEntity cinderhearthBlockEntity, @NotNull CinderhearthRecipe cinderhearthRecipe) {
		// output
		List<ItemStack> outputs = cinderhearthRecipe.getRolledOutputs(world.random);
		
		ItemStack inputStack = cinderhearthBlockEntity.inventory.getStack(INPUT_SLOT_ID);
		Item remainder = inputStack.getItem().getRecipeRemainder();
		if(remainder != null) {
			outputs.add(remainder.getDefaultStack());
		}
		InventoryHelper.addToInventory(cinderhearthBlockEntity, outputs, FIRST_OUTPUT_SLOT_ID, LAST_OUTPUT_SLOT_ID);
		
		// use up input ingredient
		inputStack.decrement(1);
		
		// grant experience
		ExperienceStorageItem.addStoredExperience(cinderhearthBlockEntity.getStack(EXPERIENCE_STORAGE_ITEM_SLOT_ID), cinderhearthRecipe.getExperience(), cinderhearthBlockEntity.world.random);
		
		// effects
		playCraftingFinishedEffects(cinderhearthBlockEntity);
		
		// reset
		cinderhearthBlockEntity.craftingTime = 0;
		cinderhearthBlockEntity.inventoryChanged();
	}
	
	public static void playCraftingFinishedEffects(@NotNull CinderhearthBlockEntity cinderhearthBlockEntity) {
		// TODO
	}
	
	@Override
	public int size() {
		return INVENTORY_SIZE;
	}
	
	@Override
	public boolean isEmpty() {
		return this.inventory.isEmpty();
	}
	
	@Override
	public ItemStack getStack(int slot) {
		return this.inventory.getStack(slot);
	}
	
	@Override
	public ItemStack removeStack(int slot, int amount) {
		this.inventoryChanged();
		return this.inventory.removeStack(slot, amount);
	}
	
	@Override
	public ItemStack removeStack(int slot) {
		this.inventoryChanged();
		return this.inventory.removeStack(slot);
	}
	
	@Override
	public void setStack(int slot, ItemStack stack) {
		this.inventoryChanged();
		this.inventory.setStack(slot, stack);
	}
	
	@Override
	public boolean canPlayerUse(PlayerEntity player) {
		return true;
	}
	
	public void inventoryChanged() {
		this.inventoryChanged = true;
		this.inventory.markDirty();
		markDirty();
	}
	
	@Override
	public void clear() {
		this.inventoryChanged();
		this.inventory.clear();
	}
	
	public Map<UpgradeType, Float> getUpgrades() {
		return this.upgrades;
	}
	
	@Override
	public void provideRecipeInputs(RecipeMatcher finder) {
	
	}
	
	@Override
	public IndividualCappedInkStorage getEnergyStorage() {
		return this.inkStorage;
	}
	
	public boolean shouldUpdateClients() {
		return !this.paused;
	}
	
}
