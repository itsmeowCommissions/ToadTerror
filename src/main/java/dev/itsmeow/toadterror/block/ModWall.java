package dev.itsmeow.toadterror.block;

import java.util.function.Function;

import dev.itsmeow.toadterror.ToadTerror;
import net.minecraft.block.WallBlock;
import net.minecraft.block.material.Material;

public class ModWall extends WallBlock {
    public ModWall(String name, Material mat) {
        this(name, Properties.create(mat));
    }

    public ModWall(String name, Material mat, Function<Properties, Properties> props) {
        this(name, props.apply(Properties.create(mat)));
    }

    public ModWall(String name, Properties props) {
        super(props);
        this.setRegistryName(ToadTerror.MODID, name);
    }
}
