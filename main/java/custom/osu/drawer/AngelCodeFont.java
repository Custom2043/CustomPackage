package custom.osu.drawer;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.LinkedList;

import custom.osu.main.MainActivity;
import custom.osu.util.Color;
import custom.osu.util.QuadColor;
import custom.osu.util.ScreenCoor;
import custom.osu.util.TextureCoor;

public class AngelCodeFont
{
    private int lineHeight;
    private final TextureManager loader;
    public Character[] chars = new Character[65536];
    private boolean correctFont = true;
    public Texture[] pageTexts;
    public AngelCodeFont(TextureManager l, String fnt)
    {
        loader = l;
        try
        {
            InputStream fis = MainActivity.getAsset(fnt);
            byte[] b = new byte[(int)MainActivity.getFileDescriptor(fnt).getLength()];
            fis.read(b);
            fis.close();

            ByteArrayInputStream bis = new ByteArrayInputStream(b);
            skipTo("pages=", bis);
            pageTexts = new Texture[readNumber(bis)];
            for (int i=0;i<pageTexts.length;i++)
            {
                skipTo("file=\"", bis);
                pageTexts[i] = loader.loadTexture(readUntil('\"', bis));
            }

            int total = readAttribute("chars count=", bis);
            for (int i=0;i< total;i++) {
                int id = readAttribute("d=", bis);
                chars[id] = new Character(id, readAttribute("=", bis), readAttribute("=", bis), readAttribute("=", bis), readAttribute("=", bis)
                        , readAttribute("=", bis), readAttribute("=", bis), readAttribute("=", bis), readAttribute("=", bis));
                if (chars[id].textHeight > lineHeight)
                    lineHeight = chars[id].textHeight;
            }


            int kern = readAttribute("kernings count=", bis);
            if (kern != -1)
                for (int i = 0;i<kern;i++)
                {
                    int first = readAttribute("=", bis);
                    chars[readAttribute("=", bis)].kers.add(new Kerning(first, readAttribute("=", bis)));
                }
        }catch(Exception e){e.printStackTrace();correctFont = false;}
    }
    public void skipTo(String to, ByteArrayInputStream bis)
    {
        String current = "";
        char reading;
        while (!current.equals(to) && bis.available() > 0) {
            reading = (char)bis.read();
            if (to.charAt(current.length()) == reading)
                current += reading;
            else
                current = "";
        }
    }
    public String readUntil(char c, ByteArrayInputStream bis)
    {
        String s = "";
        char red;
        while ((red = (char)bis.read()) != c)
            s += red;
        return s;
    }
    public int readNumber(ByteArrayInputStream bis)
    {
        int result = 0;
        char b; boolean moins = false;
        while (((b = (char)bis.read()) >= '0' && b <= '9') || b == '-')
            if (b == '-')
                moins = true;
            else
                result = result * 10 + (b - '0');
        return moins ? -result : result;
    }
    public int readAttribute(String s, ByteArrayInputStream bis)
    {
        skipTo(s, bis);
        if (bis.available() > 0)
            return readNumber(bis);
        else
            return -1;
    }
    public int getLineHeight()
    {
        return lineHeight;
    }
    public static class Character
    {
        public final int id, xOffset, yOffset, page, textX, textY, textWidth, textHeight, xAdvance;
        private final LinkedList<Kerning> kers = new LinkedList<>();
        public Character(int i, int x, int y, int w, int h, int xO, int yO, int xA, int p)
        {
            id = i;
            textX = x;
            textY = y;
            textWidth = w;
            textHeight = h;
            xOffset = xO;
            yOffset = yO;
            page = p;
            xAdvance = xA;
        }
        public int getDifAmount(int first)
        {
            for (Kerning k : kers)
                if (k.first == first)
                    return k.amount;
            return 0;
        }
    }
    private static class Kerning
    {
        private final int first, amount;
        private Kerning(int f, int a)
        {
            first = f; amount = a;
        }
    }

    public TexturedSCModel[] createModel(float xBase, float yBase, float xSize, float ySize, Color c, String s)
    {
        return createModel(xBase, yBase, xSize, ySize, toColorArray(c, s.length()), s);
    }
    public  TexturedSCModel[] createModel(float xBase, float yBase, float xSize, float ySize, Color[] c, String s)
    {
        TexturedSCModel[] models = new TexturedSCModel[s.length()];
        ScreenCoor[] coors = new ScreenCoor[s.length()];
        int maxXSize = 0, maxYSize = 0, currentX = 0, currentY = 0;
        for (int i = 0; i < s.length(); i++) {
            if (chars[s.charAt(i)] != null) {
                currentX += i == 0 ? -1 : chars[s.charAt(i)].getDifAmount(s.charAt(i - 1)); // Kernings
                coors[i] = getScreenCoor(xBase + currentX, yBase + currentY, s.charAt(i));
                currentX += chars[s.charAt(i)].xAdvance;
                if (coors[i].getHeight() > maxYSize)
                    maxYSize = (int) coors[i].getHeight();

                if (s.charAt(i) == 10) // Line return
                {
                    currentY += getLineHeight();
                    if (currentX > maxXSize)
                        maxXSize = currentX;
                    maxYSize = 0;
                    currentX = 0;
                }
            }
        }
        if (currentX > maxXSize)
            maxXSize = currentX;
        for (int i = 0; i < s.length(); i++)
            if (chars[s.charAt(i)] != null)
                coors[i] = coors[i].addGui(maxXSize * xSize, (currentY + maxYSize) * ySize, 0, 0);
        for (int i = 0; i < s.length(); i++)
            if (chars[s.charAt(i)] != null)
                models[i] = new TexturedSCModel(coors[i], new QuadColor(c[i]), getTextureCoor(s.charAt(i)), pageTexts[chars[(s.charAt(i))].page]);
        return models;
    }
    private ScreenCoor getScreenCoor(float xBase, float yBase, char c)
    {
        AngelCodeFont.Character ch = chars[c];
        return ScreenCoor.gui(xBase + ch.xOffset, yBase + ch.yOffset, ch.textWidth, ch.textHeight);
    }
    private TextureCoor getTextureCoor(char c)
    {
        AngelCodeFont.Character ch = chars[c];
        return new TextureCoor(ch.textX, ch.textY, ch.textWidth, ch.textHeight);
    }
    private static Color[] toColorArray(Color qc, int to)
    {
        Color[] c = new Color[to];
        for (int i=0;i<to;i++)
            c[i] = qc;
        return c;
    }
}
