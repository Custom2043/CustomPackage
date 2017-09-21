package compiler;

import java.io.IOException;
import java.net.URI;

import javax.tools.SimpleJavaFileObject;

/**
 * Created by trung on 5/3/15.
 */
public class SourceCode extends SimpleJavaFileObject {
    private String contents = null;

    public SourceCode(String className, String contents) {
        super(URI.create("string:///" + className.replace('.', '/') + Kind.SOURCE.extension), Kind.SOURCE);
        this.contents = contents;
    }

    @Override
	public CharSequence getCharContent(boolean ignoreEncodingErrors) throws IOException {
        return this.contents;
    }
}
