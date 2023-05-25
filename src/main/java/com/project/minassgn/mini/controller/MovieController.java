package com.project.minassgn.mini.controller;

import com.project.miniassgn.models.Movie;
import com.project.miniassgn.services.MovieService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/movie")


public class MovieController {
    private static Logger logger = LoggerFactory.getLogger(MovieService.class);
    @Autowired
    private MovieService movieService;
    @GetMapping
    public List<Movie> findAll(){
        logger.info("findAll books " + this.getClass().getName());
        return movieService.findAll();
    }
    //Retrieving the movies.csv file
    @PostMapping("/importCsvFile")
    public List<Movie> importCsv(){

        return movieService.readCsv();
    }
