package de.dafuqs.spectrum.entity;

import de.dafuqs.spectrum.entity.render.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.client.renderer.entity.*;
import net.minecraft.world.entity.*;
import net.neoforged.fml.event.lifecycle.*;


public class SpectrumEntityRenderers {
	
	public static void registerClient(FMLClientSetupEvent event) {
		register(SpectrumEntityTypes.FLOAT_BLOCK.get(), FallingBlockRenderer::new);
		register(SpectrumEntityTypes.SEAT.get(), SeatEntityRenderer::new);
		register(SpectrumEntityTypes.SHOOTING_STAR.get(), ShootingStarEntityRenderer::new);
		register(SpectrumEntityTypes.PHANTOM_FRAME.get(), PhantomFrameEntityRenderer::new);
		register(SpectrumEntityTypes.PARAMETRIC_MINING_DEVICE_ENTITY.get(), ThrownItemRenderer::new);
		register(SpectrumEntityTypes.GLOW_PHANTOM_FRAME.get(), PhantomFrameEntityRenderer::new);
		register(SpectrumEntityTypes.BLOCK_FLOODER_PROJECTILE.get(), ThrownItemRenderer::new);
		register(SpectrumEntityTypes.INK_PROJECTILE.get(), MagicProjectileEntityRenderer::new);
		register(SpectrumEntityTypes.LAGOON_FISHING_BOBBER.get(), LagoonFishingBobberEntityRenderer::new);
		register(SpectrumEntityTypes.MOLTEN_FISHING_BOBBER.get(), MoltenFishingBobberEntityRenderer::new);
		register(SpectrumEntityTypes.BEDROCK_FISHING_BOBBER.get(), BedrockFishingBobberEntityRenderer::new);
		register(SpectrumEntityTypes.FIREPROOF_ITEM.get(), ItemEntityRenderer::new);
		register(SpectrumEntityTypes.EGG_LAYING_WOOLY_PIG.get(), EggLayingWoolyPigEntityRenderer::new);
		register(SpectrumEntityTypes.GLASS_ARROW.get(), GlassArrowEntityRenderer::new);
		register(SpectrumEntityTypes.MINING_PROJECTILE.get(), MagicProjectileEntityRenderer::new);
		register(SpectrumEntityTypes.BIDENT.get(), BidentEntityRenderer::new);
		register(SpectrumEntityTypes.BIDENT_MIRROR_IMAGE.get(), BidentEntityRenderer::new);
		register(SpectrumEntityTypes.LIGHT_SHARD.get(), LightShardEntityRenderer::new);
		register(SpectrumEntityTypes.LIGHT_SPEAR.get(), LightSpearEntityRenderer::new);
		register(SpectrumEntityTypes.LIGHT_MINE.get(), LightMineEntityRenderer::new);
		register(SpectrumEntityTypes.MONSTROSITY.get(), MonstrosityEntityRenderer::new);
		register(SpectrumEntityTypes.PRESERVATION_TURRET.get(), PreservationTurretEntityRenderer::new);
		register(SpectrumEntityTypes.LIZARD.get(), LizardEntityRenderer::new);
		register(SpectrumEntityTypes.KINDLING.get(), KindlingEntityRenderer::new);
		register(SpectrumEntityTypes.KINDLING_COUGH.get(), KindlingCoughEntityRenderer::new);
		register(SpectrumEntityTypes.ERASER.get(), EraserEntityRenderer::new);
		register(SpectrumEntityTypes.ITEM_PROJECTILE.get(), ThrownItemRenderer::new);
		register(SpectrumEntityTypes.DRAGON_TALON.get(), (context) -> new BidentEntityRenderer(context, 1.5F, 0));
		register(SpectrumEntityTypes.DRACONIC_TWINSWORD.get(), (context) -> new BidentEntityRenderer(context, 2.15F, 0));
		register(SpectrumEntityTypes.MARROW.get(), MarrowRenderer::new);
		register(SpectrumEntityTypes.SPLINTERSPAWN.get(), SplinterspawnRenderer::new);
	}
	
	private static <T extends Entity> void register(EntityType<? extends T> type, EntityRendererProvider<T> factory) {
		EntityRenderers.register(type, factory);
	}
	
}