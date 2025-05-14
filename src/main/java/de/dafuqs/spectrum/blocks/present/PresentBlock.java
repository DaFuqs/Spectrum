package de.dafuqs.spectrum.blocks.present;

import com.mojang.serialization.*;
import de.dafuqs.spectrum.api.item.*;
import de.dafuqs.spectrum.helpers.*;
import de.dafuqs.spectrum.networking.s2c_payloads.*;
import de.dafuqs.spectrum.particle.effect.*;
import it.unimi.dsi.fastutil.objects.*;
import net.minecraft.client.multiplayer.*;
import net.minecraft.core.*;
import net.minecraft.core.particles.*;
import net.minecraft.network.chat.*;
import net.minecraft.server.level.*;
import net.minecraft.sounds.*;
import net.minecraft.util.*;
import net.minecraft.world.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.item.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.*;
import net.minecraft.world.level.block.state.*;
import net.minecraft.world.level.block.state.properties.*;
import net.minecraft.world.level.pathfinder.*;
import net.minecraft.world.level.storage.loot.*;
import net.minecraft.world.level.storage.loot.parameters.*;
import net.minecraft.world.phys.*;
import net.minecraft.world.phys.shapes.*;
import org.jetbrains.annotations.*;
import org.joml.*;

import java.util.*;

public class PresentBlock extends BaseEntityBlock {
	
	public static final MapCodec<PresentBlock> CODEC = simpleCodec(PresentBlock::new);
	
	protected static Map<Item, PresentUnpackBehavior> BEHAVIORS = new Object2ObjectOpenHashMap<>();
	
	public @Nullable PresentUnpackBehavior getBehaviorFor(ItemStack stack) {
		return BEHAVIORS.getOrDefault(stack.getItem(), null);
	}
	
	public static void registerBehavior(ItemLike provider, PresentUnpackBehavior behavior) {
		BEHAVIORS.put(provider.asItem(), behavior);
	}
	
	public enum WrappingPaper implements StringRepresentable {
		RED(Blocks.RED_WOOL),
		BLUE(Blocks.BLUE_WOOL),
		CYAN(Blocks.CYAN_WOOL),
		GREEN(Blocks.GREEN_WOOL),
		PURPLE(Blocks.PURPLE_WOOL),
		CAKE(Blocks.LIGHT_GRAY_WOOL),
		STRIPED(Blocks.GREEN_WOOL),
		STARRY(Blocks.PURPLE_WOOL),
		WINTER(Blocks.LIGHT_GRAY_WOOL),
		PRIDE(Blocks.ORANGE_WOOL);
		
		public final Block woolBase;
		
		WrappingPaper(Block woolBase) {
			this.woolBase = woolBase;
		}
		
		@Override
		public String getSerializedName() {
			return this.toString().toLowerCase(Locale.ROOT);
		}
	}
	
	public static final int TICKS_PER_OPENING_STEP = 20;
	public static final int OPENING_STEPS = 6;
	
	public static final BooleanProperty OPENING = BooleanProperty.create("opening");
	public static final EnumProperty<WrappingPaper> VARIANT = EnumProperty.create("variant", WrappingPaper.class);
	protected static final VoxelShape SHAPE = Block.box(2.0D, 0.0D, 2.0D, 14.0D, 10.0D, 14.0D);
	
	public PresentBlock(Properties settings) {
		super(settings);
		this.registerDefaultState(this.stateDefinition.any().setValue(OPENING, false).setValue(VARIANT, WrappingPaper.RED));
	}
	
	@Override
	public MapCodec<? extends PresentBlock> codec() {
		return CODEC;
	}
	
	@Override
	protected void createBlockStateDefinition(StateDefinition.@NotNull Builder<Block, BlockState> builder) {
		builder.add(OPENING, VARIANT);
	}
	
	@Override
	public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
		return SHAPE;
	}
	
	@Override
	public boolean canSurvive(@NotNull BlockState state, LevelReader world, BlockPos pos) {
		BlockState downState = world.getBlockState(pos.below());
		return downState.isFaceSturdy(world, pos, Direction.UP);
	}
	
	@Override
	public void setPlacedBy(@NotNull Level world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
		BlockEntity blockEntity = world.getBlockEntity(pos);
		world.setBlockAndUpdate(pos, state.setValue(PresentBlock.VARIANT, PresentBlockItem.getWrapData(itemStack).variant()));
		if (blockEntity instanceof PresentBlockEntity presentBlockEntity) {
			presentBlockEntity.setPresent(itemStack);
		}
	}
	
	@Override
	public InteractionResult useWithoutItem(BlockState state, Level world, BlockPos pos, Player player, BlockHitResult hit) {
		if (!player.getAbilities().mayBuild) {
			return InteractionResult.PASS;
		} else {
			if (world.isClientSide) {
				return InteractionResult.SUCCESS;
			} else {
				BlockEntity blockEntity = world.getBlockEntity(pos);
				if (blockEntity instanceof PresentBlockEntity presentBlockEntity) {
					if (player.isShiftKeyDown()) {
						presentBlockEntity.setOpenerUUID(player);
						state = state.setValue(OPENING, true);
						world.setBlock(pos, state, 3);
						world.scheduleTick(pos, state.getBlock(), TICKS_PER_OPENING_STEP);
					} else {
						if (presentBlockEntity.getOwnerName() != null) {
							player.displayClientMessage(Component.translatable("block.spectrum.present.tooltip.wrapped_placed.giver", presentBlockEntity.getOwnerName()), true);
						} else {
							player.displayClientMessage(Component.translatable("block.spectrum.present.tooltip.wrapped_placed"), true);
						}
						
					}
				}
				return InteractionResult.CONSUME;
			}
		}
	}
	
	@Override
	public List<ItemStack> getDrops(BlockState state, LootParams.Builder builder) {
		BlockEntity blockEntity = builder.getParameter(LootContextParams.BLOCK_ENTITY);
		if (blockEntity instanceof PresentBlockEntity presentBlockEntity) {
			return List.of(presentBlockEntity.retrievePresent());
		} else {
			return super.getDrops(state, builder);
		}
	}
	
	@Override
	public void tick(BlockState state, ServerLevel world, BlockPos pos, RandomSource random) {
		if (state.getValue(OPENING) && !world.isClientSide) {
			if (world.getBlockEntity(pos) instanceof PresentBlockEntity presentBlockEntity) {
				int openingTick = presentBlockEntity.openingTick();
				Vec3 posVec = new Vec3(pos.getX() + 0.5, pos.getY() + 0.25, pos.getZ() + 0.5);
				if (openingTick >= OPENING_STEPS) {
					spawnParticles(world, pos, presentBlockEntity.getColors());
					presentBlockEntity.triggerAdvancement();
					if (presentBlockEntity.isEmpty()) {
						world.playSound(null, posVec.x, posVec.y, posVec.z, SoundEvents.FIRE_EXTINGUISH, SoundSource.BLOCKS, 0.5F, 0.8F);
						PlayParticleWithExactVelocityPayload.playParticleWithExactVelocity(world, posVec, ParticleTypes.SMOKE, 5, Vec3.ZERO);
					} else {
						world.playSound(null, posVec.x, posVec.y, posVec.z, SoundEvents.GENERIC_EXPLODE, SoundSource.BLOCKS, 0.5F, 4.0F);
						PlayParticleWithExactVelocityPayload.playParticleWithExactVelocity(world, posVec, ParticleTypes.EXPLOSION, 1, Vec3.ZERO);
						for (ItemStack stack : presentBlockEntity.getStacks()) {
							@Nullable PresentUnpackBehavior behavior = getBehaviorFor(stack);
							if (behavior != null) {
								stack = behavior.onPresentUnpack(stack, presentBlockEntity, world, pos, random);
								if (!stack.isEmpty()) {
									continue;
								}
							}
							Containers.dropItemStack(world, pos.getX(), pos.getY(), pos.getZ(), stack);
						}
					}
					world.setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState());
				} else {
					world.playSound(null, posVec.x, posVec.y, posVec.z, SoundEvents.SAND_PLACE, SoundSource.BLOCKS, 0.8F + openingTick * 0.1F, 1.0F);
					spawnParticles(world, pos, presentBlockEntity.getColors());
				}
			}
			world.scheduleTick(pos, state.getBlock(), TICKS_PER_OPENING_STEP);
		}
	}
	
	public static void spawnParticles(ServerLevel world, BlockPos pos, Map<Integer, Integer> colors) {
		PlayPresentOpeningParticlesPayload.playPresentOpeningParticles(world, pos, colors);
	}
	
	public static void spawnParticles(ClientLevel world, BlockPos pos, Map<Integer, Integer> colors) {
		if (colors.isEmpty()) {
			int randomColor = DyeColor.byId(world.random.nextInt(DyeColor.values().length)).getTextureDiffuseColor();
			spawnParticles(world, pos, randomColor, 15);
		} else {
			for (Map.Entry<Integer, Integer> color : colors.entrySet()) {
				spawnParticles(world, pos, color.getKey(), color.getValue() * 10);
			}
		}
	}
	
	private static void spawnParticles(ClientLevel world, BlockPos pos, int color, int amount) {
		double posX = pos.getX() + 0.5;
		double posY = pos.getY() + 0.25;
		double posZ = pos.getZ() + 0.5;
		RandomSource random = world.random;
		Vector3f colorVec = SpectrumColorHelper.colorIntToVec(color);
		for (int i = 0; i < amount; i++) {
			double randX = 0.35 - random.nextFloat() * 0.7;
			double randY = random.nextFloat() * 0.7;
			double randZ = 0.35 - random.nextFloat() * 0.7;
			float randomScale = 0.5F + random.nextFloat();
			int randomLifetime = 20 + random.nextInt(20);
			
			ParticleOptions particleEffect = new DynamicParticleEffect(0.98F, colorVec, randomScale, randomLifetime, true, false);
			world.addParticle(particleEffect, posX, posY, posZ, randX, randY, randZ);
		}
	}
	
	@Nullable
	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new PresentBlockEntity(pos, state);
	}
	
	@Override
	public RenderShape getRenderShape(BlockState state) {
		return RenderShape.MODEL;
	}
	
	@Override
	public boolean isPathfindable(BlockState state, PathComputationType type) {
		return false;
	}
	
}
