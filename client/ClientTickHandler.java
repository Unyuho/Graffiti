package net.minecraft.graffiti.client;

import java.util.EnumSet;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.graffiti.ItemGraffiti;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import cpw.mods.fml.common.ITickHandler;
import cpw.mods.fml.common.TickType;

public class ClientTickHandler implements ITickHandler
{
	private Minecraft minecraft;

    public ClientTickHandler()
    {
    	minecraft = Minecraft.getMinecraft();
    }

	@Override
	public void tickStart(EnumSet<TickType> type, Object... tickData)
	{
	}

	@Override
	public void tickEnd(EnumSet<TickType> type, Object... tickData)
	{
		GuiScreen guiscreen = minecraft.currentScreen;
		if (guiscreen == null)
		{
			onTickInGame();
		}
	}

	@Override
	public EnumSet<TickType> ticks()
	{
		return EnumSet.of(TickType.RENDER);
	}

	@Override
	public String getLabel()
	{
		return null;
	}


    public void onTickInGame()
    {
		EntityPlayer entityplayer = minecraft.thePlayer;

		// アイテム情報表示
		ItemStack itemstack = entityplayer.inventory.getCurrentItem();

		if (itemstack != null) {
			Item item = itemstack.getItem();

			if (item instanceof ItemGraffiti)
			{
				((ItemGraffiti)item).displayItemInfo(itemstack);
			}
		}
    }

}
