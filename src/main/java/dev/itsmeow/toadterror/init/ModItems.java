package dev.itsmeow.toadterror.init;

import dev.itsmeow.toadterror.item.ModItem;
import dev.itsmeow.toadterror.item.ToadEyeItem;
import net.minecraft.item.Foods;
import net.minecraft.item.ItemStack;

public class ModItems {

    public static final ModItem TOAD_EYE = new ToadEyeItem("toad_eye", false);
    public static final ModItem EMPTY_TOAD_EYE = new ToadEyeItem("empty_toad_eye", true);
    public static final ModItem BROKEN_TOAD_EYE = new ToadEyeItem("broken_toad_eye", true);
    public static final ModItem TOAD_CHOPS = new ModItem("toad_chops", p -> p.food(Foods.COOKED_BEEF)) {
        @Override
        public int getUseDuration(ItemStack stack) {
            return 32;
        }
    };

}
