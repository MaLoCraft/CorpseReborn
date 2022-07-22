package org.golde.bukkit.corpsereborn.nms.nmsclasses.packetlisteners;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.lang.reflect.Field;

import net.minecraft.network.NetworkManager;
import net.minecraft.network.protocol.game.PacketPlayInUseEntity;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_19_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.InventoryView;
import org.golde.bukkit.corpsereborn.ConfigData;
import org.golde.bukkit.corpsereborn.Main;
import org.golde.bukkit.corpsereborn.Util;
import org.golde.bukkit.corpsereborn.CorpseAPI.events.CorpseClickEvent;
import org.golde.bukkit.corpsereborn.nms.Corpses.CorpseData;
import org.golde.bukkit.corpsereborn.nms.TypeOfClick;

public class PcktIn_v1_19_R1 extends ChannelInboundHandlerAdapter {
	private static Field action;
	private static Object c;
	private Player p;

	static {
		try {
			action = PacketPlayInUseEntity.class.getDeclaredField("b");
			action.setAccessible(true);

			Class<? extends Enum> aClass = (Class<? extends Enum<?>>) Class.forName("net.minecraft.network.protocol.game.PacketPlayInUseEntity$b");
			c = Enum.valueOf(aClass, "c");
		} catch (NoSuchFieldException | ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	public PcktIn_v1_19_R1(Player p) {
		this.p = p;
	}

	public void channelRead(ChannelHandlerContext ctx, Object msg)
			throws Exception {
		if (msg instanceof PacketPlayInUseEntity) {
			final PacketPlayInUseEntity packet = (PacketPlayInUseEntity) msg;
			Bukkit.getServer().getScheduler()
			.runTask(Main.getPlugin(), () -> {

				try {
					if (action.get(packet) == c) {
						for (CorpseData cd : Main.getPlugin().corpses
								.getAllCorpses()) {
							if (cd.getEntityId() == getId(packet)) {
								CorpseClickEvent cce = new CorpseClickEvent(cd, p, TypeOfClick.UNKNOWN);
								Util.callEvent(cce);
								if (ConfigData.hasLootingInventory()) {
									if(!cce.isCancelled()) {
										InventoryView view = p.openInventory(cd
												.getLootInventory());
										cd.setInventoryView(view);
									}
									break;
								}
							}
						}
					}
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}

			});
		}
		super.channelRead(ctx, msg);
	}

	private int getId(PacketPlayInUseEntity packet) {
		try {
			Field afield = packet.getClass().getDeclaredField("a");
			afield.setAccessible(true);
			int id = afield.getInt(packet);
			afield.setAccessible(false);
			return id;
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}

	public static final void registerListener(Player p) {
		Channel c = getChannel(p);
		if (c == null) {
			throw new NullPointerException("Couldn't get channel??");
		}
		c.pipeline().addBefore("packet_handler", "packet_in_listener",
				new PcktIn_v1_19_R1(p));
	}

	public static final Channel getChannel(Player p) {
		NetworkManager nm = ((CraftPlayer) p).getHandle().b.b;
		try {
			return nm.m;
			/*
			Field ifield = nm.getClass().getDeclaredField("channel");
			ifield.setAccessible(true);
			Channel c = (Channel) ifield.get(nm);
			ifield.setAccessible(false);
			return c;
			 */
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}