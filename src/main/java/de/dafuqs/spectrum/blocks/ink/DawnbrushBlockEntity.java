package de.dafuqs.spectrum.blocks.ink;

import de.dafuqs.spectrum.api.ink.*;
import de.dafuqs.spectrum.api.ink.color.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.core.*;
import net.minecraft.network.chat.*;
import net.minecraft.server.level.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.state.*;

import java.util.*;

public class DawnbrushBlockEntity extends InkGeneratorBlockEntity {
	
	public DawnbrushBlockEntity(BlockPos blockPos, BlockState blockState) {
		super(SpectrumBlockEntities.DAWNBRUSH.get(), blockPos, blockState, 3);
	}
	
	@Override
	protected Component getDefaultName() {
		return Component.translatable("block.spectrum.dawnbrush");
	}
	
	@Override
	protected boolean tickLogic(Level level) {
		List<InkAmount> generatedEnergy = generateInk(level, 16.0F);
		for(InkAmount inkAmount : generatedEnergy) {
			this.inkStorage.addEnergy(inkAmount.color(), inkAmount.amount());
		}
		return true;
	}
	
	public static List<InkAmount> generateInk(Level level, float multiplier) {
		boolean raining = level.isRaining();
		boolean thundering = level.isThundering();
		
		float rain = raining ? 1f : 0f;
		float storm = thundering ? 1f : 0f;
		
		float timeOfDay = level.getTimeOfDay(1f);
		float sunrise  = ease(cosine(timeOfDay, 0.75f, 0.15f)); // sunrise peak
		float noon     = ease(cosine(timeOfDay, 0.0f, 0.3f) + cosine(timeOfDay, 1.0f, 0.3f)); // noon peak
		float sunset   = ease(cosine(timeOfDay, 0.25f, 0.15f)); // sunset peak
		float midnight = ease(cosine(timeOfDay, 0.5f, 0.3f)); // midnight peak
		
		Map<InkColor, Float> weights = new HashMap<>();
		
		// Thunder exclusive
		weights.put(InkColors.BLACK, thundering ? 1f + midnight * 3f + storm * 6f : 0f);
		
		// Rain exclusive
		weights.put(InkColors.BROWN, (raining || thundering) ? 1f + rain * 2f + storm * 2f : 0f);
		
		// Weather independent colors
		// partially suppressed with active weather
		float weatherWeight = 1.0F;
		if (raining) weatherWeight *= 0.25f;
		if (thundering) weatherWeight *= 0.25f;
		
		// sunrise
		weights.put(InkColors.ORANGE,     (midnight * 0.25f + sunrise * 3) * weatherWeight);
		weights.put(InkColors.LIME,       (sunrise * 4f) * weatherWeight);
		weights.put(InkColors.GREEN,      (sunrise * 2f + noon * 0.25f) * weatherWeight);
		
		// noon
		weights.put(InkColors.LIGHT_BLUE, (noon * 2.5f + rain) * weatherWeight);
		weights.put(InkColors.CYAN,       (noon * 2.5f - rain) * weatherWeight);
		
		// sunset
		weights.put(InkColors.YELLOW,     (noon * 0.25f + sunset * 3f) * weatherWeight);
		weights.put(InkColors.RED,        (sunset * 4f) * weatherWeight);
		weights.put(InkColors.PINK,       (sunset * 3f + midnight * 0.25f) * weatherWeight);
		
		// night
		weights.put(InkColors.PURPLE,     (sunset * 2 + midnight * 0.5f) * weatherWeight);
		weights.put(InkColors.BLUE,       (midnight * 2f) * weatherWeight);
		weights.put(InkColors.MAGENTA,    (midnight * 0.5f + sunrise * 2) * weatherWeight);
		
		// build output
		List<InkAmount> result = new ArrayList<>();
		for (var e : weights.entrySet()) {
			float w = e.getValue();
			if (w <= 0f) continue;
			result.add(new InkAmount(e.getKey(), (long) (w * multiplier)));
		}
		
		return result;
	}
	
	private static float cosine(float x, float center, float width) {
		float d = Math.abs(x - center);
		if (d > width) return 0f;
		return (float) ((Math.cos(Math.PI * d / width) + 1) * 0.5);
	}
	
	private static float ease(float x) {
		return x * x * (3 - 2 * x);
	}
	
}
