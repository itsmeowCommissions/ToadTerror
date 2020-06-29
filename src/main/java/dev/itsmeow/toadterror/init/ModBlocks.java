package dev.itsmeow.toadterror.init;

import dev.itsmeow.toadterror.block.BlockSetContainer;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;

public class ModBlocks {

    private static final Block.Properties BRICK_PROPS = Block.Properties.create(Material.ROCK).hardnessAndResistance(1.5F, 6.0F);
    public static final BlockSetContainer SACRED_STONE_BRICK = new BlockSetContainer("sacred_stone_brick", BRICK_PROPS);
    public static final BlockSetContainer CRACKED_SACRED_STONE_BRICK = new BlockSetContainer("cracked_sacred_stone_brick", BRICK_PROPS);
    public static final BlockSetContainer MOSSY_SACRED_STONE_BRICK = new BlockSetContainer("mossy_sacred_stone_brick", BRICK_PROPS);
    public static final BlockSetContainer CHISELED_SACRED_STONE_BRICK = new BlockSetContainer("chiseled_sacred_stone_brick", BRICK_PROPS);

}
