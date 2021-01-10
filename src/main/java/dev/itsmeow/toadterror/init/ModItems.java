package dev.itsmeow.toadterror.init;

import com.google.common.collect.Sets;
import dev.itsmeow.toadterror.item.ModItem;
import dev.itsmeow.toadterror.item.ToadEyeItem;
import net.minecraft.item.Foods;
import net.minecraft.item.ItemStack;

import java.util.Set;

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
    private static final Set<ModItem> ITEMS = Sets.newHashSet(ModItems.TOAD_EYE, ModItems.EMPTY_TOAD_EYE, ModItems.BROKEN_TOAD_EYE, ModItems.TOAD_CHOPS);
    public static Set<ModItem> getAll() {
        return ITEMS;
    }

}
