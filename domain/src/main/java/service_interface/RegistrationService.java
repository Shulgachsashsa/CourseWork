package service_interface;
import connect.Response;
import dto.UserDTO;

public interface RegistrationService {
    Response registration(UserDTO request);
}
