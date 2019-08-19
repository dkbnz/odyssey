<template>
    <div>
        <div v-if="showDestinationSearch">
            <b-button v-if="!showDestinationSearchCollapse" @click="showDestinationSearchCollapse = true; showSelectedDestination = false;" variant="primary" block>Search Again</b-button>
            <b-collapse v-model="showDestinationSearchCollapse">
                <found-destinations
                        :search-public="true"
                        :destination-types="destinationTypes" @destination-click="destinationClicked">
                </found-destinations>
            </b-collapse>

            <b-alert v-model="showError" variant="danger" dismissible>
                Incorrect, try again.
            </b-alert>

            <b-collapse v-model="showSelectedDestination">
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


            <!--{{guessSuccess}}-->
        </div>
        <div v-else>
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

                <b-list-group-item v-for="objective in questAttempt.solved" href="#"
                                   class="flex-column align-items-start"
                                   :key="objective.id"
                                   draggable="false">
                    <div class="d-flex w-100 justify-content-between">
                        <p class="mb-1 mobile-text">{{objective.riddle}}</p>
                        <small>
                            <b-img src="../../../static/check_mark.png" fluid></b-img>
                        </small>
                    </div>
                    <p class="mb-1 mobile-text">
                        {{objective.destination.name}}
                    </p>
                </b-list-group-item>

                <b-list-group-item href="#"
                                   class="d-flex justify-content-between align-items-center"
                                   draggable="false" v-if="questAttempt.current != null">
                    <span class="mobile-text">{{questAttempt.current.riddle}}</span>
                    <b-button size="sm" variant="primary" @click="showDestinationSearch = true">Solve</b-button>
                </b-list-group-item>

                <b-list-group-item v-for="objective in questAttempt.unsolved" href="#"
                                   class="d-flex justify-content-between align-items-center"
                                   :key="objective.id"
                                   draggable="false" disabled>
                    <span class="mobile-text">{{objective.riddle}}</span>
                </b-list-group-item>
            </b-list-group>
        </div>
    </div>
</template>

<script>
    import FoundDestinations from "../destinations/destinationSearchList";
    export default {
        name: "questAttemptSolve",

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
                showSelectedDestination: false
            }
        },

        components: {
            FoundDestinations
        },

        mounted() {
            this.getDestinationTypes(destinationTypesTemp => this.destinationTypes = destinationTypesTemp);
        },

        methods: {
            checkGuess() {
                let self = this;
                return fetch('/v1/quests/attempt/' + this.questAttempt.id + '/guess/' + this.selectedDestination.id, {
                    method: "POST",
                    accept: "application/json"
                })
                    .then(response => response.json())
                    .then((data) => {
                        if (data.guessResult) {
                            self.showDestinationSearch = false;
                            self.guessSuccess = true;
                            self.$emit('updated-quest-attempt', data.attempt);
                        } else {
                            self.showError = true;
                        }
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


            destinationClicked(destination) {
                this.selectedDestination = destination;
                this.showDestinationSearchCollapse = false;
                this.showSelectedDestination = true;
            }
        }

    }
</script>

<style scoped>

</style>