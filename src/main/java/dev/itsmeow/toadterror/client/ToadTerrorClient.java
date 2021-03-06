package dev.itsmeow.toadterror.client;

import dev.itsmeow.imdlib.client.IMDLibClient;
import dev.itsmeow.imdlib.client.render.RenderFactory;
import dev.itsmeow.toadterror.ToadTerror;
import dev.itsmeow.toadterror.client.model.ToadProtectorModel;
import dev.itsmeow.toadterror.client.model.ToadSentinelModel;
import dev.itsmeow.toadterror.init.ModEntities;
import dev.itsmeow.toadterror.init.ModResources;
import net.minecraft.inventory.container.PlayerContainer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = ToadTerror.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ToadTerrorClient {

    public static final RenderFactory R = IMDLibClient.getRenderRegistry(ToadTerror.MODID);

    @SubscribeEvent
    public static void clientSetup(final FMLClientSetupEvent event) {
        R.addRender(ModEntities.TOAD_SENTINEL.entityType, 1F, r -> r.tMapped(e -> {
            return e.getHealth() > e.getMaxHealth() * 0.66F ? "toad_sentinel" : (e.getHealth() > e.getMaxHealth() * 0.33F ? "toad_sentinel_cracked_1" : "toad_sentinel_cracked_2");
        }).mSingle(new ToadSentinelModel<>()));
        R.addRender(ModEntities.TOAD_PROTECTOR.entityType, 1F, r -> r.tSingle("toad_protector").mSingle(new ToadProtectorModel<>()));
    }

    @SubscribeEvent
    public static void stitchTextures(TextureStitchEvent.Pre evt) {
        if(evt.getMap().getTextureLocation() == PlayerContainer.LOCATION_BLOCKS_TEXTURE) {
            evt.addSprite(ModResources.CURIO_SLOT_ICON);
        }
    }
}
