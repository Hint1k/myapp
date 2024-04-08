package com.example.myapp.rest.validationJsonParsing;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class Predictions {

    @JsonProperty("matched_substrings")
    private List<MatchedSubstrings> matchedSubstrings;

    public Predictions() {
    }

    public List<MatchedSubstrings> getMatchedSubstrings() {
        return matchedSubstrings;
    }

    public void setMatchedSubstrings(List<MatchedSubstrings> matchedSubstrings) {
        this.matchedSubstrings = matchedSubstrings;
    }
}