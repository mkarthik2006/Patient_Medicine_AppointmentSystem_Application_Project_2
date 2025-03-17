

import com.appointment.Patient.Medicine.and.Appointment.System.dto.DoctorCreationDto;
import com.appointment.Patient.Medicine.and.Appointment.System.model.Doctor;
import com.appointment.Patient.Medicine.and.Appointment.System.model.Role;
import com.appointment.Patient.Medicine.and.Appointment.System.model.User;
import com.appointment.Patient.Medicine.and.Appointment.System.repository.DoctorRepository;
import com.appointment.Patient.Medicine.and.Appointment.System.service.AppointmentService;
import com.appointment.Patient.Medicine.and.Appointment.System.service.DoctorService;
import com.appointment.Patient.Medicine.and.Appointment.System.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class DoctorServiceTest {

    @Mock
    private DoctorRepository doctorRepository;
    @Mock
    private UserService userService;
    @Mock
    private AppointmentService appointmentService;
    @InjectMocks
    private DoctorService doctorService;

    private DoctorCreationDto doctorDto;
    private User doctorUser;
    private Doctor doctor;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        doctorDto = new DoctorCreationDto();
        doctorDto.setFullName("Dr Karthik");
        doctorDto.setEmail("karthik@gmail.com");
        doctorDto.setPassword("password");
        doctorDto.setSpecialization("Pediatrics");
        doctorDto.setPhone("9600848309");

        doctorUser = new User();
        doctorUser.setId(1L);
        doctorUser.setName("Dr Karthik");
        doctorUser.setEmail("karthik@gmail.com");
        doctorUser.setPassword("encodedPassword");
        doctorUser.setRole(Role.DOCTOR);

        doctor = new Doctor();
        doctor.setId(1L);
        doctor.setFullName("Dr. Karthik");
        doctor.setSpecialization("Pediatrics");
        doctor.setPhone("9600848309");
        doctor.setUser(doctorUser);
    }

    @Test
    public void testCreateDoctor() {
        when(userService.registerUser(doctorDto.getFullName(), doctorDto.getEmail(), doctorDto.getPassword(), Role.DOCTOR))
                .thenReturn(doctorUser);
        when(doctorRepository.save(any(Doctor.class))).thenReturn(doctor);

        Doctor created = doctorService.createDoctor(doctorDto);
        assertNotNull(created);
        assertEquals("Dr. Karthik", created.getFullName());
        assertEquals("Pediatrics", created.getSpecialization());
        assertEquals("9600848309", created.getPhone());

        verify(userService, times(1)).registerUser(doctorDto.getFullName(), doctorDto.getEmail(), doctorDto.getPassword(), Role.DOCTOR);
        verify(doctorRepository, times(1)).save(any(Doctor.class));
    }

    @Test
    public void testGetDoctorByUser() {
        when(doctorRepository.findByUser(doctorUser)).thenReturn(Optional.of(doctor));
        Doctor fetched = doctorService.getDoctorByUser(doctorUser);
        assertNotNull(fetched);
        assertEquals(1L, fetched.getId());
        verify(doctorRepository, times(1)).findByUser(doctorUser);
    }

    @Test
    public void testUpdateDoctor() {
        when(doctorRepository.findById(1L)).thenReturn(Optional.of(doctor));
        when(doctorRepository.save(any(Doctor.class))).thenReturn(doctor);

        Doctor updated = doctorService.updateDoctor(1L, "Dr. Karthik Updated", "General Medicine", "555-5678");
        assertEquals("Dr. Karthik Updated", updated.getFullName());
        assertEquals("General Medicine", updated.getSpecialization());
        assertEquals("9566738309", updated.getPhone());

        verify(doctorRepository, times(1)).findById(1L);
        verify(doctorRepository, times(1)).save(any(Doctor.class));
    }

    @Test
    public void testDeleteDoctorWithExistingAppointments() {
        when(appointmentService.getAppointmentsForDoctor(1L)).thenReturn(Arrays.asList(new com.appointment.Patient.Medicine.and.Appointment.System.model.Appointment()));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            doctorService.deleteDoctor(1L);
        });
        assertTrue(exception.getMessage().contains("Cannot delete doctor with existing appointments"));
    }

    @Test
    public void testDeleteDoctorSuccess() {
        when(appointmentService.getAppointmentsForDoctor(1L)).thenReturn(Collections.emptyList());
        doNothing().when(doctorRepository).deleteById(1L);

        assertDoesNotThrow(() -> {
            doctorService.deleteDoctor(1L);
        });

        verify(appointmentService, times(1)).getAppointmentsForDoctor(1L);
        verify(doctorRepository, times(1)).deleteById(1L);
    }
}
