package com.devternity.slidy;

import com.google.common.base.Suppliers;
import com.google.common.base.Throwables;
import com.google.common.io.ByteSource;
import org.apache.pdfbox.io.MemoryUsageSetting;
import org.apache.pdfbox.multipdf.PDFMergerUtility;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.common.PDRectangle;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.function.Supplier;

import static com.google.common.io.Files.asByteSink;
import static com.google.common.io.Resources.asByteSource;

public class Deck {

    private final Supplier<File> location;

    public Deck(File location) {
        if (!location.canRead()) {
            throw new IllegalArgumentException("Sorry, slide deck is not readable (" + location.getAbsolutePath() + ")");
        }
        this.location = () -> location;
    }

    public Deck(String url) throws IOException {
        this.location = Suppliers.memoize(() -> {
            try {
                return downloaded(url);
            } catch (IOException e) {
                Throwables.throwIfUnchecked(e);
                throw new IllegalArgumentException(e);
            }
        });
    }

    private static File downloaded(String url) throws IOException {
        ByteSource bytes = asByteSource(new URL(url));
        File localFile = File.createTempFile("slide", ".slide");
        bytes.copyTo(asByteSink(localFile));
        return localFile;
    }

    public Dimensions dimensions() throws IOException {
        try (PDDocument doc = PDDocument.load(location.get())) {
            return new Dimensions(doc.getPage(0));
        }
    }

    public Deck prepend(Slide slide) throws IOException {
        return merge(slide.location(), location.get());
    }

    public Deck append(Slide slide) throws IOException {
        return merge(location.get(), slide.location());
    }

    private Deck merge(File... files) throws IOException {
        PDFMergerUtility merger = new PDFMergerUtility();
        for (File file : files) {
            merger.addSource(file);
        }

        File output = File.createTempFile("wrapped", ".slide");
        merger.setDestinationStream(new FileOutputStream(output));
        merger.mergeDocuments(MemoryUsageSetting.setupMainMemoryOnly());

        return new Deck(output);
    }

    public Output output(Storage storage) throws IOException {
        return storage.out(location());
    }

    public File location() {
        return location.get();
    }

    public static class Dimensions {

        private final PDRectangle box;

        public Dimensions(PDPage page) {
            this.box = page.getMediaBox();
        }


        public float width() {
            return box.getWidth();
        }


        public float height() {
            return box.getHeight();
        }

        public PDRectangle rectangle() {
            return new PDRectangle(width(), height());
        }


        public String x() {
            return (int) width() + "x" + (int) height();
        }

    }
}
