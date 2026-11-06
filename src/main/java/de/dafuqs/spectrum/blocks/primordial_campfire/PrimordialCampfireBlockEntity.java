package de.dafuqs.spectrum.blocks.primordial_campfire;

import de.dafuqs.spectrum.recipe.primordial_fire_burning.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.core.*;
import net.minecraft.core.component.*;
import net.minecraft.core.particles.*;
import net.minecraft.nbt.*;
import net.minecraft.network.protocol.game.*;
import net.minecraft.util.*;
import net.minecraft.world.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.*;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.*;
import net.minecraft.world.level.block.state.*;
import net.minecraft.world.level.gameevent.*;

import javax.annotation.*;
import java.util.*;

// Mostly a copy of CampfireBlockEntity
// but since we use another recipe type and CampfireBlockEntity has all its properties private
// *reimpl time*
public class PrimordialCampfireBlockEntity extends BlockEntity {
	
	private static final int BURN_COOL_SPEED = 2;
	private static final int NUM_SLOTS = 4;
	private final NonNullList<ItemStack> items;
	private final int[] cookingProgress;
	private final int[] cookingTime;
	private final RecipeManager.CachedCheck<SingleRecipeInput, PrimordialFireBurningRecipe> quickCheck;
	
	public PrimordialCampfireBlockEntity(BlockPos pos, BlockState blockState) {
		super(SpectrumBlockEntities.PRIMORDIAL_CAMPFIRE.get(), pos, blockState);
		this.items = NonNullList.withSize(NUM_SLOTS, ItemStack.EMPTY);
		this.cookingProgress = new int[NUM_SLOTS];
		this.cookingTime = new int[NUM_SLOTS];
		this.quickCheck = RecipeManager.createCheck(SpectrumRecipeTypes.PRIMORDIAL_FIRE_BURNING);
	}
	
	public static void cookTick(Level level, BlockPos pos, BlockState state, PrimordialCampfireBlockEntity blockEntity) {
		boolean flag = false;
		
		for(int i = 0; i < blockEntity.items.size(); ++i) {
			ItemStack itemstack = blockEntity.items.get(i);
			if (!itemstack.isEmpty()) {
				flag = true;
				blockEntity.cookingProgress[i]++;
				if (blockEntity.cookingProgress[i] >= blockEntity.cookingTime[i]) {
					SingleRecipeInput singlerecipeinput = new SingleRecipeInput(itemstack);
					ItemStack itemstack1 = blockEntity.quickCheck.getRecipeFor(singlerecipeinput, level).map((holder) -> (holder.value()).assemble(singlerecipeinput, level.registryAccess())).orElse(itemstack);
					if (itemstack1.isItemEnabled(level.enabledFeatures())) {
						Containers.dropItemStack(level, pos.getX(), pos.getY(), pos.getZ(), itemstack1);
						blockEntity.items.set(i, ItemStack.EMPTY);
						level.sendBlockUpdated(pos, state, state, 3);
						level.gameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Context.of(state));
					}
				}
			}
		}
		
		if (flag) {
			setChanged(level, pos, state);
		}
		
	}
	
	public static void cooldownTick(Level level, BlockPos pos, BlockState state, PrimordialCampfireBlockEntity blockEntity) {
		boolean flag = false;
		
		for(int i = 0; i < blockEntity.items.size(); ++i) {
			if (blockEntity.cookingProgress[i] > 0) {
				flag = true;
				blockEntity.cookingProgress[i] = Mth.clamp(blockEntity.cookingProgress[i] - 2, 0, blockEntity.cookingTime[i]);
			}
		}
		
		if (flag) {
			setChanged(level, pos, state);
		}
		
	}
	
	public static void particleTick(Level level, BlockPos pos, BlockState state, PrimordialCampfireBlockEntity blockEntity) {
		RandomSource randomsource = level.random;
		if (randomsource.nextFloat() < 0.11F) {
			for(int i = 0; i < randomsource.nextInt(2) + 2; ++i) {
				CampfireBlock.makeParticles(level, pos, state.getValue(CampfireBlock.SIGNAL_FIRE), false);
			}
		}
		
		int l = state.getValue(CampfireBlock.FACING).get2DDataValue();
		
		for(int j = 0; j < blockEntity.items.size(); ++j) {
			if (!blockEntity.items.get(j).isEmpty() && randomsource.nextFloat() < 0.2F) {
				Direction direction = Direction.from2DDataValue(Math.floorMod(j + l, 4));
				float f = 0.3125F;
				double d0 = (double)pos.getX() + (double)0.5F - (double)((float)direction.getStepX() * f) + (double)((float)direction.getClockWise().getStepX() * f);
				double d1 = (double)pos.getY() + (double)0.5F;
				double d2 = (double)pos.getZ() + (double)0.5F - (double)((float)direction.getStepZ() * f) + (double)((float)direction.getClockWise().getStepZ() * f);
				
				for(int k = 0; k < 4; ++k) {
					level.addParticle(ParticleTypes.SMOKE, d0, d1, d2, 0.0F, 5.0E-4, 0.0F);
				}
			}
		}
		
	}
	
	public NonNullList<ItemStack> getItems() {
		return this.items;
	}
	
	@Override
	protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
		super.loadAdditional(tag, registries);
		this.items.clear();
		ContainerHelper.loadAllItems(tag, this.items, registries);
		if (tag.contains("CookingTimes", 11)) {
			int[] aint = tag.getIntArray("CookingTimes");
			System.arraycopy(aint, 0, this.cookingProgress, 0, Math.min(this.cookingTime.length, aint.length));
		}
		
		if (tag.contains("CookingTotalTimes", 11)) {
			int[] aint1 = tag.getIntArray("CookingTotalTimes");
			System.arraycopy(aint1, 0, this.cookingTime, 0, Math.min(this.cookingTime.length, aint1.length));
		}
		
	}
	
	@Override
	protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
		super.saveAdditional(tag, registries);
		ContainerHelper.saveAllItems(tag, this.items, true, registries);
		tag.putIntArray("CookingTimes", this.cookingProgress);
		tag.putIntArray("CookingTotalTimes", this.cookingTime);
	}
	
	@Override
	public ClientboundBlockEntityDataPacket getUpdatePacket() {
		return ClientboundBlockEntityDataPacket.create(this);
	}
	
	@Override
	public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
		CompoundTag compoundtag = new CompoundTag();
		ContainerHelper.saveAllItems(compoundtag, this.items, true, registries);
		return compoundtag;
	}
	
	public Optional<RecipeHolder<PrimordialFireBurningRecipe>> getCookableRecipe(ItemStack stack) {
		return this.items.stream().noneMatch(ItemStack::isEmpty) ? Optional.empty() : this.quickCheck.getRecipeFor(new SingleRecipeInput(stack), this.level);
	}
	
	public boolean placeFood(@Nullable LivingEntity entity, ItemStack food, int cookTime) {
		for(int i = 0; i < this.items.size(); ++i) {
			ItemStack itemstack = this.items.get(i);
			if (itemstack.isEmpty()) {
				this.cookingTime[i] = cookTime;
				this.cookingProgress[i] = 0;
				this.items.set(i, food.consumeAndReturn(1, entity));
				this.level.gameEvent(GameEvent.BLOCK_CHANGE, this.getBlockPos(), GameEvent.Context.of(entity, this.getBlockState()));
				this.markUpdated();
				return true;
			}
		}
		
		return false;
	}
	
	private void markUpdated() {
		this.setChanged();
		this.getLevel().sendBlockUpdated(this.getBlockPos(), this.getBlockState(), this.getBlockState(), 3);
	}
	
	public void clearContent() {
		this.items.clear();
	}
	
	public void dowse() {
		if (this.level != null) {
			this.markUpdated();
		}
		
	}
	
	@Override
	protected void applyImplicitComponents(BlockEntity.DataComponentInput componentInput) {
		super.applyImplicitComponents(componentInput);
		componentInput.getOrDefault(DataComponents.CONTAINER, ItemContainerContents.EMPTY).copyInto(this.getItems());
	}
	
	@Override
	protected void collectImplicitComponents(DataComponentMap.Builder components) {
		super.collectImplicitComponents(components);
		components.set(DataComponents.CONTAINER, ItemContainerContents.fromItems(this.getItems()));
	}
	
	@Override
	public void removeComponentsFromTag(CompoundTag tag) {
		tag.remove("Items");
	}
	
}
