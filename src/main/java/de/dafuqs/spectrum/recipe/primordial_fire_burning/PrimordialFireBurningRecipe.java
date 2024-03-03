package de.dafuqs.spectrum.recipe.primordial_fire_burning;

import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.entity.entity.*;
import de.dafuqs.spectrum.inventories.*;
import de.dafuqs.spectrum.recipe.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.block.*;
import net.minecraft.entity.*;
import net.minecraft.inventory.*;
import net.minecraft.item.*;
import net.minecraft.recipe.*;
import net.minecraft.registry.*;
import net.minecraft.sound.*;
import net.minecraft.util.*;
import net.minecraft.util.collection.*;
import net.minecraft.util.math.*;
import net.minecraft.world.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class PrimordialFireBurningRecipe extends GatedSpectrumRecipe {
	
	public static final Identifier UNLOCK_IDENTIFIER = SpectrumCommon.locate("lategame/collect_doombloom_seed");
	
	private static AutoCraftingInventory AUTO_INVENTORY = new AutoCraftingInventory(1, 1);
	
	protected final Ingredient input;
	protected final ItemStack output;
	
	public PrimordialFireBurningRecipe(Identifier id, String group, boolean secret, Identifier requiredAdvancementIdentifier, Ingredient input, ItemStack output) {
		super(id, group, secret, requiredAdvancementIdentifier);
		
		this.input = input;
		this.output = output;
		
		registerInToastManager(getType(), this);
	}
	
	@Override
	public boolean matches(Inventory inv, World world) {
		return this.input.test(inv.getStack(0));
	}
	
	@Override
	public ItemStack craft(Inventory inv, DynamicRegistryManager drm) {
		return ItemStack.EMPTY;
	}
	
	@Override
	public boolean fits(int width, int height) {
		return true;
	}
	
	@Override
	public ItemStack getOutput(DynamicRegistryManager registryManager) {
		return output;
	}
	
	@Override
	public ItemStack createIcon() {
		return new ItemStack(SpectrumBlocks.DOOMBLOOM);
	}
	
	@Override
	public Identifier getRecipeTypeUnlockIdentifier() {
		return UNLOCK_IDENTIFIER;
	}
	
	@Override
	public RecipeSerializer<?> getSerializer() {
		return SpectrumRecipeTypes.PRIMORDIAL_FIRE_BURNING_RECIPE_SERIALIZER;
	}
	
	@Override
	public RecipeType<?> getType() {
		return SpectrumRecipeTypes.PRIMORDIAL_FIRE_BURNING;
	}
	
	@Override
	public DefaultedList<Ingredient> getIngredients() {
		DefaultedList<Ingredient> defaultedList = DefaultedList.of();
		defaultedList.add(this.input);
		return defaultedList;
	}
	
	@Override
	public String getRecipeTypeShortID() {
		return SpectrumRecipeTypes.PRIMORDIAL_FIRE_BURNING_ID;
	}
	
	public static PrimordialFireBurningRecipe getRecipeFor(@NotNull World world, ItemStack stack) {
		AUTO_INVENTORY.setInputInventory(Collections.singletonList(stack));
		return world.getRecipeManager().getFirstMatch(SpectrumRecipeTypes.PRIMORDIAL_FIRE_BURNING, AUTO_INVENTORY, world).orElse(null);
	}
	
	public static boolean processBlock(World world, BlockPos pos, BlockState state) {
		Item item = state.getBlock().asItem();
		if(item == Items.AIR) {
			return false;
		}
		
		PrimordialFireBurningRecipe recipe = PrimordialFireBurningRecipe.getRecipeFor(world, item.getDefaultStack());
		if (recipe == null) {
			return false;
		}
		
		AUTO_INVENTORY.setInputInventory(Collections.singletonList(state.getBlock().asItem().getDefaultStack()));
		ItemStack output = recipe.craft(AUTO_INVENTORY, world.getRegistryManager()).copy();
		
		world.playSound(null, pos, SpectrumSoundEvents.PRIMORDIAL_FIRE_CRACKLE, SoundCategory.BLOCKS, 0.7F, 1.0F);
		if(output.getItem() instanceof BlockItem blockItem) {
			world.setBlockState(pos, blockItem.getBlock().getDefaultState());
		} else {
			FireproofItemEntity.scatter(world, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, output);
		}
		
		return true;
	}
	
	public static boolean processItemEntity(World world, ItemEntity itemEntity) {
		Vec3d pos = itemEntity.getPos();
		
		ItemStack inputStack = itemEntity.getStack();
		PrimordialFireBurningRecipe recipe = PrimordialFireBurningRecipe.getRecipeFor(world, inputStack);
		if (recipe == null) {
			return false;
		}
		
		int inputCount = inputStack.getCount();
		AUTO_INVENTORY.setInputInventory(Collections.singletonList(inputStack));
		ItemStack outputStack = recipe.craft(AUTO_INVENTORY, world.getRegistryManager()).copy();
		outputStack.setCount(outputStack.getCount() * inputCount);
		
		inputStack.setCount(0);
		itemEntity.discard();
		
		FireproofItemEntity.scatter(world, pos.getX(), pos.getY(), pos.getZ(), outputStack);
		world.playSound(null, itemEntity.getBlockPos(), SpectrumSoundEvents.PRIMORDIAL_FIRE_CRACKLE, SoundCategory.BLOCKS, 0.7F, 1.0F);
		
		return true;
	}
}
