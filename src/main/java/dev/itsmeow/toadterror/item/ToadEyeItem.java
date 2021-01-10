package dev.itsmeow.toadterror.item;

import java.util.List;

import dev.itsmeow.toadterror.ToadTerror;
import dev.itsmeow.toadterror.init.ModItems;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.util.Constants;

public class ToadEyeItem extends ModItem {

    public ToadEyeItem(String name, boolean hidden) {
        super(name, new Item.Properties().maxStackSize(1).group(hidden ? null : ToadTerror.ITEM_GROUP));
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void addInformation(ItemStack stack, World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        if(stack.hasTag()) {
            if(stack.getTag().contains("uid")) {
                if(stack.getTag().contains("noLocate") && stack.getItem() == ModItems.EMPTY_TOAD_EYE) {
                    tooltip.add(new TranslationTextComponent("tooltip.toadterror.nolocate").mergeStyle(TextFormatting.RED));
                } else if(stack.getItem() == ModItems.TOAD_EYE) {
                    tooltip.add(new TranslationTextComponent("tooltip.toadterror.located").mergeStyle(TextFormatting.GREEN));
                    if(stack.getTag().contains("Health", Constants.NBT.TAG_FLOAT)) {
                        tooltip.add(
                        new TranslationTextComponent("tooltip.toadterror.health",
                        new StringTextComponent("" + stack.getTag().getFloat("Health")).mergeStyle(TextFormatting.GRAY))
                        .mergeStyle(TextFormatting.GREEN));
                    }
                }
                if(flagIn == ITooltipFlag.TooltipFlags.ADVANCED) {
                    tooltip.add(new StringTextComponent("UUID: " + stack.getTag().getString("uid")).mergeStyle(TextFormatting.DARK_GRAY));
                    if(Screen.hasAltDown()) {
                        tooltip.add(new StringTextComponent(stack.getTag().toString()).mergeStyle(TextFormatting.GRAY));
                    }
                }
            }
        }
    }

    @Override
    public void fillItemGroup(ItemGroup group, NonNullList<ItemStack> items) {
        if(this.isInGroup(group)) {
            items.add(new ItemStack(this));
        }
    }
}
