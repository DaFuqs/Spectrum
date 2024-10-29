package de.dafuqs.spectrum.worldgen.features;

import com.mojang.serialization.*;
import de.dafuqs.spectrum.blocks.deeper_down.groundcover.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.state.property.*;
import net.minecraft.util.math.*;
import net.minecraft.util.math.intprovider.*;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.*;
import net.minecraft.world.gen.feature.*;
import net.minecraft.world.gen.feature.util.*;

import java.util.*;

public class AshDunesFeature extends Feature<AshDunesFeatureConfig> {
	
	private static final IntProperty LAYERS = AshPileBlock.LAYERS;
	
	public AshDunesFeature(Codec<AshDunesFeatureConfig> configCodec) {
		super(configCodec);
	}
	
	@Override
	public boolean generate(FeatureContext<AshDunesFeatureConfig> context) {
		var origin = context.getOrigin();
		var config = context.getConfig();
		var random = context.getRandom();
		var world = context.getWorld();
		var bias = random.nextBoolean();
		
		var spreadProvider = config.nodeSpread();
		var strengthProvider = config.emitterStrength();
		var nodeQuantity = config.nodeQuantity().get(random);
		var cutoutQuantity = config.cutoutQuantity().get(random);
		var decay = config.emitterDecayModifier();
		List<Emitter> emitters = new ArrayList<>();
		
		for (int i = 0; i < nodeQuantity; i++) {
			generateEmitter(origin, false, bias, spreadProvider, random, world, emitters, strengthProvider.get(random));
		}
		
		if (emitters.isEmpty()) {
			return false;
		}
		
		for (int i = 0; i < cutoutQuantity; i++) {
			generateEmitter(origin, true, bias, spreadProvider, random, world, emitters, strengthProvider.get(random) / 1.667F);
		}
		
		emitters.add(new Emitter(origin.mutableCopy(), strengthProvider.get(random), false));
		var placementArea = spreadProvider.getMax() + Math.round(strengthProvider.getMax() / decay);
		var iterator = BlockPos.iterateOutwards(origin, placementArea, 0, placementArea).iterator();
		var anyPlaced = false;
		
		while (iterator.hasNext()) {
			var placementPos = iterator.next().mutableCopy();
			var originalY = placementPos.getY();
			
			if (!world.getBlockState(placementPos).isOf(SpectrumBlocks.ASH_PILE) && !canPlaceAt(world, placementPos) && !adjustPlacementHeight(world, placementPos, placementArea / 3))
				continue;
			
			var height = Math.round(getStrengthAt(placementPos, origin, emitters, originalY, placementArea, decay, config.emitterCutoutModifier()));
			
			if (height <= 0)
				continue;
			
			placeAsh(world, placementPos, height);
			anyPlaced = true;
		}
		
		return anyPlaced;
	}
	
	private static void generateEmitter(BlockPos origin, boolean cutout, boolean bias, IntProvider spreadProvider, Random random, StructureWorldAccess world, List<Emitter> emitters, float strength) {
		var potentialNode = origin.add(Math.round(spreadProvider.get(random) * (random.nextBoolean() ? 1 : -1) * (bias ? 0.667F : 1F)), 0, Math.round(spreadProvider.get(random) * (random.nextBoolean() ? 1 : -1) * (!bias ? 0.667F : 1F))).mutableCopy();
		
		if (world.getBlockState(potentialNode).isAir()
				&& world.getBlockState(potentialNode.add(0, -1, 0)).isAir()
				&& !world.getBlockState(potentialNode.add(0, -2, 0)).isAir()) {
			emitters.add(new Emitter(potentialNode.move(0, -1, 0), strength, cutout));
			return;
		}
		
		while (!world.getBlockState(potentialNode).isAir()) {
			potentialNode.move(0, 1, 0);
			
			if (world.getBlockState(potentialNode).isAir()) {
				emitters.add(new Emitter(potentialNode, strength, cutout));
				break;
			} else if (potentialNode.getY() - origin.getY() > spreadProvider.getMax() / 2) {
				break;
			}
		}
	}
	
	private void placeAsh(StructureWorldAccess world, BlockPos.Mutable pos, int height) {
		var state = world.getBlockState(pos);
		if (state.isOf(SpectrumBlocks.ASH_PILE)) {
			var layers = state.get(LAYERS);
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
	
	private void placeAshBlock(StructureWorldAccess world, BlockPos.Mutable pos, int height) {
		if (height == 8) {
			setBlockState(world, pos, SpectrumBlocks.ASH.getDefaultState());
		} else {
			setBlockState(world, pos, SpectrumBlocks.ASH_PILE.getDefaultState().with(LAYERS, height));
		}
	}
	
	private float getStrengthAt(BlockPos pos, BlockPos origin, List<Emitter> emitters, int originalY, float maxArea, float decay, float cutoutDecay) {
		float strength = 0F;
		
		for (Emitter emitter : emitters) {
			if (emitter.cutout) {
				var cutoutStrength = (float) Math.sqrt(pos.getSquaredDistance(emitter.pos));
				cutoutStrength *= -cutoutDecay;
				cutoutStrength += emitter.strength;
				
				if (cutoutStrength > 0) {
					strength -= cutoutStrength;
				}
			} else {
				var emitterStrength = (float) Math.sqrt(pos.getSquaredDistance(emitter.pos));
				emitterStrength *= -decay;
				emitterStrength += emitter.strength;
				
				if (emitterStrength > 0)
					strength += emitterStrength;
			}
		}
		
		strength = (float) MathHelper.clampedLerp(strength, 0F, Math.sqrt(pos.getSquaredDistance(origin)) / maxArea);
		return strength;
	}
	
	private boolean adjustPlacementHeight(StructureWorldAccess world, BlockPos.Mutable pos, int maxShifts) {
		var foundValidSpace = false;
		
		for (int shifts = 1; shifts < maxShifts + 1; shifts++) {
			var upPos = pos.add(0, shifts, 0);
			if (canPlaceAt(world, upPos) || world.getBlockState(pos).isOf(SpectrumBlocks.ASH_PILE)) {
				pos.move(0, shifts, 0);
				foundValidSpace = true;
				break;
			}
			
			var downPos = pos.add(0, -shifts, 0);
			if (canPlaceAt(world, downPos) || world.getBlockState(pos).isOf(SpectrumBlocks.ASH_PILE)) {
				pos.move(0, -shifts, 0);
				foundValidSpace = true;
				break;
			}
		}
		
		return foundValidSpace;
	}
	
	private static boolean canPlaceAt(StructureWorldAccess world, BlockPos pos) {
		return (world.isAir(pos) || world.getBlockState(pos).isOf(SpectrumBlocks.VARIA_SPROUT)) && SpectrumBlocks.ASH_PILE.getDefaultState().canPlaceAt(world, pos);
	}
	
	private record Emitter(BlockPos.Mutable pos, float strength, boolean cutout) {
	}
}

