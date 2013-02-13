In order to run the application with ApacheRiver put those in your running configuration vm arguments:

-Djava.security.policy=${project_loc}\start.policy
-Djava.rmi.server.RMIClassLoaderSpi=net.jini.loader.pref.PreferredClassProvider