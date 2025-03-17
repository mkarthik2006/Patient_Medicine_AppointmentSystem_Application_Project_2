

import com.appointment.Patient.Medicine.and.Appointment.System.controller.MedicationController;
import com.appointment.Patient.Medicine.and.Appointment.System.model.Patient;
import com.appointment.Patient.Medicine.and.Appointment.System.model.User;
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

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(MedicationController.class)
@Import(MedicationControllerTest.MedicationControllerTestConfig.class)
public class MedicationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private MedicationService medicationService;

    @Autowired
    private PatientService patientService;

    @Autowired
    private UserService userService;

    @Autowired
    private DoctorService doctorService;

    @Test
    @WithMockUser(username = "patient@gmail.com", roles = {"PATIENT"})
    public void testListMedicationsForPatient() throws Exception {
        when(userService.getUserByEmail(anyString())).thenReturn(new User());
        when(patientService.getPatientByUser(Mockito.any(User.class))).thenReturn(new Patient());
        when(medicationService.getMedicationsForPatient(anyLong())).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/medications"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("medications"))
                .andExpect(view().name("medications_list"));
    }

    @Test
    @WithMockUser(username = "admin@gmail.com", roles = {"ADMIN"})
    public void testShowMedicationForm() throws Exception {
        when(patientService.getAllPatients()).thenReturn(Collections.emptyList());
        when(doctorService.getAllDoctors()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/medications/new"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("medication"))
                .andExpect(view().name("medications_form"));
    }

    @TestConfiguration
    static class MedicationControllerTestConfig {
        @Bean
        public MedicationService medicationService() {

            return Mockito.mock(MedicationService.class);
        }

        @Bean
        public PatientService patientService() {
            return Mockito.mock(PatientService.class);
        }

        @Bean
        public UserService userService() {

            return Mockito.mock(UserService.class);
        }

        @Bean
        public DoctorService doctorService() {

            return Mockito.mock(DoctorService.class);
        }
    }
}
