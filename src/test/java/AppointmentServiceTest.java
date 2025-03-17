

import com.appointment.Patient.Medicine.and.Appointment.System.model.Appointment;
import com.appointment.Patient.Medicine.and.Appointment.System.model.Doctor;
import com.appointment.Patient.Medicine.and.Appointment.System.model.Patient;
import com.appointment.Patient.Medicine.and.Appointment.System.repository.AppointmentRepository;
import com.appointment.Patient.Medicine.and.Appointment.System.repository.DoctorRepository;
import com.appointment.Patient.Medicine.and.Appointment.System.repository.PatientRepository;
import com.appointment.Patient.Medicine.and.Appointment.System.service.AppointmentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class AppointmentServiceTest {

    @Mock
    private AppointmentRepository appointmentRepository;
    @Mock
    private DoctorRepository doctorRepository;
    @Mock
    private PatientRepository patientRepository;

    @InjectMocks
    private AppointmentService appointmentService;

    private Doctor doctor;
    private Patient patient;
    private Appointment appointment;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        // Initialize Doctor
        doctor = new Doctor();
        doctor.setId(1L);
        doctor.setFullName("Dr. Smith");

        // Initialize Patient
        patient = new Patient();
        patient.setId(1L);
        patient.setFullName("John Doe");

        // Initialize Appointment
        appointment = new Appointment();
        appointment.setId(1L);
        appointment.setDoctor(doctor);
        appointment.setPatient(patient);
        appointment.setDateTime(LocalDateTime.of(2023, 3, 16, 14, 0));
        appointment.setStatus("SCHEDULED");
    }

    @Test
    public void testCreateAppointmentSuccess() {
        // Arrange
        LocalDateTime appointmentTime = LocalDateTime.of(2023, 3, 16, 14, 0);
        when(doctorRepository.findById(1L)).thenReturn(Optional.of(doctor));
        when(patientRepository.findById(1L)).thenReturn(Optional.of(patient));
        when(appointmentRepository.save(any(Appointment.class))).thenAnswer(invocation -> {
            Appointment saved = invocation.getArgument(0);
            saved.setId(1L);
            return saved;
        });

        // Act
        Appointment createdAppointment = appointmentService.createAppointment(1L, 1L, appointmentTime);

        // Assert
        assertNotNull(createdAppointment, "Created appointment should not be null");
        assertEquals(1L, createdAppointment.getId(), "Appointment ID should be set");
        assertEquals("SCHEDULED", createdAppointment.getStatus(), "Status should be SCHEDULED");
        assertEquals(appointmentTime, createdAppointment.getDateTime(), "Appointment time must match");
        assertEquals(doctor, createdAppointment.getDoctor(), "Doctor should match");
        assertEquals(patient, createdAppointment.getPatient(), "Patient should match");

        verify(doctorRepository, times(1)).findById(1L);
        verify(patientRepository, times(1)).findById(1L);
        verify(appointmentRepository, times(1)).save(any(Appointment.class));
    }

    @Test
    public void testGetAppointmentByIdNotFound() {
        // Arrange
        when(appointmentRepository.findById(2L)).thenReturn(Optional.empty());

        // Act & Assert
        Exception exception = assertThrows(RuntimeException.class, () -> {
            appointmentService.getAppointmentById(2L);
        });
        assertTrue(exception.getMessage().contains("Appointment not found"), "Exception message should contain 'Appointment not found'");
        verify(appointmentRepository, times(1)).findById(2L);
    }

    @Test
    public void testUpdateAppointmentSuccess() {
        // Arrange
        Long newDoctorId = 1L;
        Long newPatientId = 1L;
        LocalDateTime newDateTime = LocalDateTime.of(2023, 3, 17, 10, 0);
        String newStatus = "COMPLETED";

        when(appointmentRepository.findById(1L)).thenReturn(Optional.of(appointment));
        when(doctorRepository.findById(newDoctorId)).thenReturn(Optional.of(doctor));
        when(patientRepository.findById(newPatientId)).thenReturn(Optional.of(patient));
        when(appointmentRepository.save(any(Appointment.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Appointment updatedAppointment = appointmentService.updateAppointment(1L, newDoctorId, newPatientId, newDateTime, newStatus);

        // Assert
        assertNotNull(updatedAppointment, "Updated appointment should not be null");
        assertEquals(newDateTime, updatedAppointment.getDateTime(), "DateTime should be updated");
        assertEquals(newStatus, updatedAppointment.getStatus(), "Status should be updated");

        verify(appointmentRepository, times(1)).findById(1L);
        verify(doctorRepository, times(1)).findById(newDoctorId);
        verify(patientRepository, times(1)).findById(newPatientId);
        verify(appointmentRepository, times(1)).save(any(Appointment.class));
    }

    @Test
    public void testDeleteAppointment() {
        // Arrange
        doNothing().when(appointmentRepository).deleteById(1L);

        // Act
        appointmentService.deleteAppointment(1L);

        // Assert
        verify(appointmentRepository, times(1)).deleteById(1L);
    }
}
