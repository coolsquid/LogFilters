package coolsquid.logfilters;

import java.io.File;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import com.typesafe.config.ConfigValue;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.relauncher.IFMLCallHook;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;

@Mod(modid = LogFilters.MODID, name = LogFilters.NAME, version = LogFilters.VERSION, dependencies = LogFilters.DEPENDENCIES, updateJSON = LogFilters.UPDATE_JSON, acceptableRemoteVersions = "*")
public class LogFilters implements IFMLLoadingPlugin, IFMLCallHook {

	public static final String MODID = "logfilters";
	public static final String NAME = "LogFilters";
	public static final String VERSION = "1.0.2";
	public static final String DEPENDENCIES = "required-after:forge@[14.21.1.2387,)";
	public static final String UPDATE_JSON = "https://gist.githubusercontent.com/coolsquid/39d310b4b62141761164e854d1d452da/raw/logfilters.json";

	@Override
	public String[] getASMTransformerClass() {
		return null;
	}

	@Override
	public String getModContainerClass() {
		return null;
	}

	@Override
	public String getSetupClass() {
		return LogFilters.class.getName();
	}

	@Override
	public void injectData(Map<String, Object> data) {

	}

	@Override
	public String getAccessTransformerClass() {
		return null;
	}

	@Override
	public Void call() throws Exception {
		Logger internal = LogManager.getFormatterLogger(NAME);
		internal.info("Setting up log filters");
		File file = new File("config/LogFilters.conf");
		if (!file.exists()) {
			file.createNewFile();
		}
		Config config = ConfigFactory.parseFile(file);
		for (Entry<String, ConfigValue> e : config.entrySet()) {
			String key = e.getKey();
			Logger log = key.equals("root") ? LogManager.getRootLogger() : LogManager.getLogger(key.startsWith("\"") && key.endsWith("\"") ? key.substring(1, key.length() - 1) : key);
			if (log instanceof org.apache.logging.log4j.core.Logger) {
				for (Config filter : config.getConfigList(key)) {
					LogFilter logFilter;
					switch (filter.getString("type")) {
						case "prefix": {
							logFilter = new LogFilter.PrefixFilter(filter.getString("filter"));
							break;
						}
						case "suffix": {
							logFilter = new LogFilter.SuffixFilter(filter.getString("filter"));
							break;
						}
						case "regex": {
							logFilter = new LogFilter.RegexFilter(filter.getString("filter"));
							break;
						}
						case "equals": {
							logFilter = new LogFilter.EqualsFilter(filter.getString("filter"));
							break;
						}
						case "contains": {
							logFilter = new LogFilter.ContainsFilter(filter.getString("filter"));
							break;
						}
						default: {
							throw new RuntimeException("Unrecognized filter type " + filter.getString("type"));
						}
					}
					if (filter.hasPath("max_level")) {
						logFilter.setMaxLevel(Level.valueOf(filter.getString("max_level").toUpperCase()));
					}
					((org.apache.logging.log4j.core.Logger) log).addFilter(logFilter);
				}
			} else {
				internal.error("Logger %s [%s] is not supported", key, log.getClass().getName());
			}
		}
		return null;
	}
}
