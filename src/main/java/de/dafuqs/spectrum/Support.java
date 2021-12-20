package de.dafuqs.spectrum;

import de.dafuqs.spectrum.items.PigmentItem;
import de.dafuqs.spectrum.progression.ClientAdvancements;
import net.minecraft.advancement.Advancement;
import net.minecraft.advancement.PlayerAdvancementTracker;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.DyeItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.server.ServerAdvancementLoader;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.tag.Tag;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.registry.Registry;
import org.apache.logging.log4j.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

public class Support {
	
	public static final List<Vec3d> VECTORS_4 = new ArrayList<>() {{
		add(new Vec3d(1.0D, 0, 0.0D));
		add(new Vec3d(0.0D, 0, 1.0D));
		add(new Vec3d(-1.0, 0, 0.0D));
		add(new Vec3d(0.0D, 0, -1.0D));
	}};
	
	public static final List<Vec3d> VECTORS_8 = new ArrayList<>() {{
			add(new Vec3d(1.0D, 0, 0.0D));
			add(new Vec3d(0.7D, 0, 0.7D));
			add(new Vec3d(0.0D, 0, 1.0D));
			add(new Vec3d(-0.7D, 0, 0.7D));
			add(new Vec3d(-1.0D, 0, 0.0D));
			add(new Vec3d(-0.7D, 0, -0.7D));
			add(new Vec3d(0.0D, 0, -1.0D));
			add(new Vec3d(0.7D, 0, -0.7D));
		}};
	
	public static final List<Vec3d> VECTORS_16 = new ArrayList<>() {{
			add(new Vec3d(1.0D, 0, 0.0D));
			add(new Vec3d(0.75D, 0, 0.5D));
			add(new Vec3d(0.7D, 0, 0.7D));
			add(new Vec3d(0.5D, 0, 0.75D));
			add(new Vec3d(0.0D, 0, 1.0D));
			add(new Vec3d(-0.5D, 0, 0.75D));
			add(new Vec3d(-0.7D, 0, 0.7D));
			add(new Vec3d(-0.75D, 0, 0.5D));
			add(new Vec3d(-1.0D, 0, 0.0D));
			add(new Vec3d(-0.75D, 0, 0.5D));
			add(new Vec3d(-0.7D, 0, -0.7D));
			add(new Vec3d(-0.5D, 0, -0.75D));
			add(new Vec3d(0.0D, 0, -1.0D));
			add(new Vec3d(0.5D, 0, -0.75D));
			add(new Vec3d(0.7D, 0, -0.7D));
			add(new Vec3d(0.75D, 0, -0.5D));
		}};
	
	public static ItemStack getEnchantedItemStack(Item item, Enchantment enchantment, int level) {
		ItemStack itemStack = new ItemStack(item);
		itemStack.addEnchantment(enchantment, level);
		return itemStack;
	}
	
	public static boolean hasTag(@NotNull BlockState blockState, @NotNull Tag<Block> tag) {
		return tag.contains(blockState.getBlock());
	}

	public static Optional<Tag> getFirstMatchingTag(@NotNull Block block, @NotNull List<Tag<Block>> tags) {
		for(Tag tag : tags) {
			if(tag.contains(block)) {
				return Optional.of(tag);
			}
		}
		return Optional.empty();
	}
	
	public static int getExperienceOrbSizeForExperience(int experience) {
		if (experience >= 2477) {
			return 10;
		} else if (experience >= 1237) {
			return 9;
		} else if (experience >= 617) {
			return 8;
		} else if (experience >= 307) {
			return 7;
		} else if (experience >= 149) {
			return 6;
		} else if (experience >= 73) {
			return 5;
		} else if (experience >= 37) {
			return 4;
		} else if (experience >= 17) {
			return 3;
		} else if (experience >= 7) {
			return 2;
		} else {
			return experience >= 3 ? 1 : 0;
		}
	}
	
	private static final DecimalFormat df = new DecimalFormat("0.00");
	
	public static String getShortenedNumberString(double number) {
		if(number > 1000000D) {
			return df.format(number / 1000000D) + "M";
		} else if(number > 1000D) {
			return df.format(number / 1000D) + "K";
		} else {
			return df.format(number);
		}
	}
	
	public static void givePlayer(PlayerEntity playerEntity, ItemStack itemStack) {
		boolean insertInventorySuccess = playerEntity.getInventory().insertStack(itemStack);
		ItemEntity itemEntity;
		if (insertInventorySuccess && itemStack.isEmpty()) {
			itemStack.setCount(1);
			itemEntity = playerEntity.dropItem(itemStack, false);
			if (itemEntity != null) {
				itemEntity.setDespawnImmediately();
			}
			
			playerEntity.world.playSound(null, playerEntity.getX(), playerEntity.getY(), playerEntity.getZ(), SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.PLAYERS, 0.2F, ((playerEntity.getRandom().nextFloat() - playerEntity.getRandom().nextFloat()) * 0.7F + 1.0F) * 2.0F);
			playerEntity.currentScreenHandler.sendContentUpdates();
		} else {
			itemEntity = playerEntity.dropItem(itemStack, false);
			if (itemEntity != null) {
				itemEntity.resetPickupDelay();
				itemEntity.setOwner(playerEntity.getUuid());
			}
		}
	}
	
	public static int getIntFromDecimalWithChance(double d, @NotNull Random random) {
		boolean roundUp = (random.nextFloat() < d % 1);
		if(roundUp) {
			return ((int) d) + 1;
		} else {
			return (int) d;
		}
	}

	public static void grantAdvancementCriterion(@NotNull ServerPlayerEntity serverPlayerEntity, String advancementString, String criterion) {
		ServerAdvancementLoader sal = SpectrumCommon.minecraftServer.getAdvancementLoader();
		PlayerAdvancementTracker tracker = serverPlayerEntity.getAdvancementTracker();

		// grant group advancement
		Identifier advancementIdentifier = new Identifier(SpectrumCommon.MOD_ID, advancementString);
		Advancement advancement = sal.get(advancementIdentifier);
		if (advancement != null) {
			tracker.grantCriterion(advancement, criterion);
		} else {
			SpectrumCommon.log(Level.ERROR, "Trying to grant a criterion \"" + criterion +  "\" for an advancement that does not exist: " + advancementIdentifier);
		}
	}

	public static boolean hasAdvancement(@NotNull PlayerEntity playerEntity, @NotNull Identifier advancementIdentifier) {
		if(playerEntity == null) {
			return false;
		} else if(advancementIdentifier == null) {
			return true;
		}
		
		if (playerEntity instanceof ServerPlayerEntity) {
			Advancement advancement = SpectrumCommon.minecraftServer.getAdvancementLoader().get(advancementIdentifier);
			if (advancement == null) {
				SpectrumCommon.log(Level.ERROR, "Player " + playerEntity.getName() + " was getting an advancement check for an advancement that does not exist: " + advancementIdentifier.toString());
				return false;
			} else {
				return ((ServerPlayerEntity) playerEntity).getAdvancementTracker().getProgress(advancement).isDone();
			}
		} else {
			return ClientAdvancements.hasDone(advancementIdentifier);
		}
	}


	public static @NotNull String getReadableDimensionString(@NotNull String dimensionKeyString) {
		switch (dimensionKeyString) {
			case "minecraft:overworld":
				return "Overworld";
			case "minecraft:nether":
				return "Nether";
			case "minecraft:end":
				return "End";
			default:
				if(dimensionKeyString.contains(":")) {
					return dimensionKeyString.substring(0, dimensionKeyString.indexOf(":"));
				} else {
					return dimensionKeyString;
				}
		}
	}

	public static Optional<DyeColor> getDyeColorOfItemStack(ItemStack itemStack) {
		if(!itemStack.isEmpty()) {
			Item item = itemStack.getItem();
			if(item instanceof DyeItem dyeItem) {
				return Optional.of(dyeItem.getColor());
			} else if(item instanceof PigmentItem pigmentItem) {
				return Optional.of(pigmentItem.getColor());
			}
		}
		return Optional.empty();
	}
	
}