package io.github.Alligrater;

import java.util.Date;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

public class NecroAttack implements Listener {
	HashMap<String, Integer> bloodattacks = new HashMap<String, Integer>();
	static HashMap<String, Integer> coinmult = new HashMap<String, Integer>();
	static HashMap<String, Long> cutlasscd = new HashMap<String, Long>();
	
	private final Set<Material> armor = EnumSet.of(
		    //NECRODANCER FLAIL
		    Material.CHAINMAIL_CHESTPLATE,
		    Material.DIAMOND_CHESTPLATE,
		    Material.IRON_CHESTPLATE,
		    Material.GOLD_CHESTPLATE,
		    Material.LEATHER_CHESTPLATE
    );
	
	static final Set<Material> ring = EnumSet.of(
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
	
	static final Set<Material> weapon = EnumSet.of(
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
	
	

    

		   
	@EventHandler(priority = EventPriority.LOWEST)
	public void onNecroWeaponAttack(EntityDamageByEntityEvent event) {
		if(event.getDamager().getType().equals(EntityType.PLAYER) && event.getEntity() instanceof LivingEntity) {
			Player player = (Player)event.getDamager();
			LivingEntity entity = (LivingEntity)event.getEntity();
			
			double basedamage = event.getDamage();
			double bonus = 0;
			double basemultiplier = 1;
			double finalmultiplier = 1;
			double reductionmultiplier = 1;
			
			if(getoriginaldamage(player.getInventory().getItemInMainHand().getType()) == 0) {
				reductionmultiplier = 1;
			}
			else {
				reductionmultiplier = event.getDamage() / getoriginaldamage(player.getInventory().getItemInMainHand().getType());
			}
			 
			
			
			if(player.getInventory().getChestplate() != null) {
				if(player.getInventory().getChestplate().getItemMeta().hasLore()) {
					List<String> lores = player.getInventory().getChestplate().getItemMeta().getLore();
					if(lores.contains("¡ì7KARATE")) {
						basemultiplier *= 2;
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
			
			if(player.getInventory().getItemInMainHand().getType() != Material.AIR) {
				
				ItemStack item = player.getInventory().getItemInMainHand();
				
				
				if(item.getItemMeta().hasLore() && weapon.contains(item.getType())) {
					List<String> lores = player.getInventory().getItemInMainHand().getItemMeta().getLore();
					
					//base damage calculation
					if(lores.contains("¡ì7GLASS")) {
						basedamage = 20;
					}
					else if(lores.contains("¡ì7BLOOD")) {

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
							basedamage = 999;
						}
						else {
							basedamage = 5;
						}
					}
					else if(lores.contains("¡ì7JEWEL")) {
						basedamage = 25;
					}
					else if(lores.contains("¡ì7TITANIUM")) {
						basedamage = 10;
					}
					else if(lores.contains("¡ì7FROST")) {
						basedamage = 5;
						boolean isfrozen = false;
						for(PotionEffect pe: entity.getActivePotionEffects()) {
							if(pe.getAmplifier() == 9 && pe.getType().equals(PotionEffectType.SLOW)) {
								isfrozen = true;
							}
						}
						if(isfrozen) {
							basedamage = 999;
						}
						else {
							entity.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 40, 9, true, true, Color.AQUA), true);
							basedamage = 5;
						}
					}
					else if(lores.contains("¡ì7OBSIDIAN")) {
						if(coinmult.containsKey(player.getName())) {
							if(coinmult.get(player.getName()) == 0) {
								player.playSound(player.getLocation(), Sound.BLOCK_NOTE_HARP, 1.0f, 0.6f);
								basedamage = 5;
							}
							else if(coinmult.get(player.getName()) >= 1 && coinmult.get(player.getName()) <= 4) {
								player.playSound(player.getLocation(), Sound.BLOCK_NOTE_HARP, 1.0f, 0.8f);
								basedamage = 10;
							}
							else if (coinmult.get(player.getName()) >= 5) {
								player.playSound(player.getLocation(), Sound.BLOCK_NOTE_HARP, 1.0f, 1.0f);
								basedamage = 15;
							}
							if(event.getFinalDamage() >= entity.getHealth()) 
								coinmult.put(player.getName(), coinmult.get(player.getName()) + 1);
						}
						else {
							if(event.getFinalDamage() >= entity.getHealth()) {
								coinmult.put(player.getName(), 1);
							}
							else {
								coinmult.put(player.getName(), 0);
							}
							basedamage = 5;
						}
					}
					else {
						basedamage = 5;
					}

					
					
					
					if(lores.contains("¡ì7RAPIER")) {
						if(entity.getLocation().distance(player.getLocation()) >= 2) {
							basemultiplier *= 2;
							Vector playerPos = player.getLocation().toVector();
							Vector mobPos = entity.getLocation().toVector();
							Vector newVelocity = (mobPos.subtract(playerPos)).normalize().multiply(1);
							player.setVelocity(newVelocity);
						}
					}
					else if(lores.contains("¡ì7DAGGER")) {
						if(event.getEntity().getLocation().distance(player.getLocation()) > 2) {
							event.setCancelled(true);
						}
					}
					else if(lores.contains("¡ì7CUTLASS")) {
						if(reductionmultiplier > 0.9)
							player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 16, 5), true);
					}
				}
			}
			
			event.setDamage(((basedamage*basemultiplier)+bonus)*finalmultiplier*reductionmultiplier);
			
			//ring calculation
			if(ring.contains(player.getInventory().getItemInOffHand().getType())) {
				if(player.getInventory().getItemInOffHand().getItemMeta().hasLore()) {
					List<String> lores = player.getInventory().getItemInOffHand().getItemMeta().getLore();
					if(lores.contains("¡ì7COURAGE") && event.getFinalDamage() >= entity.getHealth()) {
						if(event.isCancelled()) {
							
						}
						else {
							Vector playerPos = player.getLocation().toVector();
							Vector mobPos = entity.getLocation().toVector();
							Vector newVelocity = (mobPos.subtract(playerPos)).normalize().multiply(1);
							player.setVelocity(newVelocity);
							player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 40, 9), true);
							player.getWorld().playSound(player.getLocation(), Sound.ITEM_FIRECHARGE_USE, (float)1.0, (float)1.2);
						}

					}
					else if(lores.contains("¡ì7WAR")) {
						Vector playerPos = player.getLocation().toVector();
						Vector mobPos = entity.getLocation().toVector();
						Vector newVelocity = (mobPos.subtract(playerPos)).normalize().multiply(1);
						entity.setVelocity(newVelocity);
						bonus += 5;
					}
					else if(lores.contains("¡ì7WONDER")) {
						bonus += 10;
					}
				}
			}
			
			event.setDamage(((basedamage*basemultiplier)+bonus)*finalmultiplier*reductionmultiplier);
			
		}
		
		

	}
	
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerTakeDamage(EntityDamageEvent event) {
		if(event.getEntity().getType().equals(EntityType.PLAYER)) {
			Player player = (Player)event.getEntity();
			
			boolean isInvulnerable = false;
			boolean isparry = false;
			boolean removeglass = true;
			for(PotionEffect pe: player.getActivePotionEffects()) {
				if(pe.getAmplifier() == 9 && pe.getType().equals(PotionEffectType.DAMAGE_RESISTANCE)) {
					isInvulnerable = true;
					removeglass = false;
				}
				else if(pe.getAmplifier() == 5 && pe.getType().equals(PotionEffectType.DAMAGE_RESISTANCE)) {
					if(event.getCause() == DamageCause.ENTITY_ATTACK || event.getCause() == DamageCause.CUSTOM) {
						isparry = true;
						removeglass = false;
					}

				}
			}
			
			if(event.isCancelled()) {
				isInvulnerable = true;
				removeglass = false;
			}
			
			if(isInvulnerable) {
				event.setCancelled(true);
				player.getWorld().playSound(player.getLocation(), Sound.ITEM_SHIELD_BLOCK, 1.0f, 1.0f);
				return;
			}
			
			if(isparry) {
				event.setCancelled(true);
				player.getWorld().playSound(player.getLocation(), Sound.BLOCK_ANVIL_PLACE, 0.5f, 1.8f);
				player.setVelocity(player.getLocation().getDirection().multiply(-0.7));
				return;
			}
			
			
			
			if(player.getInventory().getChestplate() != null) {
				if(player.getInventory().getChestplate().getItemMeta().hasLore()) {
					
					
					List<String> lores = player.getInventory().getChestplate().getItemMeta().getLore();
					if(lores.contains("¡ì7KARATE")) {
						event.setDamage(event.getDamage()*2);
						
					}
					else if(lores.contains("¡ì7GLASS")) {
						event.setCancelled(true);
						removeglass = false;
						if(player.getInventory().getChestplate().getAmount() > 1) {
							player.getInventory().getChestplate().setAmount(player.getInventory().getChestplate().getAmount() - 1);
						}
						else {
							player.getInventory().setChestplate(null);
							
						}
						player.playSound(player.getLocation(), Sound.BLOCK_GLASS_BREAK, (float)1.0, (float)1.0);
					}
					else if(lores.contains("¡ì7QUARTZ")) {
						if(event.getDamage() >= 4) {
							event.setDamage(4);
						}
						else {
							event.setDamage(event.getDamage());
						}
						
					}
				}
			}
			
			if(ring.contains(player.getInventory().getItemInOffHand().getType())) {
				if(player.getInventory().getItemInOffHand().getItemMeta().hasLore()) {
					List<String> lores = player.getInventory().getItemInOffHand().getItemMeta().getLore();
					if(lores.contains("¡ì7SHIELDING") ) {
						event.setCancelled(true);
						player.getWorld().playSound(player.getLocation(), Sound.ITEM_SHIELD_BREAK, (float)1.0, (float)1.0);
						player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 360, 9), true);
						player.getInventory().setItemInOffHand(null);
						removeglass = false;
						return;
						
					}
					if(lores.contains("¡ì7REGEN")) {
						if(player.getHealth() - event.getDamage() < 1) {
							event.setDamage(0);
							player.setHealth(1.0);
							player.getWorld().playSound(player.getLocation(), Sound.ITEM_SHIELD_BREAK, (float)1.0, (float)1.0);
							player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 40, 9), true);
							player.getInventory().setItemInOffHand(null);
						}

					}
					else if(lores.contains("¡ì7BECOMING")){
						event.setDamage(999);
					}
					else if(lores.contains("¡ì7WONDER")) {
						if(event.getDamage() - 4 < 0.5) {
							if(event.getDamage() == 0) {
								
							}
							else {
								event.setDamage(0.5);
							}

						}
						else {
							event.setDamage(event.getDamage() - 4);
						}
					}
					else {
					}
				}

			}
			
			if(coinmult.containsKey(player.getName()) && !event.isCancelled()) {
				coinmult.put(player.getName(), 0);
			}
			
			if(removeglass = true) {
				removeGlass(player);
			}
		}
	}
	
	
	
	public void removeGlass(Player player) {
		for(ItemStack item : player.getInventory().getContents()) {
			if(item != null) {
				if(item.getItemMeta().hasLore() && !armor.contains(item.getType())) {
					List<String> lores = item.getItemMeta().getLore();
					if(lores.contains("¡ì7GLASS")) {
						player.getInventory().remove(item);
						player.playSound(player.getLocation(), Sound.BLOCK_GLASS_BREAK, (float)1.0, (float)1.0);
					}
				}
			}

		}

	}
	
	public double getoriginaldamage(Material mat) {
		String matname = mat.name();
		double dmg = 0;
		if(matname.contains("SWORD")) {
			if(matname.contains("DIAMOND")) {
				dmg = 7;
			}
			else if(matname.contains("GOLD")) {
				dmg = 4;
			}
			else if(matname.contains("IRON")) {
				dmg = 6;
			}
			else if(matname.contains("STONE")) {
				dmg = 5;
			}
			else if(matname.contains("WOOD")) {
				dmg = 4;
			}
		}
		else if(matname.contains("AXE")) {
			if(matname.contains("DIAMOND")) {
				dmg = 9;
			}
			else if(matname.contains("GOLD")) {
				dmg = 7;
			}
			else if(matname.contains("IRON")) {
				dmg = 9;
			}
			else if(matname.contains("STONE")) {
				dmg = 9;
			}
			else if(matname.contains("WOOD")) {
				dmg = 7;
			}
		}
		else if(matname.contains("HOE")) {
			dmg = 1;
		}
		else if(matname.contains("SPADE")) {
			if(matname.contains("DIAMOND")) {
				dmg = 5.5;
			}
			else if(matname.contains("GOLD")) {
				dmg = 2.5;
			}
			else if(matname.contains("IRON")) {
				dmg = 4.5;
			}
			else if(matname.contains("STONE")) {
				dmg = 3.5;
			}
			else if(matname.contains("WOOD")) {
				dmg = 2.5;
			}
		}
		else if(matname.contains("PICKAXE")) {
			if(matname.contains("DIAMOND")) {
				dmg = 5;
			}
			else if(matname.contains("GOLD")) {
				dmg = 2;
			}
			else if(matname.contains("IRON")) {
				dmg = 4;
			}
			else if(matname.contains("STONE")) {
				dmg = 3;
			}
			else if(matname.contains("WOOD")) {
				dmg = 2;
			}
		}
		
		return dmg;
	}
}
