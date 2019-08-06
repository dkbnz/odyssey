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
                                                 v-model="inputTreasureHunt.riddle"
                                                 :state="validateRiddle"></b-form-textarea>
                            </b-form-group>
                        </b-container>


                        <b-form>
                            <b-container fluid>
                                <b-row>
                                    <b-col>
                                        <h6 class="mb-1">Selected Destination:</h6>
                                        <b-list-group @click="$emit('destination-select')">
                                            <b-list-group-item href="#" class="flex-column align-items-start"
                                                               v-if="selectedDestination"
                                                               id="selectedDestination"
                                                               :disabled="selectedDestination.length === '{}'"
                                                               :variant="checkDestinationState">
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
                        destination: null,
                        riddle: "",
                        startDate: "",
                        startTime: "",
                        endDate: "",
                        endTime: "23:59"
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
                showError: false,
                showDateError: false,
                errorMessage: "",
                successTripAddedAlert: false,
                dismissSecs: 3,
                dismissCountDown: 0,
                savingTreasureHunt: false,
                letTreasureHuntSaved: false
            }
        },

        watch: {
            selectedDestination() {
                this.inputTreasureHunt.destination = this.selectedDestination;
            }
        },

        mounted() {
            this.splitDates();
            this.editingTreasureHunt();
            this.setDateTimeString();
        },

        computed: {
            /**
             * For new hunts, checks the start date is after the current date.
             * For all other hunts, checks the start date is either the same as or before the end date.
             *
             * @returns {boolean} true if start date is valid.
             */
            validateStartDate() {
                console.log("Start date");
                // For a new hunt, the start date must be after today.
                if ((this.inputTreasureHunt.startDate < this.getDateString() && !this.inputTreasureHunt.id)) {
                    return false;
                }
                // Otherwise, checks the start date is equal to or before the end date.
                return this.inputTreasureHunt.startDate <= this.inputTreasureHunt.endDate;
            },


            /**
             * Checks that the start time is not after or the same as the end time if the dates are the same,
             * and that the start time is not before the current time if the current date is today.
             *
             * @returns {boolean} true if start time is valid.
             */
            validateStartTime() {
                console.log("Start time");
                // For new hunts, check the start time is after the current time.
                if (this.inputTreasureHunt.startDate === this.getDateString() && !this.inputTreasureHunt.id) {
                    if (this.inputTreasureHunt.startTime < this.getTimeString()) {
                        return false;
                    }
                }
                // If the dates are the same, check the start time is before the end time.
                if (this.inputTreasureHunt.startDate === this.inputTreasureHunt.endDate) {
                    if (this.inputTreasureHunt.startTime >= this.inputTreasureHunt.endTime) {
                        return false;
                    }
                }
                return true;
            },


            /**
             * Checks that the end time is not before or the same as the start time if the dates are the same.
             *
             * @returns {boolean} true if end time is valid.
             */
            validateEndTime() {
                console.log("End time");
                if (this.inputTreasureHunt.startDate === this.inputTreasureHunt.endDate) {
                    if (this.inputTreasureHunt.endTime <= this.inputTreasureHunt.startTime) {
                        return false;
                    }
                }
                return true;
            },


            /**
             * For new hunts, checks the end date is after the current date.
             * For all other hunts, checks the end date is either the same as or after the start date.
             *
             * @returns {boolean} true if end date is valid.
             */
            validateEndDate() {
                // For a new hunt, the end date must be after today.
                if (this.inputTreasureHunt.endDate < this.getDateString() && !this.inputTreasureHunt.id) {
                    return false;
                }
                // Otherwise, checks the end date is equal to or after the start date.
                return this.inputTreasureHunt.endDate >= this.inputTreasureHunt.startDate;
            },


            /**
             * Returns true if the inputted riddle has length greater than 0.
             *
             * @returns {Boolean} true if validated.
             */
            validateRiddle() {
              if(this.inputTreasureHunt.riddle.length > 0){
                  return true;
              }
              return null;
            },


            /**
             * Returns true if the input destination exists and matches the one selected in the sidebar and isn't empty.
             *
             * @returns {boolean} true if valid.
             */
            validateDestination() {
                return (this.inputTreasureHunt.destination !== null
                    && this.inputTreasureHunt.destination === this.selectedDestination
                    && this.inputTreasureHunt.destination.name !== undefined
                    && this.inputTreasureHunt.destination.name.length > 0);
            },


            /**
             * Checks the validity of the destination using validateDestination and returns the appropriate state for
             * display.
             *
             * @returns 'success' if destination is valid, 'secondary' otherwise.
             */
            checkDestinationState(){
                return this.validateDestination ? "success" : "secondary"
            },
        },

        methods: {
            /**
             * Gets the current date+time as a Date object.
             *
             * @returns Current Datetime.
             */
            getCurrentDate() {
                return new Date();
            },


            /**
             * sets the input values to be their proper string versions of current date/time.
             */
            setDateTimeString() {
                if (this.inputTreasureHunt.id === null) {
                    this.inputTreasureHunt.startDate = this.getDateString();
                    this.inputTreasureHunt.endDate = this.getDateString();
                    this.inputTreasureHunt.startTime = this.getTimeString();
                }
            },


            /**
             * Gets the current date as a string in YYYY-MM-DD format, including padding O's on month/day.
             *
             * @returns Current Date in YYYY-MM-DD String Format.
             */
            getDateString() {
                let today = this.getCurrentDate();
                return today.getFullYear()+'-'+
                    ((today.getMonth()+1) < 10 ? "0" : "")
                    + (today.getMonth()+1)+'-'+
                    (today.getDate() < 10 ? "0" : "") +
                    today.getDate();
            },


            /**
             * Gets the current time as a string in HH:MM format, including padding O's.
             *
             * @returns Current Time in HH:MM String Format.
             */
            getTimeString() {
                let today = this.getCurrentDate();
                return ((today.getHours()) < 10 ? "0" : "") +
                    today.getHours() + ":"
                    + ((today.getMinutes()) < 10 ? "0" : "") +
                    today.getMinutes();
            },


            /**
             * Fills the destination with the existing destination of a hunt when editing it.
             */
            editingTreasureHunt() {
                if (this.inputTreasureHunt.id !== null) {
                    this.selectedDestination = this.inputTreasureHunt.destination;
                }
            },


            /**
             * If all field validations pass on the active treasure hunt, saves the treasure hunt using either
             * updateHunt if there is an active editing ID or saveHunt otherwise (adding a new one).
             */
            validateTreasureHunt() {
                if (this.validateStartDate && this.validateStartTime && this.validateEndDate && this.validateEndTime
                    && this.validateDestination && this.validateRiddle) {
                    if (this.inputTreasureHunt.id !== null) {
                        this.updateHunt();
                    } else {
                        this.saveHunt();
                    }
                } else {
                    this.errorMessage = "Not all fields have valid information!";
                    this.showError = true;
                }

            },


            /**
             * Used after the destination is added, resets the form for adding a destination.
             */
            resetDestForm() {
                this.selectedDestination = {};
            },


            /**
             * Creates formatted JSON of the currently active treasure hunt.
             *
             * @returns JSON string with fields 'riddle', 'destination_id', 'start_date', 'end_date'.
             */
            assembleTreasureHunt() {
                this.joinDates();
                this.inputTreasureHunt.destination = {"id": this.inputTreasureHunt.destination.id};

                // Deletes the start time and end time fields as these are already joined onto the dates and are not
                // sent in the json body of the request separately.
                delete this.inputTreasureHunt.startTime;
                delete this.inputTreasureHunt.endTime;
            },


            /**
             * POST's the currently active destination to the treasureHunts endpoint in JSON format, for newly creating
             * destinations.
             */
            saveHunt() {
                this.assembleTreasureHunt();
                let self = this;
                fetch('/v1/treasureHunts/' + this.profile.id, {
                    method: 'POST',
                    headers: {'content-type': 'application/json'},
                    body: JSON.stringify(this.inputTreasureHunt)
                })
                    .then(this.checkStatus)
                    .then(function() {
                        self.$emit('successCreate', "Treasure Hunt Successfully Created");
                        self.$emit('cancelCreate')
                    })
            },


            /**
             * PUT's the currently active destination to the treasureHunts endpoint in JSON format, for edited
             * destinations.
             */
            updateHunt() {
                this.assembleTreasureHunt();
                let self = this;
                fetch('/v1/treasureHunts/' + this.inputTreasureHunt.id, {
                    method: 'PUT',
                    headers: {'content-type': 'application/json'},
                    body: JSON.stringify(this.inputTreasureHunt)
                })
                    .then(this.checkStatus)
                    .then(function() {
                        self.$emit('cancelCreate')
                    })
            },


            /**
             * Cancels the creation or editing of a treasure hunt by emitting a value to the treasureHuntList.
             */
            cancelCreate() {
                this.$emit('cancelCreate');
            },


            /**
             * Splits the dates of the inputTreasureHunt to put in the edit fields.
             */
            splitDates() {
                if (this.inputTreasureHunt.id !== null) {
                    this.inputTreasureHunt.startDate = new Date(this.inputTreasureHunt.startDate).toLocaleString();
                    let startDate = this.inputTreasureHunt.startDate;
                    this.inputTreasureHunt.startDate = this.inputTreasureHunt.startDate.split(", ")[0];
                    this.inputTreasureHunt.startDate = this.inputTreasureHunt.startDate.split("/").reverse().join("-");
                    this.inputTreasureHunt.startTime = startDate.split(" ")[1];
                    this.inputTreasureHunt.startTime = this.inputTreasureHunt.startTime.split("+")[0];
                    this.inputTreasureHunt.startTime = this.inputTreasureHunt.startTime.split("-")[0];

                    this.inputTreasureHunt.endDate = new Date(this.inputTreasureHunt.endDate).toLocaleString();
                    let endDate = this.inputTreasureHunt.endDate;
                    this.inputTreasureHunt.endDate = this.inputTreasureHunt.endDate.split(", ")[0];
                    this.inputTreasureHunt.endDate = this.inputTreasureHunt.endDate.split("/").reverse().join("-");
                    this.inputTreasureHunt.endTime = endDate.split(" ")[1];
                    this.inputTreasureHunt.endTime = this.inputTreasureHunt.endTime.split("+")[0];
                    this.inputTreasureHunt.endTime = this.inputTreasureHunt.endTime.split("-")[0];
                }
            },


            /**
             * Combines dates and times together from input fields and adds :00 on the end for seconds.
             */
            joinDates() {
                let timeOffset = this.formatOffset();

                if(this.inputTreasureHunt.startTime.length === 5) {
                    this.inputTreasureHunt.startTime += ":00";
                }

                if(this.inputTreasureHunt.endTime.length === 5) {
                    this.inputTreasureHunt.endTime += ":00";
                }

                this.inputTreasureHunt.startDate = this.inputTreasureHunt.startDate + " "
                    + this.inputTreasureHunt.startTime + timeOffset;

                this.inputTreasureHunt.endDate = this.inputTreasureHunt.endDate + " "
                    + this.inputTreasureHunt.endTime + timeOffset;

            },


            /**
             * Gets the local time offset and pads it to be 4 numbers long.
             */
            formatOffset() {
                let timeOffset = (Math.abs(new Date().getTimezoneOffset()/60)).toString();

                let fullNumber = timeOffset.padStart(2, '0');
                fullNumber = fullNumber.padEnd(4, '0');

                let sign = (new Date().getTimezoneOffset() >= 0) ? "-": "+";

                return sign + fullNumber;
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
                // Ensures the start and end date fields are not wiped after an error occurs.
                this.splitDates();
                const error = new Error(`HTTP Error ${response.statusText}`);
                error.status = response.statusText;
                error.response = response;
                console.log(error);

                this.errorMessage = "";
                response.clone().text().then(text => {
                    text = JSON.parse(text);
                    let result = [];
                    for (let i = 0; i < text.length; i++) {
                        if (!response.ok) {
                            console.log(text);
                            result.push(text[i].message);
                        }
                        else {
                            result.push(text[i]);
                        }
                    }
                    this.errorMessage = result;

                    this.showError = true;
                });
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

