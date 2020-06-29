package dev.itsmeow.toadterror.item;

import java.util.function.Function;

import dev.itsmeow.toadterror.ToadTerror;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

public class ModBlockItem extends BlockItem {

    public ModBlockItem(Block block) {
        this(block, new Properties().group(ToadTerror.ITEM_GROUP));
    }

    public ModBlockItem(Block block, Function<Properties, Properties> props) {
        this(block, props.apply(new Properties().group(ToadTerror.ITEM_GROUP)));
    }

    public ModBlockItem(Block block, Properties props) {
        super(block, props);
        this.setRegistryName(ToadTerror.MODID, block.getRegistryName().getPath());
    }

    @Override
    public void fillItemGroup(ItemGroup group, NonNullList<ItemStack> items) {
        super.fillItemGroup(group, items);
        if(group == ToadTerror.ITEM_GROUP && !this.isInGroup(group)) {
            items.add(new ItemStack(this));
        }
    }
}
