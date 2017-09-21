package util;

public class Logger
{
	private static boolean debug = false;
	private static boolean warning = true;
	private static boolean error = true;
	private static boolean excpetionPrintStack = true;

	private Logger(){}

	public static void error(Exception s, Class c)
	{
		error(s.toString(), c);
		if (Logger.excpetionPrintStack)
			s.printStackTrace();
	}
	public static void setLoggerProperties(boolean error, boolean warning, boolean debug, boolean exceptionPrintStack)
	{
		Logger.error = error;
		Logger.warning = warning;
		Logger.debug = debug;
		Logger.excpetionPrintStack = exceptionPrintStack;
	}
	public static void error(String s, Class c)
	{
		if (error)
		{
			System.err.println("Error from class "+c.getName()+" in package "+c.getPackage().getName());
			System.err.println("    "+s);
		}
	}

	public static void debug(String s, Class c)
	{
		if (debug)
		{
			System.out.println("Debug Message from class "+c.getName()+" in package "+c.getPackage().getName());
			System.out.println("    "+s);
		}
	}

	public static void warning(String s, Class c)
	{
		if (warning)
		{
			System.out.println("Warning from class "+c.getName()+" in package "+c.getPackage().getName());
			System.out.println("    "+s);
		}
	}
}
