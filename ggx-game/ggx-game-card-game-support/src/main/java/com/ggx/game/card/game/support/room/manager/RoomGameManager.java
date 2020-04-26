package com.ggx.game.card.game.support.room.manager;

import java.util.Map;

import com.ggx.core.common.executor.support.ExecutorSupport;
import com.ggx.core.common.future.GGFuture;
import com.ggx.game.card.game.support.house.House;
import com.ggx.game.card.game.support.player.RoomPlayer;
import com.ggx.game.card.game.support.room.Room;

public interface RoomGameManager
<
P extends RoomPlayer<P, R, H>, 
R extends Room<P, R, H>, 
H extends House<P, R, H>
> 
extends ExecutorSupport
{

	P createPlayer();

	R createRoom();


	GGFuture submitTask(Runnable runnable);

	void removeRoom(String roomNo);

	void removeRoom(R room);

	void addRoom(R room);

	void addHouse(H house);

	void removeHouse(H house);

	void removeHouse(String houseNO);

	void addPlayer(P player);

	void removePlayer(P player);

	void removePlayer(String playerNo);

	H getHouse(String houseNo);

	Map<String, H> getHouses();

	Map<String, P> getPlayers();

	P getPlayer(String playerNo);
	
	void updateHouses();


}