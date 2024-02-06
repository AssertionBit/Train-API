package assertionbit.trainapi.services;

import assertionbit.trainapi.entities.RouteEntity;
import assertionbit.trainapi.entities.SitEntity;
import assertionbit.trainapi.entities.TicketEntity;
import assertionbit.trainapi.entities.TrainEntity;
import assertionbit.trainapi.messages.ErrorResponse;
import assertionbit.trainapi.messages.TicketGroupRequst;
import assertionbit.trainapi.repositories.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;

@Service
@RestController
@RequestMapping(value = "/api/v1/")
public class ApiService {
    protected Logger logger = LoggerFactory.getLogger(ApiService.class);
    protected TicketRepository ticketRepository;
    protected TrainRepository trainRepository;
    protected RoutesRepository routesRepository;
    protected WagonRepository wagonRepository;
    protected SitsRepository sitsRepository;

    public ApiService(
            TicketRepository ticketRepository,
            TrainRepository trainRepository,
            RoutesRepository routesRepository,
            WagonRepository wagonRepository,
            SitsRepository sitsRepository
    ) {
        this.ticketRepository = ticketRepository;
        this.trainRepository = trainRepository;
        this.routesRepository = routesRepository;
        this.wagonRepository = wagonRepository;
        this.sitsRepository = sitsRepository;
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

    @PostMapping("/ticket/reserve/{routeId}/{trainId}/{wagonId}/{sitId}")
    public ResponseEntity<?> reserveTicket(
            @PathVariable Long routeId,
            @PathVariable Long trainId,
            @PathVariable Long wagonId,
            @PathVariable Long sitId
    ) {
        var trains = trainRepository.getAllTrainsDetailedStream();
        var train = trains
                .filter(trainEntity -> trainEntity.getId().equals(trainId))
                .toList();
        if(train.isEmpty()) {
            return ResponseEntity
                    .status(404)
                    .body(new ErrorResponse("No such train found"));
        }

        // Wagon selector
        var wagon = train.get(0).getWagons().stream()
                .filter(s -> s.getId().equals(wagonId))
                .toList();
        if(wagon.isEmpty()) {
            return ResponseEntity
                    .status(404)
                    .body(new ErrorResponse("No such wagon of train found found"));
        }

        // Sit array for filtering
        var sit = wagon.get(0).getSitEntities().stream()
                .filter(s -> s.getId().equals(sitId))
                .toList();
        if(sit.isEmpty()) {
            return ResponseEntity
                    .status(404)
                    .body(new ErrorResponse("Sit doesn't exists in this wagon"));
        }

        if(sit.get(0).getIs_taken()) {
            return ResponseEntity
                    .status(400)
                    .body(new ErrorResponse("Sit already taken"));
        }

        var entity = new TicketEntity();
        entity.setCreation_date(LocalDateTime.now());

        ticketRepository.addTicket(
                entity,
                routeId,
                sitId,
                null
        );

        return ResponseEntity
                .status(200)
                .build();
    }

    @PostMapping("/ticket/group/{routeId}/{trainId}")
    public ResponseEntity<?> responseGroupTicket(
        @PathVariable Long routeId,
        @PathVariable Long trainId,
        @RequestBody TicketGroupRequst request
    ) {
        if(request.sitId.size() != request.wagonId.size()) {
            return ResponseEntity
                    .status(400)
                    .body(new ErrorResponse("WagonId's and sitId's are not equal"));
        }

        var trains = trainRepository.getAllTrainsDetailedStream();
        var train = trains
                .filter(trainEntity -> trainEntity.getId().equals(trainId))
                .toList();
        if(train.isEmpty()) {
            return ResponseEntity
                    .status(404)
                    .body(new ErrorResponse("No such train found"));
        }

        var sitsCollection = new ArrayList<SitEntity>();
        // Wagons validation
        for(Integer wagonId : request.wagonId) {
            // Wagon selector
            var wagon = train.get(0).getWagons().stream()
                    .filter(s -> s.getId().equals(wagonId))
                    .toList();
            if(wagon.isEmpty()) {
                return ResponseEntity
                        .status(404)
                        .body(new ErrorResponse("No such wagon of train found found"));
            }
            sitsCollection.addAll(wagon.get(0).getSitEntities());
        }

        if(sitsCollection.size() != request.sitId.size()) {
            return ResponseEntity
                    .status(400)
                    .body(new ErrorResponse("No such amount of sits in this train to process"));
        }

        ticketRepository.addGroupTickets(
            routeId,
            request.sitId
        );

        return ResponseEntity.status(200).build();
    }

    @DeleteMapping("/ticket/group/{groupId}")
    public ResponseEntity<?> deleteGroup(
        @PathVariable Long groupId
    ) {
        if(!ticketRepository.isGroupExists(groupId)) {
            return ResponseEntity
                    .status(400)
                    .body(new ErrorResponse("Such group doesn't exists"));
        }

        if(!ticketRepository.isGroupDeletable(groupId)) {
            return ResponseEntity
                    .status(400)
                    .body(new ErrorResponse("Could not delete group before 2 hours of start"));
        }

        ticketRepository.deleteTicketGroup(groupId);

        return ResponseEntity.status(200).build();
    }

    @DeleteMapping("/ticket/reserve/{ticketId}")
    public ResponseEntity<?> reserveTicket(
            @PathVariable Long ticketId
    ) {
        var ticket_list = ticketRepository
                .getAllTicketsStream()
                .filter(s -> s.getId().equals(ticketId))
                .toList();

        if(ticket_list.isEmpty()) {
            return ResponseEntity
                    .status(404)
                    .body(new ErrorResponse("Ticket not found"));
        }

        var route_id = ticketRepository.getTicketRoute(ticketId);
        if(route_id.isEmpty()) {
            return ResponseEntity
                    .status(500)
                    .body(new ErrorResponse("Internal error: ticket exist when route doesn't"));
        }

        var begin_time = routesRepository
                .getBeginTimeById(Long.valueOf(route_id.get(0)));
        if(begin_time.isEmpty()) {
            return ResponseEntity
                    .status(500)
                    .body(new ErrorResponse("Internal error: begin time doesn't exist on route_trains"));
        }

        if(!begin_time.get(0).plusHours(2).isAfter(LocalDateTime.now())) {
            return ResponseEntity
                    .status(400)
                    .body(new ErrorResponse("Could not delete after two hours of estimated time"));
        }

        if(!ticketRepository.deleteTicket(ticketId)) {
            return ResponseEntity
                    .status(400)
                    .body(new ErrorResponse("Ticket in group, deletion not permitted"));
        }

        return ResponseEntity
                .status(200)
                .build();
    }
}
