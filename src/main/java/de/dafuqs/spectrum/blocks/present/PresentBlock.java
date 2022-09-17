package de.dafuqs.spectrum.blocks.present;

import de.dafuqs.spectrum.SpectrumCommon;
import de.dafuqs.spectrum.helpers.ColorHelper;
import de.dafuqs.spectrum.networking.SpectrumS2CPacketSender;
import de.dafuqs.spectrum.particle.effect.ParticleSpawnerParticleEffect;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.pathing.NavigationType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.*;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3f;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.Random;

public class PresentBlock extends BlockWithEntity {
	
	public static final int TICKS_PER_OPENING_STEP = 20;
	public static final int OPENING_STEPS = 6;
	private static final Identifier PARTICLE_SPRITE_IDENTIFIER = SpectrumCommon.locate("particle/shooting_star");
	
	public static final BooleanProperty OPENING = BooleanProperty.of("opening");
	protected static final VoxelShape SHAPE = Block.createCuboidShape(2.0D, 0.0D, 2.0D, 14.0D, 10.0D, 14.0D);
	
	public PresentBlock(Settings settings) {
		super(settings);
		this.setDefaultState(this.stateManager.getDefaultState().with(OPENING, false));
	}
	
	@Override
	protected void appendProperties(StateManager.@NotNull Builder<Block, BlockState> builder) {
		builder.add(OPENING);
	}
	
	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		return SHAPE;
	}
	
	@Override
	public boolean canPlaceAt(@NotNull BlockState state, WorldView world, BlockPos pos) {
		BlockState downState = world.getBlockState(pos.down());
		return downState.isSideSolidFullSquare(world, pos, Direction.UP);
	}
	
	@Override
	public void onPlaced(@NotNull World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
		BlockEntity blockEntity = world.getBlockEntity(pos);
		if (blockEntity instanceof PresentBlockEntity presentBlockEntity) {
			presentBlockEntity.setDataFromPresentStack(itemStack);
		}
	}
	
	@Override
	public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
		if (!player.getAbilities().allowModifyWorld) {
			return ActionResult.PASS;
		} else {
			if (world.isClient) {
				return ActionResult.SUCCESS;
			} else {
				if (player.isSneaking()) {
					state = state.with(OPENING, true);
					world.setBlockState(pos, state, 3);
					world.createAndScheduleBlockTick(pos, state.getBlock(), TICKS_PER_OPENING_STEP);
				} else {
					BlockEntity blockEntity = world.getBlockEntity(pos);
					if (blockEntity instanceof PresentBlockEntity presentBlockEntity) {
						if (presentBlockEntity.wrapper.isPresent()) {
							player.sendMessage(new TranslatableText("block.spectrum.present.tooltip.wrapped_placed.giver", presentBlockEntity.wrapper.get()), false);
						} else {
							player.sendMessage(new TranslatableText("block.spectrum.present.tooltip.wrapped_placed"), false);
						}
					}
				}
				return ActionResult.CONSUME;
			}
		}
		
	}
	
	@Override
	public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
		if (state.get(OPENING)) {
			if (!world.isClient) {
				BlockEntity blockEntity = world.getBlockEntity(pos);
				if (blockEntity instanceof PresentBlockEntity presentBlockEntity) {
					int openingTick = presentBlockEntity.openingTick();
					if (openingTick == OPENING_STEPS) {
						Vec3d posVec = new Vec3d(pos.getX() + 0.5, pos.getY() + 0.25, pos.getZ() + 0.5);
						world.playSound(null, posVec.x, posVec.y, posVec.z, SoundEvents.BLOCK_SAND_PLACE, SoundCategory.BLOCKS, 0.7F + openingTick * 0.1F, 1.0F);
						spawnParticles(world, pos, presentBlockEntity.colors);
						if(presentBlockEntity.stacks.isEmpty()) {
							SpectrumS2CPacketSender.playParticleWithExactOffsetAndVelocity(world, posVec, ParticleTypes.SMOKE, 5);
						} else {
							SpectrumS2CPacketSender.playParticleWithExactOffsetAndVelocity(world, posVec, ParticleTypes.EXPLOSION, 1);
							ItemScatterer.spawn(world, pos, presentBlockEntity.stacks);
						}
						world.setBlockState(pos, Blocks.AIR.getDefaultState());
					} else {
						spawnParticles(world, pos, presentBlockEntity.colors);
					}
				}
				world.createAndScheduleBlockTick(pos, state.getBlock(), TICKS_PER_OPENING_STEP);
			}
		}
	}
	
	public static void spawnParticles(ServerWorld world, BlockPos pos, Map<DyeColor, Integer> colors) {
		SpectrumS2CPacketSender.playPresentOpeningParticles( world, pos, colors);
	}
	
	public static void spawnParticles(ClientWorld world, BlockPos pos, Map<DyeColor, Integer> colors) {
		if(colors.isEmpty()) {
			DyeColor randomColor = DyeColor.byId(world.random.nextInt(DyeColor.values().length));
			spawnParticles(world, pos, randomColor, 15);
		} else {
			for (Map.Entry<DyeColor, Integer> color : colors.entrySet()) {
				spawnParticles(world, pos, color.getKey(), color.getValue() * 10);
			}
		}
	}
	
	private static void spawnParticles(ClientWorld world, BlockPos pos, DyeColor color, int amount) {
		double posX = pos.getX() + 0.5;
		double posY = pos.getY() + 0.25;
		double posZ = pos.getZ() + 0.5;
		Random random = world.random;
		Vec3f colorVec = ColorHelper.getVec(color);
		for (int i = 0; i < amount; i++) {
			double randX = 0.35 - random.nextFloat() * 0.7;
			double randY = random.nextFloat() * 0.7;
			double randZ = 0.35 - random.nextFloat() * 0.7;
			float randomScale = 0.5F + random.nextFloat();
			int randomLifetime = 20 + random.nextInt(20);
			
			ParticleEffect particleEffect = new ParticleSpawnerParticleEffect(PARTICLE_SPRITE_IDENTIFIER, 0.98F, colorVec, randomScale, randomLifetime, true, false);
			world.addParticle(particleEffect, posX, posY, posZ, randX, randY, randZ);
		}
	}
	
	@Nullable
	@Override
	public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
		return new PresentBlockEntity(pos, state);
	}
	
	
	@Override
	public BlockRenderType getRenderType(BlockState state) {
		return BlockRenderType.MODEL;
	}
	
	@Override
	public boolean canPathfindThrough(BlockState state, BlockView world, BlockPos pos, NavigationType type) {
		return false;
	}
	
}
