package red.fizz.roseapi;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ApiCommand implements CommandExecutor {

    private final RoseAPI roseapi;

    public ApiCommand(RoseAPI roseapi) {
        this.roseapi = roseapi;
    }

    Utils utils = new Utils();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length >= 1 && args[0].equals("reload")) {
            roseapi.webServer.destroy();
            roseapi.getLogger().info("[RoseAPI] Disabled Webserver");
            this.roseapi.getLoader().loadWebServer();
            if (sender instanceof Player) {
                Player player = (Player) sender;
                player.sendMessage(ChatColor.GREEN + "RoseAPI Config is being reloaded.");
            }
            roseapi.getLogger().info("[RoseAPI] Reloading Config");
        } else if (args.length >= 1 && args[0].equals("new")) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                if (player.hasPermission("roseapi.use")) {
                    player.sendMessage(ChatColor.GREEN + "Resetting your key...");
                    Bukkit.getScheduler().runTaskAsynchronously(roseapi, () -> {
                        if (roseapi.isUsingMySQL()) {
                            String key = utils.createApiKey();
                            boolean keyWorked = MySQLConnection.newKey(String.valueOf(player.getUniqueId()), key);
                            if (keyWorked) {
                                player.sendMessage(ChatColor.GOLD + "Your API Key has been generated.");
                                TextComponent message = new TextComponent("Click me to copy your API key into chat.");
                                message.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, key));
                                message.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(ChatColor.RED + "IMPORTANT! Do NOT send the message, but copy and paste it somewhere safe. You can only view the key once.").create()));
                                player.spigot().sendMessage(message);
                            }
                            else {
                                player.sendMessage(ChatColor.RED + "An error occurred while generating your API key. Please contact an admin!");
                            }
                        }
                        else {
                            String key = utils.createApiKey();
                            boolean keyWorked = SQLiteConnection.newKey(String.valueOf(player.getUniqueId()), key);
                            if (keyWorked) {
                                player.sendMessage(ChatColor.GOLD + "Your API Key has been generated.");
                                TextComponent message = new TextComponent("Click me to copy your API key into chat.");
                                message.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, key));
                                message.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(ChatColor.RED + "IMPORTANT! Do NOT send the message, but copy and paste it somewhere safe. You can only view the key once.").create()));
                                player.spigot().sendMessage(message);
                            }
                            else {
                                player.sendMessage(ChatColor.RED + "An error occurred while generating your API key. Please contact an admin!");
                            }
                        }
                    });
                }
            }
        } else {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                if (roseapi.isUsingMySQL()) {
                    Bukkit.getScheduler().runTaskAsynchronously(roseapi, () -> {
                        if (MySQLConnection.getKey(String.valueOf(player.getUniqueId())) == "") {
                            if (!player.hasPermission("roseapi.use")) {
                                player.sendMessage(ChatColor.RED + "You do not have permission to use this command.");
                            }
                            else {
                                String key = utils.createApiKey();
                                boolean keyWorked = MySQLConnection.newKey(String.valueOf(player.getUniqueId()), key);
                                if (keyWorked) {
                                    player.sendMessage(ChatColor.GOLD + "Your API Key has been generated.");
                                    TextComponent message = new TextComponent("Click me to copy your API key into chat.");
                                    message.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, key));
                                    message.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(ChatColor.RED + "IMPORTANT! Do NOT send the message, but copy and paste it somewhere safe. You can only view the key once.").create()));
                                    player.spigot().sendMessage(message);
                                }
                                else {
                                    player.sendMessage(ChatColor.RED + "An error occurred while generating your API key. Please contact an admin!");
                                }
                            }
                        }
                        else {
                            if (roseapi.showAfter()) {
                                player.sendMessage(ChatColor.GOLD + "Your API Key: " + ChatColor.RED + MySQLConnection.getKey(String.valueOf(player.getUniqueId())));
                                TextComponent message = new TextComponent("Click me to copy your API key into chat.");
                                message.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, MySQLConnection.getKey(String.valueOf(player.getUniqueId()))));
                                message.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(ChatColor.RED + "IMPORTANT! Do NOT send the message, but copy and paste it somewhere safe. You can only view the key once.").create()));
                                player.spigot().sendMessage(message);
                            }
                            else {
                                player.sendMessage(ChatColor.GOLD + "You may not view your API key after the initial generation of it. You may reset your API key by using the command /api new.");
                            }
                        }
                    });
                }
                else {
                    Bukkit.getScheduler().runTaskAsynchronously(roseapi, () -> {
                        if (SQLiteConnection.getKey(String.valueOf(player.getUniqueId())) == "") {
                            if (!player.hasPermission("roseapi.use")) {
                                player.sendMessage(ChatColor.RED + "You do not have permission to use this command.");
                            }
                            else {
                                String key = utils.createApiKey();
                                boolean keyWorked = SQLiteConnection.newKey(String.valueOf(player.getUniqueId()), key);
                                if (keyWorked) {
                                    player.sendMessage(ChatColor.GOLD + "Your API Key has been generated.");
                                    TextComponent message = new TextComponent("Click me to copy your API key into chat.");
                                    message.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, key));
                                    message.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(ChatColor.RED + "IMPORTANT! Do NOT send the message, but copy and paste it somewhere safe. You can only view the key once.").create()));
                                    player.spigot().sendMessage(message);
                                }
                                else {
                                    player.sendMessage(ChatColor.RED + "An error occurred while generating your API key. Please contact an admin!");
                                }
                            }
                        } else {
                            if (roseapi.showAfter()) {
                                player.sendMessage(ChatColor.GOLD + "Your API Key: " + ChatColor.RED + SQLiteConnection.getKey(String.valueOf(player.getUniqueId())));
                                TextComponent message = new TextComponent("Click me to copy your API key into chat.");
                                message.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, SQLiteConnection.getKey(String.valueOf(player.getUniqueId()))));
                                message.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(ChatColor.RED + "IMPORTANT! Do NOT send the message, but copy and paste it somewhere safe. You can only view the key once.").create()));
                                player.spigot().sendMessage(message);
                            } else {
                                player.sendMessage(ChatColor.GOLD + "You may not view your API key after the initial generation of it. You may reset your API key by using the command /api new.");
                            }
                        }
                    });
                }
            } else {
                roseapi.getLogger().info("[RoseAPI] Run /api reload to Reload RoseAPI.");
                roseapi.getLogger().info("[RoseAPI] btw only players can generate keys.");
            }
        }
        return true;
    }
}