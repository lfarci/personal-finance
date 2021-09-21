package be.loganfarci.financial.service.api.file;

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
public class FileController {

    private final Logger logger = LoggerFactory.getLogger(FileController.class);
    private final TransactionFileService service;

    public FileController(TransactionFileService service) {
        this.service = service;
    }

    @PostMapping("/transactions/files")
    public ResponseEntity handleCSVFileUpload(@RequestParam("file") MultipartFile file) throws IOException {
        logger.info("Handle CSV file upload: " + file.getOriginalFilename());
        service.importTransactionsFrom(file.getBytes());
        return new ResponseEntity(HttpStatus.OK);
    }

}
