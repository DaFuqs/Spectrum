package de.dafuqs.spectrum.data;

import com.google.gson.*;
import de.dafuqs.spectrum.blocks.decoration.*;
import de.dafuqs.spectrum.registries.*;
import de.dafuqs.spectrum.registries.client.*;
import net.minecraft.core.*;
import net.minecraft.data.*;
import net.minecraft.data.models.*;
import net.minecraft.data.models.blockstates.*;
import net.minecraft.data.models.model.*;
import net.minecraft.resources.*;
import net.minecraft.util.*;
import net.minecraft.world.item.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.properties.*;

import java.util.*;
import java.util.function.*;

public class SpectrumModelHelper {
	public static final DeferredRegistrar.Contextual<ItemModelGenerators> ITEM_MODEL_REGISTRAR = new DeferredRegistrar.Contextual<>(DatagenProxy.IS_DATAGEN);
	public static final DeferredRegistrar.Contextual<BlockModelGenerators> BLOCK_STATE_MODEL_REGISTRAR = new DeferredRegistrar.Contextual<>(DatagenProxy.IS_DATAGEN);
	
	// Item Models
	
	public static void registerItemModel(ItemModelGenerators ctx, Item item) {
		registerItemModel(ctx, item, "");
	}
	
	public static void registerItemModel(ItemModelGenerators ctx, Item item, String suffix) {
		ModelTemplates.FLAT_ITEM.create(ModelLocationUtils.getModelLocation(item), SpectrumTextureMaps.layer0(item, suffix), ctx.output);
	}
	
	public static void registerItemModel(ItemModelGenerators ctx, Item item, ModelTemplate model) {
		model.create(ModelLocationUtils.getModelLocation(item), SpectrumTextureMaps.layer0(item, ""), ctx.output);
	}
	
	public static void registerLayeredItemModel(ItemModelGenerators ctx, Item item, ModelTemplate model, String suffix0, String suffix1) {
		model.create(ModelLocationUtils.getModelLocation(item), TextureMapping.layered(TextureMapping.getItemTexture(item, suffix0), TextureMapping.getItemTexture(item, suffix1)), ctx.output);
	}
	
	public static void registerLayeredItemModel(ItemModelGenerators ctx, Item item, ModelTemplate model, String suffix0, String suffix1, String suffix2) {
		model.create(ModelLocationUtils.getModelLocation(item), TextureMapping.layered(TextureMapping.getItemTexture(item, suffix0), TextureMapping.getItemTexture(item, suffix1), TextureMapping.getItemTexture(item, suffix2)), ctx.output);
	}
	
	public static void registerLayeredItemModel(ItemModelGenerators ctx, Item item, ModelTemplate model, String suffix0, String suffix1, String suffix2, String suffix3) {
		model.create(ModelLocationUtils.getModelLocation(item), TextureMapping.layered(TextureMapping.getItemTexture(item, suffix0), TextureMapping.getItemTexture(item, suffix1), TextureMapping.getItemTexture(item, suffix2)).put(SpectrumTextureKeys.LAYER3, TextureMapping.getItemTexture(item, suffix3)), ctx.output);
	}
	
	public static void registerBlockTexturedItemModel(ItemModelGenerators ctx, Block block) {
		registerBlockTexturedItemModel(ctx, block, "");
	}
	
	public static void registerBlockTexturedItemModel(ItemModelGenerators ctx, Block block, String suffix) {
		ModelTemplates.FLAT_ITEM.create(ModelLocationUtils.getModelLocation(block.asItem()), SpectrumTextureMaps.layer0(block, suffix), ctx.output);
	}
	
	public static void registerParentedItemModel(ItemModelGenerators ctx, ItemLike item, Item parent) {
		registerParentedItemModel(ctx, item, parent, "");
	}
	
	public static void registerParentedItemModel(ItemModelGenerators ctx, ItemLike item, Block parent) {
		registerParentedItemModel(ctx, item, parent, "");
	}
	
	public static void registerParentedItemModel(ItemModelGenerators ctx, ItemLike item, Item parent, String suffix) {
		registerParentedItemModel(ctx, item, ModelLocationUtils.getModelLocation(parent, suffix));
	}
	
	public static void registerParentedItemModel(ItemModelGenerators ctx, ItemLike item, Block parent, String suffix) {
		registerParentedItemModel(ctx, item, ModelLocationUtils.getModelLocation(parent, suffix));
	}
	
	public static void registerParentedItemModel(ItemModelGenerators ctx, ItemLike item, ResourceLocation parentModelId) {
		ctx.output.accept(ModelLocationUtils.getModelLocation(item.asItem()), new DelegatedModel(parentModelId));
	}
	
	// Block Models
	
	public static BlockStateGenerator simpleMirroredBlockModel(BlockModelGenerators ctx, Block block) {
		return createMirroredVariantsSupplier(block, TexturedModel.CUBE, TexturedModel.CUBE_MIRRORED, ctx.modelOutput);
	}
	
	public static BlockStateGenerator logBlockModel(BlockModelGenerators ctx, Block logBlock) {
		TextureMapping textureMap = SpectrumTextureMaps.sideEnd(logBlock, "", logBlock, "_top");
		ResourceLocation vertical = ModelTemplates.CUBE_COLUMN.create(logBlock, textureMap, ctx.modelOutput);
		ResourceLocation horizonal = ModelTemplates.CUBE_COLUMN_HORIZONTAL.create(logBlock, textureMap, ctx.modelOutput);
		return MultiVariantGenerator.multiVariant(logBlock).with(createAxisRotatedVariantMap(vertical, horizonal));
	}
	
	public static BlockStateGenerator woodBlockModel(BlockModelGenerators ctx, Block woodBlock, Block logBlock) {
		TextureMapping textureMap = SpectrumTextureMaps.sideEnd(logBlock, "", logBlock, "");
		ResourceLocation model = ModelTemplates.CUBE_COLUMN.create(woodBlock, textureMap, ctx.modelOutput);
		return MultiVariantGenerator.multiVariant(woodBlock, createModelVariant(model)).with(createAxisRotatedVariantMap());
	}
	
	public static BlockStateGenerator pottedPlantBlockModel(BlockModelGenerators ctx, FlowerPotBlock block, boolean tinted) {
		BlockModelGenerators.TintState tintType = tinted ? BlockModelGenerators.TintState.TINTED : BlockModelGenerators.TintState.NOT_TINTED;
		TextureMapping textureMap = TextureMapping.plant(block.getPotted());
		ResourceLocation identifier = tintType.getCrossPot().create(block, textureMap, ctx.modelOutput);
		return BlockModelGenerators.createSimpleBlock(block, identifier);
	}
	
	public static BlockStateGenerator pottedPlantWithCustomTextureBlockModel(BlockModelGenerators ctx, FlowerPotBlock block, String suffix) {
		TextureMapping textureMap = TextureMapping.plant(TextureMapping.getBlockTexture(block.getPotted()).withSuffix(suffix));
		ResourceLocation identifier = ModelTemplates.FLOWER_POT_CROSS.create(block, textureMap, ctx.modelOutput);
		return BlockModelGenerators.createSimpleBlock(block, identifier);
	}
	
	
	public static BlockStateGenerator glassPaneBlockModel(BlockModelGenerators ctx, Block glassPaneBlock, Block glassBlock) {
		TextureMapping textureMap = TextureMapping.pane(glassBlock, glassPaneBlock);
		ResourceLocation post = ModelTemplates.STAINED_GLASS_PANE_POST.create(glassPaneBlock, textureMap, ctx.modelOutput);
		ResourceLocation side = ModelTemplates.STAINED_GLASS_PANE_SIDE.create(glassPaneBlock, textureMap, ctx.modelOutput);
		ResourceLocation sideAlt = ModelTemplates.STAINED_GLASS_PANE_SIDE_ALT.create(glassPaneBlock, textureMap, ctx.modelOutput);
		ResourceLocation noside = ModelTemplates.STAINED_GLASS_PANE_NOSIDE.create(glassPaneBlock, textureMap, ctx.modelOutput);
		ResourceLocation nosideAlt = ModelTemplates.STAINED_GLASS_PANE_NOSIDE_ALT.create(glassPaneBlock, textureMap, ctx.modelOutput);
		ModelTemplates.FLAT_ITEM.create(ModelLocationUtils.getModelLocation(glassPaneBlock.asItem()), TextureMapping.layer0(glassBlock), ctx.modelOutput);
		return MultiPartGenerator.multiPart(glassPaneBlock)
				.with(Variant.variant().with(VariantProperties.MODEL, post))
				.with(Condition.condition().term(BlockStateProperties.NORTH, true), Variant.variant().with(VariantProperties.MODEL, side))
				.with(Condition.condition().term(BlockStateProperties.EAST, true), Variant.variant().with(VariantProperties.MODEL, side).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90))
				.with(Condition.condition().term(BlockStateProperties.SOUTH, true), Variant.variant().with(VariantProperties.MODEL, sideAlt))
				.with(Condition.condition().term(BlockStateProperties.WEST, true), Variant.variant().with(VariantProperties.MODEL, sideAlt).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90))
				.with(Condition.condition().term(BlockStateProperties.NORTH, false), Variant.variant().with(VariantProperties.MODEL, noside))
				.with(Condition.condition().term(BlockStateProperties.EAST, false), Variant.variant().with(VariantProperties.MODEL, nosideAlt))
				.with(Condition.condition().term(BlockStateProperties.SOUTH, false), Variant.variant().with(VariantProperties.MODEL, nosideAlt).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90))
				.with(Condition.condition().term(BlockStateProperties.WEST, false), Variant.variant().with(VariantProperties.MODEL, noside).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270));
	}
	
	public static BlockFamily registerBlockFamily(BlockFamily family) {
		BLOCK_STATE_MODEL_REGISTRAR.defer(ctx -> ctx.family(family.getBaseBlock()).generateFor(family));
		return family;
	}
	
	public static BlockFamily registerBlockFamilyExceptBase(BlockFamily family, TexturedModel.Provider variantFactory) {
		BLOCK_STATE_MODEL_REGISTRAR.defer(ctx -> {
			TexturedModel texturedModel = variantFactory.get(family.getBaseBlock());
			BlockModelGenerators.BlockFamilyProvider texturePool = ctx.new BlockFamilyProvider(texturedModel.getMapping());
			texturePool.fullBlock = ModelLocationUtils.getModelLocation(family.getBaseBlock());
			texturePool.generateFor(family);
		});
		return family;
	}
	
	public static void improvedDoor(Block doorBlock, BlockModelGenerators ctx) {
		TextureMapping   map = TextureMapping.cubeBottomTop(doorBlock);
		ResourceLocation tlc = SpectrumModels.DOOR_BOTTOM_LEFT		.create(doorBlock, map, ctx.modelOutput);
		ResourceLocation tlo = SpectrumModels.DOOR_BOTTOM_LEFT_OPEN	.create(doorBlock, map, ctx.modelOutput);
		ResourceLocation trc = SpectrumModels.DOOR_BOTTOM_RIGHT		.create(doorBlock, map, ctx.modelOutput);
		ResourceLocation tro = SpectrumModels.DOOR_BOTTOM_RIGHT_OPEN.create(doorBlock, map, ctx.modelOutput);
		ResourceLocation blc = SpectrumModels.DOOR_TOP_LEFT			.create(doorBlock, map, ctx.modelOutput);
		ResourceLocation blo = SpectrumModels.DOOR_TOP_LEFT_OPEN	.create(doorBlock, map, ctx.modelOutput);
		ResourceLocation brc = SpectrumModels.DOOR_TOP_RIGHT		.create(doorBlock, map, ctx.modelOutput);
		ResourceLocation bro = SpectrumModels.DOOR_TOP_RIGHT_OPEN	.create(doorBlock, map, ctx.modelOutput);
		ctx.createSimpleFlatItemModel(doorBlock.asItem());
		ctx.blockStateOutput.accept(BlockModelGenerators.createDoor(doorBlock, tlc, tlo, trc, tro, blc, blo, brc, bro));
	}
	
	public static void improvedOrientableTrapdoor(Block orientableTrapdoorBlock, BlockModelGenerators ctx) {
		TextureMapping   map = SpectrumTextureMaps.sideTexture(orientableTrapdoorBlock);
		ResourceLocation top = SpectrumModels.TEMPLATE_ORIENTABLE_TRAPDOOR_TOP	 .create(orientableTrapdoorBlock, map, ctx.modelOutput);
		ResourceLocation btm = SpectrumModels.TEMPLATE_ORIENTABLE_TRAPDOOR_BOTTOM.create(orientableTrapdoorBlock, map, ctx.modelOutput);
		ResourceLocation opn = SpectrumModels.TEMPLATE_ORIENTABLE_TRAPDOOR_OPEN	 .create(orientableTrapdoorBlock, map, ctx.modelOutput);
		ctx.blockStateOutput.accept(BlockModelGenerators.createOrientableTrapdoor(orientableTrapdoorBlock, top, btm, opn));
		ctx.delegateItemModel(orientableTrapdoorBlock, btm);
	}
	
	public static BlockFamily registerBlockFamilyWithImprovedModels(BlockFamily family) {
		BLOCK_STATE_MODEL_REGISTRAR.defer(ctx -> {
			BlockModelGenerators.BlockFamilyProvider texturePool = ctx.family(family.getBaseBlock());
			Block trap = family.get(BlockFamily.Variant.TRAPDOOR);
			Block door = family.get(BlockFamily.Variant.DOOR);
			texturePool.skipGeneratingModelsFor.add(trap);
			texturePool.skipGeneratingModelsFor.add(door);
			texturePool.generateFor(family);
			improvedDoor(door, ctx);
			improvedOrientableTrapdoor(trap, ctx); // assumes orientable trapdoor
		});
		
		return family;
	}
	
	// Model modifiers
	
	public static Supplier<JsonElement> paltaeriaModifier(Supplier<JsonElement> original, float s) {
		return () -> {
			JsonObject obj = original.get().getAsJsonObject();
			// construct display tag
			JsonObject transforms = new JsonObject(), transform   = new JsonObject();
			JsonArray  rotation   = new JsonArray(),  translation = new JsonArray(), scale = new JsonArray();
			List.of(s,  s,   s).forEach(scale::add);
			List.of(0,  0, 180).forEach(rotation::add);
			List.of(0, -4.5, 0).forEach(translation::add);
			// root.display.ground
			transform.add("scale", scale);
			transform.add("rotation", rotation);
			transform.add("translation", translation);
			transforms.add("ground", transform); // root.display
			obj.add("display", transforms);      // root
			return obj;
		};
	}
	
	public static BiConsumer<ResourceLocation, Supplier<JsonElement>> paltaeriaModifier(BiConsumer<ResourceLocation, Supplier<JsonElement>> original, float scale) {
		return (loc, supplier) -> original.accept(loc, paltaeriaModifier(supplier, scale));
	}
	
	// Variant Suppliers
	
	public static MultiVariantGenerator createVariantsSupplier(Block block, ResourceLocation... modelIds) {
		return MultiVariantGenerator.multiVariant(block, Arrays.stream(modelIds).map(modelId -> Variant.variant().with(VariantProperties.MODEL, modelId)).toArray(Variant[]::new));
	}
	
	public static MultiVariantGenerator createVariantsSupplier(BlockModelGenerators ctx, Block block, TexturedModel.Provider factory) {
		return createVariantsSupplier(block, factory.create(block, ctx.modelOutput));
	}
	
	public static MultiVariantGenerator createMirroredVariantsSupplier(Block block, TexturedModel.Provider factory, TexturedModel.Provider mirroredFactory, BiConsumer<ResourceLocation, Supplier<JsonElement>> modelCollector) {
		return MultiVariantGenerator.multiVariant(block,
				createModelVariant(factory.create(block, modelCollector)),
				createModelVariant(mirroredFactory.create(block, modelCollector))
		);
	}
	
	// Variant Lists
	
	public static List<Variant> createHorizontalRotationVariantList(ResourceLocation modelId) {
		return List.of(
				createModelVariant(modelId),
				createModelVariant(modelId).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90),
				createModelVariant(modelId).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180),
				createModelVariant(modelId).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270)
		);
	}
	
	// Variant Maps
	
	public static PropertyDispatch createBooleanModelMap(BooleanProperty property, ResourceLocation trueModel, ResourceLocation falseModel) {
		return PropertyDispatch.property(property)
				.select(false, createModelVariant(falseModel))
				.select(true, createModelVariant(trueModel));
	}
	
	public static PropertyDispatch createCardinalFacingVariantMap() {
		return PropertyDispatch.property(CardinalFacingBlock.CARDINAL_FACING)
				.select(false, Variant.variant().with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90))
				.select(true, Variant.variant());
	}
	
	public static PropertyDispatch createAxisRotatedVariantMap() {
		return PropertyDispatch.property(BlockStateProperties.AXIS)
				.select(Direction.Axis.X, Variant.variant().with(VariantProperties.X_ROT, VariantProperties.Rotation.R90).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90))
				.select(Direction.Axis.Y, Variant.variant())
				.select(Direction.Axis.Z, Variant.variant().with(VariantProperties.X_ROT, VariantProperties.Rotation.R90));
	}
	
	public static PropertyDispatch createAxisRotatedVariantMap(ResourceLocation verticalModelId, ResourceLocation horizontalModelId) {
		return PropertyDispatch.property(BlockStateProperties.AXIS)
				.select(Direction.Axis.X, createModelVariant(horizontalModelId).with(VariantProperties.X_ROT, VariantProperties.Rotation.R90).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90))
				.select(Direction.Axis.Y, createModelVariant(verticalModelId))
				.select(Direction.Axis.Z, createModelVariant(horizontalModelId).with(VariantProperties.X_ROT, VariantProperties.Rotation.R90));
	}
	
	public static PropertyDispatch createUpDefaultFacingVariantMap() {
		return PropertyDispatch.property(BlockStateProperties.FACING)
				.select(Direction.DOWN, Variant.variant().with(VariantProperties.X_ROT, VariantProperties.Rotation.R180))
				.select(Direction.UP, Variant.variant())
				.select(Direction.NORTH, Variant.variant().with(VariantProperties.X_ROT, VariantProperties.Rotation.R90))
				.select(Direction.SOUTH, Variant.variant().with(VariantProperties.X_ROT, VariantProperties.Rotation.R90).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180))
				.select(Direction.WEST, Variant.variant().with(VariantProperties.X_ROT, VariantProperties.Rotation.R90).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270))
				.select(Direction.EAST, Variant.variant().with(VariantProperties.X_ROT, VariantProperties.Rotation.R90).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90));
	}
	
	public static PropertyDispatch createDownDefaultFacingVariantMap(ResourceLocation horizontalModelId, ResourceLocation verticalModelId) {
		return PropertyDispatch.property(DirectionalBlock.FACING)
				.select(Direction.DOWN, createModelVariant(verticalModelId))
				.select(Direction.UP, createModelVariant(verticalModelId).with(VariantProperties.X_ROT, VariantProperties.Rotation.R180))
				.select(Direction.NORTH, createModelVariant(horizontalModelId).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270))
				.select(Direction.SOUTH, createModelVariant(horizontalModelId).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90))
				.select(Direction.WEST, createModelVariant(horizontalModelId).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180))
				.select(Direction.EAST, createModelVariant(horizontalModelId));
	}
	
	public static PropertyDispatch createNorthDefaultFacingVariantMap() {
		return PropertyDispatch.property(BlockStateProperties.FACING)
				.select(Direction.DOWN, Variant.variant().with(VariantProperties.X_ROT, VariantProperties.Rotation.R90))
				.select(Direction.UP, Variant.variant().with(VariantProperties.X_ROT, VariantProperties.Rotation.R270))
				.select(Direction.NORTH, Variant.variant())
				.select(Direction.SOUTH, Variant.variant().with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180))
				.select(Direction.WEST, Variant.variant().with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270))
				.select(Direction.EAST, Variant.variant().with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90));
	}
	
	public static PropertyDispatch createUpDefaultHorizontalFacingVariantMap() {
		return PropertyDispatch.property(BlockStateProperties.HORIZONTAL_FACING)
				.select(Direction.NORTH, Variant.variant().with(VariantProperties.X_ROT, VariantProperties.Rotation.R90))
				.select(Direction.SOUTH, Variant.variant().with(VariantProperties.X_ROT, VariantProperties.Rotation.R90).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180))
				.select(Direction.WEST, Variant.variant().with(VariantProperties.X_ROT, VariantProperties.Rotation.R90).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270))
				.select(Direction.EAST, Variant.variant().with(VariantProperties.X_ROT, VariantProperties.Rotation.R90).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90));
	}
	
	public static PropertyDispatch createNorthDefaultHorizontalFacingVariantMap() {
		return PropertyDispatch.property(BlockStateProperties.HORIZONTAL_FACING)
				.select(Direction.NORTH, Variant.variant())
				.select(Direction.SOUTH, Variant.variant().with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180))
				.select(Direction.WEST, Variant.variant().with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270))
				.select(Direction.EAST, Variant.variant().with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90));
	}
	
	public static PropertyDispatch createSouthDefaultHorizontalFacingVariantMap() {
		return PropertyDispatch.property(BlockStateProperties.HORIZONTAL_FACING)
				.select(Direction.NORTH, Variant.variant().with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180))
				.select(Direction.SOUTH, Variant.variant())
				.select(Direction.WEST, Variant.variant().with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90))
				.select(Direction.EAST, Variant.variant().with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270));
	}
	
	public static PropertyDispatch createWestDefaultHorizontalFacingVariantMap() {
		return PropertyDispatch.property(BlockStateProperties.HORIZONTAL_FACING)
				.select(Direction.NORTH, Variant.variant().with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90))
				.select(Direction.SOUTH, Variant.variant().with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270))
				.select(Direction.WEST, Variant.variant())
				.select(Direction.EAST, Variant.variant().with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180));
	}
	
	public static PropertyDispatch createEastDefaultHorizontalFacingVariantMap() {
		return PropertyDispatch.property(BlockStateProperties.HORIZONTAL_FACING)
				.select(Direction.NORTH, Variant.variant().with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270))
				.select(Direction.SOUTH, Variant.variant().with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90))
				.select(Direction.WEST, Variant.variant().with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180))
				.select(Direction.EAST, Variant.variant());
	}
	
	public static PropertyDispatch createUpNorthDefaultOrientationVariantMap() {
		return PropertyDispatch.property(BlockStateProperties.ORIENTATION)
				.select(FrontAndTop.DOWN_NORTH, Variant.variant().with(VariantProperties.X_ROT, VariantProperties.Rotation.R90))
				.select(FrontAndTop.DOWN_SOUTH, Variant.variant().with(VariantProperties.X_ROT, VariantProperties.Rotation.R90).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180))
				.select(FrontAndTop.DOWN_WEST, Variant.variant().with(VariantProperties.X_ROT, VariantProperties.Rotation.R90).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270))
				.select(FrontAndTop.DOWN_EAST, Variant.variant().with(VariantProperties.X_ROT, VariantProperties.Rotation.R90).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90))
				.select(FrontAndTop.UP_NORTH, Variant.variant().with(VariantProperties.X_ROT, VariantProperties.Rotation.R270).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180))
				.select(FrontAndTop.UP_SOUTH, Variant.variant().with(VariantProperties.X_ROT, VariantProperties.Rotation.R270))
				.select(FrontAndTop.UP_WEST, Variant.variant().with(VariantProperties.X_ROT, VariantProperties.Rotation.R270).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90))
				.select(FrontAndTop.UP_EAST, Variant.variant().with(VariantProperties.X_ROT, VariantProperties.Rotation.R270).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270))
				.select(FrontAndTop.NORTH_UP, Variant.variant())
				.select(FrontAndTop.SOUTH_UP, Variant.variant().with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180))
				.select(FrontAndTop.WEST_UP, Variant.variant().with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270))
				.select(FrontAndTop.EAST_UP, Variant.variant().with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90));
	}
	
	// Variants
	
	public static Variant createModelVariant(ResourceLocation modelId) {
		return Variant.variant().with(VariantProperties.MODEL, modelId);
	}
	
	public static Variant createModelVariant(Block block, String suffix) {
		return createModelVariant(ModelLocationUtils.getModelLocation(block, suffix));
	}
	
	public static VariantProperties.Rotation getSouthDefaultRotation(Direction direction) {
		return switch (direction) {
			case Direction.WEST -> VariantProperties.Rotation.R90;
			case Direction.NORTH -> VariantProperties.Rotation.R180;
			case Direction.EAST -> VariantProperties.Rotation.R270;
			default -> VariantProperties.Rotation.R0;
		};
	}
}
