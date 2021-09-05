package be.loganfarci.financial.service.api.transaction;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Controller
@CrossOrigin("*")
@RequestMapping("/api")
public class TransactionController {

    private final Logger logger = LoggerFactory.getLogger(TransactionController.class);
    private final TransactionService service;

    public TransactionController(TransactionService service) {
        this.service = service;
    }

    @PostMapping("/transactions/files")
    public ResponseEntity handleCSVFileUpload(@RequestParam("file") MultipartFile file) throws IOException {
        logger.info("Handle CSV file upload: " + file.getOriginalFilename());
        service.saveTransactionsFrom(file.getBytes());
        return new ResponseEntity(HttpStatus.OK);
    }

}
