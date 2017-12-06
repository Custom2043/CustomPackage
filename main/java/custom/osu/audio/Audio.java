package custom.osu.audio;

import android.content.res.AssetFileDescriptor;
import android.media.MediaExtractor;
import android.media.MediaFormat;

import java.nio.ByteBuffer;

import custom.osu.main.MainActivity;

public class Audio
{
    public static ByteBuffer inputBuffers;
    public static void mainAudio() {
        try {
            MediaExtractor extractor = new MediaExtractor();
            android.media.MediaCodec decoder;
            AssetFileDescriptor afd = MainActivity.getFileDescriptor("sounds/demo.mp3");
            extractor.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());

            extractor.selectTrack(0);
            MediaFormat format = extractor.getTrackFormat(0);

            String mime = format.getString(MediaFormat.KEY_MIME);
            format.setInteger(MediaFormat.KEY_MAX_INPUT_SIZE,655360);

            decoder = android.media.MediaCodec.createDecoderByType(mime);
            decoder.configure(format, null, null, 0);
            decoder.start();

            int id = decoder.dequeueInputBuffer(-1);
            ByteBuffer inputBuffers = decoder.getInputBuffer(id);


            int size = extractor.readSampleData(inputBuffers, 0);
            decoder.queueInputBuffer(id, 0, size, -1, 0);

            android.media.MediaCodec.BufferInfo tamer = new android.media.MediaCodec.BufferInfo();
            int jsp = decoder.dequeueOutputBuffer(tamer, -1);
            System.out.println(jsp);
            jsp = decoder.dequeueOutputBuffer(tamer, -1);
            System.out.println(jsp);
            ByteBuffer buf = decoder.getOutputBuffer(jsp);
            System.out.println("Passe");
            System.out.println(buf);

            jsp = decoder.dequeueOutputBuffer(tamer, -1);
            System.out.println(jsp);

            extractor.release();
            decoder.stop();
            decoder.release();
        }catch(Exception e) {e.printStackTrace();}
    }
}
