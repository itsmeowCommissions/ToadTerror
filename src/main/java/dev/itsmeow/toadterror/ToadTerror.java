package dev.itsmeow.toadterror;

import java.util.UUID;

import dev.itsmeow.imdlib.entity.util.EntityTypeContainer;
import dev.itsmeow.toadterror.entity.ToadProtectorEntity;
import dev.itsmeow.toadterror.gen.SacredRuinsStructure;
import dev.itsmeow.toadterror.init.ModBlocks;
import dev.itsmeow.toadterror.init.ModEntities;
import dev.itsmeow.toadterror.init.ModItems;
import dev.itsmeow.toadterror.init.ModResources;
import dev.itsmeow.toadterror.init.ModSoundEvents;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.NonNullList;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.Tuple;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.IFeatureConfig;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.structure.IStructurePieceType;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.placement.IPlacementConfig;
import net.minecraft.world.gen.placement.Placement;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeDictionary.Type;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AnvilUpdateEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.entity.item.ItemTossEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DeferredWorkQueue;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.registries.ObjectHolder;
import top.theillusivec4.curios.api.CuriosAPI;
import top.theillusivec4.curios.api.capability.ICurioItemHandler;
import top.theillusivec4.curios.api.event.LivingCurioChangeEvent;
import top.theillusivec4.curios.api.event.LivingCurioDropsEvent;
import top.theillusivec4.curios.api.imc.CurioIMCMessage;

@SuppressWarnings("deprecation")
@Mod.EventBusSubscriber(modid = ToadTerror.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
@Mod(value = ToadTerror.MODID)
public class ToadTerror {

    public static final String MODID = "toadterror";
    public static final String CURIO_ID = "toad_eye_holder";
    public static ItemGroup ITEM_GROUP;
    public static IStructurePieceType RUINS_PIECE_TYPE = null;
    @ObjectHolder(MODID + ":sacred_ruins")
    public static Structure<NoFeatureConfig> SACRED_RUINS_STRUCTURE;

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
    }

    @SubscribeEvent
    public void setup(final FMLCommonSetupEvent event) {
        DeferredWorkQueue.runLater(() -> {
            BiomeDictionary.getBiomes(Type.SWAMP).forEach((biome) -> {
                biome.addStructure(SACRED_RUINS_STRUCTURE.withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG));
                biome.addFeature(GenerationStage.Decoration.SURFACE_STRUCTURES, SACRED_RUINS_STRUCTURE.withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG).withPlacement(Placement.NOPE.configure(IPlacementConfig.NO_PLACEMENT_CONFIG)));
            });
        });
    }

    @SubscribeEvent
    public static void interModEnqueue(final InterModEnqueueEvent event) {
        InterModComms.sendTo("curios", CuriosAPI.IMC.REGISTER_TYPE, () -> new CurioIMCMessage(CURIO_ID).setSize(1).setEnabled(true).setHidden(false));
        InterModComms.sendTo("curios", CuriosAPI.IMC.REGISTER_ICON, () -> new Tuple<>(CURIO_ID, ModResources.CURIO_SLOT_ICON));
    }

    @SubscribeEvent
    public static void registerFeatures(RegistryEvent.Register<Feature<?>> event) {
        RUINS_PIECE_TYPE = Registry.register(Registry.STRUCTURE_PIECE, ModResources.SACRED_RUINS_STRUCTURE, SacredRuinsStructure.Piece::new);
        event.getRegistry().register(new SacredRuinsStructure(NoFeatureConfig::deserialize).setRegistryName(ModResources.SACRED_RUINS_STRUCTURE));
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
        ModEntities.H.ENTITIES.values().forEach(e -> event.getRegistry().register(e.entityType));
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
        public static void onCurioSlotChanged(final LivingCurioChangeEvent event) {
            if(event.getEntityLiving() instanceof PlayerEntity) {
                PlayerEntity player = (PlayerEntity) event.getEntityLiving();
                if(event.getTypeIdentifier().equals(CURIO_ID)) {
                    ItemStack emptyEye = event.getFrom().copy();
                    if(event.getTo().getItem() == ModItems.TOAD_EYE) {
                        LazyOptional<ICurioItemHandler> handler = CuriosAPI.getCuriosHandler(player);
                        handler.ifPresent(h -> {
                            ItemStack toadEye = event.getTo();
                            ItemStack newEye = new ItemStack(ModItems.EMPTY_TOAD_EYE);
                            CompoundNBT newTag = new CompoundNBT();
                            if(!toadEye.hasTag() || !toadEye.getTag().contains("uid")) {
                                newTag.putString("uid", createNewEntity(player).toString());
                            } else {
                                newTag.putString("uid", createEntityFromEye(player, toadEye.getTag()).toString());
                            }
                            newEye.setTag(newTag);
                            h.setStackInSlot(CURIO_ID, event.getSlotIndex(), newEye);
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
                                int slot = player.inventory.getSlotFor(emptyEye);
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
                                LazyOptional<ICurioItemHandler> handler = CuriosAPI.getCuriosHandler(player);
                                if(ent != null && ent instanceof ToadProtectorEntity) {
                                    handler.ifPresent(h -> {
                                        ItemStack toadEye = event.getTo();
                                        ItemStack newEye = new ItemStack(ModItems.EMPTY_TOAD_EYE);
                                        CompoundNBT newTag = new CompoundNBT();
                                        newTag.putString("uid", createEntityFromEye(player, toadEye.getTag()).toString());
                                        newEye.setTag(newTag);
                                        h.setStackInSlot(CURIO_ID, event.getSlotIndex(), newEye);
                                    });
                                } else {
                                    if(!player.inventory.addItemStackToInventory(event.getTo())) {
                                        player.dropItem(event.getTo(), false);
                                    }
                                    handler.ifPresent(h -> {
                                        h.setStackInSlot(CURIO_ID, event.getSlotIndex(), ItemStack.EMPTY);
                                    });
                                }
                            }
                        }
                    }
                }
            }
        }

        @SubscribeEvent
        public static void onCuriosDeath(LivingCurioDropsEvent event) {
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
            prot.setFireTimer(0);
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
