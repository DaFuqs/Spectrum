package de.dafuqs.spectrum.sound.music;

import com.google.gson.JsonParser;
import de.dafuqs.spectrum.SpectrumCommon;
import de.dafuqs.spectrum.registries.SpectrumSoundEvents;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.sound.SoundInstance;
import net.minecraft.resource.*;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.Pair;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.UncheckedIOException;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Environment(EnvType.CLIENT)
public class SpectrumAudioManager {

    public static final Identifier IDENTIFIER = SpectrumCommon.locate("dynamic_audio_resources");
    public static final ResourceFinder OGG_LOCATOR = new ResourceFinder("dynamic_audio", ".ogg");
    public static final ResourceFinder METADATA_LOCATOR = ResourceFinder.json("dynamic_audio");
    public static final Map<SoundEvent, Predicate<MinecraftClient>> REGISTERED_EVENTS = new HashMap<>();
    public static final Map<Identifier, CachedAudioStream> AUDIO_STREAMS = new HashMap<>();
    public static final Map<Category, List<Pair<Predicate<MinecraftClient>, Metadata>>> TICKING_EVENTS = new HashMap<>();
    private static final SpectrumAudioManager manager;
    private final MinecraftClient client;
    @NotNull
    private Optional<ControlledAudioInstance> dominantMusic = Optional.empty();
    @NotNull
    private Optional<ControlledAudioInstance> nextMusic = Optional.empty();


    private SpectrumAudioManager(MinecraftClient client) {
        this.client = client;
    }

    public void tick() {
        if (client.isPaused())
            return;

        var player = client.getCameraEntity();

        if (player == null)
            return;

        var world = player.getWorld();

        if (world == null)
            return;

        if (nextMusic.isPresent()) {
            if(dominantMusic.isPresent()) {
                var dom = dominantMusic.get();
                if (dom.isDoneYielding()) {
                    dom.stop();
                    bump();
                }
            }
            else {
                bump();
            }
        }

        var potentialAudio = Arrays.stream(Category.values())
                .filter(category -> category.canTick(world.getTime()))
                .flatMap(category -> TICKING_EVENTS.get(category).stream())
                .filter(pair -> pair.getRight().chance() > world.random.nextFloat())
                .filter(pair -> pair.getLeft().test(client))
                .map(Pair::getRight)
                .sorted(Comparator.reverseOrder())
                .toList();

        boolean musicAccepted = false;
        for (Metadata metadata : potentialAudio) {
            if (metadata.isMusic() && !musicAccepted) {

            }
        }
    }

    public void start() {
        var sound = new ControlledAudioInstance(SpectrumSoundEvents.TEST_MUSIC, SoundCategory.MUSIC, SoundInstance.createRandom());
        nextMusic = Optional.of(sound);
        client.getSoundManager().play(sound);
    }

    private void bump () {
        dominantMusic = nextMusic;
        nextMusic = Optional.empty();
    }

    public void init() {
        ResourceManagerHelper.get(ResourceType.CLIENT_RESOURCES).registerReloadListener(
                new SimpleSynchronousResourceReloadListener() {
                    @Override
                    public Identifier getFabricId() {
                        return IDENTIFIER;
                    }

                    @Override
                    public void reload(ResourceManager manager) {

                        try {
                            for (CachedAudioStream oldStream : AUDIO_STREAMS.values()) {
                                oldStream.close();
                            }
                            AUDIO_STREAMS.clear();
                            TICKING_EVENTS.clear();;

                            var oggs = OGG_LOCATOR.findResources(manager);
                            var jsons = METADATA_LOCATOR.findResources(manager);
                            for (Map.Entry<SoundEvent, Predicate<MinecraftClient>> entry: REGISTERED_EVENTS.entrySet()) {
                                var id = entry.getKey().getId();
                                var audioPair = parse(id, oggs.get(id).getInputStream(), jsons.get(id).getReader());
                                var metadata = audioPair.getRight();

                                AUDIO_STREAMS.put(id, audioPair.getLeft());
                                TICKING_EVENTS.computeIfAbsent(metadata.category, category -> new ArrayList<>())
                                        .add(new Pair<>(entry.getValue(), metadata));
                            }

                        } catch (IOException e) {
                            throw new UncheckedIOException(e);
                        }

                    }
                }
        );
    }

    private Pair<CachedAudioStream, Metadata> parse(Identifier id, InputStream rawAudio, Reader rawMetadata) throws IOException {
        var dataJson = JsonParser.parseReader(rawMetadata).getAsJsonObject();
        int duration = JsonHelper.getInt(dataJson, "length");
        int priority = JsonHelper.getInt(dataJson, "priority");
        boolean isMusic = JsonHelper.getBoolean(dataJson, "music");
        float chance = JsonHelper.getFloat(dataJson, "chance", 1);
        Category category = Category.valueOf(JsonHelper.getString(dataJson, "category"));

        var stream = new CachedAudioStream(rawAudio);
        var metadata = new Metadata(id, category, priority, isMusic, duration, stream.getSampleMultiplier(), chance);

        return new Pair<>(stream, metadata);
    }

    public void registerEvent(SoundEvent event, Predicate<MinecraftClient> predicate) {
        REGISTERED_EVENTS.put(event, predicate);
    }

    public static SpectrumAudioManager getInstance() {
        return manager;
    }

    public record Metadata(Identifier id, Category category, int priority, boolean isMusic, int durationMillis, float sampleMultiplier, float chance) implements Comparable<Metadata>{
        public int getDurationBytes() {
            return (int) (durationMillis * sampleMultiplier);
        }

        @Override
        public int compareTo(@NotNull SpectrumAudioManager.Metadata other) {
            int categoryOrder = Integer.compare(category.priority, other.category.priority);

            if (categoryOrder != 0) {
                return categoryOrder;
            }

            return Integer.compare(priority, other.priority);
        }
    }

    public enum Category {
        BOSS(4, 1),
        EXOTIC(3, 18000),
        AMBUSH(3, 200),
        STRUCTURE(2, 50),
        THREAT(1, 200),
        BIOME(0, 50),
        MISC(0, 1200);

        public final int priority;
        public final int tickFrequency;

        Category(int priority, int tickFrequency) {
            this.priority = priority;
            this.tickFrequency = tickFrequency;
        }

        public boolean shouldYieldTo(Category category) {
            return category.priority > priority;
        }

        public boolean canTick(long time) {
            return time % tickFrequency == 0;
        }
    }

    static {
        manager = new SpectrumAudioManager(MinecraftClient.getInstance());
    }
}
