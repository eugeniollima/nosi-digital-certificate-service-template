package cv.nosi.certificate.ndcapi.ndcapi.controller;

import cv.nosi.certificate.ndcapi.ndcapi.dto.CertificateRequestDto;
import cv.nosi.certificate.ndcapi.ndcapi.service.CertificateService;
import io.swagger.v3.oas.annotations.Operation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/Certificate")
public class CertificateController {

   private final Logger LOGGER = LoggerFactory.getLogger(CertificateController.class);

   private final CertificateService certificateService;

   public CertificateController(CertificateService certificateService) {
      this.certificateService = certificateService;
   }

   @Operation(tags = {"Certificate"})
   @PostMapping("/getPdfCertificate")
   public ResponseEntity<?> getCertificatePdf(@RequestBody CertificateRequestDto certificateRequest) {
      try {

         final var byteArrayResource = certificateService.generateCertificatePdf(certificateRequest);
         final var header = buildHeader("attachment;filename=certificate.pdf");

         return ResponseEntity.ok()
                 .headers(header)
                 .contentLength(byteArrayResource.contentLength())
                 .contentType(MediaType.APPLICATION_PDF)
                 .body(byteArrayResource);

      } catch (Exception ex) {
         LOGGER.error("FATAL ERROR", ex);
         return ResponseEntity.internalServerError().body("Ocorreu um erro desconhecido");
      }
   }

   @Operation(tags = {"Certificate"})
   @PostMapping("/getImageCertificate")
   public ResponseEntity<?> getCertificatePng(@RequestBody CertificateRequestDto certificateRequest) {
      try {

         final var byteArrayResource = certificateService.generateCertificatePng(certificateRequest);
         final var header = buildHeader("attachment;filename=certificate.png");

         return ResponseEntity.ok()
                 .headers(header)
                 .contentLength(byteArrayResource.contentLength())
                 .contentType(MediaType.IMAGE_PNG)
                 .body(byteArrayResource);

      } catch (Exception ex) {
         LOGGER.error("FATAL ERROR", ex);
         return ResponseEntity.internalServerError().body("Ocorreu um erro desconhecido");
      }
   }

   private HttpHeaders buildHeader(String contentDisposition) {
      final var header = new HttpHeaders();
      header.add(HttpHeaders.CONTENT_DISPOSITION, contentDisposition);
      header.add(HttpHeaders.CACHE_CONTROL, "no-cache, no-store, must-revalidate");
      header.add(HttpHeaders.PRAGMA, "no-cache");
      header.add(HttpHeaders.EXPIRES, "0");
      return header;
   }
}
