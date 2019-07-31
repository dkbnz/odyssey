<template>

    <div>

        <h1 class="page-title">{{ heading }} a Treasure Hunt!</h1>

        <b-alert dismissible v-model="showError" variant="danger">{{errorMessage}}</b-alert>

        <!-- Displays success alert and progress bar on trip creation as a loading bar
        for the trip being added to the database -->
        <b-alert
                :show="dismissCountDown"
                @dismiss-count-down="countDownChanged"
                @dismissed="dismissCountDown=0"
                dismissible
                variant="success">
            <p>Trip Successfully Saved</p>
            <b-progress
                    :max="dismissSecs"
                    :value="dismissCountDown"
                    height="4px"
                    variant="success"
            ></b-progress>
        </b-alert>

        <b-row>
            <b-col>
                    <b-form>

                        <b-container fluid>
                            <b-form-group
                                    id="treasure_hunt_riddle-field"
                                    label="Treasure Hunt Riddle:"
                                    label-for="treasure_hunt_riddle">
                                <b-form-textarea :type="'expandable-text'"
                                              id="treasure_hunt_riddle"
                                              trim
                                              v-model="inputTreasureHunt.riddle"></b-form-textarea>
                            </b-form-group>
                        </b-container>


                        <b-form @reset="resetDestForm">
                            <b-container fluid>
                                <b-row>
                                    <b-col>
                                        <h6 class="mb-1">Selected Destination:</h6>
                                        <b-list-group>
                                            <b-list-group-item href="#" class="flex-column align-items-start"
                                                               v-if="selectedDestination"
                                                               id="selectedDestination"
                                                               :disabled="selectedDestination.length === '{}'">
                                                <div class="d-flex w-100 justify-content-between">
                                                    <h5 class="mb-1" v-if="selectedDestination.name">
                                                        {{selectedDestination.name}}
                                                    </h5>
                                                    <h5 class="mb-1" v-else>Select a Destination</h5>

                                                </div>

                                                <p>
                                                    {{selectedDestination.district}}
                                                </p>
                                                <p>
                                                    {{selectedDestination.country}}
                                                </p>
                                            </b-list-group-item>
                                        </b-list-group>
                                    </b-col>

                                    <b-col>
                                        <b-form-group
                                                id="startDate-field"
                                                label="Start Date:"
                                                label-for="startDate">
                                            <b-row>
                                            <b-col cols="6">
                                                <b-form-input :type="'date'"
                                                              id="startDate"
                                                              min='getCurrentDate()'
                                                              max='9999-12-31'
                                                              trim
                                                              v-model="inputTreasureHunt.startDate"
                                                              :state="validateStartDate">

                                                </b-form-input>
                                            </b-col>

                                            <b-col cols="6">
                                                <b-form-input :type="'time'"
                                                              id="startTime"
                                                              min='getCurrentTime()'
                                                              max=''
                                                              trim
                                                              v-model="inputTreasureHunt.startTime"
                                                              :state="validateStartTime">
                                                </b-form-input>
                                            </b-col>
                                            </b-row>

                                        </b-form-group>
                                        <b-form-group
                                                id="endDate-field"
                                                label="Expiration Date:"
                                                label-for="endDate">
                                            <b-col cols="6"></b-col>
                                            <b-col cols="6"></b-col>

                                            <b-row>
                                                <b-col cols="6">
                                                    <b-form-input :type="'date'"
                                                                  id="endDate"
                                                                  min='getCurrentDate()'
                                                                  max='9999-12-31'
                                                                  trim
                                                                  v-model="inputTreasureHunt.endDate"
                                                                  :state="validateEndDate">

                                                    </b-form-input>
                                                </b-col>

                                                <b-col cols="6">
                                                    <b-form-input :type="'time'"
                                                                  id="endTime"
                                                                  min='getCurrentTime()'
                                                                  max=''
                                                                  trim
                                                                  v-model="inputTreasureHunt.endTime"
                                                                  :state="validateEndTime">
                                                    </b-form-input>
                                                </b-col>
                                            </b-row>
                                        </b-form-group>
                                    </b-col>


                                </b-row>

                                <!--<b-button @click="checkDestination"-->
                                <!--class="mr-2 float-right"-->
                                <!--variant="primary">-->
                                <!--Add Destination-->
                                <!--</b-button>-->

                            </b-container>
                        </b-form>
                    </b-form>

                <b-row>
                    <b-col cols="8">
                        <b-button @click="validateTreasureHunt" block variant="primary">
                            Save
                        </b-button>
                    </b-col>
                    <b-col>
                        <b-button @click="cancelCreate" block>
                            Cancel
                        </b-button>
                    </b-col>
                </b-row>
            </b-col>
        </b-row>

    </div>
</template>

<script>
    import DestinationSidebar from "../destinations/destinationSidebar";
    import BCol from "bootstrap-vue/es/components/layout/col";
    export default {
        name: "addTreasureHunt",

        components: {
            BCol,
            DestinationSidebar
        },

        props: {
            profile: Object,
            inputTreasureHunt: {
                default: function () {
                    return {
                        id: null,
                        riddle: "",
                        startDate: "",
                        endDate: "",
                    }
                }
            },
            newDestination: Object,
            selectedDestination: {},
            heading: String,
            containerClass: {
                default: function() {
                    return 'containerWithNav';
                }
            }
        },


        data() {
            return {
                destination: {},
                startDate: this.getDateString(),
                startTime: this.getTimeString(),
                endDate: this.getDateString(),
                endTime: "23:59",
                showError: false,
                showDateError: false,
                errorMessage: "",
                successTripAddedAlert: false,
                dismissSecs: 3,
                dismissCountDown: 0,
                savingTreasureHunt: false,
                letTreasureHuntSaved: false,
                selectedDestination: {}
            }
        },

        mounted() {
            this.getTreasureHuntDestination(destinationSolution => this.destination = destinationSolution);
            this.splitDates();
        },

        computed: {
            /**
             * Gets the current date+time as a Date object
             * @returns Current Datetime
             */
            getCurrentDate() {
                return new Date();
            },


            /**
             * Checks that the start date is not after the end date, and is not before the current date for new hunts
             * @returns true if start date is valid
             */
            validateStartDate() {
                if ((this.startDate < this.getDateString() && !this.inputTreasureHunt.id)) {
                    return false;
                }
                if (this.startDate > this.endDate) {
                    return false;
                }
                return true;
            },

            /**
             * Checks that the start time is not after or the same as the end time if the dates are the same,
             * and that the start time is not before the current time if the current date is today
             * @returns true if start time is valid
             */
            validateStartTime() {
                if (this.startDate === this.endDate) {
                    if (this.startTime >= this.endTime) {
                        return false;
                    }
                }
                if (this.startDate === this.getDateString() && !this.inputTreasureHunt.id) {
                    if (this.startTime < this.getTimeString()) {
                        return false;
                    }
                }
                return true;
            },

            /**
             * Checks that the end date is not before the start date, and is not before the current date for new hunts
             * @returns true if end date is valid
             */
            validateEndDate() {
                if (this.endDate < this.getDateString() && !this.inputTreasureHunt.id) {
                    return false;
                }
                if (this.endDate < this.startDate) {
                    return false;
                }
                return true;
            },

            /**
             * Checks that the end time is not before or the same as the start time if the dates are the same
             * @returns true if end time is valid
             */
            validateEndTime() {
                if (this.startDate === this.endDate) {
                    if (this.endTime <= this.startTime) {
                        return false;
                    }
                }
                return true;
            }
        },


        methods: {


            /**
             * If all field validations pass on the active treasure hunt, saves the treasure hunt using either
             * updateHunt if there is an active editing ID or saveHunt otherwise (adding a new one).
             */
            validateTreasureHunt() {
                if (this.validateStartDate && this.validateStartTime && this.validateEndDate && this.validateEndTime) {
                    if (this.inputTreasureHunt.id != null) {
                        this.updateHunt();
                    } else {
                        this.saveHunt();
                    }
                }
                //TODO show error
            },


            /**
             * Gets the current date as a string in YYYY-MM-DD format, including padding O's on month/day
             * @returns Current Date in YYYY-MM-DD String Format
             */
            getDateString() {
                let today = this.getCurrentDate();
                return  today.getFullYear()+'-'+
                        ((today.getMonth()+1) < 10 ? "0" : "")
                        + (today.getMonth()+1)+'-'+
                        (today.getDate() < 10 ? "0" : "") +
                        today.getDate();
            },

            /**
             * Gets the current time as a string in HH:MM format, including padding O's
             * @returns Current Time in HH:MM String Format
             */
            getTimeString() {
                let today = this.getCurrentDate();
                return ((today.getHours) < 10 ? "0" : "") +
                    today.getHours() + ":"
                    + ((today.getMinutes) < 10 ? "0" : "") +
                    today.getMinutes();
            },


            /**
             * Used after the destination is added, resets the form for adding a destination.
             */
            resetDestForm() {
                this.selectedDestination = {};
            },


            /**
             * Creates formatted JSON of the currently active treasure hunt
             * @returns JSON string with fields 'riddle', 'destination_id', 'start_date', 'end_date'
             */
            assembleTreasureHunt() {
                let json = JSON.parse("riddle: " + this.riddle
                    + "," + "destination: " + this.selectedDestination + "startDate: " + this.startDate + this.startTime
                    + ":00" + "," + "endDate: " + this.endDate + this.endTime + ":00");
                // return JSON.parse(JSON.stringify('{"riddle" : "' + this.inputTreasureHunt.riddle + '",'
                //     + '"destination" : {' + JSON.parse("id:" + this.selectedDestination.id) + '},'
                //     + '"start_date" : "' + this.startDate + ' ' + this.startTime + ':00",'
                //     + '"end_date" : "' + this.endDate + ' ' + this.endTime + ':00"}'));
                console.log(json);
                return json;
            },


            /**
             * POST's the currently active destination to the treasureHunts endpoint in JSON format, for newly creating destinations
             */
            saveHunt() {
                let self = this;
                fetch('/v1/treasureHunts/' + this.profile.id, {
                    method: 'POST',
                    headers: {'content-type': 'application/json'},
                    body: this.assembleTreasureHunt()
                })
                    .then(this.checkStatus) //TODO Add Error Banner upon failure
                    .then(function() {
                        self.$emit('cancelCreate')
                    })
            },


            /**
             * PUT's the currently active destination to the treasureHunts endpoint in JSON format, for edited destinations
             */
            updateHunt() {
                let self = this;
                fetch('/v1/treasureHunts/' + this.inputTreasureHunt.id, {
                    method: 'PUT',
                    headers: {'content-type': 'application/json'},
                    body: this.assembleTreasureHunt()
                })
                    .then(this.checkStatus) //TODO Add Error Banner upon failure
                    .then(function() {
                        self.$emit('cancelCreate')
                    })
            },


            /**
             * Cancels the creation or editing of a treasure hunt by emitting a value to the treasureHuntList
             */
            cancelCreate() {
                this.$emit('cancelCreate');
            },


            /**
             * If the treasure hunt is being edited, retrieves the destination solution to the hunt
             */
            getTreasureHuntDestination(updateHuntDestination) {
                if (this.inputTreasureHunt.id != null) {
                    fetch(`/v1/treasureHuntDest/` + this.inputTreasureHunt.id, {
                        method: 'GET'
                    })
                        .then(this.checkStatus)
                        .then(this.parseJSON)
                        .then(updateHuntDestination)
                }
            },


            /**
             * Splits the dates of the inputTreasureHunt to put in the edit fields
             */
            splitDates() {
                if (this.inputTreasureHunt.id != null) {
                    this.startDate = this.inputTreasureHunt.startDate.split(" ")[0];
                    this.startTime = this.inputTreasureHunt.startDate.split(" ")[1];

                    this.endDate = this.inputTreasureHunt.endDate.split(" ")[0];
                    this.endTime = this.inputTreasureHunt.endDate.split(" ")[1];
                }
            },


            /**
             * Displays the countdown alert on the successful saving of a trip.
             */
            showAlert() {
                this.dismissCountDown = this.dismissSecs
            },


            /**
             * Used to allow an alert to countdown on the successful saving of a trip.
             *
             * @param dismissCountDown      the name of the alert.
             */
            countDownChanged(dismissCountDown) {
                this.dismissCountDown = dismissCountDown
            },


            /**
             * Checks the Http response for errors.
             *
             * @param response the retrieved Http response.
             * @returns {*} throws the Http response error.
             */
            checkStatus(response) {
                if (response.status >= 200 && response.status < 300) {
                    return response;
                }
                const error = new Error(`HTTP Error ${response.statusText}`);
                error.status = response.statusText;
                error.response = response;
                console.log(error);
                throw error;
            },


            /**
             * Converts the retrieved Http response to a Json format.
             *
             * @param response the Http response.
             * @returns the Http response body as Json.
             */
            parseJSON(response) {
                return response.json();
            }

        }
    }
</script>

<style scoped>

</style>


