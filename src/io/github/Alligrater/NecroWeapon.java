package io.github.Alligrater;

import org.bukkit.plugin.java.JavaPlugin;

public class NecroWeapon extends JavaPlugin{
	@Override
	public void onEnable() {
		getServer().getPluginManager().registerEvents(new NecroAttack(), this);
		getServer().getPluginManager().registerEvents(new NecroSpell(), this);
	}
	
	@Override
	public void onDisable() {
		
	}
}
