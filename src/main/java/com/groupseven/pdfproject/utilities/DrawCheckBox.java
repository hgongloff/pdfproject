package com.groupseven.pdfproject.utilities;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Random;

import com.groupseven.pdfproject.MainCanvas;
import com.groupseven.pdfproject.model.Action;
import com.itextpdf.forms.PdfAcroForm;
import com.itextpdf.forms.fields.PdfButtonFormField;
import com.itextpdf.forms.fields.PdfFormField;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.layout.Document;

import javafx.event.Event;
import javafx.geometry.Point2D;
import javafx.scene.input.MouseEvent;

/**
 * @author Hunter Gongloff
 * 
 * @{ \brief This is the class to create a checkbox on user click \ref 16_3 "Task 16.3"
 */
public class DrawCheckBox implements Action {

    public static final String SRC = "src/main/resources/manipulate_pdf/test_pdf.pdf";
    public static final String DES = "src/main/resources/manipulate_pdf/test_pdf_old.pdf";

    /// \ref t16_3 "task 16.3"
    public DrawCheckBox(MainCanvas _canvas) {
        // TODO Auto-generated constructor stub
    }

    /// \ref t16_3 "task 16.3"
    @Override
    public void execute() {
        // TODO Auto-generated method stub

    }

    /// \brief on click creates checkboxes at click location
    /// \ref t16_3 "task 16.3"
    @Override
    public Action handle(Event event) {
        if (!(event instanceof MouseEvent))
            return this;

        byte[] array = new byte[7]; // length is bounded by 7
        new Random().nextBytes(array);
        String generatedString = new String(array, Charset.forName("UTF-8"));

        MouseEvent mouseEvent = (MouseEvent) event;
        Point2D mousePosition = new Point2D(mouseEvent.getX(), mouseEvent.getY());

        Point2D currentPoint = new Point2D(mouseEvent.getX(), mouseEvent.getY());
        float x = (float) currentPoint.getX();
        float y = (float) (780.0 - currentPoint.getY());

        if (event.getEventType() == MouseEvent.MOUSE_PRESSED) {

            PdfDocument pdfDoc = null;
            try {
                pdfDoc = new PdfDocument(new PdfWriter(DES).setSmartMode(true));
            } catch (FileNotFoundException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
            PdfDocument srcDoc = null;
            try {
                srcDoc = new PdfDocument(new PdfReader(SRC));
            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
            srcDoc.copyPagesTo(1, srcDoc.getNumberOfPages(), pdfDoc);

            Document doc = new Document(pdfDoc);
            PdfAcroForm form = PdfAcroForm.getAcroForm(doc.getPdfDocument(), true);
            PdfButtonFormField checkField = PdfFormField.createCheckBox(doc.getPdfDocument(),
                    new Rectangle(x, y, 15, 15), generatedString, "Off", PdfFormField.TYPE_CHECK);
            form.addField(checkField);

            pdfDoc.close();
            srcDoc.close();

            File file = new File("src/main/resources/manipulate_pdf/test_pdf.pdf");

            File rename = new File("src/main/resources/manipulate_pdf/test_pdf_old_old.pdf");

            boolean flag = file.renameTo(rename);

            if (flag == true) {
                System.out.println("File Successfully Rename");
            }

            else {
                System.out.println("Operation Failed");
            }

            file = new File("src/main/resources/manipulate_pdf/test_pdf_old.pdf");

            rename = new File("src/main/resources/manipulate_pdf/test_pdf.pdf");

            flag = file.renameTo(rename);

            if (flag == true) {
                System.out.println("File Successfully Rename");
            }

            else {
                System.out.println("Operation Failed");
            }

            file = new File("src/main/resources/manipulate_pdf/test_pdf_old_old.pdf");

            rename = new File("src/main/resources/manipulate_pdf/test_pdf_old.pdf");

            flag = file.renameTo(rename);

            if (flag == true) {
                System.out.println("File Successfully Rename");
            }

            else {
                System.out.println("Operation Failed");
            }

        }

        return null;
    }

    /// \ref t16_3 "task 16.3"
    @Override
    public boolean isComplete() {
        // TODO Auto-generated method stub
        return false;
    }

    /// \ref t16_3 "task 16.3"
    @Override
    public boolean contains(Point2D point) {
        // TODO Auto-generated method stub
        return false;
    }

    /// \ref t16_3 "task 16.3"
    @Override
    public void pdfExecute(PdfCanvas canvas, PdfPage page) {
        // TODO Auto-generated method stub

    }

}