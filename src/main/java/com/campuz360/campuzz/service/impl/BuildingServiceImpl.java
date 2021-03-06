package com.campuz360.campuzz.service.impl;

import com.campuz360.campuzz.service.BuildingService;
import com.campuz360.campuzz.domain.Building;
import com.campuz360.campuzz.repository.BuildingRepository;
import com.campuz360.campuzz.repository.search.BuildingSearchRepository;
import com.campuz360.campuzz.web.rest.dto.BuildingDTO;
import com.campuz360.campuzz.web.rest.mapper.BuildingMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing Building.
 */
@Service
@Transactional
public class BuildingServiceImpl implements BuildingService{

    private final Logger log = LoggerFactory.getLogger(BuildingServiceImpl.class);
    
    @Inject
    private BuildingRepository buildingRepository;
    
    @Inject
    private BuildingMapper buildingMapper;
    
    @Inject
    private BuildingSearchRepository buildingSearchRepository;
    
    /**
     * Save a building.
     * 
     * @param buildingDTO the entity to save
     * @return the persisted entity
     */
    public BuildingDTO save(BuildingDTO buildingDTO) {
        log.debug("Request to save Building : {}", buildingDTO);
        Building building = buildingMapper.buildingDTOToBuilding(buildingDTO);
        building = buildingRepository.save(building);
        BuildingDTO result = buildingMapper.buildingToBuildingDTO(building);
        buildingSearchRepository.save(building);
        return result;
    }

    /**
     *  Get all the buildings.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public Page<Building> findAll(Pageable pageable) {
        log.debug("Request to get all Buildings");
        Page<Building> result = buildingRepository.findAll(pageable); 
        return result;
    }

    /**
     *  Get one building by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public BuildingDTO findOne(Long id) {
        log.debug("Request to get Building : {}", id);
        Building building = buildingRepository.findOne(id);
        BuildingDTO buildingDTO = buildingMapper.buildingToBuildingDTO(building);
        return buildingDTO;
    }

    /**
     *  Delete the  building by id.
     *  
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Building : {}", id);
        buildingRepository.delete(id);
        buildingSearchRepository.delete(id);
    }

    /**
     * Search for the building corresponding to the query.
     *
     *  @param query the query of the search
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<Building> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Buildings for query {}", query);
        return buildingSearchRepository.search(queryStringQuery(query), pageable);
    }
}
