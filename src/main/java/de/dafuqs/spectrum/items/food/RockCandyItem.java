package de.dafuqs.spectrum.items.food;

import de.dafuqs.spectrum.api.item.*;
import de.dafuqs.spectrum.helpers.*;
import de.dafuqs.spectrum.particle.effect.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.core.component.*;
import net.minecraft.core.particles.*;
import net.minecraft.network.chat.*;
import net.minecraft.sounds.*;
import net.minecraft.util.*;
import net.minecraft.world.entity.item.*;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.*;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.*;
import net.minecraft.world.phys.*;
import org.jspecify.annotations.Nullable;

import java.util.*;
import java.util.function.*;

public class RockCandyItem extends Item {
	
	public static final int ITEM_SEARCH_RANGE = 5;
	public static final int REQUIRED_ITEM_COUNT_PER_STAGE = 4;
	
	protected static final Map<RockCandyVariant, Item> SUGAR_STICK_VARIANTS = new EnumMap<>(RockCandyVariant.class);
	protected static final Map<RockCandyVariant, Item> ROCK_CANDY_VARIANTS = new EnumMap<>(RockCandyVariant.class);
	
	protected final RockCandyVariant variant;
	protected final boolean fullyGrown;
	
	public RockCandyItem(Item.Properties properties, RockCandyVariant variant, boolean fullyGrown) {
		super(properties);
		this.variant = variant;
		this.fullyGrown = fullyGrown;
		
		if (fullyGrown) {
			ROCK_CANDY_VARIANTS.put(this.variant, this);
		} else {
			SUGAR_STICK_VARIANTS.put(this.variant, this);
		}
	}
	
	public RockCandyVariant getVariant() {
		return variant;
	}
	
	public static void tickSugarStickGrowing(ItemEntity itemEntity) {
		if (itemEntity.getAge() % 4 != 0 || itemEntity.hasPickUpDelay() || !itemEntity.isAlive()) {
			return;
		}
		
		ItemStack stack = itemEntity.getItem();
		if (!isIngredient(stack)) {
			return;
		}
		
		Level level = itemEntity.level();
		AABB boundingBox = itemEntity.getBoundingBox();
		
		if(level.isClientSide()) {
			if(!searchSecondaryIngredients(itemEntity, level, boundingBox).isEmpty()) {
				RandomSource random = level.getRandom();
				RockCandyVariant variant = stack.getItem() instanceof RockCandyItem rockCandyItem ? rockCandyItem.variant : RockCandyVariant.SUGAR;
				level.addParticle(ColoredCraftingParticleEffect.of(variant.getDyeColor().getFireworkColor()),
						Mth.nextDouble(random, boundingBox.minX, boundingBox.maxX),
						Mth.nextDouble(random, boundingBox.minY, boundingBox.maxY),
						Mth.nextDouble(random, boundingBox.minZ, boundingBox.maxZ),
						0.08 - random.nextFloat() * 0.16,
						0.04 - random.nextFloat() * 0.16,
						0.08 - random.nextFloat() * 0.16
				);
			}
			return;
		}
		
		if (itemEntity.getAge() % 200 != 0) {
			return;
		}
		
		List<ItemEntity> otherItemEntities = searchSecondaryIngredients(itemEntity, level, boundingBox);
		Collections.shuffle(otherItemEntities);
		for (ItemEntity otherItemEntity : otherItemEntities) {
			ItemStack otherStack = otherItemEntity.getItem();
			if (otherStack.getCount() >= REQUIRED_ITEM_COUNT_PER_STAGE) {
				@Nullable RockCandyVariant itemVariant = RockCandyVariant.getFor(otherStack);
				if (itemVariant != null) {
					Item newItem = stack.getItem() instanceof RockCandyItem ? ROCK_CANDY_VARIANTS.get(itemVariant) : SUGAR_STICK_VARIANTS.get(itemVariant);
					otherStack.shrink(REQUIRED_ITEM_COUNT_PER_STAGE);
					
					ItemStack newStack = new ItemStack(newItem);
					newStack.applyComponents(stack.getComponents());
					stack.shrink(1);
					
					Vec3 pos = itemEntity.position();
					level.playSound(null, pos.x(), pos.y(), pos.z(), itemVariant.getGrowSoundEvent(), SoundSource.BLOCKS, 0.9F + level.getRandom().nextFloat() * 0.2F, 0.9F + level.getRandom().nextFloat() * 0.2F);
					
					ItemEntity entity = new ItemEntity(level, pos.x, pos.y, pos.z, newStack);
					entity.setDefaultPickUpDelay();
					level.addFreshEntity(entity);
					
					break;
				}
			}
		}
	}
	
	// *technically* we should prob. check if the entities share the same fluid pool, but eh
	private static List<ItemEntity> searchSecondaryIngredients(ItemEntity itemEntity, Level level, AABB boundingBox) {
		return level.getEntitiesOfClass(ItemEntity.class, boundingBox.inflate(ITEM_SEARCH_RANGE),
				entity -> !entity.hasPickUpDelay() && entity.isAlive() && entity != itemEntity && entity.isInFluidType(SpectrumFluids.LIQUID_CRYSTAL_TYPE.get()) && RockCandyVariant.getFor(entity.getItem()) != null);
	}
	
	private static boolean isIngredient(ItemStack stack) {
		if(stack.is(Items.STICK)) {
			return true;
		}
		if(stack.getItem() instanceof RockCandyItem rockCandyItem) {
			return !rockCandyItem.fullyGrown;
		}
		return false;
	}
	
	public enum RockCandyVariant implements StringRepresentable {
		SUGAR(DyeColor.LIGHT_GRAY, SoundEvents.AMETHYST_BLOCK_CHIME),
		TOPAZ(DyeColor.CYAN, SpectrumSoundEvents.BLOCK_TOPAZ_BLOCK_CHIME),
		AMETHYST(DyeColor.MAGENTA, SoundEvents.AMETHYST_BLOCK_CHIME),
		CITRINE(DyeColor.YELLOW, SpectrumSoundEvents.BLOCK_CITRINE_BLOCK_CHIME),
		ONYX(DyeColor.BLACK, SpectrumSoundEvents.BLOCK_ONYX_BLOCK_CHIME),
		MOONSTONE(DyeColor.WHITE, SpectrumSoundEvents.BLOCK_MOONSTONE_BLOCK_CHIME);
		
		private final DyeColor dyeColor;
		private final SoundEvent growSoundEvent;
		
		RockCandyVariant(DyeColor dyeColor, SoundEvent growSoundEvent) {
			this.dyeColor = dyeColor;
			this.growSoundEvent = growSoundEvent;
		}
		
		public static @Nullable RockCandyVariant getFor(ItemStack stack) {
			Item item = stack.getItem();
			if (item == Items.SUGAR) {
				return RockCandyVariant.SUGAR;
			} else if (item == SpectrumItems.TOPAZ_POWDER.get()) {
				return RockCandyVariant.TOPAZ;
			} else if (item == SpectrumItems.AMETHYST_POWDER.get()) {
				return RockCandyVariant.AMETHYST;
			} else if (item == SpectrumItems.CITRINE_POWDER.get()) {
				return RockCandyVariant.CITRINE;
			} else if (item == SpectrumItems.ONYX_POWDER.get()) {
				return RockCandyVariant.ONYX;
			} else if (item == SpectrumItems.MOONSTONE_POWDER.get()) {
				return RockCandyVariant.MOONSTONE;
			}
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