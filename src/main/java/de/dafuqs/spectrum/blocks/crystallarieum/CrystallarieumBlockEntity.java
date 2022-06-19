package de.dafuqs.spectrum.blocks.crystallarieum;

import de.dafuqs.spectrum.interfaces.PlayerOwned;
import de.dafuqs.spectrum.recipe.crystallarieum.CrystallarieumRecipe;
import de.dafuqs.spectrum.registries.SpectrumBlockEntityRegistry;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.LootableContainerBlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class CrystallarieumBlockEntity extends LootableContainerBlockEntity implements PlayerOwned {
	
	protected final static int INVENTORY_SIZE = 4;
	protected final static int INK_PROVIDER_STACK_SLOT_ID = 0;
	protected final static int FIRST_CATALYST_SLOT_ID = 1;
	protected final static int CATALYST_SLOT_COUNT = INVENTORY_SIZE - FIRST_CATALYST_SLOT_ID;
	
	protected DefaultedList<ItemStack> inventory;
	protected UUID ownerUUID;
	@Nullable
	protected CrystallarieumRecipe currentRecipe;
	
	public CrystallarieumBlockEntity(BlockPos pos, BlockState state) {
		super(SpectrumBlockEntityRegistry.CRYSTALLARIEUM, pos, state);
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
		// TODO
		//return new CrystallarieumScreenHandler(syncId, playerInventory, this, this.pos);
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
	 * @return if the item of the stack matched a valid recipe, and it was used to plant a block. Gets decreased by 1, if a match is found
	 */
	public boolean tryPlantAndDecrease(ItemStack itemStack) {
		CrystallarieumRecipe recipe = CrystallarieumRecipe.getRecipeForStack(itemStack);
		if(recipe != null) {
			itemStack.decrement(1);
			BlockState placedState = recipe.getBlockStates().get(0);
			world.setBlockState(pos.up(), placedState);
			onTopBlockChange(placedState, recipe);
			world.playSound(null, pos, SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.BLOCKS, 0.8F, 0.8F + world.random.nextFloat() * 0.6F);
			return true;
		}
		return false;
	}
	
	/**
	 * Triggered when the block on top of the crystallarieum has changed
	 * Sets the new recipe matching that block state
	 * @param newState the new block state on top
	 * @param recipe optionally the matching CrystallarieumRecipe. If null is passed it will be calculated
	 */
	public void onTopBlockChange(BlockState newState, @Nullable CrystallarieumRecipe recipe) {
		CrystallarieumRecipe currentRecipe = recipe == null ? CrystallarieumRecipe.getRecipeForState(newState) : recipe;
	}
	
}
