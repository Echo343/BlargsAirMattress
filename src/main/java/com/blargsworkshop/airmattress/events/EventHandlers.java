package com.blargsworkshop.airmattress.events;

import java.util.HashMap;
import java.util.Iterator;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChunkCoordinates;
import net.minecraftforge.event.entity.player.PlayerSleepInBedEvent;
import net.minecraftforge.event.entity.player.PlayerWakeUpEvent;

import com.blargsworkshop.airmattress.AirMattressMod;

import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent.Phase;
import cpw.mods.fml.common.gameevent.TickEvent.PlayerTickEvent;

public class EventHandlers {
	static final HashMap<String, Boolean> flag = new HashMap<String, Boolean>();
	static final HashMap<String, ChunkCoordinates> spawnPoints = new HashMap<String, ChunkCoordinates>();
//	static int count = 0;
	
	@SubscribeEvent(priority=EventPriority.NORMAL, receiveCanceled=true)
	public void onEvent(PlayerWakeUpEvent event) {
		if (!event.entityPlayer.worldObj.isRemote) {
			
			//reset count if needed
//			boolean isFound = false;
//			for (Boolean b : flag.values()) {
//				if (b.equals(Boolean.TRUE)) {
//					isFound = true;
//					break;
//				}
//			}
//			if (!isFound) {
//				count = 0;
//			}
			
			EntityPlayer player = event.entityPlayer;
			AirMattressMod.debug("Waking" + event.entityPlayer.getDisplayName(), 3, player);
			AirMattressMod.debug("Old Spawn for " + player.getDisplayName() + ": " + player.getBedLocation(player.dimension), 2, player);
			
			ChunkCoordinates oldSpawnPoint = player.getBedLocation(player.dimension);
			spawnPoints.put(player.getDisplayName(), oldSpawnPoint);
			flag.put(player.getDisplayName(), Boolean.TRUE);
//			count++;
		}
	}
	
//	@SubscribeEvent(priority=EventPriority.LOWEST, receiveCanceled=false)
//	public void onEvent(PlayerSleepInBedEvent event) {
//		if (event.entityPlayer.worldObj.isRemote == false) {
//			EntityPlayerMP player = (EntityPlayerMP) event.entityPlayer;
//			Iterator<?> it = player.mcServer.getConfigurationManager().playerEntityList.iterator();
//			while (it.hasNext()) {
//				EntityPlayer aPlayer = (EntityPlayer) it.next();
//				aPlayer.addChatMessage(new ChatComponentText(aPlayer.getDisplayName() + " is sleeping."));
//			}
//		}
//	}

	@SubscribeEvent(priority=EventPriority.NORMAL, receiveCanceled=true)
	public void onEvent(PlayerTickEvent event) {
		EntityPlayer player = event.player;
		if (event.player.worldObj.isRemote == false && event.phase == Phase.START && Boolean.TRUE.equals(flag.get(player.getDisplayName()))) {
//			if (flag.get(player.getDisplayName()).equals(Boolean.TRUE)) {
				AirMattressMod.debug("Post Wake for " + event.player.getDisplayName(), 3, player);
				flag.put(player.getDisplayName(), Boolean.FALSE);
//				count--;
				
				ChunkCoordinates bedPosition = player.getBedLocation(player.dimension);
				if (bedPosition != null) {
					Block bedType = player.worldObj.getBlock(bedPosition.posX, bedPosition.posY, bedPosition.posZ);
					if (bedType != null) {
						if (bedType == AirMattressMod.airMattressBlock) {
							AirMattressMod.debug(player.getDisplayName() + " slept on an AirMattress", 1, player);
							
							ChunkCoordinates newSpawnPoint = player.getBedLocation(player.dimension);
							ChunkCoordinates oldSpawnPoint = spawnPoints.get(player.getDisplayName());
							if (!newSpawnPoint.equals(oldSpawnPoint)) {
								player.setSpawnChunk(oldSpawnPoint, false);
								AirMattressMod.debug("Spawn point adjustment for " + player.getDisplayName() + ": " + ((oldSpawnPoint.equals(player.getBedLocation(player.dimension))) ? "Successful" : "Failed"), 1, player);
							}
							
						}
						else {
							AirMattressMod.debug(player.getDisplayName() + " slept on an " + bedType.toString(), 1, player);
						}
						AirMattressMod.debug("New Spawn for " + player.getDisplayName() + ": " + player.getBedLocation(player.dimension), 2, player);
					}
				}
//			}
		}
	}
}
