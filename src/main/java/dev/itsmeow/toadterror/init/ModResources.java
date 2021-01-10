package dev.itsmeow.toadterror.init;

import dev.itsmeow.toadterror.ToadTerror;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.Tags.IOptionalNamedTag;

public class ModResources {

    public static final ResourceLocation TOAD_SENTINEL_LOOT_TABLE = new ResourceLocation(ToadTerror.MODID, "entities/toad_sentinel");
    public static final ResourceLocation CURIO_SLOT_ICON = new ResourceLocation(ToadTerror.MODID, "items/toad_eye_holder");

    public static class Tags {
        public static class Blocks {
            public static final IOptionalNamedTag<Block> SACRED_STONE_BRICKS = tag("sacred_stone_bricks");
            private static IOptionalNamedTag<Block> tag(String name) {
                return BlockTags.createOptional(new ResourceLocation(ToadTerror.MODID, name));
            }
        }

        public static class Items {
            public static final IOptionalNamedTag<Item> SACRED_STONE_BRICKS = tag("sacred_stone_bricks");
            private static IOptionalNamedTag<Item> tag(String name) {
                return ItemTags.createOptional(new ResourceLocation(ToadTerror.MODID, name));
            }
        }
    }

}
