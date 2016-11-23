package com.blargsworkshop.airmattress.events;

import java.util.HashMap;

import com.blargsworkshop.airmattress.AirMattressMod;

import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent.Phase;
import cpw.mods.fml.common.gameevent.TickEvent.PlayerTickEvent;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChunkCoordinates;
import net.minecraftforge.event.entity.player.PlayerWakeUpEvent;

public class EventHandlers {
	static final HashMap<String, Boolean> isPlayerWakingUpMap = new HashMap<String, Boolean>();
	static final HashMap<String, ChunkCoordinates> spawnPoints = new HashMap<String, ChunkCoordinates>();
	
	/**
	 * When a player wakes from bed, there spawn point is not yet set.
	 * Grab this old coordinate and save to restore after the new spawn is set.
	 * @param event
	 */
	@SubscribeEvent(priority=EventPriority.NORMAL, receiveCanceled=true)
	public void onEvent(PlayerWakeUpEvent event) {
		if (!event.entityPlayer.worldObj.isRemote) {
			EntityPlayer player = event.entityPlayer;
			AirMattressMod.debug("Waking" + event.entityPlayer.getDisplayName(), 3, player);
			AirMattressMod.debug("Old Spawn for " + player.getDisplayName() + ": " + (player.getBedLocation(player.dimension) != null ? player.getBedLocation(player.dimension) : "null"), 2, player);
			
			//A player's spawn point is not yet set right when he/she wakes up.
			ChunkCoordinates oldSpawnPoint = player.getBedLocation(player.dimension);
			spawnPoints.put(player.getDisplayName(), oldSpawnPoint);
			isPlayerWakingUpMap.put(player.getDisplayName(), Boolean.TRUE);
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
		if (event.player.worldObj.isRemote == false && event.phase == Phase.START && Boolean.TRUE.equals(isPlayerWakingUpMap.get(player.getDisplayName()))) {
			AirMattressMod.debug("Post Wake for " + event.player.getDisplayName(), 3, player);
			//clear the waking flag so this doesn't run for every tick
			isPlayerWakingUpMap.put(player.getDisplayName(), Boolean.FALSE);
			
			ChunkCoordinates bedPosition = player.getBedLocation(player.dimension);
			if (bedPosition != null) {
				Block bedType = player.worldObj.getBlock(bedPosition.posX, bedPosition.posY, bedPosition.posZ);
				if (bedType != null && bedType == AirMattressMod.airMattressBlock) {
					AirMattressMod.debug(player.getDisplayName() + " slept on an AirMattress", 1, player);
					
					ChunkCoordinates newSpawnPoint = player.getBedLocation(player.dimension);
					ChunkCoordinates oldSpawnPoint = spawnPoints.get(player.getDisplayName());
					if (!newSpawnPoint.equals(oldSpawnPoint)) {
						player.setSpawnChunk(oldSpawnPoint, false);
						ChunkCoordinates verifyBedLocation = player.getBedLocation(player.dimension);
						if ((oldSpawnPoint != null && oldSpawnPoint.equals(verifyBedLocation)) || (oldSpawnPoint == null && verifyBedLocation == null)) {
							AirMattressMod.debug("Spawn point adjustment for " + player.getDisplayName() + ": Successful", 1, player);
						}
						else {
							AirMattressMod.debug("Spawn point adjustment for " + player.getDisplayName() + ": Failed", 1, player);
						}
					}
				}
				else {
					AirMattressMod.debug(player.getDisplayName() + " slept on an " + (bedType != null ? bedType.toString() : "nullBed"), 1, player);
				}
				AirMattressMod.debug("New Spawn for " + player.getDisplayName() + ": " + (player.getBedLocation(player.dimension) != null ? player.getBedLocation(player.dimension) : "null"), 2, player);
			}
		}
	}
}
