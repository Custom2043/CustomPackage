package util;

public class Logger
{
	private static boolean debug = false;
	private static boolean warning = true;
	private static boolean error = true;
	private static boolean exceptionPrintStack = true;

	private Logger(){}

	public static void error(Exception s)
	{
		if (error)
		{
			System.err.println("Error from class "+getCallingClass());
			System.err.println("    "+s);
		}
		if (Logger.exceptionPrintStack)
			s.printStackTrace();
	}
	public static void setLoggerProperties(boolean error, boolean warning, boolean debug, boolean exceptionPrintStack)
	{
		Logger.error = error;
		Logger.warning = warning;
		Logger.debug = debug;
		Logger.exceptionPrintStack = exceptionPrintStack;
	}
	public static void error(String s)
	{
		if (error)
		{
			System.err.println("Error from class "+getCallingClass());
			System.err.println("    "+s);
		}
	}

	public static void debug(String s)
	{
		if (debug)
		{
			System.out.println("\tDebug Message from "+getCallingClass());
			System.out.println("    "+s);
		}
	}

	public static void warning(String s)
	{
		if (warning)
		{
			System.out.println("/!\\ Warning from class "+getCallingClass());
			System.out.println("    "+s);
		}
	}
	private static String getCallingClass()
	{
		return new Exception().getStackTrace()[2].toString();
	}
}
