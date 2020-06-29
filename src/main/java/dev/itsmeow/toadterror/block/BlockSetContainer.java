package dev.itsmeow.toadterror.block;

import dev.itsmeow.toadterror.item.ModBlockItem;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;

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
        Item.Properties bb = new Item.Properties().group(ItemGroup.BUILDING_BLOCKS);
        this.brick_item = new ModBlockItem(brick, bb);
        this.stairs_item = new ModBlockItem(stairs, bb);
        this.slab_item = new ModBlockItem(slab, bb);
        this.wall_item = new ModBlockItem(wall, bb);
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
