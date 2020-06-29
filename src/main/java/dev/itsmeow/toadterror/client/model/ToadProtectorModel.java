package dev.itsmeow.toadterror.client.model;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;

import dev.itsmeow.toadterror.entity.ToadProtectorEntity;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.model.ModelRenderer;

public class ToadProtectorModel<T extends ToadProtectorEntity> extends EntityModel<T> {
    public ModelRenderer BodyMain;
    public ModelRenderer EyeLeft;
    public ModelRenderer EyeRight;
    public ModelRenderer LegLeft;
    public ModelRenderer LegRight;
    public ModelRenderer ArmLeft;
    public ModelRenderer ArmRight;
    public ModelRenderer FrillLeft;
    public ModelRenderer FrillRight;
    public ModelRenderer BackFin;
    public ModelRenderer Tail;
    public ModelRenderer FootLeft;
    public ModelRenderer FootRight;
    public ModelRenderer TailFin;

    public ToadProtectorModel() {
        this.textureWidth = 80;
        this.textureHeight = 80;
        this.TailFin = new ModelRenderer(this, 50, 20);
        this.TailFin.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.TailFin.addBox(0.0F, -4.0F, 0.0F, 0, 8, 13, 0.0F);
        this.LegRight = new ModelRenderer(this, 20, 33);
        this.LegRight.setRotationPoint(-6.0F, -1.0F, 3.5F);
        this.LegRight.addBox(-1.5F, 0.0F, 0.0F, 3, 6, 9, 0.0F);
        this.BackFin = new ModelRenderer(this, 5, 10);
        this.BackFin.setRotationPoint(0.0F, -3.0F, 1.0F);
        this.BackFin.addBox(0.0F, -4.0F, -2.0F, 0, 4, 8, 0.0F);
        this.EyeLeft = new ModelRenderer(this, 1, 1);
        this.EyeLeft.setRotationPoint(2.5F, -0.5F, -4.5F);
        this.EyeLeft.addBox(0.0F, -5.0F, -2.5F, 5, 5, 5, 0.0F);
        this.EyeRight = new ModelRenderer(this, 1, 1);
        this.EyeRight.mirror = true;
        this.EyeRight.setRotationPoint(-2.5F, -0.5F, -4.5F);
        this.EyeRight.addBox(-5.0F, -5.0F, -2.5F, 5, 5, 5, 0.0F);
        this.FrillLeft = new ModelRenderer(this, 11, 12);
        this.FrillLeft.setRotationPoint(6.0F, -1.5F, -1.5F);
        this.FrillLeft.addBox(0.0F, -2.5F, 0.0F, 5, 5, 0, 0.0F);
        this.setRotateAngle(FrillLeft, 0.0F, -0.6108652381980153F, 0.0F);
        this.FrillRight = new ModelRenderer(this, 11, 12);
        this.FrillRight.mirror = true;
        this.FrillRight.setRotationPoint(-6.0F, -1.5F, -1.5F);
        this.FrillRight.addBox(-5.0F, -2.5F, 0.0F, 5, 5, 0, 0.0F);
        this.setRotateAngle(FrillRight, 0.0F, 0.6108652381980153F, 0.0F);
        this.FootLeft = new ModelRenderer(this, 15, 49);
        this.FootLeft.setRotationPoint(0.0F, 6.0F, 9.0F);
        this.FootLeft.addBox(-1.5F, 0.0F, -10.0F, 3, 2, 10, 0.0F);
        this.setRotateAngle(FootLeft, 0.17453292519943295F, 0.0F, 0.0F);
        this.ArmRight = new ModelRenderer(this, 1, 32);
        this.ArmRight.mirror = true;
        this.ArmRight.setRotationPoint(-4.0F, 2.5F, -3.0F);
        this.ArmRight.addBox(-3.0F, -1.0F, -2.0F, 3, 8, 4, 0.0F);
        this.setRotateAngle(ArmRight, 0.17453292519943295F, 0.0F, 0.0F);
        this.ArmLeft = new ModelRenderer(this, 1, 32);
        this.ArmLeft.setRotationPoint(4.0F, 2.5F, -3.0F);
        this.ArmLeft.addBox(0.0F, -1.0F, -2.0F, 3, 8, 4, 0.0F);
        this.setRotateAngle(ArmLeft, 0.17453292519943295F, 0.0F, 0.0F);
        this.LegLeft = new ModelRenderer(this, 20, 33);
        this.LegLeft.mirror = true;
        this.LegLeft.setRotationPoint(6.0F, -1.0F, 3.5F);
        this.LegLeft.addBox(-1.5F, 0.0F, 0.0F, 3, 6, 9, 0.0F);
        this.Tail = new ModelRenderer(this, 49, 8);
        this.Tail.setRotationPoint(0.0F, 0.0F, 10.5F);
        this.Tail.addBox(-1.5F, -2.5F, 0.0F, 3, 5, 10, 0.0F);
        this.setRotateAngle(Tail, 0.08726646259971647F, 0.0F, 0.0F);
        this.BodyMain = new ModelRenderer(this, 1, 1);
        this.BodyMain.setRotationPoint(0.0F, 15.5F, 0.0F);
        this.BodyMain.addBox(-6.0F, -3.5F, -11.5F, 12, 7, 23, 0.0F);
        this.setRotateAngle(BodyMain, -0.17453292519943295F, 0.0F, 0.0F);
        this.FootRight = new ModelRenderer(this, 15, 49);
        this.FootRight.mirror = true;
        this.FootRight.setRotationPoint(0.0F, 6.0F, 9.0F);
        this.FootRight.addBox(-1.5F, 0.0F, -10.0F, 3, 2, 10, 0.0F);
        this.setRotateAngle(FootRight, 0.17453292519943295F, 0.0F, 0.0F);
        this.Tail.addChild(this.TailFin);
        this.BodyMain.addChild(this.LegRight);
        this.BodyMain.addChild(this.BackFin);
        this.BodyMain.addChild(this.EyeLeft);
        this.BodyMain.addChild(this.EyeRight);
        this.BodyMain.addChild(this.FrillLeft);
        this.BodyMain.addChild(this.FrillRight);
        this.LegLeft.addChild(this.FootLeft);
        this.BodyMain.addChild(this.ArmRight);
        this.BodyMain.addChild(this.ArmLeft);
        this.BodyMain.addChild(this.LegLeft);
        this.BodyMain.addChild(this.Tail);
        this.LegRight.addChild(this.FootRight);
    }

    @Override
    public void render(MatrixStack stack, IVertexBuilder bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {
        this.BodyMain.render(stack, bufferIn, packedLightIn, packedOverlayIn);
    }

    @Override
    public void setRotationAngles(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

    }

    public void setRotateAngle(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.rotateAngleX = x;
        modelRenderer.rotateAngleY = y;
        modelRenderer.rotateAngleZ = z;
    }
}
