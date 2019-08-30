<template>
    <div>
        <div v-if="showDestinationSearch">
            <b-row>
                <b-col cols="3">
                    <b-button @click="goBack" class="buttonMarginsBottom" size="sm">Back</b-button>
                </b-col>
                <b-col cols="9">
                    <div><strong>Riddle: "{{searchedRiddle}}"</strong></div>
                </b-col>
            </b-row>
            <b-button v-if="!showDestinationSearchCollapse" @click="showDestinationSearchCollapse = true;
            showSelectedDestination = false; foundDestinationsKey += 1" variant="primary" block>Search Again</b-button>
            <b-collapse v-model="showDestinationSearchCollapse" id="destinations-collapse">
                <found-destinations
                        :key="foundDestinationsKey"
                        :search-public="true"
                        :destination-types="destinationTypes" @destination-click="destinationClicked">
                </found-destinations>
            </b-collapse>

            <b-alert v-model="showError" variant="danger" class="buttonMarginsTop" dismissible>
                Incorrect, try again.
            </b-alert>

            <b-collapse v-model="showSelectedDestination" id="selected-destination-collapse">
                <b-list-group-item href="#" class="flex-column align-items-start"
                                   id="selectedDestination"
                                   :disabled="selectedDestination === {}">
                    <div class="d-flex w-100 justify-content-between">
                        <h5 class="mb-1" v-if="selectedDestination.name">
                            {{selectedDestination.name}}
                        </h5>
                        <h5 class="mb-1" v-else>Select a Destination</h5>

                        <small>
                            <div class="d-flex justify-content-right">
                                <b-button variant="primary"
                                          @click="checkGuess"
                                          v-if="selectedDestination.name">Check Guess
                                </b-button>
                            </div>
                        </small>
                    </div>
                    <p>
                        {{selectedDestination.district}}
                    </p>
                    <p>
                        {{selectedDestination.country}}
                    </p>
                </b-list-group-item>
            </b-collapse>


        </div>
        <div v-else>
            <b-button @click="$emit('show-quest-attempt', false)" class="buttonMarginsBottom show-only-mobile" size="sm">Back</b-button>
            <h2 class="page-title">{{questAttempt.questAttempted.title}}</h2>

            <b-alert v-model="guessSuccess" variant="success" dismissible>
                Success!
            </b-alert>

            <b-progress
                    :value="questAttempt.progress"
                    :max="100"
                    :variant="(questAttempt.completed ? 'success' : 'primary')"
                    class="mb-3">
            </b-progress>

            <b-list-group>

                <!-- List the solved objectives in the quest attempt -->
                <b-list-group-item v-for="objective in questAttempt.solved" href="#"
                                   class="flex-column align-items-start"
                                   :key="objective.id"
                                   draggable="false">
                    <div class="d-flex w-100 justify-content-between">
                        <p class="mb-1 mobile-text font-weight-bold">{{objective.riddle}}</p>
                        <small>
                            <b-img src="../../../static/check_mark.png" fluid></b-img>
                        </small>
                    </div>
                    <p class="mb-1 mobile-text">
                        {{objective.destination.name}}
                    </p>
                </b-list-group-item>

                <!-- If we have an objective to solve, display it -->
                <b-list-group-item href="#"
                                   class="d-flex justify-content-between align-items-center"
                                   draggable="false" v-if="questAttempt.toSolve != null">
                    <span class="mobile-text font-weight-bold">{{questAttempt.toSolve.riddle}}</span>
                    <b-button size="sm" variant="primary" @click="destinationSearch(questAttempt.toSolve.riddle)">Solve</b-button>
                </b-list-group-item>
                <!-- If we have an objective to check in to, display it -->
                <b-list-group-item href="#"
                                   class="d-flex justify-content-between align-items-center"
                                   draggable="false" v-if="questAttempt.toCheckIn != null">
                    <div>
                    <span class="mobile-text font-weight-bold">{{questAttempt.toCheckIn.riddle}}</span>
                        <p class="mb-1 mobile-text">
                            {{questAttempt.toCheckIn.destination.name}}
                        </p>
                    </div>
                    <b-button class="no-wrap-text" size="sm" variant="warning" @click="getCurrentLocation">Check In</b-button>
                </b-list-group-item>
                <b-alert v-model="showNotValidCheckIn" variant="warning" class="buttonMarginsTop">
                    You are not at the required location, you are {{getHowClose()}} away.
                </b-alert>

                <!-- List the remaining unsolved objectives in the quest attempt -->
                <b-list-group-item v-for="objective in questAttempt.unsolved" href="#"
                                   class="d-flex justify-content-between align-items-center"
                                   :key="objective.id"
                                   draggable="false" disabled>
                    <span class="mobile-text font-weight-bold">{{objective.riddle}}</span>
                </b-list-group-item>
            </b-list-group>
        </div>
    </div>
</template>

<script>
    import FoundDestinations from "../destinations/destinationSearchList";
    export default {
        name: "activeQuestSolve",

        props: {
            questAttempt: Object
        },

        data() {
            return {
                destinationTypes: [],
                showDestinationSearch: false,
                selectedDestination: {},
                guessSuccess: false,
                showError: false,
                showDestinationSearchCollapse: true,
                showSelectedDestination: false,
                foundDestinationsKey: 0,
                currentLocation: {
                    latitude: null,
                    longitude: null
                },
                foundLocation: false,
                validCheckIn: false,
                showNotValidCheckIn: false,
                totalDistance: null,
                searchedRiddle: null
            }
        },

        watch: {
            foundLocation() {
                if (this.foundLocation) {
                    if (this.validCheckIn) {
                        this.sendCheckInRequest();
                    } else {
                        this.showNotValidCheckIn = true;
                        let self = this;
                        setTimeout(function() {
                            self.showNotValidCheckIn = false;
                        }, 3000);
                    }
                    this.foundLocation = false;
                }
            }
        },

        components: {
            FoundDestinations
        },

        mounted() {
            this.getDestinationTypes(destinationTypesTemp => this.destinationTypes = destinationTypesTemp);
        },

        methods: {
            /**
             * Sends a request to check the users guess for a given quest attempt.
             * Displays appropriate messages upon receiving a response.
             */
            checkGuess() {
                let self = this;
                return fetch('/v1/quests/attempt/' + this.questAttempt.id + '/guess/' + this.selectedDestination.id, {
                    method: "POST",
                    accept: "application/json"
                })
                    .then(response => response.json())
                    .then((data) => {
                        if (data.guessResult) {
                            self.goBack();
                            self.guessSuccess = true;
                            self.$emit('updated-quest-attempt', data.attempt);
                            setTimeout(function() {
                                self.guessSuccess = false;
                            }, 3000)
                        } else {
                            self.showError = true;
                            setTimeout(function() {
                                self.showError = false;
                            }, 3000)
                        }
                    })
            },


            /**
             * Resets all the appropriate variables and hides the search destination page when searching for a correct
             * answer to an objectives' riddle.
             */
            goBack() {
                this.showDestinationSearch = false;
                this.showDestinationSearchCollapse = true;
                this.showSelectedDestination = false;
                this.selectedDestination = {};
            },


            /**
             * Gets the current location using geolocation.
             */
            getCurrentLocation() {
                if (navigator.geolocation) {
                    navigator.geolocation.getCurrentPosition(this.currentPosition);
                } else {
                    this.$bvToast.toast('Unable to Get Current Location', {
                        title: `Geolocation Error`,
                        variant: "danger",
                        autoHideDelay: "3000",
                        solid: true
                    });
                }
            },


            /**
             * Helper function of getCurrentLocation that saves lat/long to appropriate variables.
             *
             * @param position  the current position on the map.
             */
            currentPosition(position) {
                this.currentLocation.latitude = position.coords.latitude;
                this.currentLocation.longitude = position.coords.longitude;
                this.checkCurrentLocation();
            },


            /**
             * Checks if the user's current location is within the required radius for the current objective.
             */
            checkCurrentLocation() {
                let destinationLocation = new google.maps.LatLng(
                    this.questAttempt.toCheckIn.destination.latitude,
                    this.questAttempt.toCheckIn.destination.longitude
                );
                let currentLocation = new google.maps.LatLng(this.currentLocation.latitude, this.currentLocation.longitude);
                this.totalDistance = google.maps.geometry.spherical.computeDistanceBetween(destinationLocation, currentLocation) * 0.001;
                this.validCheckIn = this.questAttempt.toCheckIn.radius >= this.totalDistance;
                this.foundLocation = true;
            },


            /**
             * Sends the request to check in to the current objective.
             */
            sendCheckInRequest() {
                let self = this;
                return fetch('/v1/quests/attempt/' + this.questAttempt.id + '/checkIn', {
                    method: "POST",
                    accept: "application/json"
                })
                    .then(response => response.json())
                    .then((data) => {
                        // Response has points gained and attempt, get attempt
                        self.$emit('updated-quest-attempt', data.attempt);
                    })
            },


            /**
             * Retrieves the different destination types from the backend.
             *
             * @param updateDestinationTypes    the list to be updated with the specified destination types.
             * @returns {Promise<any | never>}  the returned promise.
             */
            getDestinationTypes(updateDestinationTypes) {
                return fetch(`/v1/destinationTypes`, {
                    accept: "application/json"
                })
                    .then(response => response.json())
                    .then(updateDestinationTypes);
            },


            /**
             * Handles the emit event for when a destination is clicked in the search destination form.
             *
             * @param destination   the destination that has been clicked.
             */
            destinationClicked(destination) {
                this.selectedDestination = destination;
                this.showDestinationSearchCollapse = false;
                this.showSelectedDestination = true;
            },

            destinationSearch(riddle) {
                this.showDestinationSearch = true;
                this.searchedRiddle = riddle;
            },


            /**
             * Returns a string value for the distance from the user's current location to the location of the
             * objective destination.
             */
            getHowClose() {
                if (this.totalDistance >= 1) {
                    return String(Number(Math.round(this.totalDistance+'e3')+'e-3')) + " kms";
                }
                return String(Number(Math.round((this.totalDistance * 1000)+'e0')+'e-0')) + " metres";

            }
        }

    }
</script>