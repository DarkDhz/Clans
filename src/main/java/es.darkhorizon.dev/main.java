package es.darkhorizon.dev;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.Listener;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import net.milkbowl.vault.economy.Economy;

public class main extends JavaPlugin implements Listener{
    private static Economy economy = null;	
	private static main plugin;
	public static main getPlugin() {return plugin;}
	private menus invmenu;
	private placeholders pl;
	ArrayList<String> Clans = new ArrayList<String>();		
	ArrayList<String> Chat = new ArrayList<String>();
	HashMap<String, String> pclans = new HashMap<String, String>();
	HashMap<String, String> Invites = new HashMap<String, String>();
	String plversion = "2.3";	
	ConsoleCommandSender console = Bukkit.getServer().getConsoleSender();
	private String key = "key=98BE0FE67F88AB82B4C197FAF1DC3B69206EFDCC4D3B80FC83A00037510B99B4&resource=";
	public void saveFile(File f, FileConfiguration file) {try {file.save(f);} catch (IOException exception) { exception.printStackTrace(); }}	
	public void SetStringPath(String path, String msg, File f, FileConfiguration file) {if (file.getString(path) == null) {file.set(path, msg);}}
	public void SetIntPath(String path, int msg, File f, FileConfiguration file) {if (file.getString(path) == null) {file.set(path, msg);}}
	public void SetDoublePath(String path, Double msg, File f, FileConfiguration file) {if (file.getString(path) == null) {file.set(path, msg);}}
	public void SetStringListPath(String path, ArrayList<String> msg, File f, FileConfiguration file) {if (file.getString(path) == null) {file.set(path, msg);}}
	private void versionChecker() {
		try {
			HttpURLConnection connection = (HttpURLConnection) new URL("http://www.spigotmc.org/api/general.php").openConnection();
			connection.setDoOutput(true);
			connection.setRequestMethod("POST");
			connection.getOutputStream().write((key + 42608).getBytes("UTF-8"));
			String version = new BufferedReader(new InputStreamReader(connection.getInputStream())).readLine();			
			if(!version.equals(plversion)) {
				console.sendMessage("§8§l§m===========================================================================");
				console.sendMessage("");
				console.sendMessage("§8[§4§lCLANS§r§8] §bYou're running and §c§lOLD§r §bVersion of Clans!");	
				console.sendMessage("§8[§4§lCLANS§r§8] §bPlease download the last Version at:");	
				console.sendMessage("§8[§4§lCLANS§r§8] §ehttps://www.spigotmc.org/resources/clans.42608/");	
				console.sendMessage("");
				console.sendMessage("§8§l§m===========================================================================");
			} else {				
				console.sendMessage("§8§l§m===========================================================================");
				console.sendMessage("");
				console.sendMessage("§8[§4§lCLANS§r§8] §7You're running the Version §b§l" + plversion + "§r §7of Clans!");	
				if (Bukkit.getServer().getPluginManager().isPluginEnabled("Vault")) {console.sendMessage("§8[§4§lCLANS§r§8] §7Plugin linked with §eVault§r§7!");}
				if (Bukkit.getServer().getPluginManager().isPluginEnabled("MVdWPlaceholderAPI")) {console.sendMessage("§8[§4§lCLANS§r§8] §7Plugin linked with §eMVdWPlaceholderAPI§r§7!");}		
				console.sendMessage("");
				console.sendMessage("§8§l§m===========================================================================");	
			}
		} catch (IOException e ) {
			this.getServer().getConsoleSender().sendMessage("§8§l§m===========================================================================");
			this.getServer().getConsoleSender().sendMessage("§8[§4§lCLANS§r§8] §7Connection error with spigotmc.org!");
			this.getServer().getConsoleSender().sendMessage("§8§l§m===========================================================================");
			e.printStackTrace();
		}
	}
	private void LoadPlaceholders() {
		pl = new placeholders(this);
		pl.ldpl();
	}
	@Override
	public void onEnable() {
		plugin = this;
		this.Clans.clear();		
		for (String clan : this.getConfig().getStringList("clanlist")) {this.Clans.add(clan);}		
		for (String clan : this.getConfig().getStringList("clanlist")) {for (String p : this.getConfig().getStringList("clan." +  clan + ".list")) {this.pclans.put(p.toString(), clan);}}
		// Tiene Clan >> (plugin.pclans.get(entity.getName().toString()) != null)
		// No tiene Clan >> (plugin.pclans.get(entity.getName().toString()) == null)
		// Obtener Clan >> plugin.pclans.get(p.getName().toString());
		setupEconomy();		
		Bukkit.getPluginManager().registerEvents(new event(this), this);
		Bukkit.getPluginCommand("clan").setExecutor(new clancmd(this));
		Bukkit.getPluginCommand("aclan").setExecutor(new aclancmd(this));	
		if (Bukkit.getServer().getPluginManager().isPluginEnabled("MVdWPlaceholderAPI")) {LoadPlaceholders();}
		Bukkit.getPluginManager().registerEvents(this, this);
			
		getConfig().set("version", plversion);
		saveConfig();
		reloadConfig();	
		File l = new File(Bukkit.getServer().getPluginManager().getPlugin("Clans").getDataFolder(), "lang.yml");	
		FileConfiguration lfile = YamlConfiguration.loadConfiguration(l);
		File s = new File(Bukkit.getServer().getPluginManager().getPlugin("Clans").getDataFolder(), "settings.yml");	
		FileConfiguration sfile = YamlConfiguration.loadConfiguration(s);
		File g = new File(Bukkit.getServer().getPluginManager().getPlugin("Clans").getDataFolder(), "gui.yml");	
		FileConfiguration gfile = YamlConfiguration.loadConfiguration(g);
		
		versionChecker();                                       		
		
		SetStringPath("global.prefix", "&8[&4&lCLANS&r&8] &r", l, lfile);
		SetStringPath("global.only_players", "&cOnly for players!", l, lfile);
		SetStringPath("global.no_clan", "&cYou need to be in a clan to use this command!", l, lfile);
		SetStringPath("global.rank", "&cYou need to be mod or owner of the clan to use this command!", l, lfile);
		SetStringPath("global.owner", "&cYou need to be owner of the clan to use this command!", l, lfile);
		SetStringPath("global.no_exist", "&cThis clan doesn't exist!", l, lfile);
		SetStringPath("global.perms", "&cYou don't have permissions!", l, lfile);
		SetStringPath("global.negative", "&cThis number can't be negative or 0!", l, lfile);
		SetStringPath("global.disabled", "&cThis is not enabled.", l, lfile);
		
		SetStringPath("help.commands", "&8&l»&r &9&lCommands:&r", l, lfile);
		SetStringPath("help.help_cmd", "&8&l»&r &b/clan help &7Show all clan commands.", l, lfile);
		SetStringPath("help.create_cmd", "&8&l»&r &b/clan create <clan> &7Create a clan.", l, lfile);
		SetStringPath("help.join_cmd", "&8&l»&r &b/clan join <clan> &7Join a clan.", l, lfile);
		SetStringPath("help.invite_cmd", "&8&l»&r &b/clan invite <player> &7Invite a player to join your clan.", l, lfile);
		SetStringPath("help.kick_cmd", "&8&l»&r &b/clan kick <player> &7Kick a player from your clan.", l, lfile);
		SetStringPath("help.info_cmd", "&8&l»&r &b/clan info <clan> &7Show info of a clan.", l, lfile);
		SetStringPath("help.list_cmd", "&8&l»&r &b/clan list &7List of all clans.", l, lfile);
		SetStringPath("help.chat_cmd", "&8&l»&r &b/clan chat &7Changes your chat mode.", l, lfile);
		SetStringPath("help.mode_cmd", "&8&l»&r &b/clan mode &7Changes the join mode of the clan.", l, lfile);
		SetStringPath("help.promote_cmd", "&8&l»&r &b/clan promote &7Promotes a member of the clan.", l, lfile);
		SetStringPath("help.demote_cmd", "&8&l»&r &b/clan demote &7Demotes a mod of the clan.", l, lfile);
		SetStringPath("help.pvp_cmd", "&8&l»&r &b/clan pvp &7Changes clan pvp mode.", l, lfile);
		SetStringPath("help.setbase_cmd", "&8&l»&r &b/clan setbase &7Sets the Clan base at your Location.", l, lfile);
		SetStringPath("help.delbase_cmd", "&8&l»&r &b/clan delbase &7Removes the Clan base.", l, lfile);
		SetStringPath("help.base_cmd", "&8&l»&r &b/clan base &7Teleports you to the Clan base.", l, lfile);
		
		SetStringPath("ahelp.commands", "&8&l»&r &9&lCommands:&r", l, lfile);
		SetStringPath("ahelp.help_cmd", "&8&l»&r &b/aclan help &7Show all admin commands.", l, lfile);
		SetStringPath("ahelp.slots_cmd", "&8&l»&r &b/aclan slots <set/default/add/remove> <int> &7Modify a clans slots.", l, lfile);
		SetStringPath("ahelp.level_cmd", "&8&l»&r &b/aclan level <> <int> &7Modify a clans level.", l, lfile);
		SetStringPath("ahelp.exp_cmd", "&8&l»&r &b/aclan exp <> <int> &7Modify a clans exp.", l, lfile);
		SetStringPath("ahelp.erase_cmd", "&8&l»&r &b/aclan erase <clan> &7Remove a clan.", l, lfile);
		SetStringPath("ahelp.reload_cmd", "&8&l»&r &b/aclan reload &7Reload plugin files.", l, lfile);
        
		SetStringPath("create.usage", "&7Correct usage: /clan create <clan>", l, lfile);
		SetStringPath("create.created", "&f%player% &7has created the clan: &c%clan%", l, lfile);
		SetStringPath("create.no_money", "&cYou don't have money to create a clan!", l, lfile);
		SetStringPath("create.long", "&cThe name of the clan is too long!", l, lfile);
		SetStringPath("create.short", "&cThe name of the clan is too short!", l, lfile);
		SetStringPath("create.exist", "&cThis clan is already created!", l, lfile);
		SetStringPath("create.has_clan", "&cYou're already a member from other clan!", l, lfile);	
		
		SetStringPath("join.usage", "&7Correct usage: /clan join <clan>", l, lfile);
		SetStringPath("join.joined", "&7You have joined: &b%clan%", l, lfile);
		SetStringPath("join.invitation", "&cYou need an invitation to join this clan!", l, lfile);
		SetStringPath("join.full", "&cThis clan is full!", l, lfile);
		SetStringPath("join.has_clan", "&cYou're already a member from other clan!", l, lfile);
		
		SetStringPath("invite.usage", "&7Correct usage: /clan invite <user>", l, lfile);
		SetStringPath("invite.invited", "&7You have invited &b%player% &7to your clan!", l, lfile);
		SetStringPath("invite.been_invited", "&7You have been invited to clan: &b%clan%", l, lfile);
		SetStringPath("invite.has_clan", "&cThis player is already in a clan!", l, lfile);
		SetStringPath("invite.offline", "&cThat player is not online!", l, lfile);
		
		SetStringPath("info.usage", "&7Correct usage: /clan info <clan>", l, lfile);
		
		SetStringPath("pvp.enabled", "&7PvP Mode: &6Enabled!", l, lfile);
		SetStringPath("pvp.disabled", "&7PvP Mode: &cDisabled!", l, lfile);
        
		SetStringPath("leave.left", "&cYou have left your clan!", l, lfile);
		
		SetStringPath("kick.usage", "&7Correct usage: /clan kick <user>", l, lfile);
		SetStringPath("kick.kicked", "&7You have kick &b%player% &7out of your clan!", l, lfile);
		SetStringPath("kick.yourself", "&cYou can't kick yourself!", l, lfile);
		SetStringPath("kick.no_member", "&cThis player is not in your clan!", l, lfile);
		
		SetStringPath("promote.usage", "&7Correct usage: /clan promote <user>", l, lfile);
		SetStringPath("promote.mod", "&cThat player is already a mod of your clan!", l, lfile);
		SetStringPath("promote.promoted", "&7That player is now a mod of your clan!", l, lfile);
		SetStringPath("promote.no_member", "&cThat player is not in your clan!", l, lfile);
        
		SetStringPath("demote.usage", "&7Correct usage: /clan demote <user>", l, lfile);
		SetStringPath("demote.no_mod", "&cThat player is not a mod of your clan!", l, lfile);
		SetStringPath("demote.demoted", "&7That player is now a member of your clan!", l, lfile);
		SetStringPath("demote.no_member", "&cThat player is not in your clan!", l, lfile);
		
		SetStringPath("mode.inv", "&7Clan Mode: &cInvitation!", l, lfile);
		SetStringPath("mode.free", "&7Clan Mode: &6Free!", l, lfile);
        
		SetStringPath("chat.enabled", "&7Clan chat: &6Enabled!", l, lfile);
		SetStringPath("chat.disabled", "&7Clan chat: &cDisabled!", l, lfile);
         
		SetStringPath("event.pvp.same_clan", "&b%player% &7is a member of your clan!", l, lfile);
		SetStringPath("event.pvp.levelup", "&c%clan% &7has reached level &b%level%&7!", l, lfile);
        
		SetStringPath("base.set", "&7You have set the Clan base to your location.", l, lfile);
		SetStringPath("base.unset", "&7You have removed the Clan base.", l, lfile);
		SetStringPath("base.no_base", "&cThe Clan does not have base.", l, lfile);
		SetStringPath("base.tp", "&cYou have been teleported to the Clan base.", l, lfile);
		
		SetStringPath("admin.slots.usage", "&7Correct usage: /aclan slots <clan> <set/default/add/remove> <int>!", l, lfile);
		SetStringPath("admin.slots.no_exist", "&cThat clan doesn't exist!", l, lfile);
		SetStringPath("admin.slots.less_default", "&cSlots can't be less than default slots number!", l, lfile);
		SetStringPath("admin.slots.int", "&cArgument 3 must be an integer!", l, lfile);
		SetStringPath("admin.slots.set", "&7Slots of clan &b%clan% &7are now &c%slots%&7!", l, lfile);
		SetStringPath("admin.slots.default", "&7Slots of clan &b%clan% &7are now the defaults!", l, lfile);
		SetStringPath("admin.slots.add", "&c%slots% &7slots have been added to clan &b%clan%&7!", l, lfile);
		SetStringPath("admin.slots.remove", "&c%slots% &7slots have been removed from clan &b%clan%&7!", l, lfile);
    
		SetStringPath("admin.level.usage", "&7Correct usage: /aclan level <clan> <set/reset/add/remove> <int>!", l, lfile);
		SetStringPath("admin.level.no_exist", "&cThat clan doesn't exist!", l, lfile);
		SetStringPath("admin.level.less", "&cClan level can't be less than 1!", l, lfile);
		SetStringPath("admin.level.set", "Nearly!", l, lfile);
		SetStringPath("admin.level.reset", "Nearly!", l, lfile);
		SetStringPath("admin.level.add", "Nearly!", l, lfile);
		SetStringPath("admin.level.remove", "Nearly!", l, lfile);
        
        SetStringPath("admin.exp.usage", "&7Correct usage: /aclan exp <clan> <set/reset/add/remove> <int>!", l, lfile);
		SetStringPath("admin.exp.no_exist", "&cThat clan doesn't exist!", l, lfile);
		SetStringPath("admin.exp.less", "&cClan exp can't be less than 0!", l, lfile);
		SetStringPath("admin.exp.set", "Nearly!", l, lfile);
		SetStringPath("admin.exp.reset", "Nearly!", l, lfile);
		SetStringPath("admin.exp.add", "Nearly!", l, lfile);
		SetStringPath("admin.exp.remove", "Nearly!", l, lfile);
        
        SetStringPath("admin.erase.usage", "&7Correct usage: /aclan erase <clan>", l, lfile);
		SetStringPath("admin.erase.no_exist", "&cThat clan doesn't exist!", l, lfile);
		SetStringPath("admin.erase.erased", "&7You've deleted the clan: &b%clan%&7!", l, lfile);
		
		SetDoublePath("cost", 100000.00, s, sfile);
		SetIntPath("slots", 6, s, sfile);
		SetIntPath("maxlvl", 100, s, sfile);
		SetIntPath("clan_min_lenght", 3, s, sfile);
		SetIntPath("clan_max_lenght", 6, s, sfile);
		SetStringPath("chat.format", "&8[&4%rank%&c%clan%&8] &r", s, sfile);
		SetStringPath("chat.owner_prefix", "**", s, sfile);
		SetStringPath("chat.mod_prefix", "*", s, sfile);
		SetStringPath("clan_chat.format", "&8[&f&l%clan%&r&8] &c%player% &8&l»&r &f%message%", s, sfile);
		
		ArrayList<String> Lore = new ArrayList<String>();
		
		SetStringPath("info.title", "&4&LCLAN", g, gfile);
		SetStringPath("info.members.name", "&a&LMembers &8[&c%members%&7/&c%slots%&8]", g, gfile);
		Lore = new ArrayList<String>();
        Lore.clear();
		Lore.add("&8");
		Lore.add("&7Click to view the list!");
		Lore.add("&8");
		SetStringListPath("info.members.lore", Lore, g, gfile);
		
		SetStringPath("info.lvl.name", "&6&LLevel &8[&c%level%&r&8]", g, gfile);
		Lore = new ArrayList<String>();
        Lore.clear();
        Lore.add("&8");
		Lore.add("&7Exp: &c%exp%&7/&c%lvlup%");
		Lore.add("&8");
		SetStringListPath("info.lvl.lore", Lore, g, gfile);
		
		SetStringPath("info.stats.name", "&c&LStats", g, gfile);
		Lore = new ArrayList<String>();
        Lore.clear();
        Lore.add("&8");
		Lore.add("&7Kills: &c%kills%");
		Lore.add("&7Death: &c%deaths%");
		Lore.add("&7KDR: &c%kdr%");
		Lore.add("&8");
		SetStringListPath("info.stats.lore", Lore, g, gfile);
		
		SetStringPath("members.title", "&4&LMEMBERS", g, gfile);
		SetStringPath("members.status.online", "&eOnline", g, gfile);
		SetStringPath("members.status.offline", "&cOffline", g, gfile);
		SetStringPath("members.back.name", "&c&LBack", g, gfile);
		
		Lore = new ArrayList<String>();
        Lore.clear();
		Lore.add("&8");
		Lore.add("&7Click to return to clan info!");
		Lore.add("&8");
		SetStringListPath("members.back.lore", Lore, g, gfile);
		
		SetStringPath("members.panel.name", "&f&LClan Members", g, gfile);
		Lore = new ArrayList<String>();
        Lore.clear();
        Lore.add("&8");
		Lore.add("&7Seeing Clan members!");
		Lore.add("&8");
		SetStringListPath("members.panel.lore", Lore, g, gfile);
		
		SetStringPath("members.member.name", "&a&L%member%", g, gfile);
		Lore = new ArrayList<String>();
        Lore.clear();
        Lore.add("&8");		 
		Lore.add("&7Status: %status%");
		Lore.add("&8");
		SetStringListPath("members.member.lore", Lore, g, gfile);
		
		SetStringPath("list.title", "&4&LCLANS&e&l", g, gfile);
		SetStringPath("list.next_pg.name", "&f&L»", g, gfile);
		Lore = new ArrayList<String>();
        Lore.clear();
        Lore.add("&8");
		Lore.add("&7Click to go to the next page!");
		Lore.add("&8");
		SetStringListPath("list.next_pg.lore", Lore, g, gfile);

		SetStringPath("list.return_pg.name", "&f&L«", g, gfile);
		Lore = new ArrayList<String>();
        Lore.clear();
        Lore.add("&8");
		Lore.add("&7Click to go back to another page!");
		Lore.add("&8");
		SetStringListPath("list.return_pg.lore", Lore, g, gfile);
		
		SetStringPath("list.clan.name", "Nearly!", g, gfile);
		Lore = new ArrayList<String>();
        Lore.clear();
        Lore.add("&8");
		Lore.add("&7Click to view more info!");
		Lore.add("&8");
		SetStringListPath("list.clan.lore", Lore, g, gfile);
		
		SetStringPath("list.none.name", "&cClan not Found", g, gfile);
		Lore = new ArrayList<String>();
        Lore.clear();
        Lore.add("&8");
		Lore.add("&7If you want to create a clan");
		Lore.add("&7use this command: &b&l/clan create <clan>");
		Lore.add("&8");
		Lore.add("&eCost: &f%cost%");
		Lore.add("&8");
		SetStringListPath("list.none.lore", Lore, g, gfile); 
		
		this.invmenu = new menus(this);
		if (this.getConfig().getString("bases_enabled") == null) {this.getConfig().set("bases_enabled", true);}
		this.saveFile(l, lfile);
		this.saveFile(s, sfile);
		this.saveFile(g, gfile);
		saveConfig();
		reloadConfig();					
	}
	@Override
	public void onDisable() {getLogger().info("[CLANS] Plugin Disabled!");}	
	public static Economy getEconomy() {return economy;}
	private boolean setupEconomy() {
        RegisteredServiceProvider<Economy> economyProvider = getServer().getServicesManager().getRegistration(net.milkbowl.vault.economy.Economy.class);
        if (economyProvider != null) {economy = economyProvider.getProvider();}
        return (economy != null);
    }	
	public menus getInvMenus() {return this.invmenu;}
}
