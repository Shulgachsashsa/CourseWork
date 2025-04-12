package service_interface;
import connect.Response;
import dto.RegistrationDTO;

public interface RegistrationService {
    Response registration(RegistrationDTO request);
}
