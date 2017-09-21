package boot;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class Launcher
{
	public static Process launchJar(String name) throws IOException
	{
        ProcessBuilder pb = new ProcessBuilder();
        ArrayList<String> commands = new ArrayList<String>();

        commands.add(getJavaPath());
        if (System.getProperty("os.name").toLowerCase().contains("mac"))
            commands.addAll(Arrays.asList(getMacArgs(name)));
        commands.add("-jar");
        commands.add(name);

        String entireCommand = "";
        for(String cmd : commands)
            entireCommand += cmd + "\n";
        System.out.println(entireCommand);

        pb.directory(new File("./"));

        pb.command(commands);
        Process p = pb.start();
        return p;
    }
	private static String getJavaPath()
	{
        if (System.getProperty("os.name").toLowerCase().contains("win"))
            return "\"" + System.getProperty("java.home") + "/bin/java"
                    + "\"";
           return System.getProperty("java.home") + "/bin/java";
    }
	private static String[] getMacArgs(String name) {
        String[] macArgs = new String[] {
                "-Xdock:name="+name,
                "-XX:+UseConcMarkSweepGC",
                "-XX:+CMSIncrementalMode",
                "-XX:-UseAdaptiveSizePolicy"
        };

        return macArgs;
    }
}
