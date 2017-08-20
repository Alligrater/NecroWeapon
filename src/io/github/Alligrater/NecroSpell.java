package io.github.Alligrater;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class NecroSpell implements Listener{
	public void onTomeUse(PlayerInteractEvent event) {
		if(event.getAction().equals(Action.RIGHT_CLICK_AIR) || event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
			Player player = event.getPlayer();
			if(event.getItem().getType().equals(Material.BOOK)) {
				if(event.getItem().getItemMeta().hasLore()) {
					List<String> lores = event.getItem().getItemMeta().getLore();
					if(haslore(lores, "¡ì7SHIELD")) {
						event.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 360, 9), true);
					}
				}
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
	
}
