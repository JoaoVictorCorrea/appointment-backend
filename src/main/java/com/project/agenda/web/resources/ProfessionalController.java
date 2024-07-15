package com.project.agenda.web.resources;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.project.agenda.domain.services.ProfessionalService;
import com.project.agenda.dto.TimeSlotResponse;

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
