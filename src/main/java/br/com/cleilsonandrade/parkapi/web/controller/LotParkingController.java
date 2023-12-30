package br.com.cleilsonandrade.parkapi.web.controller;

import java.io.IOException;
import java.net.URI;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import br.com.cleilsonandrade.parkapi.entity.ClientParking;
import br.com.cleilsonandrade.parkapi.jwt.JwtUserDetails;
import br.com.cleilsonandrade.parkapi.repository.projection.ClientParkingProjection;
import br.com.cleilsonandrade.parkapi.service.ClientParkingService;
import br.com.cleilsonandrade.parkapi.service.ClientService;
import br.com.cleilsonandrade.parkapi.service.JasperService;
import br.com.cleilsonandrade.parkapi.service.LotParkingService;
import br.com.cleilsonandrade.parkapi.web.dto.LotParkingCreateDTO;
import br.com.cleilsonandrade.parkapi.web.dto.LotParkingResponseDTO;
import br.com.cleilsonandrade.parkapi.web.dto.PageableDTO;
import br.com.cleilsonandrade.parkapi.web.dto.mapper.ClientParkingMapper;
import br.com.cleilsonandrade.parkapi.web.dto.mapper.PageableMapper;
import br.com.cleilsonandrade.parkapi.web.exception.ErrorMessage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Tag(name = "Parking lot", description = "Contains all operations related to resources for registering, editing and reading a parking lots")
@RestController
@RequestMapping("/parking-lots")
@RequiredArgsConstructor
public class LotParkingController {
    private final LotParkingService lotParkingService;
    private final ClientParkingService clientParkingService;
    private final ClientService clientService;
    private final JasperService jasperService;

    @Operation(summary = "Create a new check-in in parking lot", description = "Resource for entering a vehicle into the parking lot"
            +
            "Request requires use of a 'Bearer token'. Restricted access to Role='ADMIN'", security = @SecurityRequirement(name = "security"), responses = {
                    @ApiResponse(responseCode = "201", description = "Resource created successfully", headers = @Header(name = HttpHeaders.LOCATION, description = "URL access to the created resource"), content = @Content(mediaType = "application/json", schema = @Schema(implementation = LotParkingResponseDTO.class))),
                    @ApiResponse(responseCode = "403", description = "Feature not allowed for profile ADMIN", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),
                    @ApiResponse(responseCode = "404", description = "Possible causes: <br/>"
                            + "- Customer CPF not registered in the system; </br>"
                            + "- No free parkings were found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),
                    @ApiResponse(responseCode = "422", description = "Resource not processed due to invalid input data", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),
            })
    @PostMapping("/check-in")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<LotParkingResponseDTO> checkIn(@RequestBody @Valid LotParkingCreateDTO dto) {
        ClientParking clientParking = ClientParkingMapper.toClientParking(dto);
        lotParkingService.checkIn(clientParking);

        LotParkingResponseDTO responseDTO = ClientParkingMapper.toDto(clientParking);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequestUri()
                .path("/{receipt}")
                .buildAndExpand(clientParking.getReceipt())
                .toUri();

        return ResponseEntity.created(location).body(responseDTO);
    }

    @Operation(summary = "Locate a parked vehicle", description = "Resource for returning a parked vehicle "
            + "hair receipt number "
            +
            "Request requires use of a 'Bearer token'", security = @SecurityRequirement(name = "security"), parameters = {
                    @Parameter(in = ParameterIn.PATH, name = "receipt", description = "Receipt number generated by check-in")
            }, responses = {
                    @ApiResponse(responseCode = "200", description = "Resource located successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = LotParkingResponseDTO.class))),
                    @ApiResponse(responseCode = "404", description = "Receipt number not found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),
            })
    @GetMapping("/check-in/{receipt}")
    @PreAuthorize("hasAnyRole('ADMIN', 'CLIENT')")
    public ResponseEntity<LotParkingResponseDTO> getByReceipt(@PathVariable String receipt) {
        ClientParking clientParking = clientParkingService.searchByReceipt(receipt);
        LotParkingResponseDTO dto = ClientParkingMapper.toDto(clientParking);

        return ResponseEntity.ok(dto);
    }

    @Operation(summary = "Checkout operation", description = "Resource for leaving a vehicle from the parking lot "
            +
            "Request requires use of a 'Bearer token'. Restricted access to Role='ADMIN'", security = @SecurityRequirement(name = "security"), parameters = {
                    @Parameter(in = ParameterIn.PATH, name = "receipt", description = "Receipt number generated by check-in")
            }, responses = {
                    @ApiResponse(responseCode = "200", description = "Resource updated successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = LotParkingResponseDTO.class))),
                    @ApiResponse(responseCode = "404", description = "Receipt number not found or vehicle has already been checked out", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),
            })
    @PutMapping("/check-out/{receipt}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<LotParkingResponseDTO> checkOut(@PathVariable String receipt) {
        ClientParking clientParking = lotParkingService.checkOut(receipt);
        LotParkingResponseDTO dto = ClientParkingMapper.toDto(clientParking);

        return ResponseEntity.ok(dto);
    }

    @Operation(summary = "Find client parking records by CPF", description = "Find client parking records by CPF"
            +
            "Request requires use of a 'Bearer token'. Restricted access to Role='ADMIN'", security = @SecurityRequirement(name = "security"), parameters = {
                    @Parameter(in = ParameterIn.QUERY, name = "cpf", content = @Content(schema = @Schema(type = "integer", defaultValue = "0")), description = "'N' of the CPF for the client to be consulted"),
                    @Parameter(in = ParameterIn.QUERY, name = "page", content = @Content(schema = @Schema(type = "integer", defaultValue = "0")), description = "Represents page returned"),
                    @Parameter(in = ParameterIn.QUERY, name = "size", content = @Content(schema = @Schema(type = "integer", defaultValue = "20")), description = "Represents the total number of elements per page"),
                    @Parameter(in = ParameterIn.QUERY, name = "sort", hidden = true, array = @ArraySchema(schema = @Schema(type = "string", defaultValue = "id,asc")), description = "Represents the ordering of results. Accepts multiple sorting criteria are supported"),
            }, responses = {
                    @ApiResponse(responseCode = "200", description = "Resource located successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = PageableDTO.class))),
                    @ApiResponse(responseCode = "403", description = "Feature not allowed for profile CLIENT", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),
            })
    @GetMapping("/cpf/{cpf}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PageableDTO> getAllLotParkingsByCpf(@PathVariable String cpf,
            @PageableDefault(size = 5, sort = "dateEntry", direction = Direction.ASC) Pageable pageable) {
        Page<ClientParkingProjection> projection = clientParkingService.searchAllByClientCpf(cpf, pageable);
        PageableDTO dto = PageableMapper.toDto(projection);
        return ResponseEntity.ok(dto);
    }

    @Operation(summary = "Find authenticated client records", description = "Find authenticated client records"
            +
            "Request requires use of a 'Bearer token'. Restricted access to Role='CLIENT'", security = @SecurityRequirement(name = "security"), parameters = {
                    @Parameter(in = ParameterIn.QUERY, name = "size", content = @Content(schema = @Schema(type = "integer", defaultValue = "20")), description = "Represents the total number of elements per page"),
                    @Parameter(in = ParameterIn.QUERY, name = "sort", hidden = true, array = @ArraySchema(schema = @Schema(type = "string", defaultValue = "id,asc")), description = "Represents the ordering of results. Accepts multiple sorting criteria are supported"),
            }, responses = {
                    @ApiResponse(responseCode = "200", description = "Resource located successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = PageableDTO.class))),
                    @ApiResponse(responseCode = "403", description = "Feature not allowed for profile ADMIN", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),
            })
    @GetMapping
    @PreAuthorize("hasRole('CLIENT')")
    public ResponseEntity<PageableDTO> getAllLotParkingsOfClient(@AuthenticationPrincipal JwtUserDetails user,
            @PageableDefault(size = 5, sort = "dateEntry", direction = Direction.ASC) Pageable pageable) {
        Page<ClientParkingProjection> projection = clientParkingService.searchAllByClientCpf(user.getId(), pageable);
        PageableDTO dto = PageableMapper.toDto(projection);
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/reports")
    @PreAuthorize("hasRole('CLIENT')")
    public ResponseEntity<Void> getReports(HttpServletResponse response, @AuthenticationPrincipal JwtUserDetails user)
            throws IOException {
        String cpf = clientService.searchByUserId(user.getId()).getCpf();
        jasperService.addParams("CPF", cpf);

        byte[] bytes = jasperService.generatedPdf();

        response.setContentType(MediaType.APPLICATION_PDF_VALUE);
        response.setHeader("Content-disposition", "inline; filename=" + System.currentTimeMillis() + ".pdf");
        response.getOutputStream().write(bytes);

        return ResponseEntity.ok().build();
    }
}
