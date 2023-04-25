package org.example.myCacheService.controller;

import org.example.myCacheService.cacheRepo.CacheRepository;
import org.example.myCacheService.service.Bond;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/bond")
@Validated
public class CacheController {
    @Autowired
    private CacheRepository<String, Bond> cacheRepository;

    @PostMapping
    public ResponseEntity<Void> saveBond(@RequestBody Bond bond){
        var key = bond.getBondId();
        if (key==null){
            return ResponseEntity.badRequest().build();
        }
        cacheRepository.put(key,bond);
        return ResponseEntity.ok().build();
    }

    @GetMapping(value = "/{bond_id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<GetResponse<Bond>> getBondById(@PathVariable String bond_id){
        if (bond_id.isEmpty()){
            return ResponseEntity.badRequest().build();
        }
        var bond = cacheRepository.get(bond_id);
        if (bond!=null){
            return  ResponseEntity.ok().body(GetResponse.success(bond));
        }
        return ResponseEntity.ok().body(GetResponse.error("can't find key in cache"));
    }
}
