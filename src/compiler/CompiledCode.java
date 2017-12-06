package compiler;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.net.URISyntaxException;

import javax.tools.SimpleJavaFileObject;

import util.Logger;

/**
 * Created by trung on 5/3/15.
 */
public class CompiledCode extends SimpleJavaFileObject {
    private FileOutputStream fos;

    public CompiledCode(String classesFolder, String className, Kind kind) throws IOException, URISyntaxException {
        super(new URI(className), kind);
        File f = new File(classesFolder + CustomCompiler.toClassFile(className));
        Logger.debug("Write in "+f);
        f.getParentFile().mkdirs();
        this.fos = new FileOutputStream(f);
    }

    @Override
    public OutputStream openOutputStream() throws IOException {
        return this.fos;
    }
}
