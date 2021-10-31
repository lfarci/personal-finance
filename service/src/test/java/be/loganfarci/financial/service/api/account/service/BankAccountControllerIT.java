package be.loganfarci.financial.service.api.account.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class BankAccountControllerIT {

    @Autowired
    private MockMvc mvc;

    @Test
    public void save() throws Exception {
        mvc.perform(post("/api/accounts")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
    }

}
