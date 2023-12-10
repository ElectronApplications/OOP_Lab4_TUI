package tui;

public class TuiUtils {

    public static void renderBorder(FramebufferSlice framebuffer) {
        framebuffer.set(0, 0, '╔');
        framebuffer.set(framebuffer.w - 1, 0, '╗');
        framebuffer.set(0, framebuffer.h - 1, '╚');
        framebuffer.set(framebuffer.w - 1, framebuffer.h - 1, '╝');

        for (int i = 1; i < framebuffer.w - 1; i++) {
            framebuffer.set(i, 0, '═');
            framebuffer.set(i, framebuffer.h - 1, '═');
        }

        for (int i = 1; i < framebuffer.h - 1; i++) {
            framebuffer.set(0, i, '║');
            framebuffer.set(framebuffer.w - 1, i, '║');
        }
    }

    public static void renderText(FramebufferSlice framebuffer, String text, boolean centered) {
        char[] textChars = text.toCharArray();
        int lines = Math.min(framebuffer.h, (int) Math.ceil((double) textChars.length / framebuffer.w));

        int i = 0;
        int curline = centered ? (framebuffer.h - lines) / 2 : 0;
        while(lines > 1) {
            for(int x = 0; x < framebuffer.w; x++) {
                framebuffer.set(x, curline, textChars[i]);
                i++;
            }
            lines--;
            curline++;
        }

        for(int x = centered ? Math.max(0, (framebuffer.w - (textChars.length - i)) / 2) : 0; i < textChars.length && x < framebuffer.w; x++, i++) {
            framebuffer.set(x, curline, textChars[i]);
        }

        if(i < textChars.length) {
            framebuffer.set(framebuffer.w - 2, framebuffer.h - 1, '.');
            framebuffer.set(framebuffer.w - 1, framebuffer.h - 1, '.');
        }
    }

}
