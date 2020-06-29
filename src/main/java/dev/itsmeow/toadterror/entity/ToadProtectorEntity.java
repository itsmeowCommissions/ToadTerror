package dev.itsmeow.toadterror.entity;

import dev.itsmeow.toadterror.init.ModEntities;
import net.minecraft.entity.AgeableEntity;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.world.World;

public class ToadProtectorEntity extends TameableEntity {

    public ToadProtectorEntity(World worldIn) {
        super(ModEntities.TOAD_PROTECTOR.entityType, worldIn);
    }

    @Override
    public AgeableEntity createChild(AgeableEntity ageable) {
        return null;
    }

}
