package cv.nosi.certificate.ndcapi.ndcapi.service;

import cv.nosi.certificate.ndcapi.ndcapi.dto.CertificateRequestDto;
import cv.nosi.certificate.ndcapi.ndcapi.util.GeneratePdfCertificateHelper;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;

import javax.imageio.ImageIO;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Service
public class CertificateService {

   private final GeneratePdfCertificateHelper generatePdfCertificateHelper;

   public CertificateService(GeneratePdfCertificateHelper generatePdfCertificateHelper) {
      this.generatePdfCertificateHelper = generatePdfCertificateHelper;
   }

   public ByteArrayResource generateCertificatePdf(CertificateRequestDto certificateRequest) throws IOException {
      final var certificateHtml = generateCertificateHtml(certificateRequest);
      final var pdfByteArray = generatePdfCertificateHelper.convertCertificateTemplateToPdf(certificateHtml);
      return new ByteArrayResource(pdfByteArray);
   }

   public ByteArrayResource generateCertificatePng(CertificateRequestDto certificateRequest) throws IOException {
      final var certificateHtml = generateCertificateHtml(certificateRequest);
      final var pdfByteArray = generatePdfCertificateHelper.convertCertificateTemplateToPdf(certificateHtml);
      final var extractPagesAsSingleByteArray = convertPdfToPngByteArray(pdfByteArray);
      return new ByteArrayResource(extractPagesAsSingleByteArray);
   }

   private String generateCertificateHtml(CertificateRequestDto certificateRequest) {
      return generatePdfCertificateHelper.generateCertificateFromTemplate(getContextData(certificateRequest), "certificate_template");
   }

   private byte[] convertPdfToPngByteArray(byte[] pdfBytes) throws IOException {
      final var byteArrayInputStream = new ByteArrayInputStream(pdfBytes);
      final var document = PDDocument.load(byteArrayInputStream);
      final var pdfRenderer = new PDFRenderer(document);
      final var bufferedImage = pdfRenderer.renderImage(0);
      final var byteArrayOutputStream = new ByteArrayOutputStream();
      ImageIO.write(bufferedImage, "png", byteArrayOutputStream);
      return byteArrayOutputStream.toByteArray();
   }

   private Context getContextData(CertificateRequestDto certificateRequest) {
      final var context = new Context();
      context.setVariable("RECIPIENT_NAME", certificateRequest.recipientName());
      context.setVariable("RECIPIENT_EMAIL", certificateRequest.recipientEmail());
      context.setVariable("COURSE_NAME", certificateRequest.courseName());
      context.setVariable("COURSE_DESCRIPTION", certificateRequest.courseDescription());
      context.setVariable("ISSUE_DATE", certificateRequest.issueDate());
      context.setVariable("SIGNATURE", certificateRequest.recipientName());
      return context;
   }
}
