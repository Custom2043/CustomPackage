package util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.DirectoryIteratorException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Hashtable;

import org.apache.commons.io.IOUtils;

public class Translator
{
	public static String currentFile = "English";
	public static Hashtable<String, String> table = new Hashtable<String, String>();
	public static String languagesRepertory = "Ressources/lang/";
	public static String[] languages;
	public static int languageNumber()
	{
		for (int i=0;i<languages.length;i++)
			if (languages[i].equals(currentFile))
				return i;
		return -1;
	}
	public static void loadLanguagesList(String reper)
	{
		String sss;
		ArrayList<String> s = new ArrayList<String>();
		languagesRepertory = reper;
    	try (DirectoryStream<Path> stream = Files.newDirectoryStream(Paths.get(reper)))
		{
		    for (Path file: stream)
				if (Files.isRegularFile(file) && file.toString().endsWith(".lang"))
		    	{
		    		sss = file.getFileName().toString();
		    		s.add(sss.substring(0, sss.length()-5));
		    	}
		}
    	catch (IOException | DirectoryIteratorException x) {}
    	languages = new String[s.size()];
    	int i=0;
		for (String ss : s)
		{
			languages[i] = ss;
			i++;
		}
	}
	public static void loadLangage(String lang)
	{
		table.clear();
		String key = "";
		String trad = "";
		int carac = 0;
		FileInputStream fis = null;
		try
		{
			fis = new FileInputStream(new File(languagesRepertory+lang+".lang"));
			currentFile = lang;
			while (carac != -1)
			{
				key = "";
				trad = "";
				carac = fis.read();
				while (carac != -1 && carac != '=')
				{
					key+=Character.toString((char)carac);
					carac = fis.read();
				}
				carac = fis.read();
				while (carac != -1 && carac != 13 && carac != 10)
				{
					trad+=Character.toString((char)carac);
					carac = fis.read();
				}
				if (carac == 13)
					carac = fis.read();
				table.put(key, trad);
			}
			System.out.println("Translator ; Successfully load langage : "+currentFile);
		}
		catch(IOException e){System.out.println("Translator ; Can't load langage : "+lang);}
		finally{
			if (fis != null)
				IOUtils.closeQuietly(fis);}
	}
	public static String translate(String key)
	{
		if (table.containsKey(key))
			return table.get(key);
		else
		{
			System.out.println("Translator ; Can't translate : "+key);
			return key;
		}
	}
}
