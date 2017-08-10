package util;

public class OS 
{
	/**
	 * CURRENT_OS == -1: OS Inconnu 
	 * CURRENT_OS == 0 : Pour tout OS
	 */
	public static final String[] OSNAME = {"_all", "win", "mac", "sol", "lin"};
	public static final int CURRENT_OS = getOSID();
	public static final int NUMBER_OS = OSNAME.length;
	
	private static int getOSID()
	{
		String osName = System.getProperty("os.name");
		for (int i=1;i<NUMBER_OS;i++) // Commence à 1, ne compte pas all
			if (osName.contains(OSNAME[i]))
				return i;
		return NUMBER_OS +1;
	}
	public static String getOSName(int id)
	{
		if (id < NUMBER_OS && id > -1)
			return OSNAME[CURRENT_OS];
		else
			return "unknown";
	}
	public static boolean isOSAcceptable(int os)
	{
		if (os == 0)
			return true;
		if (os == CURRENT_OS)
			return true;
		return false;
	}
}
