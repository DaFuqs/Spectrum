package de.dafuqs.spectrum.recipe.spirit_instiller.dynamic;

import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.api.recipe.*;
import de.dafuqs.spectrum.blocks.memory.*;
import de.dafuqs.spectrum.blocks.mob_head.*;
import de.dafuqs.spectrum.blocks.spirit_instiller.*;
import de.dafuqs.spectrum.recipe.*;
import de.dafuqs.spectrum.recipe.spirit_instiller.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.core.*;
import net.minecraft.core.component.*;
import net.minecraft.nbt.*;
import net.minecraft.network.chat.*;
import net.minecraft.server.level.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.*;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.storage.loot.*;
import net.minecraft.world.level.storage.loot.parameters.*;
import net.minecraft.world.phys.*;

import java.util.*;

public class MemoryToHeadRecipe extends SpiritInstillerRecipe {
	
	public MemoryToHeadRecipe() {
		super("", Optional.of(SpectrumCommon.locate("unlocks/memory_to_head")), Optional.empty(), List.of(),
				getMemoryStack(), IngredientStack.ofItems(SpectrumItems.VEGETAL.get(), 4), IngredientStack.ofItems(SpectrumItems.QUITOXIC_POWDER.get(), 4),
				new ItemStack(Blocks.ZOMBIE_HEAD), 200, 1, true, false);
	}
	
	private static IngredientStack getMemoryStack() {
		CompoundTag nbt = new CompoundTag();
		nbt.putString("id", "minecraft:zombie");
		
		DataComponentPatch previewPatch = DataComponentPatch.builder()
				.set(DataComponents.LORE, new ItemLore(List.of(Component.translatable("recipe.spectrum.memory_to_head.lore"))))
				.set(DataComponents.ENTITY_DATA, CustomData.of(nbt)).build();
		
		return new IngredientStack(Ingredient.of(SpectrumBlocks.MEMORY), DataComponentPredicate.EMPTY, previewPatch, 1);
	}
	
	@Override
	public RecipeSerializer<?> getSerializer() {
		return SpectrumRecipeSerializers.SPIRIT_INSTILLER_MEMORY_TO_HEAD;
	}
	
	@Override
	public ItemStack assemble(InstanceRecipeInput<SpiritInstillerBlockEntity> recipeInput, HolderLookup.Provider drm) {
		SpiritInstillerBlockEntity spiritInstillerBlockEntity = recipeInput.getInstance();
		ItemStack resultStack = ItemStack.EMPTY;
		ServerLevel level = (ServerLevel) spiritInstillerBlockEntity.getLevel();
		BlockPos pos = spiritInstillerBlockEntity.getBlockPos();
		
		Optional<ResolvableProfile> profile = MemoryItem.getPlayer(recipeInput.getItem(0));
		if (profile.isPresent()) {
			resultStack = new ItemStack(Items.PLAYER_HEAD);
			resultStack.set(DataComponents.PROFILE, profile.get());
		} else {
			/*
			 * This is moderately cursed
			 * we spawn the entity from the memory, process its loot table with a custom damage type that guarantees a head drop,
			 * search for a head drop in that loot and then discard that entity.
			 * WHY might you ask?
			 * Good question!
			 * A single entity type can have multiple head items associated with it (like fox or shulker variants)
			 * and finding out which exact mob variant is in that memory would be even more cursed
			 */
			Optional<Entity> optionalEntity = MemoryBlockEntity.hatchEntity(level, pos, spiritInstillerBlockEntity.getItem(0));
			if (optionalEntity.isPresent()) {
				if (optionalEntity.get() instanceof LivingEntity livingEntity) {
					LootTable lootTable = level.getServer().reloadableRegistries().getLootTable(livingEntity.getLootTable());
					
					LootParams.Builder builder = new LootParams.Builder(level)
							.withParameter(LootContextParams.THIS_ENTITY, livingEntity)
							.withParameter(LootContextParams.ORIGIN, Vec3.atCenterOf(pos))
							.withParameter(LootContextParams.DAMAGE_SOURCE, SpectrumDamageTypes.mobHeadDrop(level));
					
					LootParams lootContextParameterSet = builder.create(LootContextParamSets.ENTITY);
					List<ItemStack> loot = lootTable.getRandomItems(lootContextParameterSet, livingEntity.getLootTableSeed());
					
					for (ItemStack stack : loot) {
						if (stack.is(SpectrumItemTags.SKULLS)) {
							resultStack = stack;
							break;
						}
					}
				}
				optionalEntity.get().discard();
			}
		}
		
		spawnXPAndGrantAdvancements(resultStack, spiritInstillerBlockEntity, spiritInstillerBlockEntity.getUpgradeHolder(), level, pos);
		return resultStack;
	}
	
	@Override
	public boolean canCraftWithStacks(InstanceRecipeInput<SpiritInstillerBlockEntity> inventory) {
		ItemStack instillerStack = inventory.getItem(0);
		return getSkullTypeForMemory(instillerStack).isPresent();
	}
	
	private static Optional<SkullBlock.Type> getSkullTypeForMemory(ItemStack instillerStack) {
		if (!(instillerStack.getItem() instanceof MemoryItem)) {
			return Optional.empty();
		}
		
		Optional<ResolvableProfile> player = MemoryItem.getPlayer(instillerStack);
		if (player.isPresent()) {
			return Optional.of(SkullBlock.Types.PLAYER);
		}
		
		Optional<EntityType<?>> optionalMemoryEntity = MemoryItem.getEntityType(instillerStack);
		return optionalMemoryEntity.flatMap(SpectrumSkullBlock::getSkullType);
	}
	
}