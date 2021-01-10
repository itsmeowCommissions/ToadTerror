package dev.itsmeow.toadterror.init;

import com.google.common.collect.ImmutableMap;
import dev.itsmeow.toadterror.ToadTerror;
import dev.itsmeow.toadterror.gen.SacredRuinsStructure;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.WorldGenRegistries;
import net.minecraft.world.World;
import net.minecraft.world.gen.FlatChunkGenerator;
import net.minecraft.world.gen.FlatGenerationSettings;
import net.minecraft.world.gen.feature.IFeatureConfig;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.StructureFeature;
import net.minecraft.world.gen.feature.structure.IStructurePieceType;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.settings.DimensionStructuresSettings;
import net.minecraft.world.gen.settings.StructureSeparationSettings;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

@Mod.EventBusSubscriber(modid = ToadTerror.MODID)
public class ModWorldGen {

    private static final DeferredRegister<Structure<?>> STRUCTURES = DeferredRegister.create(ForgeRegistries.STRUCTURE_FEATURES, ToadTerror.MODID);

    public static IStructurePieceType RUINS_PIECE_TYPE = null;
    public static RegistryObject<Structure<NoFeatureConfig>> SACRED_RUINS_STRUCTURE = r("sacred_ruins", s -> new SacredRuinsStructure(s, NoFeatureConfig.field_236558_a_));
    public static LazyOptional<StructureFeature<?, ?>> SACRED_RUINS_CONFIGURED_STRUCTURE = LazyOptional.of(() -> SACRED_RUINS_STRUCTURE.get().withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG));

    private static <T extends Structure<?>> RegistryObject<T> r(String name, Supplier<T> s) {
        return STRUCTURES.register(name, s);
    }

    private static <T extends Structure<?>> RegistryObject<T> r(String name, Function<String, T> s) {
        return STRUCTURES.register(name, () -> s.apply(ToadTerror.MODID + ":" + name));
    }

    public static void subscribe(IEventBus bus) {
        STRUCTURES.register(bus);
        bus.addListener(ModWorldGen::setup);
    }

    public static void setup(final FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            RUINS_PIECE_TYPE = Registry.register(Registry.STRUCTURE_PIECE, new ResourceLocation(ToadTerror.MODID, "sacred_ruins"), SacredRuinsStructure.Piece::new);
            DimensionStructuresSettings.field_236191_b_ =
                    ImmutableMap.<Structure<?>, StructureSeparationSettings>builder()
                            .putAll(DimensionStructuresSettings.field_236191_b_)
                            .put(SACRED_RUINS_STRUCTURE.get(), new StructureSeparationSettings(40, 35, 801395783))
                            .build();
            Registry.register(WorldGenRegistries.CONFIGURED_STRUCTURE_FEATURE, new ResourceLocation(ToadTerror.MODID, "sacred_ruins"), SACRED_RUINS_CONFIGURED_STRUCTURE.resolve().get());
            FlatGenerationSettings.STRUCTURES.put(SACRED_RUINS_STRUCTURE.get(), SACRED_RUINS_CONFIGURED_STRUCTURE.resolve().get());
        });

    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void biomeLoad(final BiomeLoadingEvent event) {
        if (BiomeDictionary.hasType(RegistryKey.getOrCreateKey(Registry.BIOME_KEY, event.getName()), BiomeDictionary.Type.SWAMP))
            event.getGeneration().getStructures().add(SACRED_RUINS_CONFIGURED_STRUCTURE.resolve()::get);
    }

    @SubscribeEvent
    public static void addDimensionalSpacing(final WorldEvent.Load event) {
        if (event.getWorld() instanceof ServerWorld) {
            ServerWorld serverWorld = (ServerWorld) event.getWorld();
            if (serverWorld.getChunkProvider().getChunkGenerator() instanceof FlatChunkGenerator && serverWorld.getDimensionKey().equals(World.OVERWORLD)) {
                return;
            }
            Map<Structure<?>, StructureSeparationSettings> tempMap = new HashMap<>(serverWorld.getChunkProvider().generator.func_235957_b_().func_236195_a_());
            tempMap.put(SACRED_RUINS_STRUCTURE.get(), DimensionStructuresSettings.field_236191_b_.get(SACRED_RUINS_STRUCTURE.get()));
            serverWorld.getChunkProvider().generator.func_235957_b_().field_236193_d_ = tempMap;
        }
    }
}
