package dev.itsmeow.toadterror.init;

import dev.itsmeow.toadterror.ToadTerror;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.ResourceLocation;

public class ModResources {

    public static final ResourceLocation TOAD_SENTINEL_LOOT_TABLE = new ResourceLocation(ToadTerror.MODID, "entities/toad_sentinel");
    public static final ResourceLocation CURIO_SLOT_ICON = new ResourceLocation(ToadTerror.MODID, "items/toad_eye_holder");
    public static final ResourceLocation SACRED_RUINS_STRUCTURE = new ResourceLocation(ToadTerror.MODID, "sacred_ruins");

    public static class Tags {
        public static class Blocks {
            public static final BlockTags.Wrapper SACRED_STONE_BRICKS = new BlockTags.Wrapper(new ResourceLocation(ToadTerror.MODID, "sacred_stone_bricks"));
        }

        public static class Items {
            public static final ItemTags.Wrapper SACRED_STONE_BRICKS = new ItemTags.Wrapper(new ResourceLocation(ToadTerror.MODID, "sacred_stone_bricks"));
        }
    }

}
