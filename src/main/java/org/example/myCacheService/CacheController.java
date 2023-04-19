package org.example.myCacheService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/bond")
public class CacheController {
    @Autowired
    private CacheRepository<String,Bond> cacheRepository;

    @PostMapping
    public void saveBond(@Validated @RequestBody Bond bond){
        String key = bond.getKey();
        cacheRepository.put(key,bond);
    }

    @GetMapping(value = "/{bond_id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<GetResponse<Bond>> getBondById(@PathVariable String bond_id){
        Bond bond = cacheRepository.get(bond_id);

        if (bond!=null){

            return  ResponseEntity.ok().body(GetResponse.success(bond));
        }
        return ResponseEntity.ok().body(GetResponse.error("can't find key in cache"));
    }
}
