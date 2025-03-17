

import com.appointment.Patient.Medicine.and.Appointment.System.model.Doctor;
import com.appointment.Patient.Medicine.and.Appointment.System.model.Medication;
import com.appointment.Patient.Medicine.and.Appointment.System.model.Patient;
import com.appointment.Patient.Medicine.and.Appointment.System.repository.MedicationRepository;
import com.appointment.Patient.Medicine.and.Appointment.System.repository.PatientRepository;
import com.appointment.Patient.Medicine.and.Appointment.System.repository.DoctorRepository;
import com.appointment.Patient.Medicine.and.Appointment.System.service.MedicationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class MedicationServiceTest {

    @Mock
    private MedicationRepository medicationRepository;
    @Mock
    private PatientRepository patientRepository;
    @Mock
    private DoctorRepository doctorRepository;

    @InjectMocks
    private MedicationService medicationService;

    private Patient patient;
    private Doctor doctor;
    private Medication medication;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        patient = new Patient();
        patient.setId(1L);
        patient.setFullName("Harini");

        doctor = new Doctor();
        doctor.setId(1L);
        doctor.setFullName("Dr. Sahana");

        medication = new Medication();
        medication.setId(1L);
        medication.setName("Ibuprofen");
        medication.setDosage("200mg");
        medication.setFrequency("Twice a day");
        medication.setDuration("5 days");
        medication.setPatient(patient);
        medication.setPrescribingDoctor(doctor);
    }

    @Test
    public void testCreateMedicationWithDoctor() {
        when(patientRepository.findById(1L)).thenReturn(Optional.of(patient));
        when(doctorRepository.findById(1L)).thenReturn(Optional.of(doctor));
        when(medicationRepository.save(any(Medication.class))).thenReturn(medication);

        Medication created = medicationService.createMedication(1L, 1L, "Ibuprofen", "200mg", "Twice a day", "5 days");
        assertNotNull(created);
        assertEquals("Ibuprofen", created.getName());
        assertEquals(doctor, created.getPrescribingDoctor());

        verify(patientRepository, times(1)).findById(1L);
        verify(doctorRepository, times(1)).findById(1L);
        verify(medicationRepository, times(1)).save(any(Medication.class));
    }

    @Test
    public void testCreateMedicationWithoutDoctor() {
        when(patientRepository.findById(1L)).thenReturn(Optional.of(patient));
        // doctorId is null, so no lookup.
        when(medicationRepository.save(any(Medication.class))).thenReturn(medication);

        Medication created = medicationService.createMedication(1L, null, "Ibuprofen", "200mg", "Twice a day", "5 days");
        assertNotNull(created);
        assertEquals("Ibuprofen", created.getName());
        assertNull(created.getPrescribingDoctor());

        verify(patientRepository, times(1)).findById(1L);
        verify(medicationRepository, times(1)).save(any(Medication.class));
    }

    @Test
    public void testUpdateMedication() {
        when(medicationRepository.findById(1L)).thenReturn(Optional.of(medication));
        when(medicationRepository.save(any(Medication.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Medication updated = medicationService.updateMedication(1L, "400mg", "Three times a day", "7 days");
        assertEquals("400mg", updated.getDosage());
        assertEquals("Three times a day", updated.getFrequency());
        assertEquals("7 days", updated.getDuration());

        verify(medicationRepository, times(1)).findById(1L);
        verify(medicationRepository, times(1)).save(any(Medication.class));
    }

    @Test
    public void testDeleteMedication() {
        doNothing().when(medicationRepository).deleteById(1L);
        medicationService.deleteMedication(1L);
        verify(medicationRepository, times(1)).deleteById(1L);
    }
}
