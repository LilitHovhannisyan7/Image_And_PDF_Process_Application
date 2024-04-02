package com.picsart.javaInternship;


import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.PDPageTree;
import org.apache.pdfbox.pdmodel.graphics.image.LosslessFactory;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import javax.imageio.ImageIO;
import javax.imageio.stream.FileImageInputStream;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class Main
{
    private static String signature;
    private static final String FILE_NAME = "Signed.pdf";
    private static final String FILE_EXTENSION = ".pdf";
    private static final String IMAGE_PATH = "src/main/resources/stamp2.png";
    private static final int IMAGE_POSITION_X = 50;
    private static final int IMAGE_POSITION_Y = 50;
    private static final int SIGNATURE_POSITION_X = 78;
    private static final int SIGNATURE_POSITION_Y = 120;
    private static final int SIGNATURE_SIZE = 20;

    private static final int SIZE_FOR_MIN_IMAGE = 4;



    private static void addSignatureToPDF(String pathPdf, String signature)
    {
        try (PDDocument document = Loader.loadPDF(new File(pathPdf)))
        {
            PDPageTree pdPageTree = document.getPages();
            for (PDPage pdPage : pdPageTree)
            {
                PDImageXObject image = LosslessFactory.createFromImage(document, createImage(signature));
                try (PDPageContentStream contentStream = new PDPageContentStream(document, pdPage, PDPageContentStream
                        .AppendMode.APPEND, true))
                {
                    contentStream.drawImage(image, IMAGE_POSITION_X, IMAGE_POSITION_Y, image.getWidth() / SIZE_FOR_MIN_IMAGE, image.getHeight() / SIZE_FOR_MIN_IMAGE);
                }
            }
            document.save(pathPdf.replace(FILE_EXTENSION, FILE_NAME));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static BufferedImage createImage(String signature)
    {

        try
        {
            BufferedImage image = ImageIO.read(new FileImageInputStream(new File(IMAGE_PATH)));
            Graphics2D graphics2D = image.createGraphics();
            graphics2D.setColor(Color.RED);
            graphics2D.setFont(new Font("Arial", Font.BOLD, SIGNATURE_SIZE));
            graphics2D.drawString(signature,  SIGNATURE_POSITION_X,  SIGNATURE_POSITION_Y);
            graphics2D.dispose();

            return image;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public static void main(String[] args)
    {
        Scanner scanner = new Scanner(System.in);
        boolean isValid = false;
        while (!isValid) {
            System.out.println("Please enter your signature(2 or 3 characters):  ");
            signature = scanner.nextLine();
            if (signature.length() <= 3 && !signature.isEmpty()) {
                isValid = true;
            }
        }
        System.out.print("Enter the path of the PDF file: ");
        String path = scanner.nextLine();
        addSignatureToPDF(path, signature);
        System.out.println("PDF is signed");
    }
}