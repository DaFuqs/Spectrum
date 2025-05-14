package de.dafuqs.spectrum.registries.client;

import net.minecraft.data.models.model.*;
import net.minecraft.resources.*;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.*;

import static de.dafuqs.spectrum.registries.client.SpectrumTextureKeys.*;
import static net.minecraft.data.models.model.TextureMapping.*;
import static net.minecraft.data.models.model.TextureSlot.*;

public class SpectrumTextureMaps {
	
	public static TextureMapping all(Block allBlock, String allSuffix) {
		return all(getBlockTexture(allBlock, allSuffix));
	}
	
	public static TextureMapping all(ResourceLocation all) {
		return new TextureMapping().put(ALL, all);
	}
	
	public static TextureMapping cross(Block crossBlock, String crossSuffix) {
		return new TextureMapping().put(CROSS, getBlockTexture(crossBlock, crossSuffix));
	}
	
	public static TextureMapping flowerParticle(Block flowerBlock, String flowerSuffix, Block particleBlock, String particleSuffix) {
		return flowerParticle(getBlockTexture(flowerBlock, flowerSuffix), getBlockTexture(particleBlock, particleSuffix));
	}
	
	public static TextureMapping flowerParticle(ResourceLocation flower, ResourceLocation particle) {
		return new TextureMapping().put(FLOWER, flower).put(PARTICLE, particle);
	}
	
	public static TextureMapping innerOuter(Block innerBlock, String innerSuffix, Block outerBlock, String outerSuffix) {
		return innerOuter(getBlockTexture(innerBlock, innerSuffix), getBlockTexture(outerBlock, outerSuffix));
	}
	
	public static TextureMapping innerOuter(ResourceLocation inner, ResourceLocation outer) {
		return new TextureMapping().put(INNER, inner).put(OUTER, outer);
	}
	
	public static TextureMapping innerOuterParticle(ResourceLocation inner, ResourceLocation outer, ResourceLocation particle) {
		return new TextureMapping().put(INNER, inner).put(OUTER, outer).put(PARTICLE, particle);
	}
	
	public static TextureMapping layer0(Item layer0Item, String layer0Suffix) {
		return new TextureMapping().put(LAYER0, getItemTexture(layer0Item, layer0Suffix));
	}
	
	public static TextureMapping layer0(Block layer0Block, String layer0Suffix) {
		return new TextureMapping().put(LAYER0, getBlockTexture(layer0Block, layer0Suffix));
	}
	
	public static TextureMapping plant(Block plantBlock, String plantSuffix) {
		return new TextureMapping().put(PLANT, getBlockTexture(plantBlock, plantSuffix));
	}
	
	public static TextureMapping sideEnd(Block sideBlock, String sideSuffix, Block endBlock, String endSuffix) {
		return sideEnd(getBlockTexture(sideBlock, sideSuffix), getBlockTexture(endBlock, endSuffix));
	}
	
	public static TextureMapping sideEnd(ResourceLocation side, ResourceLocation end) {
		return new TextureMapping().put(SIDE, side).put(END, end);
	}
	
	public static TextureMapping sideLine(ResourceLocation side, ResourceLocation line) {
		return new TextureMapping().put(SIDE, side).put(LINE, line);
	}
	
	public static TextureMapping sideTop(Block sideBlock, String sideSuffix, Block topBlock, String topSuffix) {
		return new TextureMapping().put(SIDE, getBlockTexture(sideBlock, sideSuffix)).put(TOP, getBlockTexture(topBlock, topSuffix));
	}
	
	public static TextureMapping sideTopBottom(Block sideBlock, String sideSuffix, Block topBlock, String topSuffix, Block bottomBlock, String bottomSuffix) {
		return new TextureMapping().put(SIDE, getBlockTexture(sideBlock, sideSuffix)).put(TOP, getBlockTexture(topBlock, topSuffix)).put(BOTTOM, getBlockTexture(bottomBlock, bottomSuffix));
	}
	
	public static TextureMapping sideTopBottomFronds(Block sideBlock, String sideSuffix, Block topBlock, String topSuffix, Block bottomBlock, String bottomSuffix, Block frondsBlock, String frondsSuffix) {
		return new TextureMapping().put(SIDE, getBlockTexture(sideBlock, sideSuffix)).put(TOP, getBlockTexture(topBlock, topSuffix)).put(BOTTOM, getBlockTexture(bottomBlock, bottomSuffix)).put(FRONDS, getBlockTexture(frondsBlock, frondsSuffix));
	}
	
	public static TextureMapping sideTopBottomParticle(Block sideBlock, String sideSuffix, Block topBlock, String topSuffix, Block bottomBlock, String bottomSuffix, Block particleBlock, String particleSuffix) {
		return new TextureMapping().put(SIDE, getBlockTexture(sideBlock, sideSuffix)).put(TOP, getBlockTexture(topBlock, topSuffix)).put(BOTTOM, getBlockTexture(bottomBlock, bottomSuffix)).put(PARTICLE, getBlockTexture(particleBlock, particleSuffix));
	}
	
	public static TextureMapping sideTopBottomWall(Block sideBlock, String sideSuffix, Block topBlock, String topSuffix, Block bottomBlock, String bottomSuffix, Block wallBlock, String wallSuffix) {
		return new TextureMapping().put(SIDE, getBlockTexture(sideBlock, sideSuffix)).put(TOP, getBlockTexture(topBlock, topSuffix)).put(BOTTOM, getBlockTexture(bottomBlock, bottomSuffix)).put(WALL, getBlockTexture(wallBlock, wallSuffix));
	}
	
	public static TextureMapping sideTopInside(Block sideBlock, String sideSuffix, Block topBlock, String topSuffix, Block insideBlock, String insideSuffix) {
		return sideTopInside(getBlockTexture(sideBlock, sideSuffix), getBlockTexture(topBlock, topSuffix), getBlockTexture(insideBlock, insideSuffix));
	}
	
	public static TextureMapping sideTopInside(ResourceLocation side, ResourceLocation top, ResourceLocation inside) {
		return new TextureMapping().put(SIDE, side).put(TOP, top).put(INSIDE, inside);
	}
	
	public static TextureMapping top(Block topBlock, String topSuffix) {
		return new TextureMapping().put(TOP, getBlockTexture(topBlock, topSuffix));
	}
	
	
}
