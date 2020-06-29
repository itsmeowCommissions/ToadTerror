package dev.itsmeow.toadterror.client.model;

import java.util.Optional;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import com.mojang.blaze3d.vertex.MatrixApplyingVertexBuilder;

import dev.itsmeow.imdlib.client.util.RenderUtil;
import dev.itsmeow.toadterror.entity.ToadSentinelEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Vector3f;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.model.ModelBakery;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;

public class ToadSentinelModel<T extends ToadSentinelEntity> extends EntityModel<T> {
    public ModelRenderer Body;
    public ModelRenderer Tongue;
    public ModelRenderer HeadJoint;
    public ModelRenderer LegLeft;
    public ModelRenderer ArmLeft;
    public ModelRenderer LegRight;
    public ModelRenderer ArmRight;
    public ModelRenderer Tail;
    public ModelRenderer BackFin;
    public ModelRenderer JawUpper;
    public ModelRenderer JawLower;
    public ModelRenderer HeadFinLeft;
    public ModelRenderer HeadFinRight;
    public ModelRenderer EyeLeft;
    public ModelRenderer EyeRight;
    public ModelRenderer FootLeft;
    public ModelRenderer FootRight;
    public ModelRenderer TailFin;
    protected int tongueTicks = 0;
    protected Optional<BlockPos> targetPos = Optional.empty();
    protected BlockPos entityPos = null;
    protected float rot = 0;
    protected float health = 0;

    public ToadSentinelModel() {
        this.textureWidth = 320;
        this.textureHeight = 240;
        this.JawLower = new ModelRenderer(this, 2, 104);
        this.JawLower.setRotationPoint(0.0F, 3.0F, 1.5F);
        this.JawLower.addBox(-14.5F, -3.0F, -25.0F, 29, 15, 25, 0.0F);
        this.ArmRight = new ModelRenderer(this, 70, 150);
        this.ArmRight.mirror = true;
        this.ArmRight.setRotationPoint(-8.5F, 4.5F, -10.5F);
        this.ArmRight.addBox(-5.0F, -3.0F, -4.0F, 7, 22, 8, 0.0F);
        this.setRotateAngle(ArmRight, 0.17453292519943295F, 0.0F, 0.0F);
        this.Tail = new ModelRenderer(this, 198, 48);
        this.Tail.setRotationPoint(0.0F, -1.0F, 16.0F);
        this.Tail.addBox(-3.5F, -6.0F, 0.0F, 7, 12, 32, 0.0F);
        this.setRotateAngle(Tail, 0.08726646259971647F, 0.0F, 0.0F);
        this.HeadFinRight = new ModelRenderer(this, 20, 40);
        this.HeadFinRight.mirror = true;
        this.HeadFinRight.setRotationPoint(-9.0F, 1.0F, -1.0F);
        this.HeadFinRight.addBox(-16.0F, -8.5F, 0.0F, 16, 17, 0, 0.0F);
        this.setRotateAngle(HeadFinRight, 0.0F, 0.6108652381980153F, 0.08726646259971647F);
        this.FootLeft = new ModelRenderer(this, 115, 170);
        this.FootLeft.setRotationPoint(0.0F, 14.0F, 19.0F);
        this.FootLeft.addBox(-4.5F, 0.0F, -24.0F, 9, 4, 24, 0.0F);
        this.setRotateAngle(FootLeft, 0.17453292519943295F, 0.0F, 0.0F);
        this.EyeRight = new ModelRenderer(this, 71, 76);
        this.EyeRight.mirror = true;
        this.EyeRight.setRotationPoint(-7.0F, -2.5F, -10.0F);
        this.EyeRight.addBox(-6.0F, -6.0F, -3.0F, 6, 6, 6, 0.0F);
        this.setRotateAngle(EyeRight, 0.0F, -0.17453292519943295F, 0.0F);
        this.Body = new ModelRenderer(this, 105, 59);
        this.Body.setRotationPoint(0.0F, 2.0F, 0.0F);
        this.Body.addBox(-12.0F, -8.5F, -18.5F, 24, 17, 37, 0.0F);
        this.setRotateAngle(Body, -0.17453292519943295F, 0.0F, 0.0F);
        this.BackFin = new ModelRenderer(this, 138, 0);
        this.BackFin.setRotationPoint(0.0F, -7.0F, -11.0F);
        this.BackFin.addBox(0.0F, -14.0F, -4.0F, 0, 14, 24, 0.0F);
        this.JawUpper = new ModelRenderer(this, 2, 70);
        this.JawUpper.setRotationPoint(0.0F, -4.0F, 2.0F);
        this.JawUpper.addBox(-11.0F, -5.0F, -22.0F, 22, 10, 22, 0.0F);
        this.TailFin = new ModelRenderer(this, 196, -24);
        this.TailFin.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.TailFin.addBox(0.0F, -11.0F, 0.0F, 0, 22, 45, 0.0F);
        this.FootRight = new ModelRenderer(this, 115, 170);
        this.FootRight.mirror = true;
        this.FootRight.setRotationPoint(0.0F, 14.0F, 19.0F);
        this.FootRight.addBox(-4.5F, 0.0F, -24.0F, 9, 4, 24, 0.0F);
        this.setRotateAngle(FootRight, 0.17453292519943295F, 0.0F, 0.0F);
        this.ArmLeft = new ModelRenderer(this, 70, 150);
        this.ArmLeft.setRotationPoint(8.5F, 4.5F, -10.5F);
        this.ArmLeft.addBox(-2.0F, -3.0F, -4.0F, 7, 22, 8, 0.0F);
        this.setRotateAngle(ArmLeft, 0.17453292519943295F, 0.0F, 0.0F);
        this.Tongue = new ModelRenderer(this, 0, 0);
        this.Tongue.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.Tongue.addBox(-5.0F, -0.5F, -1.0F, 10, 1, 1, 0.0F);
        this.LegLeft = new ModelRenderer(this, 120, 130);
        this.LegLeft.setRotationPoint(10.0F, 0.0F, 3.5F);
        this.LegLeft.addBox(-4.5F, 0.0F, 0.0F, 9, 14, 19, 0.0F);
        this.HeadJoint = new ModelRenderer(this, 92, 104);
        this.HeadJoint.setRotationPoint(0.0F, -3.0F, -17.0F);
        this.HeadJoint.addBox(-0.5F, -0.5F, -0.5F, 1, 1, 1, 0.0F);
        this.setRotateAngle(HeadJoint, 0.17453292519943295F, 0.0F, 0.0F);
        this.LegRight = new ModelRenderer(this, 120, 130);
        this.LegRight.mirror = true;
        this.LegRight.setRotationPoint(-10.0F, 0.0F, 3.5F);
        this.LegRight.addBox(-4.5F, 0.0F, 0.0F, 9, 14, 19, 0.0F);
        this.HeadFinLeft = new ModelRenderer(this, 20, 40);
        this.HeadFinLeft.setRotationPoint(9.0F, 1.0F, -1.0F);
        this.HeadFinLeft.addBox(0.0F, -8.5F, 0.0F, 16, 17, 0, 0.0F);
        this.setRotateAngle(HeadFinLeft, 0.0F, -0.6108652381980153F, -0.08726646259971647F);
        this.EyeLeft = new ModelRenderer(this, 71, 76);
        this.EyeLeft.setRotationPoint(7.0F, -2.5F, -10.0F);
        this.EyeLeft.addBox(0.0F, -6.0F, -3.0F, 6, 6, 6, 0.0F);
        this.setRotateAngle(EyeLeft, 0.0F, 0.17453292519943295F, 0.0F);
        this.HeadJoint.addChild(this.JawLower);
        this.Body.addChild(this.ArmRight);
        this.Body.addChild(this.Tail);
        this.HeadJoint.addChild(this.HeadFinRight);
        this.LegLeft.addChild(this.FootLeft);
        this.JawUpper.addChild(this.EyeRight);
        this.Body.addChild(this.BackFin);
        this.HeadJoint.addChild(this.JawUpper);
        this.Tail.addChild(this.TailFin);
        this.LegRight.addChild(this.FootRight);
        this.Body.addChild(this.ArmLeft);
        this.Body.addChild(this.LegLeft);
        this.Body.addChild(this.HeadJoint);
        this.Body.addChild(this.LegRight);
        this.HeadJoint.addChild(this.HeadFinLeft);
        this.JawUpper.addChild(this.EyeLeft);
    }

    @Override
    public void render(MatrixStack stack, IVertexBuilder bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {
        stack.push();
        {
            int k3 = (int) ((((200F - health) / 200F) * 10F) - 1F);
            System.out.println(k3);
            if(k3 >= 0 && k3 <= 9) {
                IVertexBuilder buffer = new MatrixApplyingVertexBuilder(Minecraft.getInstance().getRenderTypeBuffers().getCrumblingBufferSource().getBuffer(ModelBakery.DESTROY_RENDER_TYPES.get(k3)), stack.getLast());
                this.Body.render(stack, buffer, packedLightIn, packedOverlayIn);
            }
        }
        stack.pop();
        this.Body.render(stack, bufferIn, packedLightIn, packedOverlayIn);
        if(Tongue.showModel && !Minecraft.getInstance().isGamePaused()) {
            stack.push();
            {
                RenderUtil.partTranslateRotate(stack, Body, HeadJoint, JawLower);
                stack.rotate(Vector3f.XN.rotation(JawLower.rotateAngleX));
                stack.translate(0F, -(1.5F / 16F), -(1F / 16F));
                double d0 = targetPos.get().getX() - entityPos.getX();
                double d1 = (targetPos.get().getY() - 1) - entityPos.getY();
                double d2 = targetPos.get().getZ() - entityPos.getZ();
                double d3 = (double) MathHelper.sqrt(d0 * d0 + d2 * d2);
                double pitch = MathHelper.wrapDegrees((float) (-(MathHelper.atan2(d1, d3) * (double) (180F / (float) Math.PI))));
                double yaw = MathHelper.wrapDegrees((float) (MathHelper.atan2(d2, d0) * (double) (180F / (float) Math.PI)) - 90.0F);
                // counteract previous GL rotate and re-rotate
                stack.rotate(Vector3f.XP.rotationDegrees((float) pitch));
                stack.scale(-1.0F, -1.0F, 1.0F);
                stack.rotate(Vector3f.YN.rotationDegrees(180F - rot));
                stack.rotate(Vector3f.YP.rotationDegrees(180F - (float) yaw));
                stack.scale(-1.0F, -1.0F, 1.0F);
                double len = (double) MathHelper.sqrt(d0 * d0 + d2 * d2 + d1 * d1);
                stack.scale(1F, 1F, (float) (len * 16F) + 0.5F);
                this.Tongue.render(stack, bufferIn, packedLightIn, packedOverlayIn);
            }
            stack.pop();
        }
    }

    @Override
    public void setRotationAngles(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        float partialTicks = Minecraft.getInstance().getRenderPartialTicks();
        this.health = entity.getHealth();
        this.tongueTicks = entity.getTongueTicks();
        this.targetPos = entity.getTargetPos();
        this.entityPos = entity.getPosition();
        this.rot = MathHelper.lerp(partialTicks, entity.prevRenderYawOffset, entity.renderYawOffset);
        this.Tongue.showModel = tongueTicks > 0 && targetPos.isPresent();
        this.JawLower.rotateAngleX = Tongue.showModel ? (float) Math.toRadians(30F) : (float) Math.toRadians(entity.getAttackTick() / 20F * 30F);
        this.Tail.rotateAngleY = MathHelper.sin(ageInTicks * 0.05F) * 0.1F;
        this.Tail.rotateAngleX = MathHelper.sin(ageInTicks * 0.015F) * 0.1F;
        this.Tail.rotateAngleY += MathHelper.sin(limbSwing * 0.25F) * 0.5F * limbSwingAmount;
        if(limbSwingAmount > 0.2) {
            float anim = entity.hopProgress / 10F;
            this.Body.rotationPointY = -(entity.hopProgress % 10F) / 2F;
            this.FootLeft.rotateAngleX = MathHelper.sin(anim) * limbSwingAmount * 0.5F + 0.17453292519943295F;
            this.FootRight.rotateAngleX = MathHelper.sin(anim) * limbSwingAmount * 0.5F + 0.17453292519943295F;
            this.LegLeft.rotateAngleX = MathHelper.sin(anim) * limbSwingAmount;
            this.LegRight.rotateAngleX = MathHelper.sin(anim) * limbSwingAmount;
            this.ArmLeft.rotateAngleX = MathHelper.cos(anim * 2F) * limbSwingAmount * 1.5F - (float) Math.PI / 8F + 0.17453292519943295F;
            this.ArmRight.rotateAngleX = MathHelper.cos(anim * 2F) * limbSwingAmount * 1.5F - (float) Math.PI / 8F + 0.17453292519943295F;
        } else {
            this.Body.rotationPointY = 2F;
            this.FootLeft.rotateAngleX = 0.17453292519943295F;
            this.FootRight.rotateAngleX = 0.17453292519943295F;
            this.LegLeft.rotateAngleX = 0F;
            this.LegRight.rotateAngleX = 0F;
            this.ArmLeft.rotateAngleX = 0.17453292519943295F;
            this.ArmRight.rotateAngleX = 0.17453292519943295F;
        }
    }

    public void setRotateAngle(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.rotateAngleX = x;
        modelRenderer.rotateAngleY = y;
        modelRenderer.rotateAngleZ = z;
    }

}
