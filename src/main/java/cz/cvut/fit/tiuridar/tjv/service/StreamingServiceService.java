package cz.cvut.fit.tiuridar.tjv.service;

import cz.cvut.fit.tiuridar.tjv.domain.StreamingService;

import java.util.Collection;

public interface StreamingServiceService extends CrudService<StreamingService, Long> {
    Iterable<StreamingService> returnBest();
    void addToANewStreamingService(Long filmId, Long streamingServiceId);
    void deleteFromAStreamingService(Long filmId, Long streamingServiceId);
}
