package hr.truenorth.vhs_rental.integration.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import hr.truenorth.vhs_rental.model.Rental;
import hr.truenorth.vhs_rental.model.User;
import hr.truenorth.vhs_rental.model.VHS;
import hr.truenorth.vhs_rental.model.dto.RentalDto;
import hr.truenorth.vhs_rental.model.dto.VHSReturnDto;
import hr.truenorth.vhs_rental.repository.UserRepository;
import hr.truenorth.vhs_rental.repository.VHSRepository;
import hr.truenorth.vhs_rental.service.RentalService;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.sql.Date;
import java.time.LocalDate;
import java.time.Period;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class RentalControllerIntegrationTest {
    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Resource
    private UserRepository userRepository;

    @Resource
    private VHSRepository vhsRepository;

    @Resource
    private RentalService rentalService;

    private User user;
    private VHS vhs;

    @BeforeAll
    public void initUser() {
        user = createUser("testUser");
    }

    @BeforeEach
    public void initVHS() {
        // Create new vhs for each test in order to avoid conflicts
        vhs = createVHS("testTitle");
    }

    @Test
    public void createRental_whenRentalDtoReceived_thenReturnCreated() throws Exception {
        Date start = Date.valueOf(LocalDate.now());
        Date end = Date.valueOf(LocalDate.now().plus(Period.ofDays(7)));
        RentalDto rentalDto = new RentalDto(user.getId(), vhs.getId(), start, end);

        MvcResult result = mvc.perform(post("/api/rental").content(objectMapper.writeValueAsString(rentalDto))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(rentalDto)))
                .andExpect(status().isOk())
                .andReturn();

        Rental rental = objectMapper.readValue(result.getResponse().getContentAsString(), Rental.class);
        assertThat(rental.getUser().getUsername(), is("testUser"));
        assertThat(rental.getVhs().getTitle(), is("testTitle"));
        assertThat(rental.getDateRented().toLocalDate(), equalTo(start.toLocalDate()));
        assertThat(rental.getDateDue().toLocalDate(), equalTo(end.toLocalDate()));
    }

    @Test
    public void createRental_whenAlreadyRented_thenStatusForbiddenReturned() throws Exception {
        Date start = Date.valueOf(LocalDate.now());
        Date end = Date.valueOf(LocalDate.now().plus(Period.ofDays(7)));
        rentalService.save(new Rental(0L, user, vhs, start, end, null));
        RentalDto rentalDto = new RentalDto(user.getId(), vhs.getId(),
                Date.valueOf(LocalDate.now().plus(Period.ofDays(1))),
                Date.valueOf(LocalDate.now().plus(Period.ofDays(3))));

        mvc.perform(post("/api/rental").content(objectMapper.writeValueAsString(rentalDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(rentalDto)))
                .andExpect(status().isForbidden());
    }

    @Test
    public void getRentalById_whenSavedRental_thenReturnRental() throws Exception {
        Date start = Date.valueOf(LocalDate.now());
        Date end = Date.valueOf(LocalDate.now().plus(Period.ofDays(7)));
        Rental rental = rentalService.save(new Rental(0L, user, vhs, start, end, null));

        MvcResult result = mvc.perform(get("/api/rental/" + rental.getId()))
                .andExpect(status().isOk())
                .andReturn();

        Rental rental2 = objectMapper.readValue(result.getResponse().getContentAsString(), Rental.class);
        assertEquals(rental, rental2);
    }

    @Test
    public void getAllRentals_whenRentalsSaved_thenReturnListOfRentals() throws Exception {
        User user1 = createUser("userA");
        User user2 = createUser("userB");
        User user3 = createUser("userC");
        VHS vhs1 = createVHS("vhsA");
        VHS vhs2 = createVHS("vhsB");
        VHS vhs3 = createVHS("vhsC");
        Date start = Date.valueOf("2024-02-01");
        Date end = Date.valueOf("2024-02-07");
        Rental rental1 = rentalService.save(new Rental(0L, user1, vhs1, start, end, null));
        Rental rental2 = rentalService.save(new Rental(0L, user2, vhs2, start, end, null));
        Rental rental3 = rentalService.save(new Rental(0L, user3, vhs3, start, end, null));

        MvcResult result = mvc.perform(get("/api/rental"))
                .andExpect(status().isOk())
                .andReturn();

        List<Rental> rentals = objectMapper.readerForListOf(Rental.class)
                .readValue(result.getResponse().getContentAsString());
        assertTrue(rentals.contains(rental1) && rentals.contains(rental2) && rentals.contains(rental3));
    }

    @Test
    public void updateRental_whenSavedRental_thenRentalUpdated() throws Exception {
        User user = createUser("oldUser");
        Date start = Date.valueOf(LocalDate.now());
        Date end = Date.valueOf(LocalDate.now().plus(Period.ofDays(7)));
        Rental oldRental = rentalService.save(new Rental(0L, user, vhs, start, end, null));
        user = createUser("newUser");
        vhs = createVHS("newTitle");
        start = Date.valueOf("2024-03-01");
        end = Date.valueOf("2024-03-07");
        RentalDto newRentalDto = new RentalDto(user.getId(), vhs.getId(), start, end);

        MvcResult result = mvc.perform(put("/api/rental/" + oldRental.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newRentalDto)))
                .andExpect(status().isOk())
                .andReturn();

        Rental newRental = objectMapper.readValue(result.getResponse().getContentAsString(), Rental.class);
        assertEquals(oldRental.getId(), newRental.getId());
        assertEquals(newRentalDto.getUserId(), newRental.getUser().getId());
        assertEquals(newRentalDto.getVhsId(), newRental.getVhs().getId());
        assertEquals(newRentalDto.getDateRented().toLocalDate(), newRental.getDateRented().toLocalDate());
        assertEquals(newRentalDto.getDateDue().toLocalDate(), newRental.getDateDue().toLocalDate());
    }

    @Test
    public void deleteRentalById_whenSavedRental_thenRentalDeleted() throws Exception {
        Date start = Date.valueOf(LocalDate.now());
        Date end = Date.valueOf(LocalDate.now().plus(Period.ofDays(7)));
        Rental rental = rentalService.save(new Rental(0L, user, vhs, start, end, null));

        mvc.perform(delete("/api/rental/" + rental.getId()))
                .andExpect(status().isOk())
                .andReturn();

        assertTrue(rentalService.getById(rental.getId()).isEmpty());
    }

    @Test
    public void returnVHS_whenDateDueInFuture_thenReturnResponseWithoutFee() throws Exception {
        Date start = Date.valueOf(LocalDate.now());
        Date end = Date.valueOf(LocalDate.now().plus(Period.ofDays(1)));
        Rental rental = rentalService.save(new Rental(0L, user, vhs, start, end, null));

        MvcResult result = mvc.perform(put("/api/rental/" + rental.getId() + "/return"))
                .andExpect(status().isOk())
                .andReturn();

        VHSReturnDto vhsReturnDto = objectMapper.readValue(result.getResponse().getContentAsString(), VHSReturnDto.class);
        assertEquals(0, vhsReturnDto.getFee());
    }

    @Test
    public void returnVHS_whenDateDueInPast_thenReturnResponseWithFee() throws Exception {
        Date start = Date.valueOf(LocalDate.now().minus(Period.ofDays(2)));
        Date end = Date.valueOf(LocalDate.now().minus(Period.ofDays(1)));
        Rental rental = rentalService.save(new Rental(0L, user, vhs, start, end, null));

        MvcResult result = mvc.perform(put("/api/rental/" + rental.getId() + "/return"))
                .andExpect(status().isOk())
                .andReturn();

        VHSReturnDto vhsReturnDto = objectMapper.readValue(result.getResponse().getContentAsString(), VHSReturnDto.class);
        assertTrue(vhsReturnDto.getFee() > 0);
    }

    @Test
    public void returnVHS_whenAlreadyReturned_thenStatusForbiddenReturned() throws Exception {
        Date start = Date.valueOf(LocalDate.now().minus(Period.ofDays(2)));
        Date end = Date.valueOf(LocalDate.now().minus(Period.ofDays(1)));
        Date returned = Date.valueOf(LocalDate.now());
        Rental rental = rentalService.save(new Rental(0L, user, vhs, start, end, returned));

        mvc.perform(put("/api/rental/" + rental.getId() + "/return"))
                .andExpect(status().isForbidden())
                .andReturn();
    }

    private VHS createVHS(String title) {
        VHS vhs = new VHS();
        vhs.setTitle(title);
        return vhsRepository.save(vhs);
    }

    private User createUser(String username) {
        User user = new User();
        user.setUsername(username);
        return userRepository.save(user);
    }
}
