<template>

    <div>


        <h1 class="page-title">{{ heading }} a Objective!</h1>

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
                                    label="Objective Riddle:"
                                    label-for="treasure_hunt_riddle">
                                <b-form-textarea :type="'expandable-text'"
                                                 id="treasure_hunt_riddle"
                                                 trim
                                                 v-model="inputObjective.riddle"
                                                 :state="validateRiddle"></b-form-textarea>
                            </b-form-group>
                        </b-container>


                        <b-form>
                            <b-container fluid>
                                <h6 class="mb-1">Selected Destination:</h6>
                                <b-list-group @click="$emit('destination-select')">
                                    <b-list-group-item href="#" class="flex-column align-items-start"
                                                       v-if="selectedDestination"
                                                       id="selectedDestination"
                                                       :disabled="selectedDestination.length === '{}'"
                                                       :variant="checkDestinationState"
                                                       draggable="false">
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
                                <b-form-group
                                        id="radius-field"
                                        label="Selected Destination check in radius:"
                                        label-for="radius">
                                    <!--Dropdown field for destination check in values-->
                                    <b-form-select id="radius" trim v-model="inputObjective.radius">
                                        <option :value="radius" v-for="radius in radiusList"
                                                :state="validateCheckIn">
                                            {{radius.text}}
                                        </option>
                                    </b-form-select>
                                </b-form-group>

                                <div ref="map" v-if="inputObjective.radius !== null && selectedDestination.name">
                                    <google-map ref="map"
                                                :showRadius="true"
                                                :radius="inputObjective.radius.value"
                                                :selectedRadiusDestination="selectedDestination">
                                    </google-map>
                                </div>
                            </b-container>
                        </b-form>
                    </b-form>

                <b-row>
                    <b-col cols="8">
                        <b-button @click="validateObjective" block variant="primary">
                            {{ heading }}
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
    import GoogleMap from "../map/googleMap";

    export default {
        name: "addObjective",

        components: {
            BCol,
            DestinationSidebar,
            GoogleMap
        },

        props: {
            profile: Object,
            inputObjective: {
                default: function () {
                    return {
                        id: null,
                        destination: null,
                        riddle: "",
                        radius: null
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
                savingObjective: false,
                letObjectiveSaved: false,
                radiusList: [
                    {value: 0.005, text: "5 Meters"},
                    {value: 0.01, text: "10 Meters"},
                    {value: 0.02, text: "20 Meters"},
                    {value: 0.05, text: "50 Meters"},
                    {value: 0.1, text: "100 Meters"},
                    {value: 0.5, text: "500 Meters"},
                    {value: 1, text: "1 Km"},
                    {value: 5, text: "5 Km"},
                    {value: 10, text: "10 Km"},
                ],
                initial: {
                    zoom: 5,
                    view: {
                        lat: -41.272804,
                        lng: 173.299565
                    }
                },
            }
        },

        watch: {
            selectedDestination() {
                this.inputObjective.destination = this.selectedDestination;
            },

            inputObjective() {
                this.selectedDestination = this.inputObjective.destination;
                if(this.inputObjective.radius === undefined) {
                    this.inputObjective.radius = {value:0.005, text:"5 Meters"}
                }
            }
        },

        mounted() {
            // this.splitDates();
            this.editingObjective();
            // this.setDateTimeString();
            this.initMap();
        },

        computed: {
            // /**
            //  * Checks that the start date is not after the end date, and is not before the current date for new hunts.
            //  * @returns true if start date is valid.
            //  */
            // validateStartDate() {
            //     if ((this.inputObjective.startDate < this.getDateString() && !this.inputObjective.id)) {
            //         return false;
            //     }
            //     if (this.inputObjective.startDate > this.inputObjective.endDate) {
            //         return false;
            //     }
            //     return true;
            // },
            //
            //
            // /**
            //  * Checks that the start time is not after or the same as the end time if the dates are the same,
            //  * and that the start time is not before the current time if the current date is today.
            //  * @returns true if start time is valid.
            //  */
            // validateStartTime() {
            //     if (this.inputObjective.startDate === this.inputObjective.endDate) {
            //         if (this.inputObjective.startTime >= this.inputObjective.endTime) {
            //             return false;
            //         }
            //     }
            //     if (this.inputObjective.startDate === this.getDateString() && !this.inputObjective.id) {
            //         if (this.inputObjective.startTime < this.getTimeString()) {
            //             return false;
            //         }
            //     }
            //     return true;
            // },
            //
            //
            // /**
            //  * Checks that the end date is not before the start date, and is not before the current date for new hunts.
            //  * @returns true if end date is valid.
            //  */
            // validateEndDate() {
            //     if (this.inputObjective.endDate < this.getDateString() && !this.inputObjective.id) {
            //         return false;
            //     }
            //     if (this.inputObjective.endDate < this.inputObjective.startDate) {
            //         return false;
            //     }
            //     return true;
            // },
            //
            //
            // /**
            //  * Checks that the end time is not before or the same as the start time if the dates are the same.
            //  * @returns true if end time is valid.
            //  */
            // validateEndTime() {
            //     if (this.inputObjective.startDate === this.inputObjective.endDate) {
            //         if (this.inputObjective.endTime <= this.inputObjective.startTime) {
            //             return false;
            //         }
            //     }
            //     return true;
            // },


            /**
             * Returns true if the inputted riddle has length greater than 0.
             * @returns true if validated.
             */
            validateRiddle() {
              if(this.inputObjective.riddle.length > 0){
                  return true;
              }
              return null;
            },

            /**
             * Returns true if the user has selected a check in radius
             * @returns true if validated.
             */
            validateCheckIn() {
                if (this.inputObjective.radius === null) {
                    return false;
                }
                return this.inputObjective.radius.length > 0 || this.inputObjective.radius !== null;
            },


            /**
             * Returns true if the input destination exists and matches the one selected in the sidebar and isn't empty.
             * @returns true if valid.
             */
            validateDestination() {
                if (this.inputObjective.destination !== null
                    && this.inputObjective.destination === this.selectedDestination
                    && this.inputObjective.destination.name !== undefined
                    && this.inputObjective.destination.name.length > 0
                    || this.inputObjective.destination !== null
                    && this.inputObjective.destination.id === this.selectedDestination.id) {
                    return true;
                }
                return false;
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

            // /**
            //  * Gets the current date+time as a Date object.
            //  * @returns Current Datetime.
            //  */
            // getCurrentDate() {
            //     return new Date();
            // },
            //
            //
            // /**
            //  * sets the input values to be their proper string versions of current date/time.
            //  */
            // setDateTimeString() {
            //     if (this.inputObjective.id === null) {
            //         this.inputObjective.startDate = this.getDateString();
            //         this.inputObjective.endDate = this.getDateString();
            //         this.inputObjective.startTime = this.getTimeString();
            //     }
            // },
            //
            //
            // /**
            //  * Gets the current date as a string in YYYY-MM-DD format, including padding O's on month/day.
            //  * @returns Current Date in YYYY-MM-DD String Format.
            //  */
            // getDateString() {
            //     let today = this.getCurrentDate();
            //     let date =  today.getFullYear()+'-'+
            //         ((today.getMonth()+1) < 10 ? "0" : "")
            //         + (today.getMonth()+1)+'-'+
            //         (today.getDate() < 10 ? "0" : "") +
            //         today.getDate();
            //     return date;
            // },

            //
            // /**
            //  * Gets the current time as a string in HH:MM format, including padding O's.
            //  * @returns Current Time in HH:MM String Format.
            //  */
            // getTimeString() {
            //     let today = this.getCurrentDate();
            //     return ((today.getHours()) < 10 ? "0" : "") +
            //         today.getHours() + ":"
            //         + ((today.getMinutes()) < 10 ? "0" : "") +
            //         today.getMinutes();
            // },


            /**
             * Fills the destination with the existing destination of a hunt when editing it.
             */
            editingObjective() {
                if (this.inputObjective.id !== null) {
                    this.selectedDestination = this.inputObjective.destination;
                }
            },


            /**
             * If all field validations pass on the active objective, saves the objective using either
             * updateHunt if there is an active editing ID or saveHunt otherwise (adding a new one).
             */
            validateObjective() {
                if (this.validateDestination && this.validateRiddle && this.validateCheckIn) {
                    if (this.heading === "Add") {
                        this.addHunt();
                    } else if (this.heading === "Edit") {
                        this.editHunt();
                    } else {
                        if (this.inputObjective.id !== null) {
                            this.updateHunt();
                        } else {
                            this.saveHunt();
                        }
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
             * When used in quests for adding a objective to the quests list by emitting it outside of the
             * component.
             */
            addHunt() {
                this.inputObjective.radius = this.inputObjective.radius.value;
                delete this.inputObjective.startTime;
                delete this.inputObjective.endTime;
                delete this.inputObjective.startDate;
                delete this.inputObjective.endDate;
                this.$emit('addObjective', this.inputObjective);
                this.$emit('cancelCreate')
            },


            /**
             * When used in quests for editing a objective to the quests list by emitting it outside of the
             * component.
             */
            editHunt() {
                this.inputObjective.radius = this.inputObjective.radius.value;
                delete this.inputObjective.startTime;
                delete this.inputObjective.endTime;
                delete this.inputObjective.startDate;
                delete this.inputObjective.endDate;
                this.$emit('editObjective', this.inputObjective);
                this.$emit('cancelCreate')
            },


            /**
             * Creates formatted JSON of the currently active objective.
             * @returns JSON string with fields 'riddle', 'destination_id', 'start_date', 'end_date'.
             */
            assembleObjective() {
                this.joinDates();
                this.inputObjective.destination = {"id": this.inputObjective.destination.id};

                delete this.inputObjective.startTime;
                delete this.inputObjective.endTime;
            },


            /**
             * POST's the currently active destination to the objectives endpoint in JSON format, for newly creating
             * destinations.
             */
            saveHunt() {
                this.assembleObjective();
                let self = this;
                fetch('/v1/objectives/' + this.profile.id, {
                    method: 'POST',
                    headers: {'content-type': 'application/json'},
                    body: JSON.stringify(this.inputObjective)
                })
                    .then(this.checkStatus)
                    .then(function() {
                        self.$emit('successCreate', "Objective Successfully Created");
                        self.$emit('cancelCreate')
                    })
            },


            /**
             * PUT's the currently active destination to the objectives endpoint in JSON format, for edited
             * destinations.
             */
            updateHunt() {
                this.assembleObjective();
                let self = this;
                fetch('/v1/objectives/' + this.inputObjective.id, {
                    method: 'PUT',
                    headers: {'content-type': 'application/json'},
                    body: JSON.stringify(this.inputObjective)
                })
                    .then(this.checkStatus)
                    .then(function() {
                        self.$emit('cancelCreate')
                    })
            },


            /**
             * Cancels the creation or editing of a objective by emitting a value to the objectiveList.
             */
            cancelCreate() {
                this.$emit('cancelCreate');
            },


            // /**
            //  * Splits the dates of the inputObjective to put in the edit fields.
            //  */
            // splitDates() {
            //     if (this.inputObjective.id !== null) {
            //         this.inputObjective.startDate = new Date(this.inputObjective.startDate).toLocaleString();
            //         let startDate = this.inputObjective.startDate;
            //         this.inputObjective.startDate = this.inputObjective.startDate.split(", ")[0];
            //         this.inputObjective.startDate = this.inputObjective.startDate.split("/").reverse().join("-");
            //         this.inputObjective.startTime = startDate.split(" ")[1];
            //         this.inputObjective.startTime = this.inputObjective.startTime.split("+")[0];
            //         this.inputObjective.startTime = this.inputObjective.startTime.split("-")[0];
            //
            //         this.inputObjective.endDate = new Date(this.inputObjective.endDate).toLocaleString();
            //         let endDate = this.inputObjective.endDate;
            //         this.inputObjective.endDate = this.inputObjective.endDate.split(", ")[0];
            //         this.inputObjective.endDate = this.inputObjective.endDate.split("/").reverse().join("-");
            //         this.inputObjective.endTime = endDate.split(" ")[1];
            //         this.inputObjective.endTime = this.inputObjective.endTime.split("+")[0];
            //         this.inputObjective.endTime = this.inputObjective.endTime.split("-")[0];
            //     }
            // },


            // /**
            //  * Combines dates and times together from input fields and adds :00 on the end for seconds.
            //  */
            // joinDates() {
            //     let timeOffset = this.formatOffset();
            //
            //     if(this.inputObjective.startTime.length === 5) {
            //         this.inputObjective.startTime += ":00";
            //     }
            //
            //     if(this.inputObjective.endTime.length === 5) {
            //         this.inputObjective.endTime += ":00";
            //     }
            //
            //     this.inputObjective.startDate = this.inputObjective.startDate + " "
            //         + this.inputObjective.startTime + timeOffset;
            //
            //     this.inputObjective.endDate = this.inputObjective.endDate + " "
            //         + this.inputObjective.endTime + timeOffset;
            //
            // },


            // /**
            //  * Gets the local time offset and pads it to be 4 numbers long.
            //  */
            // formatOffset() {
            //     let timeOffset = (Math.abs(new Date().getTimezoneOffset()/60)).toString();
            //
            //     let fullNumber = timeOffset.padStart(2, '0');
            //     fullNumber = fullNumber.padEnd(4, '0');
            //
            //     let sign = (new Date().getTimezoneOffset() >= 0) ? "-": "+";
            //
            //     return sign + fullNumber;
            // },


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
