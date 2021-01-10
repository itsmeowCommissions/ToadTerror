package dev.itsmeow.toadterror.gen;

import com.mojang.serialization.Codec;
import dev.itsmeow.toadterror.ToadTerror;
import dev.itsmeow.toadterror.entity.ToadSentinelEntity;
import dev.itsmeow.toadterror.init.ModEntities;
import dev.itsmeow.toadterror.init.ModWorldGen;
import net.minecraft.block.Blocks;
import net.minecraft.entity.SpawnReason;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Mirror;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.SharedSeedRandom;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.util.registry.DynamicRegistries;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.provider.BiomeProvider;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.structure.*;
import net.minecraft.world.gen.feature.template.BlockIgnoreStructureProcessor;
import net.minecraft.world.gen.feature.template.PlacementSettings;
import net.minecraft.world.gen.feature.template.Template;
import net.minecraft.world.gen.feature.template.TemplateManager;

import java.util.List;
import java.util.Random;

public class SacredRuinsStructure extends Structure<NoFeatureConfig> {

    public SacredRuinsStructure(String s, Codec<NoFeatureConfig> codec) {
        super(codec);
        Structure.NAME_STRUCTURE_BIMAP.put(s, this);
    }

    @Override
    public GenerationStage.Decoration getDecorationStage() {
        return GenerationStage.Decoration.SURFACE_STRUCTURES;
    }

    @Override
    public IStartFactory<NoFeatureConfig> getStartFactory() {
        return SacredRuinsStructure.Start::new;
    }

    @Override
    protected boolean func_230363_a_(ChunkGenerator chunkGen, BiomeProvider biomeSource, long seed, SharedSeedRandom rand, int chunkPosX, int chunkPosZ, Biome biome, ChunkPos chunkPos, NoFeatureConfig featureConfig) {
        return chunkPosX > 4 && chunkPosZ > 4;
    }

    public static class Start extends StructureStart<NoFeatureConfig> {

        public Start(Structure<NoFeatureConfig> structureIn, int chunkX, int chunkZ, MutableBoundingBox mutableBoundingBox, int referenceIn, long seedIn) {
            super(structureIn, chunkX, chunkZ, mutableBoundingBox, referenceIn, seedIn);
        }

        @Override
        public void func_230364_a_(DynamicRegistries dynamicRegistryManager, ChunkGenerator generator, TemplateManager templateManagerIn, int chunkX, int chunkZ, Biome biome, NoFeatureConfig config) {
            Rotation rotation = Rotation.values()[this.rand.nextInt(Rotation.values().length)];
            int x = (chunkX << 4) + 7;
            int z = (chunkZ << 4) + 7;
            // offset 12 to get center (roughly), move down 2 because dirt below
            int surfaceY = Math.max(generator.getNoiseHeightMinusOne(x + 12, z + 12, Heightmap.Type.WORLD_SURFACE_WG) - 2, generator.getGroundHeight() - 3);
            BlockPos blockpos = new BlockPos(x, surfaceY, z);
            Piece.start(templateManagerIn, blockpos, rotation, this.components, this.rand);
            this.recalculateStructureSize();
        }

    }

    public static class Piece extends TemplateStructurePiece {

        private ResourceLocation resourceLocation;
        private Rotation rotation;

        public Piece(TemplateManager templateManagerIn, ResourceLocation resourceLocationIn, BlockPos pos, Rotation rotationIn) {
            super(ModWorldGen.RUINS_PIECE_TYPE, 0);
            this.resourceLocation = resourceLocationIn;
            this.templatePosition = pos;
            this.rotation = rotationIn;
            this.setupPiece(templateManagerIn);
        }

        public Piece(TemplateManager templateManagerIn, CompoundNBT tagCompound) {
            super(ModWorldGen.RUINS_PIECE_TYPE, tagCompound);
            this.resourceLocation = new ResourceLocation(tagCompound.getString("Template"));
            this.rotation = Rotation.valueOf(tagCompound.getString("Rot"));
            this.setupPiece(templateManagerIn);
        }

        public static void start(TemplateManager templateManager, BlockPos pos, Rotation rotation, List<StructurePiece> pieceList, Random random) {
            int x = pos.getX();
            int z = pos.getZ();
            BlockPos rotationOffSet = new BlockPos(0, 0, 0).rotate(rotation);
            BlockPos blockpos = rotationOffSet.add(x, pos.getY(), z);
            pieceList.add(new Piece(templateManager, new ResourceLocation(ToadTerror.MODID, "sacred_ruins"), blockpos, rotation));
        }

        private void setupPiece(TemplateManager templateManager) {
            Template template = templateManager.getTemplateDefaulted(this.resourceLocation);
            PlacementSettings placementsettings = (new PlacementSettings()).setRotation(this.rotation).setMirror(Mirror.NONE);
            this.setup(template, this.templatePosition, placementsettings);
        }

        @Override
        protected void readAdditional(CompoundNBT tagCompound) {
            super.readAdditional(tagCompound);
            tagCompound.putString("Template", this.resourceLocation.toString());
            tagCompound.putString("Rot", this.rotation.name());
        }

        @Override
        protected void handleDataMarker(String function, BlockPos pos, IServerWorld worldIn, Random rand, MutableBoundingBox sbb) {
            if("sentinel".equals(function)) {
                ToadSentinelEntity e = ModEntities.TOAD_SENTINEL.entityType.create(worldIn.getWorld());
                e.enablePersistence();
                e.moveToBlockPosAndAngles(pos, 0.0F, 0.0F);
                e.onInitialSpawn(worldIn, worldIn.getDifficultyForLocation(pos), SpawnReason.STRUCTURE, null, null);
                worldIn.addEntity(e);
                worldIn.setBlockState(pos, Blocks.AIR.getDefaultState(), 2);
            }
        }

        @Override
        public boolean func_230383_a_(ISeedReader seedReader, StructureManager structureManager, ChunkGenerator chunkGenerator, Random randomIn, MutableBoundingBox structureBoundingBoxIn, ChunkPos chunkPos, BlockPos pos) {
            PlacementSettings placementsettings = (new PlacementSettings()).setRotation(this.rotation).setMirror(Mirror.NONE).addProcessor(BlockIgnoreStructureProcessor.AIR_AND_STRUCTURE_BLOCK);
            BlockPos blockpos = BlockPos.ZERO;
            this.templatePosition.add(Template.transformedBlockPos(placementsettings, new BlockPos(-blockpos.getX(), 0, -blockpos.getZ())));
            return super.func_230383_a_(seedReader, structureManager, chunkGenerator, randomIn, structureBoundingBoxIn, chunkPos, pos);
        }
    }

}
