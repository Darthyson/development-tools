package selfbus.debugger.misc;

import java.awt.EventQueue;
import java.awt.Toolkit;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractApplication
{
   private static final Logger LOGGER = LoggerFactory.getLogger(AbstractApplication.class);
   protected static AbstractApplication instance;
   private final Properties config = new Properties();
   private String osname;
   private File homeDir;
   private File tempDir;

   public static AbstractApplication getInstance()
   {
      return instance;
   }

   public void start()
   {
      instance = this;

      setupEnvironment();
      startup();

      EventQueue queue = Toolkit.getDefaultToolkit().getSystemEventQueue();
      while (queue.peekEvent() != null)
      {
         try
         {
            Thread.sleep(50L);
         }
         catch (InterruptedException localInterruptedException)
         {
         }
      }

      ready();
   }

   public void exit()
   {
      shutdown();
      Runtime.getRuntime().exit(0);
   }

   public Properties getConfig()
   {
      return config;
   }

   public void loadConfig(File configFile)
   {
      try
      {
         LOGGER.debug("Loading config {}", configFile);
         config.load(new FileInputStream(configFile));
      }
      catch (IOException e)
      {
         throw new RuntimeException("Failed to load configuration " + configFile, e);
      }
   }

   public void saveConfig(File configFile, String comment)
   {
      try
      {
         LOGGER.debug("Saving config {}", configFile);
         config.store(new FileOutputStream(configFile), comment);
      }
      catch (IOException e)
      {
         throw new RuntimeException("Failed to save configuration " + configFile, e);
      }
   }

   /**
    * The application starts.
    */
   protected abstract void startup();

   /**
    * The application shuts down.
    */
   protected abstract void shutdown();

   /**
    * The application finished startup.
    */
   protected void ready()
   {
   }
   
   public String getOS()
   {
      return this.osname;
   }

   public File getHomeDir()
   {
      return this.homeDir;
   }

   public File getTempDir()
   {
      return this.tempDir;
   }

   public String getAppDir(String appName)
   {
      if (this.osname.startsWith("windows"))
         return this.homeDir + File.separator + appName;
      return this.homeDir + File.separator + '.' + appName;
   }

   private void setupEnvironment()
   {
      String os = System.getProperty("os.name", "").toLowerCase();
      String envHomeDir;
      if (os.startsWith("windows"))
      {
         this.osname = "windows";
         this.tempDir = new File("c:/windows/temp");

         envHomeDir = System.getenv("USERPROFILE");
      }
      else
      {
         if (os.startsWith("linux"))
         {
            this.osname = "linux";
            this.tempDir = new File("/tmp");
            envHomeDir = System.getenv("HOME");
         }
         else
         {
            LOGGER.warn("Unknown operating system \"{}\", using generic settings for home and temp directory", os);

            this.osname = "other";
            this.tempDir = new File("/tmp");
            envHomeDir = System.getenv("HOME");
         }
      }
      if ((envHomeDir != null) && (!envHomeDir.isEmpty()))
         this.homeDir = new File(envHomeDir);
      else this.homeDir = this.tempDir;
   }
}
