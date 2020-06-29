package dev.itsmeow.toadterror.entity;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.UUID;

import dev.itsmeow.toadterror.ToadTerror;
import dev.itsmeow.toadterror.init.ModEntities;
import dev.itsmeow.toadterror.init.ModItems;
import dev.itsmeow.toadterror.init.ModResources;
import dev.itsmeow.toadterror.init.ModSoundEvents;
import net.minecraft.entity.AgeableEntity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.goal.FollowOwnerGoal;
import net.minecraft.entity.ai.goal.LeapAtTargetGoal;
import net.minecraft.entity.ai.goal.LookAtGoal;
import net.minecraft.entity.ai.goal.LookRandomlyGoal;
import net.minecraft.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.entity.ai.goal.OwnerHurtByTargetGoal;
import net.minecraft.entity.ai.goal.OwnerHurtTargetGoal;
import net.minecraft.entity.ai.goal.SwimGoal;
import net.minecraft.entity.ai.goal.WaterAvoidingRandomWalkingGoal;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.ListNBT;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.LazyOptional;
import top.theillusivec4.curios.api.CuriosAPI;
import top.theillusivec4.curios.api.capability.ICurioItemHandler;
import top.theillusivec4.curios.api.inventory.CurioStackHandler;

public class ToadProtectorEntity extends TameableEntity {

    public ToadProtectorEntity(World worldIn) {
        super(ModEntities.TOAD_PROTECTOR.entityType, worldIn);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new SwimGoal(this));
        this.goalSelector.addGoal(2, new LeapAtTargetGoal(this, 0.4F));
        this.goalSelector.addGoal(3, new MeleeAttackGoal(this, 1.0D, true));
        this.goalSelector.addGoal(4, new FollowOwnerGoal(this, 1.0D, 10.0F, 2.0F, true));
        this.goalSelector.addGoal(5, new WaterAvoidingRandomWalkingGoal(this, 1.0D));
        this.goalSelector.addGoal(6, new LookAtGoal(this, PlayerEntity.class, 8.0F));
        this.goalSelector.addGoal(6, new LookRandomlyGoal(this));
        this.targetSelector.addGoal(1, new OwnerHurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new OwnerHurtTargetGoal(this));
    }

    @Override
    protected void registerAttributes() {
        super.registerAttributes();
        this.getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(30D);
        this.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.4D);
        this.getAttributes().registerAttribute(SharedMonsterAttributes.ATTACK_DAMAGE);
        this.getAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(2D);
    }


    @Override
    protected SoundEvent getAmbientSound() {
        return ModSoundEvents.TOAD_PROTECTOR_IDLE;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return ModSoundEvents.TOAD_PROTECTOR_DEATH;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return ModSoundEvents.TOAD_PROTECTOR_HURT;
    }

    @Override
    public boolean processInteract(PlayerEntity player, Hand hand) {
        if(ModResources.Tags.Items.SACRED_STONE_BRICKS.contains(player.getHeldItem(hand).getItem())) {
            this.consumeItemFromStack(player, player.getHeldItem(hand));
            this.setHealth(this.getMaxHealth());
            for(int i = 0; i < 10; i++) {
                this.world.addParticle(ParticleTypes.HAPPY_VILLAGER, this.getPosX() + Math.random() * 2F - 1F, this.getPosY() + 1F, this.getPosZ() + Math.random() * 2F - 1F, 0, 0, 0);
            }
            return true;
        }
        return super.processInteract(player, hand);
    }

    @Override
    public AgeableEntity createChild(AgeableEntity ageable) {
        return null;
    }

    @Override
    public void onDeath(DamageSource cause) {
        CompoundNBT data = this.serializeNBT();
        UUID ownerID = this.getOwnerId();
        if(this.getServer() != null) {
            ServerPlayerEntity owner = this.getServer().getPlayerList().getPlayerByUUID(ownerID);
            if(owner != null) {
                LazyOptional<ICurioItemHandler> handler = CuriosAPI.getCuriosHandler(owner);
                handler.ifPresent(h -> {
                    CurioStackHandler sH = h.getStackHandler(ToadTerror.CURIO_ID);
                    for(int slot = 0; slot < sH.getSlots(); slot++) {
                        ItemStack content = sH.getStackInSlot(slot);
                        if(content.getItem() == ModItems.EMPTY_TOAD_EYE && content.hasTag() && content.getTag().contains("uid")) {
                            UUID uid = UUID.fromString(content.getTag().getString("uid"));
                            if(uid.equals(this.getUniqueID())) {
                                // MATCH!
                                ItemStack newStack = new ItemStack(ModItems.BROKEN_TOAD_EYE);
                                data.putString("uid", uid.toString());
                                newStack.setTag(data);
                                sH.setStackInSlot(slot, newStack);
                                super.onDeath(cause);
                                return;
                            }
                        }
                    }
                });
            } else {
                // offline... work time!
                try {
                    File playerFile = new File(this.getServer().getWorld(DimensionType.OVERWORLD).getSaveHandler().getPlayerFolder(), ownerID.toString() + ".dat");
                    if(playerFile.exists() && playerFile.isFile()) {
                        FileInputStream in = new FileInputStream(playerFile);
                        CompoundNBT tag = CompressedStreamTools.readCompressed(in);
                        in.close();
                        if(tag != null) {
                            if(tag.contains("ForgeCaps", Constants.NBT.TAG_COMPOUND)) {
                                CompoundNBT forgeCaps = tag.getCompound("ForgeCaps");
                                if(forgeCaps.contains("curios:inventory", Constants.NBT.TAG_COMPOUND)) {
                                    CompoundNBT curiosInv = forgeCaps.getCompound("curios:inventory");
                                    if(curiosInv.contains("Curios", Constants.NBT.TAG_LIST)) {
                                        ListNBT listIden = curiosInv.getList("Curios", Constants.NBT.TAG_COMPOUND);
                                        for(int j = 0; j < listIden.size(); j++) {
                                            CompoundNBT iden = listIden.getCompound(j);
                                            if(iden.contains("Identifier") && iden.getString("Identifier").equals(ToadTerror.CURIO_ID)) {
                                                ListNBT itemList = iden.getList("Items", Constants.NBT.TAG_COMPOUND);
                                                for(int i = 0; i < itemList.size(); i++) {
                                                    CompoundNBT itemTag = itemList.getCompound(i);
                                                    if(itemTag.contains("id") && itemTag.getString("id").equals(ModItems.EMPTY_TOAD_EYE.getRegistryName().toString())) {
                                                        if(itemTag.contains("tag") && itemTag.getCompound("tag").contains("uid")) {
                                                            if(itemTag.getCompound("tag").getString("uid").equals(this.getUniqueID().toString())) {
                                                                // MATCH!
                                                                ItemStack newStack = new ItemStack(ModItems.BROKEN_TOAD_EYE);
                                                                data.putString("uid", this.getUniqueID().toString());
                                                                newStack.setTag(data);
                                                                CompoundNBT newDat = newStack.serializeNBT();
                                                                newDat.putInt("Slot", itemTag.getInt("Slot"));
                                                                itemList.set(i, newDat);
                                                                iden.put("Items", itemList);
                                                                listIden.set(j, iden);
                                                                curiosInv.put("Curios", listIden);
                                                                forgeCaps.put("curios:inventory", curiosInv);
                                                                tag.put("ForgeCaps", forgeCaps);
                                                                FileOutputStream out = new FileOutputStream(playerFile);
                                                                CompressedStreamTools.writeCompressed(tag, out);
                                                                out.close();
                                                                super.onDeath(cause);
                                                                return;
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                } catch(Exception e) {
                    System.out.println("Error reading offline player data for Toad Terror - Player: " + ownerID.toString());
                    e.printStackTrace();
                    super.onDeath(cause);
                    return;
                }
            }
        }
        super.onDeath(cause);
    }

}
