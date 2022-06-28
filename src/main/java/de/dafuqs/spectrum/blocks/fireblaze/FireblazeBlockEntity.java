package de.dafuqs.spectrum.blocks.fireblaze;

import de.dafuqs.spectrum.blocks.MultiblockCrafter;
import de.dafuqs.spectrum.blocks.upgrade.Upgradeable;
import de.dafuqs.spectrum.helpers.InventoryHelper;
import de.dafuqs.spectrum.interfaces.PlayerOwned;
import de.dafuqs.spectrum.items.ExperienceStorageItem;
import de.dafuqs.spectrum.networking.SpectrumS2CPacketSender;
import de.dafuqs.spectrum.recipe.GatedRecipe;
import de.dafuqs.spectrum.recipe.SpectrumRecipeTypes;
import de.dafuqs.spectrum.recipe.fireblaze.FireblazeRecipe;
import de.dafuqs.spectrum.registries.SpectrumBlockEntityRegistry;
import de.dafuqs.spectrum.registries.SpectrumSoundEvents;
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
import net.minecraft.recipe.BlastingRecipe;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeMatcher;
import net.minecraft.recipe.RecipeType;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class FireblazeBlockEntity extends LockableContainerBlockEntity implements MultiblockCrafter, Inventory {
	
	public static final int INVENTORY_SIZE = 10;
	public static final int INPUT_SLOT_ID = 0;
	public static final int EXPERIENCE_STORAGE_ITEM_SLOT_ID = 1;
	public static final int FIRST_OUTPUT_SLOT_ID = 2;
	public static final int LAST_OUTPUT_SLOT_ID = 9;
	
	protected SimpleInventory inventory;
	protected boolean inventoryChanged;
	private UUID ownerUUID;
	private Map<UpgradeType, Float> upgrades;
	private Recipe currentRecipe; // blasting & fireblaze
	private int craftingTime;
	private int craftingTimeTotal;
	
	public FireblazeBlockEntity(BlockPos pos, BlockState state) {
		super(SpectrumBlockEntityRegistry.FIREBLAZE, pos, state);
		this.inventory = new SimpleInventory(INVENTORY_SIZE);
	}
	
	@Override
	public void resetUpgrades() {
		this.upgrades = null;
		this.markDirty();
	}
	
	@Override
	public void calculateUpgrades() {
		this.upgrades = Upgradeable.calculateUpgradeMods4(world, pos, 2, 2, this.ownerUUID);
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
		return null;
	}
	
	@Override
	protected ScreenHandler createScreenHandler(int syncId, PlayerInventory playerInventory) {
		return null;
	}
	
	@Override
	public void readNbt(NbtCompound nbt) {
		super.readNbt(nbt);
		this.inventory = new SimpleInventory(INVENTORY_SIZE);
		this.inventory.readNbtList(nbt.getList("inventory", 10));
		this.craftingTime = nbt.getShort("CraftingTime");
		this.craftingTimeTotal = nbt.getShort("CraftingTimeTotal");
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
		nbt.put("inventory", this.inventory.toNbtList());
		nbt.putShort("CraftingTime", (short) this.craftingTime);
		nbt.putShort("CraftingTimeTotal", (short) this.craftingTimeTotal);
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
	
	public static void serverTick(World world, BlockPos blockPos, BlockState blockState, FireblazeBlockEntity fireblazeBlockEntity) {
		if (fireblazeBlockEntity.upgrades == null) {
			fireblazeBlockEntity.calculateUpgrades();
		}
		
		if (fireblazeBlockEntity.inventoryChanged) {
			calculateRecipe(world, fireblazeBlockEntity);
			fireblazeBlockEntity.inventoryChanged = false;
		}
		
		if (fireblazeBlockEntity.currentRecipe != null) {
			if (fireblazeBlockEntity.craftingTime % 60 == 1) {
				if (!checkRecipeRequirements(world, blockPos, fireblazeBlockEntity)) {
					fireblazeBlockEntity.craftingTime = 0;
					SpectrumS2CPacketSender.sendCancelBlockBoundSoundInstance((ServerWorld) fireblazeBlockEntity.world, fireblazeBlockEntity.pos);
					return;
				}
			}
			
			if (fireblazeBlockEntity.currentRecipe != null) {
				fireblazeBlockEntity.craftingTime++;
				
				if (fireblazeBlockEntity.craftingTime == fireblazeBlockEntity.craftingTimeTotal) {
					if(fireblazeBlockEntity.currentRecipe instanceof FireblazeRecipe fireblazeRecipe) {
						craftFireblazeRecipe(world, fireblazeBlockEntity, fireblazeRecipe);
					} else if(fireblazeBlockEntity.currentRecipe instanceof BlastingRecipe blastingRecipe) {
						craftBlastingRecipe(world, fireblazeBlockEntity, blastingRecipe);
					}
				}
				
				fireblazeBlockEntity.markDirty();
			}
		} else {
			SpectrumS2CPacketSender.sendCancelBlockBoundSoundInstance((ServerWorld) fireblazeBlockEntity.world, fireblazeBlockEntity.pos);
		}
	}
	
	
	private static void calculateRecipe(@NotNull World world, @NotNull FireblazeBlockEntity fireblazeBlockEntity) {
		// test the cached recipe => faster
		if (fireblazeBlockEntity.currentRecipe != null) {
			if (fireblazeBlockEntity.currentRecipe.matches(fireblazeBlockEntity.inventory, world)) {
				return;
			}
		}
		
		// cached recipe did not match => calculate new
		ItemStack instillerStack = fireblazeBlockEntity.inventory.getStack(0);
		if (!instillerStack.isEmpty()) {
			FireblazeRecipe fireblazeRecipe = world.getRecipeManager().getFirstMatch(SpectrumRecipeTypes.FIREBLAZE, fireblazeBlockEntity, world).orElse(null);
			if(fireblazeRecipe == null) {
				BlastingRecipe blastingRecipe = world.getRecipeManager().getFirstMatch(RecipeType.BLASTING, fireblazeBlockEntity, world).orElse(null);
				if(blastingRecipe == null) {
					fireblazeBlockEntity.currentRecipe = null;
					fireblazeBlockEntity.craftingTime = 0;
				} else {
					fireblazeBlockEntity.currentRecipe = blastingRecipe;
					fireblazeBlockEntity.craftingTimeTotal = (int) Math.ceil(blastingRecipe.getCookTime() / fireblazeBlockEntity.upgrades.get(Upgradeable.UpgradeType.SPEED));
				}
			} else {
				fireblazeBlockEntity.currentRecipe = fireblazeRecipe;
				fireblazeBlockEntity.craftingTimeTotal = (int) Math.ceil(fireblazeRecipe.getCraftingTime() / fireblazeBlockEntity.upgrades.get(Upgradeable.UpgradeType.SPEED));
			}
		}
	}
	
	private static boolean checkRecipeRequirements(World world, BlockPos blockPos, @NotNull FireblazeBlockEntity fireblazeBlockEntity) {
		PlayerEntity lastInteractedPlayer = PlayerOwned.getPlayerEntityIfOnline(fireblazeBlockEntity.ownerUUID);
		if (lastInteractedPlayer == null) {
			return false;
		}
		
		if (!FireblazeBlock.verifyStructure(world, blockPos, null)) {
			world.playSound(null, fireblazeBlockEntity.getPos(), SpectrumSoundEvents.CRAFTING_ABORTED, SoundCategory.BLOCKS, 0.9F + fireblazeBlockEntity.world.random.nextFloat() * 0.2F, 0.9F + fireblazeBlockEntity.world.random.nextFloat() * 0.2F);
			return false;
		}
		
		if(fireblazeBlockEntity.currentRecipe instanceof GatedRecipe gatedRecipe) {
			return gatedRecipe.canPlayerCraft(lastInteractedPlayer);
		}
		return true;
	}
	
	public static void craftBlastingRecipe(World world, @NotNull FireblazeBlockEntity fireblazeBlockEntity, @NotNull BlastingRecipe blastingRecipe) {
		// output
		ItemStack inputStack = fireblazeBlockEntity.inventory.getStack(INPUT_SLOT_ID);
		ItemStack output = blastingRecipe.getOutput();
		
		InventoryHelper.addToInventory(fireblazeBlockEntity, output, FIRST_OUTPUT_SLOT_ID, LAST_OUTPUT_SLOT_ID);
		Item remainder = inputStack.getItem().getRecipeRemainder();
		if(remainder != null) {
			InventoryHelper.addToInventory(fireblazeBlockEntity, output, FIRST_OUTPUT_SLOT_ID, LAST_OUTPUT_SLOT_ID);
		}
		
		// use up input ingredient
		inputStack.decrement(1);
		
		// grant experience
		ExperienceStorageItem.addStoredExperience(fireblazeBlockEntity.getStack(EXPERIENCE_STORAGE_ITEM_SLOT_ID), blastingRecipe.getExperience(), world.random);
		
		// effects
		playCraftingFinishedEffects(fireblazeBlockEntity);
		
		// reset
		fireblazeBlockEntity.craftingTime = 0;
		fireblazeBlockEntity.inventoryChanged();
	}
	
	public static void craftFireblazeRecipe(World world, @NotNull FireblazeBlockEntity fireblazeBlockEntity, @NotNull FireblazeRecipe fireblazeRecipe) {
		// output
		List<ItemStack> outputs = fireblazeRecipe.getRolledOutputs(world.random);
		
		ItemStack inputStack = fireblazeBlockEntity.inventory.getStack(INPUT_SLOT_ID);
		Item remainder = inputStack.getItem().getRecipeRemainder();
		if(remainder != null) {
			outputs.add(remainder.getDefaultStack());
		}
		InventoryHelper.addToInventory(fireblazeBlockEntity, outputs, FIRST_OUTPUT_SLOT_ID, LAST_OUTPUT_SLOT_ID);
		
		// use up input ingredient
		inputStack.decrement(1);
		
		// grant experience
		ExperienceStorageItem.addStoredExperience(fireblazeBlockEntity.getStack(EXPERIENCE_STORAGE_ITEM_SLOT_ID), fireblazeRecipe.getExperience(), fireblazeBlockEntity.world.random);
		
		// effects
		playCraftingFinishedEffects(fireblazeBlockEntity);
		
		// reset
		fireblazeBlockEntity.craftingTime = 0;
		fireblazeBlockEntity.inventoryChanged();
	}
	
	public static void playCraftingFinishedEffects(@NotNull FireblazeBlockEntity fireblazeBlockEntity) {
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
	
}
