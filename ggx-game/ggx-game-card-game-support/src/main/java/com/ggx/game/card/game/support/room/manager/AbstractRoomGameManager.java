package com.ggx.game.card.game.support.room.manager;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.ggx.core.common.executor.SingleThreadTaskExecutor;
import com.ggx.core.common.executor.TaskExecutor;
import com.ggx.core.common.future.GGFuture;
import com.ggx.game.card.game.support.house.House;
import com.ggx.game.card.game.support.player.RoomPlayer;
import com.ggx.game.card.game.support.player.factory.PlayerFactory;
import com.ggx.game.card.game.support.room.Room;
import com.ggx.game.card.game.support.room.factory.RoomFactory;
import com.xzcode.ggserver.core.server.GGServer;

/**
 * 房间管理器
 * 
 * @param <P>
 * @param <R>
 * @param <H>
 * @author zai
 * 2019-12-22 11:30:22
 */
public abstract class AbstractRoomGameManager
<
P extends RoomPlayer<P, R, H>,
R extends Room<P, R, H>, 
H extends House<P, R, H>
> implements RoomGameManager<P, R, H> {
	
	/**
	 * ggserver对象
	 */
	protected GGServer ggserver;
	
	/**
	 * 大厅集合Map<houseNo, house>
	 */
	protected Map<String, H> houses = new ConcurrentHashMap<>();
	
	/**
	 * 玩家集合Map<playerNo, player>
	 */
	protected Map<String, P> players = new ConcurrentHashMap<>(1000);
	
	
	/**
	 * 任务执行器
	 */
	protected TaskExecutor executor;
	
	
	protected PlayerFactory<P> playerFactory;
	
	protected RoomFactory<R> roomFactory;
	
	
	/**
	 * 构造器
	 * @param designPlayerSize 预计总玩家数量
	 */
	public AbstractRoomGameManager(
			GGServer ggserver,
			PlayerFactory<P> playerFactory,
			RoomFactory<R> roomFactory
			) {
		
		this.ggserver = ggserver;
		this.playerFactory = playerFactory;
		this.roomFactory = roomFactory;
		
		
		executor = new SingleThreadTaskExecutor("room-game-manager-");
		
	}
	
	
	
	@Override
	public P createPlayer() {
		return playerFactory.createPlayer();
	}
	@Override
	public R createRoom() {
		R room = roomFactory.createRoom();
		//注册玩家离开房间监听器
		//当玩家离开时，同时在游戏管理器移除玩家
		room.addPlayerLeaveListener(player -> {
			removePlayer(player.getPlayerNo());
		});
		
		return room;
	}
	
	@Override
	public GGFuture submitTask(Runnable runnable) {
		return executor.submitTask(runnable);
	}
	
		
	@Override
	public void removeRoom(String roomNo) {
		House<P, R, H> house = houses.get(roomNo);
		if (house == null) {
			return;
		}
		house.removeRoom(roomNo);
	}
	
	@Override
	public void removeRoom(R room) {
		House<P, R, H> house = houses.get(room.getHouse().getHouseNo());
		if (house == null) {
			return;
		}
		house.removeRoom(room.getRoomNo());
	}
	
	@Override
	public void addRoom(R room) {
		House<P, R, H> house = houses.get(room.getRoomNo());
		if (house == null) {
			return;
		}
		house.addRoom(room);
	}
	
	@Override
	public void addHouse(H house) {
		houses.put(house.getHouseNo(), house);
	}
	
	@Override
	public void removeHouse(H house) {
		houses.remove(house.getHouseNo());
	}
	
	@Override
	public void removeHouse(String houseNO) {
		houses.remove(houseNO);
	}
	
	@Override
	public void addPlayer(P player) {
		players.put(player.getPlayerNo(), player);
	}
	@Override
	public void removePlayer(P player) {
		players.remove(player.getPlayerNo());
	}
	@Override
	public void removePlayer(String playerNo) {
		players.remove(playerNo);
	}
	
	
	
	@Override
	public H getHouse(String houseNo){
		return houses.get(houseNo);
	}
	
	@Override
	public Map<String, H> getHouses() {
		return houses;
	}
	
	@Override
	public Map<String, P> getPlayers() {
		return players;
	}
	
	@Override
	public P getPlayer(String playerNo) {
		return players.get(playerNo);
	}
	
	@Override
	public TaskExecutor getTaskExecutor() {
		return executor;
	}
	
	public GGServer getGGserver() {
		return ggserver;
	}

}
