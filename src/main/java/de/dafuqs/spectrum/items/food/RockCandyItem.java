package de.dafuqs.spectrum.items.food;

import de.dafuqs.spectrum.particle.effect.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.sounds.*;
import net.minecraft.util.*;
import net.minecraft.world.entity.item.*;
import net.minecraft.world.item.*;
import net.minecraft.world.level.*;
import net.minecraft.world.phys.*;
import org.jspecify.annotations.Nullable;

import java.util.*;

public class RockCandyItem extends Item {
	
	public static final int ITEM_SEARCH_RANGE = 5;
	public static final int REQUIRED_ITEM_COUNT_PER_STAGE = 4;
	
	protected static final Map<RockCandyVariant, Item> SUGAR_STICK_VARIANTS = new EnumMap<>(RockCandyVariant.class);
	protected static final Map<RockCandyVariant, Item>  ROCK_CANDY_VARIANTS = new EnumMap<>(RockCandyVariant.class);
	
	protected final RockCandyVariant variant;
	protected final boolean fullyGrown;
	
	public RockCandyItem(Item.Properties properties, RockCandyVariant variant, boolean fullyGrown) {
		super(properties);
		this.variant = variant;
		this.fullyGrown = fullyGrown;
		
		(fullyGrown ? ROCK_CANDY_VARIANTS : SUGAR_STICK_VARIANTS).put(variant, this);
	}
	
	public RockCandyVariant getVariant() {
		return variant;
	}
	
	public static void tickSugarStickGrowing(ItemEntity entity) {
		if (entity.getAge() % 4 != 0 || entity.hasPickUpDelay() || !entity.isAlive()) return;
		
		ItemStack stack = entity.getItem();
		if (!isIngredient(stack)) return;
		
		Level lvl = entity.level();
		AABB  box = entity.getBoundingBox();
		
		if (lvl.isClientSide()) {
			if (!searchSecondaryIngredients(entity, lvl, box).isEmpty()) {
				var random  = lvl.getRandom();
				var variant = stack.getItem() instanceof RockCandyItem item ? item.getVariant() : RockCandyVariant.SUGAR;
				
				lvl.addParticle(ColoredCraftingParticleEffect.of(variant.getDyeColor().getFireworkColor()),
						Mth.nextDouble(random, box.minX, box.maxX),
						Mth.nextDouble(random, box.minX, box.maxX),
						Mth.nextDouble(random, box.minX, box.maxX),
						0.08 - random.nextFloat() * 0.16,
						0.04 - random.nextFloat() * 0.16,
						0.08 - random.nextFloat() * 0.16);
			}
			return;
		}
		if (entity.getAge() % 200 != 0) return; // should probably be revised to depend on the randomTickSpeed gamerule (like the old logic was)
		
		List<ItemEntity> otherItemEntities = searchSecondaryIngredients(entity, lvl, box);
		Collections.shuffle(otherItemEntities);
		
		for (ItemEntity otherEntity : otherItemEntities) {
			ItemStack otherStack = otherEntity.getItem();
			if (otherStack.getCount() < REQUIRED_ITEM_COUNT_PER_STAGE) continue;
			
			RockCandyVariant itemVariant = RockCandyVariant.getFor(otherStack);
			if (itemVariant == null) continue;
			
			Item newItem = (stack.getItem() instanceof RockCandyItem ? ROCK_CANDY_VARIANTS : SUGAR_STICK_VARIANTS).get(itemVariant);
			otherStack.shrink(REQUIRED_ITEM_COUNT_PER_STAGE);
			
			ItemStack newStack = new ItemStack(newItem);
			newStack.applyComponents(stack.getComponents());
			stack.shrink(1);
			
			Vec3 pos = entity.position();
			lvl.playSound(null, pos.x(), pos.y(), pos.z(), itemVariant.getGrowSoundEvent(), SoundSource.BLOCKS, 0.9F + lvl.getRandom().nextFloat() * 0.2F, 0.9F + lvl.getRandom().nextFloat() * 0.2F);
			
			ItemEntity newEntity = new ItemEntity(lvl, pos.x, pos.y, pos.z, newStack);
			newEntity.setDefaultPickUpDelay();
			lvl.addFreshEntity(newEntity);
			break;
		}
	}
	
	// *technically* we should prob. check if the entities share the same fluid pool, but eh
	private static List<ItemEntity> searchSecondaryIngredients(ItemEntity itemEntity, Level level, AABB box) {
		return level.getEntitiesOfClass(ItemEntity.class, box.inflate(ITEM_SEARCH_RANGE),
				entity -> !entity.hasPickUpDelay() && entity.isAlive() && entity != itemEntity && entity.isEyeInFluid(SpectrumFluidTags.LIQUID_CRYSTAL) && RockCandyVariant.getFor(entity.getItem()) != null);
	}
	
	public static boolean isIngredient(ItemStack stack) {
		return stack.is(Items.STICK) || stack.getItem() instanceof RockCandyItem rockCandyItem && !rockCandyItem.fullyGrown;
	}
	
	public enum RockCandyVariant implements StringRepresentable {
		SUGAR(DyeColor.LIGHT_GRAY, SoundEvents.AMETHYST_BLOCK_CHIME),
		AMETHYST(DyeColor.MAGENTA, SoundEvents.AMETHYST_BLOCK_CHIME),
		CITRINE(DyeColor.YELLOW, SpectrumSoundEvents.BLOCK_CITRINE_BLOCK_CHIME),
		TOPAZ(DyeColor.CYAN, SpectrumSoundEvents.BLOCK_TOPAZ_BLOCK_CHIME),
		ONYX(DyeColor.BLACK, SpectrumSoundEvents.BLOCK_ONYX_BLOCK_CHIME),
		MOONSTONE(DyeColor.WHITE, SpectrumSoundEvents.BLOCK_MOONSTONE_BLOCK_CHIME);
		
		private final DyeColor dyeColor;
		private final SoundEvent growSoundEvent;
		
		RockCandyVariant(DyeColor dyeColor, SoundEvent growSoundEvent) {
			this.dyeColor = dyeColor;
			this.growSoundEvent = growSoundEvent;
		}
		
		public static @Nullable RockCandyVariant getFor(ItemStack itemStack) {
			Item item = itemStack.getItem();
			if (item == 		Items.SUGAR)			return SUGAR;
			if (item == SpectrumItems.TOPAZ_POWDER)		return TOPAZ;
			if (item == SpectrumItems.AMETHYST_POWDER)  return AMETHYST;
			if (item == SpectrumItems.CITRINE_POWDER)   return CITRINE;
			if (item == SpectrumItems.ONYX_POWDER)		return ONYX;
			if (item == SpectrumItems.MOONSTONE_POWDER) return MOONSTONE;
			return null;
		}
		
		@Override
		public String getSerializedName() {
			return this.toString().toLowerCase(Locale.ROOT);
		}
		
		public DyeColor getDyeColor() {
			return this.dyeColor;
		}
		
		public SoundEvent getGrowSoundEvent() {
			return this.growSoundEvent;
		}
	}
}