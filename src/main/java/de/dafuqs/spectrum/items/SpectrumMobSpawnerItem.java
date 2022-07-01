package de.dafuqs.spectrum.items;

import de.dafuqs.spectrum.registries.SpectrumItems;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.MobSpawnerBlockEntity;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.sound.SoundCategory;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.Property;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import org.jetbrains.annotations.Nullable;

import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class SpectrumMobSpawnerItem extends Item {
	
	public SpectrumMobSpawnerItem(FabricItemSettings fabricItemSettings) {
		super(fabricItemSettings);
	}
	
	@Override
	public ActionResult useOnBlock(ItemUsageContext context) {
		ActionResult actionResult = this.place(new ItemPlacementContext(context));
		if (!actionResult.isAccepted() && this.isFood()) {
			ActionResult actionResult2 = this.use(context.getWorld(), context.getPlayer(), context.getHand()).getResult();
			return actionResult2 == ActionResult.CONSUME ? ActionResult.CONSUME_PARTIAL : actionResult2;
		} else {
			return actionResult;
		}
	}
	
	protected boolean place(ItemPlacementContext context, BlockState state) {
		return context.getWorld().setBlockState(context.getBlockPos(), state, 11);
	}
	
	public ActionResult place(ItemPlacementContext context) {
		if (!context.canPlace()) {
			return ActionResult.FAIL;
		} else {
			BlockState blockState = Blocks.SPAWNER.getDefaultState();
			if (blockState == null) {
				return ActionResult.FAIL;
			} else if (!this.place(context, blockState)) {
				return ActionResult.FAIL;
			} else {
				BlockPos blockPos = context.getBlockPos();
				World world = context.getWorld();
				PlayerEntity playerEntity = context.getPlayer();
				ItemStack itemStack = context.getStack();
				BlockState blockState2 = world.getBlockState(blockPos);
				if (blockState2.isOf(blockState.getBlock())) {
					blockState2 = this.placeFromNbt(blockPos, world, itemStack, blockState2);
					this.postPlacement(blockPos, world, playerEntity, itemStack, blockState2);
					blockState2.getBlock().onPlaced(world, blockPos, blockState2, playerEntity, itemStack);
					if (playerEntity instanceof ServerPlayerEntity) {
						Criteria.PLACED_BLOCK.trigger((ServerPlayerEntity)playerEntity, blockPos, itemStack);
					}
				}
				
				BlockSoundGroup blockSoundGroup = blockState2.getSoundGroup();
				world.playSound(playerEntity, blockPos, Blocks.SPAWNER.getSoundGroup(blockState).getPlaceSound(), SoundCategory.BLOCKS, (blockSoundGroup.getVolume() + 1.0F) / 2.0F, blockSoundGroup.getPitch() * 0.8F);
				world.emitGameEvent(playerEntity, GameEvent.BLOCK_PLACE, blockPos);
				if (playerEntity == null || !playerEntity.getAbilities().creativeMode) {
					itemStack.decrement(1);
				}
				
				return ActionResult.success(world.isClient);
			}
		}
	}
	
	private BlockState placeFromNbt(BlockPos pos, World world, ItemStack stack, BlockState state) {
		BlockState blockState = state;
		NbtCompound nbtCompound = stack.getNbt();
		if (nbtCompound != null) {
			NbtCompound nbtCompound2 = nbtCompound.getCompound("BlockStateTag");
			StateManager<Block, BlockState> stateManager = state.getBlock().getStateManager();
			
			for (String string : nbtCompound2.getKeys()) {
				Property<?> property = stateManager.getProperty(string);
				if (property != null) {
					String string2 = Objects.requireNonNull(nbtCompound2.get(string)).asString();
					blockState = with(blockState, property, string2);
				}
			}
		}
		
		if (blockState != state) {
			world.setBlockState(pos, blockState, 2);
		}
		
		return blockState;
	}
	
	private static <T extends Comparable<T>> BlockState with(BlockState state, Property<T> property, String name) {
		return property.parse(name).map((value) -> state.with(property, value)).orElse(state);
	}
	
	protected boolean postPlacement(BlockPos pos, World world, @Nullable PlayerEntity player, ItemStack stack, BlockState state) {
		return BlockItem.writeNbtToBlockEntity(world, player, pos, stack);
	}
	
	
	public static ItemStack toItemStack(MobSpawnerBlockEntity mobSpawnerBlockEntity) {
		ItemStack itemStack = new ItemStack(SpectrumItems.SPAWNER, 1);
		
		NbtCompound blockEntityTag = mobSpawnerBlockEntity.createNbt();
		blockEntityTag.remove("x");
		blockEntityTag.remove("y");
		blockEntityTag.remove("z");
		blockEntityTag.remove("id");
		blockEntityTag.remove("delay");
		
		NbtCompound itemStackTag = new NbtCompound();
		itemStackTag.put("BlockEntityTag", blockEntityTag);
		itemStack.setNbt(itemStackTag);
		return itemStack;
	}
	
	@Override
	public void appendTooltip(ItemStack itemStack, World world, List<Text> tooltip, TooltipContext tooltipContext) {
		if (itemStack.getNbt() != null && itemStack.getNbt().get("BlockEntityTag") != null) {
			Optional<EntityType<?>> entityType = Optional.empty();
			
			NbtCompound blockEntityTag = itemStack.getNbt().getCompound("BlockEntityTag");
			
			if (blockEntityTag.contains("SpawnData", NbtElement.COMPOUND_TYPE)
					&& blockEntityTag.getCompound("SpawnData").contains("entity", NbtElement.COMPOUND_TYPE)
					&& blockEntityTag.getCompound("SpawnData").getCompound("entity").contains("id", NbtElement.STRING_TYPE)) {
				String spawningEntityType = blockEntityTag.getCompound("SpawnData").getCompound("entity").getString("id");
				entityType = EntityType.get(spawningEntityType);
			}
			
			try {
				short spawnCount = blockEntityTag.getShort("SpawnCount");
				short minSpawnDelay = blockEntityTag.getShort("MinSpawnDelay");
				short maxSpawnDelay = blockEntityTag.getShort("MaxSpawnDelay");
				short spawnRange = blockEntityTag.getShort("SpawnRange");
				short requiredPlayerRange = blockEntityTag.getShort("RequiredPlayerRange");
				
				if (entityType.isPresent()) {
					tooltip.add(new TranslatableText(entityType.get().getTranslationKey()));
				} else {
					tooltip.add(new TranslatableText("item.spectrum.spawner.tooltip.unknown_mob"));
				}
				if (spawnCount > 0) {
					tooltip.add(new TranslatableText("item.spectrum.spawner.tooltip.spawn_count", spawnCount).formatted(Formatting.GRAY));
				}
				if (minSpawnDelay > 0) {
					tooltip.add(new TranslatableText("item.spectrum.spawner.tooltip.min_spawn_delay", minSpawnDelay).formatted(Formatting.GRAY));
				}
				if (maxSpawnDelay > 0) {
					tooltip.add(new TranslatableText("item.spectrum.spawner.tooltip.max_spawn_delay", maxSpawnDelay).formatted(Formatting.GRAY));
				}
				if (spawnRange > 0) {
					tooltip.add(new TranslatableText("item.spectrum.spawner.tooltip.spawn_range", spawnRange).formatted(Formatting.GRAY));
				}
				if (requiredPlayerRange > 0) {
					tooltip.add(new TranslatableText("item.spectrum.spawner.tooltip.required_player_range", requiredPlayerRange).formatted(Formatting.GRAY));
				}
			} catch (Exception e) {
				tooltip.add(new TranslatableText("item.spectrum.spawner.tooltip.unknown_mob"));
			}
		}
	}
	
}
