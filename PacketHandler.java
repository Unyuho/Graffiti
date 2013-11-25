package net.minecraft.graffiti;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Random;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.network.IPacketHandler;
import cpw.mods.fml.common.network.Player;

public class PacketHandler implements IPacketHandler
{
    private Random rand = new Random();

    @Override
    public void onPacketData(INetworkManager manager, Packet250CustomPayload packet, Player player)
    {
		ByteArrayDataInput data = ByteStreams.newDataInput(packet.data);

		int x = data.readInt();
		int y = data.readInt();
		int z = data.readInt();

		TileEntity tileentity = null;

		World worldClient = FMLClientHandler.instance().getClient().theWorld;
		World worldServer = ((EntityPlayer)player).worldObj;

		if(worldClient != null && worldServer == null)
		{
			tileentity = worldClient.getBlockTileEntity(x, y, z);
		}

		if(worldServer != null)
		{
			tileentity = worldServer.getBlockTileEntity(x, y, z);
		}

		if (tileentity != null) {
			if(tileentity instanceof TileEntityGraffitiBlock)
			{
				((TileEntityGraffitiBlock)tileentity).readToPacket(data);
			}
		}
    }

    public static Packet getPacket(TileEntityGraffitiBlock tileentity)
    {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		DataOutputStream dos = new DataOutputStream(bos);

		int x = tileentity.xCoord;
		int y = tileentity.yCoord;
		int z = tileentity.zCoord;

		try
		{
			dos.writeInt(x);
			dos.writeInt(y);
			dos.writeInt(z);
			tileentity.writeToPacket(dos);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		Packet250CustomPayload packet = new Packet250CustomPayload();
		packet.data    = bos.toByteArray();
		packet.length  = bos.size();
		packet.isChunkDataPacket = true;
		packet.channel = "graffiti";

		return packet;

    }
}
