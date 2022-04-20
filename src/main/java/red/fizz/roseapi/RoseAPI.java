package red.fizz.roseapi;

import com.avaje.ebean.validation.NotNull;
import org.bstats.charts.SimplePie;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.bstats.bukkit.Metrics;

public final class RoseAPI extends JavaPlugin {

    private RoseApiLoader loader;
    FileConfiguration config = getConfig();
    SparkWrapper webServer;

    public boolean isUsingMySQL() {
        return config.getString("storage_type").equalsIgnoreCase("mysql");
    }

    public boolean showAfter() {
        return config.getBoolean("show_after");
    }

    @Override
    public void onEnable() {
        saveDefaultConfig();
        getLogger().info("RoseAPI is a fork of the original RestPlaceholderAPI by fredthedoggy. This fork is being maintained by fizzdev.");
        getLogger().info("This plugin is licensed under the GNU General Public License v3.0.");
        int pluginId = 14984;
        Metrics metrics = new Metrics(this, pluginId);
        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            if (isUsingMySQL()) {
                MySQLConnection.openConnection(this);
            }
            else {
                SQLiteConnection.openConnection(this);
            }
            for (String placeholder : config.getStringList("placeholders")) {
                getLogger().info("Registering placeholder: " + placeholder);
            }
            metrics.addCustomChart(new SimplePie("placeholders", () -> {
                return String.valueOf(getConfig().getConfigurationSection("placeholders").getKeys(false).size());
            }));
            metrics.addCustomChart(new SimplePie(("placeholderapi_version"), () -> {
                return Bukkit.getPluginManager().getPlugin("PlaceholderAPI").getDescription().getVersion();
            }));
            this.loader = new RoseApiLoader(this);
            this.loader.enable();
        } else {
            getLogger().severe("Could not find PlaceholderAPI! This plugin will be disabled now!");
            getLogger().severe("Please install PlaceholderAPI to use this plugin!");
            Bukkit.getPluginManager().disablePlugin(this);
        }
    }

    public RoseApiLoader getLoader() {
        return loader;
    }

    @Override
    public void onDisable() {
        getLogger().info("RoseAPI has now been disabled. Thank you for using my plugin!");
        if (isUsingMySQL()) {
            MySQLConnection.closeConnection(this);
        }
        else {
            SQLiteConnection.closeConnection(this);
        }
    }
}
