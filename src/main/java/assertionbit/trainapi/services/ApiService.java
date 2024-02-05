package assertionbit.trainapi.services;

import assertionbit.trainapi.entities.RouteEntity;
import assertionbit.trainapi.entities.TrainEntity;
import assertionbit.trainapi.repositories.RoutesRepository;
import assertionbit.trainapi.repositories.TrainRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;


@Service
@RestController
@RequestMapping(value = "/api/v1/")
public class ApiService {
    protected Logger logger = LoggerFactory.getLogger(ApiService.class);
    protected TrainRepository trainRepository;
    protected RoutesRepository routesRepository;

    public ApiService(
            TrainRepository trainRepository,
            RoutesRepository routesRepository
    ) {
        this.trainRepository = trainRepository;
        this.routesRepository = routesRepository;
    }

    @GetMapping("/trains")
    public ResponseEntity<?> getAllTrains(
        @RequestParam(name = "detailed", defaultValue = "false") String detailed
    ) {
        var allTrainsInfo = new ArrayList<HashMap<String, Object>>();
        var stream = detailed.equalsIgnoreCase("true") ?
            this.trainRepository.getAllTrainsDetailedStream()
                    .map(TrainEntity::toHashMap)
            : this.trainRepository
                .getAllTrainsStream()
                .map(TrainEntity::toHashMap)
                .peek(s -> s.remove("wagons"));

        stream.forEach(allTrainsInfo::add);

        return ResponseEntity
                .status(200)
                .body(allTrainsInfo);
    }

    @GetMapping("/routes")
    public ResponseEntity<?> getAllRoutes(
            @RequestParam(name = "detailed", defaultValue = "false") String detailed
    ) {
        var allRoutesInfo = new ArrayList<HashMap<String, Object>>();

        var data = detailed.equalsIgnoreCase("true") ?
            routesRepository.getAllRoutesDetailed().stream().map(RouteEntity::toHashMap)
            : routesRepository.getAllRoutes().stream().map(RouteEntity::toHashMap)
                .peek(s -> s.remove("trains"));

        data.forEach(allRoutesInfo::add);

        return ResponseEntity
                .status(200)
                .body(allRoutesInfo);
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
