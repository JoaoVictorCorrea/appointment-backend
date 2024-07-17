package com.project.agenda.web.resources;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.agenda.domain.services.AreaService;
import com.project.agenda.dto.AreaResponse;
import com.project.agenda.dto.ProfessionalResponse;

@RestController
@RequestMapping("areas")
public class AreaController {
    
    @Autowired
    private AreaService areaService;

    @GetMapping
    public ResponseEntity<List<AreaResponse>> getAreas() {

        return ResponseEntity.ok(areaService.getAreas());
    }
    
    @GetMapping("{id}/professionals")
    public ResponseEntity<List<ProfessionalResponse>> getProfessionalsByArea(@PathVariable int id) {
        
        return ResponseEntity.ok(areaService.getProfessionalsByArea(id));
    }
}
