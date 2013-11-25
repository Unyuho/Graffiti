package net.minecraft.graffiti.client;


import net.minecraft.block.Block;
import net.minecraft.graffiti.CommonProxy;
import net.minecraft.graffiti.TileEntityGraffitiBlock;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ClientProxy extends CommonProxy
{
    @Override
    public World getClientWorld()
    {
        return FMLClientHandler.instance().getClient().theWorld;
    }

    @Override
    public void registerRenderInformation()
    {
        ClientRegistry.registerTileEntity(TileEntityGraffitiBlock.class, "graffitiBlock", new GraffitiBlockRenderer());
    }
}