package br.com.marcosprado.timesbackend.controller.test;


import br.com.marcosprado.timesbackend.repository.test.TestDatabaseRepository;
import jakarta.persistence.PersistenceException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestCleanupController {

    @Value("${app.test.cleanup.enabled:false}")
    private boolean cleanupEnabled;

    private final TestDatabaseRepository testDatabaseRepository;

    public TestCleanupController(
            TestDatabaseRepository testDatabaseRepository
    ) {
        this.testDatabaseRepository = testDatabaseRepository;
    }

    @DeleteMapping("/test/cleanup")
    public ResponseEntity<String> cleanup() {
        if (!cleanupEnabled) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Cleanup is disabled in this environment");
        }

        try {
            this.testDatabaseRepository.truncateClientsTable();

            return ResponseEntity.status(HttpStatus.OK).body("Cleanup done!");
        } catch (PersistenceException exception) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(exception.getMessage());
        }
    }
}
