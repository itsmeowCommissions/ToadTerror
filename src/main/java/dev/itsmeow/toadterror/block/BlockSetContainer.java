package dev.itsmeow.toadterror.block;

import dev.itsmeow.toadterror.ToadTerror;
import dev.itsmeow.toadterror.item.ModBlockItem;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;

public class BlockSetContainer {

    protected final ModBlock brick;
    protected final ModStairs stairs;
    protected final ModSlab slab;
    protected final ModWall wall;
    protected final BlockItem brick_item;
    protected final BlockItem stairs_item;
    protected final BlockItem slab_item;
    protected final BlockItem wall_item;

    public BlockSetContainer(String baseName, Block.Properties baseProps) {
        this.brick = new ModBlock(baseName + "s", baseProps);
        this.stairs = new ModStairs(baseName + "_stairs", baseProps, brick::getDefaultState);
        this.slab = new ModSlab(baseName + "_slab", baseProps);
        this.wall = new ModWall(baseName + "_wall", baseProps);
        Item.Properties prop = new Item.Properties().group(ToadTerror.ITEM_GROUP);
        this.brick_item = new ModBlockItem(brick, prop);
        this.stairs_item = new ModBlockItem(stairs, prop);
        this.slab_item = new ModBlockItem(slab, prop);
        this.wall_item = new ModBlockItem(wall, prop);
    }

    public Block[] getAll() {
        return new Block[] { brick, stairs, slab, wall };
    }

    public BlockItem[] getItemBlocks() {
        return new BlockItem[] { brick_item, stairs_item, slab_item, wall_item };
    }

    public ModBlock getBricks() {
        return brick;
    }

    public ModStairs getStairs() {
        return stairs;
    }

    public ModSlab getSlab() {
        return slab;
    }

    public ModWall getWall() {
        return wall;
    }

    public BlockItem getBricksItem() {
        return brick_item;
    }

    public BlockItem getStairsItem() {
        return stairs_item;
    }

    public BlockItem getSlabItem() {
        return slab_item;
    }

    public BlockItem getWallItem() {
        return wall_item;
    }
}
