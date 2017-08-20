package io.github.Alligrater;

import org.bukkit.plugin.java.JavaPlugin;

public class NecroWeapon extends JavaPlugin{
	@Override
	public void onEnable() {
		getServer().getPluginManager().registerEvents(new NecroAttack(), this);
	}
	
	@Override
	public void onDisable() {
		
	}
}
