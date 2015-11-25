package com.mobium.google_places_api.models;

import com.annimon.stream.Optional;

/**
 *  on 27.08.15.
 */
public class AutoCompleteResult {
    private Status status;
    private Item predictions[];

    public AutoCompleteResult() {
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public void setPredictions(Item[] predictions) {
        this.predictions = predictions;
    }

    public Status getStatus() {
        return status;
    }

    public Optional<Item[]> getPredictions() {
        return Optional.of(predictions);
    }


    public static class Item {
        private String description;
        private String place_id;

        private transient Term terms;
        private AutoCompleteType[] types;


        public Item() {
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getPlace_id() {
            return place_id;
        }

        public void setPlace_id(String place_id) {
            this.place_id = place_id;
        }


        public Term getTerms() {
            return terms;
        }

        public void setTerms(Term terms) {
            this.terms = terms;
        }

        public AutoCompleteType[] getTypes() {
            return types;
        }

        public void setTypes(AutoCompleteType[] types) {
            this.types = types;
        }

        @Override
        public String toString() {
            return description;
        }

    }

    public static class Term {
        public int offset;
        public String value;

        public Term() {
        }
    }


}
