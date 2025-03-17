

import com.appointment.Patient.Medicine.and.Appointment.System.model.Patient;
import com.appointment.Patient.Medicine.and.Appointment.System.model.User;
import com.appointment.Patient.Medicine.and.Appointment.System.repository.PatientRepository;
import com.appointment.Patient.Medicine.and.Appointment.System.service.AppointmentService;
import com.appointment.Patient.Medicine.and.Appointment.System.service.PatientService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class PatientServiceTest {

    @Mock
    private PatientRepository patientRepository;
    @Mock
    private AppointmentService appointmentService;

    @InjectMocks
    private PatientService patientService;

    private Patient patient;
    private User user;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        user = new User();
        user.setId(1L);
        user.setName("Thilaga");
        user.setEmail("thilaga@gmail.com");

        patient = new Patient();
        patient.setId(1L);
        patient.setFullName("Thilaga");
        patient.setPhone("8012525058");
        patient.setGender("Female");
        patient.setMedicalHistory("None");
        patient.setUser(user);
    }

    @Test
    public void testCreatePatient() {
        when(patientRepository.save(any(Patient.class))).thenReturn(patient);
        Patient created = patientService.createPatient(user, "Thilaga", "8012525058", "Female", "None");
        assertNotNull(created);
        assertEquals("Thilaga", created.getFullName());
        verify(patientRepository, times(1)).save(any(Patient.class));
    }

    @Test
    public void testGetPatientByUser() {
        when(patientRepository.findByUser(user)).thenReturn(Optional.of(patient));
        Patient fetched = patientService.getPatientByUser(user);
        assertNotNull(fetched);
        assertEquals("Thilaga", fetched.getFullName());
        verify(patientRepository, times(1)).findByUser(user);
    }

    @Test
    public void testUpdatePatient() {
        when(patientRepository.findById(1L)).thenReturn(Optional.of(patient));
        when(patientRepository.save(any(Patient.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Patient updated = patientService.updatePatient(1L, "Ganesh", "9842446072", "Male", "Allergy");
        assertEquals("Ganesh", updated.getFullName());
        assertEquals("9842446072", updated.getPhone());
        assertEquals("Allergy", updated.getMedicalHistory());
        verify(patientRepository, times(1)).findById(1L);
        verify(patientRepository, times(1)).save(any(Patient.class));
    }

    @Test
    public void testDeletePatientWithAppointments() {
        when(appointmentService.getAppointmentsForPatient(1L)).thenReturn(Arrays.asList(new com.appointment.Patient.Medicine.and.Appointment.System.model.Appointment()));

        Exception exception = assertThrows(RuntimeException.class, () -> {
            patientService.deletePatient(1L);
        });
        assertTrue(exception.getMessage().contains("Cannot delete patient with existing appointments"));
        verify(appointmentService, times(1)).getAppointmentsForPatient(1L);
    }

    @Test
    public void testDeletePatientSuccess() {
        when(appointmentService.getAppointmentsForPatient(1L)).thenReturn(Collections.emptyList());
        doNothing().when(patientRepository).deleteById(1L);

        assertDoesNotThrow(() -> patientService.deletePatient(1L));
        verify(appointmentService, times(1)).getAppointmentsForPatient(1L);
        verify(patientRepository, times(1)).deleteById(1L);
    }
}
