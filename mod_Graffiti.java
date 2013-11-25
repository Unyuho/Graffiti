package net.minecraft.graffiti;

import java.util.EnumSet;
import java.util.logging.Level;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.graffiti.client.ClientTickHandler;
import net.minecraft.item.Item;
import net.minecraft.item.ItemDye;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.src.ModLoader;
import net.minecraftforge.common.Configuration;
import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.ITickHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.TickType;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.registry.EntityRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;
import cpw.mods.fml.common.registry.TickRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@Mod(modid = "Graffiti", name = "Graffiti Block", version = "1.0.0")
@NetworkMod(clientSideRequired = true, serverSideRequired = false, channels = {"graffiti"}, packetHandler = PacketHandler.class)
public class mod_Graffiti
{
	@SidedProxy(clientSide = "net.minecraft.graffiti.client.ClientProxy", serverSide = "net.minecraft.graffiti.CommonProxy")
	public static CommonProxy proxy;

	public static int blockId = 2180;
	public static int itemId = 2181;
	public int posionX = 0;
	public int posionY = 0;
	public int color = 0xFFFFFF;

	public static GraffitiBlock graffitiBlock = null;
	public static ItemGraffiti graffitiItem = null;

	private static float width = 0.0125F;
	private static float lineWidth = 0.01F;

	@EventHandler
	public void preInit(FMLPreInitializationEvent event)
	{
		Configuration cfg = new Configuration(event.getSuggestedConfigurationFile());

		try
		{
			cfg.load();
			blockId = cfg.getBlock("GraffitiBlockID", blockId).getInt();
			itemId = cfg.getItem("ItemID", itemId).getInt();

			posionX = cfg.get("displayinfo", "PosionX", posionX).getInt();
			posionY = cfg.get("displayinfo", "PosionY", posionY).getInt();
			color = cfg.get("displayinfo", "Color", color).getInt();
		}
		catch (Exception e)
		{
			FMLLog.log(Level.SEVERE, e, "Error Massage");
		}
		finally
		{
			cfg.save();
		}

		int renderType = proxy.getBlockNewRenderType();
		graffitiBlock = new GraffitiBlock(blockId, renderType);
		GameRegistry.registerBlock(graffitiBlock, "graffitiBlock");

		graffitiItem = new ItemGraffiti(itemId, blockId, posionX, posionY, color);
		GameRegistry.registerItem(graffitiItem, "graffitiItem");
	}

	@EventHandler
	public void init(FMLInitializationEvent event)
	{
		proxy.registerRenderInformation();

		LanguageRegistry.addName(graffitiBlock, "Graffiti Block");
		LanguageRegistry.instance().addNameForObject(graffitiBlock, "ja_JP", "落書きブロック");

		for(int i = 0 ; i < 16 ; i++)
		{
			ItemStack itemstack = new ItemStack(graffitiItem, 1, i);
			LanguageRegistry.addName(itemstack, "Graffiti Item(" + ItemDye.dyeColorNames[15 - i] + ")");
			LanguageRegistry.instance().addNameForObject(itemstack, "ja_JP", "落書きアイテム(" + ItemDye.dyeColorNames[15 - i] + ")");
		}


		GameRegistry.addRecipe(
			new ItemStack(graffitiItem, 4, 0),
			new Object[] {	" X ",
							"X X",
							" X ",
							'X', new ItemStack(Item.silk)
						}
		);

		CraftingManager.getInstance().getRecipeList().add(new ShapelessGraffitiRecipes(new ItemStack(graffitiItem, 1, 0)));
		CraftingManager.getInstance().getRecipeList().add(new ShapelessGraffitiRecipesReverse(new ItemStack(graffitiItem, 1, 0)));

		TickRegistry.registerTickHandler(new ClientTickHandler(), Side.CLIENT);
	}

	public static float getWidth()
	{
		return width;
	}

	public static float getLineWidth()
	{
		return lineWidth;
	}
}
