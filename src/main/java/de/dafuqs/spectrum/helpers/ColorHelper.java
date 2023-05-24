package de.dafuqs.spectrum.helpers;

import de.dafuqs.spectrum.energy.color.*;
import de.dafuqs.spectrum.entity.entity.*;
import de.dafuqs.spectrum.items.*;
import net.minecraft.entity.*;
import net.minecraft.entity.mob.*;
import net.minecraft.entity.passive.*;
import net.minecraft.entity.player.*;
import net.minecraft.item.*;
import net.minecraft.sound.*;
import net.minecraft.util.*;
import org.jetbrains.annotations.*;
import org.joml.Vector3f;

import java.awt.*;
import java.util.*;

public class ColorHelper {
	
	public static Vector3f getRGBVec(DyeColor dyeColor) {
		return InkColor.of(dyeColor).getColor();
	}
	
	public static int getInt(DyeColor dyeColor) {
		Vector3f vec = getRGBVec(dyeColor);
		return new Color(vec.x(), vec.y(), vec.z()).getRGB() & 0x00FFFFFF;
	}
	
	/**
	 * Returns a nicely saturated random color based on seed
	 *
	 * @param seed the seed to base the random color on
	 * @return the color
	 */
	public static int getRandomColor(int seed) {
		return Color.getHSBColor((float) seed / Integer.MAX_VALUE, 0.7F, 0.9F).getRGB();
	}
	
	@NotNull
	public static Vector3f colorIntToVec(int color) {
		Color colorObj = new Color(color);
		float[] argb = new float[4];
		colorObj.getColorComponents(argb);
		return new Vector3f(argb[0], argb[1], argb[2]);
	}

	public static Optional<DyeColor> getDyeColorOfItemStack(@NotNull ItemStack itemStack) {
		if (!itemStack.isEmpty()) {
			Item item = itemStack.getItem();
			if (item instanceof DyeItem dyeItem) {
				return Optional.of(dyeItem.getColor());
			} else if (item instanceof PigmentItem pigmentItem) {
				return Optional.of(pigmentItem.getColor());
			}
		}
		return Optional.empty();
	}
	
	public static boolean tryColorEntity(PlayerEntity user, Entity entity, DyeColor dyeColor) {
		if (entity instanceof SheepEntity sheepEntity && sheepEntity.isAlive() && !sheepEntity.isSheared()) {
			if (sheepEntity.getColor() != dyeColor) {
				sheepEntity.world.playSoundFromEntity(user, sheepEntity, SoundEvents.ITEM_DYE_USE, SoundCategory.PLAYERS, 1.0F, 1.0F);
				sheepEntity.setColor(dyeColor);
				return true;
			}
		} else if (entity instanceof EggLayingWoolyPigEntity woolyPig && woolyPig.isAlive()) {
			if (woolyPig.getColor() != dyeColor) {
				woolyPig.world.playSoundFromEntity(user, woolyPig, SoundEvents.ITEM_DYE_USE, SoundCategory.PLAYERS, 1.0F, 1.0F);
				woolyPig.setColor(dyeColor);
				return true;
			}
		} else if (entity instanceof ShulkerEntity shulkerEntity && shulkerEntity.isAlive()) {
			if (shulkerEntity.getColor() != dyeColor) {
				shulkerEntity.world.playSoundFromEntity(user, shulkerEntity, SoundEvents.ITEM_DYE_USE, SoundCategory.PLAYERS, 1.0F, 1.0F);
				shulkerEntity.setVariant(Optional.of(dyeColor));
				return true;
			}
		}
		return false;
	}
	
}