package assertionbit.trainapi.services;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@Service
@RestController
@RequestMapping(value = "/api/v1/")
public class ApiService {
    @GetMapping
    public ResponseEntity<?> getAll() {
        return ResponseEntity.status(200).build();
    }

    @GetMapping("/group")
    public ResponseEntity<?> reserveGroup() {
        return ResponseEntity.status(200).build();
    }

    @PostMapping
    public ResponseEntity<?> postCancelTicket() {
        return ResponseEntity.status(200).build();
    }
}
