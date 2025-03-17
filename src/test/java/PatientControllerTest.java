
import com.appointment.Patient.Medicine.and.Appointment.System.controller.PatientController;
import com.appointment.Patient.Medicine.and.Appointment.System.model.Patient;
import com.appointment.Patient.Medicine.and.Appointment.System.model.User;
import com.appointment.Patient.Medicine.and.Appointment.System.service.AppointmentService;
import com.appointment.Patient.Medicine.and.Appointment.System.service.DoctorService;
import com.appointment.Patient.Medicine.and.Appointment.System.service.MedicationService;
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

import java.util.Collections;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PatientController.class)
@Import(PatientControllerTest.PatientControllerTestConfig.class)
public class PatientControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserService userService;

    @Autowired
    private PatientService patientService;

    @Autowired
    private DoctorService doctorService;

    @Autowired
    private AppointmentService appointmentService;

    @Autowired
    private MedicationService medicationService;

    @Test
    @WithMockUser(username = "patient@gmail.com", roles = {"PATIENT"})
    public void testPatientDashboard() throws Exception {
        Patient patient = new Patient();
        patient.setId(1L);
        patient.setFullName("Karthik");

        when(userService.getUserByEmail(anyString())).thenReturn(new User());
        when(patientService.getPatientByUser(Mockito.any(User.class))).thenReturn(patient);
        when(appointmentService.getAppointmentsForPatient(1L)).thenReturn(Collections.emptyList());
        when(medicationService.getMedicationsForPatient(1L)).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/patient/dashboard"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("patient"))
                .andExpect(model().attributeExists("appointments"))
                .andExpect(model().attributeExists("medications"))
                .andExpect(view().name("patient_dashboard"));
    }

    @Test
    @WithMockUser(username = "patient@gmail.com", roles = {"PATIENT"})
    public void testBookAppointmentForm() throws Exception {
        when(doctorService.getAllDoctors()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/patient/book-appointment"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("doctors"))
                .andExpect(view().name("book_appointment"));
    }

    @Test
    @WithMockUser(username = "patient@gmail.com", roles = {"PATIENT"})
    public void testEditProfileForm() throws Exception {
        Patient patient = new Patient();
        patient.setId(1L);
        patient.setFullName("Karthik");

        when(userService.getUserByEmail(anyString())).thenReturn(new User());
        when(patientService.getPatientByUser(Mockito.any(User.class))).thenReturn(patient);

        mockMvc.perform(get("/patient/edit-profile"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("patient"))
                .andExpect(view().name("patient_profile_edit"));
    }

    @TestConfiguration
    static class PatientControllerTestConfig {
        @Bean
        public UserService userService() {

            return Mockito.mock(UserService.class);
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
        public AppointmentService appointmentService() {

            return Mockito.mock(AppointmentService.class);
        }

        @Bean
        public MedicationService medicationService() {

            return Mockito.mock(MedicationService.class);
        }
    }
}
