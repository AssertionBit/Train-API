package assertionbit.trainapi.services;

import assertionbit.trainapi.jooq.public_.tables.Trains;
import assertionbit.trainapi.repositories.TrainRepository;
import org.jooq.DSLContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;


@Service
@RestController
@RequestMapping(value = "/api/v1/")
public class ApiService {
    protected Logger logger = LoggerFactory.getLogger(ApiService.class);
    protected TrainRepository trainRepository;

    public ApiService(TrainRepository trainRepository) {
        this.trainRepository = trainRepository;
    }

    @GetMapping
    public ResponseEntity<?> getAll() {
        var allTrainsInfo = new ArrayList<HashMap<String, Object>>();
        logger.info("Accepting request for all trains");
        trainRepository
                .getAllTrains()
                .forEach(obj -> {
                    allTrainsInfo.add(obj.toHashMap());
                });

        var response = new HashMap<String, Object>();
        response.put("version", "1.0.0");
        response.put("trains", allTrainsInfo);

        return ResponseEntity
                .status(200)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
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
