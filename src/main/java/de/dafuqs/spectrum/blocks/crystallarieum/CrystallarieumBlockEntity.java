package de.dafuqs.spectrum.blocks.crystallarieum;

import de.dafuqs.spectrum.helpers.InventoryHelper;
import de.dafuqs.spectrum.interfaces.PlayerOwned;
import de.dafuqs.spectrum.recipe.crystallarieum.CrystallarieumCatalyst;
import de.dafuqs.spectrum.recipe.crystallarieum.CrystallarieumRecipe;
import de.dafuqs.spectrum.registries.SpectrumBlockEntities;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.LootableContainerBlockEntity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.UUID;

public class CrystallarieumBlockEntity extends LootableContainerBlockEntity implements PlayerOwned {
	
	protected final static int INVENTORY_SIZE = 2;
	protected final static int INK_PROVIDER_STACK_SLOT_ID = 0;
	protected final static int CATALYST_SLOT_ID = 1;
	
	protected DefaultedList<ItemStack> inventory;
	protected UUID ownerUUID;
	@Nullable
	protected CrystallarieumRecipe currentRecipe;
	protected CrystallarieumCatalyst currentCatalyst;
	
	protected int currentGrowthDuration;
	
	public CrystallarieumBlockEntity(BlockPos pos, BlockState state) {
		super(SpectrumBlockEntities.CRYSTALLARIEUM, pos, state);
		this.inventory = DefaultedList.ofSize(INVENTORY_SIZE, ItemStack.EMPTY);
	}
	
	public static void serverTick(@NotNull World world, BlockPos blockPos, BlockState blockState, CrystallarieumBlockEntity fusionShrineBlockEntity) {
	
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
				}
			} else if(ItemStack.canCombine(currentCatalystStack, itemStack)) {
				InventoryHelper.combineStacks(currentCatalystStack, itemStack);
				world.playSound(null, pos, SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.BLOCKS, 0.8F, 0.8F + world.random.nextFloat() * 0.6F);
			}
			updateInClientWorld();
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
		this.currentRecipe = recipe == null ? CrystallarieumRecipe.getRecipeForState(newState) : recipe;
	}
	
}
