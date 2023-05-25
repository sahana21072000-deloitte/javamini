package com.project.minassgn.mini.service;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;


import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import com.project.miniassgn.models.Movie;
import com.project.miniassgn.repositories.MovieRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.*;

import java.io.IOException;
import java.io.Reader;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;


@Service

public class MovieService {
    private static Logger logger = LoggerFactory.getLogger(MovieService.class);
    @Autowired
    private Movie movie;
    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private AmazonDynamoDB amazonDynamoDB;

    @Scheduled(fixedDelay = 24 * 60 * 60 * 1000) // Run the task once per day, adjust the delay as needed

    public void syncData() {
// Read the CSV file and retrieve the new movie details
        List<Movie> newMovies = readCsv();

// Sync the new movie details to the datastore
        List<Movie> existingMovies = (List<Movie>) movieRepository.findAll();

        for (Movie newMovie : newMovies) {
            boolean isMovieExists = existingMovies.stream()
                    .anyMatch(movie -> movie.getTitle().equals(newMovie.getTitle()));

            if (!isMovieExists) {
                movieRepository.save(newMovie);
            }
        }
    }

    public List<Movie> findAll() {

        logger.info("findAll books " + this.getClass().getName());
        return movieRepository.findAll();
    }

    public List<Movie> readCsv() {
        List<Movie> newMovies = new ArrayList<>();

        String[] record = new String[20];

        try {
// create a reader
            Reader reader = Files.newBufferedReader(Paths.get("C:\\Users\\ndandur\\Documents\\movies.csv"));

// create csv reader
            CSVReader csvReader = new CSVReader(reader);

// read one record at a time
            String[] rec;
            while ((rec = csvReader.readNext()) != null) {
                System.out.println("ID: " + rec[0]);
                movie.setImdb_title_id(rec[0]);
                movie.setTitle(rec[1]);
                movie.setOriginal_title(rec[2]);
                movie.setYear(rec[3]);
                movie.setDate_published(rec[4]);
                movie.setGenre(rec[5]);
                movie.setDuration(rec[6]);
                movie.setCountry(rec[7]);
                movie.setLanguage(rec[8]);
                movie.setDirector(rec[9]);
                movie.setWriter(rec[10]);
                movie.setProduction_company(rec[11]);
                movie.setActors(rec[12]);
                movie.setDescription(rec[13]);
                movie.setAvg_vote(rec[14]);
                movie.setVotes(rec[15]);
                movie.setBudget(rec[16]);
                movie.setUsa_gross_income(rec[17]);
                movie.setWorlwide_gross_income(rec[18]);
                movie.setMetasocre(rec[19]);
                movie.setReviews_from_users(rec[20]);
                movie.setReviews_from_critics(rec[21]);
                movieRepository.save(movie);
                newMovies.add(movie);
            }

// close readers
            csvReader.close();
            reader.close();

        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (CsvValidationException e) {
            throw new RuntimeException(e);
        }

        return newMovies;

    }

    public Movie save(Movie movie) {
        logger.info("save book " + this.getClass().getName());
        return movieRepository.save(movie);
    }
}