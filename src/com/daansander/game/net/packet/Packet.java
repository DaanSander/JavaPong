package com.daansander.game.net.packet;

import com.daansander.game.net.GameClient;
import com.daansander.game.net.GameServer;

public abstract class Packet {

	public enum PacketType {
		INVALID(-1), LOGIN(00), DISCONNECT(01), PLATFORM_MOVE(02), BALL_CHANGE_DIRECTION(03), BALL_MOVE(04);

		private int id;

		PacketType(int id) {
			this.id = id;
		}

		public int getId() {
			return id;
		}
	}

	protected byte packetId;

	protected Packet(int packetId) {
		this.packetId = (byte) packetId;
	}

	public abstract void writeData(GameClient client);

	public abstract void writeData(GameServer server);

	public String readData(byte[] data) {
		String message = new String(data).trim();
		return message.substring(2);
	}
	
	public abstract byte[] getData();
	
	public static PacketType lookupPacket(String packetId) {
		try {
			return lookupPacket(Integer.parseInt(packetId));
		} catch(NumberFormatException ex) {
			return PacketType.INVALID;
		}
	}

	public static PacketType lookupPacket(int id) {
		for(PacketType type : PacketType.values()) {
			if(type.getId() == id) {
				return type;
			}
		}
		return PacketType.INVALID;
	}
	
}