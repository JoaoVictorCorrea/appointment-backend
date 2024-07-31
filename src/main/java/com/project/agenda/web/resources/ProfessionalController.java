package com.project.agenda.web.resources;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.project.agenda.domain.services.ProfessionalService;
import com.project.agenda.dto.ProfessionalRequest;
import com.project.agenda.dto.ProfessionalResponse;
import com.project.agenda.dto.ProfessionalWithAreaResponse;
import com.project.agenda.dto.TimeSlotResponse;

import jakarta.validation.Valid;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

@RestController
@RequestMapping("professionals")
@Validated
public class ProfessionalController {

    @Autowired
    private ProfessionalService professionalService;

    @GetMapping
    public ResponseEntity<Page<ProfessionalResponse>> getProfessionals(
            @RequestParam(name = "name_like", defaultValue = "") String name,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size) {

        return ResponseEntity.ok(professionalService.findByNameContainingIgnoreCase(name, page, size));
    }
    
    @GetMapping("{id}")
    public ResponseEntity<ProfessionalResponse> getProfessional(@PathVariable long id) {

        ProfessionalResponse professional = professionalService.getById(id);

        return ResponseEntity.ok(professional);
    }

    @PostMapping
    public ResponseEntity<ProfessionalResponse> save(@Validated @RequestBody ProfessionalRequest professionalRequest) {

        ProfessionalResponse professional = professionalService.save(professionalRequest);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(professional.id())
                .toUri();

        return ResponseEntity.created(location).body(professional);
    }

    @PutMapping("{id}")
    public ResponseEntity<Void> updateProfessional(@PathVariable long id, @Valid @RequestBody ProfessionalRequest professionalRequest) {

        professionalService.update(id, professionalRequest);

        return ResponseEntity.ok().build();
    }
    
    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteProfessional(@PathVariable long id) {

        professionalService.deleteById(id);

        return ResponseEntity.noContent().build();
    }
    
    @PostMapping("{id_professional}/areas/{id_area}")
    public ResponseEntity<ProfessionalWithAreaResponse> associateProfessionalWithArea(@PathVariable long id_professional, @PathVariable int id_area) {

        ProfessionalWithAreaResponse professional = professionalService.associateProfessionalWithArea(id_professional, id_area);

        return ResponseEntity.ok().body(professional);
    }
    
    @DeleteMapping("{id_professional}/areas/{id_area}")
    public ResponseEntity<Void> disassociateProfessionalWithArea(@PathVariable long id_professional, @PathVariable int id_area) {

        professionalService.disassociateProfessionalWithArea(id_professional, id_area);

        return ResponseEntity.noContent().build();
    }
    
    @GetMapping("{id}/availability-times")
    public ResponseEntity<List<TimeSlotResponse>> getAvailabilityTimes(@PathVariable long id,
                @RequestParam(name = "date", required = false)
                @NotNull(message = "O parâmetro data é requirido.")
                @FutureOrPresent(message = "A data deve ser igual ou maior que a data atual.") 
                LocalDate date) {

        List<TimeSlotResponse> timeSlots = professionalService.getAvailabilityTimesFromProfessional(id, date);

        return ResponseEntity.ok(timeSlots);
    }

    @GetMapping("{id}/availability-days")
    public ResponseEntity<List<Integer>> getAvailabilityDays(@PathVariable long id,
                @RequestParam(name = "month", required = false)
                @NotNull(message = "O parâmetro mês é requirido.")
                @Pattern(regexp = "^(0?[1-9]|1[0-2])$", message = "Formato do mês inválido. Utilize um valor de 1 à 12")  
                String month,
                        
                @RequestParam(name = "year", required = false) 
                @NotNull(message = "O parâmetro ano é requirido.")
                @Min(value = 1900, message = "O ano deve ser maior que 1900")
                @Pattern(regexp = "^\\d{4}$", message = "Formato do ano inválido. Utilize o formato 'yyyy'") 
                String year
            ) {
                
        List<Integer> days = professionalService.getAvailabilityDaysFromProfessional(id, Integer.valueOf(month) , Integer.valueOf(year));

        return ResponseEntity.ok(days);
    }
}
