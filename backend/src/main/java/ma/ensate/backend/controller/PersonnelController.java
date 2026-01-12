package ma.ensate.backend.controller;

import ma.ensate.backend.dto.PersonnelOptionDto;
import ma.ensate.backend.repository.PersonnelRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/personnels")
public class PersonnelController {

    private final PersonnelRepository personnelRepository;

    public PersonnelController(PersonnelRepository personnelRepository) {
        this.personnelRepository = personnelRepository;
    }

    // GET /api/personnels
    @GetMapping
    public List<PersonnelOptionDto> all() {
        return personnelRepository.findAllOptions();
    }

    // GET /api/personnels/search?q=aya
    @GetMapping("/search")
    public List<PersonnelOptionDto> search(@RequestParam("q") String q) {
        if (q == null || q.trim().isEmpty()) return personnelRepository.findAllOptions();
        return personnelRepository.searchOptions(q.trim());
    }
}
