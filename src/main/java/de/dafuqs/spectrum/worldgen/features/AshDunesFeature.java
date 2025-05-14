package de.dafuqs.spectrum.worldgen.features;

import com.mojang.serialization.*;
import de.dafuqs.spectrum.blocks.deeper_down.groundcover.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.core.*;
import net.minecraft.util.*;
import net.minecraft.util.valueproviders.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.state.properties.*;
import net.minecraft.world.level.levelgen.feature.*;

import java.util.*;

public class AshDunesFeature extends Feature<AshDunesFeatureConfig> {
	
	private static final IntegerProperty LAYERS = AshPileBlock.LAYERS;
	
	public AshDunesFeature(Codec<AshDunesFeatureConfig> configCodec) {
		super(configCodec);
	}
	
	@Override
	public boolean place(FeaturePlaceContext<AshDunesFeatureConfig> context) {
		var origin = context.origin();
		var config = context.config();
		var random = context.random();
		var world = context.level();
		var bias = random.nextBoolean();
		
		var spreadProvider = config.nodeSpread();
		var strengthProvider = config.emitterStrength();
		var nodeQuantity = config.nodeQuantity().sample(random);
		var cutoutQuantity = config.cutoutQuantity().sample(random);
		var decay = config.emitterDecayModifier();
		List<Emitter> emitters = new ArrayList<>();
		
		for (int i = 0; i < nodeQuantity; i++) {
			generateEmitter(origin, false, bias, spreadProvider, random, world, emitters, strengthProvider.sample(random));
		}
		
		if (emitters.isEmpty()) {
			return false;
		}
		
		for (int i = 0; i < cutoutQuantity; i++) {
			generateEmitter(origin, true, bias, spreadProvider, random, world, emitters, strengthProvider.sample(random) / 1.667F);
		}
		
		emitters.add(new Emitter(origin.mutable(), strengthProvider.sample(random), false));
		var placementArea = spreadProvider.getMaxValue() + Math.round(strengthProvider.getMaxValue() / decay);
		var iterator = BlockPos.withinManhattan(origin, placementArea, 0, placementArea).iterator();
		var anyPlaced = false;
		
		while (iterator.hasNext()) {
			var placementPos = iterator.next().mutable();
			var originalY = placementPos.getY();
			
			if (!world.getBlockState(placementPos).is(SpectrumBlocks.ASH_PILE) && !canPlaceAt(world, placementPos) && !adjustPlacementHeight(world, placementPos, placementArea / 3))
				continue;
			
			var height = Math.round(getStrengthAt(placementPos, origin, emitters, originalY, placementArea, decay, config.emitterCutoutModifier()));
			
			if (height <= 0)
				continue;
			
			placeAsh(world, placementPos, height);
			anyPlaced = true;
		}
		
		return anyPlaced;
	}
	
	private static void generateEmitter(BlockPos origin, boolean cutout, boolean bias, IntProvider spreadProvider, RandomSource random, WorldGenLevel world, List<Emitter> emitters, float strength) {
		var potentialNode = origin.offset(Math.round(spreadProvider.sample(random) * (random.nextBoolean() ? 1 : -1) * (bias ? 0.667F : 1F)), 0, Math.round(spreadProvider.sample(random) * (random.nextBoolean() ? 1 : -1) * (!bias ? 0.667F : 1F))).mutable();
		
		if (world.getBlockState(potentialNode).isAir()
				&& world.getBlockState(potentialNode.offset(0, -1, 0)).isAir()
				&& !world.getBlockState(potentialNode.offset(0, -2, 0)).isAir()) {
			emitters.add(new Emitter(potentialNode.move(0, -1, 0), strength, cutout));
			return;
		}
		
		while (!world.getBlockState(potentialNode).isAir()) {
			potentialNode.move(0, 1, 0);
			
			if (world.getBlockState(potentialNode).isAir()) {
				emitters.add(new Emitter(potentialNode, strength, cutout));
				break;
			} else if (potentialNode.getY() - origin.getY() > spreadProvider.getMaxValue() / 2) {
				break;
			}
		}
	}
	
	private void placeAsh(WorldGenLevel world, BlockPos.MutableBlockPos pos, int height) {
		var state = world.getBlockState(pos);
		if (state.is(SpectrumBlocks.ASH_PILE)) {
			var layers = state.getValue(LAYERS);
			var layerDif = 8 - layers;
			
			if (height >= layerDif) {
				height -= layerDif;
				placeAshBlock(world, pos, 8);
				
				if (height == 0)
					return;
			} else {
				placeAshBlock(world, pos, layers + height);
				return;
			}
			
			pos.move(Direction.UP);
		}
		
		if (height <= 8) {
			placeAshBlock(world, pos, height);
			return;
		}
		
		while (height > 0) {
			if (height > 8) {
				placeAshBlock(world, pos, 8);
				pos.move(Direction.UP);
				height -= 8;
			} else {
				placeAshBlock(world, pos, height);
				height = 0;
			}
		}
	}
	
	private void placeAshBlock(WorldGenLevel world, BlockPos.MutableBlockPos pos, int height) {
		if (height == 8) {
			setBlock(world, pos, SpectrumBlocks.ASH.defaultBlockState());
		} else {
			setBlock(world, pos, SpectrumBlocks.ASH_PILE.defaultBlockState().setValue(LAYERS, height));
		}
	}
	
	private float getStrengthAt(BlockPos pos, BlockPos origin, List<Emitter> emitters, int originalY, float maxArea, float decay, float cutoutDecay) {
		float strength = 0F;
		
		for (Emitter emitter : emitters) {
			if (emitter.cutout) {
				var cutoutStrength = (float) Math.sqrt(pos.distSqr(emitter.pos));
				cutoutStrength *= -cutoutDecay;
				cutoutStrength += emitter.strength;
				
				if (cutoutStrength > 0) {
					strength -= cutoutStrength;
				}
			} else {
				var emitterStrength = (float) Math.sqrt(pos.distSqr(emitter.pos));
				emitterStrength *= -decay;
				emitterStrength += emitter.strength;
				
				if (emitterStrength > 0)
					strength += emitterStrength;
			}
		}
		
		strength = (float) Mth.clampedLerp(strength, 0F, Math.sqrt(pos.distSqr(origin)) / maxArea);
		return strength;
	}
	
	private boolean adjustPlacementHeight(WorldGenLevel world, BlockPos.MutableBlockPos pos, int maxShifts) {
		var foundValidSpace = false;
		
		for (int shifts = 1; shifts < maxShifts + 1; shifts++) {
			var upPos = pos.offset(0, shifts, 0);
			if (canPlaceAt(world, upPos) || world.getBlockState(pos).is(SpectrumBlocks.ASH_PILE)) {
				pos.move(0, shifts, 0);
				foundValidSpace = true;
				break;
			}
			
			var downPos = pos.offset(0, -shifts, 0);
			if (canPlaceAt(world, downPos) || world.getBlockState(pos).is(SpectrumBlocks.ASH_PILE)) {
				pos.move(0, -shifts, 0);
				foundValidSpace = true;
				break;
			}
		}
		
		return foundValidSpace;
	}
	
	private static boolean canPlaceAt(WorldGenLevel world, BlockPos pos) {
		return (world.isEmptyBlock(pos) || world.getBlockState(pos).is(SpectrumBlocks.VARIA_SPROUT)) && SpectrumBlocks.ASH_PILE.defaultBlockState().canSurvive(world, pos);
	}
	
	private record Emitter(BlockPos.MutableBlockPos pos, float strength, boolean cutout) {
	}
}

