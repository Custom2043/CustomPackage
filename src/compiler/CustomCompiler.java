package compiler;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.DirectoryIteratorException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.ToolProvider;

import org.apache.commons.io.FileUtils;

import util.Logger;

/**
 * Based on InMemoryJavaCompiler : https://github.com/trung/InMemoryJavaCompiler
 */
public class CustomCompiler {
    JavaCompiler javac = ToolProvider.getSystemJavaCompiler();

    private String manifest = "";
    private String manifestClasspath = "Class-Path: ";
    private String compilerClasspath = "";
    private ZipOutputStream zos;
    private final String sources, classes, zip;

    public CustomCompiler(String folder, String generatedZipName, String mainClassName) throws IOException
    {
    	this(folder+generatedZipName, folder+"bin/", folder+"src/", mainClassName, folder+"lib/");
    }

    public CustomCompiler(String generatedZip, String generatedClass, String sourceFiles, String mainClassName, String ... libFolders) throws IOException
    {
    	this.zip = generatedZip;
    	this.sources = sourceFiles;
    	this.classes = generatedClass;

    	if (this.zip != null)
    	{
	    	this.zos = new ZipOutputStream(new FileOutputStream(this.zip));
	    	this.addProperties("Manifest-Version: 1.0");
	        this.addProperties("Created-By: 1.7.0_06 (Oracle Corporation)");
	        this.addProperties("Main-Class: "+mainClassName);
	        for (String s : libFolders)
	        	this.actOnRepertory(Act.addToLib, s);
	        this.addProperties(this.manifestClasspath);
	        Logger.debug("Manifest ClassPath : "+this.manifestClasspath, this.getClass());
	        Logger.debug("Manifest ClassPath : "+this.compilerClasspath, this.getClass());
    	}
    }
    public void compileSources()
    {
    	try {
			FileUtils.deleteDirectory(new File(this.classes));
		} catch (IOException e) {
			Logger.error(e, this.getClass());
		}
    	this.actOnRepertory(Act.compile, this.sources);
    }

    public void compile(String className, String sourceCodeInText) {
        SourceCode sourceCode = new SourceCode(className, sourceCodeInText);
        Iterable<? extends JavaFileObject> compilationUnits = Arrays.asList(sourceCode);
        ExtendedStandardJavaFileManager fileManager = new ExtendedStandardJavaFileManager(this.javac.getStandardFileManager(null, null, null), this.classes);
        JavaCompiler.CompilationTask task = this.javac.getTask(null, fileManager, null, Arrays.asList("-classpath", this.compilerClasspath+this.sources+";"+this.classes), null, compilationUnits);
        Logger.debug(task.call().toString(), this.getClass());
    }

    private void actOnRepertory(Act act, String basicReper, String reper)
    {
    	if (new File(basicReper+reper).exists())
    	{
	    	int nameCount = Paths.get(basicReper).getNameCount();
	    	try (DirectoryStream<Path> stream = Files.newDirectoryStream(Paths.get(basicReper + reper)))
			{
			    for (Path file: stream)
					if (Files.isDirectory(file))
			    		this.actOnRepertory(act, basicReper, file.subpath(nameCount, file.getNameCount()).toString());
			    	else if (Files.isRegularFile(file))
			    		this.executeAct(act, basicReper, file.subpath(nameCount, file.getNameCount()).toString(), this);
			}
	    	catch (IOException | DirectoryIteratorException x) {Logger.error(x, this.getClass());}
    	}
    }
    private void actOnRepertory(Act act, String basicReper)
    {
    	this.actOnRepertory(act, basicReper, "");
    }
    public void addClassPath(String folderPath, String relativePath)
    {
    	this.manifestClasspath += "lib/"+relativePath + " ";
    	this.compilerClasspath += folderPath + relativePath + ";";
    }
    public void addProperties(String s)
    {
    	this.manifest += s + System.lineSeparator();
    }
    public void generateZip() throws IOException
    {
    	if (this.zip != null)
    	{
    		this.zos.putNextEntry(new ZipEntry("META-INF/MANIFEST.MF"));
			this.zos.write(this.manifest.getBytes(StandardCharsets.UTF_8));
	        this.zos.closeEntry();
	        this.actOnRepertory(Act.zip, this.classes);
	        this.zos.finish();
	        this.zos.close();

	        FileChannel fc = FileChannel.open(Paths.get(this.zip));
	        fc.position(18);
	        fc.close();
    	}
    }
    private void executeAct(Act act, String folderPath, String relativePath, CustomCompiler comp) throws IOException
	{
		if (act == Act.compile)//compile
		{
	    	relativePath = relativePath.replace("\\", "/");
	    	folderPath = folderPath.replace("\\", "/");
	    	Logger.debug("Compile file : "+relativePath, this.getClass());
	   	 	this.compile(toClassName(relativePath), new String(Files.readAllBytes(Paths.get(folderPath + relativePath)), StandardCharsets.UTF_8));
		}
		if (act == Act.addToLib)
			comp.addClassPath(folderPath, relativePath.replace("\\", "/"));
		if (act == Act.zip)
		{
			this.zos.putNextEntry(new ZipEntry(relativePath.replace("\\", "/")));
    		this.zos.write(Files.readAllBytes(Paths.get(folderPath + relativePath)));
    		this.zos.closeEntry();
		}
	}
    public static String toClassName(String sourceFile)
    {
    	return sourceFile.substring(0, sourceFile.lastIndexOf(".")).replace("/", ".");
    }
    public static String toClassFile(String className)
    {
    	return className.replace(".", "/") + ".class";
    }
    public static enum Act
    {
    	compile,
    	addToLib,
    	zip;
    }
}
