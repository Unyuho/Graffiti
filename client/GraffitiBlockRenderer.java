package net.minecraft.graffiti.client;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.graffiti.GraffitiBlock;
import net.minecraft.graffiti.mod_Graffiti;
import net.minecraft.tileentity.TileEntity;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

public class GraffitiBlockRenderer extends TileEntitySpecialRenderer
{
    public GraffitiBlockRenderer()
    {
    }

    @Override
    public void renderTileEntityAt(TileEntity par1tileentity, double d, double d1, double d2, float f)
    {
        float f1 = 1.0F;
        GL11.glPushMatrix();
        GL11.glTranslated(d, d1, d2);
        //GL11.glTranslated(-par1tileentity.xCoord, -par1tileentity.yCoord, -par1tileentity.zCoord);
        GL11.glEnable(GL12.GL_RESCALE_NORMAL);
        GL11.glScaled(f1, f1, f1);
        RenderHelper.disableStandardItemLighting();
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_CULL_FACE);

        if (Minecraft.isAmbientOcclusionEnabled())
        {
            GL11.glShadeModel(GL11.GL_SMOOTH);
        }
        else
        {
            GL11.glShadeModel(GL11.GL_FLAT);
        }

        //Tessellator.instance.startDrawingQuads();
        func_110628_a(TextureMap.field_110575_b);

        GraffitiBlock block = (GraffitiBlock)Block.blocksList[par1tileentity.worldObj.getBlockId(par1tileentity.xCoord, par1tileentity.yCoord, par1tileentity.zCoord)];
        RenderGraffitiBlock renderdeco = new RenderGraffitiBlock(par1tileentity.worldObj);

        renderdeco.renderBlockByRenderTypeCustom(block, par1tileentity.xCoord, par1tileentity.yCoord, par1tileentity.zCoord);
        //Tessellator.instance.draw();

        GL11.glDisable(GL12.GL_RESCALE_NORMAL);
        GL11.glPopMatrix();
    }
}
