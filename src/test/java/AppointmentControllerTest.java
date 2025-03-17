
import com.appointment.Patient.Medicine.and.Appointment.System.controller.AppointmentController;
import com.appointment.Patient.Medicine.and.Appointment.System.model.Appointment;
import com.appointment.Patient.Medicine.and.Appointment.System.model.Patient;
import com.appointment.Patient.Medicine.and.Appointment.System.model.Role;
import com.appointment.Patient.Medicine.and.Appointment.System.model.User;
import com.appointment.Patient.Medicine.and.Appointment.System.service.AppointmentService;
import com.appointment.Patient.Medicine.and.Appointment.System.service.DoctorService;
import com.appointment.Patient.Medicine.and.Appointment.System.service.PatientService;
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

import java.time.LocalDateTime;
import java.util.Collections;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(AppointmentController.class)
@Import(AppointmentControllerTest.AppointmentControllerTestConfig.class)
public class AppointmentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AppointmentService appointmentService;

    @Autowired
    private PatientService patientService;

    @Autowired
    private DoctorService doctorService;

    @Autowired
    private UserService userService;

    @Test
    @WithMockUser(username = "patient@gmail.com", roles = {"PATIENT"})
    public void testListAppointmentsForPatient() throws Exception {
        User user = new User();
        user.setId(1L);
        user.setEmail("patient@gmail.com");
        user.setRole(Role.PATIENT);

        Patient patient = new Patient();
        patient.setId(1L);
        patient.setFullName("Karthik");
        patient.setUser(user);

        Appointment appointment = new Appointment();
        appointment.setId(1L);
        appointment.setDateTime(LocalDateTime.now());
        appointment.setStatus("SCHEDULED");
        appointment.setPatient(patient);

        when(userService.getUserByEmail(anyString())).thenReturn(user);
        when(patientService.getPatientByUser(user)).thenReturn(patient);
        when(appointmentService.getAppointmentsForPatient(1L)).thenReturn(Collections.singletonList(appointment));

        mockMvc.perform(get("/appointments"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("appointments", hasSize(1)))
                .andExpect(view().name("appointments_list"));
    }

    @TestConfiguration
    static class AppointmentControllerTestConfig {
        @Bean
        public AppointmentService appointmentService() {

            return Mockito.mock(AppointmentService.class);
        }

        @Bean
        public PatientService patientService() {

            return Mockito.mock(PatientService.class);
        }

        @Bean
        public DoctorService doctorService() {

            return Mockito.mock(DoctorService.class);
        }

        @Bean
        public UserService userService() {

            return Mockito.mock(UserService.class);
        }
    }
}
