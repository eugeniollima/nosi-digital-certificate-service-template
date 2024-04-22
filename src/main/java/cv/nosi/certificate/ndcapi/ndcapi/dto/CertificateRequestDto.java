package cv.nosi.certificate.ndcapi.ndcapi.dto;

import java.time.LocalDate;

public record CertificateRequestDto(
        String recipientName,
        String recipientEmail,
        String courseName,
        String courseDescription,
        LocalDate issueDate
) {
}
