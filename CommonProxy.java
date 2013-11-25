package net.minecraft.graffiti;

import net.minecraft.block.Block;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.registry.GameRegistry;

public class CommonProxy
{
    public int getBlockNewRenderType()
    {
        return RenderingRegistry.getNextAvailableRenderId();
    }

    public World getClientWorld()
    {
        return null;
    }

    public void registerRenderInformation()
    {
    	GameRegistry.registerTileEntity(TileEntityGraffitiBlock.class, "graffitiBlock");
    }
}