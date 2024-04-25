package de.dafuqs.spectrum.deeper_down.weather;

import de.dafuqs.spectrum.registries.SpectrumBlockTags;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import org.joml.Vector3d;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.ConcurrentLinkedQueue;

public class WeatherThread {

    private final Thread thread;
    public final ConcurrentLinkedQueue<Offer> originOffers = new ConcurrentLinkedQueue<>();
    public final ConcurrentLinkedQueue<List<Vector3d>> spawnWaves = new ConcurrentLinkedQueue<>();

    public WeatherThread() {
        this.thread = new Thread(new Processor());
    }

    public void initialize() {
        thread.setDaemon(true);
        thread.setName("Deep Weather Thread");
        thread.start();
    }

    public void offer(Offer offer) {
        originOffers.offer(offer);
    }

    public Optional<List<Vector3d>> requestPrecipitationSpawn() {
        return Optional.ofNullable(spawnWaves.poll());
    }

    private class Processor implements Runnable {
        private final Random random = new Random();

        public Processor() {
        }

        @Override
        public void run() {
            while (true) {
                var offer = originOffers.poll();

                if (offer == null) {
                    try {
                        Thread.sleep(907);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                    continue;
                }

                var camera = offer.origin;
                var weatherState = offer.state;
                var attempts = weatherState.getPrecipitationQuantity();

                if (!weatherState.hasAirParticles())
                    continue;

                var spawnWave = new ArrayList<Vector3d>();
                var columnOffsets = new ArrayList<Integer>(16);
                for (int i = 0; i < attempts; i++) {
                    var x = camera.x + random.nextInt(80) - 40;
                    var y = camera.y + random.nextInt(16) + 6;
                    var z = camera.z + random.nextInt(80) - 40;
                    columnOffsets.clear();


                    if (!validatePrecipitationColumn(offer, x, y, z, columnOffsets))
                        continue;

                    if (!columnOffsets.isEmpty()) {
                        columnOffsets.stream()
                                .map(height -> new Vector3d(x - 1 + random.nextDouble() * 3, height, z - 1 + random.nextDouble() * 3))
                                .forEach(spawnWave::add);
                    }
                }

                if (!spawnWave.isEmpty())
                    spawnWaves.offer(spawnWave);
            }
        }

        private boolean validatePrecipitationColumn(Offer offer, double x, double y, double z, ArrayList<Integer> columnOffsets) {
            var world = offer.world;
            var weatherState = offer.state;

            var mutable = new BlockPos.Mutable(x, y, z);
            var biome = world.getBiome(mutable);
            var precipitationChance = weatherState.getPrecipitationChance(biome.value(), biome.getKey().orElseThrow(() -> new IllegalStateException("Keyless biome")));

            if(precipitationChance <= 0 || !weatherState.hasAirParticles()) {
                return false;
            }

            int checkedHeight = 0;
            boolean roofLess = false;
            while (world.isAir(mutable)) {
                if (checkedHeight > 64) {
                    roofLess = true;
                    break;
                }

                checkForEmptySections: {
                    if (mutable.getY() % 16 == 0) {
                        var chunk = world.getChunk(mutable);
                        var index = chunk.getSectionIndex(mutable.getY());

                        if (index < 0 || index > chunk.getSectionArray().length) {
                            break checkForEmptySections;
                        }

                        var section = chunk.getSection(index);

                        if (section.isEmpty()) {
                            mutable.move(Direction.UP, 16);
                            checkedHeight += 16;

                            for (int i = 0; i < 16; i++) {
                                if (random.nextFloat() <= precipitationChance) {
                                    columnOffsets.add(mutable.getY() + random.nextInt(16));
                                }
                            }

                            continue;
                        }
                    }
                }

                if (random.nextFloat() <= precipitationChance) {
                    columnOffsets.add(mutable.getY());
                }

                mutable.move(Direction.UP);
                checkedHeight++;
            }

            return roofLess || world.getBlockState(mutable).isIn(SpectrumBlockTags.PRECIPITATION_SOURCES);
        }
    }

    public record Offer(net.minecraft.util.math.Vec3d origin, ClientWorld world, WeatherState state) {}
}
