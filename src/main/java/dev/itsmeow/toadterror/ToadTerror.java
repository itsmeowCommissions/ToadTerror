package dev.itsmeow.toadterror;

import dev.itsmeow.imdlib.entity.util.EntityTypeContainer;
import dev.itsmeow.toadterror.entity.ToadProtectorEntity;
import dev.itsmeow.toadterror.init.*;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.GlobalEntityTypeAttributes;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.NonNullList;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AnvilUpdateEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.entity.item.ItemTossEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotTypeMessage;
import top.theillusivec4.curios.api.event.CurioChangeEvent;
import top.theillusivec4.curios.api.event.CurioDropsEvent;
import top.theillusivec4.curios.api.type.capability.ICuriosItemHandler;

import java.util.UUID;

@Mod.EventBusSubscriber(modid = ToadTerror.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
@Mod(value = ToadTerror.MODID)
public class ToadTerror {

    public static final String MODID = "toadterror";
    public static final String CURIO_ID = "toad_eye_holder";
    public static ItemGroup ITEM_GROUP;

    public ToadTerror() {
        ITEM_GROUP = new ItemGroup(MODID) {
            @Override
            public ItemStack createIcon() {
                return new ItemStack(ModItems.TOAD_EYE);
            }

            @Override
            public void fill(NonNullList<ItemStack> toDisplay) {
                super.fill(toDisplay);
                for(EntityTypeContainer<?> cont : ModEntities.H.ENTITIES.values()) {
                    if(cont.hasEgg) {
                        ItemStack stack = new ItemStack(cont.egg);
                        toDisplay.add(stack);
                    }
                }
            }
        };
        IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();
        ModWorldGen.subscribe(modBus);
    }

    @SubscribeEvent
    public static void interModEnqueue(final InterModEnqueueEvent event) {
        InterModComms.sendTo(CuriosApi.MODID, SlotTypeMessage.REGISTER_TYPE, () -> new SlotTypeMessage.Builder(CURIO_ID).size(3).icon(ModResources.CURIO_SLOT_ICON).build());
    }

    @SubscribeEvent
    public static void registerBlocks(final RegistryEvent.Register<Block> event) {
        event.getRegistry().registerAll(ModBlocks.SACRED_STONE_BRICK.getAll());
        event.getRegistry().registerAll(ModBlocks.CRACKED_SACRED_STONE_BRICK.getAll());
        event.getRegistry().registerAll(ModBlocks.MOSSY_SACRED_STONE_BRICK.getAll());
        event.getRegistry().registerAll(ModBlocks.CHISELED_SACRED_STONE_BRICK.getAll());
    }

    @SubscribeEvent
    public static void registerItems(final RegistryEvent.Register<Item> event) {
        event.getRegistry().registerAll(ModItems.TOAD_EYE, ModItems.EMPTY_TOAD_EYE, ModItems.BROKEN_TOAD_EYE, ModItems.TOAD_CHOPS);
        event.getRegistry().registerAll(ModBlocks.SACRED_STONE_BRICK.getItemBlocks());
        event.getRegistry().registerAll(ModBlocks.CRACKED_SACRED_STONE_BRICK.getItemBlocks());
        event.getRegistry().registerAll(ModBlocks.MOSSY_SACRED_STONE_BRICK.getItemBlocks());
        event.getRegistry().registerAll(ModBlocks.CHISELED_SACRED_STONE_BRICK.getItemBlocks());
        ModEntities.H.ENTITIES.values().forEach(e -> {
            if(e.hasEgg) {
                event.getRegistry().register(e.egg);
            }
        });
    }

    @SubscribeEvent
    public static void registerEntities(final RegistryEvent.Register<EntityType<?>> event) {
        ModEntities.H.ENTITIES.values().forEach(e -> {
            event.getRegistry().register(e.entityType);
            e.registerAttributes();
        });
    }

    @SubscribeEvent
    public static void registerSoundEvents(final RegistryEvent.Register<SoundEvent> event) {
        event.getRegistry().registerAll(
        ModSoundEvents.TOAD_SENTINEL_RIBBIT,
        ModSoundEvents.TOAD_SENTINEL_DEATH,
        ModSoundEvents.TOAD_SENTINEL_HURT,
        ModSoundEvents.TOAD_SENTINEL_IDLE,
        ModSoundEvents.TOAD_PROTECTOR_DEATH,
        ModSoundEvents.TOAD_PROTECTOR_HURT,
        ModSoundEvents.TOAD_PROTECTOR_IDLE);
    }

    @Mod.EventBusSubscriber(modid = ToadTerror.MODID)
    public static class EventListener {
        @SubscribeEvent
        public static void onAnvilUpdate(final AnvilUpdateEvent event) {
            ItemStack left = event.getLeft();
            ItemStack right = event.getRight();
            if(left.getItem() == ModItems.BROKEN_TOAD_EYE && left.hasTag() && left.getTag().contains("uid")) {
                if(right.getItem() == Items.EMERALD_BLOCK || right.getItem() == Items.EMERALD) {
                    int req = right.getItem() == Items.EMERALD_BLOCK ? 1 : 9;
                    if(right.getCount() >= req) {
                        event.setMaterialCost(req);
                        event.setCost(5);
                        ItemStack newStack = new ItemStack(ModItems.TOAD_EYE);
                        newStack.setTag(left.getTag());
                        event.setOutput(newStack);
                    }
                }
            }
        }

        @SubscribeEvent
        public static void onCurioSlotChanged(final CurioChangeEvent event) {
            if(event.getEntityLiving() instanceof PlayerEntity) {
                PlayerEntity player = (PlayerEntity) event.getEntityLiving();
                if(event.getIdentifier().equals(CURIO_ID)) {
                    ItemStack emptyEye = event.getFrom().copy();
                    if(event.getTo().getItem() == ModItems.TOAD_EYE) {
                        LazyOptional<ICuriosItemHandler> handler = CuriosApi.getCuriosHelper().getCuriosHandler(player);
                        handler.ifPresent(h -> {
                            h.getStacksHandler(ToadTerror.CURIO_ID).ifPresent(sH -> {
                                ItemStack toadEye = event.getTo();
                                ItemStack newEye = new ItemStack(ModItems.EMPTY_TOAD_EYE);
                                CompoundNBT newTag = new CompoundNBT();
                                if(!toadEye.hasTag() || !toadEye.getTag().contains("uid")) {
                                    newTag.putString("uid", createNewEntity(player).toString());
                                } else {
                                    newTag.putString("uid", createEntityFromEye(player, toadEye.getTag()).toString());
                                }
                                newEye.setTag(newTag);
                                sH.getStacks().setStackInSlot(event.getSlotIndex(), newEye);
                            });
                        });
                    }
                    if(event.getFrom().getItem() == ModItems.EMPTY_TOAD_EYE) {
                        if(emptyEye.hasTag() && emptyEye.getTag().contains("uid")) {
                            ItemStack mouseHeld = player.inventory.getItemStack();
                            CompoundNBT newTag = writeEntityToEye(player, UUID.fromString(emptyEye.getTag().getString("uid")));
                            ItemStack newStack = new ItemStack(newTag.contains("noLocate") ? ModItems.EMPTY_TOAD_EYE : ModItems.TOAD_EYE);
                            newStack.setTag(newTag);
                            if(!mouseHeld.isEmpty() && mouseHeld.getItem() == ModItems.EMPTY_TOAD_EYE && mouseHeld.hasTag() && mouseHeld.getTag().getString("uid").equals(emptyEye.getTag().getString("uid"))) {
                                player.inventory.setItemStack(newStack);
                            } else {
                                int slot = -1;
                                for(int i = 0; i < player.inventory.mainInventory.size(); ++i) {
                                    ItemStack temp = player.inventory.mainInventory.get(i);
                                    if(!player.inventory.mainInventory.get(i).isEmpty() && temp.getItem() == emptyEye.getItem() && ItemStack.areItemStackTagsEqual(emptyEye, temp)) {
                                        slot = i;
                                    }
                                }
                                if(slot != -1) {
                                    player.inventory.setInventorySlotContents(slot, newStack);
                                }
                            }
                        }
                    }
                    if(event.getTo().getItem() == ModItems.EMPTY_TOAD_EYE) {
                        if(event.getTo().hasTag() && event.getTo().getTag().contains("uid") && event.getTo().getTag().contains("noLocate")) {
                            if(player.world instanceof ServerWorld) {
                                ServerWorld world = (ServerWorld) player.world;
                                Entity ent = world.getEntityByUuid(UUID.fromString(event.getTo().getTag().getString("uid")));
                                LazyOptional<ICuriosItemHandler> handler = CuriosApi.getCuriosHelper().getCuriosHandler(player);
                                if(ent != null && ent instanceof ToadProtectorEntity) {
                                    handler.ifPresent(h -> {
                                        h.getStacksHandler(ToadTerror.CURIO_ID).ifPresent(sH -> {
                                            ItemStack toadEye = event.getTo();
                                            ItemStack newEye = new ItemStack(ModItems.EMPTY_TOAD_EYE);
                                            CompoundNBT newTag = new CompoundNBT();
                                            newTag.putString("uid", createEntityFromEye(player, toadEye.getTag()).toString());
                                            newEye.setTag(newTag);
                                            sH.getStacks().setStackInSlot(event.getSlotIndex(), newEye);
                                        });
                                    });
                                } else {
                                    if(!player.inventory.addItemStackToInventory(event.getTo())) {
                                        player.dropItem(event.getTo(), false);
                                    }
                                    handler.ifPresent(h -> {
                                        h.getStacksHandler(ToadTerror.CURIO_ID).ifPresent(sH -> sH.getStacks().setStackInSlot(event.getSlotIndex(), ItemStack.EMPTY));
                                    });
                                }
                            }
                        }
                    }
                }
            }
        }

        @SubscribeEvent
        public static void onCuriosDeath(CurioDropsEvent event) {
            if(event.getEntityLiving() instanceof PlayerEntity) {
                event.getDrops().forEach(e -> updateItemEntity((PlayerEntity) event.getEntityLiving(), e));
            }
        }

        @SubscribeEvent
        public static void onItemDropped(ItemTossEvent event) {
            updateItemEntity(event.getPlayer(), event.getEntityItem());
        }

        private static CompoundNBT writeEntityToEye(PlayerEntity player, UUID uuidTag) {
            CompoundNBT newTag = new CompoundNBT();
            if(player.world instanceof ServerWorld) {
                ServerWorld world = (ServerWorld) player.world;
                Entity ent = world.getEntityByUuid(uuidTag);
                if(ent != null && ent instanceof ToadProtectorEntity && ent.isAlive() && ((LivingEntity) ent).getHealth() > 0F) {
                    ToadProtectorEntity entity = (ToadProtectorEntity) ent;
                    newTag = entity.serializeNBT();
                    ent.remove();
                } else {
                    newTag.putBoolean("noLocate", true);
                }
            }
            newTag.putString("uid", uuidTag.toString());
            return newTag;
        }

        private static UUID createEntityFromEye(PlayerEntity player, CompoundNBT tag) {
            UUID id = UUID.fromString(tag.getString("uid"));
            tag.remove("uid");
            if(!tag.hasUniqueId("UUID") || !tag.getUniqueId("UUID").equals(id)) {
                return id;
            }
            ToadProtectorEntity prot = ModEntities.TOAD_PROTECTOR.entityType.create(player.world);
            prot.read(tag);
            prot.setTamed(true);
            prot.setOwnerId(player.getUniqueID());
            prot.setPositionAndRotation(player.getPosX(), player.getPosY(), player.getPosZ(), player.rotationYaw, 0);
            prot.setWorld(player.world);
            prot.setMotion(0, 0, 0);
            prot.setUniqueId(id);
            prot.fallDistance = 0F;
            prot.setFire(0);
            // stop dying
            if(prot.getHealth() <= 0F || !prot.isAlive() || prot.deathTime > 0) {
                prot.revive();
                prot.deathTime = 0;
                prot.setHealth(prot.getMaxHealth());
            }
            player.world.addEntity(prot);
            return id;
        }

        private static UUID createNewEntity(PlayerEntity player) {
            ToadProtectorEntity prot = ModEntities.TOAD_PROTECTOR.entityType.create(player.world);
            prot.setTamed(true);
            prot.setOwnerId(player.getUniqueID());
            prot.setPositionAndRotation(player.getPosX(), player.getPosY(), player.getPosZ(), player.rotationYaw, 0);
            prot.setWorld(player.world);
            prot.setMotion(0, 0, 0);
            player.world.addEntity(prot);
            return prot.getUniqueID();
        }

        private static void updateItemEntity(PlayerEntity player, ItemEntity itemE) {
            ItemStack stack = itemE.getItem();
            if(!stack.isEmpty() && stack.getItem() == ModItems.EMPTY_TOAD_EYE && stack.hasTag() && stack.getTag().contains("uid") && !stack.getTag().contains("noLocate")) {
                CompoundNBT newTag = writeEntityToEye(player, UUID.fromString(stack.getTag().getString("uid")));
                ItemStack newStack = new ItemStack(newTag.contains("noLocate") ? ModItems.EMPTY_TOAD_EYE : ModItems.TOAD_EYE);
                newStack.setTag(newTag);
                itemE.setItem(newStack);
            }
        }
    }
}
