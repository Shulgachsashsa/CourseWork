package service_interface;
import dto.RegRequest;
import dto.RegResponse;

public interface RegService {
    RegResponse registration(RegRequest request);
}
