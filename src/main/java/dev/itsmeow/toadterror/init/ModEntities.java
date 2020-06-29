package dev.itsmeow.toadterror.init;

import dev.itsmeow.imdlib.IMDLib;
import dev.itsmeow.imdlib.entity.EntityRegistrarHandler;
import dev.itsmeow.imdlib.entity.util.EntityTypeContainer;
import dev.itsmeow.imdlib.entity.util.EntityTypeContainer.Builder;
import dev.itsmeow.toadterror.ToadTerror;
import dev.itsmeow.toadterror.entity.ToadProtectorEntity;
import dev.itsmeow.toadterror.entity.ToadSentinelEntity;

public class ModEntities {

    public static final EntityRegistrarHandler H = IMDLib.entityHandler(ToadTerror.MODID);
    public static final EntityTypeContainer<ToadSentinelEntity> TOAD_SENTINEL = H.add(Builder
    .create(ToadSentinelEntity.class, ToadSentinelEntity::new, "toad_sentinel", ToadTerror.MODID)
    .egg(0x435963, 0xa9bcbb)
    .size(3F, 2F));

    public static final EntityTypeContainer<ToadProtectorEntity> TOAD_PROTECTOR = H.add(Builder
    .create(ToadProtectorEntity.class, ToadProtectorEntity::new, "toad_protector", ToadTerror.MODID)
    .size(1F, 1F));

}
