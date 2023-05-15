package se.atg.service.harrykart.java.rest;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import se.atg.service.harrykart.java.service.HarryKartService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

import static org.springframework.http.MediaType.APPLICATION_XML_VALUE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class HarryKartControllerIT {

    private final String HARRY_KART_TEMPLATE = "/java/api/play";
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @BeforeEach
    public void setup(){
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    @DisplayName("Trying to GET instead of POST should return 405 Method not allowed")
    void useGetOnPostEndpointShouldNotBePossible() throws Exception {

        mockMvc.perform(get(HARRY_KART_TEMPLATE)
                        .contentType(APPLICATION_XML_VALUE))
                .andExpect(status().is(405));

    }

    @Test
    @DisplayName("Cant Play Without body")
    void cantPlayWithoutBody() throws Exception {
        mockMvc.perform(post(HARRY_KART_TEMPLATE)
                        .contentType(APPLICATION_XML_VALUE))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Cant Play Without powerups")
    void cantPlayWithoutPowerUps() throws Exception {
        mockMvc.perform(post(HARRY_KART_TEMPLATE)
                        .content(getResourceFileAsString("input_without_powerUps.xml"))
                        .contentType(APPLICATION_XML_VALUE))
                .andExpect(status().isBadRequest()).andExpect(content().json("""
                                {
                                     "description": "cvc-complex-type.2.4.b: The content of element 'harryKart' is not complete. One of '{powerUps}' is expected.",
                                     "error": "INVALID_XML_CONTENT",
                                     "status": "BAD_REQUEST"
                                 }
"""));
    }

    @Test
    @DisplayName("Cant Play with one participant")
    void cantPlayWithOnlyOneParticipant() throws Exception {
        mockMvc.perform(post(HARRY_KART_TEMPLATE)
                        .content(getResourceFileAsString("valid_input_with_one_participant.xml"))
                        .contentType(APPLICATION_XML_VALUE))
                .andExpect(status().isBadRequest()).andExpect(content().json("""
                                {
                                      "description": "cvc-complex-type.2.4.j: The content of element 'startList' is not complete. 'participant' is expected to occur a minimum of '4' times. '3' more instances are required to satisfy this constraint.",
                                      "error": "INVALID_XML_CONTENT",
                                      "status": "BAD_REQUEST"
                                  }
"""));
    }

    @Test
    @DisplayName("Cant Play with number of loops not equal  powerups")
    void cantPlayWithNumberOfLoopsNotEqualPowerups() throws Exception {
        mockMvc.perform(post(HARRY_KART_TEMPLATE)
                        .content(getResourceFileAsString("valid_input_with_number_of_loops_not_equal_powerups.xml"))
                        .contentType(APPLICATION_XML_VALUE))
                .andExpect(status().isBadRequest()).andExpect(content().json("""
                                {
                                      "description": "Invalid number of loops vs defined power ups",
                                      "error": "INVALID_NUMBER_OF_LOOPS",
                                      "status": "BAD_REQUEST"
                                  }
"""));
    }

    @Test
    @DisplayName("Cant Play with wrong lane number")
    void cantPlayWithWrongLaneNumber() throws Exception {
        mockMvc.perform(post(HARRY_KART_TEMPLATE)
                        .content(getResourceFileAsString("valid_input_with_wrong_lane_config.xml"))
                        .contentType(APPLICATION_XML_VALUE))
                .andExpect(status().isBadRequest()).andExpect(content().json("""
                                {
                                      "description": "No Matched lane in power ups for specific participant",
                                      "error": "NO_MATCHED_LANE_IN_POWER_UPS",
                                      "status": "BAD_REQUEST"
                                  }
"""));
    }

    @Test
    @DisplayName("CantPlay with valid input (input 0)")
    void canPlayWith_ValidInput_withFile_Input_0() throws Exception {
        mockMvc.perform(post(HARRY_KART_TEMPLATE)
                        .content(getResourceFileAsString("input_0.xml"))
                        .contentType(APPLICATION_XML_VALUE))
                .andExpect(status().isOk()).andExpect(content().json("""
                                {
                                      "ranking": [
                                         {"position": 1, "horse": "TIMETOBELUCKY"},
                                         {"position": 2, "horse": "HERCULES BOKO"},
                                         {"position": 3, "horse": "CARGO DOOR"}
                                      ]
                                   }
"""));
    }

    @Test
    @DisplayName("CantPlay with valid input (input 1)")
    void canPlayWith_ValidInput_withFile_Input_1() throws Exception {
        mockMvc.perform(post(HARRY_KART_TEMPLATE)
                        .content(getResourceFileAsString("input_1.xml"))
                        .contentType(APPLICATION_XML_VALUE))
                .andExpect(status().isOk()).andExpect(content().json("""
                                {
                                      "ranking": [
                                         {"position": 1, "horse": "WAIKIKI SILVIO"},
                                         {"position": 2, "horse": "TIMETOBELUCKY"},
                                         {"position": 3, "horse": "HERCULES BOKO"}
                                      ]
                                   }
"""));
    }

    @Test
    @DisplayName("CantPlay with valid input (input 2)")
    void canPlayWith_ValidInput_withFile_Input_2() throws Exception {
        mockMvc.perform(post(HARRY_KART_TEMPLATE)
                        .content(getResourceFileAsString("input_2.xml"))
                        .contentType(APPLICATION_XML_VALUE))
                .andExpect(status().isOk()).andExpect(content().json("""
                                {
                                      "ranking": [
                                         {"position": 1, "horse": "HERCULES BOKO"},
                                         {"position": 2, "horse": "TIMETOBELUCKY"},
                                         {"position": 3, "horse": "WAIKIKI SILVIO"}
                                      ]
                                   }
"""));
    }

    @Test
    @DisplayName("CantPlay with valid input (input 3)")
    void canPlayWith_ValidInput_withFile_Input_3() throws Exception {
        mockMvc.perform(post(HARRY_KART_TEMPLATE)
                        .content(getResourceFileAsString("input_3.xml"))
                        .contentType(APPLICATION_XML_VALUE))
                .andExpect(status().isOk()).andExpect(content().json("""
                                {
                                      "ranking": [
                                         {"position": 1, "horse": "HERCULES BOKO"},
                                         {"position": 2, "horse": "TIMETOBELUCKY"},
                                         {"position": 3, "horse": "WAIKIKI SILVIO"}
                                      ]
                                   }
"""));
    }

    private String getResourceFileAsString(String fileName) throws IOException {
        ClassLoader classLoader = ClassLoader.getSystemClassLoader();
        try (InputStream is = classLoader.getResourceAsStream(fileName)) {
            if (is == null) return null;
            try (InputStreamReader isr = new InputStreamReader(is);
                 BufferedReader reader = new BufferedReader(isr)) {
                return reader.lines().collect(Collectors.joining(System.lineSeparator()));
            }
        }
    }
}
