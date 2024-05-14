package cv.nosi.certificate.ndcapi.ndcapi.util;

import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import org.jsoup.Jsoup;
import org.jsoup.helper.W3CDom;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
import org.w3c.dom.Document;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Component
public class GeneratePdfCertificateHelper {

   private final Logger LOGGER = LoggerFactory.getLogger(GeneratePdfCertificateHelper.class);

   public String generateCertificateFromTemplate(Context context, String templateName) {

      final var resolver = new ClassLoaderTemplateResolver();
      resolver.setPrefix("templates/");
      resolver.setSuffix(".html");
      resolver.setCharacterEncoding(StandardCharsets.UTF_8.name());

      final var templateEngine = new TemplateEngine();
      templateEngine.setTemplateResolver(resolver);

      return templateEngine.process(templateName, context);
   }

   public byte[] convertCertificateTemplateToPdf(String certificateTemplateWithData) throws IOException {

      final var os = new ByteArrayOutputStream();

      final var doc = html5ParseDocument(certificateTemplateWithData);
      System.out.println(doc);
      new PdfRendererBuilder().
              useFastMode()
              .withW3cDocument(doc, null)
              .toStream(os)
              .run();

      LOGGER.info("PDF generation completed");

      return os.toByteArray();
   }

   private Document html5ParseDocument(String inputHTML) {
      org.jsoup.nodes.Document document = Jsoup.parse(inputHTML, StandardCharsets.UTF_8.name());
      return new W3CDom().fromJsoup(document);
   }
}
