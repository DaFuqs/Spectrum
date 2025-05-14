package de.dafuqs.spectrum.items.map;

import com.mojang.datafixers.util.*;
import de.dafuqs.spectrum.registries.*;
import net.fabricmc.api.*;
import net.minecraft.client.*;
import net.minecraft.core.*;
import net.minecraft.core.component.*;
import net.minecraft.network.chat.*;
import net.minecraft.resources.*;
import net.minecraft.server.level.*;
import net.minecraft.world.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.item.*;
import net.minecraft.world.item.context.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.*;
import net.minecraft.world.level.chunk.*;
import net.minecraft.world.level.levelgen.*;
import net.minecraft.world.level.levelgen.structure.*;
import net.minecraft.world.level.material.*;
import net.minecraft.world.level.saveddata.maps.*;
import net.minecraft.world.phys.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class ArtisansAtlasItem extends MapItem {
	
	public static final int COOLDOWN_DURATION_TICKS = 20;
	
	public ArtisansAtlasItem(Properties settings) {
		super(settings);
	}
	
	private static void createAndSetState(ItemStack stack, ServerLevel world, int centerX, int centerZ, @Nullable StructureStart target, @Nullable ResourceLocation targetId) {
		ArtisansAtlasState state = new ArtisansAtlasState(centerX, centerZ, (byte) 1, true, true, false, world.dimension());
		MapId id = world.getFreeMapId();
		
		state.setTargetId(targetId);
		if (targetId != null) {
			state.startLocator(world);
			if (target != null) {
				state.addTarget(world, target.getBoundingBox().getCenter());
			}
		} else {
			state.cancelLocator();
		}
		
		world.setMapData(id, state);
		stack.set(DataComponents.MAP_ID, id);
	}
	
	@Override
	public void update(Level world, Entity entity, MapItemSavedData state) {
		if (world.dimension() != state.dimension || !(entity instanceof Player playerEntity) || !(state instanceof ArtisansAtlasState atlasState)) {
			return;
		}
		
		int sampleSize = 1 << state.scale;
		MapItemSavedData.HoldingPlayer playerUpdateTracker = state.getHoldingPlayer(playerEntity);
		playerUpdateTracker.step++;
		
		Vec3i delta = atlasState.getDisplayDelta();
		if (delta == null) {
			// Delta is null when the state is first created, so update the whole thing
			delta = entity.blockPosition().subtract(atlasState.getDisplayedCenter());
			int deltaX = delta.getX() / sampleSize;
			int deltaZ = delta.getZ() / sampleSize;
			
			for (int x = 0; x <= 127; x++) {
				updateVerticalStrip(world, atlasState, deltaX, deltaZ, x, 0, 127);
			}
			
			atlasState.clearDisplayDelta();
			return;
		}
		
		// Re-render the part that's moved, and copy the rest
		int deltaX = delta.getX() / sampleSize;
		int deltaZ = delta.getZ() / sampleSize;
		
		if (deltaX < 0) {
			for (int x = 127; x >= -deltaX; x--) {
				updateOrCopyVerticalStrip(world, atlasState, deltaX, deltaZ, x, playerUpdateTracker.step);
			}
			for (int x = 0; x <= Math.min(127, -deltaX - 1); x++) {
				updateVerticalStrip(world, atlasState, deltaX, deltaZ, x, 0, 127);
			}
		} else {
			for (int x = 0; x <= 127 - deltaX; x++) {
				updateOrCopyVerticalStrip(world, atlasState, deltaX, deltaZ, x, playerUpdateTracker.step);
			}
			for (int x = Math.max(0, 127 - deltaX + 1); x <= 127; x++) {
				updateVerticalStrip(world, atlasState, deltaX, deltaZ, x, 0, 127);
			}
		}
		
		if (deltaX != 0 || deltaZ != 0) {
			atlasState.clearDisplayDelta();
		}
	}
	
	private void updateOrCopyVerticalStrip(Level world, ArtisansAtlasState state, int deltaX, int deltaZ, int x, int tick) {
		if (deltaX > 127 || deltaX < -127 || deltaZ > 127 || deltaZ < -127 || (x & 15) == (tick & 15)) {
			updateVerticalStrip(world, state, deltaX, deltaZ, x, 0, 127);
		} else if (deltaZ < 0) {
			copyVerticalStrip(state, deltaX, deltaZ, x, 127, -deltaZ);
			updateVerticalStrip(world, state, deltaX, deltaZ, x, 0, -deltaZ - 1);
		} else if (deltaZ > 0) {
			copyVerticalStrip(state, deltaX, deltaZ, x, 0, 127 - deltaZ);
			updateVerticalStrip(world, state, deltaX, deltaZ, x, 127 - deltaZ + 1, 127);
		} else if (deltaX != 0) {
			copyVerticalStrip(state, deltaX, deltaZ, x, 0, 127);
		}
	}
	
	private void copyVerticalStrip(ArtisansAtlasState state, int deltaX, int deltaZ, int x, int startZ, int endZ) {
		if (startZ > endZ) {
			for (int z = startZ; z >= endZ; z--) {
				state.setColor(x, z, state.colors[(x + deltaX) + (z + deltaZ) * 128]);
			}
		} else {
			for (int z = startZ; z <= endZ; z++) {
				state.setColor(x, z, state.colors[(x + deltaX) + (z + deltaZ) * 128]);
			}
		}
	}
	
	private void updateVerticalStrip(Level world, ArtisansAtlasState state, int deltaX, int deltaZ, int x, int startZ, int endZ) {
		double previousHeight = getHeight(world, state, deltaX, deltaZ, x, startZ - 1);
		for (int z = startZ; z <= endZ; z++) {
			previousHeight = updateColor(world, state, deltaX, deltaZ, x, z, previousHeight);
		}
	}
	
	private double updateColor(Level world, ArtisansAtlasState state, int deltaX, int deltaZ, int x, int z, double previousHeight) {
		int sampleSize = 1 << state.scale;
		int sampleArea = sampleSize * sampleSize;
		int sampleMask = sampleSize - 1;
		
		boolean hasCeiling = world.dimensionType().hasCeiling();
		
		int blockX = ((state.getDisplayedCenter().getX() >> state.scale) + deltaX + x - 64) * sampleSize;
		int blockZ = ((state.getDisplayedCenter().getZ() >> state.scale) + deltaZ + z - 64) * sampleSize;
		
		LevelChunk chunk = world.getChunk(SectionPos.blockToSectionCoord(blockX), SectionPos.blockToSectionCoord(blockZ));
		if (chunk.isEmpty()) {
			return previousHeight;
		}
		
		
		int[] multiset = new int[64];
		
		int fluidDepth = 0;
		double height;
		if (hasCeiling) {
			int hash = blockX + blockZ * 231871;
			hash = hash * hash * 31287121 + hash * 11;
			if ((hash >> 20 & 1) == 0) {
				multiset[Blocks.DIRT.defaultBlockState().getMapColor(world, BlockPos.ZERO).id] += 10;
			} else {
				multiset[Blocks.STONE.defaultBlockState().getMapColor(world, BlockPos.ZERO).id] += 100;
			}
			
			height = 100.0;
		} else {
			height = 0.0;
			int bottomY = world.getMinBuildHeight();
			BlockPos.MutableBlockPos samplePos = new BlockPos.MutableBlockPos(0, 0, 0);
			
			for (int sample = 0; sample < sampleArea; sample++) {
				
				int posX = blockX + (sample >> state.scale);
				int posZ = blockZ + (sample & sampleMask);
				samplePos.setX(posX);
				samplePos.setZ(posZ);
				
				int sampleY = chunk.getHeight(Heightmap.Types.WORLD_SURFACE, posX, posZ) + 1;
				
				BlockState blockState;
				MapColor mapColor;
				if (sampleY <= bottomY + 1) {
					blockState = Blocks.BEDROCK.defaultBlockState();
					mapColor = blockState.getMapColor(world, samplePos);
				} else {
					do {
						samplePos.setY(--sampleY);
						blockState = chunk.getBlockState(samplePos);
						mapColor = blockState.getMapColor(world, samplePos);
					} while (mapColor == MapColor.NONE && sampleY > bottomY);
					
					if (sampleY > bottomY && !blockState.getFluidState().isEmpty()) {
						int fluidY = sampleY - 1;
						BlockPos.MutableBlockPos fluidPos = samplePos.mutable();
						
						BlockState fluidBlockState;
						do {
							fluidPos.setY(fluidY--);
							fluidBlockState = chunk.getBlockState(fluidPos);
						} while (fluidY > bottomY && !fluidBlockState.getFluidState().isEmpty());
						
						fluidDepth += sampleY - 1 - fluidY;
						
						FluidState fluidState = blockState.getFluidState();
						blockState = !fluidState.isEmpty() && !blockState.isFaceSturdy(world, samplePos, Direction.UP)
								? fluidState.createLegacyBlock() : blockState;
						
						mapColor = blockState.getMapColor(world, samplePos);
					}
				}
				
				state.checkBanners(world, posX, posZ);
				
				height += sampleY;
				multiset[mapColor.id]++;
			}
			
			height /= sampleArea;
			fluidDepth /= sampleArea;
		}
		
		int maxCount = 0;
		MapColor color = MapColor.NONE;
		for (int i = 0; i < multiset.length; i++) {
			if (multiset[i] > maxCount) {
				maxCount = multiset[i];
				color = MapColor.byId(i);
			}
		}
		
		MapColor.Brightness brightness;
		int odd = ((blockX ^ blockZ) / sampleSize) & 1;
		if (color == MapColor.WATER) {
			double depth = (double) fluidDepth * 0.1 + (double) odd * 0.2;
			if (depth < 0.5) {
				brightness = MapColor.Brightness.HIGH;
			} else if (depth > 0.9) {
				brightness = MapColor.Brightness.LOW;
			} else {
				brightness = MapColor.Brightness.NORMAL;
			}
		} else {
			double f = (height - previousHeight) * 4.0 / (double) (sampleSize + 4) + ((double) odd - 0.5) * 0.4;
			if (f > 0.6) {
				brightness = MapColor.Brightness.HIGH;
			} else if (f < -0.6) {
				brightness = MapColor.Brightness.LOW;
			} else {
				brightness = MapColor.Brightness.NORMAL;
			}
		}
		
		state.setColor(x, z, color.getPackedId(brightness));
		
		return height;
	}
	
	private double getHeight(Level world, ArtisansAtlasState state, int deltaX, int deltaZ, int x, int z) {
		int sampleSize = 1 << state.scale;
		int sampleArea = sampleSize * sampleSize;
		int sampleMask = sampleSize - 1;
		
		int blockX = ((state.getDisplayedCenter().getX() >> state.scale) + deltaX + x - 64) * sampleSize;
		int blockZ = ((state.getDisplayedCenter().getZ() >> state.scale) + deltaZ + z - 64) * sampleSize;
		
		LevelChunk chunk = world.getChunk(SectionPos.blockToSectionCoord(blockX), SectionPos.blockToSectionCoord(blockZ));
		if (chunk.isEmpty())
			return 0;
		
		double height;
		if (world.dimensionType().hasCeiling()) {
			height = 100.0;
		} else {
			height = 0.0;
			int bottomY = world.getMinBuildHeight();
			
			for (int sample = 0; sample < sampleArea; sample++) {
				int posX = blockX + (sample >> state.scale);
				int posZ = blockZ + (sample & sampleMask);
				
				BlockPos.MutableBlockPos samplePos = new BlockPos.MutableBlockPos(posX, 0, posZ);
				int sampleY = chunk.getHeight(Heightmap.Types.WORLD_SURFACE, posX, posZ) + 1;
				
				if (sampleY > bottomY + 1) {
					BlockState blockState;
					do {
						samplePos.setY(--sampleY);
						blockState = chunk.getBlockState(samplePos);
					} while (blockState.getMapColor(world, samplePos) == MapColor.NONE && sampleY > bottomY);
				}
				
				height += sampleY;
			}
			
			height /= sampleArea;
		}
		
		return height;
	}
	
	@Override
	public void inventoryTick(ItemStack stack, Level world, Entity entity, int slot, boolean selected) {
		if (!world.isClientSide) {
			MapItemSavedData state = getSavedData(stack, world);
			if (state instanceof ArtisansAtlasState atlasState) {
				atlasState.updateDimension(world.dimension());
			}
		}
		
		super.inventoryTick(stack, world, entity, slot, selected);
	}
	
	@Override
	public InteractionResult useOn(UseOnContext context) {
		if (!context.getLevel().isClientSide() && context.getLevel() instanceof ServerLevel serverWorld && context.getPlayer() instanceof ServerPlayer serverPlayerEntity) {
			ItemStack stack = serverPlayerEntity.getItemInHand(context.getHand());
			if (serverPlayerEntity.isShiftKeyDown()) {
				Vec3 hitPos = context.getClickLocation();
				BlockPos blockPos = BlockPos.containing(hitPos.x(), hitPos.y(), hitPos.z());
				Pair<ResourceLocation, StructureStart> pair = ArtisansAtlasState.locateAnyStructureAtBlock(serverWorld, blockPos);
				if (pair != null) {
					ResourceLocation structureId = pair.getFirst();
					if (SpectrumStructureTags.isIn(serverWorld, structureId, SpectrumStructureTags.UNLOCATABLE)) { // TODO: use c: tag
						serverPlayerEntity.displayClientMessage(Component.translatable("item.spectrum.artisans_atlas.unlocatable"), true);
					} else {
						serverPlayerEntity.displayClientMessage(Component.translatable("item.spectrum.artisans_atlas.set_structure").append(Component.translatable(structureId.toLanguageKey("structure"))), true);
						createAndSetState(stack, serverWorld, (int) serverPlayerEntity.getX(), (int) serverPlayerEntity.getZ(), pair.getSecond(), pair.getFirst());
					}
				}
				
				serverPlayerEntity.getCooldowns().addCooldown(stack.getItem(), COOLDOWN_DURATION_TICKS);
			}
		}
		
		return InteractionResult.sidedSuccess(context.getLevel().isClientSide());
	}
	
	@Override
	public InteractionResultHolder<ItemStack> use(Level world, Player user, InteractionHand hand) {
		ItemStack stack = user.getItemInHand(hand);
		
		if (!world.isClientSide() && world instanceof ServerLevel serverWorld && user instanceof ServerPlayer serverPlayerEntity) {
			if (user.isShiftKeyDown())
				createAndSetState(stack, serverWorld, (int) serverPlayerEntity.getX(), (int) serverPlayerEntity.getZ(), null, null);
		}
		
		return InteractionResultHolder.sidedSuccess(stack, world.isClientSide());
	}
	
	@Override
	@Environment(EnvType.CLIENT)
	public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag type) {
		super.appendHoverText(stack, context, tooltip, type);
		
		var world = Minecraft.getInstance().level;
		if (world == null) return;
		
		if (getSavedData(stack, world) instanceof ArtisansAtlasState atlasState) {
			ResourceLocation structureId = atlasState.getTargetId();
			if (structureId == null)
				tooltip.add(Component.translatable("item.spectrum.artisans_atlas.empty"));
			else
				tooltip.add(Component.translatable("item.spectrum.artisans_atlas.locates_structure").append(Component.translatable(structureId.toLanguageKey("structure"))));
		}
		
	}
	
}
