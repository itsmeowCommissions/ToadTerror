package dev.itsmeow.toadterror.gen;

import java.util.Random;
import java.util.function.Function;

import com.mojang.datafixers.Dynamic;

import dev.itsmeow.toadterror.ToadTerror;
import dev.itsmeow.toadterror.init.ModResources;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Mirror;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.IWorld;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.structure.ScatteredStructure;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.feature.structure.StructureStart;
import net.minecraft.world.gen.feature.structure.TemplateStructurePiece;
import net.minecraft.world.gen.feature.template.BlockIgnoreStructureProcessor;
import net.minecraft.world.gen.feature.template.PlacementSettings;
import net.minecraft.world.gen.feature.template.Template;
import net.minecraft.world.gen.feature.template.TemplateManager;

public class SacredRuinsStructure extends ScatteredStructure<NoFeatureConfig> {

    public SacredRuinsStructure(Function<Dynamic<?>, ? extends NoFeatureConfig> configFactoryIn) {
        super(configFactoryIn);
    }

    @Override
    protected int getSeedModifier() {
        return 801395783;
    }

    @Override
    public IStartFactory getStartFactory() {
        return SacredRuinsStructure.Start::new;
    }

    @Override
    public String getStructureName() {
        return ModResources.SACRED_RUINS_STRUCTURE.toString();
    }

    @Override
    public int getSize() {
        return 1;
    }

    public static class Start extends StructureStart {

        public Start(Structure<?> structIn, int int_1, int int_2, MutableBoundingBox mutableBB, int int_3, long long_1) {
            super(structIn, int_1, int_2, mutableBB, int_3, long_1);
        }

        @Override
        public void init(ChunkGenerator<?> generator, TemplateManager templateManagerIn, int chunkX, int chunkZ, Biome biomeIn) {
            int worldX = chunkX * 16;
            int worldZ = chunkZ * 16;
            BlockPos blockpos = new BlockPos(worldX, generator.getGroundHeight(), worldZ);
            this.components.add(new Piece(templateManagerIn, ModResources.SACRED_RUINS_STRUCTURE, blockpos));
            this.recalculateStructureSize();
        }

    }

    public static class Piece extends TemplateStructurePiece {

        public Piece(TemplateManager templateMgr, CompoundNBT nbt) {
            super(ToadTerror.RUINS_PIECE_TYPE, nbt);
            this.setupTemplate(templateMgr);
        }

        public Piece(TemplateManager templateMgr, ResourceLocation resLoc, BlockPos blockPos) {
            super(ToadTerror.RUINS_PIECE_TYPE, 0);
            this.templatePosition = blockPos;
            this.setupTemplate(templateMgr);
        }

        private void setupTemplate(TemplateManager templateMgr) {
            Template template = templateMgr.getTemplateDefaulted(ModResources.SACRED_RUINS_STRUCTURE);
            PlacementSettings placementsettings = (new PlacementSettings())
            .setRotation(Rotation.NONE)
            .setMirror(Mirror.NONE)
            .setCenterOffset(BlockPos.ZERO)
            .addProcessor(BlockIgnoreStructureProcessor.STRUCTURE_BLOCK);
            this.setup(template, this.templatePosition, placementsettings);
        }

        @Override
        public boolean create(IWorld worldIn, ChunkGenerator<?> chunkGenIn, Random rand, MutableBoundingBox mutableBB, ChunkPos chunkPos) {
            PlacementSettings settings = (new PlacementSettings()).setRotation(Rotation.NONE).setMirror(Mirror.NONE).setCenterOffset(BlockPos.ZERO).addProcessor(BlockIgnoreStructureProcessor.STRUCTURE_BLOCK);
            this.templatePosition.add(Template.transformedBlockPos(settings, new BlockPos(0, 0, 0)));
            return super.create(worldIn, chunkGenIn, rand, mutableBB, chunkPos);
        }

        @Override
        protected void handleDataMarker(String function, BlockPos pos, IWorld worldIn, Random rand, MutableBoundingBox sbb) {
        }
    }

}
