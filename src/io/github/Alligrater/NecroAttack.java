package io.github.Alligrater;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

public class NecroAttack implements Listener {
	HashMap<String, Integer> bloodattacks = new HashMap<String, Integer>();
	HashMap<String, Integer> coinmult = new HashMap<String, Integer>();
	
	private final Set<Material> armor = EnumSet.of(
		    //NECRODANCER FLAIL
		    Material.CHAINMAIL_CHESTPLATE,
		    Material.DIAMOND_CHESTPLATE,
		    Material.IRON_CHESTPLATE,
		    Material.GOLD_CHESTPLATE,
		    Material.LEATHER_CHESTPLATE
    );
	
	private final Set<Material> ring = EnumSet.of(
		    //NECRODANCER FLAIL
		    Material.RECORD_3,
		    Material.RECORD_4,
		    Material.RECORD_5,
		    Material.RECORD_6,
		    Material.RECORD_7,
		    Material.RECORD_8,
		    Material.RECORD_9,
		    Material.RECORD_10,
		    Material.RECORD_11,
		    Material.RECORD_12,
		    Material.GOLD_RECORD,
		    Material.GREEN_RECORD
    );
	
	private final Set<Material> weapon = EnumSet.of(
			//NECRODANCER LONGSOWRD
		    Material.DIAMOND_SWORD,
		    Material.GOLD_SWORD,
		    Material.IRON_SWORD,
		    Material.STONE_SWORD,
		    Material.WOOD_SWORD,
		    //NECRODANCER AXE
		    Material.DIAMOND_AXE,
		    Material.GOLD_AXE,
		    Material.IRON_AXE,
		    Material.STONE_AXE,
		    Material.WOOD_AXE,
		    //NECRODANCER HARP
		    Material.DIAMOND_HOE,
		    Material.GOLD_HOE,
		    Material.IRON_HOE,
		    Material.STONE_HOE,
		    Material.WOOD_HOE,
		    Material.DIAMOND_PICKAXE,
		    Material.GOLD_PICKAXE,
		    Material.IRON_PICKAXE,
		    Material.STONE_PICKAXE,
		    Material.WOOD_PICKAXE
	);
	
	

    

		   
	@EventHandler(priority = EventPriority.LOW)
	public void onNecroWeaponAttack(EntityDamageByEntityEvent event) {
		if(event.getDamager().getType().equals(EntityType.PLAYER) && event.getEntity() instanceof LivingEntity) {
			Player player = (Player)event.getDamager();
			LivingEntity entity = (LivingEntity)event.getEntity();
			if(player.getInventory().getItemInMainHand().getType() != Material.AIR) {
				if(player.getInventory().getItemInMainHand().getItemMeta().hasLore() && weapon.contains(player.getInventory().getItemInMainHand().getType())) {
					List<String> lores = player.getInventory().getItemInMainHand().getItemMeta().getLore();
					if(haslore(lores, "¡ì7GLASS")) {
						event.setDamage(40);
					}
					else if(haslore(lores, "¡ì7BLOOD")) {
							if(bloodattacks.containsKey(player.getName())) {
								
								if(bloodattacks.get(player.getName()) >= 4) {
									bloodattacks.put(player.getName(), 0);
									if(player.getHealth() + 2 < player.getMaxHealth()) {
										player.setHealth(player.getHealth() + 2);
									}
									else {
										player.setHealth(player.getMaxHealth());
									}
								}
								
								else {
									bloodattacks.put(player.getName(), bloodattacks.get(player.getName()) + 1);
								}
								
							}
							else {
								bloodattacks.put(player.getName(), 1);
							}
							if(player.getHealth() <= 6) {
								event.setDamage(999);
							}
							else {
								event.setDamage(5);
							}
					}
					else if(haslore(lores, "¡ì7FROST")) {
						
						boolean isfrozen = false;
						for(PotionEffect pe: entity.getActivePotionEffects()) {
							if(pe.getAmplifier() == 9 && pe.getType().equals(PotionEffectType.SLOW)) {
								isfrozen = true;
							}
						}
						if(isfrozen) {
							event.setDamage(999);
						}
						else {
							entity.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 40, 9, true, true, Color.AQUA), true);
							event.setDamage(5);
						}
					}
					else if(haslore(lores, "¡ì7TITANIUM")) {
						event.setDamage(10);
					}
					else if(haslore(lores, "¡ì7OBSIDIAN")) {
						if(coinmult.containsKey(player.getName())) {
							if(coinmult.get(player.getName()) == 0) {
								player.playSound(player.getLocation(), Sound.BLOCK_NOTE_HARP, 1.0f, 0.6f);
								event.setDamage(5);
							}
							else if(coinmult.get(player.getName()) >= 1 && coinmult.get(player.getName()) <= 4) {
								player.playSound(player.getLocation(), Sound.BLOCK_NOTE_HARP, 1.0f, 0.8f);
								event.setDamage(10);
							}
							else if (coinmult.get(player.getName()) >= 5) {
								player.playSound(player.getLocation(), Sound.BLOCK_NOTE_HARP, 1.0f, 1.0f);
								event.setDamage(15);
							}
							if(event.getFinalDamage() >= entity.getHealth()) {
								coinmult.put(player.getName(), coinmult.get(player.getName()) + 1);
							}
							else {
								
							}
						}
						else {
							if(event.getFinalDamage() >= entity.getHealth()) {
								coinmult.put(player.getName(), 1);
							}
							else {
								coinmult.put(player.getName(), 0);
							}
						}
					}
					
					if(haslore(lores, "¡ì7RAPIER")) {
						if(entity.getLocation().distance(player.getLocation()) >= 2) {
							event.setDamage(event.getDamage() * 2);
							Vector playerPos = player.getLocation().toVector();
							Vector mobPos = entity.getLocation().toVector();
							Vector newVelocity = (mobPos.subtract(playerPos)).normalize().multiply(1);
							player.setVelocity(newVelocity);
						}
					}
			}


			}
			if(player.getInventory().getChestplate() != null) {
				if(player.getInventory().getChestplate().getItemMeta().hasLore()) {
					List<String> lores = player.getInventory().getChestplate().getItemMeta().getLore();
					if(haslore(lores, "¡ì7KARATE")) {
						event.setDamage(event.getDamage()*2);
					}
				}
			}
			
			if(ring.contains(player.getInventory().getItemInOffHand().getType())) {
				if(player.getInventory().getItemInOffHand().getItemMeta().hasLore()) {
					List<String> lores = player.getInventory().getItemInOffHand().getItemMeta().getLore();
					if(haslore(lores, "¡ì7COURAGE") && event.getFinalDamage() >= entity.getHealth()) {
						Vector playerPos = player.getLocation().toVector();
						Vector mobPos = entity.getLocation().toVector();
						Vector newVelocity = (mobPos.subtract(playerPos)).normalize().multiply(1);
						player.setVelocity(newVelocity);
						player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 20, 9), true);
						player.playSound(player.getLocation(), Sound.ITEM_FIRECHARGE_USE, (float)1.0, (float)1.0);
					}
					else if(haslore(lores,"¡ì7WAR")) {
						Vector playerPos = player.getLocation().toVector();
						Vector mobPos = entity.getLocation().toVector();
						Vector newVelocity = (mobPos.subtract(playerPos)).normalize().multiply(1);
						entity.setVelocity(newVelocity);
						event.setDamage(event.getDamage() + 5);
					}
				}
			}
		}
		
		

	}
	
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerTakeDamage(EntityDamageEvent event) {
		if(event.getEntity().getType().equals(EntityType.PLAYER)) {
			Player player = (Player)event.getEntity();
			
			boolean isInvulnerable = false;
			for(PotionEffect pe: player.getActivePotionEffects()) {
				if(pe.getAmplifier() == 9 && pe.getType().equals(PotionEffectType.DAMAGE_RESISTANCE)) {
					isInvulnerable = true;
				}
			}
			if(isInvulnerable) {
				event.setCancelled(true);
				player.playSound(player.getLocation(), Sound.ITEM_SHIELD_BLOCK, 1.0f, 1.0f);
			}
			else if(player.getInventory().getChestplate() != null) {
				if(player.getInventory().getChestplate().getItemMeta().hasLore()) {
					
					
					List<String> lores = player.getInventory().getChestplate().getItemMeta().getLore();
					if(haslore(lores, "¡ì7KARATE")) {
						event.setDamage(event.getDamage()*2);
						removeGlass(player);
						
					}
					else if(haslore(lores, "¡ì7GLASS")) {
						event.setCancelled(true);
						if(player.getInventory().getChestplate().getAmount() > 1) {
							player.getInventory().getChestplate().setAmount(player.getInventory().getChestplate().getAmount() - 1);
						}
						else {
							player.getInventory().setChestplate(null);
							
						}
						player.playSound(player.getLocation(), Sound.BLOCK_GLASS_BREAK, (float)1.0, (float)1.0);
					}
					else if(haslore(lores, "¡ì7QUARTZ")) {
						
						event.setDamage(4);
						removeGlass(player);
						
					}
				}
			}
			else if(ring.contains(player.getInventory().getItemInOffHand().getType())) {
				if(player.getInventory().getItemInOffHand().getItemMeta().hasLore()) {
					List<String> lores = player.getInventory().getItemInOffHand().getItemMeta().getLore();
					if(haslore(lores, "¡ì7SHIELDING")) {
						event.setCancelled(true);
						player.playSound(player.getLocation(), Sound.ITEM_SHIELD_BREAK, (float)1.0, (float)1.0);
						player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 360, 9), true);
						player.getInventory().setItemInOffHand(null);
					}
					else {
						removeGlass(player);
					}
				}

			}
			else {
				removeGlass(player);
			}
			
			if(coinmult.containsKey(player.getName()) && !event.isCancelled()) {
				coinmult.put(player.getName(), 0);
			}
		}
	}
	
	
	public boolean haslore(List<String> lores, String lore) {
		boolean haslore = false;
		for(String lines: lores) {
			if(lines.equals(lore)) {
				haslore = true;
			}
		}
		return haslore;
	}
	
	public void removeGlass(Player player) {
		for(ItemStack item : player.getInventory().getContents()) {
			if(item != null) {
				if(item.getItemMeta().hasLore() && !armor.contains(item.getType())) {
					List<String> lores = item.getItemMeta().getLore();
					if(haslore(lores, "¡ì7GLASS") ) {
						player.getInventory().remove(item);
						player.playSound(player.getLocation(), Sound.BLOCK_GLASS_BREAK, (float)1.0, (float)1.0);
					}
				}
			}

		}

	}
}
