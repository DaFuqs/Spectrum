package de.dafuqs.spectrum.registries.client;

import net.minecraft.data.models.*;
import net.minecraft.data.models.model.*;
import net.minecraft.resources.*;
import net.minecraft.world.level.block.*;

import java.util.*;
import java.util.function.*;

import static de.dafuqs.spectrum.registries.client.SpectrumTextureSlots.*;
import static net.minecraft.data.models.model.TextureMapping.*;
import static net.minecraft.data.models.model.TextureSlot.*;

public class SpectrumTexturedModelProviders {
	
	public static final TexturedModel.Provider BASE_TRANS_LIGHT_CORE = TexturedModel.createDefault(b -> new TextureMapping().put(CASE, getBlockTexture(b)).put(BASE, getBlockTexture(b, "_base")).put(GLASS, getBlockTexture(b, "_glass")).put(SHELL, getBlockTexture(b, "_shell")).put(FILAMENT, getBlockTexture(b, "_filament")).put(ENDS, getBlockTexture(b, "_ends")), SpectrumModelTemplates.BASE_TRANS_LIGHT_CORE);
	public static final TexturedModel.Provider CHIME = TexturedModel.createDefault(b -> new TextureMapping().put(BASE, SpectrumTextures.BALCITE_CHIME_BASE).put(GEMSTONE, getBlockTexture(b)), SpectrumModelTemplates.CHIME);
	public static final TexturedModel.Provider CUBE_COLUMN_MIRRORED = TexturedModel.createDefault(TextureMapping::logColumn, ModelTemplates.CUBE_COLUMN_MIRRORED);
	public static final TexturedModel.Provider CUSHION = TexturedModel.createDefault(b -> SpectrumTextureMaps.sideTopBottom(b, "_side", b, "_top", b, "_bottom"), SpectrumModelTemplates.CUSHION);
	public static final TexturedModel.Provider FUSION_SHRINE = TexturedModel.createDefault(b -> new TextureMapping().put(SHRINE, getBlockTexture(b)).put(PARTICLE, getBlockTexture(b, "_breaking")), SpectrumModelTemplates.FUSION_SHRINE);
	public static final TexturedModel.Provider ROUNDEL = TexturedModel.createDefault(b -> new TextureMapping().put(ALL, getBlockTexture(b)), SpectrumModelTemplates.ROUNDEL);
	public static final TexturedModel.Provider SHOOTING_STAR = TexturedModel.createDefault(b -> new TextureMapping().put(CORE, getBlockTexture(b)).put(SIDE, getBlockTexture(b, "_side")), SpectrumModelTemplates.SHOOTING_STAR);
	
	public static TexturedModel.Provider baseTransLantern(boolean diagonal, boolean tall) {
		return TexturedModel.createDefault(b -> new TextureMapping().put(GLASS, getBlockTexture(b, "_glass_on")).put(CASE, getBlockTexture(b, "_case_on")), SpectrumModelTemplates.baseTransLantern(diagonal, tall));
	}
	
	public static TexturedModel.Provider carpet(UnaryOperator<Block> woolBlock, String woolSuffix) {
		return TexturedModel.createDefault(b -> TextureMapping.wool(ModelLocationUtils.getModelLocation(woolBlock.apply(b), woolSuffix)), ModelTemplates.CARPET);
	}
	
	public static TexturedModel.Provider complexOrientable(UnaryOperator<Block> sideBlock, String sideSuffix, UnaryOperator<Block> topBlock, String topSuffix, UnaryOperator<Block> bottomBlock, String bottomSuffix, UnaryOperator<Block> frontBlock, String frontSuffix, UnaryOperator<Block> backBlock, String backSuffix, UnaryOperator<Block> particleBlock, String particleSuffix) {
		return TexturedModel.createDefault(b -> new TextureMapping().put(SIDE, getBlockTexture(sideBlock.apply(b), sideSuffix)).put(TOP, getBlockTexture(topBlock.apply(b), topSuffix)).put(BOTTOM, getBlockTexture(bottomBlock.apply(b), bottomSuffix)).put(FRONT, getBlockTexture(frontBlock.apply(b), frontSuffix)).put(BACK, getBlockTexture(backBlock.apply(b), backSuffix)).put(PARTICLE, getBlockTexture(particleBlock.apply(b), particleSuffix)), SpectrumModelTemplates.COMPLEX_ORIENTABLE);
	}
	
	public static TexturedModel.Provider cross(UnaryOperator<Block> crossBlock, String crossSuffix) {
		return TexturedModel.createDefault(b -> SpectrumTextureMaps.cross(crossBlock.apply(b), crossSuffix), ModelTemplates.CROSS);
	}
	
	public static TexturedModel.Provider cubeAll(UnaryOperator<Block> allBlock, String allSuffix) {
		return TexturedModel.createDefault(b -> SpectrumTextureMaps.all(allBlock.apply(b), allSuffix), ModelTemplates.CUBE_ALL);
	}
	
	public static TexturedModel.Provider cubeBottomTop(UnaryOperator<Block> sideBlock, String sideSuffix, UnaryOperator<Block> topBlock, String topSuffix, UnaryOperator<Block> bottomBlock, String bottomSuffix) {
		return TexturedModel.createDefault(b -> SpectrumTextureMaps.sideTopBottom(sideBlock.apply(b), sideSuffix, topBlock.apply(b), topSuffix, bottomBlock.apply(b), bottomSuffix), ModelTemplates.CUBE_BOTTOM_TOP);
	}
	
	public static TexturedModel.Provider cubeBottomTopParticle(UnaryOperator<Block> sideBlock, String sideSuffix, UnaryOperator<Block> topBlock, String topSuffix, UnaryOperator<Block> bottomBlock, String bottomSuffix, UnaryOperator<Block> particleBlock, String particleSuffix) {
		return TexturedModel.createDefault(b -> SpectrumTextureMaps.sideTopBottomParticle(sideBlock.apply(b), sideSuffix, topBlock.apply(b), topSuffix, bottomBlock.apply(b), bottomSuffix, particleBlock.apply(b), particleSuffix), SpectrumModelTemplates.CUBE_BOTTOM_TOP_PARTICLE);
	}
	
	public static TexturedModel.Provider cubeBottomTopWall(UnaryOperator<Block> sideBlock, String sideSuffix, UnaryOperator<Block> topBlock, String topSuffix, UnaryOperator<Block> bottomBlock, String bottomSuffix, UnaryOperator<Block> wallBlock, String wallSuffix) {
		return TexturedModel.createDefault(b -> SpectrumTextureMaps.sideTopBottomWall(sideBlock.apply(b), sideSuffix, topBlock.apply(b), topSuffix, bottomBlock.apply(b), bottomSuffix, wallBlock.apply(b), wallSuffix), SpectrumModelTemplates.CUBE_BOTTOM_TOP_WALL);
	}
	
	public static TexturedModel.Provider cubeColumn(UnaryOperator<Block> sideBlock, String sideSuffix, UnaryOperator<Block> endBlock, String endSuffix) {
		return TexturedModel.createDefault(b -> SpectrumTextureMaps.sideEnd(sideBlock.apply(b), sideSuffix, endBlock.apply(b), endSuffix), ModelTemplates.CUBE_COLUMN);
	}
	
	public static TexturedModel.Provider doubleCross(UnaryOperator<Block> crossBlock, String crossSuffix) {
		return TexturedModel.createDefault(b -> SpectrumTextureMaps.cross(crossBlock.apply(b), crossSuffix), SpectrumModelTemplates.DOUBLE_CROSS);
	}
	
	public static TexturedModel.Provider farmland(UnaryOperator<Block> dirtBlock, String dirtSuffix, UnaryOperator<Block> topBlock, String topSuffix) {
		return TexturedModel.createDefault(b -> new TextureMapping().put(DIRT, getBlockTexture(dirtBlock.apply(b), dirtSuffix)).put(TOP, getBlockTexture(topBlock.apply(b), topSuffix)), ModelTemplates.FARMLAND);
	}
	
	public static TexturedModel.Provider flowerPotCross(UnaryOperator<Block> plantBlock, String plantSuffix, boolean tinted) {
		BlockModelGenerators.TintState tintType = tinted ? BlockModelGenerators.TintState.TINTED : BlockModelGenerators.TintState.NOT_TINTED;
		return TexturedModel.createDefault(b -> SpectrumTextureMaps.plant(plantBlock.apply(b), plantSuffix), tintType.getCrossPot());
	}
	
	public static TexturedModel.Provider leaves(UnaryOperator<Block> allBlock, String allSuffix) {
		return TexturedModel.createDefault(b -> SpectrumTextureMaps.all(allBlock.apply(b), allSuffix), ModelTemplates.LEAVES);
	}
	
	public static TexturedModel.Provider overgrown(UnaryOperator<Block> sideBlock, String sideSuffix, UnaryOperator<Block> topBlock, String topSuffix, UnaryOperator<Block> bottomBlock, String bottomSuffix, UnaryOperator<Block> frondsBlock, String frondsSuffix) {
		return TexturedModel.createDefault(b -> SpectrumTextureMaps.sideTopBottomFronds(sideBlock.apply(b), sideSuffix, topBlock.apply(b), topSuffix, bottomBlock.apply(b), bottomSuffix, frondsBlock.apply(b), frondsSuffix), SpectrumModelTemplates.OVERGROWN);
	}
	
	public static TexturedModel.Provider parented(ResourceLocation parentModelId) {
		return TexturedModel.createDefault(b -> new TextureMapping(), new ModelTemplate(Optional.of(parentModelId), Optional.empty()));
	}
	
	public static TexturedModel.Provider particle(UnaryOperator<Block> particleBlock, String particleSuffix) {
		return TexturedModel.createDefault(b -> TextureMapping.particle(getBlockTexture(particleBlock.apply(b), particleSuffix)), ModelTemplates.PARTICLE_ONLY);
	}
	
	public static TexturedModel.Provider particle(ResourceLocation particle) {
		return TexturedModel.createDefault(b -> TextureMapping.particle(particle), ModelTemplates.PARTICLE_ONLY);
	}
	
	public static TexturedModel.Provider sugarStick(int age, UnaryOperator<Block> sugarBlock) {
		return TexturedModel.createDefault(b -> new TextureMapping().put(KEY0, getBlockTexture(sugarBlock.apply(b))).put(KEY1, getBlockTexture(Blocks.OAK_PLANKS)).put(PARTICLE, getBlockTexture(sugarBlock.apply(b))), SpectrumModelTemplates.sugarStick(age));
	}
	
	public static TexturedModel.Provider tintableCross(UnaryOperator<Block> crossBlock, String crossSuffix, boolean tinted) {
		BlockModelGenerators.TintState tintType = tinted ? BlockModelGenerators.TintState.TINTED : BlockModelGenerators.TintState.NOT_TINTED;
		return TexturedModel.createDefault(b -> SpectrumTextureMaps.cross(crossBlock.apply(b), crossSuffix), tintType.getCross());
	}
	
}
