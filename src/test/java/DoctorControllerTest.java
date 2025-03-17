

import com.appointment.Patient.Medicine.and.Appointment.System.controller.DoctorController;
import com.appointment.Patient.Medicine.and.Appointment.System.model.Doctor;
import com.appointment.Patient.Medicine.and.Appointment.System.model.User;
import com.appointment.Patient.Medicine.and.Appointment.System.service.AppointmentService;
import com.appointment.Patient.Medicine.and.Appointment.System.service.DoctorService;
import com.appointment.Patient.Medicine.and.Appointment.System.service.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(DoctorController.class)
@Import(DoctorControllerTest.DoctorControllerTestConfig.class)
public class DoctorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserService userService;

    @Autowired
    private DoctorService doctorService;

    @Autowired
    private AppointmentService appointmentService;

    @Test
    @WithMockUser(username = "doctor@gmail.com", roles = {"DOCTOR"})
    public void testDoctorDashboard() throws Exception {
        Doctor doctor = new Doctor();
        doctor.setId(1L);
        doctor.setFullName("Dr. Karthik");

        when(userService.getUserByEmail(Mockito.any(String.class))).thenReturn(new User());
        when(doctorService.getDoctorByUser(any(User.class))).thenReturn(doctor);
        when(appointmentService.getAppointmentsForDoctor(1L)).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/doctor/dashboard"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("doctor"))
                .andExpect(model().attributeExists("appointments"))
                .andExpect(view().name("doctor_dashboard"));
    }

    @TestConfiguration
    static class DoctorControllerTestConfig {
        @Bean
        public UserService userService() {

            return Mockito.mock(UserService.class);
        }

        @Bean
        public DoctorService doctorService() {

            return Mockito.mock(DoctorService.class);
        }

        @Bean
        public AppointmentService appointmentService() {

            return Mockito.mock(AppointmentService.class);
        }
    }
}
