package dev.itsmeow.toadterror.block;

import java.util.function.Function;
import java.util.function.Supplier;

import dev.itsmeow.toadterror.ToadTerror;
import net.minecraft.block.BlockState;
import net.minecraft.block.StairsBlock;
import net.minecraft.block.material.Material;

public class ModStairs extends StairsBlock {
    public ModStairs(String name, Material mat, Supplier<BlockState> state) {
        this(name, Properties.create(mat), state);
    }

    public ModStairs(String name, Material mat, Supplier<BlockState> state, Function<Properties, Properties> props) {
        this(name, props.apply(Properties.create(mat)), state);
    }

    public ModStairs(String name, Properties props, Supplier<BlockState> state) {
        super(state, props);
        this.setRegistryName(ToadTerror.MODID, name);
    }
}
