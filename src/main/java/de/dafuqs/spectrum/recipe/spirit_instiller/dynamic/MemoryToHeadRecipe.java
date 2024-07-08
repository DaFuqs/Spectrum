package de.dafuqs.spectrum.recipe.spirit_instiller.dynamic;

import de.dafuqs.matchbooks.recipe.*;
import de.dafuqs.spectrum.blocks.memory.*;
import de.dafuqs.spectrum.blocks.mob_head.*;
import de.dafuqs.spectrum.blocks.spirit_instiller.*;
import de.dafuqs.spectrum.recipe.*;
import de.dafuqs.spectrum.recipe.spirit_instiller.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.block.*;
import net.minecraft.entity.*;
import net.minecraft.inventory.*;
import net.minecraft.item.*;
import net.minecraft.loot.*;
import net.minecraft.loot.context.*;
import net.minecraft.recipe.*;
import net.minecraft.registry.*;
import net.minecraft.server.world.*;
import net.minecraft.util.*;
import net.minecraft.util.math.*;

import java.util.*;

public class MemoryToHeadRecipe extends SpiritInstillerRecipe {
	
	public static final RecipeSerializer<MemoryToHeadRecipe> SERIALIZER = new EmptyRecipeSerializer<>(MemoryToHeadRecipe::new);
	
	public MemoryToHeadRecipe(Identifier identifier) {
		super(identifier, "", false, null,
				IngredientStack.ofItems(1, SpectrumBlocks.MEMORY), IngredientStack.ofItems(4, SpectrumItems.VEGETAL), IngredientStack.ofItems(4, SpectrumItems.QUITOXIC_POWDER),
				new ItemStack(Blocks.ZOMBIE_HEAD), 200, 1, true);
	}
	
	@Override
	public ItemStack craft(Inventory inv, DynamicRegistryManager drm) {
		ItemStack resultStack = ItemStack.EMPTY;
		if (inv instanceof SpiritInstillerBlockEntity spiritInstillerBlockEntity) {
			ServerWorld world = (ServerWorld) spiritInstillerBlockEntity.getWorld();
			BlockPos pos = spiritInstillerBlockEntity.getPos();
			
			/**
			 * This is moderately cursed
			 * we spawn the entity from the memory, process its loot table with a custom damage type that guarantees a head drop,
			 * search for a head drop in that loot and then discard that entity.
			 * WHY might you ask?
			 * Good question!
			 * A single entity type can have multiple head items associated with it (like fox or shulker variants)
			 * and finding out which exact mob variant is in that memory would be even more cursed
			 */
			Optional<Entity> optionalEntity = MemoryBlockEntity.hatchEntity(world, pos, spiritInstillerBlockEntity.getStack(0));
			if (optionalEntity.isPresent()) {
				if (optionalEntity.get() instanceof LivingEntity livingEntity) {
					LootTable lootTable = world.getServer().getLootManager().getLootTable(livingEntity.getLootTable());
					
					LootContextParameterSet.Builder builder = new LootContextParameterSet.Builder(world)
							.add(LootContextParameters.THIS_ENTITY, livingEntity)
							.add(LootContextParameters.ORIGIN, Vec3d.ofCenter(pos))
							.add(LootContextParameters.DAMAGE_SOURCE, SpectrumDamageTypes.mobHeadDrop(world)
							);
					
					LootContextParameterSet lootContextParameterSet = builder.build(LootContextTypes.ENTITY);
					List<ItemStack> loot = lootTable.generateLoot(lootContextParameterSet, livingEntity.getLootTableSeed());
					
					for (ItemStack stack : loot) {
						if (stack.isIn(SpectrumItemTags.SKULLS)) {
							resultStack = stack;
							break;
						}
					}
				}
				optionalEntity.get().discard();
			}
			
			spawnXPAndGrantAdvancements(resultStack, spiritInstillerBlockEntity, spiritInstillerBlockEntity.getUpgradeHolder(), world, pos);
		}
		
		return resultStack;
	}
	
	@Override
	public boolean canCraftWithStacks(Inventory inventory) {
		ItemStack instillerStack = inventory.getStack(0);
		return getSkullTypeForMemory(instillerStack).isPresent();
	}
	
	private static Optional<SkullBlock.SkullType> getSkullTypeForMemory(ItemStack instillerStack) {
		if (!(instillerStack.getItem() instanceof MemoryItem)) {
			return Optional.empty();
		}
		
		Optional<EntityType<?>> optionalMemoryEntity = MemoryItem.getEntityType(instillerStack.getNbt());
		if (optionalMemoryEntity.isEmpty()) {
			return Optional.empty();
		}
		
		return SpectrumSkullBlock.getSkullType(optionalMemoryEntity.get());
	}
	
}