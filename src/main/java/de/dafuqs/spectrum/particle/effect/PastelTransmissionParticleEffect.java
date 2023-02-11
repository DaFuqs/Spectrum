package de.dafuqs.spectrum.particle.effect;

import com.mojang.brigadier.*;
import com.mojang.brigadier.exceptions.*;
import com.mojang.serialization.*;
import com.mojang.serialization.codecs.*;
import de.dafuqs.spectrum.particle.*;
import net.minecraft.item.*;
import net.minecraft.network.*;
import net.minecraft.particle.*;
import net.minecraft.util.math.*;
import net.minecraft.util.registry.*;

import java.util.*;

public class PastelTransmissionParticleEffect implements ParticleEffect {

	public static final Codec<PastelTransmissionParticleEffect> CODEC = RecordCodecBuilder.create((instance) -> instance.group(
            Codec.list(BlockPos.CODEC).fieldOf("positions").forGetter((particleEffect) -> particleEffect.nodePositions),
            ItemStack.CODEC.fieldOf("stack").forGetter((effect) -> effect.stack),
            Codec.INT.fieldOf("travel_time").forGetter((particleEffect) -> particleEffect.travelTime),
            Codec.INT.fieldOf("color").forGetter((particleEffect) -> particleEffect.color)
    ).apply(instance, PastelTransmissionParticleEffect::new));

	public static final Factory<PastelTransmissionParticleEffect> FACTORY = new Factory<>() {
		public PastelTransmissionParticleEffect read(ParticleType<PastelTransmissionParticleEffect> particleType, StringReader stringReader) throws CommandSyntaxException {
            List<BlockPos> posList = new ArrayList<>();

            stringReader.expect(' ');
            int travelTime = stringReader.readInt();

            // TODO I don't care, really
            stringReader.expect(' ');
            int x1 = stringReader.readInt();
            stringReader.expect(' ');
            int y1 = stringReader.readInt();
            stringReader.expect(' ');
            int z1 = stringReader.readInt();

            stringReader.expect(' ');
            int x2 = stringReader.readInt();
            stringReader.expect(' ');
            int y2 = stringReader.readInt();
            stringReader.expect(' ');
            int z2 = stringReader.readInt();

            stringReader.expect(' ');
            int color = stringReader.readInt();

            BlockPos sourcePos = new BlockPos(x1, y1, z1);
            BlockPos destinationPos = new BlockPos(x2, y2, z2);
            posList.add(sourcePos);
            posList.add(destinationPos);
            return new PastelTransmissionParticleEffect(posList, Items.STONE.getDefaultStack(), travelTime, color);
        }

		public PastelTransmissionParticleEffect read(ParticleType<PastelTransmissionParticleEffect> particleType, PacketByteBuf buf) {
            int posCount = buf.readInt();
            List<BlockPos> posList = new ArrayList<>();
            for (int i = 0; i < posCount; i++) {
                posList.add(buf.readBlockPos());
            }
            ItemStack stack = buf.readItemStack();
            int travelTime = buf.readInt();
            int color = buf.readInt();
            return new PastelTransmissionParticleEffect(posList, stack, travelTime, color);
        }
    };

    private final List<BlockPos> nodePositions;
    private final ItemStack stack;
    private final int travelTime;
    private final int color;

    public PastelTransmissionParticleEffect(List<BlockPos> nodePositions, ItemStack stack, int travelTime, int color) {
        this.nodePositions = nodePositions;
        this.stack = stack;
        this.travelTime = travelTime;
        this.color = color;
    }

    public ParticleType getType() {
        return SpectrumParticleTypes.PASTEL_TRANSMISSION;
    }

    @Override
    public String asString() {
        int nodeCount = this.nodePositions.size();
        BlockPos source = this.nodePositions.get(0);
        BlockPos destination = this.nodePositions.get(this.nodePositions.size() - 1);
		int d = source.getX();
		int e = source.getY();
		int f = source.getZ();
		int g = destination.getX();
		int h = destination.getY();
		int i = destination.getZ();
        return String.format(Locale.ROOT, "%s %d %d %d %d %d %d %d %d %d %d", Registry.PARTICLE_TYPE.getId(this.getType()), this.travelTime, nodeCount, d, e, f, g, h, i, this.color);
	}

	@Override
    public void write(PacketByteBuf buf) {
        buf.writeInt(nodePositions.size());
        for (BlockPos pos : nodePositions) {
            buf.writeBlockPos(pos);
        }
        buf.writeItemStack(stack);
        buf.writeInt(travelTime);
        buf.writeInt(color);
    }

    public List<BlockPos> getNodePositions() {
        return this.nodePositions;
    }

    public ItemStack getStack() {
        return this.stack;
    }

    public int getTravelTime() {
        return this.travelTime;
    }

    public int getColor() {
        return this.color;
    }

}
