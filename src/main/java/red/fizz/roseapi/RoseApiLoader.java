package red.fizz.roseapi;

import java.util.Arrays;
import java.util.List;

import static java.util.UUID.randomUUID;

public class RoseApiLoader {
    private final RoseAPI parent;

    public RoseApiLoader(RoseAPI parent) {
        this.parent = parent;
    }

    public void enable() {
        this.parent.webServer = new SparkWrapper();
        org.eclipse.jetty.util.log.Log.setLog(new NoLogging());
        this.parent.getCommand("apiadmin").setExecutor(new ApiCommand(this.parent));
        this.parent.getCommand("api").setExecutor(new ApiCommand(this.parent));
        loadWebServer();
    }

    public void disable() {
        parent.getLogger().info("[RoseAPI] Disabled Webserver");
    }

    public void loadWebServer() {
        this.parent.webServer.create(this.parent.config.getInt("server_port"), this.parent.config.getStringList("placeholders"), parent.isUsingMySQL());
        parent.getLogger().info("[RoseAPI] Enabled On Port " + this.parent.config.getInt("server_port"));
    }

}