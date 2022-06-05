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

public class ItemTransferParticleEffect implements ParticleEffect {
	
	public static final Codec<ItemTransferParticleEffect> CODEC = RecordCodecBuilder.create((instance) -> {
		return instance.group(ItemTransfer.CODEC.fieldOf("item_transfer").forGetter((itemTransferParticleEffect) -> {
			return itemTransferParticleEffect.itemTransfer;
		})).apply(instance, (Function) (ItemTransferParticleEffect::new));
	});
	public static final ParticleEffect.Factory<ItemTransferParticleEffect> FACTORY = new ParticleEffect.Factory<>() {
		public ItemTransferParticleEffect read(ParticleType<ItemTransferParticleEffect> particleType, StringReader stringReader) throws CommandSyntaxException {
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
			return new ItemTransferParticleEffect(new ItemTransfer(blockPos, new BlockPositionSource(blockPos2), l));
		}
		
		public ItemTransferParticleEffect read(ParticleType<ItemTransferParticleEffect> particleType, PacketByteBuf packetByteBuf) {
			ItemTransfer itemTransfer = ItemTransfer.readFromBuf(packetByteBuf);
			return new ItemTransferParticleEffect(itemTransfer);
		}
	};
	
	private final ItemTransfer itemTransfer;
	
	public ItemTransferParticleEffect(ItemTransfer itemTransfer) {
		this.itemTransfer = itemTransfer;
	}
	
	public ItemTransferParticleEffect(Object itemTransfer) {
		this((ItemTransfer) itemTransfer);
	}
	
	public void write(PacketByteBuf buf) {
		ItemTransfer.writeToBuf(buf, this.itemTransfer);
	}
	
	public String asString() {
		BlockPos blockPos = this.itemTransfer.getOrigin();
		double d = blockPos.getX();
		double e = blockPos.getY();
		double f = blockPos.getZ();
		return String.format(Locale.ROOT, "%s %.2f %.2f %.2f %.2f %.2f %.2f %d", Registry.PARTICLE_TYPE.getId(this.getType()), d, e, f, d, e, f, this.itemTransfer.getArrivalInTicks());
	}
	
	public ParticleType<ItemTransferParticleEffect> getType() {
		return SpectrumParticleTypes.ITEM_TRANSFER;
	}
	
	public ItemTransfer getItemTransfer() {
		return this.itemTransfer;
	}
}
