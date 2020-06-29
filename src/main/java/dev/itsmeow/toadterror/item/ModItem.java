package dev.itsmeow.toadterror.item;

import java.util.function.Function;

import dev.itsmeow.toadterror.ToadTerror;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

public class ModItem extends Item {

    public ModItem(String name) {
        this(name, new Properties().group(ToadTerror.ITEM_GROUP));
    }

    public ModItem(String name, Function<Properties, Properties> props) {
        this(name, props.apply(new Properties().group(ToadTerror.ITEM_GROUP)));
    }

    public ModItem(String name, Properties props) {
        super(props);
        this.setRegistryName(ToadTerror.MODID, name);
    }

    @Override
    public void fillItemGroup(ItemGroup group, NonNullList<ItemStack> items) {
        super.fillItemGroup(group, items);
        if(group == ToadTerror.ITEM_GROUP && !this.isInGroup(group)) {
            items.add(new ItemStack(this));
        }
    }
}
