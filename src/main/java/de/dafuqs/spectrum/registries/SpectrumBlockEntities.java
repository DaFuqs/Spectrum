package de.dafuqs.spectrum.registries;

import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.blocks.*;
import de.dafuqs.spectrum.blocks.amphora.*;
import de.dafuqs.spectrum.blocks.block_flooder.*;
import de.dafuqs.spectrum.blocks.bottomless_bundle.*;
import de.dafuqs.spectrum.blocks.chests.*;
import de.dafuqs.spectrum.blocks.decoration.*;
import de.dafuqs.spectrum.blocks.deeper_down.*;
import de.dafuqs.spectrum.blocks.enchanter.*;
import de.dafuqs.spectrum.blocks.ender.*;
import de.dafuqs.spectrum.blocks.ink.*;
import de.dafuqs.spectrum.blocks.fusion_shrine.*;
import de.dafuqs.spectrum.blocks.item_bowl.*;
import de.dafuqs.spectrum.blocks.item_roundel.*;
import de.dafuqs.spectrum.blocks.jade_vines.*;
import de.dafuqs.spectrum.blocks.memory.*;
import de.dafuqs.spectrum.blocks.mob_head.*;
import de.dafuqs.spectrum.blocks.mob_head.client.*;
import de.dafuqs.spectrum.blocks.particle_spawner.*;
import de.dafuqs.spectrum.blocks.pastel_network.nodes.*;
import de.dafuqs.spectrum.blocks.pedestal.*;
import de.dafuqs.spectrum.blocks.potion_workshop.*;
import de.dafuqs.spectrum.blocks.present.*;
import de.dafuqs.spectrum.blocks.redstone.*;
import de.dafuqs.spectrum.blocks.spirit_instiller.*;
import de.dafuqs.spectrum.blocks.spirit_sallow.*;
import de.dafuqs.spectrum.blocks.structure.*;
import de.dafuqs.spectrum.blocks.titration_barrel.*;
import de.dafuqs.spectrum.blocks.upgrade.*;
import net.minecraft.client.renderer.blockentity.*;
import net.minecraft.core.registries.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.*;
import net.neoforged.bus.api.*;
import net.neoforged.fml.event.lifecycle.*;
import net.neoforged.neoforge.event.*;
import net.neoforged.neoforge.registries.*;

import java.util.*;
import java.util.function.*;

public class SpectrumBlockEntities {
	
	public static final DeferredRegister<BlockEntityType<?>> REGISTRAR = DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, SpectrumCommon.MOD_ID);
	
	public static DeferredHolder<BlockEntityType<?>, BlockEntityType<OminousSaplingBlockEntity>> OMINOUS_SAPLING = register("ominous_sapling_block_entity", OminousSaplingBlockEntity::new, SpectrumBlocks.OMINOUS_SAPLING);
	public static DeferredHolder<BlockEntityType<?>, BlockEntityType<PedestalBlockEntity>> PEDESTAL = register("pedestal_block_entity", PedestalBlockEntity::new, SpectrumBlocks.PEDESTAL_BASIC_AMETHYST, SpectrumBlocks.PEDESTAL_BASIC_TOPAZ, SpectrumBlocks.PEDESTAL_BASIC_CITRINE, SpectrumBlocks.PEDESTAL_ALL_BASIC, SpectrumBlocks.PEDESTAL_ONYX, SpectrumBlocks.PEDESTAL_MOONSTONE);
	public static DeferredHolder<BlockEntityType<?>, BlockEntityType<FusionShrineBlockEntity>> FUSION_SHRINE = register("fusion_shrine_block_entity", FusionShrineBlockEntity::new, SpectrumBlocks.FUSION_SHRINE_BASALT, SpectrumBlocks.FUSION_SHRINE_CALCITE);
	public static DeferredHolder<BlockEntityType<?>, BlockEntityType<EnchanterBlockEntity>> ENCHANTER = register("enchanter_block_entity", EnchanterBlockEntity::new, SpectrumBlocks.ENCHANTER);
	
	public static DeferredHolder<BlockEntityType<?>, BlockEntityType<ItemBowlBlockEntity>> ITEM_BOWL = register("item_bowl_block_entity", ItemBowlBlockEntity::new, SpectrumBlocks.ITEM_BOWL_BASALT, SpectrumBlocks.ITEM_BOWL_CALCITE);
	public static DeferredHolder<BlockEntityType<?>, BlockEntityType<ItemRoundelBlockEntity>> ITEM_ROUNDEL = register("item_roundel", ItemRoundelBlockEntity::new, SpectrumBlocks.ITEM_ROUNDEL);
	public static DeferredHolder<BlockEntityType<?>, BlockEntityType<EnderDropperBlockEntity>> ENDER_DROPPER = register("ender_dropper", EnderDropperBlockEntity::new, SpectrumBlocks.ENDER_DROPPER);
	public static DeferredHolder<BlockEntityType<?>, BlockEntityType<EnderHopperBlockEntity>> ENDER_HOPPER = register("ender_hopper", EnderHopperBlockEntity::new, SpectrumBlocks.ENDER_HOPPER);
	public static DeferredHolder<BlockEntityType<?>, BlockEntityType<ParticleSpawnerBlockEntity>> PARTICLE_SPAWNER = register("particle_spawner", ParticleSpawnerBlockEntity::new, SpectrumBlocks.PARTICLE_SPAWNER, SpectrumBlocks.CREATIVE_PARTICLE_SPAWNER);
	public static DeferredHolder<BlockEntityType<?>, BlockEntityType<UpgradeBlockEntity>> UPGRADE_BLOCK = register("upgrade_block", UpgradeBlockEntity::new,
			SpectrumBlocks.UPGRADE_SPEED, SpectrumBlocks.UPGRADE_SPEED2, SpectrumBlocks.UPGRADE_SPEED3
			, SpectrumBlocks.UPGRADE_EFFICIENCY, SpectrumBlocks.UPGRADE_EFFICIENCY2,
			SpectrumBlocks.UPGRADE_EXPERIENCE, SpectrumBlocks.UPGRADE_EXPERIENCE2,
			SpectrumBlocks.UPGRADE_YIELD, SpectrumBlocks.UPGRADE_YIELD2);
	public static DeferredHolder<BlockEntityType<?>, BlockEntityType<SpectrumSkullBlockEntity>> SKULL = register("skull", SpectrumSkullBlockEntity::new); // supported blocks are added in addBlockEntityTypeBlocks()
	public static DeferredHolder<BlockEntityType<?>, BlockEntityType<BottomlessBundleBlockEntity>> BOTTOMLESS_BUNDLE = register("bottomless_bundle", BottomlessBundleBlockEntity::new, SpectrumBlocks.BOTTOMLESS_BUNDLE);
	public static DeferredHolder<BlockEntityType<?>, BlockEntityType<PotionWorkshopBlockEntity>> POTION_WORKSHOP = register("potion_workshop", PotionWorkshopBlockEntity::new, SpectrumBlocks.POTION_WORKSHOP);
	public static DeferredHolder<BlockEntityType<?>, BlockEntityType<CrystallarieumBlockEntity>> CRYSTALLARIEUM = register("crystallarieum", CrystallarieumBlockEntity::new, SpectrumBlocks.CRYSTALLARIEUM);
	public static DeferredHolder<BlockEntityType<?>, BlockEntityType<CinderhearthBlockEntity>> CINDERHEARTH = register("cinderhearth", CinderhearthBlockEntity::new, SpectrumBlocks.CINDERHEARTH);
	
	public static DeferredHolder<BlockEntityType<?>, BlockEntityType<CrystalApothecaryBlockEntity>> CRYSTAL_APOTHECARY = register("crystal_apothecary", CrystalApothecaryBlockEntity::new, SpectrumBlocks.CRYSTAL_APOTHECARY);
	public static DeferredHolder<BlockEntityType<?>, BlockEntityType<ColorPickerBlockEntity>> COLOR_PICKER = register("color_picker", ColorPickerBlockEntity::new, SpectrumBlocks.COLOR_PICKER);
	public static DeferredHolder<BlockEntityType<?>, BlockEntityType<TintingStationBlockEntity>> TINTING_STATION = register("tinting_station", TintingStationBlockEntity::new, SpectrumBlocks.TINTING_STATION);
	
	public static DeferredHolder<BlockEntityType<?>, BlockEntityType<CompactingChestBlockEntity>> COMPACTING_CHEST = register("compacting_chest", CompactingChestBlockEntity::new, SpectrumBlocks.COMPACTING_CHEST);
	public static DeferredHolder<BlockEntityType<?>, BlockEntityType<FabricationChestBlockEntity>> FABRICATION_CHEST = register("fabrication_chest", FabricationChestBlockEntity::new, SpectrumBlocks.FABRICATION_CHEST);
	public static DeferredHolder<BlockEntityType<?>, BlockEntityType<HeartboundChestBlockEntity>> HEARTBOUND_CHEST = register("heartbound_chest", HeartboundChestBlockEntity::new, SpectrumBlocks.HEARTBOUND_CHEST);
	public static DeferredHolder<BlockEntityType<?>, BlockEntityType<BlackHoleChestBlockEntity>> BLACK_HOLE_CHEST = register("black_hole_chest", BlackHoleChestBlockEntity::new, SpectrumBlocks.BLACK_HOLE_CHEST);
	public static DeferredHolder<BlockEntityType<?>, BlockEntityType<TreasureChestBlockEntity>> PRESERVATION_CHEST = register("preservation_chest", TreasureChestBlockEntity::new, SpectrumBlocks.PRESERVATION_CHEST);
	public static DeferredHolder<BlockEntityType<?>, BlockEntityType<AmphoraBlockEntity>> AMPHORA = register("amphora", AmphoraBlockEntity::new, SpectrumBlocks.CHESTNUT_NOXWOOD_AMPHORA, SpectrumBlocks.EBONY_NOXWOOD_AMPHORA, SpectrumBlocks.SLATE_NOXWOOD_AMPHORA, SpectrumBlocks.IVORY_NOXWOOD_AMPHORA, SpectrumBlocks.WEEPING_GALA_AMPHORA);
	
	public static DeferredHolder<BlockEntityType<?>, BlockEntityType<ProjectorBlockEntity>> PROJECTOR = register("projector", ProjectorBlockEntity::new, SpectrumBlocks.PYRITE_PROJECTOR);
	
	public static DeferredHolder<BlockEntityType<?>, BlockEntityType<PlayerDetectorBlockEntity>> PLAYER_DETECTOR = register("player_detector", PlayerDetectorBlockEntity::new, SpectrumBlocks.PLAYER_DETECTOR);
	public static DeferredHolder<BlockEntityType<?>, BlockEntityType<RedstoneCalculatorBlockEntity>> REDSTONE_CALCULATOR = register("redstone_calculator", RedstoneCalculatorBlockEntity::new, SpectrumBlocks.REDSTONE_CALCULATOR);
	public static DeferredHolder<BlockEntityType<?>, BlockEntityType<RedstoneTransceiverBlockEntity>> REDSTONE_TRANSCEIVER = register("redstone_transceiver", RedstoneTransceiverBlockEntity::new, SpectrumBlocks.REDSTONE_TRANSCEIVER);
	public static DeferredHolder<BlockEntityType<?>, BlockEntityType<BlockPlacerBlockEntity>> BLOCK_PLACER = register("block_placer", BlockPlacerBlockEntity::new, SpectrumBlocks.BLOCK_PLACER);
	public static DeferredHolder<BlockEntityType<?>, BlockEntityType<BlockBreakerBlockEntity>> BLOCK_BREAKER = register("block_breaker", BlockBreakerBlockEntity::new, SpectrumBlocks.BLOCK_BREAKER);
	public static DeferredHolder<BlockEntityType<?>, BlockEntityType<BlockFlooderBlockEntity>> BLOCK_FLOODER = register("block_flooder", BlockFlooderBlockEntity::new, SpectrumBlocks.BLOCK_FLOODER);
	public static DeferredHolder<BlockEntityType<?>, BlockEntityType<SpiritInstillerBlockEntity>> SPIRIT_INSTILLER = register("spirit_instiller", SpiritInstillerBlockEntity::new, SpectrumBlocks.SPIRIT_INSTILLER);
	public static DeferredHolder<BlockEntityType<?>, BlockEntityType<MemoryBlockEntity>> MEMORY = register("memory", MemoryBlockEntity::new, SpectrumBlocks.MEMORY);
	public static DeferredHolder<BlockEntityType<?>, BlockEntityType<JadeVineRootsBlockEntity>> JADE_VINE_ROOTS = register("jade_vine_roots", JadeVineRootsBlockEntity::new, SpectrumBlocks.JADE_VINE_ROOTS);
	public static DeferredHolder<BlockEntityType<?>, BlockEntityType<PresentBlockEntity>> PRESENT = register("present", PresentBlockEntity::new, SpectrumBlocks.PRESENT);
	public static DeferredHolder<BlockEntityType<?>, BlockEntityType<TitrationBarrelBlockEntity>> TITRATION_BARREL = register("titration_barrel", TitrationBarrelBlockEntity::new, SpectrumBlocks.TITRATION_BARREL);
	public static DeferredHolder<BlockEntityType<?>, BlockEntityType<PastelNodeBlockEntity>> PASTEL_NODE = register("pastel_node", PastelNodeBlockEntity::new,
			SpectrumBlocks.CONNECTION_NODE, SpectrumBlocks.INK_NODE,
			SpectrumBlocks.ITEM_PROVIDER_NODE, SpectrumBlocks.ITEM_STORAGE_NODE, SpectrumBlocks.ITEM_SENDER_NODE, SpectrumBlocks.ITEM_GATHER_NODE,
			SpectrumBlocks.FLUID_PROVIDER_NODE, SpectrumBlocks.FLUID_STORAGE_NODE, SpectrumBlocks.FLUID_SENDER_NODE, SpectrumBlocks.FLUID_GATHER_NODE);
	public static DeferredHolder<BlockEntityType<?>, BlockEntityType<HummingstoneBlockEntity>> HUMMINGSTONE = register("hummingstone", HummingstoneBlockEntity::new, SpectrumBlocks.HUMMINGSTONE);
	public static DeferredHolder<BlockEntityType<?>, BlockEntityType<PlacedItemBlockEntity>> PLACED_ITEM = register("placed_item", PlacedItemBlockEntity::new, SpectrumBlocks.INCANDESCENT_AMALGAM, SpectrumBlocks.THREAT_CONFLUX, SpectrumBlocks.PARAMETRIC_MINING_DEVICE,
			SpectrumBlocks.COLORFUL_SHOOTING_STAR, SpectrumBlocks.FIERY_SHOOTING_STAR, SpectrumBlocks.GEMSTONE_SHOOTING_STAR, SpectrumBlocks.GLISTERING_SHOOTING_STAR, SpectrumBlocks.PRISTINE_SHOOTING_STAR);
	
	public static DeferredHolder<BlockEntityType<?>, BlockEntityType<PreservationControllerBlockEntity>> PRESERVATION_CONTROLLER = register("preservation_controller", PreservationControllerBlockEntity::new, SpectrumBlocks.PRESERVATION_CONTROLLER);
	public static DeferredHolder<BlockEntityType<?>, BlockEntityType<PreservationRoundelBlockEntity>> PRESERVATION_ROUNDEL = register("preservation_roundel", PreservationRoundelBlockEntity::new, SpectrumBlocks.PRESERVATION_ROUNDEL);
	public static DeferredHolder<BlockEntityType<?>, BlockEntityType<PreservationBlockDetectorBlockEntity>> PRESERVATION_BLOCK_DETECTOR = register("preservation_block_detector", PreservationBlockDetectorBlockEntity::new, SpectrumBlocks.PRESERVATION_BLOCK_DETECTOR);
	public static DeferredHolder<BlockEntityType<?>, BlockEntityType<DeepLightBlockEntity>> DEEP_LIGHT = register("deep_light", DeepLightBlockEntity::new, SpectrumBlocks.DEEP_LIGHT_CHISELED_PRESERVATION_STONE);
	public static DeferredHolder<BlockEntityType<?>, BlockEntityType<PlayerTrackerBlockEntity>> PLAYER_TRACKING = register("player_tracking", PlayerTrackerBlockEntity::new, SpectrumBlocks.MANXI, SpectrumBlocks.PRESERVATION_ITEM_BOWL);
	
	@SafeVarargs
	private static <T extends BlockEntity> DeferredHolder<BlockEntityType<?>, BlockEntityType<T>> register(String id, BlockEntityType.BlockEntitySupplier<T> factory, Supplier<? extends Block>... blocks) {
		return REGISTRAR.register(id, () -> BlockEntityType.Builder.of(factory, Arrays.stream(blocks).map(Supplier::get).toList().toArray(new Block[0])).build(null));
	}
	
	public static void register(IEventBus eventBus) {
		REGISTRAR.register(eventBus);
	}
	
	public static void addBlockEntityTypeBlocks(BlockEntityTypeAddBlocksEvent event) {
		event.modify(BlockEntityType.BARREL, SpectrumBlocks.WEEPING_GALA_BARREL.get());
		
		List<Block> skullBlocksList = new ArrayList<>(SpectrumSkullBlock.getMobHeads()
				.size() + SpectrumWallSkullBlock.getMobWallHeads()
				.size());
		skullBlocksList.addAll(SpectrumSkullBlock.getMobHeads());
		skullBlocksList.addAll(SpectrumWallSkullBlock.getMobWallHeads());
		
		event.modify(SpectrumBlockEntities.SKULL.get(), skullBlocksList.toArray(new Block[0]));
	}
	
	public static void registerClient(FMLClientSetupEvent event) {
		BlockEntityRenderers.register(SpectrumBlockEntities.PEDESTAL.get(), PedestalBlockEntityRenderer::new);
		BlockEntityRenderers.register(SpectrumBlockEntities.BOTTOMLESS_BUNDLE.get(), BottomlessBundleBlockEntityRenderer::new);
		BlockEntityRenderers.register(SpectrumBlockEntities.HEARTBOUND_CHEST.get(), HeartboundChestBlockEntityRenderer::new);
		BlockEntityRenderers.register(SpectrumBlockEntities.COMPACTING_CHEST.get(), CompactingChestBlockEntityRenderer::new);
		BlockEntityRenderers.register(SpectrumBlockEntities.FABRICATION_CHEST.get(), FabricationChestBlockEntityRenderer::new);
		BlockEntityRenderers.register(SpectrumBlockEntities.PRESERVATION_CHEST.get(), SpectrumChestBlockEntityRenderer::new);
		BlockEntityRenderers.register(SpectrumBlockEntities.BLACK_HOLE_CHEST.get(), BlackHoleChestBlockEntityRenderer::new);
		BlockEntityRenderers.register(SpectrumBlockEntities.UPGRADE_BLOCK.get(), UpgradeBlockBlockEntityRenderer::new);
		BlockEntityRenderers.register(SpectrumBlockEntities.FUSION_SHRINE.get(), FusionShrineBlockEntityRenderer::new);
		BlockEntityRenderers.register(SpectrumBlockEntities.ENCHANTER.get(), EnchanterBlockEntityRenderer::new);
		BlockEntityRenderers.register(SpectrumBlockEntities.ITEM_BOWL.get(), ItemBowlBlockEntityRenderer::new);
		BlockEntityRenderers.register(SpectrumBlockEntities.ITEM_ROUNDEL.get(), ItemRoundelBlockEntityRenderer::new);
		BlockEntityRenderers.register(SpectrumBlockEntities.PRESERVATION_ROUNDEL.get(), ItemRoundelBlockEntityRenderer::new);
		BlockEntityRenderers.register(SpectrumBlockEntities.SKULL.get(), SpectrumSkullBlockEntityRenderer::new);
		BlockEntityRenderers.register(SpectrumBlockEntities.SPIRIT_INSTILLER.get(), SpiritInstillerBlockEntityRenderer::new);
		BlockEntityRenderers.register(SpectrumBlockEntities.JADE_VINE_ROOTS.get(), JadeVineRootsBlockEntityRenderer::new);
		BlockEntityRenderers.register(SpectrumBlockEntities.CRYSTALLARIEUM.get(), CrystallarieumBlockEntityRenderer::new);
		BlockEntityRenderers.register(SpectrumBlockEntities.COLOR_PICKER.get(), ColorPickerBlockEntityRenderer::new);
		BlockEntityRenderers.register(SpectrumBlockEntities.PRESERVATION_CONTROLLER.get(), PreservationControllerBlockEntityRenderer::new);
		BlockEntityRenderers.register(SpectrumBlockEntities.PROJECTOR.get(), ProjectorBlockEntityRenderer::new);
		BlockEntityRenderers.register(SpectrumBlockEntities.DEEP_LIGHT.get(), DeepLightBlockEntityRenderer::new);
		BlockEntityRenderers.register(SpectrumBlockEntities.PLAYER_TRACKING.get(), PlayerTrackingBlockEntityRenderer::new);
		BlockEntityRenderers.register(SpectrumBlockEntities.PASTEL_NODE.get(), PastelNodeBlockEntityRenderer::new);
	}
	
}
