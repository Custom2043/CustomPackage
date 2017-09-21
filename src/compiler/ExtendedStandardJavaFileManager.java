package compiler;

import java.io.IOException;
import java.net.URISyntaxException;

import javax.tools.FileObject;
import javax.tools.ForwardingJavaFileManager;
import javax.tools.JavaFileManager;
import javax.tools.JavaFileObject;

import util.Logger;

/**
 * Created by trung on 5/3/15.
 */
public class ExtendedStandardJavaFileManager extends ForwardingJavaFileManager<JavaFileManager> {

	private String classesFolder;

    /**
     * Creates a new instance of ForwardingJavaFileManager.
     *
     * @param fileManager delegate to this file manager
     * @param cl
     */
    protected ExtendedStandardJavaFileManager(JavaFileManager fileManager, String cF) {
        super(fileManager);
        this.classesFolder = cF;
    }

    @Override
    public JavaFileObject getJavaFileForOutput(JavaFileManager.Location location, String className, JavaFileObject.Kind kind, FileObject sibling) throws IOException {
    	try {
			return new CompiledCode(this.classesFolder, className, kind);
		} catch (URISyntaxException e) {Logger.error(e, this.getClass());}
		return null;
    }
}
