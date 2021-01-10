package dev.itsmeow.toadterror.init;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import dev.itsmeow.toadterror.ToadTerror;
import net.minecraft.block.Block;
import net.minecraft.data.BlockTagsProvider;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.ItemTagsProvider;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.GatherDataEvent;

import java.util.List;

@Mod.EventBusSubscriber(modid = ToadTerror.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModDataGen {

    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        ToadTerrorBlockTagsProvider blockTags = new ToadTerrorBlockTagsProvider(event.getGenerator(), event.getExistingFileHelper());
        event.getGenerator().addProvider(blockTags);
        event.getGenerator().addProvider(new ToadTerrorItemTagsProvider(event.getGenerator(), blockTags, event.getExistingFileHelper()));
        event.getGenerator().addProvider(new ToadTerrorBlockStateProvider(event.getGenerator(), event.getExistingFileHelper()));
        event.getGenerator().addProvider(new ToadTerrorItemModelProvider(event.getGenerator(), event.getExistingFileHelper()));
        //event.getGenerator().addProvider(new ToadTerrorLootTableProvider(event.getGenerator()));
        //event.getGenerator().addProvider(new ToadTerrorRecipeProvider(event.getGenerator()));
        ModEntities.H.gatherData(event.getGenerator(), event.getExistingFileHelper());
    }

    public static class ToadTerrorBlockTagsProvider extends BlockTagsProvider {

        public ToadTerrorBlockTagsProvider(DataGenerator generator, ExistingFileHelper existingFileHelper) {
            super(generator, ToadTerror.MODID, existingFileHelper);
        }

        @Override
        public String getName() {
            return ToadTerror.MODID + "_block_tags";
        }

        @Override
        protected void registerTags() {
            Builder<Block> sacredBricks = this.getOrCreateBuilder(ModResources.Tags.Blocks.SACRED_STONE_BRICKS);
            Builder<Block> slabs = this.getOrCreateBuilder(BlockTags.SLABS);
            Builder<Block> stairs = this.getOrCreateBuilder(BlockTags.STAIRS);
            Builder<Block> stone_bricks = this.getOrCreateBuilder(BlockTags.STONE_BRICKS);
            Builder<Block> walls = this.getOrCreateBuilder(BlockTags.WALLS);
            ModBlocks.getAll().forEach(c -> {
                sacredBricks.add(c.getAll());
                slabs.add(c.getSlab());
                stairs.add(c.getStairs());
                stone_bricks.add(c.getBricks());
                walls.add(c.getWall());
            });
        }
    }

    public static class ToadTerrorItemTagsProvider extends ItemTagsProvider {

        public ToadTerrorItemTagsProvider(DataGenerator generator, ToadTerrorBlockTagsProvider blockTagProvider, ExistingFileHelper existingFileHelper) {
            super(generator, blockTagProvider, ToadTerror.MODID, existingFileHelper);
        }

        @Override
        public String getName() {
            return ToadTerror.MODID + "_item_tags";
        }

        @Override
        protected void registerTags() {
            this.copy(ModResources.Tags.Blocks.SACRED_STONE_BRICKS, ModResources.Tags.Items.SACRED_STONE_BRICKS);
            Builder<Item> slabs = this.getOrCreateBuilder(ItemTags.SLABS);
            Builder<Item> stairs = this.getOrCreateBuilder(ItemTags.STAIRS);
            Builder<Item> stone_bricks = this.getOrCreateBuilder(ItemTags.STONE_BRICKS);
            Builder<Item> walls = this.getOrCreateBuilder(ItemTags.WALLS);
            ModBlocks.getAll().forEach(c -> {
                slabs.add(c.getSlabItem());
                stairs.add(c.getStairsItem());
                stone_bricks.add(c.getBricksItem());
                walls.add(c.getWallItem());
            });
        }
    }

    public static class ToadTerrorItemModelProvider extends ItemModelProvider {

        public ToadTerrorItemModelProvider(DataGenerator generator, ExistingFileHelper existingFileHelper) {
            super(generator, ToadTerror.MODID, existingFileHelper);
        }

        @Override
        public String getName() {
            return ToadTerror.MODID + "_items";
        }

        @Override
        protected void registerModels() {
            ModItems.getAll().forEach(item -> {
                this.withExistingParent(item.getRegistryName().getPath(), "minecraft:item/generated").texture("layer0", ToadTerror.MODID + ":items/" + item.getRegistryName().getPath());
            });
            ModBlocks.getAll().forEach(c -> {
                this.wallInventory(c.getWallItem().getRegistryName().getPath(), c.getTexture());
                this.stairs(c.getStairsItem().getRegistryName().getPath(), c.getTexture(), c.getTexture(), c.getTexture());
                this.cubeAll(c.getBricksItem().getRegistryName().getPath(), c.getTexture());
                this.slab(c.getSlabItem().getRegistryName().getPath(), c.getTexture(), c.getTexture(), c.getTexture());
            });
        }

    }

    public static class ToadTerrorBlockStateProvider extends BlockStateProvider {
        public ToadTerrorBlockStateProvider(DataGenerator generator, ExistingFileHelper existingFileHelper) {
            super(generator, ToadTerror.MODID, existingFileHelper);
        }

        @Override
        public String getName() {
            return ToadTerror.MODID + "_blocks";
        }

        @Override
        protected void registerStatesAndModels() {
            ModBlocks.getAll().forEach(c -> {
                this.wallBlock(c.getWall(), c.getTexture());
                this.stairsBlock(c.getStairs(), c.getTexture());
                this.simpleBlock(c.getBricks());
                this.slabBlock(c.getSlab(), c.getTexture(), c.getTexture());
            });
        }
    }

}
