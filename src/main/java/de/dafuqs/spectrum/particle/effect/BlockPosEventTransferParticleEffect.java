package de.dafuqs.spectrum.particle.effect;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import de.dafuqs.spectrum.particle.SpectrumParticleTypes;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.event.BlockPositionSource;

import java.util.Locale;
import java.util.function.Function;

public class BlockPosEventTransferParticleEffect implements ParticleEffect {
	
	public static final Codec<BlockPosEventTransferParticleEffect> CODEC = RecordCodecBuilder.create((instance) -> {
		return instance.group(BlockPosEventTransfer.CODEC.fieldOf("block_pos_event").forGetter((blockPosEventTransferParticleEffect) -> {
			return blockPosEventTransferParticleEffect.blockPosEvent;
		})).apply(instance, (Function) (BlockPosEventTransferParticleEffect::new));
	});
	public static final Factory<BlockPosEventTransferParticleEffect> FACTORY = new Factory<>() {
		public BlockPosEventTransferParticleEffect read(ParticleType<BlockPosEventTransferParticleEffect> particleType, StringReader stringReader) throws CommandSyntaxException {
			stringReader.expect(' ');
			float f = (float) stringReader.readDouble();
			stringReader.expect(' ');
			float g = (float) stringReader.readDouble();
			stringReader.expect(' ');
			float h = (float) stringReader.readDouble();
			stringReader.expect(' ');
			float i = (float) stringReader.readDouble();
			stringReader.expect(' ');
			float j = (float) stringReader.readDouble();
			stringReader.expect(' ');
			float k = (float) stringReader.readDouble();
			stringReader.expect(' ');
			int l = stringReader.readInt();
			BlockPos blockPos = new BlockPos(f, g, h);
			BlockPos blockPos2 = new BlockPos(i, j, k);
			return new BlockPosEventTransferParticleEffect(new BlockPosEventTransfer(blockPos, new BlockPositionSource(blockPos2), l));
		}
		
		public BlockPosEventTransferParticleEffect read(ParticleType<BlockPosEventTransferParticleEffect> particleType, PacketByteBuf packetByteBuf) {
			BlockPosEventTransfer blockPosEventTransfer = BlockPosEventTransfer.readFromBuf(packetByteBuf);
			return new BlockPosEventTransferParticleEffect(blockPosEventTransfer);
		}
	};
	
	private final BlockPosEventTransfer blockPosEvent;
	
	public BlockPosEventTransferParticleEffect(BlockPosEventTransfer blockPosEvent) {
		this.blockPosEvent = blockPosEvent;
	}
	
	public BlockPosEventTransferParticleEffect(Object blockPosEvent) {
		this((BlockPosEventTransfer) blockPosEvent);
	}
	
	public void write(PacketByteBuf buf) {
		BlockPosEventTransfer.writeToBuf(buf, this.blockPosEvent);
	}
	
	public String asString() {
		BlockPos blockPos = this.blockPosEvent.getOrigin();
		double d = blockPos.getX();
		double e = blockPos.getY();
		double f = blockPos.getZ();
		return String.format(Locale.ROOT, "%s %.2f %.2f %.2f %.2f %.2f %.2f %d", Registry.PARTICLE_TYPE.getId(this.getType()), d, e, f, d, e, f, this.blockPosEvent.getArrivalInTicks());
	}
	
	public ParticleType<BlockPosEventTransferParticleEffect> getType() {
		return SpectrumParticleTypes.BLOCK_POS_EVENT_TRANSFER;
	}
	
	public BlockPosEventTransfer getBlockPosEvent() {
		return this.blockPosEvent;
	}
}
