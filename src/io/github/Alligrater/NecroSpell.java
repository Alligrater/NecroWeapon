package io.github.Alligrater;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event.Result;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;

public class NecroSpell implements Listener{
	
	static HashMap<String, Integer> beats = new HashMap<String, Integer>();
	static HashMap<String, BukkitRunnable> cancellist = new HashMap<String, BukkitRunnable>();
	static List<String> denyDrum  = new ArrayList<String>();
	static List<String> throwready = new ArrayList<String>();
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void onTomeUse(PlayerInteractEvent event) {
		if(event.getAction().equals(Action.RIGHT_CLICK_AIR) || event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
			Player player = event.getPlayer();
			ItemStack ritem = event.getPlayer().getInventory().getItemInMainHand();
			ItemStack litem = event.getPlayer().getInventory().getItemInMainHand();
			if(ritem.getType().equals(Material.BOOK)) {
				if(ritem.getItemMeta().hasLore()) {
					List<String> lores = ritem.getItemMeta().getLore();
					if(lores.contains("¡ì7SHIELD")) {
						event.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 360, 9), true);
						if(player.getGameMode() != GameMode.CREATIVE) {
							ritem.setAmount(ritem.getAmount() - 1);
							if(ritem.getAmount() <= 0) {
								player.getInventory().setItemInMainHand(null);;
							}
						}
						player.getWorld().playSound(player.getLocation(), Sound.ITEM_SHIELD_BLOCK, 1.0f, 1.0f);
					}
					else if(lores.contains("¡ì7HEAL")) {
						event.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 5, 9), true);
						if(player.getGameMode() != GameMode.CREATIVE) {
							ritem.setAmount(ritem.getAmount() - 1);
							if(ritem.getAmount() <= 0) {
								player.getInventory().setItemInMainHand(null);;
							}
						}
						player.getWorld().playSound(player.getLocation(), Sound.ITEM_BUCKET_FILL_LAVA, 1.0f, 1.0f);
					}
					else if(lores.contains("¡ì7EXCLUSION")) {
						for(Entity le : player.getNearbyEntities(5, 5, 5)) {
							if(le.getName().equals(player.getName()) && le instanceof LivingEntity) {
								Vector playerPos = player.getLocation().toVector();
								Vector mobPos = le.getLocation().toVector();
								Vector newVelocity = (mobPos.subtract(playerPos)).normalize().multiply(3);
								le.setVelocity(newVelocity);
							}
						}
						if(player.getGameMode() != GameMode.CREATIVE) {
							ritem.setAmount(ritem.getAmount() - 1);
							if(ritem.getAmount() <= 0) {
								player.getInventory().setItemInMainHand(null);;
							}
						}
						player.getWorld().playSound(player.getLocation(), Sound.ENTITY_ENDERMEN_TELEPORT, 1.0f, 1.0f);
					}
					else if(lores.contains("¡ì7GRAVITON")) {
						for(Entity le : player.getNearbyEntities(5, 5, 5)) {
							if(le.getName().equals(player.getName())  && le instanceof LivingEntity) {
								Vector playerPos = player.getLocation().toVector();
								Vector mobPos = le.getLocation().toVector();
								Vector newVelocity = (playerPos.subtract(mobPos)).normalize().multiply(3);
								le.setVelocity(newVelocity);
							}
						}
						if(player.getGameMode() != GameMode.CREATIVE) {
							ritem.setAmount(ritem.getAmount() - 1);
							if(ritem.getAmount() <= 0) {
								player.getInventory().setItemInMainHand(null);;
							}
						}
						player.getWorld().playSound(player.getLocation(), Sound.BLOCK_ENDERCHEST_OPEN, 1.0f, 1.0f);
					}
				}
			}
			else if(ritem.getType().equals(Material.JUKEBOX)){
				event.setUseInteractedBlock(Result.DENY);
				event.setUseItemInHand(Result.DENY);
				event.setCancelled(true);
				if(ritem.getItemMeta().hasLore()) {
					List<String> rlores = ritem.getItemMeta().getLore();
					if(rlores.contains("¡ì7WARDRUM")) {
						if(cancellist.containsKey(player.getName())) {
							cancellist.get(player.getName()).cancel();
						}
						
						if(denyDrum.contains(player.getName())) {
							return;
						}
						else {
							denyDrum.add(player.getName());
							player.getWorld().playSound(player.getLocation(), Sound.BLOCK_NOTE_BASEDRUM, (float)1.0, (float)1.0);
							beats.put(player.getName(), beats.getOrDefault(player.getName(), 0) + 1);
							BukkitRunnable remove = new BukkitRunnable() {
								
								@Override
								public void run() {
									beats.remove(player.getName());
									
								}
								
							};
							cancellist.put(player.getName(), remove);
							
							BukkitRunnable undeny = new BukkitRunnable() {
								
								@Override
								public void run() {
									denyDrum.remove(player.getName());
									
								}
								
							};
							cancellist.put(player.getName(), remove);
							
							remove.runTaskLater(Bukkit.getPluginManager().getPlugin("NecroWeapon"), 60);
							undeny.runTaskLater(Bukkit.getPluginManager().getPlugin("NecroWeapon"), 8);
						}
						
					}
				}
			}
			else if(NecroAttack.weapon.contains(ritem.getType())){
				if(ritem.getItemMeta().hasLore()) {
					List<String> rlores = ritem.getItemMeta().getLore();
					if(rlores.contains("¡ì7DAGGER")) {
						throwready.add(player.getName());
						TextComponent trhint = new TextComponent("¡ìe>>>Dagger Throw Ready!<<<");
						player.spigot().sendMessage(ChatMessageType.ACTION_BAR, trhint);
						BukkitRunnable unready = new BukkitRunnable() {

							@Override
							public void run() {
								throwready.remove(player.getName());
								player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent("¡ìc>>>Dagger Throw Unready...<<<"));
							}
							
						};
						unready.runTaskLater(Bukkit.getPluginManager().getPlugin("NecroWeapon"), 20);
					}
				}
			}
		}
	}
	
	
	@EventHandler
	public void onItemDrop(PlayerDropItemEvent event) {
		if(event.getItemDrop().getItemStack().hasItemMeta()) {
			ItemStack item = event.getItemDrop().getItemStack();
			if(item.getItemMeta().hasLore()) {
				if(item.getItemMeta().getLore().contains("¡ì7DAGGER") && throwready.contains(event.getPlayer().getName())) {
					Item dagger = event.getItemDrop();
					ItemStack temp = dagger.getItemStack();
					temp.setDurability((short) ((int)temp.getDurability() + 25));
					dagger.setItemStack(temp);
					Player player = event.getPlayer();
					dagger.setPickupDelay(9999);
					dagger.setVelocity(player.getLocation().getDirection().multiply(4.5));
					
					player.getWorld().playSound(player.getLocation(), Sound.ENTITY_EGG_THROW, (float)1.0, (float)1.5);
					
					List<String> dlores = item.getItemMeta().getLore();
					
					int basedamage = 5;
					int bonus = 0;
					int basemultiplier = 1;
					int finalmultiplier = 1;
					
					if(player.getInventory().getChestplate() != null) {
						if(player.getInventory().getChestplate().getItemMeta().hasLore()) {
							List<String> lores = player.getInventory().getChestplate().getItemMeta().getLore();
							if(lores.contains("¡ì7KARATE")) {
								basemultiplier *= 2;
							}
						}
					}
					
					if(NecroAttack.ring.contains(player.getInventory().getItemInOffHand().getType())) {
						if(player.getInventory().getItemInOffHand().getItemMeta().hasLore()) {
							List<String> lores = player.getInventory().getItemInOffHand().getItemMeta().getLore();
							if(lores.contains("¡ì7WAR")) {
								bonus += 5;
							}
							else if(lores.contains("¡ì7WONDER")) {
								bonus += 10;
							}
						}
					}
					
					if(NecroSpell.beats.getOrDefault(player.getName(), 0) >= 1 && NecroSpell.beats.getOrDefault(player.getName(), 0) < 7) {
						bonus += 5;
						NecroSpell.beats.put(player.getName(), 0);
					}
					else if (NecroSpell.beats.getOrDefault(player.getName(), 0) >= 7){
						bonus += 15;
						NecroSpell.beats.put(player.getName(), 0);
					}
					
					if(dlores.contains("¡ì7BLOOD")) {
						basedamage = 5;
						if(event.getPlayer().getHealth() <= 6) {
							basedamage = 999;
						}
					}
					else if(dlores.contains("¡ì7TITANIUM")) {
						basedamage = 10;
					}
					else if(dlores.contains("¡ì7OBSIDIAN")) {
						basedamage = NecroAttack.coinmult.getOrDefault(player.getName(), 5);
					}
					else if(dlores.contains("¡ì7GLASS")) {
						basedamage = 20;
					}
					else if(dlores.contains("¡ì7JEWEL")) {
						basedamage = 25;
					}
					
					
					
					damage(((basedamage*basemultiplier)+bonus)*finalmultiplier, dagger, player);
				}
			}
		}
	}
	
	
	public void damage(double damage, Item dagger, Player player) {
		List<UUID> damaged = new ArrayList<UUID>();
		damaged.add(player.getUniqueId());
		
		BukkitRunnable damager = new BukkitRunnable() {

			@Override
			public void run() {
				if(dagger.isOnGround()) {
					boolean itembreak = false;
					this.cancel();
					if(dagger.getItemStack().getItemMeta().getLore().contains("¡ì7GLASS")) {
						itembreak = true;
					}
					else if(dagger.getItemStack().getDurability() > dagger.getItemStack().getType().getMaxDurability()) {
						itembreak = true;
					}
					else {
						dagger.setPickupDelay(0);
					}
					
					if(itembreak) {
						dagger.remove();
						dagger.getWorld().spawnParticle(Particle.ITEM_CRACK, dagger.getLocation(), 20, 0.2,0.2,0.2,0.1,dagger.getItemStack());
						dagger.getWorld().playSound(dagger.getLocation(), Sound.BLOCK_GLASS_BREAK, 1.0f, 1.0f);
						dagger.getWorld().playSound(dagger.getLocation(), Sound.BLOCK_METAL_BREAK, 1.0f, 1.0f);
					}

				}
				else {
					for(Entity e : dagger.getNearbyEntities(2, 2, 2)) {
						if(e instanceof LivingEntity) {
							if(damaged.contains(e.getUniqueId())) {
								
							}
							else {
								damaged.add(e.getUniqueId());
								((LivingEntity) e).damage(damage, player);
							}

						}
					}
					dagger.getWorld().spawnParticle(Particle.END_ROD, dagger.getLocation(), 15, 0.2,0.2,0.2,0);
				}
				
			}
			
		};
		
		damager.runTaskTimer(Bukkit.getPluginManager().getPlugin("NecroWeapon"), 0, 1);
	}
	
} 
